package gestione.pack.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;


@Entity
@Table(name="associazione_ptohw")
public class AssociazionePtoHw extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idAssociazionePtoH;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

	//bi-directional many-to-one association to AnagraficaHardware
    @ManyToOne
	@JoinColumn(name="idHardware")
	private AnagraficaHardware anagraficaHardware;

    public AssociazionePtoHw() {
    }

	public int getIdAssociazionePtoH() {
		return this.idAssociazionePtoH;
	}

	public void setIdAssociazionePtoH(int idAssociazionePtoH) {
		this.idAssociazionePtoH = idAssociazionePtoH;
	}

	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}
	
	public AnagraficaHardware getAnagraficaHardware() {
		return this.anagraficaHardware;
	}

	public void setAnagraficaHardware(AnagraficaHardware anagraficaHardware) {
		this.anagraficaHardware = anagraficaHardware;
	}

}
