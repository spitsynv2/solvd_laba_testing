package com.solvd.api;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.http.HttpResponseStatus;

/**
 * @author Vadym Spitsyn
 * @created 2025-03-17
 */
public class AbstractMyAPIMethod extends AbstractApiMethodV2 {

    @Override
    public void expectResponseStatus(HttpResponseStatus status) {
        this.request.expect().statusCode(status.getCode());
        //Expected status line a string containing "OK" doesn't match actual status line "HTTP/1.1 200 ".
        //this.request.expect().statusLine(Matchers.containsString(status.getMessage()));
    }
}
