package com.ninjabooks.controller;

import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.search.SearchService;
import com.ninjabooks.util.CommonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/api/search/", method = RequestMethod.GET)
    public ResponseEntity<?> searchBook(@RequestParam(value = "query") String query) {
        logger.info("An attempt to find the following book: {}", query);
        List<Book> searchResult = searchService.search(query);

        if (searchResult.isEmpty()) {
            String message = "Unfortunately search phrase not found";
            return ResponseEntity.ok(new MessageResponse(message));
        }

        Map<String, List<BookDto>> searchResponse =
            Collections.singletonMap("searchResult", CommonUtils.domainObjectAsDto(searchResult, BookDto.class));

        return ResponseEntity.ok(searchResponse);
    }
}
