/*
 * Copyright (C) 2013 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.metrics.*;

public final class CodahaleMetricsExecuteFactory implements MetricsExecuteFactory
{
   private final MetricRegistry registry;

   public CodahaleMetricsExecuteFactory(MetricRegistry registry)
   {
      this.registry = registry;
   }

   public MetricRegistry getRegistry()
   {
      return registry;
   }

   @Override
   public IMetricsExecute create(String poolName, PoolStats poolStats)
   {
      return new CodaHaleMetricsExecute(poolName, poolStats, registry);
   }
}
