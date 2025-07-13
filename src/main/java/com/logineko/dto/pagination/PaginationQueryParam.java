package com.logineko.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jboss.resteasy.reactive.RestQuery;

@Getter
@NonFinal
@EqualsAndHashCode
public class PaginationQueryParam {
  private static final int MAX_PER_PAGE = 100;
  private static final int MIN_PER_PAGE = 1;
  private static final int DEFAULT_PER_PAGE = 20;

  private static final int MIN_PAGE = 1;
  private static final int DEFAULT_PAGE = 1;

  @DefaultValue("20")
  @RestQuery
  @Min(1)
  @Max(100)
  protected int perPage;

  @DefaultValue("1")
  @RestQuery
  @Min(1)
  protected int page;

  public PaginationQueryParam() {
    this.perPage = DEFAULT_PER_PAGE;
    this.page = DEFAULT_PAGE;
  }

  public PaginationQueryParam(int perPage, int page) {
    this.perPage = Math.min(Math.max(perPage, MIN_PER_PAGE), MAX_PER_PAGE);
    this.page = Math.max(page, MIN_PAGE);
  }

  public void setPerPage(int perPage) {
    this.perPage = Math.min(Math.max(perPage, MIN_PER_PAGE), MAX_PER_PAGE);
  }

  public void setPage(int page) {
    this.page = Math.max(page, MIN_PAGE);
  }

  @JsonIgnore
  public int getOffset() {
    return this.getPageIndex() * this.getPerPage();
  }

  @JsonIgnore
  public int getPageIndex() {
    return this.getPage() - 1;
  }
}
