package com.socix.database;

import com.socix.database.MaxNumExceedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbPool {
    private DbProperties dbProp;
    private String eid;
    private int exclusiveCon;
    private int sharedCon;
    private int sharedIndex;
    private int retryCount;
    private List<IConnection> workPool = new ArrayList<IConnection>();
    private List<IConnection> freePool = new ArrayList<IConnection>();
    private List<IConnection> sharedPool = new ArrayList<IConnection>();
    private Boolean exclusiveFlag = new Boolean(true);
    private Boolean sharedFlag = new Boolean(true);

    DbPool(DbProperties dbProp) throws SQLException {
        this.dbProp = dbProp;
        this.eid = dbProp.getEid();
        init();
    }

    private void init() throws SQLException {
    	this.exclusiveCon = this.dbProp.getExclusive();
    	this.sharedCon = this.dbProp.getShared();
    	this.retryCount = this.dbProp.getRetryCount();
    	this.sharedIndex = 0;

        try {
            for (int i = 0; i < this.exclusiveCon; i++) {
                IConnection conn = new IConnection(this.dbProp, this);
                this.freePool.add(conn);
            }

            for (int i = 0; i < this.sharedCon; i++) {
                IConnection conn = new IConnection(this.dbProp, this);
                this.sharedPool.add(conn);
            }
        } catch (Exception ex) {
        	throw new SQLException(ex);
        }
    }

    String getEID() {
        return this.eid;
    }

    IConnection getConnection() throws MaxNumExceedException, SQLException {
        synchronized (this.exclusiveFlag) {
            if (this.freePool.size() > 0) {
                IConnection conn =  this.freePool.remove(0);
                if(conn.isValid(0)) {
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn;                	
                } else {
                	conn = createConnection(this.retryCount);
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn; 
                }
            } else {
            	if(this.freePool.size() + this.workPool.size() < this.exclusiveCon) {
            		IConnection conn = createConnection(this.retryCount);
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn; 
            	} else {
            		throw new MaxNumExceedException();
            	}
            }
        }
    }

    IConnection getConnection(boolean lock) throws MaxNumExceedException, SQLException {
        synchronized (this.exclusiveFlag) {
            if (this.freePool.size() > 0) {
                IConnection conn =  this.freePool.remove(0);
                if(conn.isValid(0)) {
                	conn.setLock(lock);
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn;                	
                } else {
                	conn = createConnection(this.retryCount);
                	conn.setLock(lock);
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn; 
                }
            } else {
            	if(this.freePool.size() + this.workPool.size() < this.exclusiveCon) {
            		IConnection conn = createConnection(this.retryCount);
                	conn.setLock(lock);
                    conn.setInvokeTime(System.currentTimeMillis());
                    this.workPool.add(conn);

                    return conn; 
            	} else {
            		throw new MaxNumExceedException();
            	}
            }
        }
    }

    void closeConnection(IConnection conn) throws SQLException {
        synchronized (this.exclusiveFlag) {
            try {
                for (int i = 0; i < this.workPool.size(); i++) {
                    if (conn ==  this.workPool.get(i)) {
                        this.workPool.remove(i);
                        conn.reset();
                        this.freePool.add(conn);

                        break;
                    }
                }
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IStatement getStatement() throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement();

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IStatement getStatement(boolean lock) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement();
                state.setLock(lock);

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IStatement getStatement(int resultSetType, int resultSetConcurrency)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement(resultSetType,
                        resultSetConcurrency);

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IStatement getStatement(int resultSetType, int resultSetConcurrency, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement(resultSetType,
                        resultSetConcurrency);
                state.setLock(lock);

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IStatement getStatement(int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement(resultSetType,
                        resultSetConcurrency, resultSetHoldability);

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IStatement getStatement(int resultSetType, int resultSetConcurrency,
        int resultSetHoldability, boolean lock) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IStatement state = conn.createStatement(resultSetType,
                        resultSetConcurrency, resultSetHoldability);
                state.setLock(lock);

                return state;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    
    }

    IPreparedStatement getPreparedStatement(String sql)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IPreparedStatement getPreparedStatement(String sql, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql);
                pstate.setLock(lock);
                
                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IPreparedStatement getPreparedStatement(String sql, int autoGeneratedKeys)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        autoGeneratedKeys);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IPreparedStatement getPreparedStatement(String sql, int autoGeneratedKeys, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        autoGeneratedKeys);
                pstate.setLock(lock);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }   
    }

    IPreparedStatement getPreparedStatement(String sql, int[] columnIndexes)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        columnIndexes);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IPreparedStatement getPreparedStatement(String sql, int[] columnIndexes, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        columnIndexes);
                pstate.setLock(lock);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IPreparedStatement getPreparedStatement(String sql, String[] columnNames)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        columnNames);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IPreparedStatement getPreparedStatement(String sql, String[] columnNames, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        columnNames);
                pstate.setLock(lock);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IPreparedStatement getPreparedStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    IPreparedStatement getPreparedStatement(String sql, int resultSetType,
        int resultSetConcurrency, boolean lock) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency);
                pstate.setLock(lock);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    IPreparedStatement getPreparedStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	    	
    }

    IPreparedStatement getPreparedStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                IPreparedStatement pstate = conn.prepareStatement(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                pstate.setLock(lock);

                return pstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        } 
    }

    ICallableStatement getCallableStatement(String sql)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql);

                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    ICallableStatement getCallableStatement(String sql, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql);
                cstate.setLock(lock);

                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    ICallableStatement getCallableStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency);

                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    ICallableStatement getCallableStatement(String sql, int resultSetType,
        int resultSetConcurrency, boolean lock) throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency);
                cstate.setLock(lock);
                
                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }
    }

    ICallableStatement getCallableStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);

                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }    	
    }

    ICallableStatement getCallableStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability, boolean lock)
        throws MaxNumExceedException, SQLException {
        synchronized (this.sharedFlag) {
        	this.sharedIndex = (++this.sharedIndex)%this.sharedCon;
        	IConnection conn =  this.sharedPool.get(this.sharedIndex);
            try {
                if(!conn.isValid(0)) {
                	conn = createConnection(this.retryCount);
                	this.sharedPool.set(this.sharedIndex, conn);
                }
                ICallableStatement cstate = conn.prepareCall(sql,
                        resultSetType, resultSetConcurrency,
                        resultSetHoldability);
                cstate.setLock(lock);

                return cstate;
            } catch (MaxNumExceedException maxE) {
                throw maxE;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        }  
    }

//    void clear(long time) {
//        try {
//            synchronized (this.sharedFLag) {
//                for (int i = 0; i < this.sharedPool.size();) {
//                	if(this.sharedPool.get(i).isValid(1000)) {
//                		this.sharedPool.get(i).clear(time);
//                		i++;
//                	} else {
//                		this.sharedPool.remove(i).destroy();
//                        IConnection conn = new IConnection(this.dbProp, this);
//                        this.sharedPool.add(conn); 
//                	}
//                }
//            }
//
//            synchronized (this.standardFLag) {
//                for (int i = 0; i < this.workingPool.size();) {
//                	if(this.workingPool.get(i).isValid(1000)) {
//	                    if (!( this.workingPool.get(i)).isLocked() && ((time - ( this.workingPool.get(i)).getInvokeTime()) > this.expiredTime)) {
//	                    	this.workingPool.remove(i).destroy();
//	                    } else {
//	                        i++;
//	                    }
//                	} else {
//                		this.workingPool.remove(i).destroy();
//                	}
//                }
//                
//                for (int i = 0; i < this.freePool.size();) {
//                	if(this.freePool.get(i).isValid(1000)) {
//                		i++;
//                	} else {
//                		this.freePool.remove(i).destroy();
//                	}
//                }
//            }
//        } catch (Exception ex) {
//
//        }
//    }

    void shutdown() throws SQLException {
        try {
            synchronized (this.exclusiveFlag) {
                while (this.freePool.size() > 0) {
                    ( this.freePool.remove(0)).destroy();
                }

                while (this.workPool.size() > 0) {
                    ( this.workPool.remove(0)).destroy();
                }
            }

            synchronized (this.sharedFlag) {
                while (this.sharedPool.size() > 0) {
                    ( this.sharedPool.remove(0)).destroy();
                }
            }
        } catch (Exception ex) {
        	throw new SQLException(ex);
        }
    }

    void restart() throws SQLException {
        this.shutdown();
        this.init();
    }
    
    private IConnection createConnection(int retryCount) throws SQLException {
    	IConnection conn = null;
    	for(int i = 0; i < retryCount; i++) {
            try {
                conn = new IConnection(this.dbProp, this);
                break;
            } catch (Exception ex) {
            	continue;
            }
    	}
    	if(conn != null)
    		return conn;
    	else 
    		throw new SQLException("unable create connection over " + retryCount + " times");
    }

//    class CheckThread extends Thread {
//        private long time = -1;
//        private boolean flag = true;
//
//        CheckThread(long time) {
//            this.time = time;
//        }
//
//        public void run() {
//            try {
//                synchronized (this) {
//                    while (flag) {
//                        DbPool.this.clear(System.currentTimeMillis());
//                        this.wait(this.time);
//                    }
//                }
//            } catch (Exception ex) {
//            }
//        }
//
//        void done() {
//            this.flag = false;
//        }
//    }
}
