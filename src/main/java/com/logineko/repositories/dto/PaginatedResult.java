package com.logineko.repositories.dto;

import java.util.List;

public record PaginatedResult<T>(List<T> data, long total) {}
