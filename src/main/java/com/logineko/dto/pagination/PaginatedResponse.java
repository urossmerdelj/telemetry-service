package com.logineko.dto.pagination;

import com.logineko.repositories.dto.PaginatedResult;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaginatedResponse<T> {

  private List<T> data;
  private PaginationMeta meta;

  public PaginatedResponse(PaginationQueryParam query, PaginatedResult<T> paginatedResult) {
    this.data = paginatedResult.data();
    this.meta = new PaginationMeta(query.getPage(), query.getSize(), paginatedResult.total());
  }
}
