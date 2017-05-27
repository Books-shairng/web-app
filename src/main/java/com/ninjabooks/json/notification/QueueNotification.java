package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Queue;

/**
 * This class extend {@link GenericNotification} and add information about:
 * - order date with hour
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class QueueNotification extends GenericNotification
{
    private String orderDate;

    public QueueNotification(Queue queue) {
        convertDataToString(queue);
        obtainBookFromGenericType(queue.getBook());
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    private void convertDataToString(Queue queue) {
        orderDate = queue.getOrderDate().toString();
    }

}
