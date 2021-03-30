package seek4science.sample_template_generator;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFName;
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

		XSSFSheet cvValidationSheet = null;
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
			
			setColumnWidth(sheet,columnDefinition.getColumn(),cell.getColumnIndex());		
			
			setCellStyle(cell);
			
			if (columnDefinition.getValues().length > 0) {
				if (cvValidationSheet==null) {
					cvValidationSheet = createDropdownValidationSheet(workbook);
				}
				
				String rangeName = createNamedRange(workbook, cvValidationSheet, columnDefinition);					
				
				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
				XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
						.createFormulaListConstraint(rangeName);
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
				
		sheet.setDisplayGridlines(true);
		
		row.setRowStyle(style);

		return workbook;
	}

	private XSSFSheet createDropdownValidationSheet(Workbook workbook) {
		XSSFSheet cvValidationSheet=(XSSFSheet)workbook.createSheet("cv validations");
		int index=workbook.getSheetIndex(cvValidationSheet);
		workbook.setSheetVisibility(index, SheetVisibility.VERY_HIDDEN);
		return cvValidationSheet;
	}

	private String createNamedRange(Workbook workbook, XSSFSheet cvValidationSheet, DefinitionColumn columnDefinition) {
		Row validationRow = cvValidationSheet.createRow(columnDefinition.getIndex());
		String [] values = columnDefinition.getValuesSorted();
		for (int i = 0; i < values.length; i++) {
			Cell validationCell = validationRow.createCell(i);
			validationCell.setCellValue(values[i]);
		}
		String rangeName = "cvnamedrange" + columnDefinition.getIndex();
		Name name = workbook.createName();
		name.setNameName(rangeName);
		String formula = "'" + cvValidationSheet.getSheetName() + "'!";
		CellReference ref = new CellReference(columnDefinition.getIndex(), 0, true, true);
		formula += ref.formatAsString();
		ref = new CellReference(columnDefinition.getIndex(), columnDefinition.getValues().length - 1, true, true);
		formula += ":" + ref.formatAsString();
		name.setRefersToFormula(formula);
		return rangeName;
	}

	private void setCellStyle(Cell cell) {
		
		// make it light grey with a border
		CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();		
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		
		// bold text
		Font font = cell.getSheet().getWorkbook().getFontAt(cellStyle.getFontIndex());				
		font.setFontHeightInPoints((short) 10);		
		font.setBold(true);
		cellStyle.setFont(font);		
		
		cell.setCellStyle(cellStyle); 
		
	}

	private void setColumnWidth(XSSFSheet sheet, String text, int columnIndex) {
		sheet.setColumnWidth(columnIndex, (text.length()+2) * 256);		
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
