package com.ninjabooks.dto;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class BaseEntityDto
{
    private Long id;

    public BaseEntityDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
