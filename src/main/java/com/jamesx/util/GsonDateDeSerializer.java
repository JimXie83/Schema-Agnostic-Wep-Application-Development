package com.jamesx.util;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

 /**************************************************
 * de-serialize Json object into Java Entity/Object
 * 	convert date: <1>date format or <2> UTC date
 **************************************************/
//@Component
public class GsonDateDeSerializer implements JsonDeserializer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(HelperUtil.DATE_FORMAT);//("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); //("yyyy-MM-dd"); //
     @Override
     public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)  {
         Date newDate = null;
         String jsonStr = json.toString().replace("\"", ""); //remove "\" from date
         // if json is all numeric, assume long UTC date
         //else assume UTC date of format yyyy-MM-dd'T'HH:mm:ss.SSSX
         if (json.toString().matches("[-+]?\\d*\\.?\\d+")) {
             newDate = new java.util.Date(json.getAsJsonPrimitive().getAsLong());
         } else  {
             try {
                 newDate = dateFormat.parse(json.toString().replace("\"", ""));
             } catch (ParseException e) {
                 e.printStackTrace();
             }
         }
         return newDate;
     }

}

//
//public class CustomJsonDateDeserializer extends JsonDeserializer<Date>
//{
//    @Override
//    public Date deserialize(JsonParser jsonparser,
//                            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        String date = jsonparser.getText();
//        try {
//            return format.parse(date);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}