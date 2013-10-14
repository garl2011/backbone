package com.socix.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTask extends TimerTask {
	
	private TaskProperties prop;
	private Timer timer;
	
	protected ScheduleTask(TaskProperties prop) {
		this.prop = prop;
		timer = new Timer(prop.getName(), true); 
	}
	
	public void schedule() {
		Date current = new Date(System.currentTimeMillis());
		if(prop.isFixRate()) {
			if(prop.getFirstTime() != null) {
				if(prop.getFirstTime().after(current)) {
					timer.scheduleAtFixedRate(this, prop.getFirstTime(), prop.getPeriod()*1000);
				} else {
					long count = (current.getTime() - prop.getFirstTime().getTime()) / (prop.getPeriod() * 1000) + 1;
					Date ajustDate = new Date(prop.getFirstTime().getTime()+prop.getPeriod()*1000*count);
					prop.setFirstTime(ajustDate);
					timer.scheduleAtFixedRate(this, prop.getFirstTime(), prop.getPeriod()*1000);
					prop.getTask().doTask();
				}
			} else {
				timer.scheduleAtFixedRate(this, prop.getDelay()*1000, prop.getPeriod()*1000);
			}				
		} else {
			if(prop.getFirstTime() != null) {
				if(prop.getPeriod() != -1) {
					if(prop.getFirstTime().after(current)) {
						timer.schedule(this, prop.getFirstTime(), prop.getPeriod()*1000);
					} else {
						long count = (current.getTime() - prop.getFirstTime().getTime()) / (prop.getPeriod() * 1000) + 1;
						Date ajustDate = new Date(prop.getFirstTime().getTime()+prop.getPeriod()*1000*count);
						prop.setFirstTime(ajustDate);
						timer.schedule(this, prop.getFirstTime(), prop.getPeriod()*1000);
						prop.getTask().doTask();
					}					
				} else {
					timer.schedule(this, prop.getFirstTime());
				}
			} else {
				if(prop.getPeriod() != -1) {
					timer.schedule(this, prop.getDelay()*1000, prop.getPeriod()*1000);
				} else {
					timer.schedule(this, prop.getDelay()*1000);
				}
			}		
		}
	}
	
	public void stop() {
		timer.cancel();
	}

	public String getName() {
		return prop.getName();
	}
	
	public void run() {
		prop.getTask().doTask();
	}
	
}
