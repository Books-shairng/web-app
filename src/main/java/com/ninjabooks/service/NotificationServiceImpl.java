package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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
            .filter(borrow -> borrow.getReturnDate() != null)
            .filter(borrow -> borrow.getBorrowDate() != null)
            .filter(borrow -> borrow.getIsBorrowed() == true)
            .map(BorrowNotification::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<QueueNotification> findUserQueues(Long userID) {
        User currentUser = findUser(userID);
        List<Queue> queues = currentUser.getQueues();

        return queues.stream()
            .map(queue -> new QueueNotification(queue, computePositionInQueue(queue, currentUser)))
            .collect(Collectors.toList());
    }

    private User findUser(Long id) {
        return userDao.getById(id);
    }

    private int computePositionInQueue(Queue queue, User user) {
        Long bookID = queue.getBook().getId();
        String query = "SELECT order_date, user_id FROM Queue WHERE status =:stat and book_id =:id";
        Query<?> queueQuery = userDao.getCurrentSession().createNativeQuery(query);
        queueQuery.setParameter("stat", true);
        queueQuery.setParameter("id", bookID);

        List<Object[]> queues = (List<Object[]>) queueQuery.list();

        for (Object[] objects : queues) {
            BigInteger bigInteger = (BigInteger) objects[1];
            if (bigInteger.equals(BigInteger.valueOf(user.getId())))
                return queues.indexOf(objects) + 1;
        }

        return 0;
    }
}
