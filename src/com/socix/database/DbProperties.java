package com.socix.database;

import com.socix.configure.GlobalInfo;
import java.util.Map;

public class DbProperties {
	
    private String id;
    private String eid;
    private String driver;
    private String serverURL;
    private String username;
    private String password;
    private int shared;
    private int exclusive;
    private int state;
    private int pState;
    private int cState;
    private String validSql;
    private int retryCount;

    public DbProperties(String id) {
        this.id = id;
        loadProperties();
    }

    private void loadProperties() {
        Map<String, String> prop = GlobalInfo.getPropertyMap(id, "db");
        eid = prop.get("eid");
        driver = prop.get("driver");
        serverURL = prop.get("url");
        username = prop.get("uname");
        password = prop.get("pass");
        exclusive = Integer.parseInt(prop.get("exclusive"));
        shared = Integer.parseInt(prop.get("shared"));
        state = Integer.parseInt(prop.get("st"));
        pState = Integer.parseInt(prop.get("ps"));
        cState = Integer.parseInt(prop.get("cs"));
        validSql = prop.get("valid");
        retryCount = Integer.parseInt(prop.get("retry"));
    }

	/**
	 * @return the cState
	 */
	public int getCState() {
		return cState;
	}

	/**
	 * @param state the cState to set
	 */
	public void setCState(int cState) {
		this.cState = cState;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the eid
	 */
	public String getEid() {
		return eid;
	}

	/**
	 * @param eid the eid to set
	 */
	public void setEid(String eid) {
		this.eid = eid;
	}

	/**
	 * @return the exclusive
	 */
	public int getExclusive() {
		return exclusive;
	}

	/**
	 * @param exclusive the exclusive to set
	 */
	public void setExclusive(int exclusive) {
		this.exclusive = exclusive;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the pState
	 */
	public int getPState() {
		return pState;
	}

	/**
	 * @param state the pState to set
	 */
	public void setPState(int pState) {
		this.pState = pState;
	}

	/**
	 * @return the serverURL
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * @param serverURL the serverURL to set
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * @return the shared
	 */
	public int getShared() {
		return shared;
	}

	/**
	 * @param shared the shared to set
	 */
	public void setShared(int shared) {
		this.shared = shared;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the validSql
	 */
	public String getValidSql() {
		return validSql;
	}

	/**
	 * @param validSql the validSql to set
	 */
	public void setValidSql(String validSql) {
		this.validSql = validSql;
	}

	/**
	 * @return the retryCount
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	
}
