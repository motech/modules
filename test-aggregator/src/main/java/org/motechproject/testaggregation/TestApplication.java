package org.motechproject.testaggregation;

import org.motechproject.aggregator.inbound.EventAggregationGateway;
import org.motechproject.model.MotechEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestApplication {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext-test-aggregator.xml");
        EventAggregationGateway<MotechEvent> aggregationGateway = (EventAggregationGateway) context.getBean("messageGateway");

        send(aggregationGateway, "ABC");
        send(aggregationGateway, "ABCD");
        send(aggregationGateway, "ABC");
        send(aggregationGateway, "ABCD");
    }

    private static void send(EventAggregationGateway aggregationGateway, final String subject) throws InterruptedException {
        System.out.println("Send " + subject);
        aggregationGateway.dispatch(new MotechEvent(subject));
        Thread.sleep(3000);
    }
}
