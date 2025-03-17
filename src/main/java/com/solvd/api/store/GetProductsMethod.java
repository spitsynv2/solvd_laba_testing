package com.solvd.api.store;

import com.solvd.api.AbstractMyAPIMethod;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.config.Configuration;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
@Endpoint(url = "${base_url}", methodType = HttpMethodType.GET)
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class GetProductsMethod extends AbstractMyAPIMethod {

    public GetProductsMethod(){
        replaceUrlPlaceholder("base_url", Configuration.getRequired("api_url_task2")+"products");
    }

}
