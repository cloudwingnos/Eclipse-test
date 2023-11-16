package app.model.update.jni;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import app.model.update.RtuUpdateHelper;
import cn.sg.common.ImdgLogger;
import cn.sgepri.adapter.bus.Message;

public class RecvRtuPdr2 {
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
	
		RtuUpdateHelper helper = new RtuUpdateHelper("11.11.11.102");
		helper.setKeyList(Arrays.asList("pdrpublicHZ_PModel"));
		helper.updateDeviceIdSet("pdrpublicHZ_PModel");
//		helper.getDeviceIdSet("pdrpublicHZ_PModel").forEach(k->{
//			ImdgLogger.info(k);
//		});
	}
	
	public static void saveFile(byte[] buffer) throws IOException {
		FileOutputStream s = null;
		try {
			s = new FileOutputStream("1.txt");
			s.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	
}
