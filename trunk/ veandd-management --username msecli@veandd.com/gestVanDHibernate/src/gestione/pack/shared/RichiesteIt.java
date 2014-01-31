package gestione.pack.shared;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="richieste_it")
public class RichiesteIt extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idRichiesta;

    @Temporal( TemporalType.TIMESTAMP)
	private Date dataEvasioneRichiesta;

    @Temporal( TemporalType.TIMESTAMP)
	private Date dataRichiesta;

	private String guasto;

	private String stato;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

	//bi-directional many-to-one association to AnagraficaHardware
    @ManyToOne
	@JoinColumn(name="idHardware")
	private AnagraficaHardware anagraficaHardware;

	/**
	 * @return the idRichiesta
	 */
	public int getIdRichiesta() {
		return idRichiesta;
	}

	/**
	 * @param idRichiesta the idRichiesta to set
	 */
	public void setIdRichiesta(int idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	/**
	 * @return the dataEvasioneRichiesta
	 */
	public Date getDataEvasioneRichiesta() {
		return dataEvasioneRichiesta;
	}

	/**
	 * @param dataEvasioneRichiesta the dataEvasioneRichiesta to set
	 */
	public void setDataEvasioneRichiesta(Date dataEvasioneRichiesta) {
		this.dataEvasioneRichiesta = dataEvasioneRichiesta;
	}

	/**
	 * @return the dataRichiesta
	 */
	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	/**
	 * @param dataRichiesta the dataRichiesta to set
	 */
	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	/**
	 * @return the guasto
	 */
	public String getGuasto() {
		return guasto;
	}

	/**
	 * @param guasto the guasto to set
	 */
	public void setGuasto(String guasto) {
		this.guasto = guasto;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the personale
	 */
	public Personale getPersonale() {
		return personale;
	}

	/**
	 * @param personale the personale to set
	 */
	public void setPersonale(Personale personale) {
		this.personale = personale;
	}

	/**
	 * @return the anagraficaHardware
	 */
	public AnagraficaHardware getAnagraficaHardware() {
		return anagraficaHardware;
	}

	/**
	 * @param anagraficaHardware the anagraficaHardware to set
	 */
	public void setAnagraficaHardware(AnagraficaHardware anagraficaHardware) {
		this.anagraficaHardware = anagraficaHardware;
	}

    
    
    
}
