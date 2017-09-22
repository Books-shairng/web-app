package com.ninjabooks.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ninjabooks.util.converter.json.LocalDateTimeDeserializer;
import com.ninjabooks.util.converter.json.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueDto extends BaseEntityDto
{
    private static final long serialVersionUID = 8784779997262803415L;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime orderDate;

    public QueueDto() {
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
