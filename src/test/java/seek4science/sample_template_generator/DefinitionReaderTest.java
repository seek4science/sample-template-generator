package seek4science.sample_template_generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import seek4science.sample_template_generator.testfiles.FileReader;

public class DefinitionReaderTest {

	@Test
	public void testReadingJSON() throws Exception {
		String json = FileReader.getContents("simple-test.json");
		Definition def = new DefinitionReader(json).read();
		assertEquals("samples", def.getSheetName());
		assertEquals(1, def.getSheetIndex());
		assertEquals(2, def.getColumns().length);
		DefinitionColumn col1 = def.getColumns()[0];
		DefinitionColumn col2 = def.getColumns()[1];

		assertEquals("first", col1.getColumn());
		assertEquals(0, col1.getIndex());
		assertEquals(0, col1.getValues().length);

		assertEquals("second", col2.getColumn());
		assertEquals(1, col2.getIndex());
		assertEquals(0, col2.getValues().length);

		assertNull(def.getBaseTemplatePath());
	}

	@Test
	public void testReadingJSONWithCV() throws Exception {
		String json = FileReader.getContents("simple-test-with-cv.json");
		Definition def = new DefinitionReader(json).read();
		assertEquals(1, def.getColumns().length);
		DefinitionColumn column = def.getColumns()[0];

		assertEquals("first", column.getColumn());
		assertEquals(0, column.getIndex());
		String[] values = column.getValues();
		assertEquals(4, values.length);
		assertEquals("a", values[0]);
		assertEquals("b", values[1]);
		assertEquals("c", values[2]);
		assertEquals("d", values[3]);

		assertNull(def.getBaseTemplatePath());
	}

	@Test
	public void testReadingJSONWithQuote() throws Exception {
		String json = FileReader.getContents("quote-test-with-cv.json");
		Definition def = new DefinitionReader(json).read();
		assertEquals(1, def.getColumns().length);
		DefinitionColumn column = def.getColumns()[0];

		assertEquals("quote's", column.getColumn());
		assertEquals(0, column.getIndex());
		String[] values = column.getValues();
		assertEquals(4, values.length);
		assertEquals("a's", values[0]);
		assertEquals("b\"c", values[1]);
		assertEquals("c]d", values[2]);
		assertEquals("d[e", values[3]);

		assertNull(def.getBaseTemplatePath());
	}

	@Test
	public void testReadingJSONWithComma() throws Exception {
		String json = FileReader.getContents("comma-test-with-cv.json");
		Definition def = new DefinitionReader(json).read();
		assertEquals(3, def.getColumns().length);
		DefinitionColumn column = def.getColumns()[0];

		assertEquals("colours", column.getColumn());
		assertEquals(0, column.getIndex());
		String[] values = column.getValues();
		assertEquals(3, values.length);
		assertEquals("red, green", values[0]);
		assertEquals("blue", values[1]);
		assertEquals("yellow,orange", values[2]);

		assertNull(def.getBaseTemplatePath());
	}

	@Test
	public void testReadingJSONWithBaseTemplatePath() throws Exception {
		String json = FileReader.getContents("includes-template-path.json");
		Definition def = new DefinitionReader(json).read();
		assertEquals("/home/fred/stuff/template.xlsx", def.getBaseTemplatePath());
	}

	@Test(expected = InvalidJSONKeyException.class)
	public void testInvalidJSONKey() throws Exception {
		String json = FileReader.getContents("invalid-key.json");
		new DefinitionReader(json).read();
	}

	@Test(expected = MissingJSONKeyException.class)
	public void testMissingSheetNameKey() throws Exception {
		String json = FileReader.getContents("missing-sheet-name-key.json");
		new DefinitionReader(json).read();
	}

	@Test(expected = MissingJSONKeyException.class)
	public void testMissingSheetIndexKey() throws Exception {
		String json = FileReader.getContents("missing-sheet-index-key.json");
		new DefinitionReader(json).read();
	}

	@Test(expected = MissingJSONKeyException.class)
	public void testMissingColumnsKey() throws Exception {
		String json = FileReader.getContents("missing-column-key.json");
		new DefinitionReader(json).read();
	}
}
