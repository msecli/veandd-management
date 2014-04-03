package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoCostiDipendentiModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoCostiDipendentiModel(){
		
	}
	
	public RiepilogoCostiDipendentiModel(int idDip, String nome, String costoAnnuo, String tipoOrario,  String orePreviste, String costoOrario,
			 String costoStruttura, String costoOneri, String costoSwCadVari, String costoSwOffice,  String costoHw, String costoOrarioTotale, String gruppoLavoro){
		
		set("idPersonale", idDip);
		set("nome", nome);
		set("costoAnnuo", costoAnnuo);
		set("tipoOrario", tipoOrario);
		set("orePreviste", orePreviste);
		set("costoOrario", costoOrario);
		set("costoStruttura", costoStruttura);
		set("costoOneri", costoOneri);
		set("costoSwCadVari",costoSwCadVari);
		set("costoSwOffice", costoSwOffice);
		set("costoHw", costoHw);
		set("costoOrarioTotale", costoOrarioTotale);	
		set("gruppoLavoro", gruppoLavoro);
		
	}
	
	
}
