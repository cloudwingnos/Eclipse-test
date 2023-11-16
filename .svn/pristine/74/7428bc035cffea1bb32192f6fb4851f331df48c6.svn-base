package cn.sgepri.adapter.bus;
/** 

* @author  Donghao.F 

* @date 2021 Mar 14 12:24:00 

* 

*/
/**
 *  Bus message header 
 *
 */
public class MessageHeader
{
    public short       	len;
    public short       	serv;
    public short       	seq;
    public short       	event;
    public char   		domain;
    public char   		ctxt;
    public short       	stid;
    public short       	dtid;
    public char   		ver_coding;
    public char   		remain;
    
    /**
     * copy function for the message header
     * 
     * @param from copy from 
     * @param to copy to
     */
	public static void copy(MessageHeader from, MessageHeader to) {
		to.len = from.len;
		to.serv = from.serv;
		to.seq = from.seq;
		to.event = from.event;
		to.domain = from.domain;
		to.ctxt = from.ctxt;
		to.stid = from.stid;
		to.dtid = from.dtid;
		to.ver_coding = from.ver_coding;
		to.remain = from.remain;
	}
}
