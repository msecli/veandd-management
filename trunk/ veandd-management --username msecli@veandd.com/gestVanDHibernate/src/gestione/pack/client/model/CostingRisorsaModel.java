package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CostingRisorsaModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CostingRisorsaModel(){
		
	}
	
	public CostingRisorsaModel(int idCostingRisorsa, String area, String cliente, String progetto, String commessa, int idRisorsa, String risorsa, Float costoOrario,
			Float oreLavoro, Float oreViaggio, int giorniViaggio, Float diaria, Float costoTotOre, Float costoTrasferta, Float costoTotale, String efficienza, 
			Float oreDaFatturare, Float oreTrasferta, String tariffa, Float fatturatoTotale, String ebit, String ebitPerc){
		
		set("idCostingRisorsa",idCostingRisorsa);
		set("area",area);	
		set("cliente",cliente);
		set("progetto",progetto);
		set("commessa",commessa);
		set("idRisorsa", idRisorsa);
		set("risorsa", risorsa);		
		set("costoOrario",costoOrario);
		set("oreLavoro", oreLavoro);
		set("oreViaggio", oreViaggio);
		set("giorniViaggio", giorniViaggio);
		set("diaria",diaria);
		set("costoTotOre",costoTotOre);
		set("costoTrasferta", costoTrasferta);
		set("costoTotale", costoTotale);
		set("efficienza", efficienza);
		set("oreDaFatturare", oreDaFatturare);
		set("oreTrasferta", oreTrasferta);
		set("tariffa",tariffa);
		set("fatturatoTotale", fatturatoTotale);
		set("ebit",ebit);
		set("ebitPerc",ebitPerc);
		
	}
}
