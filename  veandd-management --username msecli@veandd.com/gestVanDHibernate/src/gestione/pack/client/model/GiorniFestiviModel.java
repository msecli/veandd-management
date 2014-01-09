package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GiorniFestiviModel extends BaseModel implements IsSerializable{

	private static final long serialVersionUID = 1L;

	public GiorniFestiviModel(){
		
	}
	
	public GiorniFestiviModel(int idGiorno, Date giorno){
		set("idGiorno", idGiorno);
		
		set("giorno", giorno);
	}
	
}
