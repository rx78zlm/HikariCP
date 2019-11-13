package com.zaxxer.hikari.metrics;

/**
 * Created by zhangleimin on 2019/10/22.
 */
public interface IMetricsExecute extends AutoCloseable {

   void markExecute(String sql);

   void markExecuteErr(String sql);

   void markElapsed(String sql, long m);

   @Override
   default void close() {}
}
