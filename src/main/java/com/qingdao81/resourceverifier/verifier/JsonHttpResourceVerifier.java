package com.qingdao81.resourceverifier.verifier;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import com.jayway.jsonpath.JsonPath;

import java.net.URL;

/**
 * Resource verifier for json http resources. Looks for a given key in the json document and compares with an
 * expected value.
 */
public class JsonHttpResourceVerifier extends AbstractResourceVerifier {

    private final String key;

    private final String expected;

    private final String jsonPath;

    public JsonHttpResourceVerifier(ResourceVerifier verifier, String key, String expected, String jsonPath) {
        super(verifier);
        this.key = key;
        this.expected = expected;
        this.jsonPath = jsonPath;
    }

    @Override
    boolean doVerify() throws Exception {
        final String actual = JsonPath.parse(
            new URL(String.format("http://%s:%d/%s", getHost(), getPort(), jsonPath))).read(key);

        if (expected.toLowerCase().equals(actual.toLowerCase())) {
            return true;
        }
        return false;
    }
}
