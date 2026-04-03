package com.starlords.sirmeows.api;

import com.starlords.sirmeows.dto.BookDto;
import com.starlords.sirmeows.entity.Book;
import com.starlords.sirmeows.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.starlords.sirmeows.config.ModelMapperConfig.LIST_TYPE_BOOK_DTO;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;
    private ModelMapper modelMapper;

    @GetMapping
    public List<BookDto> findAll(
            @Positive @RequestParam(defaultValue = "20") int limit,
            @PositiveOrZero @RequestParam(defaultValue = "0") int offset) {
        return modelMapper.map(bookService.findAll(limit, offset), LIST_TYPE_BOOK_DTO);
    }

    @GetMapping("/{id}")
    public BookDto findById(@Positive @PathVariable Integer id) {
        return modelMapper.map(bookService.findById(id), BookDto.class);
    }

    @PostMapping
    public BookDto create(@Valid @RequestBody BookDto request) {
        var book = modelMapper.map(request, Book.class);
        return modelMapper.map(bookService.create(book), BookDto.class);
    }

    @PatchMapping("/{id}")
    public BookDto update(@Positive @PathVariable Integer id, @Valid @RequestBody BookDto request) {
        var patch = modelMapper.map(request, Book.class);
        return modelMapper.map(bookService.update(id, patch), BookDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable Integer id) {
        bookService.delete(id);
    }
}
