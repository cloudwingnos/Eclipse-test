package app.model.preset;
/** 

* @author  Donghao.F 

* @date 2021 Jun 11 09:24:00 

* 

*/
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.ByteString;

import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sg.model.PModelProtoBufAdapter;
import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PhysicalModelFactory;
import cn.sg.model.physical.common.BasePModelObject;
import cn.sg.model.physical.common.Edge;
import cn.sg.model.physical.generation.production.GeneratingUnit;
import cn.sg.model.physical.topology.AcLineEnd;
import cn.sg.model.physical.wires.Breaker;
import cn.sg.model.physical.wires.BusbarSection;
import cn.sg.model.physical.wires.DcBreaker;
import cn.sg.model.physical.wires.DcDisconnector;
import cn.sg.model.physical.wires.Disconnector;
import cn.sg.model.physical.wires.EnergyConsumer;
import cn.sg.model.physical.wires.RectifierInverter;
import cn.sg.model.physical.wires.SeriesCompensator;
import cn.sg.model.physical.wires.ShuntCompensator;
import cn.sg.model.physical.wires.TransformerWinding;
import cn.sg.topo.ContainerTopology;
import data.Byte2Gbk;
import message.ServiceGridModel;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceGridModel.RTKEY;
import message.ServiceModelMeas.ModelMeasResponse;
import message.ServiceModelMeas.RTMeasValue;
import message.ServicePointValue.PointValue;
import message.ServicePointValue.PointValueList;
import message.ServicePointValue.PointValueList.Builder;
import message.ServiceRtnetCase;
import message.ServiceRtnetCase.NasCaseInfo;
import message.ServiceRtnetCase.RtnetCaseResponse;
@Deprecated
public class PreSetValueGenerator {

	private PlatfromClient client;
	
	
	public PreSetValueGenerator(PlatfromClient client) {
		this.client = client;
		
	}

	private Map<Long, Long> eqloadMap = new ConcurrentHashMap<Long, Long>();

	private Set<Long> saveDeviceSet = new HashSet<Long>();
	
	public void mapModel(GridModelResponse model, PointValueList.Builder presetValueList) {
		Map<ServiceGridModel.RTKEY, Integer> baseVoltageMap = new ConcurrentHashMap<ServiceGridModel.RTKEY, Integer>();
		model.getAclninfoList().forEach(obj -> {
			baseVoltageMap.put(obj.getId(), obj.getVolType());
		});
		model.getAclnendinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), baseVoltageMap.get(obj.getAclnsegId())));
		});
		model.getElcbusinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		
		model.getXfwdinfoList().forEach(obj -> {
			saveDeviceSet.add(obj.getId().getId1() + obj.getId().getId2());
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getLoadinfoList().forEach(obj -> {
			if (obj.getId().getId1() != 418) {
				presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
						PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
			} else {
				RTKEY deviceKey = obj.getDevId();
//				presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
//						client.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
				if (deviceKey != null) {
					eqloadMap.put(obj.getId().getId1() + obj.getId().getId2(), deviceKey.getId1() + deviceKey.getId2());
					if (!saveDeviceSet.contains(deviceKey.getId1() + deviceKey.getId2())) {
						presetValueList.addValue(client.generatePresetValue(deviceKey.getId1(), deviceKey.getId2(),
								PlatfromClient.getCloRec(deviceKey.getId1(), "voltage_type"), obj.getVolType()));
					}
				}
			}
		});
		model.getUnitinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getCpinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getScpinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getCvtinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getAcvolType()));
		});
		model.getDisinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getBreakinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		model.getXfmrinfoList().forEach(obj -> {
			presetValueList.addValue(client.generatePresetValue(obj.getId().getId1(), obj.getId().getId2(),
					PlatfromClient.getCloRec(obj.getId().getId1(), "voltage_type"), obj.getVolType()));
		});
		
		ImdgLogger.info("[model preset finish ]");

	}
/*
	public void mapRtu(ModelMeasResponse rtu, PointValueList.Builder presetValueList) {
		
		
		rtu.getRtMeasinfoList().forEach(obj -> {
			int size = obj.getAttributeNameCount();
			for (int i = 0; i < size; i++) {
				message.ServiceModelMeas.RTKEY id = obj.getId();
				RTMeasValue value = obj.getValue(i);
				if (id.getId1() != 418) {
					if (value.getValueType() == 1) {
						if (obj.getId().getId1() == 1312
								&& obj.getAttributeName(i).toStringGbk().equals("v")) {

						} 
						//q cp
						else if (obj.getId().getId1() == 1402
								&& obj.getAttributeName(i).toStringGbk().equals("q")) {

						} 
						else if (obj.getId().getId1() == 1401
								&& obj.getAttributeName(i).toStringGbk().equals("q")) {

						} 
						else if (obj.getId().getId1() == 1301
								&& obj.getAttributeName(i).toStringGbk().equals("f")) {

						}
						else {
							presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(),
									client.getCloRec(id.getId1(),
											new StringBuffer().append(obj.getAttributeName(i).toStringGbk())
													.append("_pre").toString()),
									obj.getValue(i).getFloatValue()));
						}
					} else if (value.getValueType() == 0) {
						//winding tap 
						if (obj.getId().getId1() == 1312) {
							presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(),
									client.getCloRec(id.getId1(), "tap_pre"), obj.getValue(i).getIntValue()));
						} else {
							presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(),
									client.getCloRec(id.getId1(), "pnt_pre"), obj.getValue(i).getIntValue()));
						}
					}
				}else {
					long objId = obj.getId().getId1() + obj.getId().getId2();
					if (eqloadMap.get(objId) != null) {
						long deviceId = (long) eqloadMap.get(objId);
						long deviceId_id1 = deviceId % 10000;
						// TODO fixed bug
						if (!saveDeviceSet.contains(deviceId)) {
							presetValueList.addValue(client.generatePresetValue(deviceId_id1, deviceId - deviceId_id1,
									client.getCloRec(deviceId_id1,
											new StringBuffer().append(obj.getAttributeName(i).toStringGbk())
													.append("_pre").toString()),
									obj.getValue(i).getFloatValue()));
						}
					}
				}
			}
			
//			meas.getId()
		});
		ImdgLogger.info("[rtu preset finish ]");
	}
	
	*/
	
	@Deprecated
	public void mapRtu(ModelMeasResponse rtu, PointValueList.Builder presetValueList, String prefix) {
		rtu.getRtMeasinfoList().forEach(obj -> {
			int size = obj.getAttributeNameCount();
			for (int i = 0; i < size; i++) {
				message.ServiceModelMeas.RTKEY id = obj.getId();
				RTMeasValue rtMeasValue = obj.getValue(i);
				Object value = getMeasValue(rtMeasValue);
				String attributeName  = Byte2Gbk.byteString2Gbk(obj.getAttributeName(i));
				if (attributeName.equals("point") || attributeName.equals("v") || attributeName.equals("f")
						|| id.getId1() == 418) {
					continue;
				}
				presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(),
						PlatfromClient.getCloRec(id.getId1(), new StringBuffer().append(attributeName)
								.append(prefix).toString()), value));
				// other device
				if (id.getId1() == 418) {
					long objId = obj.getId().getId1() + obj.getId().getId2();
					if (eqloadMap.get(objId) != null) {
						long deviceId = (long) eqloadMap.get(objId);
						long id1 = deviceId % 10000;
						long id2 = deviceId - id1;
						//if not save device need added 
						if (!saveDeviceSet.contains(deviceId)) {
							presetValueList.addValue(client.generatePresetValue(id1, id2,
									PlatfromClient.getCloRec(id1,
											new StringBuffer().append(Byte2Gbk.byteString2Gbk(obj.getAttributeName(i)))
													.append(prefix).toString()),
									obj.getValue(i).getFloatValue()));
						}
					}
				}
			}
		});
		ImdgLogger.info("[rtu preset finish ]");
	}

	private Object getMeasValue(RTMeasValue rtMeasValue) {
		// float
		if (rtMeasValue.getValueType() == 1) {
			return rtMeasValue.getFloatValue();
		}
		// int
		else {
			return rtMeasValue.getIntValue();
		}

	}
	public void mapResult(NasCaseInfo info, PointValueList.Builder presetValueList) {
		info.getCaseAcdotList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "p"), obj.getP()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
//			ImdgLogger.info(obj.getId().getId1()+":"+obj.getId().getId2()+ "      value: "+ new String(
//					client.generatePresetValue(obj.getId(), client.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland())
//							.getValue().toByteArray()));
		});
		info.getCaseBreakList().forEach(obj -> {
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "point"), obj.getStats()));

			// island > 0 then tpcoclor >0
			if (obj.getIsland1() == obj.getIsland2()) {
				presetValueList.addValue(
						client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland1()));
			} else {
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), 0));
			}
		});
		info.getCaseDisList().forEach(obj -> {
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "point"), obj.getStats()));

			// island > 0 then tpcoclor >0
			if (obj.getIsland1() == obj.getIsland2()) {
				presetValueList.addValue(
						client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland1()));
			} else {
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), 0));
			}
		});
		info.getCaseDcbreakList().forEach(obj -> {
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "point"), obj.getStats()));

			// island > 0 then tpcoclor >0
			if (obj.getIsland1() == obj.getIsland2()) {
				presetValueList.addValue(
						client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland1()));
			} else {
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), 0));
			}
		});
		info.getCaseDcdisList().forEach(obj -> {
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "point"), obj.getStats()));

			// island > 0 then tpcoclor >0
			if (obj.getIsland1() == obj.getIsland2()) {
				presetValueList.addValue(
						client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland1()));
			} else {
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), 0));
			}
		});
		info.getCaseTrwdList().forEach(obj -> {
			
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "p"), obj.getP()));
			// ImdgLogger.info(client.generatePresetValue(obj.getId(),
			// client.getCloRec(obj.getId().getId1(), "p"), obj.getP()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
		});
		info.getCaseLoadList().forEach(obj -> {
			if (obj.getId().getId1() != 418) {
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "p"), obj.getP()));
				presetValueList
						.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
				presetValueList.addValue(
						client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
			} else {
				//eqload p and q
				long objId = obj.getId().getId1() + obj.getId().getId2();
				presetValueList.addValue(client.generatePresetValue(obj.getId(),
						PlatfromClient.getCloRec(obj.getId().getId1(), "p"), obj.getP()));
				presetValueList.addValue(client.generatePresetValue(obj.getId(),
						PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
				// origin device  p q tpcolor
				if (eqloadMap.get(objId) != null) {
					long deviceId =  eqloadMap.get(objId);
					long deviceId_id1 = deviceId % 10000;
					String pAttribute = "p";
					String qAttribute = "q";
					if (deviceId_id1 == 154) {
						pAttribute = "pac";
						qAttribute = "qac";
					}
					//if device still exist , that should be ignore
					if (!saveDeviceSet.contains(deviceId)) {
						presetValueList.addValue(client.generatePresetValue(deviceId_id1, deviceId - deviceId_id1,
								PlatfromClient.getCloRec(deviceId_id1, pAttribute), obj.getP()));
						presetValueList.addValue(client.generatePresetValue(deviceId_id1, deviceId - deviceId_id1,
								PlatfromClient.getCloRec(deviceId_id1, qAttribute), obj.getQ()));
						presetValueList.addValue(client.generatePresetValue(deviceId_id1, deviceId - deviceId_id1,
								PlatfromClient.getCloRec(deviceId_id1, "tpcolor"), obj.getIsland()));
					}
					
				}
			}
		});
		info.getCaseBusList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "v"), obj.getV()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
		});
		info.getCaseUnitList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "p"), obj.getP()));

			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "v"), obj.getVe()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
		});
		info.getCaseCvtList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "pac"), obj.getP()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "qac"), obj.getQ()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
		});
		info.getCaseScpList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "pi"), obj.getPi()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "qi"), obj.getQi()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "pj"), obj.getPj()));
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "qj"), obj.getQj()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland1()));
		});
		info.getCaseCpList().forEach(obj -> {
			presetValueList
					.addValue(client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "q"), obj.getQ()));
			presetValueList.addValue(
					client.generatePresetValue(obj.getId(), PlatfromClient.getCloRec(obj.getId().getId1(), "tpcolor"), obj.getIsland()));
		});
		ImdgLogger.info("[result preset finish ]");
	}


	public void mapCal(GridModelResponse model, NasCaseInfo info, PointValueList.Builder presetValueList) {
		HashMap<Long, Long> map = new HashMap<Long, Long>();
		HashMap<Long, Float> pmap = new HashMap<Long, Float>();
		HashMap<Long, Float> qmap = new HashMap<Long, Float>();
		model.getUnitinfoList().forEach(unit -> {
//			map.put(unit.getId().getId1()+unit.getId().getId2(), value)
			//genertate map for find stid
			map.put(unit.getId().getId1() + unit.getId().getId2(), unit.getStId().getId1() + unit.getId().getId2());
		});
		info.getCaseUnitList().forEach(unit -> {
			long id = unit.getId().getId1() + unit.getId().getId2();
			Long stid = map.get(id);
			// add all pq to st-pq
			if (pmap.get(stid) == null) {
				pmap.put(stid, 0f);
			}
			if (qmap.get(stid) == null) {
				qmap.put(stid, 0f);
			}
			pmap.put(stid, pmap.get(stid) + unit.getP());
			qmap.put(stid, qmap.get(stid) + unit.getQ());
		});
		
		
		model.getStinfoList().forEach(substation -> {
			ServiceGridModel.RTKEY id = substation.getId();
			if (id.getId1() == 111) {
				presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(), PlatfromClient.getCloRec(id.getId1(), "pgen"),
						pmap.get(id.getId1() + id.getId2())));
				presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(), PlatfromClient.getCloRec(id.getId1(), "qgen"),
						pmap.get(id.getId1() + id.getId2())));
			}
		});
		
	}
	
	public void mapTopoColor(GridModelResponse model, ModelMeasResponse rtu, Builder presetValueList) {
		PModelContainer container = PhysicalModelFactory.eINSTANCE.createPModelContainer();
		PModelProtoBufAdapter containerAdapter = new PModelProtoBufAdapter(container);
		containerAdapter.mapData(model.toBuilder());
		containerAdapter.mapRTUData(rtu.toBuilder());
		mapTopoColor(container, presetValueList);
	}
	
	public void mapTopoColor(PModelContainer container, Builder presetValueList) {
		new ContainerTopology().topoColorAnalysis(container, 10);
		container.getSubstaions().forEach(sub -> {
			sub.getConnectivityNodeSet().values().forEach(node -> {
				node.getConnectedVertexs().forEach(vertex -> {
					BasePModelObject<?, ?> obj = (BasePModelObject<?, ?>) vertex;
					RTKEY.Builder id = obj.getObjId();
					int tpcolor = 0;
					if (node.isStatus()) {
						tpcolor = node.getIntIslandFlag();
					}
					CloRec clo = PlatfromClient.getCloRec(id.getId1(), "tpcolor");
					if (clo.getClo() != 0) {
						presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(), clo, tpcolor));
					}
				});
				node.getConnectedEdges().forEach(edge -> {
					Edge<?, ?> obj = (Edge<?, ?>) edge;
					RTKEY.Builder id = obj.getObjId();
					int tpcolor = 0;
					if (obj.getFromConNode() != null && obj.getToConNode() != null && edge.isInsideSubstation()) {
						if (obj.getFromConNode().getNodeNumber() == node.getNodeNumber()) {
							if (obj.getFromConNode().isStatus() && obj.getToConNode().isStatus()) {
								tpcolor = node.getIntIslandFlag();
							}
							CloRec clo = PlatfromClient.getCloRec(id.getId1(), "tpcolor");
							if (clo.getClo() != 0) {
								presetValueList
										.addValue(client.generatePresetValue(id.getId1(), id.getId2(), clo, tpcolor));
							}
						}
					}
				});
			});
		});
	}
	
	public static Builder mapTopoColor(PModelContainer container) {
		new ContainerTopology().topoColorAnalysis(container, 1000);
		boolean haveResult = true;
		Builder valueList = PointValueList.newBuilder();
		if (container.getRtnetCaseResponse() == null ) {
			container.setRtnetCaseResponse(RtnetCaseResponse.newBuilder());
			haveResult = false;
		}
		RtnetCaseResponse.Builder builder = container.getRtnetCaseResponse();
		NasCaseInfo.Builder caseBuilder = builder.getCaseInfoBuilder();
		container.getSubstaions().forEach(sub -> {
			sub.getConnectivityNodeSet().values().forEach(node -> {
				node.getConnectedVertexs().forEach(vertex -> {
					BasePModelObject<?, ?> obj = (BasePModelObject<?, ?>) vertex;
					int tpcolor = 0;
					if (node.isStatus()) {
						tpcolor = node.getIntIslandFlag();
					}
//					CloRec clo = PlatfromClient.getCloRec(id.getId1(), "tpcolor");
//					if (clo.getClo() != 0) {
						addResult(obj, caseBuilder, tpcolor,valueList);
//						presetValueList.addValue(client.generatePresetValue(id.getId1(), id.getId2(), clo, tpcolor));
//					}
				});
				node.getConnectedEdges().forEach(edge -> {
					Edge<?, ?> obj = (Edge<?, ?>) edge;
					int tpcolor = 0;
					if (obj.getFromConNode() != null && obj.getToConNode() != null) {
						if (obj.getFromConNode().getNodeNumber() == node.getNodeNumber()) {
							if (obj.getFromConNode().isStatus() && obj.getToConNode().isStatus()) {
								tpcolor = node.getIntIslandFlag();
							}
//							CloRec clo = PlatfromClient.getCloRec(id.getId1(), "tpcolor");
//							if (clo.getClo() != 0) {
								addResult(obj, caseBuilder, tpcolor,valueList);
//								presetValueList
//										.addValue(client.generatePresetValue(id.getId1(), id.getId2(), clo, tpcolor));
//							}
						}
					}
				});
			});
		});
		if (!haveResult) {
			PModelProtoBufAdapter adapter = new PModelProtoBufAdapter(container);
			adapter.mapResultData(container.getRtnetCaseResponse());
			return null;
		}
		return valueList;
	}
	final static ByteString Island = ByteString.copyFrom("island".getBytes());
	final static ByteString Island1 = ByteString.copyFrom("island".getBytes());
	final static ByteString Island2 = ByteString.copyFrom("island".getBytes());
	private static PointValueList addResult(BasePModelObject<?, ?> obj, message.ServiceRtnetCase.NasCaseInfo.Builder caseBuilder,
			int tpcolor,PointValueList.Builder valueList) {
		if (obj.getResultRec() == null) {
			if (obj instanceof AcLineEnd) {
				caseBuilder.addCaseAcdotBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof EnergyConsumer) {
				caseBuilder.addCaseLoadBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof GeneratingUnit) {
				caseBuilder.addCaseUnitBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof BusbarSection) {
				caseBuilder.addCaseBusBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof TransformerWinding) {
				caseBuilder.addCaseTrwdBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof SeriesCompensator) {
				caseBuilder.addCaseScpBuilder().setId(generateKey(obj)).setIsland1(tpcolor).setIsland2(tpcolor);
			} else if (obj instanceof ShuntCompensator) {
				caseBuilder.addCaseCpBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof Breaker) {
				caseBuilder.addCaseBreakBuilder().setId(generateKey(obj)).setIsland1(tpcolor).setIsland2(tpcolor);
			} else if (obj instanceof Disconnector) {
				caseBuilder.addCaseDisBuilder().setId(generateKey(obj)).setIsland1(tpcolor).setIsland2(tpcolor);
			} else if (obj instanceof RectifierInverter) {
				caseBuilder.addCaseCvtBuilder().setId(generateKey(obj)).setIsland(tpcolor);
			} else if (obj instanceof DcBreaker) {
				caseBuilder.addCaseDcbreakBuilder().setId(generateKey(obj)).setIsland1(tpcolor).setIsland2(tpcolor);
			} else if (obj instanceof DcDisconnector) {
				caseBuilder.addCaseDcdisBuilder().setId(generateKey(obj)).setIsland1(tpcolor).setIsland2(tpcolor);
			}
		}else {
			if (obj instanceof AcLineEnd) {
				if (((AcLineEnd) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((AcLineEnd) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof EnergyConsumer) {
				if (((EnergyConsumer) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((EnergyConsumer) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof GeneratingUnit) {
				if (((GeneratingUnit) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((GeneratingUnit) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof BusbarSection) {
				if (((BusbarSection) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((BusbarSection) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof TransformerWinding) {
				if (((TransformerWinding) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((TransformerWinding) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof SeriesCompensator) {
				if (((SeriesCompensator) obj).getResultRec().getIsland1() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island1);
					((SeriesCompensator) obj).getResultRec().setIsland1(tpcolor);
				}
				if (((SeriesCompensator) obj).getResultRec().getIsland2() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island2);
					((SeriesCompensator) obj).getResultRec().setIsland2(tpcolor);
				}

			} else if (obj instanceof ShuntCompensator) {
				if (((ShuntCompensator) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((ShuntCompensator) obj).getResultRec().setIsland(tpcolor);
				}
			} else if (obj instanceof Breaker) {
				if (((Breaker) obj).getResultRec().getIsland1() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island1);
					((Breaker) obj).getResultRec().setIsland1(tpcolor);
				}
				if (((Breaker) obj).getResultRec().getIsland2() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island2);
					((Breaker) obj).getResultRec().setIsland2(tpcolor);
				}
			} else if (obj instanceof Disconnector) {
				if (((Disconnector) obj).getResultRec().getIsland1() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island1);
					((Disconnector) obj).getResultRec().setIsland1(tpcolor);
				}
				if (((Disconnector) obj).getResultRec().getIsland2() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island2);
					((Disconnector) obj).getResultRec().setIsland2(tpcolor);
				}
			} else if (obj instanceof RectifierInverter) {
				if (((RectifierInverter) obj).getResultRec().getIsland() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island);
					((RectifierInverter) obj).getResultRec().setIsland(tpcolor);
				}

			} else if (obj instanceof DcBreaker) {
				if (((DcBreaker) obj).getResultRec().getIsland1() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island1);
					((DcBreaker) obj).getResultRec().setIsland1(tpcolor);
				}
				if (((DcBreaker) obj).getResultRec().getIsland2() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island2);
					((DcBreaker) obj).getResultRec().setIsland2(tpcolor);
				}
			} else if (obj instanceof DcDisconnector) {
				if (((DcDisconnector) obj).getResultRec().getIsland1() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island1);
					((DcDisconnector) obj).getResultRec().setIsland1(tpcolor);
				}
				if (((DcDisconnector) obj).getResultRec().getIsland2() != tpcolor) {
					addPresetValue(valueList, tpcolor, obj, Island2);
					((DcDisconnector) obj).getResultRec().setIsland2(tpcolor);
				}
			}
		}
		return null;
	}

	private static void addPresetValue(Builder valueList, int tpcolor, BasePModelObject<?, ?> obj, ByteString island) {
		PointValue.Builder value = valueList.addValueBuilder();
		value.setName(island);
		value.setId(message.ServicePointValue.RTKEY.newBuilder().setId1(obj.getObjId().getId1())
				.setId2(obj.getObjId().getId2()));
		value.setValue(ByteString.copyFrom(String.valueOf(tpcolor).getBytes()));

	}
	
	private static message.ServiceRtnetCase.RTKEY generateKey(BasePModelObject<?, ?> obj) {
		return ServiceRtnetCase.RTKEY.newBuilder().setId1(obj.getObjId().getId1())
				.setId2(obj.getObjId().getId2()).build();
	}
	
}
