package com.ninjabooks.service.rest.notification;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.EntityUtils;
import org.hibernate.query.NativeQuery;
import org.modelmapper.ModelMapper;
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
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public NotificationServiceImpl(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BorrowNotification> findUserBorrows(Long userID) {
        User currentUser = EntityUtils.getEnity(userService, userID);
        List<Borrow> borrows = currentUser.getBorrows();

        return borrows.stream()
            .filter(BaseEntity::getIsActive)
            .map(borrow -> new BorrowNotification(borrow, modelMapper))
            .collect(Collectors.toList());
    }

    @Override
    public List<QueueNotification> findUserQueues(Long userID) {
        User currentUser = EntityUtils.getEnity(userService, userID);
        List<Queue> queues = currentUser.getQueues();

        return queues.stream()
            .filter(BaseEntity::getIsActive)
            .map(queue -> new QueueNotification(queue, computePositionInQueue(queue, currentUser), modelMapper))
            .collect(Collectors.toList());
    }


    private int computePositionInQueue(Queue queue, User user) {
        final List<Object[]> queues = getMatchingQueues(queue);

        for (Object[] object : queues) {
            Long id = ((BigInteger) object[1]).longValue();
            if (id.equals(user.getId())) {
                return queues.indexOf(object) + 1;
            }
        }

        return 0;
    }

    private List<Object[]> getMatchingQueues(Queue queue) {
        Long bookID = queue.getBook().getId();
        String query = "SELECT order_date, user_id FROM Queue WHERE active =:stat and book_id =:id";
        NativeQuery queueQuery = userService.getSession().createNativeQuery(query);
        queueQuery.setParameter("stat", true);
        queueQuery.setParameter("id", bookID);

        return queueQuery.getResultList();
    }
}
