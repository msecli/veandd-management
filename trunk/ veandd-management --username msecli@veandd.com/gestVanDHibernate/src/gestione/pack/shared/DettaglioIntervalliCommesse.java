package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the dettaglio_intervalli_commesse database table.
 * 
 */
@Entity
@Table(name="dettaglio_intervalli_commesse")
public class DettaglioIntervalliCommesse extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_DETTAGLIO_INTERVALLI_COMMESSE")
	private int idDettaglioIntervalliCommesse;

	private String estensioneCommessa;

	private int idAttivita;

	private String numeroCommessa;

	private String oreLavorate;

	private String oreViaggio;

	//bi-directional many-to-one association to DettaglioOreGiornaliere
	@ManyToOne
	@JoinColumn(name="ID_DETTAGLIO_ORE")
	private DettaglioOreGiornaliere dettaglioOreGiornaliere;

    public DettaglioIntervalliCommesse() {
    }

	public int getIdDettaglioIntervalliCommesse() {
		return this.idDettaglioIntervalliCommesse;
	}

	public void setIdDettaglioIntervalliCommesse(int idDettaglioIntervalliCommesse) {
		this.idDettaglioIntervalliCommesse = idDettaglioIntervalliCommesse;
	}

	public String getEstensioneCommessa() {
		return this.estensioneCommessa;
	}

	public void setEstensioneCommessa(String estensioneCommessa) {
		this.estensioneCommessa = estensioneCommessa;
	}

	public int getIdAttivita() {
		return this.idAttivita;
	}

	public void setIdAttivita(int idAttivita) {
		this.idAttivita = idAttivita;
	}

	public String getNumeroCommessa() {
		return this.numeroCommessa;
	}

	public void setNumeroCommessa(String numeroCommessa) {
		this.numeroCommessa = numeroCommessa;
	}

	public String getOreLavorate() {
		return this.oreLavorate;
	}

	public void setOreLavorate(String oreLavorate) {
		this.oreLavorate = oreLavorate;
	}

	public String getOreViaggio() {
		return this.oreViaggio;
	}

	public void setOreViaggio(String oreViaggio) {
		this.oreViaggio = oreViaggio;
	}

	public DettaglioOreGiornaliere getDettaglioOreGiornaliere() {
		return this.dettaglioOreGiornaliere;
	}

	public void setDettaglioOreGiornaliere(DettaglioOreGiornaliere dettaglioOreGiornaliere) {
		this.dettaglioOreGiornaliere = dettaglioOreGiornaliere;
	}
	
}