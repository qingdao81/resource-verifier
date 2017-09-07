package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import com.google.common.collect.Range;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Resource verifier for http resources. Response status code 200-299 is considered valid, other codes are
 * considered invalid.
 *
 */
public class HttpResourceVerifier extends AbstractResourceVerifier {

    private final String uri;

    public HttpResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
        this.uri = verifier.uri();
    }

    @Override
    boolean doVerify() throws Exception {
        if (Range.closedOpen(200, 300).contains(executeRequest().getResponseCode())) {
            return true;
        }
        return false;
    }

    private HttpURLConnection executeRequest() throws Exception {
        final HttpURLConnection con = (HttpURLConnection)
                new URL(String.format("http://%s:%d/%s", getHost(), getPort(), uri))
                        .openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.connect();
        return con;
    }
}
