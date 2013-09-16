package gestione.pack.server;

import gestione.pack.client.model.DatiFatturazioneMeseJavaBean;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.RiepilogoAnnualeJavaBean;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliJavaBean;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.Commessa;
import gestione.pack.shared.DatiOreMese;
import gestione.pack.shared.DatiRiepilogoMensileCommesse;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.Ordine;

import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Personale;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;


import org.hibernate.Session;
import org.hibernate.Transaction;

public class ServerUtility {
	
	public static int getGiorniMese(String mese, String anno){
		int giorni=0;
		int a=Integer.valueOf(anno);
		boolean bisestile=false;

		if((a%4) == 0 && (a % 100 != 0 || a % 400 == 0))
			bisestile=true;

		switch (mese) {
        case "Gen":  giorni = 31;
                 break;
        case "Feb":  {  	
        		if(!bisestile)
        			giorni = 28;
        		else giorni=29;
        		}
                 break;
        case "Mar":  giorni = 31;
                 break;
        case "Apr":  giorni = 30;
                 break;
        case "Mag":  giorni = 31;
                 break;
        case "Giu":  giorni = 30;
                 break;
        case "Lug":   giorni = 31;
                 break;
        case "Ago":   giorni = 31;
                 break;
        case "Set":   giorni = 30;
                 break;
        case "Ott":  giorni = 31;
                 break;
        case "Nov":  giorni = 30;
                 break;
        case "Dic":  giorni = 31;
                 break;
		}			
		return giorni;
	}
	/*
	public static String traduciMese(String month){
		String mese=new String();
		
		switch (month) {
        case "Jan":  mese="gen";
                 break;
        case "Feb":  mese="feb";
                 break;
        case "Mar":  mese="mar";
                 break;
        case "Apr":  mese="apr";
                 break;
        case "May":  mese="mag";
                 break;
        case "Jun":  mese="giu";
                 break;
        case "Jul":   mese="lug";
                 break;
        case "Aug":   mese="ago";
                 break;
        case "Sep":   mese="set";
                 break;
        case "Oct":  mese="ott";
                 break;
        case "Nov":  mese="nov";
                 break;
        case "Dec":  mese="dic";
                 break;
		}	
		return mese;
	}

	*/
	public static int calcolaOreLavorativeMese(Date giornoRiferimento, String orePreviste) {
		String data=new String();
		String mese=new String();
		String anno= new String();
		String giorno= new String();
		String daydd=new String();
		String dataGiornoMese= new String();
		String dataCompleta= new String();
		
		int giorniMese;
		int oreLavorative=0;
		
		Date date;
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM", Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
			
		giorniMese=ServerUtility.getGiorniMese(mese, anno);
					
		
		for(int i=1;i<=giorniMese;i++){
			if(i<10)
				dataGiornoMese=(anno+"-"+mese+"-"+"0"+i);
			else
				dataGiornoMese=(anno+"-"+mese+"-"+i);
			
			formatter = new SimpleDateFormat("yyyy-MMM-dd",Locale.ITALIAN);
			try {
				date = (Date)formatter.parse(dataGiornoMese);
				data=date.toString();	
				giorno=data.substring(0, 3);
				
				formatter = new SimpleDateFormat("yyyy") ; 
				anno=formatter.format(date);
				formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
				mese=formatter.format(date);
			    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
			    formatter= new SimpleDateFormat("dd");
			    daydd=formatter.format(date);
			    
			    dataCompleta=(anno+"-"+mese+"-"+daydd);
			    			
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(giorno.compareTo("Sun")!=0 && giorno.compareTo("Sat")!=0 && !isFestivo(dataCompleta)) //TODO creare uno strumento per l'aggiunta di date festive/chiusura
				oreLavorative= oreLavorative + Integer.parseInt(orePreviste);			
		}
		return oreLavorative;
	}
	
	
	public static Boolean isFestivo(String dataCompleta){
		//TODO crearne una versione dinamica
		
		if(dataCompleta.compareTo("2013-Apr-01")==0)
			return true;	
		if(dataCompleta.compareTo("2013-Apr-25")==0)
			return true;
		if(dataCompleta.compareTo("2013-Mag-01")==0)
			return true;
		if(dataCompleta.compareTo("2013-Ago-15")==0)
			return true;
		if(dataCompleta.compareTo("2013-Nov-01")==0)
			return true;	
		if(dataCompleta.compareTo("2013-Giu-24")==0)
			return true;
		return false;
	}
	
	
	public static String calcolaOreAssenza(String totOre, int orePreviste) {
		String totA= new String();
		String totB=new String();
		String oreAssenzaA= new String();
		String oreAssenzaB= new String();
		String oreAssenza= new String();
		//Calcolo le ore di assenza in base al monte ore totali lavorate
		totA=totOre.substring(0,totOre.indexOf("."));
		totB=totOre.substring(totOre.indexOf(".")+1, totOre.length());		
		
		int minA=0, minB, minOrePreviste;
		float minTot;
		
		if(Float.valueOf(totOre)>=orePreviste)
			return "0.0";
		
		minA=Integer.parseInt(totA,10)*60;
		minB=Integer.parseInt(totB,10);
		minOrePreviste=orePreviste*60;
		
		minTot=Math.abs(minA+minB-minOrePreviste);
		
		totA=String.valueOf(minTot/60);
		if(minTot%60 < 10)
			totB=String.valueOf("0"+minTot%60);
		else
			totB=String.valueOf(minTot%60);
		
		if(totA.indexOf(".")!=-1)
			oreAssenzaA=(totA.substring(0,totA.indexOf(".")));
		else oreAssenzaA=totA;
		
		if(totB.indexOf(".")!=-1)
			oreAssenzaB=(totB.substring(0,totB.indexOf(".")));
		else oreAssenzaA=totB;
		
		oreAssenza=(oreAssenzaA+"."+oreAssenzaB);
		
		return oreAssenza;
	}


	public static String aggiornaOreRecuperoMese(String totOre, String totaleOreGiorno) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;
		
			if(totOre.indexOf(".")!=-1){
				totA=totOre.substring(0,totOre.indexOf("."));
				totB=totOre.substring(totOre.indexOf(".")+1, totOre.length());	
			}else {
				totA=totOre;
				totB="0";
			}
				
			if(totaleOreGiorno.indexOf(".")!=-1){
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.indexOf("."));
				giornoB= totaleOreGiorno.substring(totaleOreGiorno.indexOf(".")+1, totaleOreGiorno.length());
			}else{
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.length());
				giornoB= "0";		
			}
				
			if(totA.substring(0,1).compareTo("-")==0)
				totB="-"+totB;
			if(giornoA.substring(0,1).compareTo("-")==0)
				giornoB="-"+giornoB;
			
			a=Integer.parseInt(totA)*60+ Integer.parseInt(giornoA)*60;
			b=Integer.parseInt(totB) + Integer.parseInt(giornoB);
			totMinuti=a+b;
				
			totA=String.valueOf(Math.abs(totMinuti/60));
			if(Math.abs(totMinuti%60) < 10)
				totB=String.valueOf("0"+Math.abs(totMinuti%60));
			else
				totB=String.valueOf(Math.abs(totMinuti%60));
			
			if(String.valueOf(totMinuti).substring(0, 1).compareTo("-")!=0)
				totale=(totA+"."+totB);
			else
				totale=("-"+totA+"."+totB);
			
		return totale;
	}


	public static String aggiornaMonteOreRecuperoTotale(String totOre, String totaleOreGiorno) {
		String totale= new String();
		//String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;
		
			if(totOre.indexOf(".")!=-1){
				totA=totOre.substring(0,totOre.indexOf("."));
				totB=totOre.substring(totOre.indexOf(".")+1, totOre.length());	
			}else {
				totA=totOre;
				totB="0";
			}
				
			if(totaleOreGiorno.indexOf(".")!=-1){
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.indexOf("."));
				giornoB= totaleOreGiorno.substring(totaleOreGiorno.indexOf(".")+1, totaleOreGiorno.length());
			}else{
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.length());
				giornoB= "0";		
			}
				
			if(totA.substring(0,1).compareTo("-")==0)
				totB="-"+totB;
			if(giornoA.substring(0,1).compareTo("-")==0)
				giornoB="-"+giornoB;
			
			a=Integer.parseInt(totA)*60+ Integer.parseInt(giornoA)*60;
			b=Integer.parseInt(totB) + Integer.parseInt(giornoB);
			totMinuti=a+b;
				
			totA=String.valueOf(Math.abs(totMinuti/60));
			if(Math.abs(totMinuti%60) < 10)
				totB=String.valueOf("0"+Math.abs(totMinuti%60));
			else
				totB=String.valueOf(Math.abs(totMinuti%60));
			
			if(String.valueOf(totMinuti).substring(0, 1).compareTo("-")!=0)
				totale=(totA+"."+totB);
			else
				totale=("-"+totA+"."+totB);
			
		return totale;
		
		
	}


	public static String aggiornaOreStraordinario(String oreStraordinarioMese, String oreStraordinarioGiorno) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=oreStraordinarioMese.substring(0, oreStraordinarioMese.indexOf("."));
		totB=oreStraordinarioMese.substring(oreStraordinarioMese.indexOf(".")+1, oreStraordinarioMese.length());
		
		if( oreStraordinarioGiorno.indexOf(".")!=-1){
			giornoA= oreStraordinarioGiorno.substring(0, oreStraordinarioGiorno.indexOf("."));
			giornoB= oreStraordinarioGiorno.substring(oreStraordinarioGiorno.indexOf(".")+1, oreStraordinarioGiorno.length());
			
		}else{
			giornoA= oreStraordinarioGiorno.substring(0, oreStraordinarioGiorno.length());
			giornoB= "00";
		}
							
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60 <10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else
					totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				totale = (String.valueOf(a)+"."+String.valueOf(b));
		
		return totale;
	}	
	

	public static String aggiornaTotGenerale(String totOre, String totaleOreGiorno) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;
		
			if(totOre.indexOf(".")!=-1){
				totA=totOre.substring(0,totOre.indexOf("."));
				totB=totOre.substring(totOre.indexOf(".")+1, totOre.length());	
			}else {
				totA=totOre;
				totB="0";
			}
				
			if(totaleOreGiorno.indexOf(".")!=-1){
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.indexOf("."));
				giornoB= totaleOreGiorno.substring(totaleOreGiorno.indexOf(".")+1, totaleOreGiorno.length());
			}else{
				giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.length());
				giornoB= "0";		
			}
				
			if(totA.substring(0,1).compareTo("-")==0)
				totB="-"+totB;
			if(giornoA.substring(0,1).compareTo("-")==0)
				giornoB="-"+giornoB;
			
			a=Integer.parseInt(totA)*60+ Integer.parseInt(giornoA)*60;
			b=Integer.parseInt(totB) + Integer.parseInt(giornoB);
			totMinuti=a+b;
				
			totA=String.valueOf(Math.abs(totMinuti/60));
			if(Math.abs(totMinuti%60) < 10)
				totB=String.valueOf("0"+Math.abs(totMinuti%60));
			else
				totB=String.valueOf(Math.abs(totMinuti%60));
			
			if(String.valueOf(totMinuti).substring(0, 1).compareTo("-")!=0)
				totale=(totA+"."+totB);
			else
				totale=("-"+totA+"."+totB);
			
			return totale;
					
	}

	
	static boolean isLastDayOfMonth(String anno, String mese, String giorno) {
		boolean isLast= false;
		
		int giorniMese=ServerUtility.getGiorniMese(mese, anno);
		
		if(giorno.compareTo(String.valueOf(giorniMese))==0)
			isLast=true;
		
		return isLast;
	}

	
	public static String getMesePrecedente(String month) {
		String mese=new String();
		String anno = new String();
		mese=month.substring(0,3);
		anno=month.substring(3, month.length());
		
		if(mese.compareTo("Gen") ==0){
			mese="Dic";
			anno=String.valueOf(Integer.parseInt(anno)-1);
			mese=mese+anno;
		}
		if(mese.compareTo("Feb") ==0){
			mese="Gen";
			mese=mese+anno;
		}
		if(mese.compareTo("Mar") ==0){
			mese="Feb";
			mese=mese+anno;
		}
		if(mese.compareTo("Apr") ==0){
			mese="Mar";
			mese=mese+anno;
		}
		if(mese.compareTo("Mag") ==0){
			mese="Apr";
			mese=mese+anno;
		}
		if(mese.compareTo("Giu") ==0){
			mese="Mag";
			mese=mese+anno;
		}
		if(mese.compareTo("Lug") ==0){
			mese="Giu";
			mese=mese+anno;
		}
		if(mese.compareTo("Ago") ==0){
			mese="Lug";
			mese=mese+anno;
		}
		if(mese.compareTo("Set") ==0){
			mese="Ago";
			mese=mese+anno;
		}
		if(mese.compareTo("Ott") ==0){
			mese="Set";
			mese=mese+anno;
		}
		if(mese.compareTo("Nov") ==0){
			mese="Ott";
			mese=mese+anno;
		}
		if(mese.compareTo("Dic") ==0){
			mese="Nov";
			mese=mese+anno;
		}
	
		return mese;
	}

	public static String getDifference(String oreResidueBudget, String oreFatturare) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;

		
			if(oreResidueBudget.indexOf(".")!=-1){
				totA=oreResidueBudget.substring(0,oreResidueBudget.indexOf("."));
				totB=oreResidueBudget.substring(oreResidueBudget.indexOf(".")+1, oreResidueBudget.length());	
			}else {
				totA=oreResidueBudget;
				totB="0";
			}
			
			if(oreFatturare.indexOf(".")!=-1){
				giornoA= oreFatturare.substring(0, oreFatturare.indexOf("."));
				giornoB= oreFatturare.substring(oreFatturare.indexOf(".")+1, oreFatturare.length());
			}else{
				giornoA= oreFatturare.substring(0, oreFatturare.length());
				giornoB= "0";
				
			}
			
			if(totA.substring(0,1).compareTo("-")==0)
				totB="-"+totB;
			if(giornoA.substring(0,1).compareTo("-")==0)
				giornoB="-"+giornoB;
			
			a=Integer.parseInt(totA)*60- Integer.parseInt(giornoA)*60;
			
			b=Integer.parseInt(totB) - Integer.parseInt(giornoB);
			totMinuti=a+b;
			
			totA=String.valueOf(Math.abs(totMinuti/60));
			if(Math.abs(totMinuti%60) < 10)
				totB=String.valueOf("0"+Math.abs(totMinuti%60));
			else
				totB=String.valueOf(Math.abs(totMinuti%60));
			if(String.valueOf(totMinuti).substring(0, 1).compareTo("-")!=0)
				totale=(totA+"."+totB);
			else
				totale=("-"+totA+"."+totB);
			
			return totale;		
	}
	
	
	@SuppressWarnings("unchecked")
	public static Boolean PrintRiepilogoOreMese(String dataRif, String sedeOperativa){
		List<RiepilogoFoglioOreModel> listaGiorni= new ArrayList<RiepilogoFoglioOreModel>();
		List<DettaglioOreGiornaliere> listaDettGiorno= new ArrayList<DettaglioOreGiornaliere>();
		List<FoglioOreMese> listaMesi=new ArrayList<FoglioOreMese>();
		List<Personale> listaPers= new ArrayList<Personale>();
		RiepilogoFoglioOreModel giorno;
		DatiOreMese datoG= new DatiOreMese();		
		
		List<DatiOreMese> listaDatiMese= new ArrayList<DatiOreMese>();
		Boolean exportOk= false;
		
		String sumTotGiorno="0.00";
		String sumOreViaggio="0.00";
		String sumDeltaOreViaggio="0.00";
		String sumOreTotali="0.00";
		String sumOreRecupero="0.00";
		String sumOreFerie="0.00";
		String sumOrePermesso="0.00";
		String sumOreStraordinario="0.00";
		String sumDeltaGiorno="0.00";
		
		/*		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		String anno=formatter.format(dataRif);
		formatter = new SimpleDateFormat("MMM", Locale.ITALIAN);
		String mese=formatter.format(dataRif);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    
		String data=(mese+anno);
		*/
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			listaPers=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede")
					.setParameter("sede", sedeOperativa).list();
						
		    for(Personale p:listaPers){
			  		
			 if(!p.getFoglioOreMeses().isEmpty()){
								
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(f.getMeseRiferimento().compareTo(dataRif)==0){
						
						listaDettGiorno.addAll(f.getDettaglioOreGiornalieres());
						Collections.sort(listaDettGiorno, new Comparator<DettaglioOreGiornaliere>(){
							  public int compare(DettaglioOreGiornaliere s1, DettaglioOreGiornaliere s2) {
								 return s1.getGiornoRiferimento().compareTo(s2.getGiornoRiferimento());
							  }
							});
						break;
					}
				}
				
				//caricare la lista con tutti i giorni di tutti i dipendenti: l'ultima con il totale
				
				for(DettaglioOreGiornaliere d:listaDettGiorno){
					
					datoG=new DatiOreMese();
					
					String day=new String();
					String oreTotali= "0.00";
					
					DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
					day=formatter.format(d.getGiornoRiferimento());
					
					if(d.getOreViaggio().compareTo("0.00")!=0){
						if(Float.valueOf(d.getDeltaOreGiorno())<0){
					
							if(Float.valueOf(d.getOreViaggio())>Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){							
								oreTotali=(p.getTipologiaOrario()+".00");					
							}								
							if(Float.valueOf(d.getOreViaggio())<=Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){
								String oreTotGenerale=ServerUtility.aggiornaTotGenerale(d.getTotaleOreGiorno(), d.getOreViaggio());			
								oreTotali=(oreTotGenerale);
							}
						}else					
						if(Float.valueOf(d.getDeltaOreGiorno())>=0){			
							oreTotali=d.getTotaleOreGiorno();
						}
						
					}else{						
						oreTotali=(d.getTotaleOreGiorno());
					}
										
					Date gRiferimento=d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN);
					String giornoR=formatter.format(gRiferimento);
										
					datoG.setUsername(p.getCognome()+" "+p.getNome());
					datoG.setGiornoRiferimento(giornoR);
					datoG.setTotGiorno(d.getTotaleOreGiorno());
					datoG.setOreViaggio(d.getOreViaggio());
					datoG.setDeltaOreViaggio(d.getDeltaOreViaggio());
					datoG.setOreTotali(oreTotali);
					datoG.setOreFerie(d.getOreFerie());
					datoG.setOrePermesso(d.getOrePermesso());
					datoG.setOreRecupero(d.getOreAssenzeRecupero());
					datoG.setOreStraordinario(d.getOreStraordinario());
					datoG.setGiustificativo(d.getGiustificativo());
					datoG.setNoteAggiuntive(d.getNoteAggiuntive());
					datoG.setDeltaGiornaliero(d.getDeltaOreGiorno());
					
					listaDatiMese.add(datoG);
					
					sumTotGiorno=ServerUtility.aggiornaTotGenerale(sumTotGiorno,d.getTotaleOreGiorno());
					sumOreViaggio=ServerUtility.aggiornaTotGenerale(sumOreViaggio, d.getOreViaggio());
					sumDeltaOreViaggio=ServerUtility.aggiornaTotGenerale(sumDeltaOreViaggio, d.getDeltaOreViaggio());
					sumOreTotali=ServerUtility.aggiornaTotGenerale(sumOreTotali, oreTotali);
					sumOreRecupero=ServerUtility.aggiornaTotGenerale(sumOreRecupero, d.getOreAssenzeRecupero());
					sumOreFerie=ServerUtility.aggiornaTotGenerale(sumOreFerie, d.getOreFerie());
					sumOrePermesso=ServerUtility.aggiornaTotGenerale(sumOrePermesso, d.getOrePermesso());
					sumOreStraordinario=ServerUtility.aggiornaTotGenerale(sumOreStraordinario, d.getOreStraordinario());
					sumDeltaGiorno=ServerUtility.aggiornaTotGenerale(sumDeltaGiorno, d.getDeltaOreGiorno());
				}
				
				//Elaboro il residuo per le ore a recupero
				String monteOreRecuperoTotale= p.getOreRecupero();
				String parzialeOreRecuperoMese="0.00";
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();			
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f:listaMesi){
						if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo(dataRif)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f.getDettaglioOreGiornalieres().isEmpty()){
								parzialeOreRecuperoMese="0.00";
								listaGiorniM.addAll(f.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									parzialeOreRecuperoMese=ServerUtility.aggiornaTotGenerale(parzialeOreRecuperoMese, d.getOreAssenzeRecupero());
										
								}	
								//se sono negative le escludo dal conteggio totale
								//if(Float.valueOf(parzialeOreRecuperoMese)>0)
									monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(monteOreRecuperoTotale,parzialeOreRecuperoMese);		
							}
						}									
					}		
				}
				
				datoG=new DatiOreMese();
				datoG.setUsername(p.getCognome()+" "+p.getNome());
				datoG.setGiornoRiferimento("RESIDUI");
				datoG.setTotGiorno("");
				datoG.setOreViaggio("");
				datoG.setDeltaOreViaggio("");
				datoG.setOreTotali("");
				datoG.setOreFerie("");
				datoG.setOrePermesso("");
				datoG.setOreRecupero(monteOreRecuperoTotale);
				datoG.setOreStraordinario("");
				datoG.setGiustificativo("");
				datoG.setNoteAggiuntive("");
				datoG.setDeltaGiornaliero("");
				listaDatiMese.add(datoG);
				
				datoG=new DatiOreMese();
				datoG.setUsername(p.getCognome()+" "+p.getNome());
				datoG.setGiornoRiferimento("TOTALE");
				datoG.setTotGiorno(sumTotGiorno);
				datoG.setOreViaggio(sumOreViaggio);
				datoG.setDeltaOreViaggio(sumDeltaOreViaggio);
				datoG.setOreTotali(sumOreTotali);
				datoG.setOreFerie(sumOreFerie);
				datoG.setOrePermesso(sumOrePermesso);
				datoG.setOreRecupero(ServerUtility.aggiornaTotGenerale(sumOreRecupero, monteOreRecuperoTotale));
				datoG.setOreStraordinario(sumOreStraordinario);
				datoG.setGiustificativo("");
				datoG.setNoteAggiuntive("");
				datoG.setDeltaGiornaliero(sumDeltaGiorno);
				listaDatiMese.add(datoG);
				
				//resetto a 0 il necessario
				listaDettGiorno.clear();
				listaMesi.clear();
				
				sumTotGiorno="0.00";
				sumOreViaggio="0.00";
				sumDeltaOreViaggio="0.00";
				sumOreTotali="0.00";
				sumOreRecupero="0.00";
				sumOreFerie="0.00";
				sumOrePermesso="0.00";
				sumOreStraordinario="0.00";
				sumDeltaGiorno="0.00";
								
			}
		 }
		  
		  session.createSQLQuery("truncate datioremese").executeUpdate();
		    
		  tx.commit();
		  		  
		  exportOk=exportListaDatiOreMese(listaDatiMese);
		  
		  if(exportOk)
			  return true;
		  else 
			  return false;
				 
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
			
	}
	
	//TODO aggiunta abbuono anche tabella datioremese
	//PER IL SINGOLO DIPENDENTE
	public static Boolean PrintRiepilogoOreMese(String dataRif, String sedeOperativa, String username){
		List<DettaglioOreGiornaliere> listaDettGiorno= new ArrayList<DettaglioOreGiornaliere>();
		List<FoglioOreMese> listaMesi=new ArrayList<FoglioOreMese>();
		DatiOreMese datoG= new DatiOreMese();		
		
		List<DatiOreMese> listaDatiMese= new ArrayList<DatiOreMese>();
		Boolean exportOk= false;
		
		String sumTotGiorno="0.00";
		String sumOreViaggio="0.00";
		String sumDeltaOreViaggio="0.00";
		String sumOreTotali="0.00";
		String sumOreRecupero="0.00";
		String sumOreFerie="0.00";
		String sumOrePermesso="0.00";
		String sumOreStraordinario="0.00";
		String sumDeltaGiorno="0.00";
		String sumOreAbbuono="0.00";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			Personale p=(Personale) session.createQuery("from Personale where username=:username")
					.setParameter("username", username).uniqueResult();
						  		
			 if(!p.getFoglioOreMeses().isEmpty()){
								
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(f.getMeseRiferimento().compareTo(dataRif)==0){
						
						listaDettGiorno.addAll(f.getDettaglioOreGiornalieres());
						Collections.sort(listaDettGiorno, new Comparator<DettaglioOreGiornaliere>(){
							  public int compare(DettaglioOreGiornaliere s1, DettaglioOreGiornaliere s2) {
								 return s1.getGiornoRiferimento().compareTo(s2.getGiornoRiferimento());
							  }
							});
						break;
					}
				}
				
				for(DettaglioOreGiornaliere d:listaDettGiorno){
					
					datoG=new DatiOreMese();
					
					String day=new String();
					String oreTotali= "0.00";
					
					DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
					day=formatter.format(d.getGiornoRiferimento());
					
					if(d.getOreViaggio().compareTo("0.00")!=0){
						if(Float.valueOf(d.getDeltaOreGiorno())<0){
					
							if(Float.valueOf(d.getOreViaggio())>Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){							
								oreTotali=(p.getTipologiaOrario()+".00");					
							}								
							if(Float.valueOf(d.getOreViaggio())<=Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){
								String oreTotGenerale=ServerUtility.aggiornaTotGenerale(d.getTotaleOreGiorno(), d.getOreViaggio());			
								oreTotali=(oreTotGenerale);
							}
						}else					
						if(Float.valueOf(d.getDeltaOreGiorno())>=0){			
							oreTotali=d.getTotaleOreGiorno();
						}
						
					}else{						
						oreTotali=(d.getTotaleOreGiorno());
					}
										
					Date gRiferimento=d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN);
					String giornoR=formatter.format(gRiferimento);
										
					datoG.setUsername(p.getCognome()+" "+p.getNome());
					datoG.setGiornoRiferimento(giornoR);
					datoG.setTotGiorno(d.getTotaleOreGiorno());
					datoG.setOreViaggio(d.getOreViaggio());
					datoG.setDeltaOreViaggio(d.getDeltaOreViaggio());
					datoG.setOreTotali(oreTotali);
					datoG.setOreFerie(d.getOreFerie());
					datoG.setOrePermesso(d.getOrePermesso());
					datoG.setOreRecupero(d.getOreAssenzeRecupero());
					datoG.setOreStraordinario(d.getOreStraordinario());
					datoG.setGiustificativo(d.getGiustificativo());
					datoG.setNoteAggiuntive(d.getNoteAggiuntive());
					datoG.setDeltaGiornaliero(d.getDeltaOreGiorno());
					
					
					listaDatiMese.add(datoG);
					
					sumTotGiorno=ServerUtility.aggiornaTotGenerale(sumTotGiorno,d.getTotaleOreGiorno());
					sumOreViaggio=ServerUtility.aggiornaTotGenerale(sumOreViaggio, d.getOreViaggio());
					sumDeltaOreViaggio=ServerUtility.aggiornaTotGenerale(sumDeltaOreViaggio, d.getDeltaOreViaggio());
					sumOreTotali=ServerUtility.aggiornaTotGenerale(sumOreTotali, oreTotali);
					sumOreRecupero=ServerUtility.aggiornaTotGenerale(sumOreRecupero, d.getOreAssenzeRecupero());
					sumOreFerie=ServerUtility.aggiornaTotGenerale(sumOreFerie, d.getOreFerie());
					sumOrePermesso=ServerUtility.aggiornaTotGenerale(sumOrePermesso, d.getOrePermesso());
					sumOreStraordinario=ServerUtility.aggiornaTotGenerale(sumOreStraordinario, d.getOreStraordinario());
					sumDeltaGiorno=ServerUtility.aggiornaTotGenerale(sumDeltaGiorno, d.getDeltaOreGiorno());
				}
				
				//Elaboro il residuo per le ore a recupero
				String monteOreRecuperoTotale= p.getOreRecupero();
				String parzialeOreRecuperoMese="0.00";
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();			
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f1:listaMesi){
						if(f1.getMeseRiferimento().compareTo("Feb2013")!=0 && f1.getMeseRiferimento().compareTo(dataRif)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f1.getDettaglioOreGiornalieres().isEmpty()){
								parzialeOreRecuperoMese="0.00";
								listaGiorniM.addAll(f1.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									parzialeOreRecuperoMese=ServerUtility.aggiornaTotGenerale(parzialeOreRecuperoMese, d.getOreAssenzeRecupero());
										
								}	
								//se sono negative le escludo dal conteggio totale
								//if(Float.valueOf(parzialeOreRecuperoMese)>0)
									monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(monteOreRecuperoTotale,parzialeOreRecuperoMese);	
							}
						}									
					}		
				}
				
				datoG=new DatiOreMese();
				datoG.setUsername(p.getCognome()+" "+p.getNome());
				datoG.setGiornoRiferimento("RESIDUI");
				datoG.setTotGiorno("");
				datoG.setOreViaggio("");
				datoG.setDeltaOreViaggio("");
				datoG.setOreTotali("");
				datoG.setOreFerie("");
				datoG.setOrePermesso("");
				datoG.setOreRecupero(monteOreRecuperoTotale);
				datoG.setOreStraordinario("");
				datoG.setGiustificativo("");
				datoG.setNoteAggiuntive("");
				datoG.setDeltaGiornaliero("");
				listaDatiMese.add(datoG);
				
				datoG=new DatiOreMese();
				datoG.setUsername(p.getCognome()+" "+p.getNome());
				datoG.setGiornoRiferimento("TOTALE");
				datoG.setTotGiorno(sumTotGiorno);
				datoG.setOreViaggio(sumOreViaggio);
				datoG.setDeltaOreViaggio(sumDeltaOreViaggio);
				datoG.setOreTotali(sumOreTotali);
				datoG.setOreFerie(sumOreFerie);
				datoG.setOrePermesso(sumOrePermesso);
				datoG.setOreRecupero(ServerUtility.aggiornaTotGenerale(sumOreRecupero, monteOreRecuperoTotale));
				datoG.setOreStraordinario(sumOreStraordinario);
				datoG.setGiustificativo("");
				datoG.setNoteAggiuntive("");
				datoG.setDeltaGiornaliero(sumDeltaGiorno);
				listaDatiMese.add(datoG);
				
				//resetto a 0 il necessario
				listaDettGiorno.clear();
				listaMesi.clear();
				
				sumTotGiorno="0.00";
				sumOreViaggio="0.00";
				sumDeltaOreViaggio="0.00";
				sumOreTotali="0.00";
				sumOreRecupero="0.00";
				sumOreFerie="0.00";
				sumOrePermesso="0.00";
				sumOreStraordinario="0.00";
				sumDeltaGiorno="0.00";
								
			}
		 
		  
		  session.createSQLQuery("truncate datioremese").executeUpdate();
		    
		  tx.commit();
		  		  
		  exportOk=exportListaDatiOreMese(listaDatiMese);
		  
		  if(exportOk)
			  return true;
		  else 
			  return false;
				 
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
			
	}

	
	private static Boolean exportListaDatiOreMese(List<DatiOreMese> listaDatiMese) {
				
		Boolean saveRecordOK=true;
		
		for(DatiOreMese d: listaDatiMese){
			
			saveRecordOK=saveDateG(d);		
		}
		
		if(saveRecordOK)		
			return true;
		else
			return false;
	}
	

	private static Boolean saveDateG(DatiOreMese d) {
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			session.save(d);
			tx.commit();
			return true;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}		
	}
	
	
	public static Boolean getRiepilogoGiornalieroCommesse(
			String username, String data) throws IllegalArgumentException {
		
		List<DatiRiepilogoMensileCommesse> listaG= new ArrayList<DatiRiepilogoMensileCommesse>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaD= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalliC= new ArrayList<DettaglioIntervalliCommesse>();
		List<AssociazionePtoA> listaAssociazioniPA= new ArrayList<AssociazionePtoA>();
		
		FoglioOreMese fM=new FoglioOreMese();
		Personale p= new Personale();
		Commessa c= new Commessa();
		
		String totaleOreLavoroC= "0.00";
		String totaleOreViaggioC= "0.00";
		String totaleOreC= "0.00";
				
		Date giorno= new Date();  
		String dipendente= new String();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		/*String anno=formatter.format(meseRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		String mese=formatter.format(meseRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
	    String data=mese+anno;
	    */
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			dipendente= p.getCognome()+" "+p.getNome();
			
			listaF.addAll(p.getFoglioOreMeses());
			listaAssociazioniPA.addAll(p.getAssociazionePtoas());
			
			for(FoglioOreMese f:listaF){//scorro i mesi per trovare il foglio ore desiderato
				if(f.getMeseRiferimento().compareTo(data)==0){
					fM=f;
					break;
				}
			}
			
			listaD.addAll(fM.getDettaglioOreGiornalieres()); //prendo tutti i giorni del mese
			Collections.sort(listaD, new Comparator<DettaglioOreGiornaliere>(){
				  public int compare(DettaglioOreGiornaliere s1, DettaglioOreGiornaliere s2) {
					 return s1.getGiornoRiferimento().compareTo(s2.getGiornoRiferimento());
				  }
			});
						
			for(DettaglioOreGiornaliere d: listaD ){//scorro i giorni del mese e calcolo il totale ore per ogni commessa selezionata
					giorno= d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd-MMM-yyy",Locale.ITALIAN) ; 
					String giornoF=formatter.format(giorno);
					
					if(!d.getDettaglioIntervalliCommesses().isEmpty())
						listaIntervalliC.addAll(d.getDettaglioIntervalliCommesses());
								
					for(DettaglioIntervalliCommesse dett:listaIntervalliC){
						
						if(Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio()))>0){
							DatiRiepilogoMensileCommesse riep= new DatiRiepilogoMensileCommesse();
						
							riep.setDataRiferimento(data);
							riep.setUsername(username);
							riep.setGiorno(giornoF);
							riep.setNumeroCommessa(dett.getNumeroCommessa()+"."+dett.getEstensioneCommessa());
							riep.setOreLavoro(dett.getOreLavorate());
							riep.setOreViaggio(dett.getOreViaggio());
							riep.setOreTotali(aggiornaTotGenerale(dett.getOreLavorate(), dett.getOreViaggio()));
							
							listaG.add(riep);							
						}
					}					
					listaIntervalliC.clear();							
			}
			
			//elaboro un record per i totali per ogni commessa
			for(AssociazionePtoA ass:listaAssociazioniPA){
				String commessa= ass.getAttivita().getCommessa().getNumeroCommessa() +"."+ ass.getAttivita().getCommessa().getEstensione();
				
				for(DatiRiepilogoMensileCommesse g:listaG){
					if(g.getNumeroCommessa().compareTo(commessa)==0){
						String oreLavoro=String.valueOf(g.getOreLavoro());
						String oreViaggio=String.valueOf(g.getOreViaggio());
						String oreTotali=String.valueOf(g.getOreTotali());
						
						if(oreLavoro.substring(oreLavoro.indexOf(".")+1, oreLavoro.length()).length()==1)
							oreLavoro=oreLavoro+"0";
						if(oreViaggio.substring(oreViaggio.indexOf(".")+1, oreViaggio.length()).length()==1)
							oreViaggio=oreViaggio+"0";
						if(oreTotali.substring(oreTotali.indexOf(".")+1, oreTotali.length()).length()==1)
							oreTotali=oreTotali+"0";
						
						totaleOreLavoroC= ServerUtility.aggiornaTotGenerale(totaleOreLavoroC, oreLavoro);
						totaleOreViaggioC= ServerUtility.aggiornaTotGenerale(totaleOreViaggioC, oreViaggio);
						totaleOreC=ServerUtility.aggiornaTotGenerale(totaleOreC, oreTotali);
					}
				}
				
				DatiRiepilogoMensileCommesse riep= new DatiRiepilogoMensileCommesse();
						
				//if(totaleOreC.compareTo("0.00")!=0){
					riep.setDataRiferimento(data);
					riep.setUsername(username);
					riep.setGiorno("TOTALE");
					riep.setNumeroCommessa(commessa);
					riep.setOreLavoro(totaleOreLavoroC);
					riep.setOreViaggio(totaleOreViaggioC);
					riep.setOreTotali(totaleOreC);
				
					listaG.add(riep);
				//}
				
				Collections.sort(listaG, new Comparator<DatiRiepilogoMensileCommesse>(){
					  public int compare(DatiRiepilogoMensileCommesse s1, DatiRiepilogoMensileCommesse s2) {
						 return s1.getNumeroCommessa().compareTo(s2.getNumeroCommessa());
					  }
				});
				
				totaleOreLavoroC= "0.00";
				totaleOreViaggioC= "0.00";
				totaleOreC= "0.00";
			}
			
			//eliminare dalla tabella i record presenti per username e periodo indicato(in modo tale da non creare duplicati)
			session.createSQLQuery("delete from dati_riepilogomensile_commesse where username=:username and dataRiferimento=:dataRif")
			.setParameter("username", username).setParameter("dataRif", data).executeUpdate();
			tx.commit();
					
			Boolean exportOK=exportListaDatiCommesse(listaG);
			if(exportOK)	
				return true;
			else
				return false;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	private static Boolean exportListaDatiCommesse(List<DatiRiepilogoMensileCommesse> listaDatiMese) {
		
		Boolean saveRecordOK=true;
		
		for(DatiRiepilogoMensileCommesse d: listaDatiMese){
			
			saveRecordOK=saveDateC(d);		
		}
		
		if(saveRecordOK)		
			return true;
		else
			return false;
	}

	private static Boolean saveDateC(DatiRiepilogoMensileCommesse d) {
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			session.save(d);
			tx.commit();
			return true;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}		
	}
	public static boolean isNotIncludedCommessa(List<DettaglioIntervalliCommesse> listaCommesse,
			DettaglioIntervalliCommesse dett) {
		
		boolean trovata=false;
		
		if(listaCommesse.isEmpty())
			return true;
		
		for(DettaglioIntervalliCommesse c:listaCommesse){
					
			String comp1= c.getNumeroCommessa()+"."+c.getEstensioneCommessa();
			String comp2= dett.getNumeroCommessa()+"."+dett.getEstensioneCommessa();
			
			if(comp1.compareTo(comp2)==0){
				trovata=true;
			}
		}
		
		if(trovata)
			return false;
		else
			return true;				
	}
		
	
	public static boolean isPrecedente(String periodoFF, String periodoRif) {
		
		String annoFF=periodoFF.substring(3,7);
		String meseFF=periodoFF.substring(0, 3);
			
		String annoRif=periodoRif.substring(3,7);
		String meseRif=periodoRif.substring(0, 3);
		
		Date data= new Date();
		Date data1= new Date();
		
	    meseFF=(meseFF.substring(0,1).toLowerCase()+meseFF.substring(1,3));
	    meseRif=(meseRif.substring(0,1).toLowerCase()+meseRif.substring(1,3));
	    
	    DateFormat  formatter = new SimpleDateFormat("MMMyyyy", Locale.ITALIAN);
	    	    
		try {			
			data= formatter.parse(meseFF+annoFF);
			data1= formatter.parse(meseRif+annoRif);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(data.before(data1)||data.equals(data1))
			return true;
		else
			return false;
	}
	
	
	public static float calcolaImporto(String tariffa, String totOre) {
		String ore= new String();
		String minuti= new String();
		Float res;
		
		if(totOre.indexOf(".")!=-1){
			ore=totOre.substring(0,totOre.indexOf("."));
			minuti=totOre.substring(totOre.indexOf(".")+1, totOre.length());
		}
		else{
			ore=totOre;
			minuti="0";
		}
		
		
		if(minuti.compareTo("15")==0)
			minuti="25";
		else
		if(minuti.compareTo("30")==0)
			minuti="50";
		else
		if(minuti.compareTo("45")==0)
			minuti="75";
		else minuti="0";
		
		totOre=ore+"."+minuti;
		
		res= Float.valueOf(totOre)*Float.valueOf(tariffa);
		return res;
	}
	
	
	public static boolean esisteCommessa(String numeroCommessa,
			List<String> listaNomiCommesse) {
		
		for(String nome:listaNomiCommesse){
			if(nome.compareTo(numeroCommessa)==0)
				return true;				
		}
		
		return false;
	}
	
	
	public static String exsistGiorno(List<DettaglioOreGiornaliere> listaG,
			String giornoR) {
		
		DettaglioOreGiornaliere dettSelected= new DettaglioOreGiornaliere();
		String giorno=new String();
		boolean existDay=false;
		
		for(DettaglioOreGiornaliere d:listaG){
			
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd") ; 
			giorno=formatter.format(d.getGiornoRiferimento());
			if(giorno.compareTo(giornoR)==0){
				existDay=true;
				dettSelected=d;
				break;
			}
						
		}
		
		if(!existDay)
			return "1";
		else 
			//TODO
			if(dettSelected.getDettaglioIntervalliIUs().size()>0 && hasValue(dettSelected.getDettaglioIntervalliCommesses()))
				return "0";								
			else
				if(!hasValue(dettSelected.getDettaglioIntervalliCommesses()))
					return "2";
		
		return "1";
	}	
	
	
	public static boolean isGiustificato(DettaglioOreGiornaliere d) {
		
		if(d.getGiustificativo().compareTo("")!=0 || d.getOreFerie().compareTo("0.00")!=0
				|| d.getOrePermesso().compareTo("0.00")!=0 )
			return true;		
		return false;
	}
	
	
	public static String traduciMeseToNumber(String month){
		
		if(month.compareTo("gen") ==0)
			month="01";
		if(month.compareTo("feb") ==0)
			month="02";
		if(month.compareTo("mar") ==0)
			month="03";
		if(month.compareTo("apr") ==0)
			month="04";
		if(month.compareTo("mag") ==0)
			month="05";
		if(month.compareTo("giu") ==0)
			month="06";
		if(month.compareTo("lug") ==0)
			month="07";
		if(month.compareTo("ago") ==0)
			month="08";
		if(month.compareTo("set") ==0)
			month="09";
		if(month.compareTo("ott") ==0)
			month="10";
		if(month.compareTo("nov") ==0)
			month="11";
		if(month.compareTo("dic") ==0)
			month="12";
		
		return month;
	}
	
	
	public static boolean hasValue(
			Set<DettaglioIntervalliCommesse> dettaglioIntervalliCommesses) {
		
		for(DettaglioIntervalliCommesse d: dettaglioIntervalliCommesses){
			
			if(d.getOreLavorate().compareTo("0.00")!=0)
				return true;			
		}
		return false;
	}
	
	
	public static List<RiepilogoAnnualeJavaBean> PrintRiepilogoOreAnnuale(String anno,
			String sede) {
		
		List<RiepilogoAnnualeJavaBean> listaR= new ArrayList<RiepilogoAnnualeJavaBean>();
		List<Personale> listaP= new ArrayList<Personale>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaG= new ArrayList<DettaglioOreGiornaliere>();
		RiepilogoAnnualeJavaBean riep= new RiepilogoAnnualeJavaBean();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		String oreOrdinarie="0.00";
		String oreStraOrdinarie="0.00";
		String orePermessoROL="0.00";
		String oreRecupero="0.00";
		String oreViaggio="0.00";
		String oreTotali="0.00";
		String oreFerie="0.00";
		String oreMutua="0.00";
		String oreCassa="0.00";
		String oreLegge104="0.00";
		String oreMaternita="0.00";
		String oreTotaliGiustificativi="0.00";
		
		try {
			tx=session.beginTransaction();
			
			listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede order by cognome ASC").setParameter("sede", sede).list();
			
			for(Personale p:listaP){
				
				listaF.addAll(p.getFoglioOreMeses());
				
				for(FoglioOreMese f: listaF){
					if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo("Mar2013")!=0 &&
						f.getMeseRiferimento().compareTo("Apr2013")!=0 && f.getMeseRiferimento().compareTo("Mag2013")!=0 )
						listaG.addAll(f.getDettaglioOreGiornalieres());
				}				
				
				for(DettaglioOreGiornaliere d:listaG){
					if(d.getGiustificativo().compareTo("")!=0)
						if(d.getGiustificativo().compareTo("06.Malattia")==0 || d.getGiustificativo().compareTo("07.Infortunio")==0)
							oreMutua=ServerUtility.aggiornaTotGenerale(oreMutua, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("24.Cassa Integrazione")==0)
							oreCassa=ServerUtility.aggiornaTotGenerale(oreCassa, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("25.Permesso Legge 104")==0)
							oreLegge104=ServerUtility.aggiornaTotGenerale(oreLegge104, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("08.Maternita' Obblig.")==0 || d.getGiustificativo().compareTo("09.Maternita' Facolt.")==0
								|| d.getGiustificativo().compareTo("09.1.Maternita' Antic.")==0)
							oreMaternita=ServerUtility.aggiornaTotGenerale(oreMaternita, d.getDeltaOreGiorno());
					
								
					oreViaggio=ServerUtility.aggiornaTotGenerale(oreViaggio, d.getDeltaOreViaggio());
					oreFerie=ServerUtility.aggiornaTotGenerale(oreFerie, d.getOreFerie());
					orePermessoROL=ServerUtility.aggiornaTotGenerale(orePermessoROL, d.getOrePermesso());
					oreStraOrdinarie=ServerUtility.aggiornaTotGenerale(oreStraOrdinarie, d.getOreStraordinario());
					oreRecupero=ServerUtility.aggiornaTotGenerale(oreRecupero, d.getOreAssenzeRecupero());
					oreOrdinarie=ServerUtility.aggiornaTotGenerale(oreOrdinarie, d.getTotaleOreGiorno());
												
				}
				
				if(Float.valueOf(oreRecupero)>0)
					oreOrdinarie=ServerUtility.getDifference(oreOrdinarie, oreRecupero);
				oreOrdinarie=ServerUtility.getDifference(oreOrdinarie, oreStraOrdinarie);
				
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreOrdinarie);
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreViaggio);
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreStraOrdinarie);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreMaternita);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreMutua);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreFerie);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, orePermessoROL);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreLegge104);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreCassa);
				
				
				riep=new RiepilogoAnnualeJavaBean(anno, p.getCognome(), p.getNome(), Float.valueOf(oreOrdinarie), Float.valueOf(oreStraOrdinarie), Float.valueOf(oreRecupero),
						Float.valueOf(oreViaggio), Float.valueOf(oreTotali), Float.valueOf(oreFerie), Float.valueOf(orePermessoROL), Float.valueOf(oreMutua), Float.valueOf(oreCassa)
						, Float.valueOf(oreLegge104), Float.valueOf(oreMaternita), Float.valueOf(oreTotaliGiustificativi));
						
				listaR.add(riep);
				
				oreOrdinarie="0.00";
				oreStraOrdinarie="0.00";
				oreRecupero="0.00";
				oreViaggio="0.00";
				oreTotali="0.00";
				oreFerie="0.00";
				oreMutua="0.00";
				oreCassa="0.00";
				oreLegge104="0.00";
				oreMaternita="0.00";
				oreTotaliGiustificativi="0.00";
				orePermessoROL="0.00";
				
				listaF.clear();
				listaG.clear();			
			}			
			
			tx.commit();
			return listaR;
			
		} catch (Exception e) {
		if (tx != null)
			tx.rollback();
		e.printStackTrace();
		return null;
	}finally{
		session.close();
	}	
	
	}
	
	
	public static List<DatiFatturazioneMeseJavaBean> PrintRiepilogoDatiFatturazione(
			String anno, String mese) {
		List<DatiFatturazioneMeseJavaBean> listaDati= new ArrayList<DatiFatturazioneMeseJavaBean>();
		List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
		List<String> matricolePM= new ArrayList<String>();
		DatiFatturazioneMeseJavaBean datiModel;
		Ordine o;
		float importo;
		float margine;
		String oreMargine= new String();
		String oreScaricate= new String();
		String totOreEseguite="0.00";
		String totOreFatturate="0.00";
		String totVarSal="0.00";
		String totVarPcl="0.00";
		Float totImport= (float) 0;
		Float importoSal= (float) 0;
		Float importoPcl= (float) 0;
		Float totImportoSal= (float) 0;
		Float totImportoPcl= (float) 0;
		String totOreScaricate="0.00";
		String totOreMargine="0.00";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		mese=mese+anno;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		try {
			tx = session.beginTransaction();
		
			listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where meseCorrente=:mese").setParameter("mese", mese).list();
			for(FoglioFatturazione f: listaFF){	
				if(f.getCommessa().getMatricolaPM()!=null && !exsistMatricolaPM(f.getCommessa().getMatricolaPM(), matricolePM))
					matricolePM.add(f.getCommessa().getMatricolaPM());
				if(!f.getCommessa().getOrdines().isEmpty()){
					o=f.getCommessa().getOrdines().iterator().next();
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreScaricate=oreMargine;					
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(oreMargine);
					importo=ServerUtility.calcolaImporto(o.getTariffaOraria(),f.getOreFatturare());
					importoSal=ServerUtility.calcolaImporto(o.getTariffaOraria(),f.getVariazioneSAL());
					importoPcl=ServerUtility.calcolaImporto(o.getTariffaOraria(),f.getVariazionePCL());
					
					datiModel=new DatiFatturazioneMeseJavaBean(f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), o.getRda().getCliente().getRagioneSociale(),
							o.getCommessa().getDenominazioneAttivita(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare()), Float.valueOf(f.getTariffaUtilizzata()),
							importo, Float.valueOf(f.getVariazioneSAL()), importoSal, Float.valueOf(f.getVariazionePCL()), importoPcl, Float.valueOf(oreScaricate), margine, f.getNote());
				}
				else{
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreScaricate=oreMargine;
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(oreMargine);
					importoSal=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazioneSAL());
					importoPcl=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazionePCL());
					
					datiModel=new DatiFatturazioneMeseJavaBean(f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), "#",
							f.getCommessa().getDenominazioneAttivita(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare()),
							Float.valueOf(f.getTariffaUtilizzata()), (float) 0.0, Float.valueOf(f.getVariazioneSAL()), importoSal, Float.valueOf(f.getVariazionePCL()), importoPcl, Float.valueOf(oreScaricate),margine, f.getNote());	
				}
				listaDati.add(datiModel);
			}			
			
			tx.commit();
			
			List<DatiFatturazioneMeseJavaBean> listaApp= new ArrayList<DatiFatturazioneMeseJavaBean>();
			listaApp.addAll(listaDati);
			
			for(String pm:matricolePM){
				for(DatiFatturazioneMeseJavaBean df:listaApp){
					if(df.getPm().compareTo(pm)==0){
						totOreEseguite=ServerUtility.aggiornaTotGenerale(totOreEseguite, d.format(df.getOreEseguite()));
						totOreFatturate=ServerUtility.aggiornaTotGenerale(totOreFatturate, d.format(df.getOreFatturate()));
						totVarSal=ServerUtility.aggiornaTotGenerale(totVarSal, d.format(df.getVariazioneSal()));
						totVarPcl=ServerUtility.aggiornaTotGenerale(totVarPcl, d.format(df.getVariazionePcl()));
						totImport=totImport+Float.valueOf(df.getOreFatturate())*df.getTariffaOraria();
						totOreScaricate=ServerUtility.aggiornaTotGenerale(totOreScaricate, d.format(df.getOreScaricate()));
						totOreMargine=ServerUtility.aggiornaTotGenerale(totOreMargine, d.format(df.getMargine()));
						totImportoSal=totImportoSal+Float.valueOf(df.getVariazioneSal())*df.getTariffaOraria();
						totImportoPcl=totImportoPcl+Float.valueOf(df.getVariazionePcl())*df.getTariffaOraria();
												
					}				
				}	
				datiModel=new DatiFatturazioneMeseJavaBean(pm, "TOTALE", "", "", Float.valueOf(totOreEseguite), Float.valueOf(totOreFatturate), 
						Float.valueOf("0"),totImport, Float.valueOf(totVarSal), totImportoSal, Float.valueOf(totVarPcl), totImportoPcl, 
						Float.valueOf(totOreScaricate), Float.valueOf(totOreMargine), "");
				
				listaDati.add(datiModel);
				totOreEseguite="0.00";
				totOreFatturate="0.00";
				totVarSal="0.00";
				totVarPcl="0.00";
				totImport=(float) 0;
				totOreScaricate="0.00";
				totOreMargine="0.00";
			}
			
			return listaDati;
		}catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	
	private static boolean exsistMatricolaPM(String matricolaPM,
			List<String> matricolePM) {
		
		for(String pm: matricolePM)
			if (pm.compareTo(matricolaPM)==0)
				return true;
		return false;		
	}
	
	public static boolean ExistDip(List<RiepilogoOreDipFatturazione> listaAppCalcoloOreIU, int idDip) {
		
		for(RiepilogoOreDipFatturazione r:listaAppCalcoloOreIU){
			if(r.getIdPersonale()==idDip && r.getNumeroCommessa().compareTo(".TOTALE")==0)
				return true;
		}
		return false;
	}
	
	public static List<String> getListaMesiPerAnno(String data) {
		List<String> listaMesi=new ArrayList<String>();
		
		listaMesi.add("Gen"+data);
		listaMesi.add("Feb"+data);
		listaMesi.add("Mar"+data);
		listaMesi.add("Apr"+data);
		listaMesi.add("Mag"+data);
		listaMesi.add("Giu"+data);
		listaMesi.add("Lug"+data);
		listaMesi.add("Ago"+data);
		listaMesi.add("Set"+data);
		listaMesi.add("Ott"+data);
		listaMesi.add("Nov"+data);
		listaMesi.add("Dic"+data);
		
		return listaMesi;
	}
	
	
	public static String getOreCentesimi(String totOreCommMese) {	
		String ore=totOreCommMese.substring(0, totOreCommMese.indexOf("."));
		String decimali=totOreCommMese.substring(totOreCommMese.indexOf(".")+1,totOreCommMese.length());
		
		if(decimali.compareTo("00")==0)
			return totOreCommMese;		
		if(decimali.compareTo("15")==0){
			decimali="25";
			return ore+"."+decimali;
		}		
		if(decimali.compareTo("30")==0){
			decimali="50";
			return ore+"."+decimali;
		}
		if(decimali.compareTo("45")==0){
			decimali="75";
			return ore+"."+decimali;
		}	
		return totOreCommMese;
	}
	
	
	public static List<RiepilogoOreNonFatturabiliJavaBean> traduciNonFatturabiliModelToBean(
			List<RiepilogoOreNonFatturabiliModel> lista) {
		
		List<RiepilogoOreNonFatturabiliJavaBean> listaR = new ArrayList<RiepilogoOreNonFatturabiliJavaBean>();
		RiepilogoOreNonFatturabiliJavaBean rB;
		
		for(RiepilogoOreNonFatturabiliModel r:lista){
			
			rB=new RiepilogoOreNonFatturabiliJavaBean(r.getSede(), r.getGruppoLavoro(), r.getAttivita(), r.getRisorsa(), 
					r.getM1(),r.getM2() , r.getM3(), r.getM4(), r.getM5(), r.getM6(), r.getM7(), r.getM8(), r.getM9(), 
					r.getM10(), r.getM11(), r.getM12(), r.getCostoOrario(), r.getTotaleOre(), r.getCostoEffettivo());
			listaR.add(rB);
		}		
		
		return listaR;
	}
}

