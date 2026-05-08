package com.esdc.dto;

import java.util.List;

public record LibraryResponse(Long id,
                               String name,
                               String address,
                               int foundedYear,
                               List<BookResponse> books) {
}
