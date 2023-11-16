package app.model.update;

import java.util.List;
import java.util.stream.Collectors;

import client.PlatfromClient;
import cn.sg.model.physical.common.rec.BasePModelRec;
import cn.sgepri.model.new_system_hispmu.rec.WamsSeqDataRec;
import data.ModelUpdateData;

public class PmuUpdateHelper {
	

	public PmuUpdateHelper() {
		
	}
	
	public static List<ModelUpdateData> getYcModelUpdateDataList(List<? extends BasePModelRec> ycRecord) {
		List<ModelUpdateData> eventList = ycRecord.stream().map(rec -> {
			
			WamsSeqDataRec wamsRec = (WamsSeqDataRec)rec;
			
			int column_id = (int) (wamsRec.getId2() % 10000);
			long id2 = wamsRec.getId2() - column_id;
			long id = wamsRec.getId1() + id2;
			String attributteName = getAttributeName(wamsRec.getId1(), column_id);
			return new ModelUpdateData(id, attributteName, wamsRec.getValue(), 0);
		}).collect(Collectors.toList());
		return eventList;
	}

	private static String getAttributeName(long id1, int columnId) {
		return PlatfromClient.getAttributeName(id1, columnId);
	}



}
