package com.ninjabooks.service.rest.search;

import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.book.BookDaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.Query;
import org.hibernate.CacheMode;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class BookSearchServiceImpl implements SearchService
{
    private static final Logger logger = LogManager.getLogger(BookSearchServiceImpl.class);
    private static final String[] FIELDS = {"title", "author", "isbn", "allFields"};

    private final BookDaoService bookService;
    private final SearchWrapper searchWrapper;

    @Autowired
    public BookSearchServiceImpl(BookDaoService bookService, SearchWrapper searchWrapper) {
        this.bookService = bookService;
        this.searchWrapper = searchWrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> search(String query) {
        logger.info("An attempt to find the following book: {}", query);

        FullTextSession fullTextSession = searchWrapper.search(bookService.getSession());
        performQueryIndexing(fullTextSession);
        QueryBuilder queryBuilder = createQueryBuilder(fullTextSession);
        Query luceneQuery = createLuceneQuery(query, queryBuilder);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
        logProperMessage(fullTextQuery.getResultSize());

        return fullTextQuery.getResultList();
    }

    private QueryBuilder createQueryBuilder(FullTextSession fullTextSession) {
        return fullTextSession.getSearchFactory()
            .buildQueryBuilder()
            .forEntity(Book.class)
            .get();
    }

    private Query createLuceneQuery(String query, QueryBuilder queryBuilder) {
        return queryBuilder
            .bool()
            .should(queryBuilder.keyword().fuzzy().onFields(FIELDS).matching(query).createQuery())
            .createQuery();
    }

    private void performQueryIndexing(FullTextSession fullTextSession) {
        try {
            fullTextSession
                .createIndexer(Book.class)
                .typesToIndexInParallel(2)
                .batchSizeToLoadObjects(25)
                .cacheMode(CacheMode.NORMAL)
                .threadsToLoadObjects(5)
                .idFetchSize(150)
                .startAndWait();
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    private void logProperMessage(int resultSize) {
        if (resultSize > 0)
            logger.info("Found {} records", resultSize);
        else
            logger.info("No match records found");
    }
}
