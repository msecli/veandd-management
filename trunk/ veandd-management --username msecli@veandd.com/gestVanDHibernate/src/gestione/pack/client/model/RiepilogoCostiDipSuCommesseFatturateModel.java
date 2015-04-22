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
	
	
	public RiepilogoCostiDipSuCommesseFatturateModel(int idCommessa, String pm, String numeroCommessa, String commessa, String estensione, String attivita, 
			String dipendente, Float oreEseguite, Float costoOrario, Float costoTotale, Float importoFatturato, Float importoScaricato, 
			Float margine, Float rapporto, Float importoBilancio, Float differenza, Float diffPerc){
		
		set("idCommessa",idCommessa);
		set("pm",pm);
		set("numeroCommessa", numeroCommessa);
		set("commessa", commessa);
		set("estensione", estensione);
		set("attivita", attivita);
		set("dipendente", dipendente);
		set("oreEseguite", oreEseguite);
		set("costoOrario", costoOrario);
		set("costoTotale", costoTotale);
		set("importoFatturato", importoFatturato);
		set("importoScaricato", importoScaricato);
		set("margine",margine);
		set("rapporto",rapporto);// margine/fatturato
		set("importoBilancio", importoBilancio);
		set("differenzaBilancioScaricato", differenza);
		set("differenzaPerc", diffPerc);
		
	}
	
}
