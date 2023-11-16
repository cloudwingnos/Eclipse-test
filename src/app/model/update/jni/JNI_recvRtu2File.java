package app.model.update.jni;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.model.data.BuildConfig;
import app.model.update.RtuUpdateHelper;
import client.PlatfromClient;
import cn.sg.common.ImdgLogger;
import cn.sgepri.adapter.bus.Message;

public class JNI_recvRtu2File {
	
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
		int i = 0;
		new File("1.txt").delete();
		try {
			writer= new FileWriter("1.txt", true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (i <300) {
			
			ret = MessageJNI.messageReceive(msg);
			i++;
			if (ret < 0) {
				ImdgLogger.info("recv failed! ret=" + ret);
			} else {
				if (msg.event == 1041) {
//					try {
//						saveFile(msg.Msg_buf);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				} else if (msg.event == 1040) {
					try {
						if (msg.Msg_buf != null)
							saveFile(msg.Msg_buf);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ImdgLogger.info("[Failure] message parsing failure, failed event number :" + msg.event);
				}

			}
		}
		
		try {
			writer .close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static FileWriter writer;

	public static void saveFile(byte[] buffer) throws IOException {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < buffer.length; i++) {
			b.append(buffer[i]);
			b.append(",");
		}
		b.append("\r\n");
		writer.write(b.toString());
	}
	 
	
}
