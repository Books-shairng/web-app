package com.ninjabooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ninjabooks.domain.Book;
import com.ninjabooks.error.qrcode.QRCodeException;
import com.ninjabooks.json.book.BookInfResponse;
import com.ninjabooks.service.rest.book.BookRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class BookController
{
    private final BookRestService bookService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookController(BookRestService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.POST)
    public ResponseEntity<ObjectNode> addBook(@RequestBody Book book) throws QRCodeException {
        String generatedCode = bookService.addBook(book);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("generatedCode", generatedCode);

        return new ResponseEntity<>(objectNode, HttpStatus.CREATED);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/api/books/{bookID}", method = RequestMethod.GET)
    public BookInfResponse getBookInfo(@PathVariable Long bookID) {
//        Book book = bookService.(bookID).get();
//        return new BookInfResponse(book);
        return null;
    }
}
