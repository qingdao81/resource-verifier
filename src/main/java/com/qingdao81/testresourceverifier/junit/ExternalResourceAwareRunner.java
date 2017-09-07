package com.qingdao81.testresourceverifier.junit;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import com.qingdao81.testresourceverifier.annotation.ResourceVerifiers;
import com.qingdao81.testresourceverifier.verifier.ResourceVerifierExecutor;
import com.google.common.collect.Sets;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.Collections;
import java.util.Set;

/**
 * Junit runner to handle {@link ResourceVerifier}.
 *
 * Executes each {@link ResourceVerifier} which is annotated to the class.
 *
 */
public class ExternalResourceAwareRunner extends BlockJUnit4ClassRunner {

    private static final Set<String> VERIFIED_TEST_CLASSES = Collections.synchronizedSet(Sets.newHashSet());

    private final ResourceVerifierExecutor executor;

    public ExternalResourceAwareRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.executor = new ResourceVerifierExecutor();
        executeResourceVerifiers(klass);
    }

    private void executeResourceVerifiers(final Class<?> testClass) {

        final ResourceVerifier verifier = testClass.getAnnotation(ResourceVerifier.class);
        final ResourceVerifiers verifiers = testClass.getAnnotation(ResourceVerifiers.class);

        if (verifier == null && verifiers == null) {
            return;
        }

        if (!VERIFIED_TEST_CLASSES.add(testClass.getName())) {
            return;
        }

        if (verifier != null) {
            executor.executeResourceVerifier(verifier);
        }
        if (verifiers != null) {
            for (ResourceVerifier v : verifiers.value()) {
                executor.executeResourceVerifier(v);
            }
        }
    }


}
