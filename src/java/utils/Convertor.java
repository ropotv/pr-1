package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Convertor {
    public static void convertData(ArrayList<String> data, String type, String value) {
        switch (type) {
            case "application/xml":
                data.add(Convertor.convertXMLtoJSON(value));
                break;
            case "text/csv":
                data.add(Convertor.convertCSVtoJSON(value));
                break;
            case "application/x-yaml":
                data.add(Convertor.convertYamlToJson(value));
                break;
            case "json":
                data.add(value);
                break;
        }
    }

    private static String convertXMLtoJSON(String xml) {
        int INDENTATION = 4;
        try {
            JSONObject jsonObj = XML.toJSONObject(xml);
            return jsonObj.toString(INDENTATION);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertCSVtoJSON(String csv) {
        try {
            CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
            CsvMapper csvMapper = new CsvMapper();
            List<Object> readAll;
            readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(csv).readAll();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String convertYamlToJson(String yamlString) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            yamlReader.findAndRegisterModules();
            Object obj = yamlReader.readValue(yamlString, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();

            return jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
