package com.ninjabooks.controller;

import com.ninjabooks.json.history.GenericHistoryResponse;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.history.HistoryRestService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */

@RestController
public class HistoryController
{
    private final HistoryRestService historyRestService;

    @Autowired
    public HistoryController(HistoryRestService historyRestService) {
        this.historyRestService = historyRestService;
    }

    @RequestMapping(value = "/api/history/{userID}/", method = RequestMethod.GET)
    public ResponseEntity<?> fetchUserHistory(@PathVariable(value = "userID") final Long userID,
                                              @RequestParam(value = "minusDays", required = false, defaultValue = "0") final Long minusDays) {
        List<GenericHistoryResponse> history = historyRestService.getHistory(minusDays, userID);
        if (history.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("User has no history"), HttpStatus.OK);
        }

        return ResponseEntity.ok(history);
    }
}
