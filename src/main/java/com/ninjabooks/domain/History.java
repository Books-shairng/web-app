package com.ninjabooks.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

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
public class History  extends BaseEntity
{
    public History() {
    }
}
