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
			Float orePermesso, Float oreRecupero, Float oreStraordinario, Float oreAbbuono, String giustificativo, String note, String statoCompilazione){
		
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
		set("compilato",statoCompilazione);
	}

	public RiepilogoFoglioOreModel(String username,
			String nome, String mese, String giorno, boolean compilato, String confermato, String i1,
			String u1, String i2, String u2, String i3, String u3, String i4, String u4) {		
		set("username",username);
		set("nome",nome);
		set("mese", mese);
		set("giorno", giorno);
		set("compilato",compilato);
		set("confermato", confermato);
		set("i1",i1);
		set("u1",u1);
		set("i2",i2);
		set("u2",u2);
		set("i3",i3);
		set("u3",u3);
		set("i4",i4);
		set("u4",u4);
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
