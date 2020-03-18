package io.github.xiaoyureed.concurrentjava.soa.sca.tuscany;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class HelloWorldConsumer {
    private HelloWorld service=null;

    public void setService(HelloWorld service){
        this.service=service;
    }

    public String execute(String name){
        return service.sayHello(name);
    }
}
