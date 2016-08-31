package seek4science.sample_template_generator;

import java.io.File;

public class OptionsParser {
	private String json = null;
	private File destinationFile = null;

	public String getJson() {
		return json;
	}

	public File getDestinationFile() {
		return destinationFile;
	}

	public OptionsParser(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("-f")) {
				i++;
				this.destinationFile = new File(args[i]);
			} else if (arg.equals("-j")) {
				i++;
				this.json = args[i];
			} else {
				throw new Exception("Unrecognised option: " + args[i]);
			}
		}
		if (this.json == null) {
			throw new Exception("JSON not set with -j option");
		}

		if (this.destinationFile == null) {
			throw new Exception("destination file not set with -f option");
		}
	}
}
