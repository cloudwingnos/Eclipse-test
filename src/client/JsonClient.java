package client;
/**
* @author Donghao
* @version date��2021��11��6�� ����4:17:21
* 
*/

import java.util.Set;

import com.google.gson.JsonObject;
import com.kedong.platform.imdg.core.KDMDGInstance;

import cn.sg.model.physical.PModelContainer;
import cn.sg.model.physical.core.JsonAdapter;

public class JsonClient {

	public PlatfromClient client ;
	
	public JsonClient(KDMDGInstance hzInstance) {
		client = new PlatfromClient(hzInstance);
	}
	
	public JsonClient(String... strings) {
		client = new PlatfromClient(strings);
	}

	public Set<Object> getModelKey() {
		return client.getModelKey();
	}
	
	public JsonObject getModel(String key) {
		PModelContainer container = client.getContainer(key);
		return new JsonAdapter().toJson(container).getAsJsonObject();
	}
	
	public void shuntdown() {
		client.shutdown();
	}
}
