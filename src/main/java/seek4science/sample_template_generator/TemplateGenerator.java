package seek4science.sample_template_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TemplateGenerator {

	private Definition definition;

	public static Workbook generate(Definition def) throws Exception {
		return new TemplateGenerator(def).generate();
	}

	public static Workbook generate(Definition def, File destinationFile) throws Exception {
		return new TemplateGenerator(def).generate(destinationFile);
	}

	public TemplateGenerator(Definition definition) throws Exception {
		this.definition = definition;
	}

	public Workbook generate() throws Exception {
		Workbook workbook = initialiseWorkbook();

		XSSFSheet sheet = null;
		if (workbook.getNumberOfSheets() <= definition.getSheetIndex()) {
			for (int i = workbook.getNumberOfSheets(); i < definition.getSheetIndex(); i++) {
				sheet = (XSSFSheet) workbook.createSheet();
			}
		}

		if (workbook.getSheet(definition.getSheetName()) != null) {
			throw new DuplicateSheetException("The sheet name " + definition.getSheetName() + " already exists");
		}

		sheet = (XSSFSheet) workbook.createSheet(definition.getSheetName());
		workbook.setSheetOrder(definition.getSheetName(), definition.getSheetIndex());

		Row row = sheet.createRow(0);
		for (DefinitionColumn columnDefinition : definition.getColumns()) {
			Cell cell = row.createCell(columnDefinition.getIndex());
			cell.setCellValue(columnDefinition.getColumn());
			if (columnDefinition.getValues().length > 0) {
				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
				XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
						.createExplicitListConstraint(columnDefinition.getValues());
				CellRangeAddressList addressList = new CellRangeAddressList(1, 1, columnDefinition.getIndex(),
						columnDefinition.getIndex());
				XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint,
						addressList);
				validation.setShowErrorBox(true);
				sheet.addValidationData(validation);
			}
		}

		// set font to bold
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);

		font.setBold(true);
		style.setFont(font);
		row.setRowStyle(style);

		return workbook;
	}

	public Workbook generate(File destinationFile) throws Exception {
		Workbook book = generate();

		FileOutputStream fileOut = new FileOutputStream(destinationFile);
		book.write(fileOut);
		fileOut.close();

		return book;
	}

	private Workbook initialiseWorkbook()
			throws EncryptedDocumentException, InvalidFormatException, FileNotFoundException, IOException {
		if (definition.getBaseTemplatePath() == null) {
			return new XSSFWorkbook();
		} else {
			return WorkbookFactory.create(definition.getBaseTemplateStream());
		}
	}

}
