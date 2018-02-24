package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBUserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.db.QueryFinder;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBUserDaoTest
{
    private static final String NEW_NAME = "Peter Datov";
    private static final Supplier<Stream<User>> USER_STREAM_SUPPLIER = CommonUtils.asSupplier(USER);
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(USER);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private QueryFinder specifiedElementFinderMock;

    private UserDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBUserDao(sessionFactoryMock, specifiedElementFinderMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddUser() throws Exception {
        when(sessionMock.save(any())).thenReturn(ID);
        sut.add(USER);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteByEnityUser() throws Exception {
        doNothing().when(sessionMock).delete(USER);
        sut.delete(USER);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(USER);
        Optional<User> actual = sut.getById(ID);

        assertThat(actual).contains(USER);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getById(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userBeforeUpdate = createFreshEntity();
        userBeforeUpdate.setName(NEW_NAME);
        doNothing().when(sessionMock).update(userBeforeUpdate);
        sut.update(userBeforeUpdate);

        verify(sessionMock, atLeastOnce()).update(any());
    }

    @Test
    public void testGetUserByNameWhichNotExistShouldReturnEmptyOptional() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(Optional.empty());
        Optional<User> actual = sut.getByName(NAME);

        assertThat(actual).isEmpty();
    }


    @Test
    public void testGetUserByEmailWhichNotExistShouldReturnEmptyOptional() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(Optional.empty());
        Optional<User> actual = sut.getByEmail(EMAIL);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        when(queryMock.stream()).thenReturn(USER_STREAM_SUPPLIER.get());
        Stream<User> actual = sut.getAll();

        assertThat(actual).containsExactly(USER);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllWhenDbIsEmptyShouldReturnsEmptyStream() throws Exception {
        Stream<User> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetUserByName() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(USER_OPTIONAL);
        Optional<User> actual = sut.getByName(NAME);

        assertThat(actual).contains(USER);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(USER_OPTIONAL);
        Optional<User> actual = sut.getByEmail(EMAIL);

        assertThat(actual).contains(USER);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    private User createFreshEntity() {
        return new User(NAME, EMAIL, PLAIN_PASSWORD);
    }

}
