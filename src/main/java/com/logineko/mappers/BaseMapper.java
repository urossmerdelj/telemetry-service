package com.logineko.mappers;

import java.util.List;

import com.logineko.dto.pagination.PaginatedResponse;
import com.logineko.dto.pagination.PaginationQueryParam;
import com.logineko.repositories.dto.PaginatedResult;

/**
 * A generic base mapper interface that provides common mapping functionalities.
 *
 * @param <E> The Entity type
 * @param <D> The DTO type
 */
public interface BaseMapper<E, D> {

  D toDto(E entity);

  List<D> toDto(List<E> entities);

  default PaginatedResponse<D> toPaginatedResponse(
      PaginationQueryParam params, PaginatedResult<E> paginatedResult) {
    PaginatedResult<D> result =
        new PaginatedResult<>(toDto(paginatedResult.data()), paginatedResult.total());
    return new PaginatedResponse<>(params, result);
  }
}
