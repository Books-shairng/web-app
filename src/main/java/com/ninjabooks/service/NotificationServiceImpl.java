package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements  NotificationService
{
    private final static Logger logger = LogManager.getLogger(NotificationService.class);

    private final UserDao userDao;

    @Autowired
    public NotificationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<BorrowNotification> findUserBorrows(Long userID) {
        User currentUser = findUser(userID);
        List<Borrow> borrows = currentUser.getBorrows();

        return borrows.stream()
            .map(BorrowNotification::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<QueueNotification> findUserQueues(Long userID) {
        User currentUser = findUser(userID);
        List<Queue> queues = currentUser.getQueues();

        return queues.stream()
            .map(QueueNotification::new)
            .collect(Collectors.toList());
    }

    private User findUser(Long id) {
        return userDao.getById(id);
    }

}
