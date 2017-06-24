package com.ninjabooks.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
