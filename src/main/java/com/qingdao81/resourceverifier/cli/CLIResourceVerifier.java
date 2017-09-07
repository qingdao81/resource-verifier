package com.qingdao81.resourceverifier.cli;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.annotation.Annotation;

/**
 * Implementation of {@link ResourceVerifier} which uses {@link CLIProperties}.
 */
public class CLIResourceVerifier implements ResourceVerifier {

    private final CLIProperties props;

    public CLIResourceVerifier(CLIProperties props) {
        this.props = props;
    }

    @Override
    public String host() {
        return props.host();
    }

    @Override
    public int port() {
        return props.port();
    }

    @Override
    public Type type() {
        return props.type();
    }

    @Override
    public long timeout() {
        return props.timeout();
    }

    @Override
    public String uri() {
        return props.uri();
    }

    @Override
    public boolean logDetails() {
        return props.logDetails();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        throw new NotImplementedException();
    }
}
