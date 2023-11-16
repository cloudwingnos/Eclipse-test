package app.model.data;

import com.google.gson.Gson;

/**
 * @author Donghao
 * @version date：2022年4月20日 上午9:10:44
 * 
 */
public class OperationData extends BaseData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2235929880494457756L;
	public String dev_id;
	public String value;

	public String attribute;
	
	
	public OperationData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationData(String dev_id, String value, String attribute) {
		super();
		this.dev_id = dev_id;
		this.value = value;
		this.attribute = attribute;
	}

	public String getDev_id() {
		return dev_id;
	}




	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}




	public String getValue() {
		return value;
	}




	public void setValue(String value) {
		this.value = value;
	}




	public String getAttribute() {
		return attribute;
	}




	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}




	public static OperationData createOperationData(String str) {
		return new Gson().fromJson(str, OperationData.class);
	}
	
	
	

}
