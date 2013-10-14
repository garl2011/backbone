package com.socix.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class IStatement {
    public static int CLOSE_ALL_RESULTS = Statement.CLOSE_ALL_RESULTS;
    public static int CLOSE_CURRENT_RESULT = Statement.CLOSE_CURRENT_RESULT;
    public static int EXECUTE_FAILED = Statement.EXECUTE_FAILED;
    public static int KEEP_CURRENT_RESULT = Statement.KEEP_CURRENT_RESULT;
    public static int NO_GENERATED_KEYS = Statement.NO_GENERATED_KEYS;
    public static int RETURN_GENERATED_KEYS = Statement.RETURN_GENERATED_KEYS;
    public static int SUCCESS_NO_INFO = Statement.SUCCESS_NO_INFO;
    private Statement state;
    private IConnection conn;
    private String eid;
    private boolean lock = false;
    private long invokeTime = -1;

    IStatement(IConnection conn, Statement state) {
        this.conn = conn;
        this.state = state;
        this.eid = this.conn.getEID();
        this.invokeTime = System.currentTimeMillis();
    }

    String getEID() {
        return this.eid;
    }

    void setLock(boolean lock) {
        this.lock = lock;
    }

    boolean isLocked() {
        return this.lock;
    }

    long getInvokeTime() {
        return this.invokeTime;
    }

    public void close() throws SQLException {
        try {
            this.conn.closeStatement(this);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    void destroy() throws SQLException {
        try {
            this.state.close();
            this.invokeTime = -1;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void addBatch(String sql) throws SQLException {
        try {
            this.state.addBatch(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void cancel() throws SQLException {
        try {
            this.state.cancel();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void clearBatch() throws SQLException {
        try {
            this.state.clearBatch();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void clearWarnings() throws SQLException {
        try {
            this.state.clearWarnings();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql) throws SQLException {
        try {
            return this.state.execute(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            return this.state.execute(sql, autoGeneratedKeys);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            return this.state.execute(sql, columnIndexes);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        try {
            return this.state.execute(sql, columnNames);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int[] executeBatch() throws SQLException {
        try {
            return this.state.executeBatch();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            return this.state.executeQuery(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        try {
            return this.state.executeUpdate(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            return this.state.executeUpdate(sql, autoGeneratedKeys);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            return this.state.executeUpdate(sql, columnIndexes);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException {
        try {
            return this.state.executeUpdate(sql, columnNames);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public IConnection getConnection() throws SQLException {
        try {
            return this.conn;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getFetchDirection() throws SQLException {
        try {
            return this.state.getFetchDirection();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getFetchSize() throws SQLException {
        try {
            return this.state.getFetchSize();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return this.state.getGeneratedKeys();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getMaxFieldSize() throws SQLException {
        try {
            return this.state.getMaxFieldSize();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getMaxRows() throws SQLException {
        try {
            return this.state.getMaxRows();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean getMoreResults() throws SQLException {
        try {
            return this.state.getMoreResults();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean getMoreResults(int current) throws SQLException {
        try {
            return this.state.getMoreResults(current);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getQueryTimeout() throws SQLException {
        try {
            return this.state.getQueryTimeout();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet getResultSet() throws SQLException {
        try {
            return this.state.getResultSet();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetConcurrency() throws SQLException {
        try {
            return this.state.getResultSetConcurrency();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetHoldability() throws SQLException {
        try {
            return this.state.getResultSetHoldability();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetType() throws SQLException {
        try {
            return this.state.getResultSetType();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getUpdateCount() throws SQLException {
        try {
            return this.state.getUpdateCount();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        try {
            return this.state.getWarnings();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setCursorName(String name) throws SQLException {
        try {
            this.state.setCursorName(name);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        try {
            this.state.setEscapeProcessing(enable);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setFetchDirection(int direction) throws SQLException {
        try {
            this.state.setFetchDirection(direction);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setFetchSize(int rows) throws SQLException {
        try {
            this.state.setFetchSize(rows);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setMaxFieldSize(int max) throws SQLException {
        try {
            this.state.setMaxFieldSize(max);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setMaxRows(int max) throws SQLException {
        try {
            this.state.setMaxRows(max);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        try {
            this.state.setQueryTimeout(seconds);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}