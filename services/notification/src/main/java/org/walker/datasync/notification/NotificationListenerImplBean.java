package org.walker.datasync.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(name = "NotificationListener",
            portName="NotificationListenerPort",
            serviceName="NotificationListener",
			endpointInterface = "org.walker.datasync.notification.NotificationListener",
			targetNamespace = "http://notification.datasync.walker.org",
			wsdlLocation = "META-INF/wsdl/NotificationListener.wsdl")
public class NotificationListenerImplBean implements NotificationListener {
    private final Logger logger = LoggerFactory.getLogger(NotificationListenerImplBean.class);

    public NotificationListenerImplBean() {
    }

    public void notify(Event event) {
        StringBuffer eventMessage = new StringBuffer("org.walker.datasync.notification.Event[ ");
        eventMessage.append("eventLedgerId=").append(event.getEventLedgerId()).append(", ");
        eventMessage.append("primaryObjectNameTable=").append(event.getPrimaryObjectNameTable()).append(", ");
        eventMessage.append("primaryObjectNameId=").append(event.getPrimaryObjectNameId()).append(", ");
        eventMessage.append("eventActionCode=").append(event.getEventActionCode()).append(", ");
        eventMessage.append("createdDate=").append(event.getCreatedDate()).append(", ");
        eventMessage.append("createByUserLogin=").append(event.getCreateByUserLogin()).append(" ]");
        logger.info(eventMessage.toString());
    }
}
