package org.motechproject.testaggregation;

import org.motechproject.aggregator.inbound.EventAggregationGateway;
import org.motechproject.model.MotechEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestApplication {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test-aggregator.xml");
        EventAggregationGateway aggregationGateway = (EventAggregationGateway) context.getBean("messageGateway");

        System.out.println("Send ABC");
        aggregationGateway.dispatch(new MotechEvent("ABC"));
        Thread.sleep(3000);

        System.out.println("Send ABCD");
        aggregationGateway.dispatch(new MotechEvent("ABCD"));

        System.out.println("Send ABC");
        aggregationGateway.dispatch(new MotechEvent("ABC"));
        Thread.sleep(3000);

        System.out.println("Send ABCD");
        aggregationGateway.dispatch(new MotechEvent("ABCD"));
    }
}
