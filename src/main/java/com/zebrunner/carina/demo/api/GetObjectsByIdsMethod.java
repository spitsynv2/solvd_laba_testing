package com.zebrunner.carina.demo.api;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatus;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-13
 */

@Endpoint(url = "${base_url}", methodType = HttpMethodType.GET)
@ResponseTemplatePath(path = "api/objects/_get/rs_by_ids.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class GetObjectsByIdsMethod extends AbstractApiMethodV2 {

    public GetObjectsByIdsMethod(){
        replaceUrlPlaceholder("base_url", "https://api.restful-api.dev/objects?id=3&id=5&id=10");
    }

    @Override
    public void expectResponseStatus(HttpResponseStatus status) {
        this.request.expect().statusCode(status.getCode());
        //Expected status line a string containing "OK" doesn't match actual status line "HTTP/1.1 200 ".
        //this.request.expect().statusLine(Matchers.containsString(status.getMessage()));
    }

}
