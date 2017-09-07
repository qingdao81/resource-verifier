package com.qingdao81.resourceverifier.verifier;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;

import java.nio.ByteBuffer;

/**
 * Resource verifier for kafka.
 *
 */
public class KafkaResourceVerifier extends SocketResourceVerifier {

    public KafkaResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
        this.initialHandshakeMessage = createKafkaHandshake();
    }

    // see https://cwiki.apache.org/confluence/display/KAFKA/A+Guide+To+The+Kafka+Protocol
    private ByteBuffer createKafkaHandshake() {
        ByteBuffer bb = ByteBuffer.allocate(22);

        bb.putInt(18); // Size

        bb.putShort((short) 3); // 2 ApiKey
        bb.putShort((short) 0); // 2 ApiVersion
        bb.putInt(666); // 4 CorrelationId

        bb.putShort((short) 4); // 2 ClientId-Length
        bb.put("test".getBytes()); // 4 ClientId

        bb.putInt(0); // 4 TopicMetadataRequest (empty array)

        return bb;
    }
}
