package com.ninjabooks.scheduler;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.generic.GenericService;
import com.ninjabooks.service.dao.queue.QueueService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class DBCleanScheduler extends AbstractDBScheduler
{
    private static final Logger logger = LogManager.getLogger(DBCleanScheduler.class);
    private static final LocalDateTime NEXT_EXECUTION =
        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(3, 30, 0));

    private final QueueService queueService;
    private final BorrowService borrowService;

    @Autowired
    public DBCleanScheduler(QueueService queueService, BorrowService borrowService) {
        this.queueService = queueService;
        this.borrowService = borrowService;
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void borrowTableCleanupTask() {
        execute(borrowService, Borrow.class);
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void queueTableCleanupTask() {
        execute(queueService, Queue.class);
    }

    private <E extends BaseEntity> void execute(GenericService<E, Long> genericService, Class<E> clazz) {
        logger.info("Execute scheduled cleanup db task for table: {}", clazz.getSimpleName());
        executeTask(genericService);
        logger.info("Successfully cleanup table, next task scheduled at: {}", NEXT_EXECUTION);
    }

    @Override
    protected <E extends BaseEntity> void executeTask(GenericService<E, Long> genericService) {
        genericService.getAll()
            .parallel()
            .filter(e -> !e.getIsActive())
            .distinct()
            .forEach(genericService::remove);
    }
}
