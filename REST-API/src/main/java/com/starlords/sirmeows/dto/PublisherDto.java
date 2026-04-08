package com.starlords.sirmeows.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDto {
    private Integer id;

    @NotBlank
    @Size(max=40)
    private String name;
}
