package seek4science.sample_template_generator;

public class Definition {

	private int sheetIndex;
	private String sheetName;
	private DefinitionColumn[] columns;

	public Definition(String sheetName, int sheetIndex, DefinitionColumn[] columns) {
		this.sheetName = sheetName;
		this.sheetIndex = sheetIndex;
		this.columns = columns;
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

}
