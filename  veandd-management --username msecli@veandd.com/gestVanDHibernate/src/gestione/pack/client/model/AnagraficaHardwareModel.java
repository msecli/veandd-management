package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AnagraficaHardwareModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AnagraficaHardwareModel(){
		
	}
	
	
	public AnagraficaHardwareModel(int idHardware, String username, String gruppoLavoro, String assistenza, String codiceModello,  String cpu, String fornitoreAssistenza, String hardware,
			String hd, String ip, String ipFiat, String modello, String nodo, String note, String ram, Date scadenzaControllo,  String sede,
			String serialId, String serialNumber, String sistemaOperativo, String stato, String svga, String tipologia, String utilizzo){
		
		set("idHardware", idHardware);
		set("username", username);
		set("gruppoLavoro", gruppoLavoro);
		set("assistenza", assistenza);
		set("codiceModello", codiceModello);
		set("cpu", cpu);
		set("fornitoreAssistenza", fornitoreAssistenza);
		set("hardware", hardware);
		set("hd",hd);
		set("ip",ip);
		set("ipFiat",ipFiat);
		set("modello", modello);
		set("nodo", nodo);
		set("note",note);
		set("ram", ram);
		set("scadenzaControllo", scadenzaControllo);
		set("sede",sede);
		set("serialId", serialId);
		set("serialNumber", serialNumber);
		set("sistemaOperativo", sistemaOperativo);
		set("stato",stato);
		set("svga",svga);
		set("tipologia", tipologia);
		set("utilizzo", utilizzo);
	}	
	
	public AnagraficaHardwareModel(int idHardware, String codiceModello){
		set("idHardware",idHardware);
		set("codiceModello", codiceModello);
	}

}
