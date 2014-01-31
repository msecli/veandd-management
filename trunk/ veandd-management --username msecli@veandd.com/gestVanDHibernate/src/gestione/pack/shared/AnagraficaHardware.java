package gestione.pack.shared;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="anagrafica_hardware")
public class AnagraficaHardware extends LightEntity implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idHardware;

	private String assistenza;

	private String codiceModello;

	private String cpu;

	private String fornitoreAssistenza;

	private String hardware;

	private String hd;

	private String ip;

	private String ipFiat;

	private String modello;

	private String nodo;

	private String note;

	private String ram;

    @Temporal( TemporalType.DATE)
	private Date scadenzaControllo;

	private String sede;

	private String serialId;

	private String serialNumber;

	private String sistemaOperativo;

	private String stato;

	private String svga;

	private String tipologia;

	private String utilizzo;

	//bi-directional many-to-one association to AssociazionePtohw
	@OneToMany(mappedBy="anagraficaHardware", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<AssociazionePtoHw> associazionePtohws;

	//bi-directional many-to-one association to RichiesteIt
	@OneToMany(mappedBy="anagraficaHardware", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<RichiesteIt> richiesteIts;

	/**
	 * @return the idHardware
	 */
	public int getIdHardware() {
		return idHardware;
	}

	/**
	 * @param idHardware the idHardware to set
	 */
	public void setIdHardware(int idHardware) {
		this.idHardware = idHardware;
	}

	/**
	 * @return the assistenza
	 */
	public String getAssistenza() {
		return assistenza;
	}

	/**
	 * @param assistenza the assistenza to set
	 */
	public void setAssistenza(String assistenza) {
		this.assistenza = assistenza;
	}

	/**
	 * @return the codiceModello
	 */
	public String getCodiceModello() {
		return codiceModello;
	}

	/**
	 * @param codiceModello the codiceModello to set
	 */
	public void setCodiceModello(String codiceModello) {
		this.codiceModello = codiceModello;
	}

	/**
	 * @return the cpu
	 */
	public String getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the fornitoreAssistenza
	 */
	public String getFornitoreAssistenza() {
		return fornitoreAssistenza;
	}

	/**
	 * @param fornitoreAssistenza the fornitoreAssistenza to set
	 */
	public void setFornitoreAssistenza(String fornitoreAssistenza) {
		this.fornitoreAssistenza = fornitoreAssistenza;
	}

	/**
	 * @return the hardware
	 */
	public String getHardware() {
		return hardware;
	}

	/**
	 * @param hardware the hardware to set
	 */
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	/**
	 * @return the hd
	 */
	public String getHd() {
		return hd;
	}

	/**
	 * @param hd the hd to set
	 */
	public void setHd(String hd) {
		this.hd = hd;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ipFiat
	 */
	public String getIpFiat() {
		return ipFiat;
	}

	/**
	 * @param ipFiat the ipFiat to set
	 */
	public void setIpFiat(String ipFiat) {
		this.ipFiat = ipFiat;
	}

	/**
	 * @return the modello
	 */
	public String getModello() {
		return modello;
	}

	/**
	 * @param modello the modello to set
	 */
	public void setModello(String modello) {
		this.modello = modello;
	}

	/**
	 * @return the nodo
	 */
	public String getNodo() {
		return nodo;
	}

	/**
	 * @param nodo the nodo to set
	 */
	public void setNodo(String nodo) {
		this.nodo = nodo;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the ram
	 */
	public String getRam() {
		return ram;
	}

	/**
	 * @param ram the ram to set
	 */
	public void setRam(String ram) {
		this.ram = ram;
	}

	/**
	 * @return the scadenzaControllo
	 */
	public Date getScadenzaControllo() {
		return scadenzaControllo;
	}

	/**
	 * @param scadenzaControllo the scadenzaControllo to set
	 */
	public void setScadenzaControllo(Date scadenzaControllo) {
		this.scadenzaControllo = scadenzaControllo;
	}

	/**
	 * @return the sede
	 */
	public String getSede() {
		return sede;
	}

	/**
	 * @param sede the sede to set
	 */
	public void setSede(String sede) {
		this.sede = sede;
	}

	/**
	 * @return the serialId
	 */
	public String getSerialId() {
		return serialId;
	}

	/**
	 * @param serialId the serialId to set
	 */
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the sistemaOperativo
	 */
	public String getSistemaOperativo() {
		return sistemaOperativo;
	}

	/**
	 * @param sistemaOperativo the sistemaOperativo to set
	 */
	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
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
	 * @return the svga
	 */
	public String getSvga() {
		return svga;
	}

	/**
	 * @param svga the svga to set
	 */
	public void setSvga(String svga) {
		this.svga = svga;
	}

	/**
	 * @return the tipologia
	 */
	public String getTipologia() {
		return tipologia;
	}

	/**
	 * @param tipologia the tipologia to set
	 */
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	/**
	 * @return the utilizzo
	 */
	public String getUtilizzo() {
		return utilizzo;
	}

	/**
	 * @param utilizzo the utilizzo to set
	 */
	public void setUtilizzo(String utilizzo) {
		this.utilizzo = utilizzo;
	}

	/**
	 * @return the associazionePtohws
	 */
	public Set<AssociazionePtoHw> getAssociazionePtohws() {
		return associazionePtohws;
	}

	/**
	 * @param associazionePtohws the associazionePtohws to set
	 */
	public void setAssociazionePtohws(Set<AssociazionePtoHw> associazionePtohws) {
		this.associazionePtohws = associazionePtohws;
	}

	/**
	 * @return the richiesteIts
	 */
	public Set<RichiesteIt> getRichiesteIts() {
		return richiesteIts;
	}

	/**
	 * @param richiesteIts the richiesteIts to set
	 */
	public void setRichiesteIts(Set<RichiesteIt> richiesteIts) {
		this.richiesteIts = richiesteIts;
	}

	
	
}
