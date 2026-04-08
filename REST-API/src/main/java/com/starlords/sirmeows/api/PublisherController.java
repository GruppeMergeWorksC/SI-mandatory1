package com.starlords.sirmeows.api;

import com.starlords.sirmeows.dto.PublisherDto;
import com.starlords.sirmeows.entity.Publisher;
import com.starlords.sirmeows.service.PublisherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.starlords.sirmeows.config.ModelMapperConfig.LIST_TYPE_PUBLISHER_DTO;

@AllArgsConstructor
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private PublisherService publisherService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public List<PublisherDto> findAll() {
        return modelMapper.map(publisherService.findAll(), LIST_TYPE_PUBLISHER_DTO);
    }

    @GetMapping("/{id}")
    public PublisherDto findById(@Positive @PathVariable Integer id) {
        return modelMapper.map(publisherService.findById(id), PublisherDto.class);
    }

    @PostMapping("")
    public PublisherDto create(@Valid @RequestBody PublisherDto request) {
        var publisher = modelMapper.map(request, Publisher.class);
        return modelMapper.map(publisherService.create(publisher), PublisherDto.class);
    }

    @PatchMapping("/{id}")
    public PublisherDto update(@Positive @PathVariable Integer id, @Valid @RequestBody PublisherDto request) {
        var existing =  publisherService.findById(id);
        modelMapper.map(request, existing);
        return modelMapper.map(publisherService.update(existing), PublisherDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@Positive @PathVariable Integer id) {
        publisherService.delete(id);
    }
}
