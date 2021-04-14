package org.ga4gh.starterkit.drs.controller;

import org.ga4gh.starterkit.drs.App;
import org.ga4gh.starterkit.drs.AppConfig;
import org.ga4gh.starterkit.drs.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
@ContextConfiguration(classes={App.class, AppConfig.class, ServiceInfo.class})
@WebAppConfiguration
public class ServiceInfoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    private static final String serviceInfoDir = "/responses/service-info/";
    private static final String showDir = serviceInfoDir + "show/";
    private static final String serviceInfoFile = showDir + "00.json";

    @Test
    public void testGetServiceInfo() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/service-info"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody = ResourceLoader.load(serviceInfoFile);
        Assert.assertEquals(responseBody, expResponseBody);
    }
}
