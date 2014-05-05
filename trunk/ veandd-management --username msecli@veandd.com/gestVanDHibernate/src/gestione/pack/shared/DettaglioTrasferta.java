package gestione.pack.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@Table(name="dettaglio_trasferta")
public class DettaglioTrasferta extends LightEntity implements IsSerializable{

	private static final long serialVersionUID = 1L;

	@Id
	private int ID_DETTAGLIO_TRASFERTA;
	private String numGiorni;
	private String numNotti;
	private String numViaggi;
	private String oreViaggio;
	private Float kmStradali;
	private String costoCarburante;
	private String diariaGiorno;
	private String costoAutostrada;
	private String usoVetturaPropria;
	private String costoTreno;
	private String costoAereo;
	private String costoAlbergo;
	private String costoPranzo;
	private String costoCena;
	private String costoNoleggioAuto;
	private String costoTrasportiLocali;
	private String costiVari;
	
	//bi-directional many-to-one association to FoglioOreMese
	@ManyToOne
	@JoinColumn(name="ID_COSTING_RISORSA")
	private CostingRisorsa costingRisorsa;	
	
	/**
	 * @return the iD_DETTAGLIO_TRASFERTA
	 */
	public int getID_DETTAGLIO_TRASFERTA() {
		return ID_DETTAGLIO_TRASFERTA;
	}
	/**
	 * @param iD_DETTAGLIO_TRASFERTA the iD_DETTAGLIO_TRASFERTA to set
	 */
	public void setID_DETTAGLIO_TRASFERTA(int iD_DETTAGLIO_TRASFERTA) {
		ID_DETTAGLIO_TRASFERTA = iD_DETTAGLIO_TRASFERTA;
	}
	/**
	 * @return the numGiorni
	 */
	public String getNumGiorni() {
		return numGiorni;
	}
	/**
	 * @param numGiorni the numGiorni to set
	 */
	public void setNumGiorni(String numGiorni) {
		this.numGiorni = numGiorni;
	}
	/**
	 * @return the numViaggi
	 */
	public String getNumViaggi() {
		return numViaggi;
	}
	/**
	 * @param numViaggi the numViaggi to set
	 */
	public void setNumViaggi(String numViaggi) {
		this.numViaggi = numViaggi;
	}
	/**
	 * @return the oreViaggio
	 */
	public String getOreViaggio() {
		return oreViaggio;
	}
	/**
	 * @param oreViaggio the oreViaggio to set
	 */
	public void setOreViaggio(String oreViaggio) {
		this.oreViaggio = oreViaggio;
	}
	/**
	 * @return the kmStradali
	 */
	public Float getKmStradali() {
		return kmStradali;
	}
	/**
	 * @param kmStradali the kmStradali to set
	 */
	public void setKmStradali(Float kmStradali) {
		this.kmStradali = kmStradali;
	}
	/**
	 * @return the costoCarburante
	 */
	public String getCostoCarburante() {
		return costoCarburante;
	}
	/**
	 * @param costoCarburante the costoCarburante to set
	 */
	public void setCostoCarburante(String costoCarburante) {
		this.costoCarburante = costoCarburante;
	}
	/**
	 * @return the diariaGiorno
	 */
	public String getDiariaGiorno() {
		return diariaGiorno;
	}
	/**
	 * @param diariaGiorno the diariaGiorno to set
	 */
	public void setDiariaGiorno(String diariaGiorno) {
		this.diariaGiorno = diariaGiorno;
	}
	/**
	 * @return the costoAutostrada
	 */
	public String getCostoAutostrada() {
		return costoAutostrada;
	}
	/**
	 * @param costoAutostrada the costoAutostrada to set
	 */
	public void setCostoAutostrada(String costoAutostrada) {
		this.costoAutostrada = costoAutostrada;
	}
	/**
	 * @return the usoVetturaPropria
	 */
	public String getUsoVetturaPropria() {
		return usoVetturaPropria;
	}
	/**
	 * @param usoVetturaPropria the usoVetturaPropria to set
	 */
	public void setUsoVetturaPropria(String usoVetturaPropria) {
		this.usoVetturaPropria = usoVetturaPropria;
	}
	/**
	 * @return the costoTreno
	 */
	public String getCostoTreno() {
		return costoTreno;
	}
	/**
	 * @param costoTreno the costoTreno to set
	 */
	public void setCostoTreno(String costoTreno) {
		this.costoTreno = costoTreno;
	}
	/**
	 * @return the costoAereo
	 */
	public String getCostoAereo() {
		return costoAereo;
	}
	/**
	 * @param costoAereo the costoAereo to set
	 */
	public void setCostoAereo(String costoAereo) {
		this.costoAereo = costoAereo;
	}
	/**
	 * @return the costoAlbergo
	 */
	public String getCostoAlbergo() {
		return costoAlbergo;
	}
	/**
	 * @param costoAlbergo the costoAlbergo to set
	 */
	public void setCostoAlbergo(String costoAlbergo) {
		this.costoAlbergo = costoAlbergo;
	}
	/**
	 * @return the costoPranzo
	 */
	public String getCostoPranzo() {
		return costoPranzo;
	}
	/**
	 * @param costoPranzo the costoPranzo to set
	 */
	public void setCostoPranzo(String costoPranzo) {
		this.costoPranzo = costoPranzo;
	}
	/**
	 * @return the costoCena
	 */
	public String getCostoCena() {
		return costoCena;
	}
	/**
	 * @param costoCena the costoCena to set
	 */
	public void setCostoCena(String costoCena) {
		this.costoCena = costoCena;
	}
	/**
	 * @return the costoNoleggioAuto
	 */
	public String getCostoNoleggioAuto() {
		return costoNoleggioAuto;
	}
	/**
	 * @param costoNoleggioAuto the costoNoleggioAuto to set
	 */
	public void setCostoNoleggioAuto(String costoNoleggioAuto) {
		this.costoNoleggioAuto = costoNoleggioAuto;
	}
	/**
	 * @return the costoTrasportiLocali
	 */
	public String getCostoTrasportiLocali() {
		return costoTrasportiLocali;
	}
	/**
	 * @param costoTrasportiLocali the costoTrasportiLocali to set
	 */
	public void setCostoTrasportiLocali(String costoTrasportiLocali) {
		this.costoTrasportiLocali = costoTrasportiLocali;
	}
	public CostingRisorsa getCostingRisorsa() {
		return costingRisorsa;
	}
	public void setCostingRisorsa(CostingRisorsa costingRisorsas) {
		this.costingRisorsa = costingRisorsas;
	}
	public String getCostiVari() {
		return costiVari;
	}
	public void setCostiVari(String costiVari) {
		this.costiVari = costiVari;
	}
	public String getNumNotti() {
		return numNotti;
	}
	public void setNumNotti(String numNotti) {
		this.numNotti = numNotti;
	}
	
	
	
}
