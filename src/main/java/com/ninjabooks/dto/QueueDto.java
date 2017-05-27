package com.ninjabooks.dto;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueDto
{
    private String orderDate;

    public QueueDto() {
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate.toString();
    }
}
