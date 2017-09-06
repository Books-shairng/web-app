package com.ninjabooks.service.rest.search;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Component;

/**
 * Only purpose of exist class is working with Mockito Test API.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class SearchWrapper
{
    public FullTextSession search(Session session) {
        return Search.getFullTextSession(session);
    }
}
