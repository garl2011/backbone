package com.socix.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.socix.configure.GlobalInfo;

public class TaskProperties {
	private String id;
	private String name;
	private Task task;
	private Date firstTime;
	private long delay;
	private long period;
	private boolean fixRate;
	
	public TaskProperties(String id) {
		this.id = id;
		loadProperties();
	}
	
	public long getDelay() {
		return delay;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public Date getFirstTime() {
		return firstTime;
	}
	
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getPeriod() {
		return period;
	}
	
	public void setPeriod(long period) {
		this.period = period;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	public boolean isFixRate() {
		return fixRate;
	}

	public void setFixRate(boolean fixRate) {
		this.fixRate = fixRate;
	}
	
    private void loadProperties() {
    	try {
	        Map<String, String> prop = GlobalInfo.getPropertyMap(id, "task");
	        name = prop.get("name");
			task = (Task)ClassLoader.getSystemClassLoader().loadClass(prop.get("class")).newInstance();
	    	if(prop.get("firstTime") != null) {
		    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	firstTime = format.parse(prop.get("firstTime"));
	    	}
	    	if(prop.get("delay") != null) {
	    		delay = Long.parseLong(prop.get("delay"));
	    	} else {
	    		delay = 0;
	    	}
	    	if(prop.get("period") != null) {
	    		period = Long.parseLong(prop.get("period"));
	    	} else {
	    		period = -1;
	    	}
	    	if(prop.get("fixRate") != null) {
	    		fixRate = Boolean.parseBoolean(prop.get("fixRate"));
	    	} else {
	    		fixRate = false;
	    	}
    	} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			firstTime = new Date(System.currentTimeMillis());
		}
    }

}
