package io.github.xiaoyureed.concurrentjava.soa.esb.mule;

import org.mule.api.MuleContext;
import org.mule.context.DefaultMuleContextFactory;

/**
 * Publish service
 *
 * check localhost:12345/services/HelloWorldService?wsdl for webservice descriptor of HelloWorldService
 *
 * @author xiaoyu
 * @since 1.0
 */
public class PublishService {
    public static void main(String[] args) throws Exception{
        MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("mule/publish_service.xml");
        muleContext.start();
        System.out.println("Server Started");
        Object object=new Object();
        synchronized (object) {
            object.wait();
        }
    }
}
