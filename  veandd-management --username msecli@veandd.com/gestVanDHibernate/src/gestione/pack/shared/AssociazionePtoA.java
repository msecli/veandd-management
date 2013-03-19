package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The persistent class for the associazione_ptoa database table.
 * 
 */
@Entity
@Table(name="associazione_ptoa")
public class AssociazionePtoA extends LightEntity implements IsSerializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ASSOCIAZIONE_PA")
	private int idAssociazionePa;

   	//bi-directional many-to-one association to Attivita
	@ManyToOne
	@JoinColumn(name="ID_ATTIVITA")
	private Attivita attivita;

	//bi-directional many-to-one association to Personale
	@ManyToOne
	@JoinColumn(name="ID_PERSONALE")
	private Personale personale;

    public AssociazionePtoA() {
    }

	public int getIdAssociazionePa() {
		return this.idAssociazionePa;
	}

	public void setIdAssociazionePa(int idAssociazionePa) {
		this.idAssociazionePa = idAssociazionePa;
	}

	
	public Attivita getAttivita() {
		return this.attivita;
	}

	public void setAttivita(Attivita attivita) {
		this.attivita = attivita;
	}
	
	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}
	
}
