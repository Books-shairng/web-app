package com.ninjabooks.util;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.History;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.generic.GenericService;
import com.ninjabooks.util.entity.EntityUtils;

import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW;
import static com.ninjabooks.util.constants.DomainTestConstants.HISTORY;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.QR_CODE;
import static com.ninjabooks.util.constants.DomainTestConstants.QUEUE;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class EntityUtilsTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private GenericService<? extends BaseEntity, Long> genericServiceMock;

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Query queryMock;

    @Mock
    private Session sessionMock;

    @Before
    public void setUp() throws Exception {
        new EntityUtils().setSessionFactory(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testGetEntityShouldReturnUserObject() throws Exception {
        Optional user = Optional.ofNullable(USER);
        when(genericServiceMock.getById(ID)).thenReturn(user);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(User.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnBookObject() throws Exception {
        Optional book = Optional.ofNullable(BOOK);
        when(genericServiceMock.getById(ID)).thenReturn(book);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(Book.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnBorrowObject() throws Exception {
        Optional borrow = Optional.ofNullable(BORROW);
        when(genericServiceMock.getById(ID)).thenReturn(borrow);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(Borrow.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnHistoryObject() throws Exception {
        Optional history = Optional.ofNullable(HISTORY);
        when(genericServiceMock.getById(ID)).thenReturn(history);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(History.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnQRCodeObject() throws Exception {
        Optional qrCode = Optional.ofNullable(QR_CODE);
        when(genericServiceMock.getById(ID)).thenReturn(qrCode);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(QRCode.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnQueueObject() throws Exception {
        Optional user = Optional.ofNullable(QUEUE);
        when(genericServiceMock.getById(ID)).thenReturn(user);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, ID);

        assertThat(actual).isInstanceOf(Queue.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldThrowsExceptionWhenObjectNotPresent() throws Exception {
        when(genericServiceMock.getById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> EntityUtils.getEnity(genericServiceMock, ID))
            .withNoCause()
            .withMessageContaining("not found");

        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityByClassShouldReturnUserObject() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.of(USER));
        User actual = EntityUtils.getEnity(User.class, ID);

        assertThat(actual).isInstanceOf(User.class);
        verify(sessionFactoryMock, atLeastOnce()).getCurrentSession();
        verify(sessionMock, atLeastOnce()).createQuery(any(), any());
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
    }

    @Test
    public void testGetEntityByClassShouldThrowsExceptionWhenObjectNotPresent() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> EntityUtils.getEnity(User.class, ID))
            .withNoCause()
            .withMessageContaining("not found");

        verify(sessionFactoryMock, atLeastOnce()).getCurrentSession();
        verify(sessionMock, atLeastOnce()).createQuery(any(), any());
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
    }
}
