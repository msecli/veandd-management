package gestione.pack.server;

import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.Commessa;
import gestione.pack.shared.DatiOreMese;
import gestione.pack.shared.DatiRiepilogoMensileCommesse;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioOreGiornaliere;

import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Personale;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
					
		/*String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;

		totA=totOre.substring(0,totOre.indexOf("."));
		totB=totOre.substring(totOre.indexOf(".")+1, totOre.length());	
		
		if(totaleOreGiorno.indexOf(".")!=-1){
			giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.indexOf("."));
			giornoB= totaleOreGiorno.substring(totaleOreGiorno.indexOf(".")+1, totaleOreGiorno.length());
		}else{
			giornoA= totaleOreGiorno.substring(0, totaleOreGiorno.length());
			giornoB= "0.0";
			
		}
		
		a=Integer.parseInt(totA)*60+ Integer.parseInt(giornoA)*60;
		b=Integer.parseInt(totB) + Integer.parseInt(giornoB);
		totMinuti=a+b;
		
		totA=String.valueOf(totMinuti/60);
		if(totMinuti%60 < 10)
			totB=String.valueOf("0"+totMinuti%60);
		else
			totB=String.valueOf(totMinuti%60);
			
		totale=(totA+"."+totB);
		return totale;*/
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
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();			
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f:listaMesi){
						if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo(dataRif)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f.getDettaglioOreGiornalieres().isEmpty()){
								listaGiorniM.addAll(f.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(d.getOreAssenzeRecupero(), monteOreRecuperoTotale);	
								}		
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
				datoG.setOrePermesso(sumOreFerie);
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
	
	
	//PER IL SINGOLO DIPENDENTE //TODO
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
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();			
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f1:listaMesi){
						if(f1.getMeseRiferimento().compareTo("Feb2013")!=0 && f1.getMeseRiferimento().compareTo(dataRif)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f1.getDettaglioOreGiornalieres().isEmpty()){
								listaGiorniM.addAll(f1.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(d.getOreAssenzeRecupero(), monteOreRecuperoTotale);	
								}		
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
				datoG.setOrePermesso(sumOreFerie);
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
						
				if(totaleOreC.compareTo("0.00")!=0){
					riep.setDataRiferimento(data);
					riep.setUsername(username);
					riep.setGiorno("TOTALE");
					riep.setNumeroCommessa(commessa);
					riep.setOreLavoro(totaleOreLavoroC);
					riep.setOreViaggio(totaleOreViaggioC);
					riep.setOreTotali(totaleOreC);
				
					listaG.add(riep);
				}
				
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
	
}

