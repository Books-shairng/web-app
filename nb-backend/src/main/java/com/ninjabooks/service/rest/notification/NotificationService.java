package com.ninjabooks.service.rest.notification;

import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;

import java.util.List;

/**
 * This service is responsible for return all user books and queues.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface NotificationService
{
    /**
     * Search all borrows related with user.
     *
     * @param userID - specifies the user to search for
     * @return list with borrow notifications
     */

    List<BorrowNotification> findUserBorrows(Long userID);

    /**
     * Search all queues related with user.
     *
     * @param userID - specifies the user to search for
     * @return list with borrow notifications
     */

    List<QueueNotification> findUserQueues(Long userID);

}
