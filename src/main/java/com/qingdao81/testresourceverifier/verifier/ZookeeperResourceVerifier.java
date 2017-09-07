package com.qingdao81.testresourceverifier.verifier;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Resource verifier for zookeeper. Use {@link Watcher} to get notified about zookeeper events.
 *
 */
public class ZookeeperResourceVerifier extends AbstractResourceVerifier {

    private final CountDownLatch sync = new CountDownLatch(1);

    public ZookeeperResourceVerifier(ResourceVerifier verifier) {
        super(verifier);
    }

    @Override
    boolean doVerify() throws Exception {
        final ZooKeeper zk = new ZooKeeper(String.format("%s:%d", getHost(), getPort()), 3000,
                watchedEvent -> sync.countDown());
        sync.await(100, TimeUnit.MILLISECONDS);
        zk.close();
        return sync.getCount() == 0;
    }
}