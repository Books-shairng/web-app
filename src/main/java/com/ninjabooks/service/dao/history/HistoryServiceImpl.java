package com.ninjabooks.service.dao.history;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class HistoryServiceImpl extends GenericServiceImpl<History, Long> implements HistoryService
{
    private final HistoryDao historyDao;

    @Autowired
    public HistoryServiceImpl(GenericDao<History, Long> genericDao, HistoryDao historyDao) {
        super(genericDao);
        this.historyDao = historyDao;
    }
}
