package com.qingdao81.resourceverifier.cli;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import com.qingdao81.resourceverifier.verifier.ResourceVerifierExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application to execute resource verifiers.
 */
public class CLIResourceVerifierApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(CLIResourceVerifierApp.class);

    private ResourceVerifier resourceVerifier;

    CLIResourceVerifierApp(CLIProperties props) {
        resourceVerifier = new CLIResourceVerifier(props);
    }

    void execute() {
        new ResourceVerifierExecutor().executeResourceVerifier(resourceVerifier);
    }

    public static void main(String[] args) throws Exception {
        CLIProperties props = new CLIParser().parse(args);

        try {
            new CLIResourceVerifierApp(props).execute();
            System.exit(0);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            System.exit(1);
        }
    }
}
