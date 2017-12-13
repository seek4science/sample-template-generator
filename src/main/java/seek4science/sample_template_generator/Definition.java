package seek4science.sample_template_generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Definition {

	private int sheetIndex;
	private String sheetName;
	private DefinitionColumn[] columns;
	private String baseTemplatePath;

	public Definition(String sheetName, int sheetIndex, DefinitionColumn[] columns, String baseTemplatePath) {
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.columns = columns;
		this.baseTemplatePath = baseTemplatePath;
	}

	public String getSheetName() {
		return sheetName;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public DefinitionColumn[] getColumns() {
		return columns;
	}

	public String getBaseTemplatePath() {
		return baseTemplatePath;
	}

	public InputStream getBaseTemplateStream() throws FileNotFoundException {
		return new FileInputStream(getBaseTemplatePath());
	}

}
