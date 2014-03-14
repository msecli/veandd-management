package gestione.pack.shared;

import java.util.Date;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author VE&D
 *
 */
@Entity
@Table(name="rtv")
public class Rtv  extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idRtv;

	private String numeroRtv;
	
	private String codiceFornitore;
	
	@Temporal( TemporalType.DATE)
	private Date dataOrdine;
	
	@Temporal( TemporalType.DATE)
	private Date dataEmissione;
	
	private String attivita;
	
	private String statoLavori;
	
	private String cdcRichiedente;
	
	private String commessaCliente;
	
	private String ente;
	
	private float importo;

	private String meseRiferimento;

	//bi-directional many-to-one association to Ordine
    @ManyToOne
	@JoinColumn(name="idOrdine")
	private Ordine ordine;

    public Rtv() {
    }

	public int getIdRtv() {
		return this.idRtv;
	}

	public void setIdRtv(int idRtv) {
		this.idRtv = idRtv;
	}

	public float getImporto() {
		return this.importo;
	}

	public void setImporto(float importo) {
		this.importo = importo;
	}

	public String getMeseRiferimento() {
		return this.meseRiferimento;
	}

	public void setMeseRiferimento(String meseRiferimento) {
		this.meseRiferimento = meseRiferimento;
	}

	public Ordine getOrdine() {
		return this.ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	/**
	 * @return the numeroRtv
	 */
	public String getNumeroRtv() {
		return numeroRtv;
	}

	/**
	 * @param numeroRtv the numeroRtv to set
	 */
	public void setNumeroRtv(String numeroRtv) {
		this.numeroRtv = numeroRtv;
	}

	/**
	 * @return the codiceFornitore
	 */
	public String getCodiceFornitore() {
		return codiceFornitore;
	}

	/**
	 * @param codiceFornitore the codiceFornitore to set
	 */
	public void setCodiceFornitore(String codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}

	/**
	 * @return the dataOrdine
	 */
	public Date getDataOrdine() {
		return dataOrdine;
	}

	/**
	 * @param dataOrdine the dataOrdine to set
	 */
	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	/**
	 * @return the dataEmissione
	 */
	public Date getDataEmissione() {
		return dataEmissione;
	}

	/**
	 * @param dataEmissione the dataEmissione to set
	 */
	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	/**
	 * @return the attivita
	 */
	public String getAttivita() {
		return attivita;
	}

	/**
	 * @param attivita the attivita to set
	 */
	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	/**
	 * @return the statoLavori
	 */
	public String getStatoLavori() {
		return statoLavori;
	}

	/**
	 * @param statoLavori the statoLavori to set
	 */
	public void setStatoLavori(String statoLavori) {
		this.statoLavori = statoLavori;
	}

	/**
	 * @return the cdcCliente
	 */
	public String getCdcCliente() {
		return cdcRichiedente;
	}

	/**
	 * @param cdcCliente the cdcCliente to set
	 */
	public void setCdcCliente(String cdcCliente) {
		this.cdcRichiedente = cdcCliente;
	}

	/**
	 * @return the commessaCliente
	 */
	public String getCommessaCliente() {
		return commessaCliente;
	}

	/**
	 * @param commessaCliente the commessaCliente to set
	 */
	public void setCommessaCliente(String commessaCliente) {
		this.commessaCliente = commessaCliente;
	}

	public String getEnte() {
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}
	
	
	
}
