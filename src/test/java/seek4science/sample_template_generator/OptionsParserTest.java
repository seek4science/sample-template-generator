package seek4science.sample_template_generator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.UUID;

import org.junit.Test;

import seek4science.sample_template_generator.testfiles.FileReader;

public class OptionsParserTest {

	@Test
	public void testParse() throws Exception {
		String filepath = tmpFilePath();
		String json = FileReader.getContents("simple-test.json");
		String[] args = new String[] { "-f", filepath, "-j", json };
		OptionsParser parser = new OptionsParser(args);
		assertEquals(filepath, parser.getDestinationFile().getAbsolutePath());
		assertEquals(json, parser.getJson());
	}

	@Test(expected = Exception.class)
	public void testBadOption() throws Exception {
		String[] args = new String[] { "-f", tmpFilePath(), "-p", "a word" };
		new OptionsParser(args);
	}

	@Test(expected = Exception.class)
	public void testMissingOption() throws Exception {
		String[] args = new String[] { "-f", tmpFilePath() };
		new OptionsParser(args);
	}

	@Test(expected = Exception.class)
	public void testMissingOption2() throws Exception {
		String json = FileReader.getContents("simple-test.json");
		String[] args = new String[] { "-j", json };
		new OptionsParser(args);
	}

	private String tmpFilePath() {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		String uuid = UUID.randomUUID().toString();
		File file = new File(tmpDir, uuid + ".xlsx");
		return file.getPath();
	}
}
