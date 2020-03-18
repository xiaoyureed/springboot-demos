package io.github.xiaoyureed.concurrentjava.soa.esb.mule;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class HelloWorldConsumer {
    private HelloWorld service=null;

    public void setService(HelloWorld service){
        this.service=service;
    }

    public HelloWorld getService(){
        return service;
    }

    public String execute(String name){
        String response=service.sayHello(name);
        System.out.println(response);
        return response;
    }
}
