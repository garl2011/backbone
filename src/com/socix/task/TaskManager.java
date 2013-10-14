package com.socix.task;

import java.util.HashMap;
import java.util.Map;

import com.socix.configure.GlobalInfo;

public class TaskManager {

	private static Map<String, ScheduleTask> taskContainer = new HashMap<String, ScheduleTask>();
	
	private TaskManager() {
	}
	
	public static void initTask() {
        String[] ids = GlobalInfo.getCategoryIDS("task");
        for(int i = 0; i < ids.length; i++) {
        	TaskProperties prop = new TaskProperties(ids[i]);
        	ScheduleTask tmp = new ScheduleTask(prop);
        	taskContainer.put(tmp.getName(), tmp);
        }
	}
	
	public static void startTasks() {
		for(String name : taskContainer.keySet()) {
			taskContainer.get(name).schedule();
		}
	}
	
	public static void stopTasks() {
		for(String name : taskContainer.keySet()) {
			taskContainer.get(name).stop();
		}
	}
	
}
