package org.ga4gh.starterkit.drs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChecksumTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {Long.valueOf(1L), "47a43e2f1981cef23dee95dd85fb8233", "md5"},
            {Long.valueOf(200L), "d3882df45306b4cc6f6e9227b54a6026", "trunc512"},
            {Long.valueOf(4000000L), "92f9760ae68487f8959d6e0e6a5f219e", "crc32c"}
        };
    }

    private void assertions(Checksum checksumObj, Long id, String checksum, String type) {
        Assert.assertEquals(checksumObj.getId(), id);
        Assert.assertEquals(checksumObj.getChecksum(), checksum);
        Assert.assertEquals(checksumObj.getType(), type);
    }

    @Test(dataProvider = "cases")
    public void testChecksumConstructor(Long id, String checksum, String type) {
        Checksum checksumObj = new Checksum(id, checksum, type);
        assertions(checksumObj, id, checksum, type);
    }

    @Test(dataProvider = "cases")
    public void testChecksumSetter(Long id, String checksum, String type) {
        Checksum checksumObj = new Checksum();
        checksumObj.setId(id);
        checksumObj.setChecksum(checksum);
        checksumObj.setType(type);
        assertions(checksumObj, id, checksum, type);
    }
}
