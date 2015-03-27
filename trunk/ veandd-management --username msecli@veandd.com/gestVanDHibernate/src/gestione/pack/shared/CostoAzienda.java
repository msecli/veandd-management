package gestione.pack.shared;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="costo_azienda")
public class CostoAzienda extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idCostoAzienda;

	private String costoOneri;

	private String costoAnnuo;
	
	private String oreAnno;
	
	private String costoStruttura;
	
	private String costoSw;
	
	private String costoTrasferta;
	
	private String costoHw;
	
	private String annoRiferimento;
	
	private String costoOrario;

	//bi-directional many-to-one association to Personale
    @ManyToOne
	@JoinColumn(name="idPersonale")
	private Personale personale;

    public CostoAzienda() {
    }

	public int getIdCostoAzienda() {
		return this.idCostoAzienda;
	}

	public void setIdCostoAzienda(int idCostoAzienda) {
		this.idCostoAzienda = idCostoAzienda;
	}

	public String getCostiOneri() {
		return this.costoOneri;
	}

	public void setCostiOneri(String costiOneri) {
		this.costoOneri = costiOneri;
	}

	public String getCostoAnnuo() {
		return this.costoAnnuo;
	}

	public void setCostoAnnuo(String costoAnnuo) {
		this.costoAnnuo = costoAnnuo;
	}

	public String getCostoStruttura() {
		return this.costoStruttura;
	}

	public void setCostoStruttura(String costoStruttura) {
		this.costoStruttura = costoStruttura;
	}

	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}


	/**
	 * @return the costoOneri
	 */
	public String getCostoOneri() {
		return costoOneri;
	}

	/**
	 * @param costoOneri the costoOneri to set
	 */
	public void setCostoOneri(String costoOneri) {
		this.costoOneri = costoOneri;
	}

	/**
	 * @return the oreAnno
	 */
	public String getOreAnno() {
		return oreAnno;
	}

	/**
	 * @param oreAnno the oreAnno to set
	 */
	public void setOreAnno(String oreAnno) {
		this.oreAnno = oreAnno;
	}

	/**
	 * @return the costoSwCadVari
	 */
	public String getCostoSw() {
		return costoSw;
	}

	/**
	 * @param costoSwCadVari the costoSwCadVari to set
	 */
	public void setCostoSw(String costoSw) {
		this.costoSw = costoSw;
	}

	/**
	 * @return the costoSwOffice
	 */
	public String getCostoTrasferta() {
		return costoTrasferta;
	}

	/**
	 * @param costoSwOffice the costoSwOffice to set
	 */
	public void setCostoTrasferta(String costoTrasferta) {
		this.costoTrasferta = costoTrasferta;
	}

	/**
	 * @return the costoHw
	 */
	public String getCostoHw() {
		return costoHw;
	}

	/**
	 * @param costoHw the costoHw to set
	 */
	public void setCostoHw(String costoHw) {
		this.costoHw = costoHw;
	}

	public String getAnnoRif() {
		return annoRiferimento;
	}

	/**
	 * @param annoRif the annoRif to set
	 */
	public void setAnnoRif(String annoRif) {
		this.annoRiferimento = annoRif;
	}

	public String getCostoOrario() {
		return costoOrario;
	}

	public void setCostoOrario(String costoOrario) {
		this.costoOrario = costoOrario;
	}
	
	
	
}