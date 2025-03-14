package com.solvd.carina.demo;

import com.solvd.api.*;
import com.zebrunner.carina.api.apitools.validation.JsonCompareKeywords;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-14
 */
public class APITest implements IAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetObjects() {
        LOGGER.info("testGetObjects");
        GetObjectsMethod getObjectsMethod = new GetObjectsMethod();
        getObjectsMethod.callAPIExpectSuccess();
        getObjectsMethod.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetObjectsById() {
        LOGGER.info("testGetObjectsById");
        GetObjectsByIdsMethod getObjectsByIdsMethod = new GetObjectsByIdsMethod();
        getObjectsByIdsMethod.callAPIExpectSuccess();
        getObjectsByIdsMethod.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetSingleObject() {
        LOGGER.info("testGetSingleObject");
        GetSingleObjectMethod getSingleObjectMethod = new GetSingleObjectMethod();
        getSingleObjectMethod.callAPIExpectSuccess();
        getSingleObjectMethod.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testCreateObject() {
        LOGGER.info("testCreateObject");
        PostObjectMethod postObjectMethod = new PostObjectMethod();
        postObjectMethod.setProperties("api/objects/objects.properties");

        postObjectMethod.callAPIExpectSuccess();
        postObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testUpdateObject() {
        LOGGER.info("testUpdateObject");
        PutObjectMethod putObjectMethod = new PutObjectMethod();
        putObjectMethod.setProperties("api/objects/objects.properties");

        putObjectMethod.callAPIExpectSuccess();
        putObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testPatchObject() {
        LOGGER.info("testPatchObject");
        PatchObjectMethod patchObjectMethod = new PatchObjectMethod();
        patchObjectMethod.setProperties("api/objects/objects.properties");

        patchObjectMethod.callAPIExpectSuccess();
        patchObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testDeleteObject() {
        DeleteObjectMethod deleteObjectMethod = new DeleteObjectMethod();

        deleteObjectMethod.callAPIExpectSuccess();
        deleteObjectMethod.validateResponse();
    }

}
