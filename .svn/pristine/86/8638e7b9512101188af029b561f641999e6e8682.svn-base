package app.model.table.process;
/** 

* @author  Donghao.F 

* @date 2021 Jul 14 14:24:00 

* 

*/
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.kedong.platform.imdg.map.EntryBackupProcessor;
import com.kedong.platform.imdg.map.EntryProcessor;
import com.kedong.platform.imdg.nio.ObjectDataInput;
import com.kedong.platform.imdg.nio.ObjectDataOutput;
import com.kedong.platform.imdg.nio.serialization.DataSerializable;

import cn.sg.common.ImdgLogger;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceGridModel.RTKEY;
import message.ServiceTable.RowValue;
import message.ServiceTable.Table;
import warpper.MessageWarpper;

public class ModelTableEntryProcess implements EntryProcessor<String, MessageWarpper>, DataSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 289754258041726272L;
	String tableName;

	public ModelTableEntryProcess() {

	}

	public ModelTableEntryProcess(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public EntryBackupProcessor<String, MessageWarpper> getBackupProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process(Entry<String, MessageWarpper> entry) {
		MessageWarpper warpper = entry.getValue();
		Table.Builder table = Table.newBuilder();
		long time = System.currentTimeMillis();
		if (warpper != null) {
			 GeneratedMessage base = warpper.getResponse();
			if (base instanceof GridModelResponse) {
				GridModelResponse response = (GridModelResponse)base;
				if (tableName.equals("GridModelSubstation")) {
					mapTable(response.getStinfoList(), table);
				}
				else if (tableName.equals("GridModelAcline")) {
					mapTable(response.getAclninfoList(), table);
				}
				else if (tableName.equals("GridModelAclineend")) {
					mapTable(response.getAclnendinfoList(), table);
				}
				else if (tableName.equals("GridModelBaseValue")) {
					mapTable(response.getBasevlList(), table);
				}
				else if (tableName.equals("GridModelBreaker")) {
					mapTable(response.getBreakinfoList(), table);
				}
				else if (tableName.equals("GridModelDis")) {
					mapTable(response.getDisinfoList(), table);
				}
				else if (tableName.equals("GridModelCp")) {
					mapTable(response.getCpinfoList(), table);
				}
				else if (tableName.equals("GridModelCvt")) {
					mapTable(response.getCvtinfoList(), table);
				}
				else if (tableName.equals("GridModelDcDis")) {
					mapTable(response.getDcdisinfoList(), table);
				}
				else if (tableName.equals("GridModelDcfilter")) {
					mapTable(response.getDcfilterList(), table);
				}
				else if (tableName.equals("GridModelDclineend")) {
					mapTable(response.getDclineendinfoList(), table);
				}
				else if (tableName.equals("GridModelDcline")) {
					mapTable(response.getDclineinfoList(), table);
				}
				else if (tableName.equals("GridModelDcpole")) {
					mapTable(response.getDcpoleinfoList(), table);
				}
				else if (tableName.equals("GridModelDcsmoothingreactor")) {
					mapTable(response.getDcsmoothingreactorList(), table);
				}
				else if (tableName.equals("GridModelDCWAVETRAPPER")) {
					mapTable(response.getDcwavetrapperList(), table);
				}
				else if (tableName.equals("GridModelDcsys")) {
					mapTable(response.getDcsysinfoList(), table);
				}
				else if (tableName.equals("GridModelDcBreaker")) {
					mapTable(response.getDisbrkinfoList(), table);
				}
				else if (tableName.equals("GridModelArea")) {
					mapTable(response.getDivinfoList(), table);
				}
				else if (tableName.equals("GridModelElcBus")) {
					mapTable(response.getElcbusinfoList(), table);
				}
				else if (tableName.equals("GridModelHVDCGroundStation")) {
					mapTable(response.getHvdcgroundstationList(), table);
				}
				else if (tableName.equals("GridModelHVDCGroundSYS")) {
					mapTable(response.getHvdcgroundsysList(), table);
				}
				else if (tableName.equals("GridModelLoad")) {
					mapTable(response.getLoadinfoList(), table);
				}
				else if (tableName.equals("GridModelScp")) {
					mapTable(response.getScpinfoList(), table);
				}
				else if (tableName.equals("GridModelUnit")) {
					mapTable(response.getUnitinfoList(), table);
				}
				else if (tableName.equals("GridModelTransfm")) {
					mapTable(response.getXfmrinfoList(), table);
				}
				else if (tableName.equals("GridModelTransfmwd")) {
					mapTable(response.getXfwdinfoList(), table);
				}
				else if (tableName.equals("GridModelDCGROUND")) {
					mapTable(response.getDcgroundList(), table);
				}
			}
		}
		byte[] array = table.build().toByteArray();
		ImdgLogger.info("table task time(ms)" + (System.currentTimeMillis() - time));
		return array;
	}
	

	private void mapTable(List<? extends GeneratedMessage> infoList, message.ServiceTable.Table.Builder table) {
		if (infoList.size() > 0) {
			GeneratedMessage obj1 = infoList.get(0);
			obj1.getAllFields().forEach((k, v) -> {
				table.addAttributeName(ByteString.copyFrom(k.getName().getBytes()));
			});
			infoList.forEach(obj -> {
				RowValue.Builder rowValue = table.addRowValueBuilder();
				obj.getAllFields().forEach((k, v) -> {
					FieldDescriptor.Type type = k.getType();
					rowValue.addValue(getString(type, v));
				});
			});
		}
	}

	private static ByteString getString(FieldDescriptor.Type type, Object value) {
		switch (type) {
		case INT32:
		case SINT32:
		case SFIXED32:
			return ByteString.copyFrom(((Integer) value).toString().getBytes());
		case INT64:
		case SINT64:
		case SFIXED64:
			return ByteString.copyFrom(((Long) value).toString().getBytes());
		case BOOL:
			return ByteString.copyFrom((((Boolean) value).toString()).getBytes());
		case FLOAT:
			return ByteString.copyFrom(((Float) value).toString().getBytes());
		case DOUBLE:
			return ByteString.copyFrom(((Double) value).toString().getBytes());
		case BYTES:
			return ((ByteString) value);
		case MESSAGE:
			RTKEY key = ((RTKEY) value);
			return ByteString.copyFrom(
					new StringBuilder().append(key.getId1()).append(":").append(key.getId2()).toString().getBytes());
		default:
			break;
		}
		return null;
	}
	@Override
	public void readData(ObjectDataInput in) throws IOException {
		tableName  = in.readUTF();
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(tableName);
	}

}
