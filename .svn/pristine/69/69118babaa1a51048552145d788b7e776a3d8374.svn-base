package app.model.update.jni;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import app.model.update.MessageJniConfiguration;
import app.model.update.RtuUpdateHelper;
import cn.sg.common.ImdgLogger;
import cn.sgepri.adapter.bus.Message;

public class RecvRtu {
	public static void main(String[] args)
			throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		MessageJniConfiguration config = new MessageJniConfiguration("pdr", "public", "JNI_recv_pdr",
				Arrays.asList("pdrscadaHZ_PModel"));
		RtuUpdateHelper helper = new RtuUpdateHelper("127.0.0.1");
		helper.setKeyList(config.getKeyList());
		helper.start();
		Message msg = new Message();
		int ret = MessageJNI.messageInit(config.getCtxt(), config.getApp(), config.getProc());
		if (ret < 0) {
			ImdgLogger.info("messageInit failed! ret=" + ret);
			System.exit(0);
		}
		Arrays.asList(args).parallelStream().forEach(str -> {
			// channel number
			short channelNumber = new Short(str);
			int retR = MessageJNI.messageSubscribe(channelNumber, config.getCtxt());
			if (retR < 0) {
				ImdgLogger.info("messageSubscribe failed! ret=" + ret);
				System.exit(0);
			}
		
			while (true) {
				
				retR = MessageJNI.messageReceive(msg);
				if (retR < 0) {
					ImdgLogger.info("recv failed! ret=" + ret);
				} else {
					if (msg.event == 1041) {
//							ImdgLogger.info("[Successful] message parsing successful"+"msg.event = " + msg.event);
						helper.updateModelYx(msg);
					} else if (msg.event == 1040) {
//							ImdgLogger.info("[Successful] message parsing successful"+"msg.event = " + msg.event);
						helper.updateModelYc(msg);
					} else {
						ImdgLogger.info("[Failure] message parsing failure, failed event number :" + msg.event);
					}
				}
				
			}
		});
	}


	
}
