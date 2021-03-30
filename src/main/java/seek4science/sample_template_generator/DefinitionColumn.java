package seek4science.sample_template_generator;

import java.util.Arrays;

public class DefinitionColumn {

	private String title;
	private String[] values;
	private int index;

	public DefinitionColumn(String name, String[] values, int index) {
		this.title = name;
		this.index = index;
		this.values = values;
	}

	public int getIndex() {
		return index;
	}

	public String getColumn() {
		return title;
	}

	public String[] getValues() {
		return values;
	}
	
	public String[] getValuesSorted() {
		String [] copy = values.clone();
		Arrays.sort(copy);
		return copy;
	}

}
