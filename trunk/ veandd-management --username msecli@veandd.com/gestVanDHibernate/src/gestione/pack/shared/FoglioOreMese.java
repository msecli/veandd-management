package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;


/**
 * The persistent class for the foglio_ore_mese database table.
 * 
 */
@Entity
@Table(name="foglio_ore_mese")
public class FoglioOreMese extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_FOGLIO_ORE")
	private int idFoglioOre;

	@Column(name="meseRiferimento")
   	private String meseRiferimento;

	@Column(name="statoRevisioneMese")
	private String statoRevisioneMese;

	//bi-directional many-to-one association to DettaglioOreGiornaliere
	@OneToMany(mappedBy="foglioOreMese", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<DettaglioOreGiornaliere> dettaglioOreGiornalieres;

	//bi-directional many-to-one association to Personale
	@ManyToOne
	@JoinColumn(name="ID_PERSONALE")	
	private Personale personale;

    public FoglioOreMese() {
    }

	public int getIdFoglioOre() {
		return this.idFoglioOre;
	}

	public void setIdFoglioOre(int idFoglioOre) {
		this.idFoglioOre = idFoglioOre;
	}

	public String getMeseRiferimento() {
		return this.meseRiferimento;
	}

	public void setMeseRiferimento(String meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}

	public String getStatoRevisioneMese() {
		return this.statoRevisioneMese;
	}

	public void setStatoRevisioneMese(String statoRevisioneMese) {
		this.statoRevisioneMese = statoRevisioneMese;
	}

	public Set<DettaglioOreGiornaliere> getDettaglioOreGiornalieres() {
		return this.dettaglioOreGiornalieres;
	}

	public void setDettaglioOreGiornalieres(Set<DettaglioOreGiornaliere> dettaglioOreGiornalieres) {
		this.dettaglioOreGiornalieres = dettaglioOreGiornalieres;
	}
	
	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((meseRiferimento == null) ? 0 : meseRiferimento.hashCode());
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
		FoglioOreMese other = (FoglioOreMese) obj;
		if (meseRiferimento == null) {
			if (other.meseRiferimento != null)
				return false;
		} else if (!meseRiferimento.equals(other.meseRiferimento))
			return false;
		return true;
	}
	
	
}