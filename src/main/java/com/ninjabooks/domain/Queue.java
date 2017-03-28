package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    public Queue() {

    }

    /**
     * Create mew instace of queuee objet
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
}
