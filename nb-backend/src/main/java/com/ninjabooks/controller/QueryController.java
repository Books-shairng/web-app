package com.ninjabooks.controller;

import com.ninjabooks.service.rest.database.DBQueryService;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
@Profile(value = {"dev", "test"})
public class QueryController
{
    private final DBQueryService dbQueryService;

    @Autowired
    public QueryController(DBQueryService dbQueryService) {
        this.dbQueryService = dbQueryService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/api/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String executeSQLQuery(@RequestParam(value = "q") String query) throws JsonProcessingException {
        List<Map<String, String>> result = dbQueryService.execute(query);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper.writeValueAsString(result);
    }
}
