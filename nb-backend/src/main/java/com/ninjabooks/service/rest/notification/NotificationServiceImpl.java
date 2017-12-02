package com.ninjabooks.service.rest.notification;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.QueueUtils;
import com.ninjabooks.util.entity.EntityUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService
{
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final QueueUtils queueUtils;

    @Autowired
    public NotificationServiceImpl(UserService userService, ModelMapper modelMapper, QueueUtils queueUtils) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.queueUtils = queueUtils;
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
        return queueUtils.computePositionInQueue(queue, user);
    }
}
