package seek4science.sample_template_generator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import static org.junit.Assert.*;

public class TemplateGeneratorTest {
	
	@Test
	public void simpleGenerateWithNoColumns() throws Exception {
		Definition def = new Definition("samples test", 1, new DefinitionColumn[0]);
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		assert(book instanceof XSSFWorkbook);
		assertEquals(2,book.getNumberOfSheets());
		assertEquals("samples test",book.getSheetAt(1).getSheetName());	
		
		def = new Definition("samples test", 0, new DefinitionColumn[0]);
		book = TemplateGenerator.generate(def);		
		assertEquals(1,book.getNumberOfSheets());
		assertEquals("samples test",book.getSheetAt(0).getSheetName());
		
		def = new Definition("samples test sheet", 5, new DefinitionColumn[0]);
		book = TemplateGenerator.generate(def);		
		assertEquals(6,book.getNumberOfSheets());
		assertEquals("samples test sheet",book.getSheetAt(5).getSheetName());
	}
	
	@Test
	public void simpleGenerateWithFewColumns() throws Exception {
		DefinitionColumn column1=new DefinitionColumn("aaa",0);
		DefinitionColumn column2=new DefinitionColumn("bbb",1);
		DefinitionColumn[] columns = new DefinitionColumn[]{column1,column2};
		Definition def = new Definition("samples", 0, columns);
		Workbook book = TemplateGenerator.generate(def);
		assertNotNull(book);
		Sheet sheet = book.getSheet("samples");
		assertNotNull(sheet);
		
		assertEquals(0,sheet.getFirstRowNum());
		Row row = sheet.getRow(0);
		assertEquals(0,row.getFirstCellNum());		
		assertEquals("aaa",row.getCell(0).getStringCellValue());
		assertEquals("bbb",row.getCell(1).getStringCellValue());
		
		//check it uses the index
		column1=new DefinitionColumn("aaa",1);
		column2=new DefinitionColumn("bbb",0);
		columns = new DefinitionColumn[]{column1,column2};
		def = new Definition("samples", 0, columns);
		book = TemplateGenerator.generate(def);
		assertNotNull(book);
		sheet = book.getSheet("samples");
		assertNotNull(sheet);
		
		assertEquals(0,sheet.getFirstRowNum());
		row = sheet.getRow(0);
		assertEquals(0,row.getFirstCellNum());		
		assertEquals("bbb",row.getCell(0).getStringCellValue());
		assertEquals("aaa",row.getCell(1).getStringCellValue());
	}
	
	

}
