package gestione.pack.server;

import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.shared.DatiOreMese;
import gestione.pack.shared.DatiTimbratriceExt;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.DettaglioTimbrature;
import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Personale;


import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        case "Jan":  giorni = 31;
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
        case "May":  giorni = 31;
                 break;
        case "Jun":  giorni = 30;
                 break;
        case "Jul":   giorni = 31;
                 break;
        case "Aug":   giorni = 31;
                 break;
        case "Sep":   giorni = 30;
                 break;
        case "Oct":  giorni = 31;
                 break;
        case "Nov":  giorni = 30;
                 break;
        case "Dec":  giorni = 31;
                 break;
		}
			
		return giorni;
	}
	
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
		DateFormat formatter = new SimpleDateFormat("yyyy", Locale.ENGLISH) ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM", Locale.ENGLISH);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
			
		giorniMese=ServerUtility.getGiorniMese(mese, anno);
					
		
		for(int i=1;i<=giorniMese;i++){
			if(i<10)
				dataGiornoMese=(anno+"-"+mese+"-"+"0"+i);
			else
				dataGiornoMese=(anno+"-"+mese+"-"+i);
			
			formatter = new SimpleDateFormat("yyyy-MMM-dd",Locale.ENGLISH);
			try {
				date = (Date)formatter.parse(dataGiornoMese);
				data=date.toString();	
				giorno=data.substring(0, 3);
				
				formatter = new SimpleDateFormat("yyyy") ; 
				anno=formatter.format(date);
				formatter = new SimpleDateFormat("MMM",Locale.ENGLISH);
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
		if(dataCompleta.compareTo("2013-May-01")==0)
			return true;
		if(dataCompleta.compareTo("2013-Nov-01")==0)
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
		
		if(mese.compareTo("Jan") ==0){
			mese="Dec";
			anno=String.valueOf(Integer.parseInt(anno)-1);
			mese=mese+anno;
		}
		if(mese.compareTo("Feb") ==0){
			mese="Jan";
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
		if(mese.compareTo("May") ==0){
			mese="Apr";
			mese=mese+anno;
		}
		if(mese.compareTo("Jun") ==0){
			mese="May";
			mese=mese+anno;
		}
		if(mese.compareTo("Jul") ==0){
			mese="Jun";
			mese=mese+anno;
		}
		if(mese.compareTo("Aug") ==0){
			mese="Jul";
			mese=mese+anno;
		}
		if(mese.compareTo("Sep") ==0){
			mese="Aug";
			mese=mese+anno;
		}
		if(mese.compareTo("Oct") ==0){
			mese="Sep";
			mese=mese+anno;
		}
		if(mese.compareTo("Nov") ==0){
			mese="Oct";
			mese=mese+anno;
		}
		if(mese.compareTo("Dec") ==0){
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
	public static Boolean PrintRiepilogoOreMese(Date dataRif){
		List<RiepilogoFoglioOreModel> listaGiorni= new ArrayList<RiepilogoFoglioOreModel>();
		List<DettaglioOreGiornaliere> listaDettGiorno= new ArrayList<DettaglioOreGiornaliere>();
		List<FoglioOreMese> listaMesi=new ArrayList<FoglioOreMese>();
		List<Personale> listaPers= new ArrayList<Personale>();
		RiepilogoFoglioOreModel giorno;
		DatiOreMese datoG= new DatiOreMese();		
		
		List<DatiOreMese> listaDatiMese= new ArrayList<DatiOreMese>();
		Boolean exportOk= false;
				
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		String anno=formatter.format(dataRif);
		formatter = new SimpleDateFormat("MMM");
		String mese=formatter.format(dataRif);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    
		String data=(mese+anno);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			listaPers=(List<Personale>)session.createQuery("from Personale").list();//TODO distinguere torino da brescia
						
		    for(Personale p:listaPers){
			  		
			 if(!p.getFoglioOreMeses().isEmpty()){
								
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(f.getMeseRiferimento().compareTo(data)==0){
						
						/*datoG= new DatiOreMese();		  
						datoG.setUsername(p.getCognome()+" "+p.getNome()); //riga con solo l'username
						listaDatiMese.add(datoG);
						*/
						listaDettGiorno.addAll(f.getDettaglioOreGiornalieres());
						Collections.sort(listaDettGiorno, new Comparator<DettaglioOreGiornaliere>(){
							  public int compare(DettaglioOreGiornaliere s1, DettaglioOreGiornaliere s2) {
								 return s1.getGiornoRiferimento().compareTo(s2.getGiornoRiferimento());
							  }
							});
						break;
					}
				}
				
				//caricare la lista con tutti i giorni di tutti i dipendenti: la prima riga di ogni dipendente deve essere solo con l'username, l'ultima con il totale, poi due vuote
				
				for(DettaglioOreGiornaliere d:listaDettGiorno){
					
					datoG=new DatiOreMese();
					
					String day=new String();
					String oreTotali= "0.00";
					
					formatter = new SimpleDateFormat("dd-MMM-yyyy");
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
					formatter = new SimpleDateFormat("dd/MM/yy");
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
					
					listaDatiMese.add(datoG);
				}
				listaDettGiorno.clear();
				listaMesi.clear();
								
			}
		 }
		  
		  tx.commit();
		  
		  exportOk=exportListaDatiCommesse(listaDatiMese);
		  
		  return true;
				 
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
			
	}

	private static Boolean exportListaDatiCommesse(List<DatiOreMese> listaDatiMese) {
				
		for(DatiOreMese d: listaDatiMese){
			
			saveDateG(d);		
		}
		
				
		return true;	
	}

	private static void saveDateG(DatiOreMese d) {
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			session.save(d);
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}		
	}
}

