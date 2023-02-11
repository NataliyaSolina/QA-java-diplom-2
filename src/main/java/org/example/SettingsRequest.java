package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SettingsRequest {
    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String API_PREFIX = "/api";

    protected RequestSpecification getSpec() {
        try {
            Thread.sleep(500);                      //задержку пришлось поставить что бы исключить постоянную ошибку 429
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .setBasePath(API_PREFIX)
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .log(LogDetail.BODY)
                .build();
    }
}