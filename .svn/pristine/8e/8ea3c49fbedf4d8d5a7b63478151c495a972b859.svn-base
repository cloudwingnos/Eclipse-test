package client;

import message.ServicePointValue.PointValueList;
import message.ServicePointValue.PointValueList.Builder;
@Deprecated
public class PointAdapter {

	private Builder valueList ;

	public PointAdapter() {
		super();
		this.valueList =  PointValueList.newBuilder();;
	}
	
	public void addPoint(long id1, long id2, int len, int type, Object value) {
		valueList.addValue(PlatfromClient.generatePresetValue(id1, id2, len, type, value));
	}

	public Builder getValueList() {
		return valueList;
	}

	public void setValueList(Builder valueList) {
		this.valueList = valueList;
	};

}
