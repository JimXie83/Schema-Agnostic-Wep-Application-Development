package com.jamesx.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**************************************************
 * By JamesXie 2016
 **************************************************/
public class GsonSqlDateDeSerializer implements JsonDeserializer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    @Override
    public java.sql.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)  {
        java.sql.Date newDate = null;
        String jsonStr = json.toString().replace("\"", ""); //remove "\" from date
        // if json is all numeric, assume long UTC date
        //else assume UTC date of format yyyy-MM-dd'T'HH:mm:ss.SSSX
        if (json.toString().matches("[-+]?\\d*\\.?\\d+")) {
            newDate = new java.sql.Date(json.getAsJsonPrimitive().getAsLong());
        } else  {
            try {
                Date d1= dateFormat.parse(json.toString().replace("\"", ""));
                newDate =  new java.sql.Date(d1.getTime());
                int i=1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newDate;
    }
}