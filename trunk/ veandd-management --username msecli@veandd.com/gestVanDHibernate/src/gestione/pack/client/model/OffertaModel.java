package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;


public class OffertaModel  extends BaseModel implements IsSerializable{


	private static final long serialVersionUID = 1L;

	public OffertaModel(){}
	
	public OffertaModel(int idofferta, String numOfferta, String numRda, String dataOfferta, String descrizione, String tariffa){
		
		set("idOfferta", idofferta );
    	set("numeroOfferta", numOfferta);
    	set("numeroRda",numRda);
    	set("descrizione", descrizione);
    	set("tariffa", tariffa);
    	set("dataOfferta", dataOfferta);
		
	}
	
	public String getNumeroRda() {
		return get("numeroRda");
	}

	public int getIdOfferta() {
		return get("idOfferta");
	}
	
	public String getNumeroOfferta() {
		return get("numeroOfferta");
	}
	
	public String getDescrizione() {
		return get("descrizione");
	}
	
	public String getTariffa() {
		return get("tariffa");
	}
	
	public String getData(){
		
		return get("dataOfferta");
	}
}
