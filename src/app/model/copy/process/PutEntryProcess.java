package app.model.copy.process;
/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
import java.io.IOException;
import java.util.Map.Entry;

import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.DataSerializable;

import cn.sg.common.ImdgLogger;
import warpper.MessageWarpper;

public class PutEntryProcess implements EntryProcessor<String, MessageWarpper>, DataSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8652760269367665425L;

	byte[] byteArray;

	String typeName;


	
	public PutEntryProcess() {
		super();
	}


	public PutEntryProcess(String typeName, byte[] byteArray) {
		super();
		this.byteArray = byteArray;
		this.typeName = typeName;
	}


	@Override
	public Object process(Entry<String, MessageWarpper> entry) {
		long time = System.currentTimeMillis();
		MessageWarpper warpper = new MessageWarpper();
		warpper.build(typeName, byteArray);
		entry.setValue(warpper);
		ImdgLogger.info(warpper.getBuilder().getClass().getSimpleName() + "," + entry.getKey()
				+ ",warpper.build(typeName, byteArray) time :" + (System.currentTimeMillis() - time));
		return null;
	}


	@Override
	public EntryBackupProcessor<String, MessageWarpper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeByteArray(byteArray);
		out.writeUTF(typeName);
		
	}


	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.byteArray=in.readByteArray();
		this.typeName=in.readUTF();
		
	}


	

}
