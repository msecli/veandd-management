package gestione.pack.shared;

import java.util.Set;

import javax.persistence.*;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The persistent class for the personale database table.
 * 
 */
@Entity
@Table(name="personale")
public class Personale  extends LightEntity implements IsSerializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONALE")
	private int ID_PERSONALE;

	private String cognome;

	private String costoOrario;

	private String costoStruttura;

	private String gruppoLavoro;

	private String nome;

	private String numeroBadge;

	private String password;

	private String ruolo;

	private String tipologiaLavoratore;

	private String tipologiaOrario;

	private String username;
	
	private String sede;
	
	private String sedeOperativa;
	
	private String oreDirette;
	
	private String oreIndirette;
	
	@Column(name="FERIE")
	private String oreFerie;
	
	@Column(name="PERMESSI")
	private String orePermessi;
	
	@Column(name="EX_FEST")
	private String oreExFest;
	
	private String oreRecupero;
	
	private byte[] curriculum;
	
	private String nomeFile;
	
	private String statoRapporto; //attivo, cessato
	
	//bi-directional many-to-one association to AssociazionePtoa
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<AssociazionePtoA> associazionePtoas;
	
	//bi-directional many-to-one association to FoglioOreMese
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<FoglioOreMese> foglioOreMeses;
	
	//bi-directional many-to-one association to AssociazionePtohwsw
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<AssociazionePtohwsw> associazionePtohwsws;

	//bi-directional many-to-one association to CostoAzienda
	//in realtà gestito come una onetoone
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<CostoAzienda> costoAziendas;
	
	//bi-directional many-to-one association to CostingRisorsa
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<CostingRisorsa> costingRisorsas;
	
	//bi-directional many-to-one association to RichiesteIt
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<RichiesteIt> richiesteIts;
	
	//bi-directional many-to-one association to AssociazionePtohwsw
	@OneToMany(mappedBy="personale", fetch=FetchType.LAZY)
	@Cascade({CascadeType.SAVE_UPDATE})
	private Set<AssociazionePtoHw> associazionePtoHw;

    public Personale() {
    }

	public int getId_PERSONALE() {
		return this.ID_PERSONALE;
	}

	public void setId_PERSONALE(int id_personale) {
		this.ID_PERSONALE = id_personale;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCostoOrario() {
		return this.costoOrario;
	}

	public void setCostoOrario(String costoOrario) {
		this.costoOrario = costoOrario;
	}

	public String getCostoStruttura() {
		return this.costoStruttura;
	}

	public void setCostoStruttura(String costoStruttura) {
		this.costoStruttura = costoStruttura;
	}

	public String getGruppoLavoro() {
		return this.gruppoLavoro;
	}

	public void setGruppoLavoro(String gruppoLavoro) {
		this.gruppoLavoro = gruppoLavoro;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumeroBadge() {
		return this.numeroBadge;
	}

	public void setNumeroBadge(String numeroBadge) {
		this.numeroBadge = numeroBadge;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRuolo() {
		return this.ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getTipologiaLavoratore() {
		return this.tipologiaLavoratore;
	}

	public void setTipologiaLavoratore(String tipologiaLavoratore) {
		this.tipologiaLavoratore = tipologiaLavoratore;
	}

	public String getTipologiaOrario() {
		return this.tipologiaOrario;
	}

	public void setTipologiaOrario(String tipologiaOrario) {
		this.tipologiaOrario = tipologiaOrario;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getOreDirette() {
		return oreDirette;
	}

	public void setOreDirette(String oreDirette) {
		this.oreDirette = oreDirette;
	}

	public String getOreIndirette() {
		return oreIndirette;
	}

	public void setOreIndirette(String oreIndirette) {
		this.oreIndirette = oreIndirette;
	}

	public String getOreFerie() {
		return oreFerie;
	}

	public void setOreFerie(String oreFerie) {
		this.oreFerie = oreFerie;
	}

	public String getOrePermessi() {
		return orePermessi;
	}

	public void setOrePermessi(String orePermessi) {
		this.orePermessi = orePermessi;
	}

	public String getOreExFest() {
		return oreExFest;
	}

	public void setOreExFest(String oreExFest) {
		this.oreExFest = oreExFest;
	}

	public String getOreRecupero() {
		return oreRecupero;
	}

	public void setOreRecupero(String oreRecupero) {
		this.oreRecupero = oreRecupero;
	}

	public byte[] getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(byte[] curriculum) {
		this.curriculum = curriculum;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Set<AssociazionePtoA> getAssociazionePtoas() {
		return associazionePtoas;
	}

	public void setAssociazionePtoas(Set<AssociazionePtoA> associazionePtoas) {
		this.associazionePtoas = associazionePtoas;
	}

	public Set<FoglioOreMese> getFoglioOreMeses() {
		return foglioOreMeses;
	}

	public void setFoglioOreMeses(Set<FoglioOreMese> foglioOreMeses) {
		this.foglioOreMeses = foglioOreMeses;
	}

	public String getSedeOperativa() {
		return sedeOperativa;
	}

	public void setSedeOperativa(String sedeOperativa) {
		this.sedeOperativa = sedeOperativa;
	}

	public Set<AssociazionePtohwsw> getAssociazionePtohwsws() {
		return associazionePtohwsws;
	}

	public void setAssociazionePtohwsws(Set<AssociazionePtohwsw> associazionePtohwsws) {
		this.associazionePtohwsws = associazionePtohwsws;
	}

	public Set<CostoAzienda> getCostoAziendas() {
		return costoAziendas;
	}

	public void setCostoAziendas(Set<CostoAzienda> costoAziendas) {
		this.costoAziendas = costoAziendas;
	}

	public Set<CostingRisorsa> getCostingRisorsas() {
		return costingRisorsas;
	}

	public void setCostingRisorsas(Set<CostingRisorsa> costingRisorsas) {
		this.costingRisorsas = costingRisorsas;
	}

	public Set<RichiesteIt> getRichiesteIts() {
		return richiesteIts;
	}

	public void setRichiesteIts(Set<RichiesteIt> richiesteIts) {
		this.richiesteIts = richiesteIts;
	}

	public Set<AssociazionePtoHw> getAssociazionePtoHw() {
		return associazionePtoHw;
	}

	public void setAssociazionePtoHw(Set<AssociazionePtoHw> associazionePtoHw) {
		this.associazionePtoHw = associazionePtoHw;
	}

	public String getStatoRapporto() {
		return statoRapporto;
	}

	public void setStatoRapporto(String statoRapporto) {
		this.statoRapporto = statoRapporto;
	}

	
	
	
}