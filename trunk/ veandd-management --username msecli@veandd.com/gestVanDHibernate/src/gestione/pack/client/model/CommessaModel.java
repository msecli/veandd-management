package gestione.pack.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CommessaModel extends BaseModel  implements IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommessaModel(){}
	
	
	public CommessaModel(int idCommessa, String numeroCommessa, String estensione, String descrizione){
		set("idCommessa",idCommessa);
		set("numeroCommessa",numeroCommessa);
		set("estensione",estensione);
		set("descrizione",descrizione);	
		set("commessa", numeroCommessa+"."+estensione+" ("+descrizione+")");
	}
	
	public CommessaModel( int idCommessa, String numeroCommessa, String rdo, String numeroOfferta, String numeroOrdine, String estensione, String tipoCommessa,
			String ragioneSociale, String pm, String statoCommessa, String oreLavoro, String oreLavoroResidue, String tariffaSal, String salAttuale, String pclAttuale, 
			String annoProtocollo, String dataInizio, String dataChiusura, String descrizione, String note, String numRisorse ){
			
		set("idCommessa",idCommessa);
		set("numeroCommessa",numeroCommessa);
		set("rdo",rdo);
		set("numeroOfferta",numeroOfferta);
		set("numeroOrdine",numeroOrdine);
		set("estensione",estensione);
		set("tipoCommessa", tipoCommessa);
		set("ragioneSociale",ragioneSociale); //derivato nella converterToModel
		set("pm",pm);
		set("statoCommessa",statoCommessa);
		set("annoProtocollo",annoProtocollo);
		set("dataInizio",dataInizio); //data Attivazione eventualmente ricavata automaticamente come data "odierna"
		set("dataChiusura", dataChiusura); //data eventualmente ricavata automaticamente al momento della chiusura (click btnChiusura) 
		set("descrizione",descrizione);		
		set("note",note);
		set("oreLavoro", oreLavoro);
		set("oreLavoroResidue", oreLavoroResidue);
		set("tariffaSal", tariffaSal);
		set("pclAttuale", pclAttuale);
		set("salAttuale", salAttuale);
		set("commessa", numeroCommessa+"."+estensione);
			
		//set("numRisorse", numRisorse); //da usare quando sono presenti le associazioni con il personale per ricavarne il numero		
	}
	
	public int getIdCommessa(){
		return get("idCommessa");
	}
	
	public String getNumeroCommessa() {
		return get("numeroCommessa");
	}
	
	public String getEstensione() {
		return get("estensione");
	}
	
	public String getPm() {
		return get("pm");
	}
	
	public String getNumeroOrdine(){
		return get("numeroOrdine");
	}
		
	public String getDataInizio() {
		return get("dataInizio");
	}
	
	public String getDataFine() {
		return get("dataFine");
	}
	
	public String getTipoCommessa() {
		return get("tipoCommessa");
	}
	
	public String getStatoCommessa() {
		return get("statoCommessa");
	}
	
	public String getRagioneSociale() {
		return get("ragioneSociale");
	}
	
	public String getTariffaSal() {
		return get("tariffaSal");
	}
	
	public String getSalIniziale() {
		return get("salAttuale");
	}
	
	public String getPclIniziale() {
		return get("pclAttuale");
	}
	
	public String getOreLavoro() {
		return get("oreLavoro");
	}
	
	public String getOreLavoroResidue() {
		return get("oreLavoroResidue");
	}
	
	public String getDescrizione() {
		return get("descrizione");
	}
	
	public String getNote() {
		return get("note");
	}
	
}
