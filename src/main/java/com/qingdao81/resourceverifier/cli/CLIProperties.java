package com.qingdao81.resourceverifier.cli;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import org.immutables.value.Value;

/**
 * CLI parser properties, auto generated by immutable library
 *
 */
@Value.Immutable
public interface CLIProperties {

    String host();

    int port();

    ResourceVerifier.Type type();

    long timeout();

    String uri();

    boolean logDetails();
}
