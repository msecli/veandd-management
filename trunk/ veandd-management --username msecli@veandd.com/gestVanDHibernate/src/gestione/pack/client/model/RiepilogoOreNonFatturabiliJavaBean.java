package gestione.pack.client.model;

public class RiepilogoOreNonFatturabiliJavaBean {

	private String  sede;
	private String gruppolavoro;
	private String attivita;
	private String risorsa;
	private Float m1;
	private Float m2;
	private Float m3;
	private Float m4;
	private Float m5;
	private Float m6;
	private Float m7;
	private Float m8;
	private Float m9;
	private Float m10;
	private Float m11;
	private Float m12;
	private String costoOrario;
	private Float totOre;
	private Float costoEffettivo;
	
	public RiepilogoOreNonFatturabiliJavaBean (String sede, String gruppoLavoro, String attivita, String risorsa, Float m1,
			Float m2, Float m3, Float m4, Float m5, Float m6, Float m7, Float m8, Float m9, Float m10, Float m11, Float m12,
			String costoOrario, Float totOre, Float costoEffettivo){
		
		this.sede= sede;
		this.gruppolavoro= gruppoLavoro;
		this.attivita=attivita;
		this.risorsa= risorsa;
		this.m1=m1;
		this.m2=m2;
		this.m3=m3;
		this.m4=m4;
		this.m5=m5;
		this.m6=m6;
		this.m7=m7;
		this.m8=m8;
		this.m9=m9;
		this.m10=m10;
		this.m11=m11;
		this.m12=m12;
		this.costoOrario=costoOrario;
		this.totOre= totOre;
		this.costoEffettivo=costoEffettivo;
		
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getGruppolavoro() {
		return gruppolavoro;
	}

	public void setGruppolavoro(String gruppolavoro) {
		this.gruppolavoro = gruppolavoro;
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public String getRisorsa() {
		return risorsa;
	}

	public void setRisorsa(String risorsa) {
		this.risorsa = risorsa;
	}

	public Float getM1() {
		return m1;
	}

	public void setM1(Float m1) {
		this.m1 = m1;
	}

	public Float getM2() {
		return m2;
	}

	public void setM2(Float m2) {
		this.m2 = m2;
	}

	public Float getM3() {
		return m3;
	}

	public void setM3(Float m3) {
		this.m3 = m3;
	}

	public Float getM4() {
		return m4;
	}

	public void setM4(Float m4) {
		this.m4 = m4;
	}

	public Float getM5() {
		return m5;
	}

	public void setM5(Float m5) {
		this.m5 = m5;
	}

	public Float getM6() {
		return m6;
	}

	public void setM6(Float m6) {
		this.m6 = m6;
	}

	public Float getM7() {
		return m7;
	}

	public void setM7(Float m7) {
		this.m7 = m7;
	}

	public Float getM8() {
		return m8;
	}

	public void setM8(Float m8) {
		this.m8 = m8;
	}

	public Float getM9() {
		return m9;
	}

	public void setM9(Float m9) {
		this.m9 = m9;
	}

	public Float getM10() {
		return m10;
	}

	public void setM10(Float m10) {
		this.m10 = m10;
	}

	public Float getM11() {
		return m11;
	}

	public void setM11(Float m11) {
		this.m11 = m11;
	}

	public Float getM12() {
		return m12;
	}

	public void setM12(Float m12) {
		this.m12 = m12;
	}

	public String getCostoOrario() {
		return costoOrario;
	}

	public void setCostoOrario(String costoOrario) {
		this.costoOrario = costoOrario;
	}

	public Float getTotOre() {
		return totOre;
	}

	public void setTotOre(Float totOre) {
		this.totOre = totOre;
	}

	public Float getCostoEffettivo() {
		return costoEffettivo;
	}

	public void setCostoEffettivo(Float costoEffettivo) {
		this.costoEffettivo = costoEffettivo;
	}
	
	
}
