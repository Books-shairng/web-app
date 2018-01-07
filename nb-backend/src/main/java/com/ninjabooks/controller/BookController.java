package com.ninjabooks.controller;

import com.ninjabooks.domain.Book;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.json.book.BookInfo;
import com.ninjabooks.json.book.BookRequest;
import com.ninjabooks.service.rest.book.BookRestService;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class BookController
{
    private final BookRestService bookRestService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookController(BookRestService bookRestService, ObjectMapper objectMapper) {
        this.bookRestService = bookRestService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/api/book/", method = RequestMethod.POST)
    public ResponseEntity<ObjectNode> addBook(@Valid @RequestBody BookRequest bookRequest) throws QRCodeException {
        Book book = new ModelMapper().map(bookRequest, Book.class);
        String generatedCode = bookRestService.addBook(book);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("generatedCode", generatedCode);

        return new ResponseEntity<>(objectNode, HttpStatus.CREATED);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/api/book/{bookID}", method = RequestMethod.GET)
    public BookInfo getBookInfo(@PathVariable Long bookID) {
        return bookRestService.getBookInfo(bookID);
    }
}
