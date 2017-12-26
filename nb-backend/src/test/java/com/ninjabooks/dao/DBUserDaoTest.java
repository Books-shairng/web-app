package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBUserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.db.QueryFinder;

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
    private static final Supplier<Stream<User>> USER_STREAM_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.USER);
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.USER);

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
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.USER);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteByEnityUser() throws Exception {
        doNothing().when(sessionMock).delete(DomainTestConstants.USER);
        sut.delete(DomainTestConstants.USER);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.USER);
        Optional<User> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.USER);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getById(DomainTestConstants.ID);

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
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        assertThat(actual).isEmpty();
    }


    @Test
    public void testGetUserByEmailWhichNotExistShouldReturnEmptyOptional() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(Optional.empty());
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        when(queryMock.stream()).thenReturn(USER_STREAM_SUPPLIER.get());
        Stream<User> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.USER);
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
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        assertThat(actual).contains(DomainTestConstants.USER);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(USER_OPTIONAL);
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertThat(actual).contains(DomainTestConstants.USER);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    private User createFreshEntity() {
        return new User(DomainTestConstants.NAME, DomainTestConstants.EMAIL, DomainTestConstants.PASSWORD);
    }

}
