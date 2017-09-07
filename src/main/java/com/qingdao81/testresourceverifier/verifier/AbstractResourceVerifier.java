package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractResourceVerifier {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractResourceVerifier.class);

    private final Stopwatch stopwatch;

    private final String host;

    private final int port;

    private final String resourceName;

    private final boolean logDetails;

    AbstractResourceVerifier(ResourceVerifier verifier) {
        this.host = verifier.host();
        this.port = verifier.port() == -1 ? verifier.type().getDefaultPort() : verifier.port();
        this.resourceName = verifier.type().name();
        this.logDetails = verifier.logDetails();
        stopwatch = Stopwatch.createStarted();
        logInfo(verifier.timeout());
    }

    private void logInfo(long timeout) {
        LOGGER.info("Trying to verify {} [{}:{}] within {} ms.", new Object[]{resourceName, host, port, timeout});
    }

    private void logErrorAndDelay(Exception ex) {
        if (logDetails) {
            LOGGER.warn("Verification attempt failed! {} [{}:{}], details: {}",
                    new Object[]{resourceName, host, port, ex.getMessage()});
            LOGGER.warn("", ex);
        }
        try {Thread.sleep(500);} catch (InterruptedException e) {}
    }

    private void logSuccess() {
        LOGGER.info("Success! {} [{}:{}] was verified after {}ms.", new Object[]{resourceName, host, port,
                stopwatch.elapsed(TimeUnit.MILLISECONDS)});
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }

    abstract boolean doVerify() throws Exception;

    public boolean verify() {
        try {
            boolean success = doVerify();
            if (success) {
                logSuccess();
            }
            return success;
        } catch (Exception ex) {
            logErrorAndDelay(ex);
            return false;
        }
    }
}
