package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Queue;
import com.ninjabooks.dto.QueueDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.modelmapper.ModelMapper;

/**
 * This class extend {@link GenericNotification} and add information about:
 * - order date with hour
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueNotification extends GenericNotification
{
    private static final long serialVersionUID = -1474981638299034456L;

    @JsonUnwrapped
    @JsonIgnoreProperties(value = {"active", "id"})
    private final QueueDto queueDto;
    private final int positionInQueue;

    @JsonCreator
    public QueueNotification(Queue queue,
                             @JsonProperty(value = "positionInQueue") int positionInQueue,
                             ModelMapper modelMapper) {
        super(modelMapper, queue.getBook());
        this.queueDto = modelMapper.map(queue, QueueDto.class);
        this.positionInQueue = positionInQueue;
    }

    public QueueDto getQueueDto() {
        return queueDto;
    }

    public int getPositionInQueue() {
        return positionInQueue;
    }

}
