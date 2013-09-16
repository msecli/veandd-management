package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RiepilogoOreNonFatturabiliModel extends BaseModel implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	public RiepilogoOreNonFatturabiliModel(){}
	
	public RiepilogoOreNonFatturabiliModel (String sede, String pm, String attivita, String risorsa, Float m1,
			Float m2, Float m3, Float m4, Float m5, Float m6, Float m7, Float m8, Float m9, Float m10, Float m11, Float m12,
			String costoOrario, Float totOre, Float costoEffettivo){
		
		set("sede", sede);
		set("gruppolavoro", pm);
		set("attivita", attivita);
		set("risorsa", risorsa);
		set("m1", m1);
		set("m2", m2);
		set("m3", m3);
		set("m4", m4);
		set("m5", m5);
		set("m6", m6);
		set("m7", m7);
		set("m8", m8);
		set("m9", m9);
		set("m10", m10);
		set("m11", m11);
		set("m12", m12);
		set("costoOrario", costoOrario);
		set("totOre", totOre);
		set("costoEffettivo",costoEffettivo);
		
	}

	public String getSede(){
		return get("sede");
	}
	public String getGruppoLavoro(){
		return get("gruppolavoro");
	}
	public String getAttivita(){
		return get("attivita");
	}
	public String getRisorsa(){
		return get("risorsa");
	}
	public Float getM1(){
		return get("m1");
	}
	public Float getM2(){
		return get("m2");
	}
	public Float getM3(){
		return get("m3");
	}
	public Float getM4(){
		return get("m4");
	}
	public Float getM5(){
		return get("m5");
	}
	public Float getM6(){
		return get("m6");
	}
	public Float getM7(){
		return get("m7");
	}
	public Float getM8(){
		return get("m8");
	}
	public Float getM9(){
		return get("m9");
	}
	public Float getM10(){
		return get("m10");
	}
	public Float getM11(){
		return get("m11");
	}
	public Float getM12(){
		return get("m12");
	}
	public String getCostoOrario(){
		return get("costoOrario");
	}
	public Float getCostoEffettivo(){
		return get("costoEffettivo");
	}
	public Float getTotaleOre(){
		return get("totOre");
	}
	
}
