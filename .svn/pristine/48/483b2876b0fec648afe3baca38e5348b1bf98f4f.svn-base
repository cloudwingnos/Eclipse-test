package app.model.preset.process;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.DataSerializable;
import com.google.protobuf.Descriptors.FieldDescriptor;

import message.ServiceGridModel.GridModelAcline;
import message.ServiceGridModel.GridModelAclineend;
import message.ServiceGridModel.GridModelBreaker;
import message.ServiceGridModel.GridModelCp;
import message.ServiceGridModel.GridModelCvt;
import message.ServiceGridModel.GridModelDis;
import message.ServiceGridModel.GridModelElcBus;
import message.ServiceGridModel.GridModelLoad;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceGridModel.GridModelScp;
import message.ServiceGridModel.GridModelTransfm;
import message.ServiceGridModel.GridModelTransfmwd;
import message.ServiceGridModel.GridModelUnit;
import message.ServiceGridModel.RTKEY;
import message.ServiceModelMeas.ModelMeasResponse;
import message.ServiceModelMeas.RTMeas.Builder;
import message.ServicePointValue.PointValue;
import message.ServicePointValue.PointValueList;
import message.ServiceRtnetCase.NasAcDotResult;
import message.ServiceRtnetCase.NasBreakResult;
import message.ServiceRtnetCase.NasBusResult;
import message.ServiceRtnetCase.NasCompensatorPResult;
import message.ServiceRtnetCase.NasCompensatorSResult;
import message.ServiceRtnetCase.NasConverterResult;
import message.ServiceRtnetCase.NasDcBreakResult;
import message.ServiceRtnetCase.NasDcDisconnectorResult;
import message.ServiceRtnetCase.NasDisconnectorResult;
import message.ServiceRtnetCase.NasLoadResult;
import message.ServiceRtnetCase.NasTrwdResult;
import message.ServiceRtnetCase.NasUnitResult;
import message.ServiceRtnetCase.RtnetCaseResponse;
import warpper.MessageWarpper;

public class GetEqloadIdEntryProcess implements EntryProcessor<String, MessageWarpper>, DataSerializable {
	/**
	 * 
	 */

	String dev_id;

	public GetEqloadIdEntryProcess() {

	}

	public GetEqloadIdEntryProcess(String dev_id) {
		this.dev_id = dev_id;
	}

	@Override
	public EntryBackupProcessor<String, MessageWarpper> getBackupProcessor() {
		return null;
	}

	@Override
	public Object process(Entry<String, MessageWarpper> entry) {
		MessageWarpper warpper = entry.getValue();
		if (warpper != null) {
			Message.Builder response = warpper.getBuilder();
			if (response instanceof GridModelResponse.Builder) {
				String[] devIdArray = dev_id.split(":");
				if (devIdArray.length == 2) {
					long id = Long.valueOf(devIdArray[0]) + Long.valueOf(devIdArray[1]);
					Message.Builder base =  warpper.getEqLoadModelMap().get(id);
					if(base!=null) {
						GridModelLoad.Builder load = (GridModelLoad.Builder)base;
						return load.getId().getId1()+":"+load.getId().getId2();
					}
				}
			}
		}
		return "";
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.dev_id = in.readUTF();
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(dev_id);
	}

}
