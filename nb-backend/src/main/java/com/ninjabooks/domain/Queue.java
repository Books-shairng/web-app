package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * This class represent queue in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "QUEUE")
public class Queue extends BaseEntity
{
    @Column(name = "ORDER_DATE")
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Queue() {
    }

    /**
     * Create mew instace of queuee objet
     *
     * @param orderDate date when user order book
     */

    public Queue(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue = (Queue) o;
        return
            Objects.equals(this.getId(), queue.getId()) &&
            Objects.equals(orderDate, queue.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, book, user);
    }

    @Override
    public String toString() {
        return "Queue{" +
            "orderDate=" + orderDate +
            '}';
    }
}
