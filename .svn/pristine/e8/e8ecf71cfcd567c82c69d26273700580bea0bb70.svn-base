package app.model.copy.process;
/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
import java.io.Serializable;
import java.util.Map.Entry;

import com.google.protobuf.GeneratedMessage;
import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;

import cn.sg.common.ImdgLogger;
import warpper.MessageWarpper;

public class GetEntryProcess implements EntryProcessor<String, MessageWarpper>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2382414618176570803L;


	public GetEntryProcess() {
		super();
	}


	@Override
	public Object process(Entry<String, MessageWarpper> entry) {
		long time = System.currentTimeMillis();
		MessageWarpper warpper = entry.getValue();
		if (warpper != null) {
			GeneratedMessage response = warpper.getResponse();
			byte[] array = response.toByteArray();
			ImdgLogger.info("warpper.getResponse().toByteArray() time :" + (System.currentTimeMillis() - time));
			return array;
		}
		return new byte[0];
	}


	@Override
	public EntryBackupProcessor<String, MessageWarpper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}


	

}
