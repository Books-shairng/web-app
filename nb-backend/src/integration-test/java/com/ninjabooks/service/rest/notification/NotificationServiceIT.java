package com.ninjabooks.service.rest.notification;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql")
public class NotificationServiceIT
{
    private static final int EXPTECTED_SIZE  = 1;

    @Autowired
    NotificationService sut;

    @Test
    public void testFindUserBorrowsShouldContainsExpectedSize() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(DomainTestConstants.ID);

        Assertions.assertThat(actual).hasSize(EXPTECTED_SIZE);
    }

    @Test
    public void testFindUserQuuesShouldContainsExpectedSize() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(DomainTestConstants.ID);

        Assertions.assertThat(actual).hasSize(EXPTECTED_SIZE);
    }
}

