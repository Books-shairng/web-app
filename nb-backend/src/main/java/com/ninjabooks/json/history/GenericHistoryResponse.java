package com.ninjabooks.json.history;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.History;
import com.ninjabooks.dto.HistoryDto;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class GenericHistoryResponse implements Serializable
{
    private static final long serialVersionUID = -7702537035127045678L;

    @JsonUnwrapped
    private HistoryDto historyDto;

    public GenericHistoryResponse(History history, ModelMapper modelMapper) {
        historyDto = modelMapper.map(history, HistoryDto.class);
    }

    public HistoryDto getHistoryDto() {
        return historyDto;
    }

    public void setHistoryDto(HistoryDto historyDto) {
        this.historyDto = historyDto;
    }
}
