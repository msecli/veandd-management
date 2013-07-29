package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoSALPCLModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoSALPCLModel(){}
	
	public RiepilogoSALPCLModel(String pm, String numeroCommessa, String estensione, String cliente, 
			String attivita, Float precedente, Float variazione, Float attuale, String tariffa, 
			Float importoComplessivo, Float oreEseguite, Float margine, Float importoMese){
		
		set("pm",pm);
		set("numeroCommessa", numeroCommessa);	
		set("estensione", estensione);
		set("cliente",cliente);
		set("attivita", attivita);
		set("precedente", precedente);
		set("variazione",variazione);
		set("attuale",attuale);
		set("tariffa", tariffa);
		set("importoComplessivo",importoComplessivo);
		set("oreEseguite", oreEseguite);
		set("margine", margine);
		set("importoMese", importoMese);
	}
	
	
}
