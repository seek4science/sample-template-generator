package seek4science.sample_template_generator;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TemplateGenerator {	
	
	private Definition definition;

	public static Workbook generate(Definition def) throws Exception {
		return new TemplateGenerator(def).generate();
	}
	
	public TemplateGenerator(Definition definition) throws Exception {
		this.definition = definition;	
	}
	
	public Workbook generate() throws Exception {
		Workbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = null;
		for (int i=0;i<definition.getSheetIndex();i++) {
			sheet = (XSSFSheet) workbook.createSheet();
		}	
		sheet = (XSSFSheet) workbook.createSheet(definition.getSheetName());
		
        return workbook; 
	}

}
