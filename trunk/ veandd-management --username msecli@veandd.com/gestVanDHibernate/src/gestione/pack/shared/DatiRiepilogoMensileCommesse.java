package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the dettaglio_ore_giornaliere database table.
 * 
 */
@Entity
@Table(name="dati_riepilogomensile_commesse")
public class DatiRiepilogoMensileCommesse extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idRiepilogo")
	private int idDatiOreMese;
	private String username;
	private String dataRiferimento;
	private String numeroCommessa;
	private String giorno;
	private String oreLavoro;
	private String oreViaggio;
	private String oreTotali;
	public int getIdDatiOreMese() {
		return idDatiOreMese;
	}
	public void setIdDatiOreMese(int idDatiOreMese) {
		this.idDatiOreMese = idDatiOreMese;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDataRiferimento() {
		return dataRiferimento;
	}
	public void setDataRiferimento(String dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}
	public String getNumeroCommessa() {
		return numeroCommessa;
	}
	public void setNumeroCommessa(String numeroCommessa) {
		this.numeroCommessa = numeroCommessa;
	}
	public String getGiorno() {
		return giorno;
	}
	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}
	public String getOreLavoro() {
		return oreLavoro;
	}
	public void setOreLavoro(String oreLavoro) {
		this.oreLavoro = oreLavoro;
	}
	public String getOreViaggio() {
		return oreViaggio;
	}
	public void setOreViaggio(String oreViaggio) {
		this.oreViaggio = oreViaggio;
	}
	public String getOreTotali() {
		return oreTotali;
	}
	public void setOreTotali(String oreTotali) {
		this.oreTotali = oreTotali;
	}

}
