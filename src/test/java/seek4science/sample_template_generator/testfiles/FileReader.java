package seek4science.sample_template_generator.testfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class FileReader {

	public static InputStream getStream(String name) throws IOException {
		return FileReader.class.getResource(name).openStream();
	}

	@SuppressWarnings("resource")
	public static String getContents(String name) throws IOException {
		java.util.Scanner scanner = new Scanner(getStream(name)).useDelimiter("\\A");
	    return scanner.hasNext() ? scanner.next() : "";
	}
}
