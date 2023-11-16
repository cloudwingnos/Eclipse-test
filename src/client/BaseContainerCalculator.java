package client;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.util.List;

import cn.sg.model.physical.PModelContainer;
import message.ServicePointValue.PointValueList.Builder;

public abstract class BaseContainerCalculator {

	private PointAdapter adapter;
	
	private List<String> keyList;
	
	public BaseContainerCalculator(List<String> keyList) {
		this.keyList = keyList;
		this.adapter = new PointAdapter();
	}

	abstract public void calulate(PModelContainer container, PointAdapter adapter);
//	

	public Builder calculate(PModelContainer container) {
		calulate(container, adapter);
		return this.adapter.getValueList();
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	
}
