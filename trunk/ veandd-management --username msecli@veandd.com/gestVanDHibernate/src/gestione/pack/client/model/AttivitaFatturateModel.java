package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AttivitaFatturateModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttivitaFatturateModel(){
		
	}
	
	public AttivitaFatturateModel(String descrizione, String importo){
		set("importo", importo);
		set("descrizione", descrizione);
	}
	
}
