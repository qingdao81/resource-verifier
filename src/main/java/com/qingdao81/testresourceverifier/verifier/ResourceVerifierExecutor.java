package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import org.awaitility.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

public class ResourceVerifierExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceVerifierExecutor.class);

    public void executeResourceVerifier(final ResourceVerifier verifier) {
        final AtomicReference<AbstractResourceVerifier> verifierSource = new AtomicReference<>();
        switch (verifier.type()) {
            case KAFKA:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new KafkaResourceVerifier(verifier));
                break;
            case ZOOKEEPER:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new ZookeeperResourceVerifier(verifier));
                break;
            case SAMZA:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new SamzaResourceVerifier(verifier));
                break;
            case MONGO:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new MongoResourceVerifier(verifier));
                break;
            case ELASTICSEARCH:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new JsonHttpResourceVerifier(verifier,
                        "status", "green", "_cluster/health"));
                break;
            case HADOOP_YARN:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new JsonHttpResourceVerifier(verifier,
                        "clusterInfo.state", "started", "ws/v1/cluster/info"));
                break;
            case HADOOP_HDFS:
                logURINotSupportedByVerifierIfNecessary(verifier);
                verifierSource.set(new JsonHttpResourceVerifier(verifier,
                        "beans[4].State", "active", "jmx"));
                break;
            case HTTP:
                verifierSource.set(new HttpResourceVerifier(verifier));
                break;
            default:
                throw new IllegalArgumentException("resource resource verifier type not supported: " + verifier.type().name());
        }
        final Duration duration = new Duration(500, TimeUnit.MILLISECONDS);
        try {
            await().atMost(verifier.timeout(), TimeUnit.MILLISECONDS).pollDelay(duration).until(() -> verifierSource.get().verify());
        } catch (final Exception ex) {
            throw new RuntimeException(String.format("could not verify resource %s within %d ms.", verifier.type().name(), verifier.timeout()), ex);
        }
    }

    private void logURINotSupportedByVerifierIfNecessary(final ResourceVerifier verifier) {
        if (verifier.uri() != null && !verifier.uri().isEmpty()) {
            LOGGER.warn("{} does not support uri argument, given uri was: {}", verifier.type().name(), verifier.uri());
        }
    }
}
