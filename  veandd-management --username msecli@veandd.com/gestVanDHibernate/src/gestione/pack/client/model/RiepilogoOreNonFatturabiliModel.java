package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreNonFatturabiliModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RiepilogoOreNonFatturabiliModel(){}
	
	public RiepilogoOreNonFatturabiliModel (String sede, String pm, String attivita, String risorsa, String m1,
			String m2, String m3, String m4, String m5, String m6, String m7, String m8, String m9, String m10, String m11, String m12){
		
		set("sede", sede);
		set("pm", pm);
		set("attivita", attivita);
		set("risorsa", risorsa);
		set("m1", m1);
		set("m2", m2);
		set("m3", m3);
		set("m4", m4);
		set("m5", m5);
		set("m6", m6);
		set("m7", m7);
		set("m8", m8);
		set("m9", m9);
		set("m10", m10);
		set("m11", m11);
		set("m12", m12);
		
	}
	
	

}
