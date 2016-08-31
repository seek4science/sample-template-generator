package seek4science.sample_template_generator;

public class SampleTemplateGeneratorEntry {
	
	public static void main (String [] args) throws Exception {
		OptionsParser parser = new OptionsParser(args);
		Definition definition = DefinitionReader.read(parser.getJson());
		TemplateGenerator.generate(definition, parser.getDestinationFile());		
	}
}
