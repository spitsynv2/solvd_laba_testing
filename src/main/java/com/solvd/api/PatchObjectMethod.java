package com.solvd.api;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.RequestTemplatePath;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatus;
import com.zebrunner.carina.api.http.HttpResponseStatusType;

/**
  * @author Vadym Spitsyn
  * @created 2025-03-13
*/

@Endpoint(url = "${base_url}", methodType = HttpMethodType.PATCH)
@RequestTemplatePath(path = "api/objects/_patch/rq.json")
@ResponseTemplatePath(path = "api/objects/_patch/rs.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class PatchObjectMethod extends AbstractApiMethodV2 {

    public PatchObjectMethod(){
        replaceUrlPlaceholder("base_url", "https://api.restful-api.dev/objects/7");
    }

    @Override
    public void expectResponseStatus(HttpResponseStatus status) {
        this.request.expect().statusCode(status.getCode());
        //this.request.expect().statusLine(Matchers.containsString(status.getMessage()));
    }

}
