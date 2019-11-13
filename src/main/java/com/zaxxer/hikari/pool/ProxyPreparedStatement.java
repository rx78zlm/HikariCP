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

package com.zaxxer.hikari.pool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is the proxy class for java.sql.PreparedStatement.
 *
 * @author Brett Wooldridge
 */
public abstract class ProxyPreparedStatement extends ProxyStatement implements PreparedStatement {

   private String sql;

   ProxyPreparedStatement(ProxyConnection connection, PreparedStatement statement, String sql) {
      super(connection, statement);
      this.sql = sql;
   }

   // **********************************************************************
   //              Overridden java.sql.PreparedStatement Methods
   // **********************************************************************

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean execute() throws SQLException {
      if (connection.getPoolEntry().getMetricsExecute() == null) {
         return ((PreparedStatement) delegate).execute();
      } else {
         long start = System.currentTimeMillis();
         try {
            connection.markCommitStateDirty();
            connection.getPoolEntry().getMetricsExecute().markExecute(sql);
            return ((PreparedStatement) delegate).execute();
         } catch (SQLException e) {
            connection.getPoolEntry().getMetricsExecute().markExecuteErr(sql);
            throw e;
         } finally {
            long elapsed = System.currentTimeMillis() - start;
            connection.getPoolEntry().getMetricsExecute().markElapsed(sql, elapsed);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ResultSet executeQuery() throws SQLException {
      if (connection.getPoolEntry().getMetricsExecute() == null) {
         ResultSet resultSet = ((PreparedStatement) delegate).executeQuery();
         return ProxyFactory.getProxyResultSet(connection, this, resultSet);
      } else {
         long start = System.currentTimeMillis();
         try {
            connection.markCommitStateDirty();
            connection.getPoolEntry().getMetricsExecute().markExecute(sql);
            ResultSet resultSet = ((PreparedStatement) delegate).executeQuery();
            return ProxyFactory.getProxyResultSet(connection, this, resultSet);
         } catch (SQLException e) {
            connection.getPoolEntry().getMetricsExecute().markExecuteErr(sql);
            throw e;
         } finally {
            long elapsed = System.currentTimeMillis() - start;
            connection.getPoolEntry().getMetricsExecute().markElapsed(sql, elapsed);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int executeUpdate() throws SQLException {
      if (connection.getPoolEntry().getMetricsExecute() == null) {
         return ((PreparedStatement) delegate).executeUpdate();
      } else {
         long start = System.currentTimeMillis();
         try {
            connection.markCommitStateDirty();
            connection.getPoolEntry().getMetricsExecute().markExecute(sql);
            return ((PreparedStatement) delegate).executeUpdate();
         } catch (SQLException e) {
            connection.getPoolEntry().getMetricsExecute().markExecuteErr(sql);
            throw e;
         } finally {
            long elapsed = System.currentTimeMillis() - start;
            connection.getPoolEntry().getMetricsExecute().markElapsed(sql, elapsed);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public long executeLargeUpdate() throws SQLException {
      if (connection.getPoolEntry().getMetricsExecute() == null) {
         return ((PreparedStatement) delegate).executeLargeUpdate();
      } else {
         long start = System.currentTimeMillis();
         try {
            connection.markCommitStateDirty();
            connection.getPoolEntry().getMetricsExecute().markExecute(sql);
            return ((PreparedStatement) delegate).executeLargeUpdate();
         } catch (SQLException e) {
            connection.getPoolEntry().getMetricsExecute().markExecuteErr(sql);
            throw e;
         } finally {
            long elapsed = System.currentTimeMillis() - start;
            connection.getPoolEntry().getMetricsExecute().markElapsed(sql, elapsed);
         }
      }
   }
}
