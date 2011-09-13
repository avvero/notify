package org.walker.datasync.message;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.management.Notification;

import org.walker.datasync.notification.Event;

import org.walker.datasync.notification.NotificationListener_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

@MessageDriven(name = "NotifyMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/notification"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class NotifyMDB implements MessageListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onMessage(Message message) {
        Event event = null;
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            event = (Event) objectMessage.getObject();
        } catch (JMSException ex) {
            logger.error("JMS TextMessage Error", ex);
        }

        URL url = null;
        try {
            url = new URL("http://localhost:8080/notification/NotificationListener/NotificationListenerPort?wsdl");
        } catch (MalformedURLException ex) {
            logger.error("MalformedURL Error", ex);
        }
        NotificationListener_Service service = new NotificationListener_Service(url) ;


        service.getNotificationListenerPort().notify(event);

    }

}
