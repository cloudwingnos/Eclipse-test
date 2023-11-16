package app.model.update.jni;
import cn.sgepri.adapter.bus.Message;

public class MessageJNI {
	static {
		System.loadLibrary("jrte");
	}

	public native static int messageSend(Message messageP, int messageLength);

	public native static int messageInit(String context_name, String app_name, String proc_name);

	public native static int messageReceive(Message messageP);

	public native static int messageSubscribe(short set_id, String context_name);

	public native static int messageUnSubscribe(short set_id, String context_name);

	public native static int messageExit(int proc_key);
}
