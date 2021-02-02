package org.ga4gh.drs.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class DrsObjectTest {

    private static class TestCase {
        
        protected String id;
        protected URI selfURI;
        protected List<Checksum> checksums;
        protected LocalDateTime createdTime;
        protected long size;
        protected List<AccessMethod> accessMethods;
        protected List<String> aliases;
        protected List<ContentsObject> contents;
        protected LocalDateTime updatedTime;
        protected String description;
        protected String mimeType;
        protected String name;
        protected String version;

        public TestCase(String id, URI selfURI, List<Checksum> checksums,
            LocalDateTime createdTime, long size, List<AccessMethod> accessMethods,
            List<String> aliases, List<ContentsObject> contents, LocalDateTime updatedTime,
            String description, String mimeType, String name, String version) {
            
            this.id = id;
            this.selfURI = selfURI;
            this.checksums = checksums;
            this.createdTime = createdTime;
            this.size = size;
            this.accessMethods = accessMethods;
            this.aliases = aliases;
            this.contents = contents;
            this.updatedTime = updatedTime;
            this.description = description;
            this.mimeType = mimeType;
            this.name = name;
            this.version = version;
        }
    }

    @DataProvider(name = "cases")
    public Object[][] getData() throws URISyntaxException {
        return new Object[][] {
            {new TestCase(
                "id:00001",
                new URI("drs://drs.exampleserver.org/id:00001"),
                new ArrayList<Checksum>() {{
                    add(new Checksum("51430d4cc2306f850cfbd8315badc53a", "md5"));
                }},
                LocalDateTime.now(),
                1024,
                new ArrayList<AccessMethod>() {{
                    add(new AccessMethod("id:00001", AccessType.FILE));
                }},
                new ArrayList<String>() {{
                    add("Patient 0 BAM");
                }},
                new ArrayList<ContentsObject>() {{
                    add(new ContentsObject("Patient 0 tumor sample BAM file"));
                }},
                LocalDateTime.now(),
                "Patient 0 normal sample BAM file",
                "application/x-bam",
                "sample1.bam",
                "13.12.1"
            )}
        };
    }

    private void setters(DrsObject drsObject, TestCase testCase) {
        drsObject.setAccessMethods(testCase.accessMethods);
        drsObject.setAliases(testCase.aliases);
        drsObject.setContents(testCase.contents);
        drsObject.setUpdatedTime(testCase.updatedTime);
        drsObject.setDescription(testCase.description);
        drsObject.setMimeType(testCase.mimeType);
        drsObject.setName(testCase.name);
        drsObject.setVersion(testCase.version);
    }

    private void assertions(DrsObject drsObject, TestCase testCase) {
        Assert.assertEquals(drsObject.getId(), testCase.id);
        Assert.assertEquals(drsObject.getSelfURI(), testCase.selfURI);
        Assert.assertEquals(drsObject.getChecksums(), testCase.checksums);
        Assert.assertEquals(drsObject.getCreatedTime(), testCase.createdTime);
        Assert.assertEquals(drsObject.getSize(), testCase.size);
        Assert.assertEquals(drsObject.getAccessMethods(), testCase.accessMethods);
        Assert.assertEquals(drsObject.getAliases(), testCase.aliases);
        Assert.assertEquals(drsObject.getContents(), testCase.contents);
        Assert.assertEquals(drsObject.getUpdatedTime(), testCase.updatedTime);
        Assert.assertEquals(drsObject.getDescription(), testCase.description);
        Assert.assertEquals(drsObject.getMimeType(), testCase.mimeType);
        Assert.assertEquals(drsObject.getName(), testCase.name);
        Assert.assertEquals(drsObject.getVersion(), testCase.version);
    }

    @Test(dataProvider = "cases")
    public void testDrsObjectConstructor(TestCase testCase) {
        DrsObject drsObject = new DrsObject(testCase.id, testCase.selfURI, testCase.checksums, testCase.createdTime, testCase.size);
        setters(drsObject, testCase);
        assertions(drsObject, testCase);
    }

    @Test(dataProvider = "cases")
    public void testDrsObjectSetters(TestCase testCase) {
        DrsObject drsObject = new DrsObject(null, null, null, null, 0);
        drsObject.setId(testCase.id);
        drsObject.setSelfURI(testCase.selfURI);
        drsObject.setChecksums(testCase.checksums);
        drsObject.setCreatedTime(testCase.createdTime);
        drsObject.setSize(testCase.size);
        setters(drsObject, testCase);
        assertions(drsObject, testCase);
    }
}
