package com.solvd.api.objects.method;

import com.solvd.api.AbstractMyAPIMethod;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.config.Configuration;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-13
 */

@Endpoint(url = "${base_url}", methodType = HttpMethodType.GET)
@ResponseTemplatePath(path = "api/objects/_get/rs_single.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class GetSingleObjectMethod extends AbstractMyAPIMethod {

    public GetSingleObjectMethod(String objectId){
        replaceUrlPlaceholder("base_url", Configuration.getRequired("api_url_task1")+"/"+objectId);
    }

}
