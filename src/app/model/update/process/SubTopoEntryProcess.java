package app.model.update.process;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;

import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;

import cn.sg.common.ImdgLogger;
import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PModelContainerWapper;
import cn.sg.model.physical.core.Substation;
import cn.sg.topo.impl.SubstationTopologyProcessorImpl;

/**
 * @author Donghao
 * @version date��2021��9��23�� ����3:45:36
 * 
 */
public class SubTopoEntryProcess implements EntryProcessor<String, PModelContainerWapper> ,Serializable  {
	/**
	 * 
	 */
	transient private static final long serialVersionUID = 1423859249777675285L;


	List<Long> idList;
	public SubTopoEntryProcess() {
		
	}

	
	public SubTopoEntryProcess(List<Long> idList) {
		super();
		this.idList = idList;
	}


	@Override
	public Object process(Entry<String, PModelContainerWapper> entry) {
		PModelContainerWapper warpper = entry.getValue();
		if (warpper != null) {
//			ImdgLogger.info("Topo task running : " + entry.getKey());
			PModelContainer container = warpper.getContainer();
			idList.parallelStream().forEach(id -> {
				Substation sub = container.getSubstation(id);
				if (sub != null) {
					new SubstationTopologyProcessorImpl(sub).topologyAnalysis(false);
				}
			});
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


}
