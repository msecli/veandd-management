package gestione.pack.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="mesi_calendario")
public class MesiCalendario extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int idMese;

	private String giorni;

	private String mese;

	private String ore;

	//bi-directional many-to-one association to Calendario
    @ManyToOne
	@JoinColumn(name="idCalendario")
	private Calendario calendario;

    public MesiCalendario() {
    }

	public int getIdMese() {
		return this.idMese;
	}

	public void setIdMese(int idMese) {
		this.idMese = idMese;
	}

	public String getGiorni() {
		return this.giorni;
	}

	public void setGiorni(String giorni) {
		this.giorni = giorni;
	}

	public String getMese() {
		return this.mese;
	}

	public void setMese(String mese) {
		this.mese = mese;
	}

	public String getOre() {
		return this.ore;
	}

	public void setOre(String ore) {
		this.ore = ore;
	}

	public Calendario getCalendario() {
		return this.calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

}
