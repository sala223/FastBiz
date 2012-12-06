package com.fastbiz.common.utils.json;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class JsonUtils{

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
        objectMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.PUBLIC_ONLY);
        objectMapper.setVisibility(JsonMethod.SETTER, Visibility.NON_PRIVATE);
        objectMapper.setVisibility(JsonMethod.GETTER, Visibility.NON_PRIVATE);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String toJson(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonGenerationException ex) {
            throw new JsonException(ex);
        } catch (JsonMappingException ex) {
            throw new JsonException(ex);
        } catch (IOException ex) {
            throw new JsonException(ex);
        }
    }

    public static <T> T readObject(File file, Class<T> type){
        try {
            return objectMapper.readValue(file, type);
        } catch (JsonGenerationException ex) {
            throw new JsonException(ex);
        } catch (JsonMappingException ex) {
            throw new JsonException(ex);
        } catch (IOException ex) {
            throw new JsonException(ex);
        }
    }

    public static void toJson(File file, Object obj){
        try {
            objectMapper.writeValue(file, obj);
        } catch (JsonGenerationException ex) {
            throw new JsonException(ex);
        } catch (JsonMappingException ex) {
            throw new JsonException(ex);
        } catch (IOException ex) {
            throw new JsonException(ex);
        }
    }

    public static <T> Map<String, List<T>> wrapList(String propertyName, List<T> objects){
        Map<String, List<T>> wrapper = new HashMap<String, List<T>>(1);
        wrapper.put(propertyName, objects);
        return wrapper;
    }

    public static <T> Map<String, T[]> wrapArray(String propertyName, T ... objects){
        Map<String, T[]> wrapper = new HashMap<String, T[]>(1);
        wrapper.put(propertyName, objects);
        return wrapper;
    }
}
