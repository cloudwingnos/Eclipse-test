package app.model.update.process;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.io.Serializable;
import java.util.Map.Entry;

import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.KDMDGInstanceAware;
import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;

import app.model.preset.PreSetUpdateHelper;
import app.model.preset.process.ModelPointValueSetEntryProcess;
import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PModelContainerWapper;
import message.ServicePointValue.PointValueList.Builder;
import message.ServiceRtnetCase.RtnetCaseResponse;

/**
 * @author Donghao
 * @version date：2021年9月23日 下午3:45:36
 * 
 */
public class TopoEntryProcess implements EntryProcessor<String, PModelContainerWapper> ,Serializable ,KDMDGInstanceAware {
	/**
	 * 
	 */
	transient private static final long serialVersionUID = 1423859249777675285L;

	private transient KDMDGInstance hz;

	public TopoEntryProcess() {
		
	}

	@Override
	public Object process(Entry<String, PModelContainerWapper> entry) {
		PModelContainerWapper warpper = entry.getValue();
		if (warpper != null) {
			ImdgLogger.info("Topo task running : " + entry.getKey());
			PModelContainer container = warpper.getContainer();
			long time = System.currentTimeMillis();
			PlatfromClient client = new PlatfromClient(hz);
			Builder valueList = PreSetUpdateHelper.mapRtneResponse(container);
			if (valueList == null) {
				client.putProtoBuf(entry.getKey(), container.getRtnetCaseResponse().build());
			} else {
				client.getMap(RtnetCaseResponse.class).submitToKey(entry.getKey(),
						new ModelPointValueSetEntryProcess(valueList.build()));
			}
			entry.setValue(warpper);
			ImdgLogger.info("Topo task ended(ms) : " + (System.currentTimeMillis() - time));
		} else {
			ImdgLogger.info("Topo task start erro ,model is not exit! ");
		}
		return null;
	}

	@Override
	public EntryBackupProcessor<String, PModelContainerWapper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void setKDMDGInstance(KDMDGInstance imdgInstance) {
		this.hz = imdgInstance;
		
	}

}
