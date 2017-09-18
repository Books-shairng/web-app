package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @OneToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne
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
        return Objects.equals(orderDate, queue.orderDate) &&
            Objects.equals(book, queue.book) &&
            Objects.equals(user, queue.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, book, user);
    }

    @Override
    public String toString() {
        return "Queue{" +
            "orderDate=" + orderDate +
            ", book=" + book +
            ", user=" + user +
            '}';
    }
}
