package org.ga4gh.starterkit.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FileAccessObjectTest {
    
    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(1L),
                "/path/to/the/file.bam",
                new DrsObject() {{
                    setId("12345");
                    setSize(Long.valueOf(1L));
                }},
                "12345"
            },
            {
                Long.valueOf(200L),
                "/data/phenopackets/0001.json",
                new DrsObject() {{
                    setId("98765");
                    setSize(Long.valueOf(1L));
                }},
                "98765"
            },
            {
                Long.valueOf(4000000L),
                "./data/cram/myfile.cram",
                new DrsObject() {{
                    setId("00001");
                    setSize(Long.valueOf(1L));
                }},
                "00001"
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testFileAccessObject(Long id, String path, DrsObject drsObject, String expDrsObjectId) {
        FileAccessObject fileAccessObject = new FileAccessObject();
        fileAccessObject.loadRelations();
        fileAccessObject.setId(id);
        fileAccessObject.setPath(path);
        fileAccessObject.setDrsObject(drsObject);

        Assert.assertEquals(fileAccessObject.getId(), id);
        Assert.assertEquals(fileAccessObject.getPath(), path);
        Assert.assertEquals(fileAccessObject.getDrsObject().getId(), expDrsObjectId);
    }
}
