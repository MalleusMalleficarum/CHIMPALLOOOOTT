package edu.kit.ipd.creativecrowd.crowdplatform;
/**
 * this class represents a pybossa taskrun, however it does not contain
 * all fields retrievable from the pybossa server. This class is mainly
 * used by JACKSON.
 * @author Robin
 *
 */
class PyBossaTaskrun {
	private int info;
	private int user_id;
	private int user_ip;
	private int project_id;
	private int task_id;
	private int id;
	
	public PyBossaTaskrun() {
		
	}

	public int getInfo() {
		return info;
	}
	public void setInfo(int info) {
		this.info = info;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(int user_ip) {
		this.user_ip = user_ip;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
