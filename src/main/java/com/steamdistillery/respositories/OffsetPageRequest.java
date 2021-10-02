package com.steamdistillery.respositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record OffsetPageRequest(int limit, long offset, Sort sort) implements Pageable {

  public OffsetPageRequest {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset must not be less than zero");
    }

    if (limit < 1) {
      throw new IllegalArgumentException("Limit must not be less than one");
    }
  }

  public OffsetPageRequest(int limit, long offset) {
    this(limit, offset, Sort.unsorted());
  }

  @Override
  public int getPageNumber() {
    return (int) (offset / limit);
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @NotNull
  @Override
  public Sort getSort() {
    return sort;
  }

  @NotNull
  @Override
  public Pageable next() {
    return new OffsetPageRequest((int) (getOffset() + getPageSize()), getPageSize(), getSort());
  }

  @NotNull
  public Pageable previous() {
    return hasPrevious()
        ? new OffsetPageRequest((int) (getOffset() - getPageSize()), getPageSize(), getSort())
        : this;
  }

  @NotNull
  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @NotNull
  @Override
  public Pageable first() {
    return new OffsetPageRequest(0, getPageSize(), getSort());
  }

  @NotNull
  @Override
  public Pageable withPage(int pageNumber) {
    return new OffsetPageRequest(limit * pageNumber, getPageSize(), getSort());
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }
}
