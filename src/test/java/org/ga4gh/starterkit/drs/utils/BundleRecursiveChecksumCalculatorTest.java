package org.ga4gh.starterkit.drs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.ga4gh.starterkit.drs.model.Checksum;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class BundleRecursiveChecksumCalculatorTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            // single child object
            {
                new DrsObject() {{
                    setDrsObjectChildren(new ArrayList<DrsObject>() {{
                        add(new DrsObject() {{
                            setDrsObjectChildren(new ArrayList<DrsObject>());
                            setChecksums(new ArrayList<Checksum>() {{
                                add(new Checksum(1L, "f81ea43c74824cc72c77a39a92bf7b71", "md5"));
                                add(new Checksum(1L, "34880a6b8aa517a6999da912614753ffb0a837a8", "sha1"));
                                add(new Checksum(1L, "ec44e2ad7ec84c7c42ba57b205e67c7c7416ae1932029d8364cc053cef7abe58", "sha256"));
                            }});
                        }});
                    }});
                }},
                new HashMap<String, String>() {{
                    put("md5", "e827f65c5899cbc7b8699f3153eb3b2d");
                    put("sha1", "170d8e07cd285064cd80771bb8f9885d3ec0a7c4");
                    put("sha256", "2115eca84650617091aabe4d6c37956fa6a9a9f88a5cc0462b2c42af28a28a51");
                }}
            },

            // multiple child object
            {
                new DrsObject() {{
                    setDrsObjectChildren(new ArrayList<DrsObject>() {{
                        add(new DrsObject() {{
                            setDrsObjectChildren(new ArrayList<DrsObject>());
                            setChecksums(new ArrayList<Checksum>() {{
                                add(new Checksum(1L, "f81ea43c74824cc72c77a39a92bf7b71", "md5"));
                                add(new Checksum(1L, "34880a6b8aa517a6999da912614753ffb0a837a8", "sha1"));
                                add(new Checksum(1L, "ec44e2ad7ec84c7c42ba57b205e67c7c7416ae1932029d8364cc053cef7abe58", "sha256"));
                            }});
                        }});
                        add(new DrsObject() {{
                            setDrsObjectChildren(new ArrayList<DrsObject>());
                            setChecksums(new ArrayList<Checksum>() {{
                                add(new Checksum(1L, "1cbab050aa20410dc14ce6906f0312fa", "md5"));
                                add(new Checksum(1L, "3f2f2133054faf71ca9d678fa1fd8918a521faec", "sha1"));
                                add(new Checksum(1L, "2709878797b4e8c6a7db824fa596f42885551cef730d1408b2b620c9eee43089", "sha256"));
                            }});
                        }});
                    }});
                }},
                new HashMap<String, String>() {{
                    put("md5", "b0ba59688de0c9445bda6439621cbe33");
                    put("sha1", "225c7c0737f7aa0b3db41a240be7497362c6e104");
                    put("sha256", "726a70c07bf9683093cc50eb1b4df5cedc2838832e002f8c3c5d20a73c9861a9");
                }}
            },

            // missing checksums in some children
            {
                new DrsObject() {{
                    setDrsObjectChildren(new ArrayList<DrsObject>() {{
                        add(new DrsObject() {{
                            setDrsObjectChildren(new ArrayList<DrsObject>());
                            setChecksums(new ArrayList<Checksum>() {{
                                add(new Checksum(1L, "f81ea43c74824cc72c77a39a92bf7b71", "md5"));
                                add(new Checksum(1L, "34880a6b8aa517a6999da912614753ffb0a837a8", "sha1"));
                                add(new Checksum(1L, "ec44e2ad7ec84c7c42ba57b205e67c7c7416ae1932029d8364cc053cef7abe58", "sha256"));
                            }});
                        }});
                        add(new DrsObject() {{
                            setDrsObjectChildren(new ArrayList<DrsObject>());
                            setChecksums(new ArrayList<Checksum>() {{
                                add(new Checksum(1L, "1cbab050aa20410dc14ce6906f0312fa", "md5"));
                            }});
                        }});
                    }});
                }},
                new HashMap<String, String>() {{
                    put("md5", "b0ba59688de0c9445bda6439621cbe33");
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testRecursiveCalculateChecksums(DrsObject drsObject, Map<String, String> expChecksumMap) {
        Map<String, String> checksumMap = BundleRecursiveChecksumCalculator.recursiveCalculateChecksums(drsObject);
        Assert.assertEquals(checksumMap.size(), expChecksumMap.size());

        for (String key : expChecksumMap.keySet()) {
            Assert.assertEquals(checksumMap.get(key), expChecksumMap.get(key));
        }
    }
}
