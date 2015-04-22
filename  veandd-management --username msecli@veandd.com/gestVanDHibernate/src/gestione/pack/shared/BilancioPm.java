package gestione.pack.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="bilancio_pm")
public class BilancioPm extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int idBilancioPm;

	private String pm;

	private float valorePm;
	
	private String cognome;

	//bi-directional many-to-one association to Bilancio
    @ManyToOne
	@JoinColumn(name="idBilancio")
	private Bilancio bilancio;

    public BilancioPm() {
    }

	public int getIdBilancioPm() {
		return this.idBilancioPm;
	}

	public void setIdBilancioPm(int idBilancioPm) {
		this.idBilancioPm = idBilancioPm;
	}

	public String getPm() {
		return this.pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public float getValorePm() {
		return this.valorePm;
	}

	public void setValorePm(float valorePm) {
		this.valorePm = valorePm;
	}

	public Bilancio getBilancio() {
		return this.bilancio;
	}

	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

}
