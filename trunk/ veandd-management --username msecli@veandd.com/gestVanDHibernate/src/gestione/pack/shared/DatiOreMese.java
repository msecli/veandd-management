package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the dettaglio_ore_giornaliere database table.
 * 
 */
@Entity
@Table(name="datioremese")
public class DatiOreMese extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idDatiOreMese")
	private int idDatiOreMese;

	private String username;
    
	private String giorno;
	
	private String totGiorno;
	
	private String oreViaggio;
	
	private String deltaOreViaggio;

	private String oreTotali;
		
	private String oreRecupero;

	private String oreFerie;

	private String orePermesso;

	private String oreStraordinario;

	private String giustificativo;

	private String noteAggiuntive;

    public DatiOreMese() {
    }

	public int getIdDettaglioOreGiornaliere() {
		return this.idDatiOreMese;
	}

	public void setIdDettaglioOreGiornaliere(int idDettaglioOreGiornaliere) {
		this.idDatiOreMese = idDettaglioOreGiornaliere;
	}

	
	public String getGiorno() {
		return this.giorno;
	}

	public void setGiornoRiferimento(String giorno) {
		this.giorno = giorno;
	}

	public String getGiustificativo() {
		return this.giustificativo;
	}

	public void setGiustificativo(String giustificativo) {
		this.giustificativo = giustificativo;
	}

	public String getOreAssenzeRecupero() {
		return this.oreRecupero;
	}

	public void setOreAssenzeRecupero(String oreAssenzeRecupero) {
		this.oreRecupero = oreAssenzeRecupero;
	}

	public String getOreViaggio() {
		return this.oreViaggio;
	}

	public void setOreViaggio(String oreViaggio) {
		this.oreViaggio = oreViaggio;
	}

	public String getOreFerie() {
		return this.oreFerie;
	}

	public void setOreFerie(String oreFerie) {
		this.oreFerie = oreFerie;
	}

	public String getOrePermesso() {
		return this.orePermesso;
	}

	public void setOrePermesso(String orePermesso) {
		this.orePermesso = orePermesso;
	}

	public String getOreStraordinario() {
		return this.oreStraordinario;
	}

	public void setOreStraordinario(String oreStraordinario) {
		this.oreStraordinario = oreStraordinario;
	}

	public String getDeltaOreViaggio() {
		return deltaOreViaggio;
	}

	public void setDeltaOreViaggio(String deltaOreViaggio) {
		this.deltaOreViaggio = deltaOreViaggio;
	}

	public String getNoteAggiuntive() {
		return noteAggiuntive;
	}

	public void setNoteAggiuntive(String noteAggiuntive) {
		this.noteAggiuntive = noteAggiuntive;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTotGiorno() {
		return totGiorno;
	}

	public void setTotGiorno(String totGiorno) {
		this.totGiorno = totGiorno;
	}

	public String getOreTotali() {
		return oreTotali;
	}

	public void setOreTotali(String oreTotali) {
		this.oreTotali = oreTotali;
	}

	
}
