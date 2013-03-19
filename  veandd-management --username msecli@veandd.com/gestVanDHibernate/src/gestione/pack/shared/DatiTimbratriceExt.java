package gestione.pack.shared;


import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the dati_timbratrice_ext database table.
 * 
 */
@Entity
@Table(name="dati_timbratrice_ext")
public class DatiTimbratriceExt extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idTimbratura;
	
	private String codice;

	private String codice1;

	private String codice2;

	private String codice3;

	private String giorno;

	private String matricolaPersonale;

	private String movimento;

	private String orario;

    public DatiTimbratriceExt() {
    }

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getCodice1() {
		return this.codice1;
	}

	public void setCodice1(String codice1) {
		this.codice1 = codice1;
	}

	public String getCodice2() {
		return this.codice2;
	}

	public void setCodice2(String codice2) {
		this.codice2 = codice2;
	}

	public String getCodice3() {
		return this.codice3;
	}

	public void setCodice3(String codice3) {
		this.codice3 = codice3;
	}

	public String getGiorno() {
		return this.giorno;
	}

	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}

	public String getMatricolaPersonale() {
		return this.matricolaPersonale;
	}

	public void setMatricolaPersonale(String matricolaPersonale) {
		this.matricolaPersonale = matricolaPersonale;
	}

	public String getMovimento() {
		return this.movimento;
	}

	public void setMovimento(String movimento) {
		this.movimento = movimento;
	}

	public String getOrario() {
		return this.orario;
	}

	public void setOrario(String orario) {
		this.orario = orario;
	}

	public int getIdTimbratura() {
		return idTimbratura;
	}

	public void setIdTimbratura(int idTimbratura) {
		this.idTimbratura = idTimbratura;
	}

}