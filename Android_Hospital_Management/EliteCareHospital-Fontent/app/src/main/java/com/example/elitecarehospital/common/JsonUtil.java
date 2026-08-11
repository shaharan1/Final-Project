package com.example.elitecarehospital.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    public static String pretty(String key) {
        if (key == null || key.isEmpty()) return key;
        StringBuilder sb = new StringBuilder();
        boolean cap = true;
        for (char c : key.toCharArray()) {
            if (c == '_' || c == '.') {
                sb.append(' ');
                cap = true;
            } else {
                sb.append(cap ? Character.toUpperCase(c) : c);
                cap = false;
            }
        }
        return sb.toString();
    }

    public static String valueToString(JsonElement el) {
        if (el == null || el.isJsonNull()) return null;
        if (el.isJsonPrimitive()) {
            JsonPrimitive p = el.getAsJsonPrimitive();
            if (p.isBoolean()) return p.getAsBoolean() ? "Yes" : "No";
            return p.getAsString();
        }
        if (el.isJsonArray()) {
            JsonArray arr = el.getAsJsonArray();
            if (arr.size() == 0) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                if (i > 0) sb.append(", ");
                JsonElement item = arr.get(i);
                if (item.isJsonPrimitive()) {
                    sb.append(valueToString(item));
                } else {
                    sb.append(item.toString());
                }
                if (sb.length() > 200) {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }
        return el.toString();
    }

    public static Map<String, String> flatten(JsonObject obj) {
        Map<String, String> map = new LinkedHashMap<>();
        flattenRecursive(obj, "", map);
        return map;
    }

    private static void flattenRecursive(JsonObject obj, String prefix, Map<String, String> out) {
        for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
            String key = e.getKey();
            JsonElement val = e.getValue();
            String path = prefix.isEmpty() ? key : prefix + "." + key;

            if (val != null && val.isJsonObject()) {
                flattenRecursive(val.getAsJsonObject(), path, out);
            } else if (val != null && val.isJsonArray()) {
                JsonArray arr = val.getAsJsonArray();
                if (arr.size() == 0) continue;
                if (arr.get(0) != null && arr.get(0).isJsonObject()) {
                    out.put(pretty(path), "[" + arr.size() + " items]");
                } else {
                    String s = valueToString(arr);
                    if (s != null) out.put(pretty(path), s);
                }
            } else {
                String s = valueToString(val);
                if (s != null && !s.isEmpty()) out.put(pretty(path), s);
            }
        }
    }

    public static List<String> getFields(JsonObject obj, String jsonFieldList) {
        List<String> fields = new ArrayList<>();
        if (jsonFieldList == null || jsonFieldList.isEmpty()) return fields;
        try {
            JsonElement el = com.google.gson.JsonParser.parseString(jsonFieldList);
            if (el.isJsonArray()) {
                for (JsonElement item : el.getAsJsonArray()) {
                    fields.add(item.getAsString());
                }
            } else {
                fields.add(el.getAsString());
            }
        } catch (Exception ignored) {
        }
        return fields;
    }

    public static String joinValues(JsonObject obj, List<String> fields, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String f : fields) {
            JsonElement el = obj.get(f);
            String s = valueToString(el);
            if (s != null && !s.isEmpty() && !s.equals("null")) {
                if (sb.length() > 0) sb.append(separator);
                sb.append(s);
            }
        }
        return sb.toString();
    }
}
