package com.zebrunner.carina.demo;

import com.zebrunner.carina.api.APIMethodPoller;
import com.zebrunner.carina.api.apitools.validation.JsonCompareKeywords;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.core.registrar.tag.Priority;
import com.zebrunner.carina.core.registrar.tag.TestPriority;
import com.zebrunner.carina.demo.api.*;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    public void testCreateObject() throws Exception {
        LOGGER.info("testCreateObject");
        PostObjectMethod postObjectMethod = new PostObjectMethod();
        postObjectMethod.setProperties("api/objects/objects.properties");

        postObjectMethod.callAPIExpectSuccess();
        postObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testUpdateObject() throws Exception {
        LOGGER.info("testUpdateObject");
        PutObjectMethod putObjectMethod = new PutObjectMethod();
        putObjectMethod.setProperties("api/objects/objects.properties");

        putObjectMethod.callAPIExpectSuccess();
        putObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testPatchObject() throws Exception {
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
