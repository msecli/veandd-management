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
	
	private String oreStraordinario;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numeroCommessa == null) ? 0 : numeroCommessa.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DettaglioIntervalliCommesse other = (DettaglioIntervalliCommesse) obj;
		
		if(numeroCommessa!=null && estensioneCommessa!=null)
			if(numeroCommessa.equals(other.numeroCommessa))
				if(estensioneCommessa.equals(other.estensioneCommessa))
					return true;
				else
					return false;
			else
				return false;
		else
			return false;
		
		/*
		if (numeroCommessa == null) {
			if (other.numeroCommessa != null)
				return false;
		} else if (!numeroCommessa.equals(other.numeroCommessa))
			return false;
		
		return true;*/
	}

	public String getOreStraordinario() {
		return oreStraordinario;
	}

	public void setOreStraordinario(String oreStraordinario) {
		this.oreStraordinario = oreStraordinario;
	}

	
	
}