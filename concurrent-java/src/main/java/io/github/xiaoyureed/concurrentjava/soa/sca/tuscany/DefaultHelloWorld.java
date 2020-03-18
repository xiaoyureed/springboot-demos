package io.github.xiaoyureed.concurrentjava.soa.sca.tuscany;

import io.github.xiaoyureed.concurrentjava.soa.esb.mule.HelloWorld;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class DefaultHelloWorld implements HelloWorld {
    @Override
    public String sayHello(String name) {
        System.out.println("Server receive: " + name);
        //throw new IllegalArgumentException("TEST");
        return "Server response: Hello "+name;
    }
}
