package com.starlords.sirmeows.service;

import com.starlords.sirmeows.entity.Publisher;
import com.starlords.sirmeows.exception.PublisherAlreadyExistsException;
import com.starlords.sirmeows.exception.PublisherNotFoundException;
import com.starlords.sirmeows.repo.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@AllArgsConstructor
@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public Collection<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Publisher findById(Integer id) {
        return publisherRepository.findById(id).orElseThrow(PublisherNotFoundException::new);
    }

    public Publisher create(Publisher publisher) {
        var name = publisher.getName();
        if (publisherRepository.existsByName(name)) {
            throw new PublisherAlreadyExistsException(name);
        }
        return publisherRepository.save(publisher);
    }

    public Publisher update(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public void delete(Integer id) {
        publisherRepository.deleteById(id);
    }
}
