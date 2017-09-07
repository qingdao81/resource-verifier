package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * Resource Verifier for samza jobs.
 *
 */
public class SamzaResourceVerifier extends AbstractResourceVerifier {

    public SamzaResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
    }

    @Override
    boolean doVerify() throws Exception {
        final ZooKeeper zk = new ZooKeeper(String.format("%s:%d", getHost(), getPort()), 3000,
                watchedEvent -> LOGGER.trace("got event"));
        final List<String> children = zk.getChildren("/brokers/topics", false);
        zk.close();
        for (final String child : children) {
            // checkpoint topic is created if the samza job is up and running
            // checkpointing must be enabled for the samza job
            if (child.contains("checkpoint")) {
                return true;
            }
        }
        return false;
    }
}
