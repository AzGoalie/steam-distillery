package com.steam_distillery.web;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@SchemaMapping(typeName = "PageInfo")
public class PageInfoResource {

  @SchemaMapping
  boolean hasNext(Page<?> page) {
    return page.hasNext();
  }

  @SchemaMapping
  boolean hasPrevious(Page<?> page) {
    return page.hasPrevious();
  }
}
