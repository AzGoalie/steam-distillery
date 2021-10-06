package com.steamdistillery.graphql;

import graphql.ExecutionResult;
import graphql.kickstart.execution.BatchedDataLoaderGraphQLBuilder;
import graphql.kickstart.execution.FutureExecutionResult;
import graphql.kickstart.execution.GraphQLInvoker;
import graphql.kickstart.execution.GraphQLQueryResult;
import graphql.kickstart.execution.config.GraphQLBuilder;
import graphql.kickstart.execution.input.GraphQLInvocationInput;
import graphql.kickstart.execution.input.GraphQLSingleInvocationInput;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalGraphQLInvoker extends GraphQLInvoker {
  public TransactionalGraphQLInvoker(GraphQLBuilder graphQLBuilder,
      BatchedDataLoaderGraphQLBuilder batchedDataLoaderGraphQLBuilder) {
    super(graphQLBuilder, batchedDataLoaderGraphQLBuilder);
  }

  @Transactional
  @Override
  public FutureExecutionResult execute(GraphQLInvocationInput invocationInput) {
    return super.execute(invocationInput);
  }

  @Transactional
  @Override
  public CompletableFuture<ExecutionResult> executeAsync(
      GraphQLSingleInvocationInput invocationInput) {
    return super.executeAsync(invocationInput);
  }

  @Transactional
  @Override
  public GraphQLQueryResult query(GraphQLInvocationInput invocationInput) {
    return super.query(invocationInput);
  }

  @Transactional
  @Override
  public CompletableFuture<GraphQLQueryResult> queryAsync(GraphQLInvocationInput invocationInput) {
    return super.queryAsync(invocationInput);
  }
}
