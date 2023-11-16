package app.model.update.jni;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.model.update.RtuUpdateHelper;
import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sgepri.adapter.bus.Message;

public class JNI_recvRtu {
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		Message msg = new Message();
		String ctxt = "realtime";
		String app = "public";
		String proc = "JNI_recv";
		
		//channel number
		short channelNumber = new Short(args[0]);
		
		int ret = MessageJNI.messageInit(ctxt, app, proc);
		if (ret < 0) {
			ImdgLogger.info("messageInit failed! ret=" + ret);
			System.exit(0);
		}
		ret = MessageJNI.messageSubscribe(channelNumber, ctxt);
		if (ret < 0) {
			ImdgLogger.info("messageSubscribe failed! ret=" + ret);
			System.exit(0);
		}

		
		RtuUpdateHelper helper = new RtuUpdateHelper("127.0.0.1");
		Map<String, List<Long>> areaMap = PlatfromClient.readBuildConf();
		PlatfromClient.getConfig(areaMap);
		List<String> list = areaMap.keySet().stream().collect(Collectors.toList());
		helper.setKeyList(list);
		helper.start();
		while (true) {
			ret = MessageJNI.messageReceive(msg);
			if (ret < 0) {
				ImdgLogger.info("recv failed! ret=" + ret);
			} else {
				if (msg.event == 1041) {
					ImdgLogger.info("[Successful] message parsing successful"+"msg.event = " + msg.event);
					helper.updateModelYx(msg);
				} else if (msg.event == 1040) {
					ImdgLogger.info("[Successful] message parsing successful"+"msg.event = " + msg.event);
					helper.updateModelYc(msg);
				} else {
					ImdgLogger.info("[Failure] message parsing failure, failed event number :" + msg.event);
				}

			}
		}
	}
	
	public static void saveFile(byte[] buffer)  {
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
