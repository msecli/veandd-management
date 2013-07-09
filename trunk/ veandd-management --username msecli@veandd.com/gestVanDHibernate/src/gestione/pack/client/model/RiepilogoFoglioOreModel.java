package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoFoglioOreModel extends BaseModel implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RiepilogoFoglioOreModel(){
		
	}
	
	public RiepilogoFoglioOreModel(int idDettaglioGiorno, String nome, String mese, String giorno, String orePreviste, Float oreTimbrature, Float oreViaggio, Float deltaViaggio, Float oreTotali, Float oreFerie, 
			Float orePermesso, Float oreRecupero, Float oreStraordinario, Float oreAbbuono, String giustificativo, String note, Boolean compilato){
		
		set("idDettaglio", idDettaglioGiorno);
		set("nome",nome);
		set("mese", mese);
		set("giorno", giorno);
		set("orePreviste", orePreviste);
		set("oreTimbrature", oreTimbrature);
		set("oreViaggio", oreViaggio);
		set("deltaViaggio", deltaViaggio);
		set("oreTotali", oreTotali);
		set("oreFerie", oreFerie);
		set("orePermesso", orePermesso);
		set("oreRecupero", oreRecupero);
		set("oreStraordinario", oreStraordinario);
		set("oreAbbuono", oreAbbuono);
		set("giustificativo", giustificativo);
		set("note", note);
		set("compilato",compilato);
	}

	public RiepilogoFoglioOreModel(int idDettaglioGiorno, String username,
			String nome, String mese, String giorno, boolean compilato, String confermato) {		
		set("idDettaglio", idDettaglioGiorno);
		set("username",username);
		set("nome",nome);
		set("mese", mese);
		set("giorno", giorno);
		set("compilato",compilato);
		set("confermato", confermato);
	}

	public int getDettaglioGiorno(){
		return get("idDettaglio");
	}
	
	public String getOrePreviste(){
		return get("orePreviste");
	}
	
	public Float getOreTimbrature(){
		return get("oreTimbrature");
	}
	public Float getOreViaggio(){
		return get("oreViaggio");
	}
	public Float getDeltaViaggio(){
		return get("deltaViaggio");
	}
	public Float getOreTotali(){
		return get("oreTotali");
	}
	public Float getOreFerie(){
		return get("oreFerie");
	}
	public Float getOrePermesso(){
		return get("orePermesso");
	}
	public Float getOreRecupero(){
		return get("oreRecupero");
	}
	public Float getOreStraordinario(){
		return get("oreStraordinario");
	}

	public Float getOreAbbuono() {
		return get("oreAbbuono");
	}
	
	public String getNome(){
		return get("nome");
	}
	
}
