package gestione.pack.client.model;

public class RiepilogoAnnualeJavaBean {

	private String anno;
	private String cognome;
	private String nome;
	private Float oreOrdinarie;
	private Float oreStraordinarie;
	private Float oreRecupero;
	private Float oreViaggio;
	private Float totaleOreLavoro;
	private Float oreFerie;
	private Float orePermessoRol;
	private Float oreMutua;
	private Float oreCig;
	private Float oreLegge104;
	private Float oreMaternita;
	private Float oreTotaliGiustificativi;
	
	public RiepilogoAnnualeJavaBean(String anno,String cognome, String nome, Float oreOrdinarie2, Float oreStraOrdinarie2,Float oreRecupero2
			, Float oreViaggio2, Float oreTotali, Float oreFerie2, Float orePermessoROL2, Float oreMutua2, Float oreCassa, Float oreLegge1042, Float oreMaternita2,   Float oreTotaliGiustificativi2 ){
		
		this.anno=anno;
		this.cognome=cognome;
		this.nome=nome;
		this.oreOrdinarie=oreOrdinarie2;
		this.oreStraordinarie=oreStraOrdinarie2;
		this.oreRecupero=oreRecupero2;
		this.oreViaggio=oreViaggio2;
		this.totaleOreLavoro=oreTotali;
		this.oreFerie=oreFerie2;
		this.orePermessoRol=orePermessoROL2;
		this.oreMutua=oreMutua2;
		this.oreCig=oreCassa;
		this.oreLegge104=oreLegge1042;
		this.oreMaternita=oreMaternita2;
		this.oreTotaliGiustificativi=oreTotaliGiustificativi2;
	
	}
	
	public RiepilogoAnnualeJavaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Float getOreOrdinarie() {
		return oreOrdinarie;
	}

	public void setOreOrdinarie(Float oreOrdinarie) {
		this.oreOrdinarie = oreOrdinarie;
	}

	public Float getOreStraordinarie() {
		return oreStraordinarie;
	}

	public void setOreStraordinarie(Float oreStraordinarie) {
		this.oreStraordinarie = oreStraordinarie;
	}

	public Float getOreRecupero() {
		return oreRecupero;
	}

	public void setOreRecupero(Float oreRecupero) {
		this.oreRecupero = oreRecupero;
	}

	public Float getOreViaggio() {
		return oreViaggio;
	}

	public void setOreViaggio(Float oreViaggio) {
		this.oreViaggio = oreViaggio;
	}

	public Float getTotaleOreLavoro() {
		return totaleOreLavoro;
	}

	public void setTotaleOreLavoro(Float totaleOreLavoro) {
		this.totaleOreLavoro = totaleOreLavoro;
	}

	public Float getOreFerie() {
		return oreFerie;
	}

	public void setOreFerie(Float oreFerie) {
		this.oreFerie = oreFerie;
	}

	public Float getOrePermessoRol() {
		return orePermessoRol;
	}

	public void setOrePermessoRol(Float orePermessoRol) {
		this.orePermessoRol = orePermessoRol;
	}

	public Float getOreMutua() {
		return oreMutua;
	}

	public void setOreMutua(Float oreMutua) {
		this.oreMutua = oreMutua;
	}

	public Float getOreCig() {
		return oreCig;
	}

	public void setOreCig(Float oreCig) {
		this.oreCig = oreCig;
	}

	public Float getOreLegge104() {
		return oreLegge104;
	}

	public void setOreLegge104(Float oreLegge104) {
		this.oreLegge104 = oreLegge104;
	}

	public Float getOreMaternita() {
		return oreMaternita;
	}

	public void setOreMaternita(Float oreMaternita) {
		this.oreMaternita = oreMaternita;
	}

	public Float getOreTotaliGiustificativi() {
		return oreTotaliGiustificativi;
	}

	public void setOreTotaliGiustificativi(Float oreTotaliGiustificativi) {
		this.oreTotaliGiustificativi = oreTotaliGiustificativi;
	}

	
	
}
