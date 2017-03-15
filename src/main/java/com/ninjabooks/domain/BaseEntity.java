package com.ninjabooks.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public BaseEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
