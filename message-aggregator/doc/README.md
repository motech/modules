Module: message-aggregator
===========================

What is it? Why is it needed?
-----------------------------

This is a module which implements an integration pattern we've noticed a few times. The pattern is: Aggregation of a
stream of messages which keep coming in, till a certain point in time. Once this time is reached, the aggregated
messages need to be dispatched.

For instance, this can be used to aggregate alerts for a week and then, at a pre-defined time of the week, these alerts
can be combined into one or many SMSes and dispatched.

One other reason for doing this is that configuration of spring-integration is not very easy to get right the first
time. So, this module is an attempt to reduce the effort taken to do this.

How does it work?
-----------------

This is a fairly standard implementation of the
"[aggregator](http://static.springsource.org/spring-integration/reference/html/overview.html#overview-endpoints-aggregator)"
pattern in spring-integration. So, much of the implementation revolves around [this configuration
file](https://github.com/motech/motech-contrib/blob/master/message-aggregator/src/main/resources/applicationContext-messaging-gateway.xml).

1. There is a channel for input messages (called "inputMessages") and a message gateway, implemented by the
   [EventAggregationGateway](https://github.com/motech/motech-contrib/blob/master/message-aggregator/src/main/java/org/motechproject/aggregator/inbound/EventAggregationGateway.java) class, which writes to the inputMessages channel.

2. The messages which have been input in this way gets stored in a JDBCMessageStore, implemented by the
   [MessageStore](https://github.com/motech/motech-contrib/blob/master/message-aggregator/src/main/java/org/motechproject/aggregator/repository/MessageStore.java)
   class. This uses a data source configured in the above-mentioned configuration file.

3. There is a "reaper" which is used to "expire" (or dispatch) the aggregated messages. The reaper is triggered by a
   CronTriggerBean instance (a bean called "reaperTrigger"). So, at a predetermined time (configurable), the reaper will
   be triggered. It will aggregate the messages in the MessageStore using the
   [MessageDispatcher#aggregateEvents method](https://github.com/motech/motech-contrib/blob/master/message-aggregator/src/main/java/org/motechproject/aggregator/aggregation/MessageDispatcher.java)
   and then will dispatch the aggregated messagae using the
   [AggregatedMessageDispatcher](https://github.com/motech/motech-contrib/blob/master/message-aggregator/src/main/java/org/motechproject/aggregator/outbound/AggregatedMessageDispatcher.java).

4. This creates a MotechEvent containing all the individual messages, which any client can listen to.

What do I need to do to make it work?
-------------------------------------

1. In your Spring application context file, you need to have this import:

          <import resource="classpath:applicationContext-messaging-gateway.xml"/>

2. You can now get a bean named "messageGateway", of class
   EventAggregationGateway and send messages through it, to be aggregated.
   Suppose you are aggregating String objects, you can do something like this:

          EventAggregationGateway<String> gateway = (EventAggregationGateway) context.getBean("messageGateway");
          gateway.dispatch("ABCD");
          gateway.dispatch("123456");
          gateway.dispatch("cafe");

   You can use any serializable object instead of "String" in the code above.

3. You can now decide how aggregation should be done (or, what kind of buckets to put the individual messages into). For
   instance, considering the above example, if you want to aggregate the messages by size of the string, such that all
   4-character strings will be aggregated together and all 6-character strings will be aggregated together, you can do
   this:

          @Component
          public class AggregationBySubject implements AggregationHandler<String> {
              @Override
              public String groupId(String message) {
                  return String.valueOf(message.length());
              }
              @Override
              public boolean canBeDispatched(List<String> messages) {
                  return true;
              }
          }

   The message-aggregator module expects an implementation of AggregationHandler in the spring context, which
   implements the groupId() and canBeDispatched() methods.

   * The **groupId()** method is used to find the group of a given message. In this case, since message length is used
     for grouping, all messages of the same length will have the same group ID and so, will be aggregated.

   * The **canBeDispatched()** method gives the client of this module an ability to decide whether this group of
     messages can be dispatched at this time. This callback method is called for every group of messages every time the
     CronTriggerBean is configured to run the reaper.

4. Finally, using the usual motech-platform configurations, you need to implement the motech listener for the aggregated
   event, like so:

          @Component
          public class Dispatcher {
              @MotechListener(subjects = AggregateMotechEvent.SUBJECT)
              public void dispatch(MotechEvent aggregatedEvent) {
                  List<String> events = (List<String>) aggregatedEvent.getParameters().get(AggregateMotechEvent.VALUES_KEY);
                  System.out.println("Aggregated events: " + events);
              }
          }

    In the case of the example shown below, you will receive 2 event callbacks here. One with the messages, "ABCD" and
    "cafe", and one more with just the message "123456".

5. Coniguration files you'll need to setup in the classpath:

   * Usual motech-platform configuration files: activemq.properties, couchdb.properties, osgi.properties and
     quartz.properties.

   * aggregator.properies: This is the properties file which defines the configurations for the message store and the
     cron expression for the aggregation. Here's an example of it:

            aggregator.host=localhost
            aggregator.database=motechaggregator
            aggregator.db.driver=com.mysql.jdbc.Driver
            aggregator.db.username=root
            aggregator.db.password=password
            aggregator.db.connection.string=jdbc:mysql://${aggregator.host}:3306/${aggregator.database}?autoReconnect=true
            aggregator.cron.expression=* * * ? * *

That's it!
