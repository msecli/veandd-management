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
@Table(name="bilancio")
public class Bilancio extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int idBilancio;

	private String anno;

	//bi-directional many-to-one association to BilancioPm
	
	@OneToMany(mappedBy="bilancio", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
	private Set<BilancioPm> bilancioPms;
	
    public Bilancio() {
    }

	public int getIdBilancio() {
		return this.idBilancio;
	}

	public void setIdBilancio(int idBilancio) {
		this.idBilancio = idBilancio;
	}

	public String getAnno() {
		return this.anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public Set<BilancioPm> getBilancioPms() {
		return this.bilancioPms;
	}

	public void setBilancioPms(Set<BilancioPm> bilancioPms) {
		this.bilancioPms = bilancioPms;
	}

}
