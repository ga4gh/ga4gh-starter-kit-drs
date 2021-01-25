package org.ga4gh.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class ErrorTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "The requested resource: 'fooid123' could not be found",
                404,
                "status_code: 404 message: The requested resource: 'fooid123' could not be found"
            },
            {
                "Invalid request parameter",
                400,
                "status_code: 400 message: Invalid request parameter"
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testErrorConstructor(String msg, int statusCode, String expMessage) {
        Error error = new Error(msg, statusCode);
        Assert.assertEquals(error.getMsg(), msg);
        Assert.assertEquals(error.getStatusCode(), statusCode);
    }

    @Test(dataProvider = "cases")
    public void testErrorSetters(String msg, int statusCode, String expMessage) {
        Error error = new Error(null, 0);
        error.setMsg(msg);
        error.setStatusCode(statusCode);
        Assert.assertEquals(error.getMsg(), msg);
        Assert.assertEquals(error.getStatusCode(), statusCode);
    }

    @Test(dataProvider = "cases")
    public void testErrorGetMessage(String msg, int statusCode, String expMessage) {
        Error error = new Error(msg, statusCode);
        Assert.assertEquals(error.getMessage(), expMessage);
    }
}
