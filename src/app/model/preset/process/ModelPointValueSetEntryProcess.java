package app.model.preset.process;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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

public class ModelPointValueSetEntryProcess
		extends BasePointValueEntryProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285904857536496767L;

	public ModelPointValueSetEntryProcess() {

	}

	public ModelPointValueSetEntryProcess(PointValueList PointValueList) {
		super(PointValueList);
	}


	@Override
	public Object process(Entry<String, Object> entry) {
		if (entry.getValue() != null) {
			if (entry.getValue() instanceof MessageWarpper) {
				MessageWarpper warpper = (MessageWarpper) entry.getValue();
				if (warpper != null) {
					GeneratedMessage response = warpper.getResponse();
					if (response instanceof GridModelResponse) {
						mapValue(warpper);
					} else if (response instanceof ModelMeasResponse) {
						mapPreValue(warpper);
					} else if (response instanceof RtnetCaseResponse) {
						mapValue(warpper);
					}
				}
				entry.setValue(warpper);
			}else if (entry.getValue() instanceof AddtionModelData) {
				AddtionModelData data = (AddtionModelData) entry.getValue();
				setData(data);
				entry.setValue(data);
			}
		}
		
		return null;
	}
	
	private void setData(AddtionModelData data) {
		this.builderList.getValueList().forEach(value -> {
			{
				if (data.getDataValue() == null) {
					data.setDataValue(new HashMap<>());
				}
				int id1 = (int) value.getId().getId1();
				if (data.getDataValue().get(id1) == null) {
					data.getDataValue().put(id1, new HashMap<Long, ByteString> ());
				}
				data.getDataValue().get(id1).put(value.getId().getId2(), value.getValue());
			}
		});
	}
	
	private void mapValue(MessageWarpper warpper) {
		builderList.getValueList().forEach(value -> {
			if (value.getName() != null) {
				long clo_id = value.getId().getId2() % 10000;
				long id = value.getId().getId1() + value.getId().getId2() - clo_id;
				Message.Builder obj = warpper.getDeviceModelMap().get(id);
				if (obj == null) {
					obj = warpper.getEqLoadModelMap().get(id);
				}
				try {
					setData(obj, value);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void mapPreValue(MessageWarpper warpper) {
		builderList.getValueBuilderList().forEach(value -> {
			if (value.getName() != null && value.getValue() != null) {
				long clo_id = value.getId().getId2() % 10000;
				long id = value.getId().getId1() + value.getId().getId2() - clo_id;
				Message.Builder base = warpper.getDeviceModelMap().get(id);
				if (base == null) {
					base =  warpper.getEqLoadModelMap().get(id);
				}
				if (base != null) {
					Builder obj = (Builder) base;
					int size = obj.getAttributeNameCount();
					for (int i = 0; i < size; i++) {
						if (value.getName().equals(obj.getAttributeName(i))) {
							try {
								String valueString = value.getValue().toString("gbk");
								float fValue = Float.valueOf(valueString);
								if (obj.getValue(i).getValueType() == 1) {
									obj.getValueBuilder(i).setFloatValue(fValue);
								} else {
									obj.getValueBuilder(i).setIntValue((int) fValue);
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							break;
						}
					}
				}
			}
		});

	}

	private void setData(com.google.protobuf.Message.Builder obj,
			PointValue value) throws UnsupportedEncodingException {
		if (obj != null) {
			String attributeName = value.getName().toString("gbk");
			Optional<Entry<FieldDescriptor, Object>> option = obj.getAllFields().entrySet().stream()
					.filter(entry -> entry.getKey().getName().equals(attributeName)).findAny();
			if (option.isPresent()) {
				Entry<FieldDescriptor, Object> fieldEntry = option.get();
				Object nextValue = null;
				String valueString = value.getValue().toString("gbk");
				if (fieldEntry.getKey().getType() == FieldDescriptor.Type.INT64) {
					nextValue = Long.valueOf(valueString);
				} else if (fieldEntry.getKey().getType() == FieldDescriptor.Type.BYTES) {
					nextValue = value.getValue();
				} else if (fieldEntry.getKey().getType() == FieldDescriptor.Type.INT32) {
					nextValue = (int) Float.valueOf(valueString).longValue();
				} else if (fieldEntry.getKey().getType() == FieldDescriptor.Type.FLOAT) {
					nextValue = Float.valueOf(valueString);
				}
				if (nextValue != null) {
					obj.setField(fieldEntry.getKey(), nextValue);
				}
			}  
		}
	}
	
	@Override
	public int getId() {
		return 104;
	}

}
