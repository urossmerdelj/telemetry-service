package com.logineko.dto.pagination;

import java.util.List;

import com.logineko.repositories.dto.PaginatedResult;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaginatedResponse<T> {

  private List<T> data;
  private PaginationMeta meta;

  public PaginatedResponse(PaginationQueryParam query, List<T> data, long total) {
    this.data = data;
    this.meta = new PaginationMeta(query.getPerPage(), query.getPage(), total);
  }

  public PaginatedResponse(PaginationQueryParam query, PaginatedResult<T> paginatedResult) {
    this.data = paginatedResult.data();
    this.meta = new PaginationMeta(query.getPerPage(), query.getPage(), paginatedResult.total());
  }
}
