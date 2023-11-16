package app.model.table.task;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.IOException;
import java.util.concurrent.Callable;

import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.KDMDGInstanceAware;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.IdentifiedDataSerializable;
import com.kedong.platform.imdg.scheduledexecutor.NamedTask;

import app.model.table.process.ModelTableEntryProcess;
import client.PlatfromClient;
import message.ServiceGridModel.GridModelResponse;

public class TableTask implements Callable<byte[]>, KDMDGInstanceAware, IdentifiedDataSerializable, NamedTask {
	String tableName;
	String key;
	public TableTask() {

	}
	
	public TableTask( String key ,String name) {
		this.key = key ;
		this.tableName = name;
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
		key = in.readUTF();
		tableName = in.readUTF();
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(key);
		out.writeUTF(tableName);
	}
	
	@Override
	public byte[] call() throws Exception {
		PlatfromClient client = new PlatfromClient(hz);
		return (byte[]) client.getMap(GridModelResponse.class).executeOnKey(key, new ModelTableEntryProcess(tableName));
		
	}
	
	@Override
	public int getFactoryId() {
		return 1000;
	}


	@Override
	public int getId() {
		return 103;
	}

}
