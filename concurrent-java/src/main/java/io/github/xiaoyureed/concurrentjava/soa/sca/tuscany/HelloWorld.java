package io.github.xiaoyureed.concurrentjava.soa.sca.tuscany;

import org.oasisopen.sca.annotation.Remotable;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Remotable
public interface HelloWorld {
    String sayHello(String name);
}
