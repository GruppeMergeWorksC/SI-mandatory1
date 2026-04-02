package com.starlords.sirmeows.api;

import com.starlords.sirmeows.entity.Publisher;
import com.starlords.sirmeows.service.PublisherService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.starlords.sirmeows.config.ModelMapperConfig.LIST_TYPE_PUBLISHER_DTO;

@AllArgsConstructor
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private PublisherService publisherService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public List<Publisher> findAll() {
        return modelMapper.map(publisherService.findAll(), LIST_TYPE_PUBLISHER_DTO);
    }

}
