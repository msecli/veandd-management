package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoCostiDipSuCommesseFatturateModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoCostiDipSuCommesseFatturateModel(){
		
	}
	
	
	public RiepilogoCostiDipSuCommesseFatturateModel(String pm, String numeroCommessa, String estensione, String attivita, 
			String dipendente, Float oreEseguite, Float costoOrario, Float costoTotale, Float importoFatturato, Float importoScaricato){
		
		set("pm",pm);
		set("numeroCommessa", numeroCommessa);
		set("estensione", estensione);
		set("attivita", attivita);
		set("dipendente", dipendente);
		set("oreEseguite", oreEseguite);
		set("costoOrario", costoOrario);
		set("costoTotale", costoTotale);
		set("importoFatturato", importoFatturato);
		set("importoScaricato", importoScaricato);
		
	}
	
}
