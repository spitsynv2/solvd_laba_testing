package com.solvd.api.objects.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.api.objects.method.PostObjectMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
public class CreateObjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String getNewestObjectId(){
        LOGGER.info("getNewestObjectId");
        PostObjectMethod postObjectMethod = new PostObjectMethod();
        postObjectMethod.setProperties("api/objects/objects.properties");
        String rs = postObjectMethod.callAPIExpectSuccess().asString();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(rs);
            return jsonNode.get("id").asText();
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }

    }

}
