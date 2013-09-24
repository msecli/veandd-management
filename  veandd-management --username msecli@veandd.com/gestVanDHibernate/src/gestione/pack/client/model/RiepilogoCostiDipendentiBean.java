package gestione.pack.client.model;

public class RiepilogoCostiDipendentiBean {

	private int idDip;
	private String nome;
	private String costoAnnuo;
	private String costoOrario;
	private String gruppoLavoro;
	private String costoStruttura;
	private String costoOneri;
	private String tipoCad;
	private String tipoTc;
	private String costoCad;
	private String costoTc;
	private String tipoHardware;
	private String costoHardware;
	private String sommaCostoHwSw;
	private String costoAggiuntivo;
	private String costoTotaleRisorsa; 
	private String tipoOrario;
	private String colocation;
	private Float oreCig;
	private Float orePreviste;
	private Float orePianificate;
	private String saturazione;
	private String oreAssegnare;
	
	public RiepilogoCostiDipendentiBean(int idDip, String nome, String costoAnnuo, String costoOrario, String gruppoLavoro, String costoStruttura, 
			String costoOneri, String tipoCad, String tipoTc, String costoCad, String costoTc, String tipoHardware, String costoHardware, String sommaCostoHwSw,
			String costoAggiuntivo, String costoTotaleRisorsa, String tipoOrario, String colocation, Float oreCig, Float orePreviste, Float orePianificate,
			String saturazione, String oreAssegnare){
		
		this.idDip=idDip;
		this.nome= nome;
		this.costoAnnuo=costoAnnuo;
		this.costoOrario= costoOrario;
		this.gruppoLavoro=gruppoLavoro;
		this.costoStruttura= costoStruttura;
		this.costoOneri=costoOneri;
		this.tipoCad= tipoCad;
		this.tipoTc=tipoTc;
		this.costoCad= costoCad;
		this.costoTc=costoTc;
		this.tipoHardware= tipoHardware;
		this.costoHardware=costoHardware;
		this.sommaCostoHwSw= sommaCostoHwSw;
		this.costoAggiuntivo=costoAggiuntivo;
		this.costoTotaleRisorsa= costoTotaleRisorsa;
		this.tipoOrario=tipoOrario;
		this.colocation= colocation;
		this.oreCig=oreCig;
		this.orePreviste= orePreviste;
		this.orePianificate=orePianificate;
		this.saturazione= saturazione;
		this.oreAssegnare=oreAssegnare;
		
	}

	public int getIdDip() {
		return idDip;
	}

	public String getNome() {
		return nome;
	}

	public String getCostoAnnuo() {
		return costoAnnuo;
	}

	public String getCostoOrario() {
		return costoOrario;
	}

	public String getGruppoLavoro() {
		return gruppoLavoro;
	}

	public String getCostoStruttura() {
		return costoStruttura;
	}

	public String getCostoOneri() {
		return costoOneri;
	}

	public String getTipoCad() {
		return tipoCad;
	}

	public String getTipoTc() {
		return tipoTc;
	}

	public String getCostoCad() {
		return costoCad;
	}

	public String getCostoTc() {
		return costoTc;
	}

	public String getTipoHardware() {
		return tipoHardware;
	}

	public String getCostoHardware() {
		return costoHardware;
	}

	public String getSommaCostoHwSw() {
		return sommaCostoHwSw;
	}

	public String getCostoAggiuntivo() {
		return costoAggiuntivo;
	}

	public String getCostoTotaleRisorsa() {
		return costoTotaleRisorsa;
	}

	public String getTipoOrario() {
		return tipoOrario;
	}

	public String getColocation() {
		return colocation;
	}

	public Float getOreCig() {
		return oreCig;
	}

	public Float getOrePreviste() {
		return orePreviste;
	}

	public Float getOrePianificate() {
		return orePianificate;
	}

	public String getSaturazione() {
		return saturazione;
	}

	public String getOreAssegnare() {
		return oreAssegnare;
	}
	
	
}
