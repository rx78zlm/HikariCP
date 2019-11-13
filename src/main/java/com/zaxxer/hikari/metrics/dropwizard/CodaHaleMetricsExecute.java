package com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.metrics.IMetricsExecute;
import com.zaxxer.hikari.metrics.PoolStats;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Created by zhangleimin on 2019/10/23.
 */
public class CodaHaleMetricsExecute implements IMetricsExecute {

   private final String poolName;

   private ConcurrentMap<String, Histogram> executeTimes = new ConcurrentHashMap<>();

   private ConcurrentMap<String, Meter> executeMeters = new ConcurrentHashMap<>();

   private ConcurrentMap<String, Meter> executeErrMeters = new ConcurrentHashMap<>();

   private final MetricRegistry registry;

   private static final String METRIC_CATEGORY = "pool";
   private static final String METRIC_NAME_EXECUTE = "execute_request";
   private static final String METRIC_NAME_EXECUTE_ERR = "execute_request_err";
   private static final String METRIC_NAME_TIME = "execute_time";

   public CodaHaleMetricsExecute(final String poolName, final PoolStats poolStats, final MetricRegistry registry) {
      this.poolName = poolName;
      this.registry = registry;
   }

   @Override
   public void markExecute(String sql) {
      Meter meter = this.executeMeters.computeIfAbsent(sql, new Function<String, Meter>() {
         @Override
         public Meter apply(String s) {
            return registry.meter(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_EXECUTE, s));
         }
      });
      meter.mark();
   }

   @Override
   public void markExecuteErr(String sql) {
      Meter meter = this.executeErrMeters.computeIfAbsent(sql, new Function<String, Meter>() {
         @Override
         public Meter apply(String s) {
            return registry.meter(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_EXECUTE_ERR, s));
         }
      });
      meter.mark();
   }

   @Override
   public void markElapsed(String sql, long m) {
      Histogram histogram = this.executeTimes.computeIfAbsent(sql, new Function<String, Histogram>() {
         @Override
         public Histogram apply(String s) {
            return registry.histogram(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_TIME, s));
         }
      });
      histogram.update(m);
   }

   @Override
   public void close() {
      closeExecuteMeters();
      closeExecuteErrMeters();
      closeExecuteTimes();
   }

   private void closeExecuteMeters() {
      for (String sql : executeMeters.keySet()) {
         registry.remove(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_EXECUTE, sql));
      }
      executeMeters.clear();
   }

   private void closeExecuteErrMeters() {
      for (String sql : executeErrMeters.keySet()) {
         registry.remove(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_EXECUTE_ERR, sql));
      }
      executeErrMeters.clear();
   }

   private void closeExecuteTimes() {
      for (String sql : executeTimes.keySet()) {
         registry.remove(MetricRegistry.name(poolName, METRIC_CATEGORY, METRIC_NAME_TIME, sql));
      }
      executeTimes.clear();
   }
}
