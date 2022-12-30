package com.example.library.studentlibrary.controller;

import com.example.library.studentlibrary.models.Author;
import com.example.library.studentlibrary.models.Book;
import com.example.library.studentlibrary.models.Genre;
import com.example.library.studentlibrary.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add required annotations

@RestController
public class BookController {


    @Autowired
    BookService bookService;

    //Write createBook API with required annotations
    @PostMapping("/book")
    public ResponseEntity<String> createBook(@RequestBody() Book book)
    {
        bookService.createBook(book);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
    //Add required annotations
    @GetMapping("/book")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "available", required = false, defaultValue = "false") boolean available,
                                   @RequestParam(value = "author", required = false) String author){


        return new ResponseEntity<>(bookService.getBooks(genre, available, author), HttpStatus.OK);

    }
}
