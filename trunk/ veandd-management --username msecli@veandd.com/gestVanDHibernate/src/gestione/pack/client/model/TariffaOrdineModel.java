package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TariffaOrdineModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TariffaOrdineModel(){
		
	}
	
	public TariffaOrdineModel(String idAttivitaOrdine, String tariffaAttivita, String descrizione){
		
		set("idAttivitaOrdine",idAttivitaOrdine);
		set("tariffaAttivita",tariffaAttivita);
		set("descrizione",descrizione);
		
	}
	
}
