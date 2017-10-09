package com.ninjabooks.util;

import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.user.UserService;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class QueueUtils
{
    private final UserService userService;

    @Autowired
    public QueueUtils(UserService userService) {
        this.userService = userService;
    }

    public int computePositionInQueue(Queue queue, User user) {
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
