package app.model.update.task;

import java.io.Serializable;
import java.util.Map.Entry;

import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.KDMDGInstanceAware;
import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;

import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sg.model.PModelProtoBufAdapter;
import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PModelContainerWapper;
import cn.sg.model.physical.PhysicalModelFactory;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceModelMeas.ModelMeasResponse;

/**
 * @author Donghao
 * @version date：2021年9月23日 下午3:45:36
 * 
 */
public class BuildContainerEntryProcess implements EntryProcessor<String, PModelContainerWapper>, KDMDGInstanceAware, Serializable {
	/**
	 * 
	 */
	transient private static final long serialVersionUID = 1423859249777675285L;

	private transient KDMDGInstance hz;

	public BuildContainerEntryProcess() {
		super();
	}
	

	@Override
	public void setKDMDGInstance(KDMDGInstance hz) {
		this.hz = hz;
	}



	@Override
	public Object process(Entry<String, PModelContainerWapper> entry) {
		ImdgLogger.info("ContainerBuildTask  running : " + entry.getKey());
		
		long time = System.currentTimeMillis();
		PlatfromClient client = new PlatfromClient(hz);
		GridModelResponse model = (GridModelResponse) client.getProtoBuf(entry.getKey(), GridModelResponse.class);
		ModelMeasResponse rtu = (ModelMeasResponse) client.getProtoBuf(entry.getKey(), ModelMeasResponse.class);
		if (model != null && rtu != null) {
			PModelContainer container = PhysicalModelFactory.eINSTANCE.createPModelContainer();
			PModelProtoBufAdapter adapter = new PModelProtoBufAdapter(container);
			adapter.mapData(model.toBuilder());
			adapter.mapRTUData(rtu.toBuilder());
			entry.setValue(new PModelContainerWapper(container));
			ImdgLogger.info("ContainerBuildTask ended(ms) : " + (System.currentTimeMillis() - time));
		} else {
			ImdgLogger.info("model is not exit : " + entry.getKey());
		}
		return null;
	}

	@Override
	public EntryBackupProcessor<String, PModelContainerWapper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
