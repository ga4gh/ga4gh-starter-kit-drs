package org.ga4gh.starterkit.drs.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class ContentsObjectTest {

    @DataProvider(name = "cases")
    public Object[][] getData() throws URISyntaxException {
        return new Object[][] {
            {
                "sample1-data",
                new ArrayList<ContentsObject>() {{
                    add(new ContentsObject("sample1-tumor.bam"));
                }},
                new ArrayList<URI>() {{
                    add(new URI("drs://drs.example.org/00001"));
                }},
                "data:00001"
            }

        };
    }

    @Test(dataProvider = "cases")
    public void testContentsObject(String name, List<ContentsObject> contents, List<URI> drsUri, String id) {
        ContentsObject contentsObject = new ContentsObject(name);
        contentsObject.setName(name);
        contentsObject.setContents(contents);
        contentsObject.setDrsUri(drsUri);
        contentsObject.setId(id);
        Assert.assertEquals(contentsObject.getName(), name);
        Assert.assertEquals(contentsObject.getContents(), contents);
        Assert.assertEquals(contentsObject.getDrsUri(), drsUri);
        Assert.assertEquals(contentsObject.getId(), id);
    }
}
