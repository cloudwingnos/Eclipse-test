package app.model.preset;
/** 

* @author  Donghao.F 

* @date 2022 Jul 14 14:24:00 

* 

*/
import java.io.Serializable;

public class CloRec implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2801100803438178311L;
	int clo;
	int type;
	int len;

	public CloRec() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	public CloRec(int clo, int type, int len) {
		super();
		this.clo = clo;
		this.type = type;
		this.len = len;
	}



	public int getClo() {
		return clo;
	}


	public void setClo(int clo) {
		this.clo = clo;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getLen() {
		return len;
	}


	public void setLen(int len) {
		this.len = len;
	}
	
	

}
