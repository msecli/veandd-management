package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;


public class OffertaModel  extends BaseModel implements IsSerializable{


	private static final long serialVersionUID = 1L;

	public OffertaModel(){}
	
	ClienteModel c= new ClienteModel();
	
	public OffertaModel(Integer idOfferta, Integer idCliente, String numOfferta, String ragioneSociale, String dataOfferta, 
			String descrizione, String importo, String statoOfferta, ClienteModel cM){
		
		set("idOfferta", idOfferta );
		set("idCliente", idCliente);
    	set("numeroOfferta", numOfferta);
    	set("ragioneSociale",ragioneSociale);
    	set("descrizione", descrizione);
    	set("importo", importo);
    	set("dataOfferta", dataOfferta);
    	set("statoOfferta", statoOfferta);
    	set("cliente", cM);
		
	}
	
	public ClienteModel getCliente(){
		return c;
	}
	
	public void setCliente(ClienteModel c){
		this.c=c;
	}
	
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
