package gestione.pack.client.utility;

import java.util.ArrayList;
import java.util.List;

public class ClientUtility {

	public static String getURLRedirect(){	
		//return "http://127.0.0.1:8888/GestVanDHibernate.html?gwt.codesvr=127.0.0.1:9997";
		//return "http://localhost:8080";
		return "https://54.247.74.173:8443";
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
	

	public static String calcoloDelta(String scaricate, String oreEseguite ) {//per la differenza
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a, b, totMinuti;

			if(scaricate.indexOf(".")!=-1){
				totA=scaricate.substring(0,scaricate.indexOf("."));
				totB=scaricate.substring(scaricate.indexOf(".")+1, scaricate.length());	
			}else {
				totA=scaricate;
				totB="0";
			}
					
			if(oreEseguite.indexOf(".")!=-1){
				giornoA= oreEseguite.substring(0, oreEseguite.indexOf("."));
				giornoB= oreEseguite.substring(oreEseguite.indexOf(".")+1, oreEseguite.length());
			}else{
				giornoA= oreEseguite;
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

	
	public static String calcolaSommaGiustificativi(String oreRecupero, String orePermessi, String oreFerie, String oreExt) {
		String totale= new String();
		String opA= new String();
		String opB=new String();
				
		if(oreRecupero.substring(0,1).compareTo("-")==0)
			oreRecupero=oreRecupero.substring(1,oreRecupero.length());
		if(orePermessi.substring(0,1).compareTo("-")==0)
			orePermessi=orePermessi.substring(1,orePermessi.length());
		if(oreFerie.substring(0,1).compareTo("-")==0)
			oreFerie=oreFerie.substring(1,oreFerie.length());
		if(oreExt.substring(0,1).compareTo("-")==0)
			oreExt=oreExt.substring(1,oreExt.length());
		
		opA=String.valueOf(Integer.parseInt(oreRecupero.substring(0, oreRecupero.indexOf(".")))+
				Integer.parseInt(orePermessi.substring(0, orePermessi.indexOf("."))) + 
				Integer.parseInt(oreFerie.substring(0, oreFerie.indexOf("."))) +
				Integer.parseInt(oreExt.substring(0, oreExt.indexOf("."))));
		
		opB=String.valueOf(Integer.parseInt(oreRecupero.substring(oreRecupero.indexOf(".")+1, oreRecupero.length()))+
				Integer.parseInt(orePermessi.substring(orePermessi.indexOf(".")+1, orePermessi.length())) + 
				Integer.parseInt(oreFerie.substring(oreFerie.indexOf(".")+1, oreFerie.length())) +
				Integer.parseInt(oreExt.substring(oreExt.indexOf(".")+1, oreExt.length())));
		
		if((Integer.parseInt(opB)==0)&&(Integer.parseInt(opA)==0))
			return "0.00" ;
		
		if(Integer.parseInt(opB)==0)
			return totale=("-"+opA+".00");
		if(Integer.parseInt(opB)>59)
			if((Integer.parseInt(opB)%60)<10)
				totale=String.valueOf("-"+(Integer.parseInt(opA)+(Integer.parseInt(opB)/60))+".0"+(Integer.parseInt(opB)%60));
			else
				totale=String.valueOf("-"+(Integer.parseInt(opA)+(Integer.parseInt(opB)/60))+"."+(Integer.parseInt(opB)%60));
		else
			if((Integer.parseInt(opB))<10)	totale=("-"+opA+".0"+opB);
			else totale=("-"+opA+"."+opB);
			
		return totale;
	}
	
	
	public static String sommaOreStrOreRec(String oreStraordinario, String oreRecupero) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=oreStraordinario.substring(0, oreStraordinario.indexOf("."));
		totB=oreStraordinario.substring(oreStraordinario.indexOf(".")+1, oreStraordinario.length());
		
		giornoA= oreRecupero.substring(0, oreRecupero.indexOf("."));
		giornoB= oreRecupero.substring(oreRecupero.indexOf(".")+1, oreRecupero.length());
						
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60<10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				if(b<10)
					totale = (String.valueOf(a)+".0"+String.valueOf(b));
				else
					totale = (String.valueOf(a)+"."+String.valueOf(b));
		
		return totale;
	}

	/*
	public static String calcolaOreViaggio(String totOreViaggio, String oreViaggioGiorno) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=totOreViaggio.substring(0, totOreViaggio.indexOf("."));
		totB=totOreViaggio.substring(totOreViaggio.indexOf(".")+1, totOreViaggio.length());
		
		giornoA= oreViaggioGiorno.substring(0, oreViaggioGiorno.indexOf("."));
		giornoB= oreViaggioGiorno.substring(oreViaggioGiorno.indexOf(".")+1, oreViaggioGiorno.length());
						
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60<10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				if(b<10)
					totale = (String.valueOf(a)+".0"+String.valueOf(b));
				else
					totale = (String.valueOf(a)+"."+String.valueOf(b));
		return totale;
	}


	public static String calcolaOreLavoro(String totOreLavoro, String oreLavoroGiorno) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=totOreLavoro.substring(0, totOreLavoro.indexOf("."));
		totB=totOreLavoro.substring(totOreLavoro.indexOf(".")+1, totOreLavoro.length());
		
		giornoA= oreLavoroGiorno.substring(0, oreLavoroGiorno.indexOf("."));
		giornoB= oreLavoroGiorno.substring(oreLavoroGiorno.indexOf(".")+1, oreLavoroGiorno.length());
						
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60<10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				if(b<10)
					totale = (String.valueOf(a)+".0"+String.valueOf(b));
				else
					totale = (String.valueOf(a)+"."+String.valueOf(b));
		
		return totale;
	}
	
	*/
	public static String calcolaParzialeIntervalli(String ingresso, String uscita) {
		String totale= new String();
								
		String txtIa=new String();
		String txtIb=new String();
		String txtUa=new String();
		String txtUb=new String();
					
		int ia, ib, ua, ub, resA, resB;
		
		txtIa=ingresso.substring(0,ingresso.substring(0, ingresso.indexOf(":")).length());
		txtIb=ingresso.substring(ingresso.indexOf(":")+1, ingresso.length());
		txtUa=uscita.substring(0,uscita.substring(0, uscita.indexOf(":")).length());
		txtUb=uscita.substring(uscita.indexOf(":")+1, uscita.length());
					
		ia=Integer.parseInt(txtIa);
		ib=Integer.parseInt(txtIb);
		ua=Integer.parseInt(txtUa);
		ub=Integer.parseInt(txtUb);
		
		if(ua<ia) return totale="errore: L'orario di uscita e' inferiore a quello d'ingresso!";
		if(ua==ia && ub<=ib) return totale="errore: L'orario di uscita e' inferiore a quello d'ingresso!";
		
		
		if(ub<ib){
			resB=60-(ib-ub);
			resA=ua-ia-1;
		}else{
			resB=ub-ib;
			resA=ua-ia;				
		}		
		totale=String.valueOf(resA)+"."+String.valueOf(resB);
		return totale;
	}
	
	
	public static  String calcolaTempo(List<String> listaParziali) {
		
		String totale=new String();
		String parziale= new String();
		
		int a_1, b_1 , a_2, b_2, totA, totB;
		
		totale=listaParziali.get(0);
		
		for(int i=0;i<=3;i++){
			
			parziale=listaParziali.get(i+1);
			
			if(totale.indexOf(".")!=-1){
				a_1= Integer.parseInt(totale.substring(0,totale.indexOf(".")));
				b_1= Integer.parseInt(totale.substring(totale.indexOf(".")+1, totale.length()));
			}else{
				a_1=Integer.parseInt(totale);
				b_1=0;				
			}
					
			if(parziale.indexOf(".")!=-1){
				a_2= Integer.parseInt(parziale.substring(0,parziale.indexOf(".")));
				b_2= Integer.parseInt(parziale.substring(parziale.indexOf(".")+1, parziale.length()));
			}else{
				if(parziale.length()<10){
					a_2=Integer.parseInt(parziale);
					b_2=0;				
				}else{
					a_2=0;
					b_2=0;					
				}					
			}
			
			if(b_1+b_2>59){
				totB=b_1 + b_2-60;
				totA=a_1 + a_2+1;				
			}else{
				totB=b_1 + b_2;
				totA=a_1+a_2;				
			}
			
			if(String.valueOf(totB).length()==1)
			  totale=(String.valueOf(totA)+".0"+String.valueOf(totB));
			else
				totale=(String.valueOf(totA)+"."+String.valueOf(totB));
		}		
		return totale;
	
	} 		
	

	public static String traduciMese(String month) {
		String mese=new String();
		
		if(month.compareTo("Gennaio") ==0)
			mese="Gen";
		if(month.compareTo("Febbraio") ==0)
			mese="Feb";
		if(month.compareTo("Marzo") ==0)
			mese="Mar";
		if(month.compareTo("Aprile") ==0)
			mese="Apr";
		if(month.compareTo("Maggio") ==0)
			mese="Mag";
		if(month.compareTo("Giugno") ==0)
			mese="Giu";
		if(month.compareTo("Luglio") ==0)
			mese="Lug";
		if(month.compareTo("Agosto") ==0)
			mese="Ago";
		if(month.compareTo("Settembre") ==0)
			mese="Set";
		if(month.compareTo("Ottobre") ==0)
			mese="Ott";
		if(month.compareTo("Novembre") ==0)
			mese="Nov";
		if(month.compareTo("Dicembre") ==0)
			mese="Dic";
		
		return mese;
	}
	
	public static String meseToLong(String month) {
		String mese=new String();
		
		if(month.compareTo("Gen") ==0)
			mese="Gennaio";
		if(month.compareTo("Feb") ==0)
			mese="Febbraio";
		if(month.compareTo("Mar") ==0)
			mese="Marzo";
		if(month.compareTo("Apr") ==0)
			mese="Aprile";
		if(month.compareTo("Mag") ==0)
			mese="Maggio";
		if(month.compareTo("Giu") ==0)
			mese="Giugno";
		if(month.compareTo("Lug") ==0)
			mese="Luglio";
		if(month.compareTo("Ago") ==0)
			mese="Agosto";
		if(month.compareTo("Set") ==0)
			mese="Settembre";
		if(month.compareTo("Ott") ==0)
			mese="Ottobre";
		if(month.compareTo("Nov") ==0)
			mese="Novembre";
		if(month.compareTo("Dic") ==0)
			mese="Dicembre";
		
		return mese;
	}
	
	
	public static String traduciMeseToIt(String month){
		//String mese=new String();
		
		if(month.compareTo("Jan") ==0)
			month="Gen";
		if(month.compareTo("Feb") ==0)
			month="Feb";
		if(month.compareTo("Mar") ==0)
			month="Mar";
		if(month.compareTo("Apr") ==0)
			month="Apr";
		if(month.compareTo("May") ==0)
			month="Mag";
		if(month.compareTo("Jun") ==0)
			month="Giu";
		if(month.compareTo("Jul") ==0)
			month="Lug";
		if(month.compareTo("Aug") ==0)
			month="Ago";
		if(month.compareTo("Sep") ==0)
			month="Set";
		if(month.compareTo("Oct") ==0)
			month="Ott";
		if(month.compareTo("Nov") ==0)
			month="Nov";
		if(month.compareTo("Dec") ==0)
			month="Dic";
		
		return month;
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
	
	
	public static int getGiorniMese(String mese, String anno){
		int giorni=0;
		int a=Integer.valueOf(anno);
		boolean bisestile=false;

		if((a%4) == 0 && (a % 100 != 0 || a % 400 == 0))
			bisestile=true;

		if(mese.compareTo("Gen")==0)
			giorni = 31;
		else
			if(mese.compareTo("Feb")==0){
				if(!bisestile)
        			giorni = 28;
        		else giorni=29;
			}
		else
			if(mese.compareTo("Mar")==0)
				giorni = 31;
		else
			if(mese.compareTo("Apr")==0)
				giorni = 30;
			else
				if(mese.compareTo("Mag")==0)
					giorni = 31;
				else
					if(mese.compareTo("Giu")==0)
						giorni = 30;
					else
						if(mese.compareTo("Lug")==0)
							giorni = 31;
						else
							if(mese.compareTo("Ago")==0)
								giorni = 31;
							else
								if(mese.compareTo("Set")==0)
									giorni = 30;
								else
									if(mese.compareTo("Ott")==0)
										giorni = 31;
									else
										if(mese.compareTo("Nov")==0)
											giorni = 30;
										else
											if(mese.compareTo("Dic")==0)
												giorni = 31;
				
		return giorni;
	}
	
	public static String arrotondaIntervallo(String intervallo, String movimento){
		String arrotondato=new String();
		String minuti=intervallo.substring(intervallo.indexOf(":")+1, intervallo.length());
		String ore=intervallo.substring(0,intervallo.indexOf(":"));
		
		int min=Integer.valueOf(minuti);
		int oreI= Integer.valueOf(ore);
		
		//Ai 15 Minuti
		if(min==0){
			minuti="00";
			ore=String.valueOf(oreI);
			arrotondato=(ore+":"+minuti);
			return arrotondato;
		}
		
		if(0<min && min<15) 
			if(movimento.compareTo("I")==0)
				min=15;
			else {
				minuti="00";
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
		if(15<min && min<30) 
			if(movimento.compareTo("I")==0)
				min=30;
			else min=15;
		if(30<min && min<45) 
			if(movimento.compareTo("I")==0)
				min=45;
			else min=30;
		if(45<min && min<=59) 
			if(movimento.compareTo("I")==0){
				min=0;
				oreI=oreI+1;
				minuti=String.valueOf("0"+min);
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
			else
				min=45;
			
		//Ai 5 minuti
		/*
		if(0<min && min<5) 
			if(movimento.compareTo("I")==0){
				min=5;
				minuti=String.valueOf("0"+min);
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
			else {
				min=0;
				minuti=String.valueOf("0"+min);
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
		if(5<min && min<10) 
			if(movimento.compareTo("I")==0)
				min=10;
			else {
				min=5;
				minuti=String.valueOf("0"+min);
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
		if(10<min && min<15) 
			if(movimento.compareTo("I")==0)
				min=15;
			else min=10;
		if(15<min && min<20) 
			if(movimento.compareTo("I")==0)
				min=20;
			else min=15;
		if(20<min && min<25) 
			if(movimento.compareTo("I")==0)
				min=25;
			else min=20;
		if(25<min && min<30) 
			if(movimento.compareTo("I")==0)
				min=30;
			else min=25;
		if(30<min && min<35) 
			if(movimento.compareTo("I")==0)
				min=35;
			else min=30;
		if(35<min && min<40) 
			if(movimento.compareTo("I")==0)
				min=40;
			else min=35;
		if(40<min && min<45) 
			if(movimento.compareTo("I")==0)
				min=45;
			else min=40;
		if(45<min && min<50) 
			if(movimento.compareTo("I")==0)
				min=50;
			else min=45;
		if(50<min && min<55) 
			if(movimento.compareTo("I")==0)
				min=55;
			else min=50;
		if(55<min && min<=59) 
			if(movimento.compareTo("I")==0){
				min=0;
				oreI=oreI+1;
				minuti=String.valueOf("0"+min);
				ore=String.valueOf(oreI);
				arrotondato=(ore+":"+minuti);
				return arrotondato;
			}
			else{
				min=55;
			}
			
		*/
		
		minuti=String.valueOf(min);
		ore=String.valueOf(oreI);
		arrotondato=(ore+":"+minuti);
		return arrotondato;
	}


	public static float calcolaImporto(String tariffa, String totOre) {
		String ore= new String();
		String minuti= new String();
		
		ore=totOre.substring(0,totOre.indexOf("."));
		minuti=totOre.substring(totOre.indexOf(".")+1, totOre.length());
		
		
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
		
		return Float.valueOf(totOre)*Float.valueOf(tariffa);
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


	public static String elaboraCodiceGiustificativo(String oreStraordinario, String oreAssRecupero, String oreFerie,
			String orePermesso) {
		
		String codiceGiustificativo="";
		int contaG=0;
		
		if(Float.valueOf(oreStraordinario)!=0)
			contaG++;
		if(Float.valueOf(oreAssRecupero)!=0)
			contaG++;
		if(Float.valueOf(oreFerie)!=0)
			contaG++;
		if(Float.valueOf(orePermesso)!=0)
			contaG++;
					
		if(contaG==1){
			
			//if(Float.valueOf(oreStraordinario)!=0)
				//codiceGiustificativo="02"; ce ne sono troppi
			if(Float.valueOf(oreAssRecupero)!=0)
				if(Float.valueOf(oreAssRecupero)<0)
					codiceGiustificativo="09";
				else
					codiceGiustificativo="27";
			if(Float.valueOf(oreFerie)!=0)
				codiceGiustificativo="01";
			if(Float.valueOf(orePermesso)!=0)
				codiceGiustificativo="02";
				
		}
		else codiceGiustificativo="00";
		
		return codiceGiustificativo;	
	}
	
	
	public static String getOreCentesimi(String totOreCommMese) {	
		
		if(totOreCommMese.indexOf(".")!=-1)
		{	
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
		}else
			return totOreCommMese;
	}
	
}
