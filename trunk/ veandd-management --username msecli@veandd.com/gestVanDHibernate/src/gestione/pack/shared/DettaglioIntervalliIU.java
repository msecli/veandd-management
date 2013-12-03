package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the dettaglio_intervalli_i_u database table.
 * 
 */
@Entity
@Table(name="dettaglio_intervalli_i_u")
public class DettaglioIntervalliIU extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_DETTAGLIO_INTERVALLI_I_U")
	private int idDettaglioIntervalliIU;

	private String movimento;

	private String orario;

	private String sorgente;

	private String sostituito;

	//bi-directional many-to-one association to DettaglioOreGiornaliere
	@ManyToOne
	@JoinColumn(name="ID_DETT_ORE_GIORNO")
	private DettaglioOreGiornaliere dettaglioOreGiornaliere;

    public DettaglioIntervalliIU() {
    }

	public int getIdDettaglioIntervalliIU() {
		return this.idDettaglioIntervalliIU;
	}

	public void setIdDettaglioIntervalliIU(int idDettaglioIntervalliIU) {
		this.idDettaglioIntervalliIU = idDettaglioIntervalliIU;
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

	public String getSorgente() {
		return this.sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}

	public String getSostituito() {
		return this.sostituito;
	}

	public void setSostituito(String sostituito) {
		this.sostituito = sostituito;
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
				+ ((movimento == null) ? 0 : movimento.hashCode());
		result = prime * result + ((orario == null) ? 0 : orario.hashCode());
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
		DettaglioIntervalliIU other = (DettaglioIntervalliIU) obj;
		
		if(movimento!=null && orario!=null)
		if(movimento.equals(other.movimento))
			if(orario.equals(other.orario))
				return true;
		
		if (movimento == null) {
			if (other.movimento != null)
				return false;
		} else if (!movimento.equals(other.movimento))
			return false;	
		
		if (orario == null) {
			if (other.orario != null)
				return false;
		} else if (!orario.equals(other.orario))
			return false;
			
		return true;
	}

	
	
	
}