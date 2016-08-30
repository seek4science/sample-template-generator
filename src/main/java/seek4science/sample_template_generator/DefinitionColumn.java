package seek4science.sample_template_generator;

public class DefinitionColumn {

	private String name;

	private int index;

	public DefinitionColumn(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

}
