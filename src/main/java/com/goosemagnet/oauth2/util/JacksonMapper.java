package com.goosemagnet.oauth2.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import lombok.val;

@Provider
public final class JacksonMapper implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonMapper() {
        mapper = createMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }

    public static ObjectMapper createMapper() {
        val mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
