package client;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.kedong.platform.imdg.client.KDMDGClient;
import com.kedong.platform.imdg.client.config.ClientConfig;
import com.kedong.platform.imdg.core.IMap;
import com.kedong.platform.imdg.core.ISet;
import com.kedong.platform.imdg.core.KDMDGInstance;
import com.kedong.platform.imdg.core.Member;
import com.kedong.platform.imdg.core.MessageListener;
import com.kedong.platform.imdg.map.listener.MapListener;
import com.kedong.platform.imdg.scheduledexecutor.IScheduledFuture;

import app.model.copy.process.GetEntryProcess;
import app.model.copy.process.PutEntryProcess;
import app.model.data.BuildConfig;
import app.model.data.ModelData;
import app.model.preset.CloRec;
import app.model.update.process.ModelObjectTopoEntryProcess;
import app.model.update.process.ModelObjectUpdateEntryProcess;
import app.model.update.process.ModelUpdateEntryProcess;
import cn.sg.common.ImdgLogger;
import cn.sg.model.PModelProtoBufAdapter;
import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.PModelContainerWapper;
import cn.sg.model.physical.PhysicalModelFactory;
import data.ModelUpdateData;
import library.Boost;
import message.Data.Configure;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceModelMeas.ModelMeasResponse;
import message.ServiceModelPmuMeas.ModelPmuMeasResponse;
import message.ServicePointValue.EntryList;
import message.ServicePointValue.PointValue;
import message.ServicePointValue.PointValueList;
import message.ServicePointValue.PointValueList.Builder;
import message.ServicePointValue.RTKEY;
import message.ServiceRtnetCase.RtnetCaseResponse;
import message.ServiceTable.Table;
import warpper.MessageWarpper;

public class PlatfromClient {
	
	private final static String CODE_TYPE = "GBK";

	
	private KDMDGInstance hz;

	public PlatfromClient(String... arg0) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getNetworkConfig().addAddress(arg0);
		clientConfig.getNetworkConfig().setConnectionAttemptPeriod(10000);
		clientConfig.getNetworkConfig().setConnectionAttemptLimit(120);
		clientConfig.getSerializationConfig().getDataSerializableFactoryClasses().put(1000,
				"warpper.MessageWarpperFactory");
		
		hz=KDMDGClient.newKDMDGClient(clientConfig);
		PlatfromClient.readConf();
	}
	
	public PlatfromClient() {
		PlatfromClient.readConf();
		
	}
	

	public PlatfromClient(KDMDGInstance hzInstance) {
		hz = hzInstance;
		PlatfromClient.readConf();
	}
	
	public void putProtoBuf(String key, GeneratedMessage message) {
		String cClassName = MessageWarpper.getCClassName(message.getClass());
		
		ImdgLogger.info(
				"orgin class:" + message.getClass().getName() + "  ,  putting class : " + cClassName + ",key :" + key);
		                                                                                                    
		hz.getMap(cClassName).set(key, new MessageWarpper(message));
	}
	
	
	public void putModel(String key, PModelContainer container) {
		hz.getMap(PModelContainer.class.getSimpleName()).set(key, new PModelContainerWapper(container));
		ImdgLogger.info( "putting container ,key :" + key);
	}
	
	public void putProtoBuf(String key, PModelContainer container) {
		if (container.getGridModelResponse() != null) {
			this.putProtoBuf(key, container.getGridModelResponse().build());
			if (container.getRealMeasResponse() != null) {
				this.putProtoBuf(key, container.getRealMeasResponse().build());
			}
			if (container.getRtnetCaseResponse() != null) {
				this.putProtoBuf(key, container.getRtnetCaseResponse().build());
			}else {
				this.putProtoBuf(key, RtnetCaseResponse.newBuilder().build());
			}
		}
	}
	
	public GeneratedMessage getProtoBuf(String key, Class messageClass) {
		MessageWarpper warpper = getWarpper(key, messageClass);
		if (warpper == null) {
			return null;
		} else {
			return warpper.getResponse();
		}
	}
	
	public MessageWarpper getWarpper(String key, Class messageClass) {
		long time = System.currentTimeMillis();
		String cClassName = MessageWarpper.getCClassName(messageClass);
		if (cClassName != null) {
			MessageWarpper warpper = (MessageWarpper) hz.getMap(cClassName).get(key);
			ImdgLogger.info("[Time]get message: " + (System.currentTimeMillis() - time));
			return warpper;
		} else {
			ImdgLogger.info("getting class : " + cClassName);
		}
		return null;
	}
	
	public IMap<Object,Object> getMap(Class messageClass) {
		String cClassName = MessageWarpper.getCClassName(messageClass);
		if (cClassName != null) {
//			ImdgLogger.info("getting class : " + cClassName);
			return hz.getMap(cClassName);
		} else {
			ImdgLogger.info("getting java class error : " + messageClass);
		}
		return null;
	}
	
	public Set<Object> getModelKey(Class messageClass) {
		String cClassName = MessageWarpper.getCClassName(messageClass);
		return hz.getMap(cClassName).keySet();
	}
	
	public Set<Object> getModelKey() {
		String cClassName = MessageWarpper.getCClassName(GridModelResponse.class);
		return hz.getMap(cClassName).keySet();
	}

	public void deleteModel(String key, Class messageClass) {
		String cClassName = MessageWarpper.getCClassName(messageClass);
		ImdgLogger.info("delete proto key : " + key +"      type : " + cClassName);
		hz.getMap(cClassName).delete(key);
	}
	
	public void shutdown() {
		this.hz.shutdown();
	}


	public KDMDGInstance getHz() {
		return hz;
	}

	public void setHz(KDMDGInstance hz) {
		this.hz = hz;
	}
	
	
	
	public Object updateModel(String modelKey, List<ModelUpdateData> dataList) {
		try {
			return this.getHz().getMap(MessageWarpper.getCClassName(ModelMeasResponse.class)).submitToKey(modelKey,
					new ModelUpdateEntryProcess(dataList)).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return new ArrayList<ModelUpdateData>();
		}
		
	}
	
	
	public Object updateModelObject(String modelKey, List<ModelUpdateData> dataList) {
		try {
			return this.getHz().getMap(PModelContainer.class.getSimpleName()).submitToKey(modelKey,
					new ModelObjectUpdateEntryProcess(dataList)).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Deprecated
	public void putTopoMessage(String modelKey , Map<String, String> map) {
		Map<String, byte[]> presetValueMap = new HashMap<String, byte[]>();
		List<String> otherList= new ArrayList<String>();
		otherList.add(PModelContainer.Substation);
		otherList.add(PModelContainer.Line);
		otherList.forEach(key -> {
			String v = map.get(key);
			map.remove(key);
			presetValueMap.put(key, PointValue.newBuilder().setId(RTKEY.newBuilder())
					.setValue(ByteString.copyFrom(v.getBytes())).build().toByteArray());
		});
		
		map.forEach((key, value) -> {
			
			long id1 = Long.valueOf(key) % 10000;
			long id2 = Long.valueOf(key) - id1;
			StringBuilder b = new StringBuilder();
			if (id1 < 1000) {
				b.append(0);
			}
			presetValueMap.put(b.append(id1).append(":").append(id2).toString(),
					PointValue.newBuilder().setId(RTKEY.newBuilder().setId1(id1).setId2(id2))
							.setValue(ByteString.copyFrom(value.getBytes())).build().toByteArray());

		});
		
		this.getHz().getMap(modelKey + "_topo").putAll(presetValueMap);
		ImdgLogger.info("put topo message ,size : " + map.size());
	}

	public BlockingQueue<Boolean> getUpdateQue() {
		return this.hz.getQueue("update");
	}

	public Object calTopoMessage(String modelKey) {
		try {
			return this.getHz().getMap(PModelContainer.class.getSimpleName()).submitToKey(modelKey,
					new ModelObjectTopoEntryProcess()).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public void updatePreSetMap(String key, PointValueList.Builder PointValueList) {
		Map<String, byte[]> map = new ConcurrentHashMap<String, byte[]>();

		PointValueList.getValueList().parallelStream().forEach(value -> {
			RTKEY id = value.getId();
			map.put(new StringBuilder().append(id.getId1()).append(":").append(id.getId2()).toString(),
					value.toByteArray());
		});
		ImdgLogger.info("**updated preset value size :" + PointValueList.getValueCount()+","+ map.size());
//		this.getHz().getMap(key + "_PresetValueMap").putAll(map);
		this.getHz().getMap(key).putAll(map);
	}

	@Deprecated
	public void updatePreSetMap(String key, List<ModelUpdateData> dataList) {
		Builder pointValueList = PointValueList.newBuilder();
		dataList.forEach(data -> {
			long id1 = data.getDeviceId() % 10000;
			long id2 = data.getDeviceId() - id1;
			pointValueList
					.addValue(generatePresetValue(id1, id2, getCloRec(id1, data.getAttributeName()), data.getValue()));
		});
		this.updatePreSetMap(key, pointValueList);
//		ImdgLogger.info(PointValueList);
	}
	@Deprecated
	public PointValue.Builder generatePresetValue(message.ServiceRtnetCase.RTKEY key, CloRec rec, Object value) {
		return generatePresetValue(key.getId1(), key.getId2(), rec, value);
	}
	@Deprecated
	public PointValue.Builder generatePresetValue(long id1, long id2, CloRec rec, Object value) {
		return generatePresetValue(id1, id2 + rec.getClo(), rec.getLen(), rec.getType(), value);
	}
	@Deprecated
	public static PointValue.Builder generatePresetValue(long id1, long id2, int len, int type, Object value) {
		PointValue.Builder b = PointValue.newBuilder();
		b.setId(RTKEY.newBuilder().setId1(id1).setId2(id2));
		b.setLen(len);
		b.setType(type);
		b.setValue(ByteString.copyFrom(String.valueOf(value).getBytes()));
		return b;
	}
	
	static private Map<Long, Map<String, CloRec>> cloRecMap = new HashMap<Long, Map<String, CloRec>>();
	static private Map<Long, Map<Integer, String>> map = new HashMap<Long, Map<Integer, String>>();

	public static void readConf() {
		if (map.size() == 0) {
			Map[] mapArray = readconfigurePresetMap();
			map = mapArray[0];
			cloRecMap = mapArray[1];
		}
	}
	
	public static CloRec getCloRec(long l, String attributeName) {
		return getCloRecBase(l, attributeName);
	}
	
	final static CloRec zeroRec = new CloRec();

	public  static CloRec getCloRecBase(long l, String attributeName) {
		Map<String, CloRec> cloRec = cloRecMap.get(l);
		if (cloRec != null) {
			CloRec clo_number = cloRec.get(attributeName);
			if (clo_number != null)
				return clo_number;
			else {
//				ImdgLogger.info("miss attributeName : " + l + "  " + attributeName);
				return zeroRec;
			}
		} else {
//			ImdgLogger.info("miss table : " + l);
			return zeroRec;
		}
	}
	
	final static String contorlKey = "taskContorl";

	public void pulishMessage(String key, GeneratedMessage protoBuf) {
		if (protoBuf != null) {
			this.hz.getTopic(key).publish(new MessageWarpper(protoBuf));
			ImdgLogger.info("publish messgae:"+ key + "," + protoBuf.getClass().getSimpleName());
		}
	}
	List<BaseContainerCalculator> cList = new ArrayList<BaseContainerCalculator>();
	
	public void addContainerCalculator(BaseContainerCalculator calculator) {
		cList.add(calculator);
	}
	
	public void calculate() {
		this.cList.forEach(calculator->{
			calculator.getKeyList().forEach(key->{
				PModelContainer container = this.getContainer(key);
				Builder PointValueList = calculator.calculate(container);
				this.updatePreSetMap(key, PointValueList);
			});
		});
	}
	      
	public static boolean isUpdateByMessage(String key) {
		return key.contains("grid_mirroring_model")||key.contains("scada")||key.contains("pdr");
	}
	
	


	
	public Map<String, List<Long>> getSingleBuildConf() {
		Map<String, List<Long>> buildConfMap =readBuildConf();
		getConfig(buildConfMap);
		return buildConfMap;
	}
	
	public static Map<String, List<Long>> readBuildConf() {
		Map<String, List<Long>> areaMap = new HashMap<String, List<Long>>();
		InputStream stream = null;
		try {
			stream = new FileInputStream(ImdgLogger.getDir()+ "/build_configure");
			BufferedReader din = new BufferedReader(new InputStreamReader(stream, CODE_TYPE));

			din.lines().forEach(str -> {
				String[] strArray = str.split("\\s+");
				List<Long> areaIdList = new ArrayList<Long>();
				for (int i = 1; i < strArray.length; i++) {
					areaIdList.add(Long.valueOf(strArray[i]));
				}
				areaMap.put(strArray[0], areaIdList);
			});
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return areaMap;
	}
	
	public static BuildConfig getConfig(Map<String, List<Long>> areaMap) {
		List<Long> list = areaMap.get("#Configure");
		areaMap.remove("#Configure");
		if (list != null) {
			BuildConfig config = new BuildConfig(list.get(0), list.get(1), list.get(2));
//			ImdgLogger.info(config.toString());
			return config;
		}
		return null;
	}

	public void lookScheduledExecutorService(String scheduledExecutorServiceName) {
		hz.getScheduledExecutorService(scheduledExecutorServiceName).getAllScheduledFutures().forEach((k, v) -> {
			v.forEach(f -> {
				ImdgLogger.info("Member:" + k.getAddress() + "[" + f.getHandler().getSchedulerName() + "]"
						+ ", Task name:" + f.getHandler().getTaskName() + "[runs:" + f.getStats().getTotalRuns() + ",total(ms):"
						+ f.getStats().getTotalRunTime(TimeUnit.MILLISECONDS) + ",last(ms)"
						+ f.getStats().getLastRunDuration((TimeUnit.MILLISECONDS)) + "]");
			});
		});
	}
	
	public void cancleScheduledExecutorServiceTask(String scheduledExecutorServiceName,
			String taskName) {
		hz.getScheduledExecutorService(scheduledExecutorServiceName).getAllScheduledFutures().forEach((k, v) -> {
			v.forEach(f -> {
				if (f.getHandler().getTaskName().equals(taskName)) {
					f.dispose();
				}
			});
		});

	}
	
	public String getTaskIp(String scheduledExecutorServiceName, String taskName) {
		for (Entry<Member, List<IScheduledFuture<Object>>> entry : hz
				.getScheduledExecutorService(scheduledExecutorServiceName).getAllScheduledFutures().entrySet()) {
			if (entry.getValue().stream().anyMatch(f -> f.getHandler().getTaskName().equals(taskName))) {
				return entry.getKey().getAddress().getHost();
			}
		}
		return null;
	}

	public boolean checkTaskIp(String scheduledExecutorServiceName, String taskName, String ip) {
		String taskIp = getTaskIp(scheduledExecutorServiceName, taskName);
//		ImdgLogger.info("check:" + taskIp + "," + ip);
		if (taskIp != null) {
			return taskIp.equals(ip);
		}
		return false;
	}
	
	
	public List<String> getTaskList(String scheduledExecutorServiceName) {
		List<String> taskNameList = new ArrayList<String>();
		Map<Member, List<IScheduledFuture<Object>>> map = hz.getScheduledExecutorService(scheduledExecutorServiceName)
				.getAllScheduledFutures();
		map.forEach((k, v) -> {
			v.forEach(f -> {
				taskNameList.add(f.getHandler().getTaskName());
			});
		});
		return taskNameList;
	}
	
	public void diposeTask(String scheduledExecutorServiceName,String taskName) {
		Map<Member, List<IScheduledFuture<Object>>> map = hz.getScheduledExecutorService(scheduledExecutorServiceName)
				.getAllScheduledFutures();
		map.forEach((k, v) -> {
			v.forEach(f -> {
				if(f.getHandler().getTaskName().equals(taskName)) {
					f.dispose();
				}
			});
		});
	}

	public static Map[] readconfigurePresetMap() {
		
		Map<Long, Map<Integer, String>> map = new HashMap<Long, Map<Integer, String>>();
		Map<Long, Map<String, CloRec>> recMap = new HashMap<Long, Map<String, CloRec>>();
		File file = new File(ImdgLogger.getDir() + "/presetValueMap_conf.txt");
		if (file.exists()) {
			InputStreamReader stream = null;
			try {
				stream = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader din = new BufferedReader(stream);
			din.lines().filter(str -> !str.startsWith("table") && !str.startsWith("<")).forEach(str -> {
				String[] array = str.split("\\s+");
				long tabel_id = Long.valueOf(array[0]);
				if (map.get(tabel_id) == null) {
					map.put(tabel_id, new HashMap<Integer, String>());
				}
				map.get(tabel_id).put(Integer.valueOf(array[1]), array[3]);

				if (recMap.get(tabel_id) == null) {
					recMap.put(tabel_id, new HashMap<String, CloRec>());
				}
				recMap.get(tabel_id).put(array[3],
						new CloRec(Integer.valueOf(array[1]), Integer.valueOf(array[5]), Integer.valueOf(array[6])));
			});
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Map[] mapArray = new Map[2];
		mapArray[0] = map;
		mapArray[1] = recMap;
		return mapArray;
	}

	public void addTopicListener(String context, String app, String name, Class clz, MessageListener<Object> listener) {
		String topicName = context + app + name + "_" + MessageWarpper.getCClassName(clz);
		ImdgLogger.info("topic name:" + topicName);
		this.getHz().getTopic(topicName).addMessageListener(listener);
	}

	public void addTopicListener(String context, String app, String name, String dataName,
			MessageListener<Object> listener) {
		String topicName = context + app + name + "_" + dataName;
		ImdgLogger.info("topic name:" + topicName);
		this.getHz().getTopic(topicName).addMessageListener(listener);
	}
	
	public String addTopicListener(String key,
			MessageListener<Object> listener) {
		ImdgLogger.info("topic name:" + key);
		return this.getHz().getTopic(key).addMessageListener(listener);
	}
	
	public void addModelListener(MapListener listener) {
		this.getMap(GridModelResponse.class).addEntryListener(listener, false);
	}
	
	public PModelContainer getContainer(String contexName, String appName, String modelName) {
		return getContainer(contexName + appName + modelName);
	}
	
	public PModelContainer getModelFromProto(String contexName, String appName, String modelName) {
		return getContainer(contexName + appName + modelName);
	}
	public PModelContainer getContainer(String modelKey) {
		ImdgLogger.info("getting model : " +modelKey);
		PModelContainer container = PhysicalModelFactory.eINSTANCE.createPModelContainer();
		PModelProtoBufAdapter containerAdapter = new PModelProtoBufAdapter(container);
		GeneratedMessage model = this.getProtoBuf(modelKey, GridModelResponse.class);
		if (model != null) {
			containerAdapter.mapData((GridModelResponse.Builder) model.toBuilder());
		} else {
			ImdgLogger.info("model is not existed!");
			return null;
		}
		GeneratedMessage rtu = this.getProtoBuf(modelKey, ModelMeasResponse.class);
		if (rtu != null) {
			containerAdapter.mapRTUData((ModelMeasResponse.Builder) rtu.toBuilder());
		} else {
			ImdgLogger.info("rtu is not existed!");
		}
		GeneratedMessage result = this.getProtoBuf(modelKey, RtnetCaseResponse.class);
		if (result != null) {
			containerAdapter.mapResultData(((RtnetCaseResponse) result).toBuilder());
		} else {
			ImdgLogger.info("result is not existed!");
		}
		return container;
	}
	
	public PModelContainer getContainerOnlyLine(String modelKey) {
		ImdgLogger.info("getting model : " + modelKey);
		PModelContainer container = PhysicalModelFactory.eINSTANCE.createPModelContainer();
		PModelProtoBufAdapter containerAdapter = new PModelProtoBufAdapter(container);
		GeneratedMessage model = this.getProtoBuf(modelKey, GridModelResponse.class);
		if (model != null) {
			containerAdapter.mapDataMin((GridModelResponse.Builder) model.toBuilder());
		} else {
			ImdgLogger.info("model is not existed!");
			return null;
		}
		return container;
	}

	public ISet<Object> getPdrTopicKeySet() {
		return this.getHz().getSet("pdr_topic");
	}
	
	public void putPresetValueMap(String key, EntryList entryList) {
		if (Boost.libararyExist) {
//			ImdgLogger.info(entryList);
			this.getHz().getMap(key).set(key, Boost.boost(entryList.toByteArray()));
			ImdgLogger.info("put preset map:" + key);
		} else {
			ImdgLogger.info("libjboost is not exit!");
		}
	}
	
	public void putTable(String key, Table table) {
		if (Boost.libararyExist) {
//			ImdgLogger.info(entryList);
			ImdgLogger.info("table size :" + table.getRowValueCount());
			byte[] byteArray = table.toByteArray();
			ImdgLogger.info("byte array size :" + byteArray.length);
			byteArray = Boost.boostTable(byteArray);
			ImdgLogger.info("boost byte array size :" + byteArray.length);
			this.getHz().getMap(key).set(key, byteArray);
			ImdgLogger.info("put Table:" + key);
		} else {
			ImdgLogger.info("libjboost is not exit!");
		}
	}
	
	public EntryList.Builder getPresetValueMap(String key) {
		if (Boost.libararyExist) {
			try {
				if (this.getHz().getMap(key).get(key) == null) {
					return EntryList.newBuilder();
				}
				return EntryList.parseFrom(Boost.backBoost((byte[]) this.getHz().getMap(key).get(key))).toBuilder();
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImdgLogger.info("put preset map:" + key);
		}
		return EntryList.newBuilder();
	}

	public void readFile2Grid(List<String> args) {
		args.forEach(arg -> {
			long time = System.currentTimeMillis();
			ImdgLogger.info("Model key:" + arg);
			File file = new File(ImdgLogger.getDir() + "/" + arg + "_model.message");
			if (file.exists()) {
				try {
					GridModelResponse response = GridModelResponse.parseFrom(new FileInputStream(file));
					this.putProtoBuf(arg, response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));

				File file2 = new File(ImdgLogger.getDir() + "/" + arg + "_rtu.message");
				if (file2.exists()) {
					try {
						ModelMeasResponse response2 = ModelMeasResponse.parseFrom(new FileInputStream(file2));
						this.putProtoBuf(arg, response2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
					File file3 = new File(ImdgLogger.getDir() + "/" + arg + "_result.message");
					if (file3.exists()) {
						try {
							RtnetCaseResponse response3 = RtnetCaseResponse.parseFrom(new FileInputStream(file3));
							this.putProtoBuf(arg, response3);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
					}
				}
				File file4 = new File(ImdgLogger.getDir() + "/" + arg + "_pmu.message");//liyadi,2023.3.16,add
				if (file4.exists()) {
					try {
						ModelPmuMeasResponse response4 = ModelPmuMeasResponse.parseFrom(new FileInputStream(file4));
						this.putProtoBuf(arg, response4);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
				}
			}
		});
	}
	final private static Map<Long, Long> cacheMap = new HashMap<Long, Long>();
	
	
	public static Map<Long, Long> getCachemap() {
		return cacheMap;
	}

	public void readGrid2File(List<String> args) {
		args.forEach(arg -> {
			try {
				long time = System.currentTimeMillis();
				ImdgLogger.info("Model key:" + arg);

				File file = new File(ImdgLogger.getDir() + "/" + arg + "_model.message");

				file.createNewFile();

				FileOutputStream output = new FileOutputStream(file);
				GridModelResponse model = (GridModelResponse) this.getProtoBuf(arg, GridModelResponse.class);
				if (model != null) {
					model.writeTo(output);
					output.close();

					ImdgLogger.info("message saved(ms) : "+ (System.currentTimeMillis() - time));

					
					GeneratedMessage proto = this.getProtoBuf(arg, ModelMeasResponse.class);
					if (proto != null) {
						File file2 = new File(ImdgLogger.getDir() + "/" + arg + "_rtu.message");
						file2.createNewFile();
						FileOutputStream output2 = new FileOutputStream(file2);
						proto.writeTo(output2);
						output2.close();
						ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
					}

					proto = this.getProtoBuf(arg, RtnetCaseResponse.class);
					if (proto != null) {
						File file3 = new File(ImdgLogger.getDir() + "/" + arg + "_result.message");
						file3.createNewFile();
						FileOutputStream output3 = new FileOutputStream(file3);

						proto.writeTo(output3);
						output3.close();
						ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
					}
					proto = this.getProtoBuf(arg, ModelPmuMeasResponse.class);//liyadi ,add ,20230315
					if (proto != null) {
						File file3 = new File(ImdgLogger.getDir() + "/" + arg + "_pmu.message");
						file3.createNewFile();
						FileOutputStream output3 = new FileOutputStream(file3);

						proto.writeTo(output3);
						output3.close();
						ImdgLogger.info("message saved(ms) : " + (System.currentTimeMillis() - time));
					}
					
				} else {
					ImdgLogger.info("model does not exit : " + arg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public void putTaskConfigure(Class<Configure> clz, Map map) {
		String cClassName = MessageWarpper.getCClassName(clz);
		this.getHz().getMap(cClassName).putAll(map);
	}
	
	public static String getAttributeName(long id1, int column_id) {
		Map<Integer, String> tabileMap = map.get(id1);
		if (tabileMap != null) {
			String attribute = tabileMap.get(column_id);
			if (attribute != null) {
				return tabileMap.get(column_id);
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public IMap<String, ModelData> getOutputMap() {
		return this.hz.getMap("OutputMap");
	}

	public IMap<String, PModelContainerWapper> getContainerMap() {
		return this.getHz().getMap(PModelContainer.class.getSimpleName());
	}
	
	public void logBuildInfo(String info,String string) {
		this.getHz().getMap("Log").put(string+"_BuildInfo", info);
	}
	
	public String getBuildInfo(String string) {
		return (String) this.getHz().getMap("Log").get(string+"_BuildInfo");
	}
	
	public Map<Object,Object> getAllBuildInfo() {
		return (Map) this.getHz().getMap("Log");
	}
	
	public void copyModel(String fromKey, String toKey) {
		ImdgLogger.info("fromKey :" + fromKey + ", toKey:" + toKey);
		List<Class<?>> list = Arrays.asList(GridModelResponse.class, ModelMeasResponse.class, RtnetCaseResponse.class);
		list.parallelStream().forEach(clz -> {
			try {
				this.getMap(clz).delete(toKey);
				byte[] byteArray = (byte[]) this.getMap(clz).submitToKey(fromKey, new GetEntryProcess()).get();
				ImdgLogger.info("byte length:" + byteArray.length);
				this.getMap(clz).submitToKey(toKey, new PutEntryProcess(MessageWarpper.getCClassName(clz), byteArray));
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
