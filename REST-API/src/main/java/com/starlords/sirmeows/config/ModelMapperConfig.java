package com.starlords.sirmeows.config;

import com.starlords.sirmeows.dto.AuthorDto;
import com.starlords.sirmeows.dto.BookDto;
import com.starlords.sirmeows.dto.PublisherDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;

@Configuration
public class ModelMapperConfig {
    public static final Type LIST_TYPE_PUBLISHER_DTO = new TypeToken<ArrayList<PublisherDto>>(){}.getType();
    public static final Type LIST_TYPE_AUTHOR_DTO = new TypeToken<ArrayList<AuthorDto>>(){}.getType();
    public static final Type LIST_TYPE_BOOK_DTO = new TypeToken<ArrayList<BookDto>>(){}.getType();

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
