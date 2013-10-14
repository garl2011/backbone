package com.socix.data.impl;

public class ReplyData extends RRDataImpl {
	
	public ReplyData() {
		path = "";
	}

	public String getRequestID() {
		String[] paths = this.path.split("/");
		String[] requests = paths[paths.length - 2].split(":");
		return requests[0];
	}
	public String getRequestSeq() {
		String[] paths = this.path.split("/");
		String[] requests = paths[paths.length - 2].split(":");
		return requests[1];
	}
	public String getServiceID() {
		String[] paths = this.path.split("/");
		return paths[paths.length - 1];
	}
}
