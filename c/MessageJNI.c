/* ************************************************************************
 *       Filename:  HelloJNI.c
 *    Description:  
 *        Version:  1.0
 *        Created:  2016年06月03日 14时29分58秒
 *       Revision:  none
 *       Compiler:  gcc
 *         Author:  YOUR NAME (), 
 *        Company:  KeDong
 * ************************************************************************/

#include "app_model_update_jni_MessageJNI.h"
#include "message_inv.h"
#define BYTE unsigned char
message_invocation  inv;
int ret;
JNIEXPORT jint JNICALL JNICALL Java_app_model_update_jni_MessageJNI_messageInit(JNIEnv *env, jclass jc, jstring context_name, jstring app_name, jstring proc_name)
{
    char* context = (char*)env->GetStringUTFChars(context_name,0);  
    char* app = (char*)env->GetStringUTFChars(app_name,0);  
    char* proc = (char*)env->GetStringUTFChars(proc_name,0);  
    printf("MessageInit ctxt:%s app:%s proc:%s\n", context,app,proc);
    ret=inv.messageInit((char*)context,(char*)app,(char*)proc);
    env->ReleaseStringUTFChars(context_name, context);
    env->ReleaseStringUTFChars(app_name, app);
    env->ReleaseStringUTFChars(proc_name, proc);
    	return ret;
}
JNIEXPORT jint JNICALL Java_app_model_update_jni_MessageJNI_messageExit (JNIEnv *env, jclass jc, jint proc_key)
{
    ret=inv.messageExit(proc_key);
	return ret;
}
JNIEXPORT jint JNICALL Java_app_model_update_jni_MessageJNI_messageSubscribe(JNIEnv *env, jclass jc, jshort set_id, jstring context_name)
{
    char* context = (char*)env->GetStringUTFChars(context_name,0);
    printf("messageSubscribe ctxt:%s set_id:%d\n", context,set_id);
    ret=inv.messageSubscribe(set_id,(char*)context);
    env->ReleaseStringUTFChars(context_name, context);
	return ret;
}
JNIEXPORT jint JNICALL Java_app_model_update_jni_MessageJNI_messageUnSubscribe (JNIEnv *env, jclass jc, jshort set_id, jstring context_name) 
{
    char* context = (char*)env->GetStringUTFChars(context_name,0);
    printf("messageUnSubscribe ctxt:%s set_id:%d\n", context,set_id);
    ret=inv.messageUnSubscribe(set_id,(char*)context);    
    env->ReleaseStringUTFChars(context_name, context);
	return ret;
}

JNIEXPORT jint JNICALL JNICALL Java_app_model_update_jni_MessageJNI_messageSend (JNIEnv *env, jclass jc, jobject mobj, jint len)
{  
    Message msg;
    memset(&msg,0,sizeof(Message));
    jclass objectClass =  env->GetObjectClass(mobj);         
    
    jfieldID temServ = env->GetFieldID(objectClass,"serv","S");
    msg.header.serv = env->GetCharField(mobj, temServ);
    jfieldID temEvent = env->GetFieldID(objectClass,"event","S");
    msg.header.event = env->GetCharField(mobj, temEvent);
    jfieldID byteData = env->GetFieldID(objectClass,"Msg_buf","[B");
    jbyteArray pDataIn = (jbyteArray) (env)->GetObjectField(mobj, byteData);
    jbyte * olddata = (jbyte*)env->GetByteArrayElements(pDataIn, 0);  
    jsize  oldsize = env->GetArrayLength(pDataIn);  
    BYTE* bytearr = (BYTE*)olddata;  
    char* tmpdata = (char*)bytearr;
    msg.header.len = (short)oldsize; 
    memmove(msg.Msg_buf,tmpdata,msg.header.len);
    ret=inv.messageSend((Message*)&msg,msg.header.len);
    env->DeleteLocalRef(objectClass);
    env->DeleteLocalRef(mobj);
    env->ReleaseByteArrayElements(pDataIn, olddata, 0);
    return ret;

}

JNIEXPORT jint JNICALL JNICALL Java_app_model_update_jni_MessageJNI_messageReceive(JNIEnv *env, jclass jc, jobject mobj)
{
    Message msg;
    memset(&msg,0,sizeof(msg));
    ret=inv.messageReceive(&msg);
	if(ret<0){
		return ret;
	}
    jclass clazz;
    jfieldID fid;
    clazz =env->GetObjectClass(mobj);
    if (0 == clazz) {
        printf("GetObjectClass returned 0\n");
        return (-1);
    }
    

    fid = env->GetFieldID(clazz, "len", "S");
    env->SetShortField( mobj, fid, msg.header.len);
    fid = env->GetFieldID(clazz, "serv", "S");
    env->SetShortField( mobj, fid, msg.header.serv);
    fid = env->GetFieldID( clazz, "seq", "S");
    env->SetShortField( mobj, fid, msg.header.seq);
    fid = env->GetFieldID( clazz, "event", "S");
    env->SetShortField( mobj, fid, msg.header.event);
    fid = env->GetFieldID( clazz, "domain", "C");
    env->SetCharField( mobj, fid, msg.header.domain);
    fid = env->GetFieldID( clazz, "ctxt", "C");
    env->SetCharField( mobj, fid, msg.header.ctxt);
    fid = env->GetFieldID( clazz, "stid", "S");
    env->SetShortField( mobj, fid, msg.header.stid);
    fid = env->GetFieldID( clazz, "dtid", "S");
    env->SetShortField( mobj, fid, msg.header.dtid);
    fid = env->GetFieldID( clazz, "ver_coding", "C");
    env->SetCharField( mobj, fid, msg.header.ver_coding);
    fid = env->GetFieldID( clazz, "remain", "C");
    env->SetCharField( mobj, fid, msg.header.remain);

    char *pat;
    BYTE *pData;
    jbyte *jy;
    pat = (char*)malloc(msg.header.len);
    memmove(pat,msg.Msg_buf,msg.header.len);

    jbyteArray  jbarray;

    pData=(BYTE*)pat;           //将char强制转换成BYTE
    jbarray = env->NewByteArray(msg.header.len);//建立jbarray数组
    jy=(jbyte*)pData;  //BYTE强制转换成Jbyte；
    env->SetByteArrayRegion(jbarray, 0, msg.header.len, jy);            //将Jbyte 转换为jbarray数组

    fid=env->GetFieldID(clazz,"Msg_buf","[B"); 
    env->SetObjectField(mobj, fid, jbarray); 
    env->DeleteLocalRef(clazz);
    free(pat);
    env->DeleteLocalRef(jbarray);
	return ret;


}
