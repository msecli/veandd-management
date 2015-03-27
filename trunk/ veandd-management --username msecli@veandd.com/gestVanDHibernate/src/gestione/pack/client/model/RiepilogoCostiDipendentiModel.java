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
	
	public RiepilogoCostiDipendentiModel(int idDip, int idCosto, String nome, String costoAnnuo, String tipoOrario,  String orePreviste, String costoOrario, 
			String costoTrasferta, String costoStruttura, String costoOneri, String costoSw, String costoHw, String costoHwSw, String costoOrarioTotale, String gruppoLavoro){
		
		set("idPersonale", idDip);
		set("idCosto",idCosto);
		set("nome", nome);
		set("costoAnnuo", costoAnnuo);
		set("tipoOrario", tipoOrario);
		set("orePreviste", orePreviste);
		set("costoOrario", costoOrario);
		set("costoTrasferta", costoTrasferta);
		set("costoStruttura", costoStruttura);
		set("costoOneri", costoOneri);
		set("costoSw",costoSw);
		set("costoHw", costoHw);
		set("costoHwSw", costoHwSw);
		set("costoOrarioTotale", costoOrarioTotale);	
		set("gruppoLavoro", gruppoLavoro);
		
	}
	
	
}
