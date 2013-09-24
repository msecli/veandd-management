package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GestioneCostiDipendentiModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GestioneCostiDipendentiModel(){
		
	}
	
	public GestioneCostiDipendentiModel(int idPersonale, String cognome, String costoAnnuo, String costoStruttura, String costoOneri, String tipoOrario, Float oreCig, Float orePianificate
			){
		
		set("idPersonale", idPersonale);
		set("cognome",cognome);
    	set("costoAnnuo", costoAnnuo);
    	set("costoStruttura", costoStruttura);
    	set("costoOneri", costoOneri);
    	set("tipoOrario", tipoOrario);
    	set("oreCig", oreCig);
    	set("orePianificate", orePianificate);
    	
	}
	
}
