package com.ninjabooks.domain;

import javax.persistence.*;

/**
 * This class represent history table in database.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 *
 * <b> EMPTY </b>
 */
@Entity
@Table(name = "HISTORY")
public class History extends BaseEntity
{

    public History() {
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + getId() +
                '}';
    }
}
