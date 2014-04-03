package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GestioneCostiDipendentiModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GestioneCostiDipendentiModel(){
		
	}
	
	public GestioneCostiDipendentiModel(int idPersonale, String cognome, String costoAnnuo, String oreAnno, String costoStruttura, 
			String costoOneri, String tipoOrario, String costoSwCadVari, String costoSwOffice, String costoHw, Float orePianificate, 
			String costoOrarioTot, String gruppoLavoro){
		
		set("idPersonale", idPersonale);
		set("cognome",cognome);
    	set("costoAnnuo", costoAnnuo);
    	set("oreAnno", oreAnno);
    	set("costoStruttura", costoStruttura);
    	set("costoOneri", costoOneri);
    	set("tipoOrario", tipoOrario);
    	set("costoSwCadVari",costoSwCadVari);
    	set("costoSwOffice",costoSwOffice);
    	set("costoHw",costoHw);
    	set("orePianificate", orePianificate);
    	set("costoOrarioTot", costoOrarioTot);
    	set("gruppoLavoro", gruppoLavoro);
    	
	}
	
}
