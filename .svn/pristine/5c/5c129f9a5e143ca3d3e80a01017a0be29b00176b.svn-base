package jni;
/** 

* @author  Donghao.F 

* @date 2020 Dec 14 12:24:00 

* 

*/
import cn.sg.common.ImdgLogger;

public class ReadTableRowJNI {
	static {
		try {
			System.loadLibrary("RT_Row");
			ReadTableRowJNI.libararyExist = true;
		} catch (UnsatisfiedLinkError e) {
			ImdgLogger.info(e.getStackTrace());
			ReadTableRowJNI.libararyExist = false;
		}
	}

	public static boolean libararyExist;

	public native static Object readTableRow(String sys_name, String bob_name, String db_name, String tb_name,
			String rowId,String rowValue,String className);
}
