package com.ninjabooks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * This class represent queue in database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Entity
@Table(name = "Queue")
public class Queue extends BaseEntity
{
    @Column(name = "order_date")
    private LocalDate oderDate;

    public Queue() {

    }

    /**
     * Create mew instace of queuee objet
     * @param oderDate date when user order book
     */

    public Queue(LocalDate oderDate) {
        this.oderDate = oderDate;
    }

    public LocalDate getOderDate() {
        return oderDate;
    }

    public void setOderDate(LocalDate oderDate) {
        this.oderDate = oderDate;
    }
}
