package com.starlords.sirmeows.api;

import com.starlords.sirmeows.dto.AuthorDto;
import com.starlords.sirmeows.entity.Author;
import com.starlords.sirmeows.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.starlords.sirmeows.config.ModelMapperConfig.LIST_TYPE_AUTHOR_DTO;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private AuthorService authorService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public List<AuthorDto> findAll() {
        return modelMapper.map(authorService.findAll(), LIST_TYPE_AUTHOR_DTO);
    }

    @GetMapping("/{id}")
    public AuthorDto findById(@Positive @PathVariable Integer id) {
        return modelMapper.map(authorService.findById(id), AuthorDto.class);
    }
    @PostMapping("")
    public AuthorDto create(@Valid @RequestBody AuthorDto request) {
        var author = modelMapper.map(request, Author.class);
        return modelMapper.map(authorService.create(author), AuthorDto.class);
    }

    @PatchMapping("/{id}")
    public AuthorDto update(@Positive @PathVariable Integer id, @Valid @RequestBody AuthorDto request) {
        var existing = authorService.findById(id);
        modelMapper.map(request, existing);
        return modelMapper.map(authorService.update(existing), AuthorDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@Positive @PathVariable Integer id) {
        authorService.delete(id);
    }
}
