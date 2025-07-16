package com.logineko.dto.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jboss.resteasy.reactive.RestQuery;

@Getter
@NonFinal
@EqualsAndHashCode
public class PaginationQueryParam {
  private static final int MIN_PAGE = 1;
  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 100;
  private static final int DEFAULT_SIZE = 20;

  @DefaultValue("1")
  @RestQuery
  @Min(MIN_PAGE)
  protected int page;

  @DefaultValue("20")
  @RestQuery
  @Min(MIN_SIZE)
  @Max(MAX_SIZE)
  protected int size;

  public PaginationQueryParam() {
    this.page = MIN_PAGE;
    this.size = DEFAULT_SIZE;
  }

  @JsonIgnore
  public int getPageIndex() {
    return this.getPage() - 1;
  }

  @JsonIgnore
  public int getOffset() {
    return this.getPageIndex() * this.getSize();
  }
}
