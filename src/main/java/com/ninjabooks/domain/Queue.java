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

    @Column(name = "STATUS")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Queue() {
        this.isActive = true;
    }

    /**
     * Create mew instace of queuee objet
     *
     * @param orderDate date when user order book
     */

    public Queue(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        this.isActive = true;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue = (Queue) o;
        return isActive == queue.isActive &&
            Objects.equals(orderDate, queue.orderDate) &&
            Objects.equals(book, queue.book) &&
            Objects.equals(user, queue.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, isActive, book, user);
    }

    @Override
    public String toString() {
        return "Queue{" +
            "orderDate=" + orderDate +
            ", isActive=" + isActive +
            ", book=" + book +
            ", user=" + user +
            '}';
    }
}
