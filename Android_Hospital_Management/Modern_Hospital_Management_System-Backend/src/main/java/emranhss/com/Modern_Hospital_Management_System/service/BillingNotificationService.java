package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingNotification;

import java.util.List;

public interface BillingNotificationService {

    List<BillingNotification> getAll();

    List<BillingNotification> getUnread();

    Long getUnreadCount();

    void markAsRead(Long id);

    void markAllAsRead();

    BillingNotification createNotification(BillingNotification notification);

    void deleteNotification(Long id);
}
