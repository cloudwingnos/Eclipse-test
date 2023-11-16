package app.model.data;

import java.io.Serializable;

import com.google.gson.Gson;

/**
* @author Donghao
* @version date：2022年5月16日 上午9:25:31
* 
*/
public class ModelData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5067389114191419112L;
	int noYC;
	int noYX;
	public ModelData(int noYC, int noYX) {
		super();
		this.noYC = noYC;
		this.noYX = noYX;
	}
	public int getNoYC() {
		return noYC;
	}
	public int getNoYX() {
		return noYX;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
}
