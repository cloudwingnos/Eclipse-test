package app.model.copy.task;
/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
import java.io.IOException;
import java.util.concurrent.Callable;

import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.KDMDGInstanceAware;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.DataSerializable;
import com.kedong.platform.imdg.scheduledexecutor.NamedTask;


import client.PlatfromClient;
import cn.sg.common.ImdgLogger;

public class CopyTask implements Callable<Object>, KDMDGInstanceAware, DataSerializable, NamedTask {
	String fromKey;
	String toKey;
	public CopyTask() {

	}
	
	public CopyTask( String fromKey ,String toKey) {
		this.fromKey = fromKey ;
		this.toKey = toKey;
	}

	private transient KDMDGInstance hz;

	@Override
	public void setKDMDGInstance(KDMDGInstance localHz) {
		this.hz = localHz;
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.fromKey = in.readUTF();
		this.toKey = in.readUTF();
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(this.fromKey);
		out.writeUTF(this.toKey);
	}
	
	@Override
	public byte[] call() throws Exception {
		ImdgLogger.info(this.getName()+" running!");
		PlatfromClient client = new PlatfromClient(hz);
		client.copyModel(fromKey, toKey);
		return null;
		
	}
	

}
