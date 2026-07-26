package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.entity.BillingNotification;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.BillingNotificationRepository;
import emranhss.com.Modern_Hospital_Management_System.service.BillingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingNotificationServiceImp implements BillingNotificationService {

    private final BillingNotificationRepository billingNotificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BillingNotification> getAll() {
        return billingNotificationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingNotification> getUnread() {
        return billingNotificationRepository.findByIsRead(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        return billingNotificationRepository.countByIsRead(false);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        BillingNotification notification = billingNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        notification.setIsRead(true);
        billingNotificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        List<BillingNotification> unread = billingNotificationRepository.findByIsRead(false);
        unread.forEach(n -> n.setIsRead(true));
        billingNotificationRepository.saveAll(unread);
    }

    @Override
    @Transactional
    public BillingNotification createNotification(BillingNotification notification) {
        notification.setIsRead(false);
        return billingNotificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        if (!billingNotificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }
        billingNotificationRepository.deleteById(id);
    }
}
