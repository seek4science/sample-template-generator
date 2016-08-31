package seek4science.sample_template_generator;

public class DefinitionColumn {

	private String title;

	private int index;

	public DefinitionColumn(String name, int index) {
		this.title = name;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getColumn() {
		return title;
	}

}
