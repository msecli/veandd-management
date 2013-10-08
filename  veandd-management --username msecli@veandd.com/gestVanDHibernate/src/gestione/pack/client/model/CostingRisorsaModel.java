package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CostingRisorsaModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CostingRisorsaModel(){
		
	}
	
	public CostingRisorsaModel(int idCostingRisorsa, String area, String cliente, String progetto, String commessa, int idRisorsa, String risorsa, String costoOrario, String orePianificate,
			String oreCorrette, String lc, String costoRisorsa, String costoOrarioStruttura, String costoRisorsaStruttura, String costoTotaleAzienda, String incidenzaCostiAzienda,
			String costoHwSw, String costoOneri, String costoHSommaHwSwOneri, String costoRisorsaSommaHwSwOneri, String incidenzaCostiHwSw, String costoConsulenza, 
			String costoTotaleHwSw, String efficienza, String oreFatturare,  String tariffa, String tariffaDerivata, String fatturato, String mol, String molPerc, String ebit, String ebitPerc){
		
		set("idCostingRisorsa",idCostingRisorsa);
		set("area",area);	
		set("cliente",cliente);
		set("progetto",progetto);
		set("commessa",commessa);
		set("idRisorsa", idRisorsa);
		set("risorsa", risorsa);		
		set("costoOrario",costoOrario);
		set("orePianificate",orePianificate);
		set("oreCorrette",oreCorrette);
		set("lc",lc);
		set("costoRisorsa",costoRisorsa);
		set("costoOrarioStruttura",costoOrarioStruttura);
		set("costoRisorsaStruttura",costoRisorsaStruttura);
		set("costoTotaleAzienda",costoTotaleAzienda);
		set("incidenzaCostiAzienda",incidenzaCostiAzienda);		
		set("costoHwSw",costoHwSw);
		set("costoOneri",costoOneri);
		set("costoSommaHwSwOneri",costoHSommaHwSwOneri);
		set("costoRisorsaSommaHwSwOneri",costoRisorsaSommaHwSwOneri);
		set("incidenzaCostiHwSw",incidenzaCostiHwSw);
		set("costoConsulenza",costoConsulenza);
		set("costoTotaleHwSw",costoTotaleHwSw);
		set("efficienza",efficienza);
		set("oreFatturare",oreFatturare);
		set("tariffa",tariffa);
		set("tariffaDerivata",tariffaDerivata);
		set("fatturato",fatturato);
		set("mol",mol);
		set("molPerc",molPerc);
		set("ebit",ebit);
		set("ebitPerc",ebitPerc);
		
	}
}
