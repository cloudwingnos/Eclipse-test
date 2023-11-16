package app.model.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author Donghao
 * @version date：2022年4月20日 上午9:10:44
 * 
 */
public class ServerData extends BaseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6120799571563186365L;
	public String type;
	public String context_name;
	public String app_name;
	public String model_name;
	public String index;
	public List<OperationData> opr_info;
	public String from_context_name;
	public String from_app_name;
	public String from_model_name;
	public String dev_id;
	public String table_name;
	public String dev_type;
	public TableBean table;
	public String getContext_name() {
		return context_name;
	}

	public void setContext_name(String context_name) {
		this.context_name = context_name;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public List<OperationData> getOpr_info() {
		if (opr_info == null) {
			opr_info = new ArrayList<OperationData>();
		}
		return opr_info;
	}

	public void setOpr_info(List<OperationData> opr_info) {
		this.opr_info = opr_info;
	}

	public static ServerData createServerData(String str) {
		return new Gson().fromJson(str, ServerData.class);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom_context_name() {
		return from_context_name;
	}

	public void setFrom_context_name(String from_context_name) {
		this.from_context_name = from_context_name;
	}

	public String getFrom_app_name() {
		return from_app_name;
	}

	public void setFrom_app_name(String from_app_name) {
		this.from_app_name = from_app_name;
	}

	public String getFrom_model_name() {
		return from_model_name;
	}

	public void setFrom_model_name(String from_model_name) {
		this.from_model_name = from_model_name;
	}

	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	

	public String getKey() {
		return this.context_name + this.app_name + this.model_name;
	}
	public String getFromKey() {
		return this.from_context_name + this.from_app_name + this.getFrom_model_name();
	}
	public String getDev_id() {
		return dev_id;
	}

	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}

	public String getDev_type() {
		return dev_type;
	}

	public void setDev_type(String dev_type) {
		this.dev_type = dev_type;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public TableBean getTable() {
		if(table==null) {
			table = new TableBean();
		}
		return table;
	}

	public void setTable(TableBean table) {
		this.table = table;
	}

}
