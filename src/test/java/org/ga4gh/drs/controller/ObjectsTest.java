package org.ga4gh.drs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.ga4gh.drs.App;
import org.ga4gh.drs.AppConfig;
import org.ga4gh.drs.model.DrsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class, Objects.class})
@WebAppConfiguration
public class ObjectsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
        };
    }


    @Test(dataProvider = "cases")
    public void testGetObjectById(String objectId, boolean expSuccess, ResultMatcher expStatus) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/objects/" + objectId))
            .andExpect(expStatus)
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        DrsObject responseDrsObject = mapper.readValue(responseBody, DrsObject.class);
    }
}
