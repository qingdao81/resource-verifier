# resource verifier

Resource verifier can be used in tests and in a docker environment to verify that a certain service is available.

##### table of contents
[description](#description)  
[linking](#linking)<br/>
[supported resource verifiers](#verifiers)  
[test resource verifier in an integration test](#integration)
 * [junit](#junit)
 * [testng](#testng)
 * [maven](#maven)
 
[test resource verifier in a docker environment](#docker)
 * [build docker image](#dockerimage)
 * [docker](#dockercommand)
 * [docker compose](#dockercompose)

<a name="description" />

## description

Just because a docker container with a service is up does not necessarily mean that the service is really running and available. Some services
require a couple of seconds (depending on the service and the environment) for startup. If you try to run a test against a service which is not
available you most likely run into issues. A very simple approach to solve this would be to use a thread.sleep in the beginning of your test. This
is not a nice way and also not reliable. So that's where resource verifiers come into play.

The idea of a resource verifier is to make sure that a certain service is really available before starting the test.
Resource verifiers can be used in integration tests and in docker environments - the core logic is the same. 

<a name="linking" />

## linking

* checkout the repo
* build the project `mvn clean install`
* include dependency into your project

```
    <dependency>
        <groupId>com.qingdao81</groupId>
        <artifactId>resource-verifier</artifactId>
        <version>VERSION</version>
        <scope>test</scope>
    </dependency>
```

<a name="verifiers" />

## supported resource verifiers

* kafka
* zookeeper
* elasticsearch
* hadoop yarn
* hadoop hdfs
* http
* mongo
* samza

See [ResourceVerifier.Type](https://github.com/qingdao81/resource-verifier/blob/master/src/main/java/com/qingdao81/resourceverifier/annotation/ResourceVerifier.java#L58) for more details.

<a name="integration" />

## test resource verifier in an integration test

<a name="junit" />

### junit

In an integration test a resource verifier is just an annotation which you add to your test class. 

```
@ResourceVerifier(host = "localhost", timeout = 25000, port = 9092, type = ResourceVerifier.Type.KAFKA)
@ResourceVerifier(host = "localhost", timeout = 25000, port = 2181, type = ResourceVerifier.Type.ZOOKEEPER)
@RunWith(ExternalResourceAwareRunner.class)
public class KafkaIT {

    @Test
    public void testKafka() throws Exception {
        // do some communication with kafka
    }
}
```

These resource verifier check continuously for the availability of kafka and zookeeper on the given host and port
and fail in case the verification takes longer than the timeout. In case of a timeout the tests of the test class are not executed.

Each resource verifier provides default values for host, port and timeout. If the default values are sufficient the above resource
verifiers can also be defined like this:

```
@ResourceVerifier(type = ResourceVerifier.Type.KAFKA)
@ResourceVerifier(type = ResourceVerifier.Type.ZOOKEEPER)
```

<a name="testng" />

### testng

TODO

<a name="maven" />

### maven

[maven failsafe plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) and resource verifiers are a powerful combination. The failsafe plugin can prepare and destroy a
local container bases test environment in the corresponding build phases (pre-integration-test and post-integration-test) 
and the resource verifiers are used to check for the availability of the services before executing a test.
See the **integration-test profile in the pom.xml** for more details. 

You can run the integration test of the resource verifier project with the command:

```
mvn clean verify -Pintegration-test
```

<a name="docker" />

## test resource verifier in a docker environment

In a docker environment the resource verifier is a started in a docker container and tries to verify
that a service running in another docker container is available.

<a name="dockerimage" />

### docker image

To build the docker image execute a maven build with the **docker** profile:

```
mvn clean install -Pdocker
```

<a name="dockercommand" />

### docker command

```
docker run --rm -e HOST=zookeeper -e TYPE=ZOOKEEPER -e PORT=2181 -e TIMEOUT=60000 qingdao81/resource-verifier:0.0.1-SNAPSHOT
```

<a name="dockercompose" />

### docker compose

```
version: '3'
services:

    zookeeper:
        image: wurstmeister/zookeeper:latest
        ports:
            - "2181:2181"

    verify-zookeeper:
        image: qingdao81/resource-verifier:0.0.1-SNAPSHOT
        environment:
            HOST: zookeeper
            TYPE: ZOOKEEPER
            PORT: 2181
            TIMEOUT: 60000
```

Copy this to a file docker-compose.yml and run with:

```
docker-compose up 
```

This resource verifier container checks continuously if the zookeeper container is available on dns zookeeper and port 2181. 
If the zookeeper container is not up after 60000 ms the resource verifier docker container fails, prints an error message and
exits with exit code 1. If the verification was successful the resource verifier docker container prints a success message 
and exits with exit code 0.
