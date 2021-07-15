package ru.netology.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.DataGenerator;

import static io.restassured.RestAssured.given;

public class BankServices {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    public boolean checkCardNumber(String service, DataGenerator.CardInfo cardInfo){
        String cardStatus = given()
                .spec(requestSpec)
                .body(cardInfo)
                .filter(new AllureRestAssured())
        .when()
                .post(service)
        .then()
                .statusCode(200)
                .extract()
                .path("status")
        ;
        return cardStatus.equals(cardInfo.getStatus());
    }
}
