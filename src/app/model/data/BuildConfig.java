package app.model.data;
/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
import java.io.Serializable;

public class BuildConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4317521235423949184L;
	String ip = "127.0.0.1";
	long period;
	boolean isUpdateParmeter;
	long publishPeriod;
	
	public BuildConfig(Long period, long isUpdateParmeter,long publishPeriod) {
		this.period = period;
		this.isUpdateParmeter = isUpdateParmeter == 1 ? true : false;
		this.publishPeriod =publishPeriod;
	}
	
	public long getPeriod() {
		return period;
	}
	public void setPeriod(long period) {
		this.period = period;
	}
	public boolean isUpdateParmeter() {
		return isUpdateParmeter;
	}
	public void setUpdateParmeter(boolean isUpdateParmeter) {
		this.isUpdateParmeter = isUpdateParmeter;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getPublishPeriod() {
		return publishPeriod;
	}

	public void setPublishPeriod(long publishPeriod) {
		this.publishPeriod = publishPeriod;
	}

	@Override
	public String toString() {
		return "BuildConfig [ip=" + ip + ", period=" + period + ", isUpdateParmeter=" + isUpdateParmeter
				+ ", publishPeriod=" + publishPeriod + "]";
	}
	
}
