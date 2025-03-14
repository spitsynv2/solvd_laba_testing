package com.solvd.api;

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

@Endpoint(url = "${base_url}/users/1", methodType = HttpMethodType.DELETE)
@ResponseTemplatePath(path = "api/objects/_delete/rs.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class DeleteObjectMethod extends AbstractApiMethodV2 {

    public DeleteObjectMethod(){
        replaceUrlPlaceholder("base_url", "https://api.restful-api.dev/objects/6");
    }

    @Override
    public void expectResponseStatus(HttpResponseStatus status) {
        this.request.expect().statusCode(status.getCode());
        //this.request.expect().statusLine(Matchers.containsString(status.getMessage()));
    }

}
