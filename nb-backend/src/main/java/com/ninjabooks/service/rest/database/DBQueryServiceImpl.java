package com.ninjabooks.service.rest.database;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Profile(value = {"dev", "test"})
@Transactional
public class DBQueryServiceImpl implements DBQueryService
{
    private static final String EXECUTE_RESULT = "EXECUTE RESULT";

    private final Pattern pattern = Pattern.compile("SELECT|select");
    private final SessionFactory sessionFactory;

    @Autowired
    public DBQueryServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Map<String, String>> execute(String query) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery nativeQuery = session.createNativeQuery(query);

        return isSelectableQuery(query) ?
            performSelection(nativeQuery) :
            executeQuery(nativeQuery);
    }

    private boolean isSelectableQuery(String query) {
        return pattern.matcher(query).find();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> performSelection(NativeQuery<?> nativeQuery) {
        return (List<Map<String, String>>) nativeQuery
            .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            .getResultList();
    }

    private List<Map<String, String>> executeQuery(NativeQuery<?> nativeQuery) {
        int result = nativeQuery.executeUpdate();
        Map<String, String> executionResult = Collections.singletonMap(EXECUTE_RESULT, String.valueOf(result));
        return Collections.singletonList(executionResult);
    }
}

