package seek4science.sample_template_generator.testfiles;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class FileReader {

	public static InputStream getStream(String name) throws IOException {
		return FileReader.class.getResource(name).openStream();
	}
	
	public static String getContents(String name) throws IOException {
		return IOUtils.toString(getStream(name),StandardCharsets.UTF_8.toString());
	}
}
