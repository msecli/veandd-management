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

	private String costiOneri;

	private String costoAnnuo;

	private String costoStruttura;
	
	private String tipoOrario;

	private String oreCig;

	private String orePianificate;

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
		return this.costiOneri;
	}

	public void setCostiOneri(String costiOneri) {
		this.costiOneri = costiOneri;
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

	public String getOreCig() {
		return this.oreCig;
	}

	public void setOreCig(String oreCig) {
		this.oreCig = oreCig;
	}

	public String getOrePianificate() {
		return this.orePianificate;
	}

	public void setOrePianificate(String orePianificate) {
		this.orePianificate = orePianificate;
	}

	public Personale getPersonale() {
		return this.personale;
	}

	public void setPersonale(Personale personale) {
		this.personale = personale;
	}

	public String getTipoOrario() {
		return tipoOrario;
	}

	public void setTipoOrario(String tipoOrario) {
		this.tipoOrario = tipoOrario;
	}
	
}