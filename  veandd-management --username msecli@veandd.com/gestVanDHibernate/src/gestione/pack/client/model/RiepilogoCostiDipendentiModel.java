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
	
	public RiepilogoCostiDipendentiModel(int idDip, String nome, String costoAnnuo, String costoOrario, String gruppoLavoro, String costoStruttura, 
			String costoOneri, String tipoCad, String tipoTc, String costoCad, String costoTc, String tipoHardware, String costoHardware, String sommaCostoHwSw,
			String costoAggiuntivo, String costoTotaleRisorsa, String tipoOrario, String colocation, Float oreCig, Float orePreviste, Float orePianificate,
			String saturazione, String oreAssegnare){
		
		set("idPersonale", idDip);
		set("nome", nome);
		set("costoAnnuo", costoAnnuo);
		set("costoOrario", costoOrario);
		set("gruppoLavoro", gruppoLavoro);
		set("costoStruttura", costoStruttura);
		set("costoOneri", costoOneri);
		set("tipoCad", tipoCad);
		set("tipoTc", tipoTc);
		set("costoCad", costoCad);
		set("costoTc", costoTc);
		set("tipoHardware", tipoHardware);
		set("costoHardware", costoHardware);
		set("sommaCostoHwSw", sommaCostoHwSw);
		set("costoAggiuntivo", costoAggiuntivo);
		set("costoTotaleRisorsa", costoTotaleRisorsa);
		set("tipoOrario", tipoOrario);
		set("colocation", colocation);
		set("oreCig", oreCig);
		set("orePreviste", orePreviste);
		set("orePianificate", orePianificate);
		set("saturazione", saturazione);
		set("oreAssegnare", oreAssegnare);	
		
	}
	
	
}
