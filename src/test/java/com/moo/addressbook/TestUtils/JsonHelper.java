package com.moo.addressbook.TestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonHelper {
    public static <T> T readObjectFromString(String obj, Class<T> c) {
        try {
            return new ObjectMapper().readValue(obj, c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> readListFromString(String obj, Class<T> c) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(obj, mapper.getTypeFactory()
                    .constructCollectionType(List.class, c));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
