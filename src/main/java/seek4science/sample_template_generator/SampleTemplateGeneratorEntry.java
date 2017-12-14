package seek4science.sample_template_generator;

public class SampleTemplateGeneratorEntry {

	public static void main(String[] args) throws Exception {
		OptionsParser parser = new OptionsParser(args);
		Definition definition = new DefinitionReader(parser.getJson()).read();
		TemplateGenerator.generate(definition, parser.getDestinationFile());
	}
}
