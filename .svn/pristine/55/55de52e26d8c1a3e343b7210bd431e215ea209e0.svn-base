package library;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import cn.sg.common.ImdgLogger;
import message.ServicePointValue.Entry;
import message.ServicePointValue.EntryList;
import message.ServicePointValue.EntryList.Builder;

/**
 * @author Donghao
 * @version date：2022年5月7日 下午4:37:58
 * 
 */
public class Boost {
	public static boolean libararyExist;

	static {
		try {

			System.loadLibrary("jboost");
			Boost.libararyExist = true;
		} catch (UnsatisfiedLinkError e) {
			ImdgLogger.info(System.getProperty("java.library.path"));
			ImdgLogger.info(e.getStackTrace());
			Boost.libararyExist = false;
		}

	}

	public Boost() {
		
	}
	
	public static native byte[] boost(byte[] byteArray);
	
	public static native byte[] boostTable(byte[] byteArray);
	
	public static native byte[] backBoost(byte[] byteArray);
	public static void main(String[] args) {
		
		Builder b = EntryList.newBuilder();
		for (int i = 0; i < args.length; i++) {
			String[] array = args[i].split(":");
			b.addValue(Entry.newBuilder().setKey(ByteString.copyFrom(array[0].getBytes()))
					.setValue(ByteString.copyFrom(array[1].getBytes())));
		}
		
		byte[] array = b.build().toByteArray();
		array = boost(array);
		
		array = backBoost(array);
		
		try {
			EntryList list = EntryList.parseFrom(array);
			System.out.println(list);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
  }
}
