package com.ninjabooks.controller;

import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.service.rest.search.SearchService;
import com.ninjabooks.util.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class SearchController
{
    private static final Logger logger = LogManager.getLogger(SearchController.class);

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @RequestMapping(value = "/api/search/{query}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<BookDto>>> searchBook(@PathVariable String query) {
        logger.info("An attempt to find the following book: {}", query);
//        List<BookDto> searchResult = searchService.search(query).collect(Collectors.toList());
        List<Book> searchResult = searchService.search(query);

        if (searchResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Map<String, List<BookDto>> searchResponse =
            Collections.singletonMap("searchResult", CommonUtils.domainObjectAsDto(searchResult, BookDto.class));

        return ResponseEntity.ok(searchResponse);

//            return new ResponseEntity<>(searchResponse, HttpStatus.NO_CONTENT);

    }
}
