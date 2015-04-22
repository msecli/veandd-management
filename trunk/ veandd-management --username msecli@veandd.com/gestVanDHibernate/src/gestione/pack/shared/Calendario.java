package gestione.pack.shared;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="calendario")
public class Calendario extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int idCalendario;

	private String annoRiferimento;

	private int totaleGiorniLavorativi;

	private int totaleOre;

	//bi-directional many-to-one association to MesiCalendario
	@OneToMany(mappedBy="calendario", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
	private Set<MesiCalendario> mesiCalendarios;

    public Calendario() {
    }

	public int getIdCalendario() {
		return this.idCalendario;
	}

	public void setIdCalendario(int idCalendario) {
		this.idCalendario = idCalendario;
	}

	public String getAnnoRiferimento() {
		return this.annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public int getTotaleGiorniLavorativi() {
		return this.totaleGiorniLavorativi;
	}

	public void setTotaleGiorniLavorativi(int totaleGiorniLavorativi) {
		this.totaleGiorniLavorativi = totaleGiorniLavorativi;
	}

	public int getTotaleOre() {
		return this.totaleOre;
	}

	public void setTotaleOre(int totaleOre) {
		this.totaleOre = totaleOre;
	}

	public Set<MesiCalendario> getMesiCalendarios() {
		return this.mesiCalendarios;
	}

	public void setMesiCalendarios(Set<MesiCalendario> mesiCalendarios) {
		this.mesiCalendarios = mesiCalendarios;
	}
	
}
