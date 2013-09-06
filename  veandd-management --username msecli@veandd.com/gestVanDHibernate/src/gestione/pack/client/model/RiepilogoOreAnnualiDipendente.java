package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreAnnualiDipendente extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoOreAnnualiDipendente(){
		
	}
	
	public RiepilogoOreAnnualiDipendente(String anno,String cognome, String nome, Float oreOrdinarie, Float oreStraordinarie,Float oreRecupero
			, Float oreViaggio, Float totaleOreLavoro, Float oreFerie, Float orePermessoRol, Float oreMutua, Float oreCig, Float oreLegge104, Float oreMaternita,   Float oreTotaliGiustificativi ){
		
		set("anno",anno);
		set("cognome",cognome);
		set("nome",nome);
		set("oreOrdinarie",oreOrdinarie);
		set("oreStraordinarie", oreStraordinarie);
		set("oreRecupero", oreRecupero);
		set("oreViaggio", oreViaggio);
		set("totaleOreLavoro", totaleOreLavoro);
		set("oreFerie", oreFerie);
		set("orePermessoRol", orePermessoRol);
		set("oreMutua", oreMutua);
		set("oreCig", oreCig);
		set("oreLegge104",oreLegge104);
		set("oreMaternita", oreMaternita);
		set("oreTotaliGiustificativi", oreTotaliGiustificativi);
	
	}
	
	
	
}
