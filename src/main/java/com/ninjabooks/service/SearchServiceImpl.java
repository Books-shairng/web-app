package com.ninjabooks.service;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.Query;
import org.hibernate.CacheMode;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.modelmapper.ModelMapper;
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
public class SearchServiceImpl implements SearchService
{
    private final static Logger logger = LogManager.getLogger(SearchServiceImpl.class);

    private final String[] FIELDS = {"title", "author", "isbn"};

    private final BookDao bookDao;
    private final ModelMapper modelMapper;

    @Autowired
    public SearchServiceImpl(BookDao bookDao, ModelMapper modelMapper) {
        this.bookDao = bookDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BookDto> searchBook(String query) {
        logger.info("An attempt to find the following book:" + query);
        FullTextSession fullTextSession = Search.getFullTextSession(bookDao.getCurrentSession());
        indexQueries(fullTextSession);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder()
            .forEntity(Book.class).get();

        Query luceneQuery = createLuceneQuery(query, queryBuilder);
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

        List<Book> result = fullTextQuery.list();
        if (!result.isEmpty())
            logger.warn("No match record found");
        else
            logger.info("Found {" + result.size() + "} records");

        return result.stream()
            .map(book -> modelMapper.map(book, BookDto.class))
            .collect(Collectors.toList());
    }

    private Query createLuceneQuery(String query, QueryBuilder queryBuilder) {
        return queryBuilder
                .bool()
                .should(queryBuilder.keyword().fuzzy().onField("searchField").matching(query).createQuery())
                .should(queryBuilder.keyword().fuzzy().onFields(FIELDS).matching(query).createQuery())
                .createQuery();
    }

    private void indexQueries(FullTextSession fullTextSession) {
        try {
            fullTextSession
                .createIndexer(Book.class)
                .typesToIndexInParallel(2)
                .batchSizeToLoadObjects(25)
                .cacheMode(CacheMode.NORMAL )
                .threadsToLoadObjects(5)
                .idFetchSize(150)
                .startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
