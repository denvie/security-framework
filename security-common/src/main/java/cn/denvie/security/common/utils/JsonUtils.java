/*
 * Copyright © 2020-2020 尛飛俠（Denvie） All rights reserved.
 */

package cn.denvie.security.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * JSON工具类。
 *
 * @author denvie
 * @since 2020/8/23
 */
public class JsonUtils {

    // 默认的JSON解析器
    private static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JSON_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // result.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 获取默认的JSON解析器。
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return JSON_MAPPER;
    }

    /**
     * 将对象转换成Json字符串。
     *
     * @param value Object
     * @return Json字符串
     */
    public static String writeValueAsString(Object value) {
        try {
            return value == null ? "" : JSON_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Map<String, Object> toMap(Object value) throws Exception {
        return convertValue(value, Map.class);
    }

    public static <T> T convertValue(Object value, Class<T> clazz) throws Exception {
        if (value== null || StringUtils.isBlank(value.toString())) return null;
        try {
            if (value instanceof String) {
                value = JSON_MAPPER.readTree((String) value);
            }
            return JSON_MAPPER.convertValue(value, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
