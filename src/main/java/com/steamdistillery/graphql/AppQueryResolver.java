package com.steamdistillery.graphql;

import com.steamdistillery.models.App;
import com.steamdistillery.respositories.AppRepository;
import com.steamdistillery.respositories.OffsetPageRequest;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppQueryResolver implements GraphQLQueryResolver {

  private final AppRepository repository;

  public AppQueryResolver(AppRepository repository) {
    this.repository = repository;
  }

  public Connection<App> apps(int first, @Nullable String cursor) {
    log.debug("Apps query: first: {}, cursor: {}", first, cursor);

    var decodedCursor = cursor == null ? "0" : new String(Base64.getDecoder().decode(cursor));
    var offset = Long.parseLong(decodedCursor);
    var limit = Math.min(first, 100);
    var page = repository.findAll(new OffsetPageRequest(limit, offset));

    var edges = page
        .<Edge<App>>map(app -> new DefaultEdge<>(app, generateCursor(app)))
        .toList();

    var pageInfo = new DefaultPageInfo(
        edges.get(0).getCursor(),
        edges.get(edges.size() - 1).getCursor(),
        page.hasPrevious(),
        page.hasNext());

    return new DefaultConnection<>(edges, pageInfo);
  }

  private ConnectionCursor generateCursor(App app) {
    var idString = Long.toString(app.getId());
    var bytes = idString.getBytes(StandardCharsets.UTF_8);
    return new DefaultConnectionCursor(Base64.getEncoder().encodeToString(bytes));
  }
}
