package gestione.pack.client.utility;

import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;

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
		if(ua==ia && ub<ib) return totale="errore: L'orario di uscita e' inferiore a quello d'ingresso!";
		
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
			
			a_1= Integer.parseInt(totale.substring(0,totale.indexOf(".")));
			b_1= Integer.parseInt(totale.substring(totale.indexOf(".")+1, totale.length()));
			
			a_2= Integer.parseInt(parziale.substring(0,parziale.indexOf(".")));
			b_2= Integer.parseInt(parziale.substring(parziale.indexOf(".")+1, parziale.length()));
			
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
			mese="Jan";
		if(month.compareTo("Febbraio") ==0)
			mese="Feb";
		if(month.compareTo("Marzo") ==0)
			mese="Mar";
		if(month.compareTo("Aprile") ==0)
			mese="Apr";
		if(month.compareTo("Maggio") ==0)
			mese="May";
		if(month.compareTo("Giugno") ==0)
			mese="Jun";
		if(month.compareTo("Luglio") ==0)
			mese="Jul";
		if(month.compareTo("Agosto") ==0)
			mese="Aug";
		if(month.compareTo("Settembre") ==0)
			mese="Sep";
		if(month.compareTo("Ottobre") ==0)
			mese="Oct";
		if(month.compareTo("Novembre") ==0)
			mese="Nov";
		if(month.compareTo("Dicembre") ==0)
			mese="Dec";
		
		return mese;
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
	
}
