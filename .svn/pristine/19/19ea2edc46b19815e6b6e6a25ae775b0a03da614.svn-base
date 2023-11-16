package app.model.task;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

import com.google.protobuf.ByteString;
import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.KDMDGInstanceAware;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.IdentifiedDataSerializable;
import com.kedong.platform.imdg.scheduledexecutor.NamedTask;

import app.model.preset.CloRec;
import app.model.preset.process.ModelPointValueEntryProcess;
import client.PlatfromClient;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceModelMeas.ModelMeasResponse;
import message.ServicePointValue.PointValueList;
import message.ServicePointValue.PointValueList.Builder;
import message.ServicePointValue.RTKEY;
import message.ServiceRtnetCase.RtnetCaseResponse;
import warpper.MessageWarpper;

public class PointValueTask
		implements Callable<byte[]>, KDMDGInstanceAware, IdentifiedDataSerializable, NamedTask {
	PointValueList.Builder builderList;
	String key;

	public PointValueTask() {

	}

	public PointValueTask(PointValueList list, String key) {
		this.builderList = list.toBuilder();
		this.key = key;
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
		builderList = PointValueList.parseFrom(in.readByteArray()).toBuilder();
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(key);
		out.writeByteArray(builderList.build().toByteArray());
	}

	@Override
	public byte[] call() throws Exception {
		Builder modelList = PointValueList.newBuilder();
		Builder preValueList = PointValueList.newBuilder();
		Builder valueList = PointValueList.newBuilder();
		if (key.contains("grid_mirroring_model") || key.contains("scada") || key.contains("pdr")) {
			builderList.getValueBuilderList().forEach(value -> {
				RTKEY key = value.getId();
				short column_id = (short) (key.getId2() % 10000);
				String attributeName = PlatfromClient.getAttributeName(key.getId1(), column_id);
				if (attributeName != null) {
					value.setName(ByteString.copyFrom(attributeName.getBytes()));
					if ("voltage_type".equals(attributeName) || "name".equals(attributeName)||"tap_n".equals(attributeName)) {
						modelList.addValue(value);
					} else if ("tpcolor".equals(attributeName)) {
						valueList.addValue(value);
					} else {
						preValueList.addValue(value);
					}
				}
			});
		} else {
			builderList.getValueBuilderList().forEach(value -> {
				RTKEY key = value.getId();
				short column_id = (short) (key.getId2() % 10000);
				String attributeName = PlatfromClient.getAttributeName(key.getId1(), column_id);
				if (attributeName != null) {
					value.setName(ByteString.copyFrom(attributeName.getBytes()));
					if ("voltage_type".equals(attributeName) || "name".equals(attributeName)||"tap_n".equals(attributeName)) {
						modelList.addValue(value);
					} else if (attributeName.contains("_pre")) {
						attributeName = attributeName.replace("_pre", "");
						value.setName(ByteString.copyFrom(attributeName.getBytes()));
						preValueList.addValue(value);
					} else {
						valueList.addValue(value);
					}
				}
			});
		}
		Builder returnList = PointValueList.newBuilder();
		if (modelList.getValueCount() > 0) {
			PointValueList modelReturnList = ((PointValueList) hz
					.getMap(MessageWarpper.getCClassName(GridModelResponse.class))
					.executeOnKey(key, new ModelPointValueEntryProcess(modelList.build())));
			modelReturnList.getValueList().forEach(value -> {
				returnList.addValue(value);
			});
		}
		if (preValueList.getValueCount() > 0) {
			PointValueList preValueReturnList = (PointValueList) hz
					.getMap(MessageWarpper.getCClassName(ModelMeasResponse.class))
					.executeOnKey(key, new ModelPointValueEntryProcess(preValueList.build()));
			preValueReturnList.getValueList().forEach(value -> {
				returnList.addValue(value);
			});
		}
		if (valueList.getValueCount() > 0) {
			PointValueList valueReturnList = (PointValueList) hz
					.getMap(MessageWarpper.getCClassName(RtnetCaseResponse.class))
					.executeOnKey(key, new ModelPointValueEntryProcess(valueList.build()));
			valueReturnList.getValueList().forEach(value -> {
				returnList.addValue(value);
			});
		}
		mapAttributeLength(returnList);
		return returnList.build().toByteArray();
	}

	private void mapAttributeLength(Builder returnList) {
		returnList.getValueBuilderList().forEach(value -> {
			try {
				CloRec rec = PlatfromClient.getCloRecBase(value.getId().getId1(), value.getName().toString("gbk"));
				value.setType(rec.getType());
				value.setLen(rec.getLen());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		});

	}

	@Override
	public int getFactoryId() {
		return 1000;
	}

	@Override
	public int getId() {
		return 102;
	}

}
