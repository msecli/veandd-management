package gestione.pack.client.utility;

import gestione.pack.client.AdministrationService;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class DatiComboBox {
	
	public static List<String> getRuoli(){
		
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("AMM");
		listaR.add("AU");
		listaR.add("DIR");
		listaR.add("PM");
		listaR.add("UG");
		listaR.add("UA");
		listaR.add("DIP");
		
		return listaR;
	}
	
	public static List<String> getTipoOrario(){
		
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("8");
		listaR.add("7");
		listaR.add("6");
		listaR.add("4");
		listaR.add("A");
		
		return listaR;
	}

	public static List<String> getTipoLavoratore(){
	
		List <String> listaR=new ArrayList<String>();
	
		listaR.add("D");
		listaR.add("C");
		listaR.add("S");
		listaR.add("I");
		listaR.add("AU");
		
		return listaR;
	}


	public static List<String> getSede(){
	
		List <String> listaR=new ArrayList<String>();
	
		listaR.add("F");
		listaR.add("S");
		
		return listaR;
	}
	
	
	public static List<String> getGruppoLavoro(){
		
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("Autotelaio");
		listaR.add("Trasmissioni");
		listaR.add("Motori");
		listaR.add("Affidabilita");
		listaR.add("Trasm. E Dinamica Veicolo");
		listaR.add("Mov.Terra / Veic.Ind.Agr.");
		listaR.add("Defence");
		listaR.add("Defence/Trasm E Din.Veic.");
		listaR.add("Indiretti");
			
		return listaR;
		}

	
	public static List<String> getRdos() {
		final List<String> lista= new ArrayList<String>();
		
		try {		
			AdministrationService.Util.getInstance().getAllNumeroRdo(new AsyncCallback<List<String>>() {			
				@Override
				public void onSuccess(List<String> result) {
					lista.addAll(result);			
				}			
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione;");
					caught.printStackTrace();		
				}					
			});
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
		return lista;
	}
	

	public static List<String> getStatoCommessa() {
		
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("Aperta");
		//listaR.add("Prevista");
		//listaR.add("Attiva");
		listaR.add("Conclusa");
					
		return listaR;
	}
	
	
	public static List<String> getTipoCommessa() {
		
		List <String> listaR=new ArrayList<String>();	
		listaR.add("c");
		listaR.add("e");							
		return listaR;
	}
	
	
	public static List<String> getGiustificativi() {
		
		List <String> listaR=new ArrayList<String>();
		
		//listaR.add("01.Permesso(ROL)");
		//listaR.add("02.Ferie");
		//listaR.add("03.Straordinario");
		//listaR.add("04.Ore a Recupero");
		listaR.add("05.Permesso non Retrib.");
		listaR.add("06.Malattia");
		listaR.add("07.Infortunio");
		listaR.add("08.Maternita' Obblig.");
		listaR.add("09.Maternita' Facolt.");
		listaR.add("10.Aspettativa");
		listaR.add("11.Permesso Elettor.");
		listaR.add("12.Permesso Sindac.");
		listaR.add("13.Donazione Sangue");
		listaR.add("14.Congedo Matrim.");
		listaR.add("15.Allattamento");
		listaR.add("16.Presente");
		listaR.add("17.Assente");
		//listaR.add("17.Recup.Straord.Mese Preced.");
		listaR.add("18.Sciopero");
		listaR.add("19.Assenza neo Assunti");
		listaR.add("20.Assenza neo Licenz.");
		listaR.add("21.Assemblea");
		listaR.add("22.Permesso Lutto");
		listaR.add("23.Abbuono");
		listaR.add("24.Cassa Integrazione");
		listaR.add("25.Permesso Legge 104");
		listaR.add("26.Assenza Studio");
		listaR.add("27.Ore Viaggio");
							
		return listaR;
	}
	

	public static List<String> getMese() {
		
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("Gennaio");
		listaR.add("Febbraio");
		listaR.add("Marzo");
		listaR.add("Aprile");
		listaR.add("Maggio");
		listaR.add("Giugno");
		listaR.add("Luglio");
		listaR.add("Agosto");
		listaR.add("Settembre");
		listaR.add("Ottobre");
		listaR.add("Novembre");
		listaR.add("Dicembre");
		
		return listaR;
	}

	public static List<String> getAnno() {
		List <String> listaR=new ArrayList<String>();
		
		listaR.add("2010");
		listaR.add("2011");
		listaR.add("2012");
		listaR.add("2013");
		listaR.add("2014");
		listaR.add("2015");
		listaR.add("2016");
		listaR.add("2017");
		listaR.add("2018");
		listaR.add("2019");
		listaR.add("2020");
		
		return listaR;
	}	
}
