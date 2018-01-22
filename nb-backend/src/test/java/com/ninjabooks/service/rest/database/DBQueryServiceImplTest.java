package com.ninjabooks.service.rest.database;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBQueryServiceImplTest
{
    private static final String SELECT_QUERY = "SELECT * FROM USER";
    private static final String INSERT_QUERY = "INSERT INTO USER (ID) VALUES (1)";
    private static final String EXPECTED_INSERT_RESULT = "0";
    private static final Map<String, String> EXPECTED_SELECTION_RESULT =
        Collections.singletonMap("RESULT", "SELECT QUERY");
    public static final String MALFORMED_SQL_QUERY = "THIS IS TEXT";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private NativeQuery nativeQueryMock;

    @Mock
    private Query queryMock;

    private DBQueryService sut;

    @Before
    public void setUp() throws Exception {
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createNativeQuery(any())).thenReturn(nativeQueryMock);
        this.sut = new DBQueryServiceImpl(sessionFactoryMock);
    }

    @Test
    public void testExecuteWithSelectQueryShouldPerformSelectFromDB() throws Exception {
        when(nativeQueryMock.setResultTransformer(any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.singletonList(EXPECTED_SELECTION_RESULT));
        List<Map<String, String>> actual = sut.execute(SELECT_QUERY);

        assertThat(actual).containsOnly(EXPECTED_SELECTION_RESULT);
        verify(sessionFactoryMock, atLeastOnce()).getCurrentSession();
        verify(sessionMock, atLeastOnce()).createNativeQuery(anyString());
        verify(nativeQueryMock, atLeastOnce()).setResultTransformer(any());
        verify(queryMock, atLeastOnce()).getResultList();
    }

    @Test
    public void testExecuteWithInsertQueryShouldPerformInsertIntoDB() throws Exception {
        List<Map<String, String>> actual = sut.execute(INSERT_QUERY);

        assertThat(actual)
            .flatExtracting(Map::entrySet)
            .contains(entry("EXECUTE RESULT", EXPECTED_INSERT_RESULT));
        verify(sessionFactoryMock, atLeastOnce()).getCurrentSession();
        verify(sessionMock, atLeastOnce()).createNativeQuery(anyString());
    }

    @Test
    public void testExecuteWithMalformedSQLQueryShouldThrowsException() throws Exception {
        doThrow(SQLGrammarException.class).when(sessionMock).createNativeQuery(MALFORMED_SQL_QUERY);

        assertThatExceptionOfType(SQLGrammarException.class)
            .isThrownBy(() -> sut.execute(MALFORMED_SQL_QUERY))
            .withNoCause();

        verify(sessionMock, atLeastOnce()).createNativeQuery(MALFORMED_SQL_QUERY);
    }
}
