package seek4science.sample_template_generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import seek4science.sample_template_generator.testfiles.FileReader;

public class TemplateGeneratorTest {

	@Test
	public void simpleGenerateWithNoColumns() throws Exception {
		Definition def = new Definition("samples test", 1, new DefinitionColumn[0], null);
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		assert (book instanceof XSSFWorkbook);
		assertEquals(2, book.getNumberOfSheets());
		assertEquals("samples test", book.getSheetAt(1).getSheetName());

		def = new Definition("samples test", 0, new DefinitionColumn[0], null);
		book = TemplateGenerator.generate(def);
		assertEquals(1, book.getNumberOfSheets());
		assertEquals("samples test", book.getSheetAt(0).getSheetName());

		def = new Definition("samples test sheet", 5, new DefinitionColumn[0], null);
		book = TemplateGenerator.generate(def);
		assertEquals(6, book.getNumberOfSheets());
		assertEquals("samples test sheet", book.getSheetAt(5).getSheetName());
	}

	@Test
	public void simpleGenerateWithFewColumns() throws Exception {
		DefinitionColumn column1 = new DefinitionColumn("aaa", new String[] {}, 0);
		DefinitionColumn column2 = new DefinitionColumn("bbb", new String[] {}, 1);
		DefinitionColumn[] columns = new DefinitionColumn[] { column1, column2 };
		Definition def = new Definition("samples", 0, columns, null);
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		Sheet sheet = book.getSheet("samples");
		assertNotNull(sheet);

		assertEquals(0, sheet.getFirstRowNum());
		Row row = sheet.getRow(0);
		assertEquals(0, row.getFirstCellNum());
		assertEquals("aaa", row.getCell(0).getStringCellValue());
		assertEquals("bbb", row.getCell(1).getStringCellValue());

		// check it uses the index
		column1 = new DefinitionColumn("aaa", new String[] {}, 1);
		column2 = new DefinitionColumn("bbb", new String[] {}, 0);
		columns = new DefinitionColumn[] { column1, column2 };
		def = new Definition("samples", 0, columns, null);
		book = TemplateGenerator.generate(def);
		assertNotNull(book);
		sheet = book.getSheet("samples");
		assertNotNull(sheet);

		assertEquals(0, sheet.getFirstRowNum());
		row = sheet.getRow(0);
		assertEquals(0, row.getFirstCellNum());
		assertEquals("bbb", row.getCell(0).getStringCellValue());
		assertEquals("aaa", row.getCell(1).getStringCellValue());
	}

	@Test
	public void generateToFile() throws Exception {
		Definition def = new Definition("samples test", 1, new DefinitionColumn[0], null);
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		String uuid = UUID.randomUUID().toString();
		File saveFile = new File(tmpDir, uuid + ".xlsx");
		TemplateGenerator.generate(def, saveFile);

		Workbook book = WorkbookFactory.create(saveFile);
		assert (book instanceof XSSFWorkbook);
		assertEquals(2, book.getNumberOfSheets());
		assertEquals("samples test", book.getSheetAt(1).getSheetName());

	}

	@Test
	public void simpleGenerateWithValues() throws Exception {
		DefinitionColumn column1 = new DefinitionColumn("colours", new String[] { "red", "green", "blue" }, 0);
		DefinitionColumn column2 = new DefinitionColumn("gender", new String[] { "male", "female" }, 1);
		DefinitionColumn[] columns = new DefinitionColumn[] { column1, column2 };
		Definition def = new Definition("samples", 0, columns, null);
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		Sheet sheet = book.getSheet("samples");
		assertNotNull(sheet);
		assertEquals(0, sheet.getFirstRowNum());
		Row row = sheet.getRow(0);
		assertEquals(0, row.getFirstCellNum());
		assertEquals("colours", row.getCell(0).getStringCellValue());
		assertEquals("gender", row.getCell(1).getStringCellValue());

	}

	@Test
	public void generateFromJSONWithQuotes() throws Exception {
		String json = FileReader.getContents("quote-test-with-cv.json");
		Definition def = new DefinitionReader(json).read();
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		Sheet sheet = book.getSheet("samples");
		assertNotNull(sheet);
		assertEquals(0, sheet.getFirstRowNum());
		Row row = sheet.getRow(0);
		assertEquals(0, row.getFirstCellNum());
		assertEquals("quote's", row.getCell(0).getStringCellValue());
	}

	@Test
	public void generateFromBaseTemplate() throws Exception {
		String path = getTestBaseTemplatePath();
		Workbook originalWorkbook = WorkbookFactory.create(new FileInputStream(path));
		assertEquals(18, originalWorkbook.getNumberOfSheets()); // a lot are
																// hidden sheets
																// from
																// RightField
		assertEquals("Metadata", originalWorkbook.getSheetAt(0).getSheetName());
		assertNull(originalWorkbook.getSheet("samples test"));
		Definition def = new Definition("samples test", 18, new DefinitionColumn[0], path);
		Workbook book = TemplateGenerator.generate(def);
		assertEquals(19, book.getNumberOfSheets());
		assertEquals("Metadata", book.getSheetAt(0).getSheetName());
		assertNotNull(book.getSheet("samples test"));
		assertEquals("samples test", book.getSheetAt(18).getSheetName());
	}

	@Test
	public void generateFromBaseTemplateInsertedSheet() throws Exception {
		String path = getTestBaseTemplatePath();

		Definition def = new Definition("samples test", 1, new DefinitionColumn[0], path);
		Workbook book = TemplateGenerator.generate(def);
		assertEquals(19, book.getNumberOfSheets());
		assertEquals("Metadata", book.getSheetAt(0).getSheetName());
		assertNotNull(book.getSheet("samples test"));
		assertEquals("samples test", book.getSheetAt(1).getSheetName());

		def = new Definition("samples test", 2, new DefinitionColumn[0], path);
		book = TemplateGenerator.generate(def);
		assertEquals(19, book.getNumberOfSheets());
		assertEquals("Metadata", book.getSheetAt(0).getSheetName());
		assertNotNull(book.getSheet("samples test"));
		assertEquals("samples test", book.getSheetAt(2).getSheetName());
	}

	@Test(expected = DuplicateSheetException.class)
	public void duplicateSheetExceptionThrown() throws Exception {
		Definition def = new Definition("Metadata", 1, new DefinitionColumn[0], getTestBaseTemplatePath());
		Workbook book = TemplateGenerator.generate(def);
	}

	private String getTestBaseTemplatePath() throws Exception {
		return FileReader.class.getResource("test-base-template.xlsx").getPath();
	}

}
