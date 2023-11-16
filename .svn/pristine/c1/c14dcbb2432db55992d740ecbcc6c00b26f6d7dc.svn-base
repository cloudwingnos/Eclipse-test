package app.model.preset.process;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

import app.model.data.AddtionModelData;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceModelMeas.ModelMeasResponse;
import message.ServiceModelMeas.RTMeas.Builder;
import message.ServicePointValue.PointValue;
import message.ServicePointValue.PointValueList;
import message.ServiceRtnetCase.RtnetCaseResponse;
import warpper.MessageWarpper;

public class ModelPointValueGetEntryProcess extends BasePointValueEntryProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7692640881813842406L;

	public ModelPointValueGetEntryProcess() {

	}

	public ModelPointValueGetEntryProcess(PointValueList PointValueList) {
		super(PointValueList);
	}

	@Override
	public Object process(Entry<String, Object> entry) {
		if (entry.getValue() != null) {
			if (entry.getValue() instanceof MessageWarpper) {
				MessageWarpper warpper = (MessageWarpper) entry.getValue();
				if (warpper != null) {
					Message.Builder response = warpper.getBuilder();
					if (response instanceof GridModelResponse.Builder) {
						mapValue(warpper);
					} else if (response instanceof ModelMeasResponse.Builder) {
						mapPreValue(warpper);
					} else if (response instanceof RtnetCaseResponse.Builder) {
						mapValue(warpper);
					}
				}
			} else if (entry.getValue() instanceof AddtionModelData) {
				AddtionModelData data = (AddtionModelData) entry.getValue();
				getData(data);
			}
		}
		return this.builderList.build().toByteArray();
	}

	private void getData(AddtionModelData data) {
		this.builderList.getValueBuilderList().forEach(value -> {
			{
				if (data.getDataValue() != null) {
					int id1 = (int) value.getId().getId1();
					if (data.getDataValue().get(id1) != null) {
						ByteString v = data.getDataValue().get(id1).get(value.getId().getId2());
						if (v != null) {
							value.setValue(v);
						}
					}
				}
			}
		});

	}

	final static ByteString one = ByteString.copyFrom("1".getBytes());
	final static ByteString zero = ByteString.copyFrom("0".getBytes());

	private void mapValue(MessageWarpper warpper) {
		builderList.getValueBuilderList().forEach(value -> {
			if (value.getName() != ByteString.EMPTY) {
				long clo_id = value.getId().getId2() % 10000;
				long id = value.getId().getId1() + value.getId().getId2() - clo_id;

				Message.Builder obj = warpper.getDeviceModelMap().get(id);
				if (obj == null) {
					obj = warpper.getEqLoadModelMap().get(id);
				}
				try {
					getData(obj, value);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				long clo_id = value.getId().getId2() % 10000;
				long id = value.getId().getId1() + value.getId().getId2() - clo_id;

				Message.Builder obj = warpper.getDeviceModelMap().get(id);
				if (obj != null) {
					value.setValue(one);
				} else {
					value.setValue(zero);
				}
			}
		});
	}

	private void mapPreValue(MessageWarpper warpper) {
		builderList.getValueBuilderList().forEach(value -> {
			if (value.getName() != null) {
				long clo_id = value.getId().getId2() % 10000;
				long id = value.getId().getId1() + value.getId().getId2() - clo_id;
				Message.Builder base = warpper.getDeviceModelMap().get(id);
				if (base == null) {
					base = warpper.getEqLoadModelMap().get(id);
				}
				if (base != null) {
					Builder obj = (Builder) base;
					int size = obj.getAttributeNameCount();
					for (int i = 0; i < size; i++) {
						if (value.getName().equals(obj.getAttributeName(i))) {
							if (obj.getValue(i).getValueType() == 1) {
								value.setValue(getByteString(obj.getValue(i).getFloatValue()));
							} else {
								value.setValue(getByteString(obj.getValue(i).getIntValue()));
							}
							break;
						}
					}
				}
			}
		});
	}

	private void getData(Message.Builder obj,
			PointValue.Builder value) throws UnsupportedEncodingException {
		if (obj != null) {
			String attributeName = value.getName().toString("gbk");
			Optional<Entry<FieldDescriptor, Object>> option = obj.getAllFields().entrySet().stream()
					.filter(entry -> entry.getKey().getName().equals(attributeName)).findAny();
			if (option.isPresent()) {
				Entry<FieldDescriptor, Object> fieldEntry = option.get();
				if (fieldEntry.getKey().getType() == FieldDescriptor.Type.BYTES) {
					value.setValue((ByteString) fieldEntry.getValue());
				} else {
					value.setValue(getByteString(fieldEntry.getValue()));
				}

			}
		}
	}

	@Override
	public int getId() {
		return 105;
	}

}
