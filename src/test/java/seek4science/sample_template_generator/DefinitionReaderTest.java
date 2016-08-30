package seek4science.sample_template_generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seek4science.sample_template_generator.testfiles.FileReader;

public class DefinitionReaderTest  {
    
	@Test
	public void testReadingJSON() throws Exception {
		String json = FileReader.getContents("simple-test.json");
		Definition def = DefinitionReader.read(json);
		assertEquals("samples", def.getSheetName());
		assertEquals(1,def.getSheetIndex());
		assertEquals(2,def.getColumns().length);
		DefinitionColumn col1 = def.getColumns()[0];
		DefinitionColumn col2 = def.getColumns()[1];
		
		assertEquals("first", col1.getName());
		assertEquals(0, col1.getIndex());
		
		assertEquals("second", col2.getName());
		assertEquals(1, col2.getIndex());
	}
}
