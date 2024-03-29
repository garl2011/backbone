package com.socix.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.socix.configure.GlobalInfo;


public class DbFactory {
    private static DbFactory factory = null;
    private static Map<String, DbPool> poolContainer = null;

    private DbFactory() {
    }

    public static DbFactory getInstance() {
        return factory;
    }

    public static void initPool() throws SQLException {
        if (factory == null) {
        	poolContainer = new HashMap<String, DbPool>();
            String[] ids = GlobalInfo.getCategoryIDS("db");

            for (int i = 0; i < ids.length; i++) {
                DbProperties prop = new DbProperties(ids[i]);
                DbPool pool = new DbPool(prop);
                poolContainer.put(prop.getEid(), pool);
            }

            factory = new DbFactory();
        }
    }

    public static void reloadPool() throws SQLException {
    	for(String eid : poolContainer.keySet()) {
    		(poolContainer.remove(eid)).shutdown();
    	}
        
        poolContainer = null;

        poolContainer = new HashMap<String, DbPool>();

        String[] ids = GlobalInfo.getCategoryIDS("db");

        for (int i = 0; i < ids.length; i++) {
            DbProperties prop = new DbProperties(ids[i]);
            DbPool pool = new DbPool(prop);
            poolContainer.put(prop.getEid(), pool);
        }
    }

    public static void shutDown() throws SQLException {
    	for(String eid : poolContainer.keySet()) {
    		(poolContainer.remove(eid)).shutdown();
    	}
        
        poolContainer = null;
        
        factory = null;
    }

    public static void restart(String eid) throws SQLException {
        (poolContainer.get(eid)).restart();
    }
    
    public static void restart() throws SQLException {
    	for(String eid : poolContainer.keySet()) {
    		(poolContainer.get(eid)).restart();
    	}
    }

    public IConnection getConnection(String EID) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getConnection();
    }

    public IConnection getConnection(String EID, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getConnection(lock);
    }

    public IStatement getStatement(String EID) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement();
    }

    public IStatement getStatement(String EID, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement(lock);
    }

    public IStatement getStatement(String EID, int resultSetType,
        int resultSetConcurrency) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement(resultSetType, resultSetConcurrency);
    }

    public IStatement getStatement(String EID, int resultSetType,
        int resultSetConcurrency, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement(resultSetType, resultSetConcurrency, lock);
    }

    public IStatement getStatement(String EID, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    public IStatement getStatement(String EID, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int autoGeneratedKeys) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, autoGeneratedKeys);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int autoGeneratedKeys, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, autoGeneratedKeys, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int[] columnIndexes) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, columnIndexes);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int[] columnIndexes, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, columnIndexes, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        String[] columnNames) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, columnNames);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        String[] columnNames, boolean lock) throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, columnNames, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, resultSetType,
                resultSetConcurrency, lock);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public IPreparedStatement getPreparedStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getPreparedStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability, lock);
    }

    public ICallableStatement getCallableStatement(String EID, String sql)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql);
    }

    public ICallableStatement getCallableStatement(String EID, String sql, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql, lock);
    }

    public ICallableStatement getCallableStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    public ICallableStatement getCallableStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql, resultSetType,
                resultSetConcurrency, lock);
    }

    public ICallableStatement getCallableStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public ICallableStatement getCallableStatement(String EID, String sql,
        int resultSetType, int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws SQLException, MaxNumExceedException {
        DbPool pool = this.getPool(EID);
        return pool.getCallableStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability, lock);
    }

    private DbPool getPool(String EID) {
    	synchronized(poolContainer) {
    		return poolContainer.get(EID);
    	}
    }
}
