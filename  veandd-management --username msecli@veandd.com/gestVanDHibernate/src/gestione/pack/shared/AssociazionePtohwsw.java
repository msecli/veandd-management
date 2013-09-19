package gestione.pack.shared;


import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


@Entity
@Table(name="associazione_ptohwsw")
public class AssociazionePtohwsw extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idAssociazionePtoHwSw;

	//bi-directional many-to-one association to CostiHwSw
    @ManyToOne
	@JoinColumn(name="idCostiHwSw")
	private CostiHwSw costiHwSw;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

    public AssociazionePtohwsw() {
    }

	public int getIdAssociazionePtoHwSw() {
		return this.idAssociazionePtoHwSw;
	}

	public void setIdAssociazionePtoHwSw(int idAssociazionePtoHwSw) {
		this.idAssociazionePtoHwSw = idAssociazionePtoHwSw;
	}

	public CostiHwSw getCostiHwSw() {
		return this.costiHwSw;
	}

	public void setCostiHwSw(CostiHwSw costiHwSw) {
		this.costiHwSw = costiHwSw;
	}
	
	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}
	
}
