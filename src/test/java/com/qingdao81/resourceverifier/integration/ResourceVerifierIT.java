package com.qingdao81.resourceverifier.integration;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import com.qingdao81.resourceverifier.cli.ImmutableCLIProperties;
import com.qingdao81.resourceverifier.junit.ExternalResourceAwareRunner;
import com.qingdao81.resourceverifier.cli.CLIResourceVerifier;
import com.qingdao81.resourceverifier.verifier.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for resource verifiers.
 *
 */
@ResourceVerifier(type = ResourceVerifier.Type.MONGO)
@ResourceVerifier(type = ResourceVerifier.Type.ELASTICSEARCH)
@ResourceVerifier(type = ResourceVerifier.Type.KAFKA)
@ResourceVerifier(type = ResourceVerifier.Type.ZOOKEEPER)
@ResourceVerifier(type = ResourceVerifier.Type.HADOOP_YARN)
@ResourceVerifier(type = ResourceVerifier.Type.HADOOP_HDFS)
@ResourceVerifier(type = ResourceVerifier.Type.HTTP)
@RunWith(ExternalResourceAwareRunner.class)
public class ResourceVerifierIT {

    @Test
    public void testMongo() throws Exception {
        assertThat(new MongoResourceVerifier(v("localhost", 27017, ResourceVerifier.Type.MONGO)).verify()).isTrue();
        assertThat(new MongoResourceVerifier(v("localhost", 27018, ResourceVerifier.Type.MONGO)).verify()).isFalse();
    }

    @Test
    public void testElasticSearch() throws Exception {
        assertThat(new JsonHttpResourceVerifier(v("localhost", 9200, ResourceVerifier.Type.ELASTICSEARCH),
                "status", "green", "_cluster/health").verify()).isTrue();
        assertThat(new JsonHttpResourceVerifier(v("localhost", 9201, ResourceVerifier.Type.ELASTICSEARCH),
                "status", "green", "_cluster/health").verify()).isFalse();
    }

    @Test
    public void testKafka() throws Exception {
        assertThat(new KafkaResourceVerifier(v("localhost", 9092, ResourceVerifier.Type.KAFKA)).verify()).isTrue();
        assertThat(new KafkaResourceVerifier(v("localhost", 9093, ResourceVerifier.Type.KAFKA)).verify()).isFalse();
    }

    @Test
    public void testZookeeper() throws Exception {
        assertThat(new ZookeeperResourceVerifier(v("localhost", 2181, ResourceVerifier.Type.ZOOKEEPER)).verify()).isTrue();
        assertThat(new ZookeeperResourceVerifier(v("localhost", 2182, ResourceVerifier.Type.ZOOKEEPER)).verify()).isFalse();
    }

    @Test
    public void testHadoopYarn() throws Exception {
        assertThat(new JsonHttpResourceVerifier(v("localhost", 8088, ResourceVerifier.Type.HADOOP_YARN),
                "clusterInfo.state", "started", "ws/v1/cluster/info").verify()).isTrue();
        assertThat(new JsonHttpResourceVerifier(v("localhost", 8089, ResourceVerifier.Type.HADOOP_YARN),
                "clusterInfo.state", "started", "ws/v1/cluster/info").verify()).isFalse();
    }

    @Test
    public void testHadoopHdfs() throws Exception {
        assertThat(new JsonHttpResourceVerifier(v("localhost", 50070, ResourceVerifier.Type.HADOOP_HDFS),
                "beans[4].State", "active", "jmx").verify()).isTrue();
        assertThat(new JsonHttpResourceVerifier(v("localhost", 50071, ResourceVerifier.Type.HADOOP_HDFS),
                "beans[4].State", "active", "jmx").verify()).isFalse();
    }

    @Test
    public void testHttp() throws Exception {
        assertThat(new HttpResourceVerifier(v("localhost", 80, ResourceVerifier.Type.HTTP)).verify()).isTrue();
        assertThat(new HttpResourceVerifier(v("localhost", 81, ResourceVerifier.Type.HTTP)).verify()).isFalse();
    }

    private ResourceVerifier v(String host, int port, ResourceVerifier.Type type) {
        return new CLIResourceVerifier(
                ImmutableCLIProperties.builder()
                    .host(host)
                    .port(port)
                    .type(type)
                    .timeout(0)
                    .uri("")
                    .logDetails(false)
                    .build());
    }
}
