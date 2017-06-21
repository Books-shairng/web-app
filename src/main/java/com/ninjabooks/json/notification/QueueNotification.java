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
    private int positionInQueue;

    public QueueNotification(Queue queue, int positionInQueue) {
        convertDateToString(queue);
        obtainBookFromGenericType(queue.getBook());
        this.positionInQueue = positionInQueue;
    }

    public int getPositionInQueue() {
        return positionInQueue;
    }

    public void setPositionInQueue(int positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    private void convertDateToString(Queue queue) {
        orderDate = queue.getOrderDate().toString();
    }

}
