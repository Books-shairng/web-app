package com.ninjabooks.util;

import com.ninjabooks.domain.*;
import com.ninjabooks.service.dao.generic.GenericService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class EntityUtilsTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private GenericService<? extends BaseEntity, Long> genericServiceMock;

    @Test
    public void testGetEntityShouldReturnUserObject() throws Exception {
        Optional user = Optional.ofNullable(DomainTestConstants.USER);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(user);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(User.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnBookObject() throws Exception {
        Optional book = Optional.ofNullable(DomainTestConstants.BOOK);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(book);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(Book.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnBorrowObject() throws Exception {
        Optional borrow = Optional.ofNullable(DomainTestConstants.BORROW);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(borrow);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(Borrow.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnHistoryObject() throws Exception {
        Optional history = Optional.ofNullable(DomainTestConstants.HISTORY);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(history);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(History.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());

    }

    @Test
    public void testGetEntityShouldReturnQRCodeObject() throws Exception {
        Optional qrCode = Optional.ofNullable(DomainTestConstants.QR_CODE);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(qrCode);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(QRCode.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldReturnQueueObject() throws Exception {
        Optional user = Optional.ofNullable(DomainTestConstants.QUEUE);
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(user);

        BaseEntity actual = EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID);

        assertThat(actual).isInstanceOf(Queue.class);
        verify(genericServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetEntityShouldThrowsExceptionWhenObjectNotPresent() throws Exception {
        when(genericServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> EntityUtils.getEnity(genericServiceMock, DomainTestConstants.ID))
            .withNoCause()
            .withMessageContaining("not found");

        verify(genericServiceMock, atLeastOnce()).getById(any());
    }
}
