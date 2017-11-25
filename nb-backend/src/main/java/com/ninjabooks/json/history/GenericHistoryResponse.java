package com.ninjabooks.json.history;

import com.ninjabooks.dto.HistoryDto;

import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class GenericHistoryResponse implements Serializable
{
    private static final long serialVersionUID = -7702537035127045678L;

    private HistoryDto historyDto;

    public GenericHistoryResponse(HistoryDto historyDto) {
        this.historyDto = historyDto;
    }

    public HistoryDto getHistoryDto() {
        return historyDto;
    }

    public void setHistoryDto(HistoryDto historyDto) {
        this.historyDto = historyDto;
    }
}
