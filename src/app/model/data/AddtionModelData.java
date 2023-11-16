package app.model.data;
/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
import java.io.IOException;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.IdentifiedDataSerializable;

public class AddtionModelData implements IdentifiedDataSerializable{

	Map<Integer, Map<Long, ByteString>> dataValue ;
	
	public Map<Integer, Map<Long, ByteString>> getDataValue() {
		return dataValue;
	}

	public void setDataValue(Map<Integer, Map<Long, ByteString>> dataValue) {
		this.dataValue = dataValue;
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFactoryId() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 106;
	}

}
