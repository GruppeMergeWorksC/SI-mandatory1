package com.starlords.sirmeows.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Integer id;

    @NotBlank
    @Size(max=255)
    private String title;

    @NotNull
    private Integer authorId;

    @NotNull
    private Integer publisherId;

    @NotNull
    @Min(1900) // This is a constraint from the original sqlite spec
    private Integer publishingYear;
}
