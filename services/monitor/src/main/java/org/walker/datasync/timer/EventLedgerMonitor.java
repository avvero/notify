package org.walker.datasync.timer;


import org.walker.datasync.notification.Event;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;
import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EventLedgerMonitor {
    @Resource(mappedName="java:/queue/notification")
    private Queue notificationQueue;

    @Resource(name="java:/ConnectionFactory")
    private QueueConnectionFactory qcf;

    private final Logger logger = LoggerFactory.getLogger(EventLedgerMonitor.class);

    @Schedule(second="*/30", minute="*",hour="*", persistent=false)
    public void checkEventLedger() {
        QueueConnection connection = null;
        QueueSession session = null;
        MessageProducer publisher = null;

        List<Event> events = findUnsyncedEvents();

        if (!events.isEmpty()) {
            try {
                connection = qcf.createQueueConnection();
                session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                publisher = session.createProducer(notificationQueue);

                for (Event e: events) {
                    ObjectMessage message = session.createObjectMessage(e);
                    publisher.send(message);
                }
            } catch (JMSException ex) {
                logger.error("JMS Error", ex);
            } finally {
                closeJMSResources(connection, session, publisher);
            }
        }
    }

    private void closeJMSResources(QueueConnection connection, QueueSession session, MessageProducer publisher) {
        try {
            publisher.close();
        } catch (JMSException e) {
            logger.error("Error Closing MessageProducer", e);
        }

        try {
            session.close();
        } catch (JMSException e) {
            logger.error("Error Closing QueueSession", e);
        }

        try {
            connection.close();
        } catch (JMSException e) {
            logger.error("Error Closing QueueConnection", e);
        }
    }

	private List<Event> findUnsyncedEvents() {
		//dummy code
		Event e1 = new Event();
		Event e2 = new Event();
		List<Event> eventList = new ArrayList<Event>();
		eventList.add(e1);
		eventList.add(e2);
		return eventList;
		
	}
}
