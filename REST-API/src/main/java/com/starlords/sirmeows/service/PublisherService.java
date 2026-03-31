package com.starlords.sirmeows.service;

import com.starlords.sirmeows.repo.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
}
