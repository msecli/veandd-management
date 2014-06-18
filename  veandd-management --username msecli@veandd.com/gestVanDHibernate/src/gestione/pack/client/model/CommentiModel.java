package gestione.pack.client.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CommentiModel extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentiModel(){
		
	}
	
	public CommentiModel(int id, String username, String data, String testo, Boolean editated, String sedeOperativa ){
		
		set("id",id);
		set("username",username);
		set("data",data);
		set("testo",testo);
		set("editated", editated);
		set("sedeOperativa", sedeOperativa);
		
	}
	
}
