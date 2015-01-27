package gestione.pack.client.model;

import java.util.HashSet;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoMensileOrdiniModel extends BaseModel implements IsSerializable{

	
	private static final long serialVersionUID = 1L;

	private Set<CommessaModel> listaCommesse= new HashSet<CommessaModel>();
	
	public RiepilogoMensileOrdiniModel(){
		
	}
	
	public RiepilogoMensileOrdiniModel(int idAttivitaOrdine,String cliente, String pm, String numeroOrdine, String dataOrdine, String commessa, String numeroRda, String attivita,
			String numeroOfferta, String tariffa, Float importoOrdine, Float oreOrdine,  Float importoResiduo, Float oreResidue, String stato){
		
		set("idAttivitaOrdine",idAttivitaOrdine);
		set("cliente",cliente);
		set("pm",pm);
		set("numeroOrdine",numeroOrdine);
		set("dataOrdine",dataOrdine);
		set("commessa",commessa);
		set("numeroRda",numeroRda);
		set("attivita",attivita);
		set("numeroOfferta",numeroOfferta);
		set("tariffa",tariffa);
		set("importoOrdine",importoOrdine);
		set("oreOrdine",oreOrdine);
		set("importoResiduo",importoResiduo);
		set("oreResidue",oreResidue);
		set("statoOrdine",stato);
		
	}
	
	public Set<CommessaModel> getListaCommessa() {
		return this.listaCommesse;
	}

	public void setRdas(Set<CommessaModel> listaCommesse) {
		this.listaCommesse = listaCommesse;
	}
	
	public RiepilogoMensileOrdiniModel(int idAttivitaOrdine,String cliente, String pm, String numeroOrdine, String dataOrdine, String commessa, String numeroRda, String attivita,
			String numeroOfferta, String tariffa, Float importoOrdine, Float oreOrdine,  
			 Float m1, Float m2, Float m3, Float m4, Float m5, Float m6, Float m7, Float m8, Float m9, Float m10, Float m11, Float m12,
			Float importoResiduo, Float oreResidue, String stato){
		
		set("idAttivitaOrdine",idAttivitaOrdine);
		set("cliente",cliente);
		set("pm",pm);
		set("numeroOrdine",numeroOrdine);
		set("dataOrdine",dataOrdine);
		set("commessa",commessa);
		set("numeroRda",numeroRda);
		set("attivita",attivita);
		set("numeroOfferta",numeroOfferta);
		set("tariffa",tariffa);
		set("importoOrdine",importoOrdine);
		set("oreOrdine",oreOrdine);
		set("importoResiduo",importoResiduo);
		set("oreResidue",oreResidue);
		set("statoOrdine",stato);
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
