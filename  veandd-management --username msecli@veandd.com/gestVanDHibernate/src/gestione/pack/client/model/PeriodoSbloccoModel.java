package gestione.pack.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PeriodoSbloccoModel extends BaseModel implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PeriodoSbloccoModel(){
		
	}
	
	public PeriodoSbloccoModel(int idPeriodo, String sede, Date dataInizio, Date dataFine){
		set("idPeriodo", idPeriodo);
		set("sede",sede);
		set("dataInizio", dataInizio);
		set("dataFine",dataFine);
	}

}
