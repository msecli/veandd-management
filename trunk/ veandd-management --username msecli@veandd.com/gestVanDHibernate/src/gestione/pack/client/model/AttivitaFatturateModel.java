package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AttivitaFatturateModel extends BaseModel  implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttivitaFatturateModel(){
		
	}
	
	public AttivitaFatturateModel(int idAttivita, String descrizione, String importo){
		set("importo", importo);
		set("descrizione", descrizione);
		set("idAttivita", idAttivita);
	}
	
}
