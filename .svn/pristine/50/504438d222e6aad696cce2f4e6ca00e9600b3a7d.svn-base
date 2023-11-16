package app.model.update;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kedong.platform.imdg.core.EntryEvent;
import com.kedong.platform.imdg.map.listener.EntryAddedListener;
import com.kedong.platform.imdg.map.listener.EntryUpdatedListener;

import app.model.update.process.ModelDeviceIdSetEntryProcess;
import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sgepri.adapter.bus.Message;
import data.ModelUpdateData;
import message.ScadaEvent.TransEventPkgProto;
import message.ServiceGridModel.GridModelDcsysOrBuilder;
import message.ServiceGridModel.GridModelResponse;
import message.ServiceModelMeas.ModelMeasResponse;
import message.YcTrans.RealDataPkg;
import warpper.MessageWarpper;

public class RtuUpdateHelper {
	
	PlatfromClient client;
	
	List<String> keyList;
	
	private BlockingQueue<List<ModelUpdateData> > eventListQueue = new LinkedBlockingQueue<>();
	
	public RtuUpdateHelper(String... arg0) {
		this.client = new PlatfromClient(arg0);
	}

	public RtuUpdateHelper(PlatfromClient client) {
		this.client = client;

	}
	
	public PlatfromClient getClient() {
		return client;
	}


	public void setClient(PlatfromClient client) {
		this.client = client;
	}


	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	
	private void updateDeviceIdSet(EntryEvent<String, MessageWarpper> event) {
		if (keyList.contains(event.getKey())) {
			updateDeviceIdSet(event.getKey());
			List<ModelUpdateData> yxCacheList = yxCacheDataListMap.get(event.getKey());
			if (yxCacheList != null) {
				client.updateModel(event.getKey(), yxCacheList);
				yxCacheList.clear();
			}
		}
	}
	
	Thread thread = new Thread() {
		ArrayList<ModelUpdateData> eventList = new ArrayList<>();
	
		public void run() {
			ImdgLogger.info("rtu helper running!");
			keyList.forEach(key -> {
				updateDeviceIdSet(key);
			});
			
			client.getMap(GridModelResponse.class).addEntryListener(new EntryAddedListener<String, MessageWarpper>() {
				@Override
				public void entryAdded(EntryEvent<String, MessageWarpper> event) {
					updateDeviceIdSet(event);
				}
			}, false);
			
			client.getMap(GridModelResponse.class).addEntryListener(new EntryUpdatedListener<String, MessageWarpper>() {
				@Override
				public void entryUpdated(EntryEvent<String, MessageWarpper> event) {
					updateDeviceIdSet(event);
				}
			}, false);
			
			long time = 0;
			while (true) {
				try {
					eventList.addAll(getEventListQueue().take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (System.currentTimeMillis() - time > 1000) {
					time = System.currentTimeMillis();
					updateModel(eventList);
					eventList.clear();
				}
			}
		}
	};
	
	
	Thread prtintThread = new Thread() {
		public void run() {
			ImdgLogger.info("rtu helper running!");
			while (true) {
				try {
					getEventListQueue().take().forEach(event->{
						System.out.println(event);
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void startPrint() {
		prtintThread.start();
	}
	
	public void start() {
		thread.start();

	}
	public void stop() {
		thread.interrupt();
	}

	Map<String, List<ModelUpdateData>> yxCacheDataListMap = new ConcurrentHashMap<String, List<ModelUpdateData>>();
	
	
	public void updateModel(List<ModelUpdateData> eventList) {
//		mapOutputData(eventList);
		keyList.parallelStream().forEach(modelKey -> {
			Set<Object> deviceIdSet = this.getDeviceIdSet(modelKey);
			List<ModelUpdateData> dataList = (List<ModelUpdateData>) eventList.stream()
					.filter(event -> deviceIdSet.contains(event.getDeviceId())).collect(Collectors.toList());
			if (dataList.size() > 0) {
				ImdgLogger.info("old szie :" + eventList.size() + ",new szie :" + dataList.size());
				List<ModelUpdateData> changeYXDataList = (List<ModelUpdateData>) client.updateModel(modelKey, dataList);
				if (changeYXDataList.size() > 0) {
					client.updateModelObject(modelKey, changeYXDataList);
					cacheYxDataList(modelKey, changeYXDataList);
					publishYxDataList(modelKey,changeYXDataList);
				}
			}
			
		});
	}
	
//	private void mapOutputData(List<ModelUpdateData> eventList) {
//		long yxSize = eventList.stream()
//				.filter(event -> event.getDeviceId() % 10000 == 1321 || event.getDeviceId() % 10000 == 1322).count();
//		IMap<String, ModelData> map = client.getOutputMap();
//		ModelData data = new ModelData(eventList.size(), (int) yxSize);
//		keyList.stream().forEach(modelKey -> {
//			map.set(modelKey, data);
//		});
//	}
	
	private void publishYxDataList(String modelKey, List<ModelUpdateData> changeYXDataList) {
		client.getHz().getTopic(modelKey).publish(changeYXDataList);
	}

	Map<String, List<ModelUpdateData>> yxDataListMap = new ConcurrentHashMap<String, List<ModelUpdateData>>();
	Map<String, Long> lastTimeMap = new ConcurrentHashMap<String, Long>();
	
	private void cacheYxDataList(String modelKey, List<ModelUpdateData> changeYXDataList) {
		if (yxCacheDataListMap.get(modelKey) == null) {
			yxCacheDataListMap.put(modelKey, new ArrayList<ModelUpdateData>());
		}
		List<ModelUpdateData> yxDataList = yxCacheDataListMap.get(modelKey);
		if (yxDataList != null) {
			yxDataList.addAll(changeYXDataList);
			ImdgLogger.info("yx cache size:" + yxDataList.size());
			if (yxDataList.size() > 10000) {
				System.err.println("[ERRO] : yx cache list over load!!!!!!");
				yxDataList.clear();
			}
		}
	}

	public void updateModelYx(Message msg) {
		updateModelYx(msg.Msg_buf);
	}

	public void updateModelYx(byte[] buf) {
		List<ModelUpdateData> eventList = getYxModelUpdateDataList(buf);
		getEventListQueue().add(eventList);
		if (getEventListQueue().size() > 10000) {
			ImdgLogger.info("[erro , overload !, cache queue size]: " + getEventListQueue().size());
			getEventListQueue().clear();
		}
	}
	
	public List<ModelUpdateData> getYxModelUpdateDataList(byte[] buf) {
		try {
			TransEventPkgProto eventPackage = TransEventPkgProto.parseFrom(buf);
			List<ModelUpdateData> eventList = eventPackage.getEventInfoList().stream()
					//reject 418 message
					.filter(event -> event.getKeyId1() != 418)
					.map(event -> {
						int column_id = (int) (event.getKeyId2() % 10000);
						long id2 = event.getKeyId2() - column_id;
						long id = event.getKeyId1() + id2;
						String attributteName = getAttributeName(event.getKeyId1(), column_id);
						System.out.println(attributteName);
						return new ModelUpdateData(id, attributteName, event.getValue(), event.getStatus());
					}).collect(Collectors.toList());
			return eventList;
		} catch (InvalidProtocolBufferException e) {
			System.err.println("[YX]TransEventPkgProto.parseFrom filed!!!!!");
			
		}
		return new ArrayList<ModelUpdateData>();
	}
	
	public List<ModelUpdateData> getYcModelUpdateDataList(byte[] buf) {
		try {
			RealDataPkg realDataPkg = RealDataPkg.parseFrom(buf);
			List<ModelUpdateData> eventList = realDataPkg.getMesList().stream()
					//reject 418 message
					.filter(event -> event.getKeyidFirstLong() != 418).map(event -> {
				int column_id = (int) (event.getKeyidSecondLong() % 10000);
				long id2 = event.getKeyidSecondLong() - column_id;
				long id = event.getKeyidFirstLong() + id2;
				String attributteName = getAttributeName(event.getKeyidFirstLong(), column_id);
				return new ModelUpdateData(id, attributteName, event.getValue(), event.getStatus());
			}).collect(Collectors.toList());
			return eventList;
		} catch (InvalidProtocolBufferException e) {
			System.err.println("[YC]TransEventPkgProto.parseFrom filed!!!!!");
		}
		return new ArrayList<ModelUpdateData>();
	}

	public void updateModelYc(Message msg) {
		updateModelYc(msg.Msg_buf);
	}
	
	
	public void updateModelYc(byte[] buf) {
		List<ModelUpdateData> eventList = getYcModelUpdateDataList(buf);

		getEventListQueue().add(eventList);
		if (getEventListQueue().size() > 1000) {
			ImdgLogger.info("[overload !, queue size]: " + getEventListQueue().size());
		}
	}

	public String getAttributeName(long id1, int columnId) {
		return PlatfromClient.getAttributeName(id1, columnId);
	}

	
	Map<String, Set<Object>> deviceIdSetMap = new HashMap<String, Set<Object>>();

	public Set<Object> getDeviceIdSet(String modelKey) {
		//idSetMap updated after model update
		return this.deviceIdSetMap.get(modelKey);
	}
	
	public void updateDeviceIdSet(String modelKey) {
		// idSetMap updated after model update
		try {
			Set<Object> localSet = (Set<Object>) client.getMap(GridModelResponse.class).submitToKey(modelKey,
					new ModelDeviceIdSetEntryProcess()).get();
			deviceIdSetMap.put(modelKey, localSet);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BlockingQueue<List<ModelUpdateData> > getEventListQueue() {
		return eventListQueue;
	}

	public void setEventListQueue(BlockingQueue<List<ModelUpdateData> > eventListQueue) {
		this.eventListQueue = eventListQueue;
	}



}
