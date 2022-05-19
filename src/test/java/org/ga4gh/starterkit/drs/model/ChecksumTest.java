package org.ga4gh.starterkit.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChecksumTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(1L),
                "47a43e2f1981cef23dee95dd85fb8233",
                "md5",
                new DrsObject() {{
                    setId("12345");
                    setSize(Long.valueOf(1L));
                }},
                "12345"
            },
            {
                Long.valueOf(200L),
                "d3882df45306b4cc6f6e9227b54a6026",
                "trunc512",
                new DrsObject() {{
                    setId("98765");
                    setSize(Long.valueOf(1L));
                }},
                "98765"
            },
            {
                Long.valueOf(4000000L),
                "92f9760ae68487f8959d6e0e6a5f219e",
                "crc32c",
                new DrsObject() {{
                    setId("00001");
                    setSize(Long.valueOf(1L));
                }},
                "00001"
            }
        };
    }

    private void assertions(Checksum checksumObj, Long id, String checksum, String type, String expId) {
        Assert.assertEquals(checksumObj.getId(), id);
        Assert.assertEquals(checksumObj.getChecksum(), checksum);
        Assert.assertEquals(checksumObj.getType(), type);
        Assert.assertEquals(checksumObj.getDrsObject().getId(), expId);
    }

    @Test(dataProvider = "cases")
    public void testChecksumConstructor(Long id, String checksum, String type, DrsObject drsObject, String expId) {
        Checksum checksumObj = new Checksum(id, checksum, type);
        checksumObj.loadRelations();
        checksumObj.setDrsObject(drsObject);
        assertions(checksumObj, id, checksum, type, expId);
    }

    @Test(dataProvider = "cases")
    public void testChecksumSetter(Long id, String checksum, String type, DrsObject drsObject, String expId) {
        Checksum checksumObj = new Checksum();
        checksumObj.loadRelations();
        checksumObj.setId(id);
        checksumObj.setChecksum(checksum);
        checksumObj.setType(type);
        checksumObj.setDrsObject(drsObject);
        assertions(checksumObj, id, checksum, type, expId);
    }
}
