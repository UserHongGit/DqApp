package com.hong.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParser {
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONArray words = new JSONObject(new JSONTokener(json)).getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                ret.append(words.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String parseGrammarResult(String json) {
        String str = "w";
        StringBuffer ret = new StringBuffer();
        try {
            JSONArray words = new JSONObject(new JSONTokener(json)).getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString(str).contains("nomatch")) {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("【结果】");
                    stringBuilder.append(obj.getString(str));
                    ret.append(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("【置信度】 ");
                    stringBuilder.append(obj.getInt("sc"));
                    ret.append(stringBuilder.toString());
                    ret.append("\n ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append(" 没有匹配结果 .");
        }
        return ret.toString();
    }

    public static String parseLocalGrammarResult(String json) {
        String str = "w";
        StringBuffer ret = new StringBuffer();
        try {
            JSONObject joResult = new JSONObject(new JSONTokener(json));
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString(str).contains("nomatch")) {
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("【结果】");
                    stringBuilder.append(obj.getString(str));
                    ret.append(stringBuilder.toString());
                    ret.append("\n ");
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("【置信度】 ");
            stringBuilder2.append(joResult.optInt("sc"));
            ret.append(stringBuilder2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ret.append(" 没有匹配结果 .");
        }
        return ret.toString();
    }
}
