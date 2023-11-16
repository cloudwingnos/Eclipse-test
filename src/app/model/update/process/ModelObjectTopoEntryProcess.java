package app.model.update.process;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.util.Map.Entry;

import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;

import cn.sg.model.physical.PModelContainerWapper;

public class ModelObjectTopoEntryProcess implements EntryProcessor<String, PModelContainerWapper>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8496546302576957652L;

	public ModelObjectTopoEntryProcess() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public EntryBackupProcessor<String, PModelContainerWapper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process(Entry<String, PModelContainerWapper> entry) {
		PModelContainerWapper warpper = entry.getValue();
		return warpper.getContainer().getTopoMessage();
	}
	
	


}
