package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CostiHwSwModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CostiHwSwModel(){
		
	}
	
	public CostiHwSwModel(int idCosto, String tipologia, String descrizione, String costo, boolean utilizzato ){
		set("idCosto",idCosto);
		set("tipologia",tipologia);
		set("descrizione",descrizione);
		set("costo",costo);
		set("utilizzato",utilizzato); //se esiste l'associazione è utilizzato
	}
	
	
	
}
