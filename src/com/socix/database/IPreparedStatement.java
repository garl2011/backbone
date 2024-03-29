package com.socix.database;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class IPreparedStatement {
    public static int CLOSE_ALL_RESULTS = PreparedStatement.CLOSE_ALL_RESULTS;
    public static int CLOSE_CURRENT_RESULT = PreparedStatement.CLOSE_CURRENT_RESULT;
    public static int EXECUTE_FAILED = PreparedStatement.EXECUTE_FAILED;
    public static int KEEP_CURRENT_RESULT = PreparedStatement.KEEP_CURRENT_RESULT;
    public static int NO_GENERATED_KEYS = PreparedStatement.NO_GENERATED_KEYS;
    public static int RETURN_GENERATED_KEYS = PreparedStatement.RETURN_GENERATED_KEYS;
    public static int SUCCESS_NO_INFO = PreparedStatement.SUCCESS_NO_INFO;
    private PreparedStatement pstate;
    private IConnection conn;
    private String eid;
    private boolean lock = false;
    private long invokeTime = -1;

    IPreparedStatement(IConnection conn, PreparedStatement pstate) {
        this.conn = conn;
        this.pstate = pstate;
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
            this.conn.closePreparedStatement(this);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    void destroy() throws SQLException {
        try {
            this.pstate.close();
            this.invokeTime = -1;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void addBatch() throws SQLException {
        try {
            this.pstate.addBatch();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void addBatch(String sql) throws SQLException {
        try {
            this.pstate.addBatch(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void cancel() throws SQLException {
        try {
            this.pstate.cancel();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void clearBatch() throws SQLException {
        try {
            this.pstate.clearBatch();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void clearParameters() throws SQLException {
        try {
            this.pstate.clearParameters();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void clearWarnings() throws SQLException {
        try {
            this.pstate.clearWarnings();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute() throws SQLException {
        try {
            return this.pstate.execute();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql) throws SQLException {
        try {
            return this.pstate.execute(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            return this.pstate.execute(sql, autoGeneratedKeys);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            return this.pstate.execute(sql, columnIndexes);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        try {
            return this.pstate.execute(sql, columnNames);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int[] executeBatch() throws SQLException {
        try {
            return this.pstate.executeBatch();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet executeQuery() throws SQLException {
        try {
            return this.pstate.executeQuery();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            return this.pstate.executeQuery(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate() throws SQLException {
        try {
            return this.pstate.executeUpdate();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        try {
            return this.pstate.executeUpdate(sql);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            return this.pstate.executeUpdate(sql, autoGeneratedKeys);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            return this.pstate.executeUpdate(sql, columnIndexes);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException {
        try {
            return this.pstate.executeUpdate(sql, columnNames);
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
            return this.pstate.getFetchDirection();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getFetchSize() throws SQLException {
        try {
            return this.pstate.getFetchSize();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return this.pstate.getGeneratedKeys();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getMaxFieldSize() throws SQLException {
        try {
            return this.pstate.getMaxFieldSize();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getMaxRows() throws SQLException {
        try {
            return this.pstate.getMaxRows();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return this.pstate.getMetaData();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean getMoreResults() throws SQLException {
        try {
            return this.pstate.getMoreResults();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public boolean getMoreResults(int current) throws SQLException {
        try {
            return this.pstate.getMoreResults(current);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return this.pstate.getParameterMetaData();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getQueryTimeout() throws SQLException {
        try {
            return this.pstate.getQueryTimeout();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public ResultSet getResultSet() throws SQLException {
        try {
            return this.pstate.getResultSet();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetConcurrency() throws SQLException {
        try {
            return this.pstate.getResultSetConcurrency();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetHoldability() throws SQLException {
        try {
            return this.pstate.getResultSetHoldability();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getResultSetType() throws SQLException {
        try {
            return this.pstate.getResultSetType();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public int getUpdateCount() throws SQLException {
        try {
            return this.pstate.getUpdateCount();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        try {
            return this.pstate.getWarnings();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setArray(int i, Array x) throws SQLException {
        try {
            this.pstate.setArray(i, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length)
        throws SQLException {
        try {
            this.pstate.setAsciiStream(parameterIndex, x, length);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x)
        throws SQLException {
        try {
            this.pstate.setBigDecimal(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length)
        throws SQLException {
        try {
            this.pstate.setBinaryStream(parameterIndex, x, length);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setBlob(int i, Blob x) throws SQLException {
        try {
            this.pstate.setBlob(i, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setBoolean(int parameterIndex, boolean x)
        throws SQLException {
        try {
            this.pstate.setBoolean(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        try {
            this.pstate.setByte(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setBytes(int parameterIndex, byte[] x)
        throws SQLException {
        try {
            this.pstate.setBytes(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length)
        throws SQLException {
        try {
            this.pstate.setCharacterStream(parameterIndex, reader, length);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setClob(int i, Clob x) throws SQLException {
        try {
            this.pstate.setClob(i, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setCursorName(String name) throws SQLException {
        try {
            this.pstate.setCursorName(name);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        try {
            this.pstate.setDate(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setDate(int parameterIndex, Date x, Calendar cal)
        throws SQLException {
        try {
            this.pstate.setDate(parameterIndex, x, cal);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setDouble(int parameterIndex, double x)
        throws SQLException {
        try {
            this.pstate.setDouble(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        try {
            this.pstate.setEscapeProcessing(enable);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setFetchDirection(int direction) throws SQLException {
        try {
            this.pstate.setFetchDirection(direction);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setFetchSize(int rows) throws SQLException {
        try {
            this.pstate.setFetchSize(rows);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        try {
            this.pstate.setFloat(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        try {
            this.pstate.setInt(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        try {
            this.pstate.setLong(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setMaxFieldSize(int max) throws SQLException {
        try {
            this.pstate.setMaxFieldSize(max);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setMaxRows(int max) throws SQLException {
        try {
            this.pstate.setMaxRows(max);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setNull(int parameterIndex, int sqlType)
        throws SQLException {
        try {
            this.pstate.setNull(parameterIndex, sqlType);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setNull(int paramIndex, int sqlType, String typeName)
        throws SQLException {
        try {
            this.pstate.setNull(paramIndex, sqlType, typeName);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setObject(int parameterIndex, Object x)
        throws SQLException {
        try {
            this.pstate.setObject(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType)
        throws SQLException {
        try {
            this.pstate.setObject(parameterIndex, x, targetSqlType);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType,
        int scale) throws SQLException {
        try {
            this.pstate.setObject(parameterIndex, x, targetSqlType, scale);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        try {
            this.pstate.setQueryTimeout(seconds);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setRef(int i, Ref x) throws SQLException {
        try {
            this.pstate.setRef(i, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        try {
            this.pstate.setShort(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setString(int parameterIndex, String x)
        throws SQLException {
        try {
            this.pstate.setString(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        try {
            this.pstate.setTime(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setTime(int parameterIndex, Time x, Calendar cal)
        throws SQLException {
        try {
            this.pstate.setTime(parameterIndex, x, cal);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x)
        throws SQLException {
        try {
            this.pstate.setTimestamp(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
        throws SQLException {
        try {
            this.pstate.setTimestamp(parameterIndex, x, cal);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    /*
        public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
            try {
                this.pstate.setUnicodeStream(parameterIndex, x, length);
            }catch(Exception ex) {
                throw new SQLException(ex);
            }
        }
    */
    public void setURL(int parameterIndex, URL x) throws SQLException {
        try {
            this.pstate.setURL(parameterIndex, x);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}
