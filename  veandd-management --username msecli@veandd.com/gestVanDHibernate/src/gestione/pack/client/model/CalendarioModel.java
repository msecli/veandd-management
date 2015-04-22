package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CalendarioModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalendarioModel(){
		
	}
	
	public CalendarioModel(int idCalendario, String anno, String mese, String ore, String giorni, Float importoMese, String pm){
		set("idCalendario", idCalendario);
		set("anno", anno);
		set("mese", mese);
		set("ore", ore);
		set("giorni", giorni);
		set("importoMese", importoMese);
		set("pm", pm);
	}
	
}
