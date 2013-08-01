package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoMeseGiornalieroModel extends BaseModel implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiepilogoMeseGiornalieroModel(){
		
	}
	
	public RiepilogoMeseGiornalieroModel(String username,  String dipendente,
			String giorno1, String giorno2, String giorno3,String giorno4,String giorno5,String giorno6,String giorno7,String giorno8,
			String giorno9,String giorno10,String giorno11,String giorno12,String giorno13,String giorno14,String giorno15,String giorno16,String giorno17,String giorno18,String giorno19,String giorno20,
			String giorno21,String giorno22,String giorno23,String giorno24,String giorno25,String giorno26,String giorno27,String giorno28,String giorno29,String giorno30,String giorno31){
	
		set("username",username);
		set("dipendente", dipendente);
		set("giorno1", giorno1); //in ogni giorno indico un flag colorato che indichi lo stato compilazione
		set("giorno2", giorno2);
		set("giorno3",giorno3);
		set("giorno4",giorno4);
		set("giorno5",giorno5);
		set("giorno6",giorno6);
		set("giorno7",giorno7);
		set("giorno8",giorno8);
		set("giorno9",giorno9);
		set("giorno10",giorno10);
		set("giorno11",giorno11);
		set("giorno12",giorno12);
		set("giorno13",giorno13);
		set("giorno14",giorno14);
		set("giorno15",giorno15);
		set("giorno16",giorno16);
		set("giorno17",giorno17);
		set("giorno18",giorno18);
		set("giorno19",giorno19);
		set("giorno20",giorno20);
		set("giorno21",giorno21);
		set("giorno22",giorno22);
		set("giorno23",giorno23);
		set("giorno24",giorno24);
		set("giorno25",giorno25);
		set("giorno26",giorno26);
		set("giorno27",giorno27);
		set("giorno28",giorno28);
		set("giorno29",giorno29);
		set("giorno30",giorno30);
		set("giorno31",giorno31);
		
	}
}
