package org.ga4gh.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChecksumTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {"47a43e2f1981cef23dee95dd85fb8233", "md5"},
            {"d3882df45306b4cc6f6e9227b54a6026", "trunc512"},
            {"92f9760ae68487f8959d6e0e6a5f219e", "crc32c"}
        };
    }

    private void assertions(Checksum checksumObj, String checksum, String type) {
        Assert.assertEquals(checksumObj.getChecksum(), checksum);
        Assert.assertEquals(checksumObj.getType(), type);
    }

    @Test(dataProvider = "cases")
    public void testChecksumConstructor(String checksum, String type) {
        Checksum checksumObj = new Checksum(checksum, type);
        assertions(checksumObj, checksum, type);
    }

    @Test(dataProvider = "cases")
    public void testChecksumSetter(String checksum, String type) {
        Checksum checksumObj = new Checksum(null, null);
        checksumObj.setChecksum(checksum);
        checksumObj.setType(type);
        assertions(checksumObj, checksum, type);
    }
}
