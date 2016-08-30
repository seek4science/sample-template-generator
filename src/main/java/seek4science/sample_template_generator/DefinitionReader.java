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

		JSONObject jsonObj = parseJSONObject(json);
		return new Definition(getSheetName(jsonObj), getSheetIndex(jsonObj), getColumns(jsonObj));

	}

	private static int getSheetIndex(JSONObject obj) {
		String strSheetIndex = obj.get("sheet_index").toString();
		return Integer.valueOf(strSheetIndex);
	}

	private static String getSheetName(JSONObject obj) {
		String sheetName = obj.get("sheet_name").toString();
		return sheetName;
	}

	private static JSONObject parseJSONObject(String json) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(json);
		return obj;
	}

	private static DefinitionColumn[] getColumns(JSONObject obj) {
		ArrayList<DefinitionColumn> result = new ArrayList<DefinitionColumn>();
		JSONArray columns = (JSONArray) obj.get("columns");
		int index = 0;
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = columns.iterator();
		while (iterator.hasNext()) {
			JSONObject columnObj = iterator.next();
			String columnName = (String) columnObj.keySet().toArray()[0];
			DefinitionColumn c = new DefinitionColumn(columnName, index++);
			result.add(c);
		}
		return result.toArray(new DefinitionColumn[0]);
	}

}
