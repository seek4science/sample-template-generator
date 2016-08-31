package seek4science.sample_template_generator;

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
	
	

}
