package com.qingdao81.resourceverifier.verifier;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Resource verifier for socket resources.
 *
 */
public class SocketResourceVerifier extends AbstractResourceVerifier {

    protected ByteBuffer initialHandshakeMessage = ByteBuffer.wrap("HELLO".getBytes(StandardCharsets.UTF_8));

    public SocketResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
    }

    public SocketResourceVerifier withInitialHandshakeMessage(String initialHandshakeMessage) {
        this.initialHandshakeMessage = ByteBuffer.wrap(initialHandshakeMessage.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    @Override
    boolean doVerify() throws Exception {
        try (final SocketChannel s = SocketChannel.open(new InetSocketAddress(getHost(), getPort()))) {
            initialHandshakeMessage.rewind();
            s.write(initialHandshakeMessage);
            if (s.read(ByteBuffer.allocate(100)) < 1) {
                return false;
            }
            return true;
        }
    }
}
