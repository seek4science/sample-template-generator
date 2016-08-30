package seek4science.sample_template_generator;


import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


//reads the JSON definition

public class DefinitionReader {
	
	public static Definition read(String json) throws ParseException {
		
		JSONObject obj = parseJSONObject(json);
		
		String sheetName=obj.get("sheet_name").toString();
		String strSheetIndex=obj.get("sheet_index").toString();
		int sheetIndex=Integer.valueOf(strSheetIndex);
		
		Definition def = new Definition(sheetName,sheetIndex,parseColumns(obj));
		
		return def;
		
	}

	private static JSONObject parseJSONObject(String json) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject)parser.parse(json);
		return obj;
	}
	
	private static DefinitionColumn[] parseColumns(JSONObject obj) {	
		ArrayList<DefinitionColumn> result = new ArrayList<DefinitionColumn>();
		JSONArray columns=(JSONArray)obj.get("columns");
		int index=0;
		Iterator<JSONObject> iterator= columns.iterator();
		while(iterator.hasNext()) {
			JSONObject columnObj = iterator.next();
			String columnName = (String)columnObj.keySet().toArray()[0];
			DefinitionColumn c = new DefinitionColumn(columnName, index++);
			result.add(c);
		}
		return  result.toArray(new DefinitionColumn[0]);
	}
	
}
