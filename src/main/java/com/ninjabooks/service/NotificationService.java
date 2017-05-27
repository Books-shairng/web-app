package com.ninjabooks.service;

import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;

import java.util.List;

/**
 * This service is responsible for return all user books and
 * queues.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface NotificationService
{
    /**
     * @param userID
     * @return
     */

    List<BorrowNotification> findUserBorrows(Long userID);

    /**
     * @param userID
     * @return
     */

    List<QueueNotification> findUserQueues(Long userID);

}
