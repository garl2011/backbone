package com.socix.database;

import com.socix.database.MaxNumExceedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class IConnection {
    public static int TRANSACTION_NONE = Connection.TRANSACTION_NONE;
    public static int TRANSACTION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
    public static int TRANSACTION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
    public static int TRANSACTION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
    public static int TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
    private Connection conn;
    private DbPool dbPool;
    private String eid;
    private boolean lock = false;
    private long invokeTime = -1;
    private DbProperties dbProp;
    private List<IStatement> stateContainer = new ArrayList<IStatement>();
    private List<IPreparedStatement> pstateContainer = new ArrayList<IPreparedStatement>();
    private List<ICallableStatement> cstateContainer = new ArrayList<ICallableStatement>();
    private int maxState = 0;
    private int maxPState = 0;
    private int maxCstate = 0;
    private String validSQL;

    IConnection(DbProperties dbProp, DbPool dbPool) throws SQLException {
        this.dbProp = dbProp;
        this.eid = dbProp.getEid();
        this.dbPool = dbPool;
        this.maxState = dbProp.getState();
        this.maxPState = dbProp.getPState();
        this.maxCstate = dbProp.getCState();
        this.validSQL = dbProp.getValidSql();
        init();
    }

    private void init() throws SQLException {
        try {
        	Class.forName(dbProp.getDriver());
            Properties pr;
            pr = new Properties();
            pr.put("user", dbProp.getUsername());
            pr.put("password", dbProp.getPassword());
            this.conn = DriverManager.getConnection(dbProp.getServerURL(), pr);
        } catch (Exception e) {
        	throw new SQLException(e);
        }
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

    void setInvokeTime(long time) {
        this.invokeTime = time;
    }

    public Connection getConn() {
		return this.conn;
	}

	public IStatement createStatement()
        throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement();
                IStatement xstate = new IStatement(this, state);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IStatement createStatement(boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement();
                IStatement xstate = new IStatement(this, state);
                xstate.setLock(lock);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IStatement createStatement(int resultSetType,
        int resultSetConcurrency) throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement(resultSetType,
                        resultSetConcurrency);
                IStatement xstate = new IStatement(this, state);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IStatement createStatement(int resultSetType,
        int resultSetConcurrency, boolean lock) throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement(resultSetType,
                        resultSetConcurrency);
                IStatement xstate = new IStatement(this, state);
                xstate.setLock(lock);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IStatement createStatement(int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement(resultSetType,
                        resultSetConcurrency, resultSetHoldability);
                IStatement xstate = new IStatement(this, state);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IStatement createStatement(int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.stateContainer) {
            if (this.stateContainer.size() >= this.maxState) {
                throw new MaxNumExceedException();
            }

            try {
                Statement state = this.conn.createStatement(resultSetType,
                        resultSetConcurrency, resultSetHoldability);
                IStatement xstate = new IStatement(this, state);
                xstate.setLock(lock);
                this.stateContainer.add(xstate);

                return xstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    void closeStatement(IStatement xstate) throws SQLException {
        synchronized (this.stateContainer) {
            try {
                for (int i = 0; i < this.stateContainer.size(); i++) {
                    if (xstate ==  this.stateContainer.get(i)) {
                        ( this.stateContainer.remove(i)).destroy();

                        break;
                    }
                }
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        autoGeneratedKeys);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int autoGeneratedKeys, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        autoGeneratedKeys);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        columnIndexes);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int[] columnIndexes, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        columnIndexes);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        columnNames);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, String[] columnNames, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        columnNames);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, boolean lock) throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.pstateContainer) {
            if (this.pstateContainer.size() >= this.maxPState) {
                throw new MaxNumExceedException();
            }

            try {
                PreparedStatement pstate = this.conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                IPreparedStatement xpstate = new IPreparedStatement(this, pstate);
                xpstate.setLock(lock);
                this.pstateContainer.add(xpstate);

                return xpstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    void closePreparedStatement(IPreparedStatement xpstate)
        throws SQLException {
        synchronized (this.pstateContainer) {
            try {
                for (int i = 0; i < this.pstateContainer.size(); i++) {
                    if (xpstate ==  this.pstateContainer.get(
                                i)) {
                        ( this.pstateContainer.remove(i)).destroy();

                        break;
                    }
                }
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql)
        throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                xcstate.setLock(lock);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, boolean lock) throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                xcstate.setLock(lock);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        synchronized (this.cstateContainer) {
            if (this.cstateContainer.size() >= this.maxCstate) {
                throw new MaxNumExceedException();
            }

            try {
                CallableStatement cstate = this.conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                ICallableStatement xcstate = new ICallableStatement(this, cstate);
                xcstate.setLock(lock);
                this.cstateContainer.add(xcstate);

                return xcstate;
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

    void closeCallableStatement(ICallableStatement xcstate)
        throws SQLException {
        synchronized (this.cstateContainer) {
            try {
                for (int i = 0; i < this.cstateContainer.size(); i++) {
                    if (xcstate ==  this.cstateContainer.get(
                                i)) {
                        ( this.cstateContainer.remove(i)).destroy();

                        break;
                    }
                }
            } catch (Exception ex) {

                throw new SQLException(ex);
            }
        }
    }

//    void clear(long time) {
//        try {
//            synchronized (this.stateContainer) {
//                for (int i = 0; i < this.stateContainer.size();) {
//                    if (!( this.stateContainer.get(i)).isLocked() && ((time - ( this.stateContainer.get(i)).getInvokeTime()) > this.expiredTime)) {
//                        ( this.stateContainer.remove(i)).destroy();
//                    } else {
//                        i++;
//                    }
//                }
//            }
//
//            synchronized (this.pstateContainer) {
//                for (int i = 0; i < this.pstateContainer.size();) {
//                    if (!( this.pstateContainer.get(i)).isLocked() && ((time - ( this.pstateContainer.get(i)).getInvokeTime()) > this.expiredTime)) {
//                        ( this.pstateContainer.remove(i)).destroy();
//                    } else {
//                        i++;
//                    }
//                }
//            }
//
//            synchronized (this.cstateContainer) {
//                for (int i = 0; i < this.cstateContainer.size();) {
//                    if (!( this.cstateContainer.get(i)).isLocked() && ((time - ( this.cstateContainer.get(i)).getInvokeTime()) > this.expiredTime)) {
//                        ( this.cstateContainer.remove(i)).destroy();
//                    } else {
//                        i++;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//
//        }
//    }

    public void close() throws SQLException {
        try {
            this.dbPool.closeConnection(this);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    void destroy() throws SQLException {
        try {
            synchronized (this.stateContainer) {
                while (this.stateContainer.size() > 0) {
                    ( this.stateContainer.remove(0)).destroy();
                }
            }

            synchronized (this.pstateContainer) {
                while (this.pstateContainer.size() > 0) {
                    ( this.pstateContainer.remove(0)).destroy();
                }
            }

            synchronized (this.cstateContainer) {
                while (this.cstateContainer.size() > 0) {
                    ( this.cstateContainer.remove(0)).destroy();
                }
            }

            this.conn.close();
            this.invokeTime = -1;
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    void reset() throws SQLException {
        try {
            synchronized (this.stateContainer) {
                while (this.stateContainer.size() > 0) {
                    ( this.stateContainer.remove(0)).destroy();
                }
            }

            synchronized (this.pstateContainer) {
                while (this.pstateContainer.size() > 0) {
                    ( this.pstateContainer.remove(0)).destroy();
                }
            }

            synchronized (this.cstateContainer) {
                while (this.cstateContainer.size() > 0) {
                    ( this.cstateContainer.remove(0)).destroy();
                }
            }

            this.setAutoCommit(true);
            this.invokeTime = -1;
        } catch (Exception ex) {
        	throw new SQLException(ex);
        }
    }

    public void clearWarnings() throws SQLException {
        try {
            this.conn.clearWarnings();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void commit() throws SQLException {
        try {
            this.conn.commit();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public boolean getAutoCommit() throws SQLException {
        try {
            return this.conn.getAutoCommit();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public String getCatalog() throws SQLException {
        try {
            return this.conn.getCatalog();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public int getHoldability() throws SQLException {
        try {
            return this.conn.getHoldability();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return this.conn.getMetaData();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public int getTransactionIsolation() throws SQLException {
        try {
            return this.conn.getTransactionIsolation();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try {
            return this.conn.getTypeMap();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        try {
            return this.conn.getWarnings();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public boolean isClosed() throws SQLException {
        try {
            return this.conn.isClosed();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public boolean isReadOnly() throws SQLException {
        try {
            return this.conn.isReadOnly();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }
    
    public boolean isValid(int timeout) throws SQLException {
        try {
        	Statement st = this.conn.createStatement();
        	st.executeQuery(this.validSQL);
        	st.close();
        	return true;
        } catch (Exception ex) {
        	return false;
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        try {
            return this.conn.nativeSQL(sql);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            this.conn.releaseSavepoint(savepoint);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void rollback() throws SQLException {
        try {
            this.conn.rollback();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            this.conn.rollback(savepoint);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        try {
            this.conn.setAutoCommit(autoCommit);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setCatalog(String catalog) throws SQLException {
        try {
            this.conn.setCatalog(catalog);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setHoldability(int holdability) throws SQLException {
        try {
            this.conn.setHoldability(holdability);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        try {
            this.conn.setReadOnly(readOnly);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        try {
            return this.conn.setSavepoint();
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        try {
            return this.conn.setSavepoint(name);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setTransactionIsolation(int level) throws SQLException {
        try {
            this.conn.setTransactionIsolation(level);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try {
            this.conn.setTypeMap(map);
        } catch (Exception ex) {

            throw new SQLException(ex);
        }
    }
}
