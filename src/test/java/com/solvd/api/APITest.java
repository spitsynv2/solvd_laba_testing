package com.solvd.api;

import com.solvd.api.objects.method.*;
import com.solvd.api.objects.service.CreateObjectService;
import com.solvd.api.store.GetAllCartsMethod;
import com.solvd.api.store.GetProductByIdMethod;
import com.solvd.api.store.GetProductsMethod;
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
        getObjectsByIdsMethod.addParameter("id","3");
        getObjectsByIdsMethod.addParameter("id","5");
        getObjectsByIdsMethod.addParameter("id","10");
        getObjectsByIdsMethod.callAPIExpectSuccess();
        getObjectsByIdsMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetSingleObject() {
        LOGGER.info("testGetSingleObject");
        GetSingleObjectMethod getSingleObjectMethod = new GetSingleObjectMethod("7");
        getSingleObjectMethod.callAPIExpectSuccess();
        getSingleObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetProducts() {
        LOGGER.info("testGetProducts");
        GetProductsMethod getProductsMethod = new GetProductsMethod();
        getProductsMethod.callAPIExpectSuccess();
        getProductsMethod.validateResponseAgainstSchema("api/store/_get/products_schema.json");
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetSingleProduct() {
        LOGGER.info("testGetSingleProduct");
        GetProductByIdMethod getProductByIdMethod = new GetProductByIdMethod("1");
        getProductByIdMethod.callAPIExpectSuccess();
        getProductByIdMethod.validateResponseAgainstSchema("api/store/_get/single_product_schema.json");
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testGetCarts() {
        LOGGER.info("testGetCarts");
        GetAllCartsMethod getAllCartsMethod = new GetAllCartsMethod();
        getAllCartsMethod.callAPIExpectSuccess();
        getAllCartsMethod.validateResponseAgainstSchema("api/store/_get/carts_schema.json");
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
        String objectId = CreateObjectService.getNewestObjectId();
        PutObjectMethod putObjectMethod = new PutObjectMethod(objectId);
        putObjectMethod.setProperties("api/objects/objects.properties");

        putObjectMethod.callAPIExpectSuccess();
        putObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testPatchObject() {
        LOGGER.info("testPatchObject");
        String objectId = CreateObjectService.getNewestObjectId();
        PatchObjectMethod patchObjectMethod = new PatchObjectMethod(objectId);
        patchObjectMethod.setProperties("api/objects/objects.properties");

        patchObjectMethod.callAPIExpectSuccess();
        patchObjectMethod.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "Laba")
    public void testDeleteObject() {
        LOGGER.info("testDeleteObject");
        String objectId = CreateObjectService.getNewestObjectId();
        DeleteObjectMethod deleteObjectMethod = new DeleteObjectMethod(objectId);

        deleteObjectMethod.callAPIExpectSuccess();
        deleteObjectMethod.validateResponse();
    }

}
