package gestione.pack.shared;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="costi_hw_sw")
public class CostiHwSw extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idCostoHwSw;

	private String costo;

	private String descrizione;

	private String tipologia;

	//bi-directional many-to-one association to AssociazionePtohwsw
	@OneToMany(mappedBy="costiHwSw", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<AssociazionePtohwsw> associazionePtohwsws;

    public CostiHwSw() {
    }

	public int getIdCostoHwSw() {
		return this.idCostoHwSw;
	}

	public void setIdCostoHwSw(int idCostoHwSw) {
		this.idCostoHwSw = idCostoHwSw;
	}

	public String getCosto() {
		return this.costo;
	}

	public void setCosto(String costo) {
		this.costo = costo;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipologia() {
		return this.tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public Set<AssociazionePtohwsw> getAssociazionePtohwsws() {
		return this.associazionePtohwsws;
	}

	public void setAssociazionePtohwsws(Set<AssociazionePtohwsw> associazionePtohwsws) {
		this.associazionePtohwsws = associazionePtohwsws;
	}
	
}