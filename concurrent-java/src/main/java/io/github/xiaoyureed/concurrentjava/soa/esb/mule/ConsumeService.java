package io.github.xiaoyureed.concurrentjava.soa.esb.mule;

import org.mule.api.MuleContext;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.context.DefaultMuleContextFactory;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class ConsumeService {
    public static void main(String[] args) throws Exception{
        MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("mule/consume_service.xml");
        muleContext.start();

        //MuleClient client =new MuleClient();
        MuleClient client = new DefaultLocalMuleClient(muleContext);
        client.send("vm://helloworld.queue", "BlueDavy", null);

    }
}
