package cn.sgepri.adapter.bus;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
import java.nio.ByteBuffer;

/**
 * Class for define bus message object 
 * 
 *
 */
public class Message extends MessageHeader
{
    /* bus message payload is a byte array */
    public  byte[]  Msg_buf;

    /**
     * get String of the length from the ByteBuffer (from the head position)
     * 
     * @param buffer byte buffer
     * @param len String length
     * @return the String
     */
    public static String getString(ByteBuffer buffer, int len) {
    	byte[] temp = new byte[len];
    	buffer.get(temp, 0, len);
    	return new String(temp);    	
    }
}   

