package seek4science.sample_template_generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//reads the JSON definition

public class DefinitionReader {

	private static List<String> MANDATORY_KEYS = Arrays.asList("sheet_index", "sheet_name", "columns");
	private static List<String> OPTIONAL_KEYS = Arrays.asList("base_template_path");

	public static Definition read(String json) throws ParseException, MissingJSONKeyException, InvalidJSONKeyException {
		JSONObject jsonObj = parseJSONObject(json);
		checkKeys(jsonObj);
		return new Definition(getSheetName(jsonObj), getSheetIndex(jsonObj), getColumns(jsonObj),
				getBaseTemplatePath(jsonObj));
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
			String[] values = (String[]) ((JSONArray) columnObj.get(columnName)).toArray(new String[] {});
			DefinitionColumn c = new DefinitionColumn(columnName, values, index++);
			result.add(c);
		}
		return result.toArray(new DefinitionColumn[0]);
	}

	private static String getBaseTemplatePath(JSONObject obj) {
		if (obj.containsKey("base_template_path")) {
			return obj.get("base_template_path").toString();
		} else {
			return null;
		}

	}

	private static void checkKeys(JSONObject obj) throws MissingJSONKeyException, InvalidJSONKeyException {
		checkMissingKey(obj);
		checkInvalidKey(obj);
	}

	private static void checkMissingKey(JSONObject obj) throws MissingJSONKeyException {
		for (String key : MANDATORY_KEYS) {
			if (!obj.containsKey(key)) {
				throw new MissingJSONKeyException(key + " not found in JSON");
			}
		}
	}

	private static void checkInvalidKey(JSONObject obj) throws InvalidJSONKeyException {
		for (Object key : obj.keySet()) {
			if (!(MANDATORY_KEYS.contains(key) || OPTIONAL_KEYS.contains(key))) {
				throw new InvalidJSONKeyException(key + " not expected in JSON");
			}
		}
	}

}
