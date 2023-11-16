package app.model.update.process;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.DataSerializable;

import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PModelContainerWapper;
import cn.sg.model.physical.common.BasePModelObject;
import data.ModelUpdateData;

public class ModelObjectUpdateEntryProcess implements EntryProcessor<String, PModelContainerWapper> , DataSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5281077126420394741L;
	List<ModelUpdateData> dataList;

	public ModelObjectUpdateEntryProcess() {
		// TODO Auto-generated constructor stub
	}
	public ModelObjectUpdateEntryProcess(List<ModelUpdateData> dataList) {
		this.dataList = dataList;
	}

	@Override
	public EntryBackupProcessor<String, PModelContainerWapper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process(Entry<String, PModelContainerWapper> entry) {
//		throw new RuntimeException("Programming error: this function should be recide: " + this.getClass().getSimpleName());
		PModelContainerWapper warpper = entry.getValue();
		if (warpper != null) {
			PModelContainer container = warpper.getContainer();
			updateContainer(dataList,container);
			entry.setValue(warpper);
		}
		return null;
	}

	public static void updateContainer(List<ModelUpdateData> dataList, PModelContainer container) {
		dataList.forEach(data -> {
			BasePModelObject<?, ?> obj = container.getDevice(data.getDeviceId());
			if (obj != null) {
				obj.getMeas(data.getAttributeName()).setIntValue((int) data.getValue());
			}
		});
	}
	
	@Override
	public void readData(ObjectDataInput in) throws IOException {
		int size = in.readInt();
		this.dataList = new ArrayList<ModelUpdateData>();
		for (int i = 0; i < size; i++) {
			ModelUpdateData data = new ModelUpdateData();
			data.readData(in);
			this.dataList.add(data);
		}
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeInt(this.dataList.size());
		this.dataList.forEach(data->{
			try {
				data.writeData(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	

}
