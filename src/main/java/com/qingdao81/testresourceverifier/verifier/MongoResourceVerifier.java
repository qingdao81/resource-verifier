package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

/**
 * Resource verifier for mongo db.
 *
 */
public class MongoResourceVerifier extends AbstractResourceVerifier {

    public MongoResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
    }

    @Override
    boolean doVerify() throws Exception {
        final MongoClient client = new MongoClient(new ServerAddress(getHost(), getPort()),
                MongoClientOptions.builder().serverSelectionTimeout(3000).build());
        client.getAddress();
        return true;
    }
}
