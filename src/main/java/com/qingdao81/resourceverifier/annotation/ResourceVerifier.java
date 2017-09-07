package com.qingdao81.resourceverifier.annotation;

import com.google.common.base.Joiner;

import java.lang.annotation.*;

/**
 * Resource verifier annotation.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ResourceVerifiers.class)
public @interface ResourceVerifier {

    /**
     * Default is localhost, can be overwritten.
     *
     * @return host os the resource
     */
    String host() default "localhost";

    /**
     * Default port is the port of the {@Type} enum, can be overwritten.
     *
     * @return port of the resource
     */
    int port() default -1;

    /**
     * The only mandatory parameter of the resource verifier.
     *
     * @return {@Type}
     */
    Type type();

    /**
     * Default timeout is 2 min, can be overwritten.
     *
     * @return timeout for resource verification
     */
    long timeout() default 120_000;

    /**
     * Some resource verifier provide an additional uri param (for example for http requests).
     *
     * @return uri of the resource
     */
    String uri() default "";

    /**
     * By default don't log details, can be overwritten.
     *
     * @return flag if to log details
     */
    boolean logDetails() default false;

    enum Type {
        KAFKA(9092),
        ZOOKEEPER(2181),
        SAMZA(2181),
        MONGO(27017),
        ELASTICSEARCH(9200),
        HADOOP_YARN(8088),
        HADOOP_HDFS(50070),
        HTTP(80);

        int defaultPort;

        Type(int defaultPort) {
            this.defaultPort = defaultPort;
        }

        public int getDefaultPort() {
            return defaultPort;
        }

        /**
         * Parse {@link Type} from a name. Allows to provide the type in lower chars.
         *
         * @param name
         * @return
         */
        public static Type fromName(String name) {
            for (Type type : Type.values()) {
                if (type.toString().equals(name.toUpperCase())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("no type for name: " + name + " available types:" +
                Joiner.on(',').join(Type.values()));
        }
    }
}
