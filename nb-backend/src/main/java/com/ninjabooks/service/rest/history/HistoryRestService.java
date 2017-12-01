package com.ninjabooks.service.rest.history;

import com.ninjabooks.json.history.GenericHistoryResponse;

import java.util.List;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface HistoryRestService
{
    /**
     * Returns the history for the selected entity from the database.
     *
     * @param minusDaysFromToday - minus day of actual date to return history
     * @param entityID           - id of entity which should be extracted from database
     * @return - list which contains proper hirtory response
     */

    <R extends GenericHistoryResponse> List<R> getHistory(final long minusDaysFromToday, final Long entityID);
}
