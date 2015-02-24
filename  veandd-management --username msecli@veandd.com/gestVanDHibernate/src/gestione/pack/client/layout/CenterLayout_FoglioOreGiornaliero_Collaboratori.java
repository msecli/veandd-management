package gestione.pack.client.layout;


import gestione.pack.client.AdministrationService;
import gestione.pack.client.utility.MyImages;

//import gestione.pack.client.layout.CenterLayout_FoglioOreGiornalieroAutoTimb.FldsetGiustificativi;
import gestione.pack.client.layout.panel.DialogInvioCommenti;
import gestione.pack.client.layout.panel.FormInserimentoIntervalloCommessa;
import gestione.pack.client.layout.panel.PanelRiepilogoGiornalieroCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseGiornalieroCommesseHorizontal;
import gestione.pack.client.model.IntervalliCommesseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_FoglioOreGiornaliero_Collaboratori extends LayoutContainer {
	public CenterLayout_FoglioOreGiornaliero_Collaboratori() {
	
	}

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private LayoutContainer layoutContainer= new LayoutContainer();
	
	private TextField<String> txtfldUsername= new TextField<String>();
	private TextField<String> txtfldRuolo= new TextField<String>();

	private int statoRevisione=0;
	
	private Button btnConferma= new Button();		
	private Button btnInviaCommenti= new Button();
	private Button btnRiepilogoCommesse= new Button();
	
	public DateField dtfldGiorno= new DateField(); //settato al momento della creazione del form sul valore del datefield interno
												   //quando viene cambiata la data viene resettato anche questo 
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);   
	    	    
		final FitLayout fl= new FitLayout();
		
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);		
		
		txtfldUsername.setVisible(false);	
		
		try {
			selectLayout();
			
		} catch (Exception e) {
			Window.alert("error: Impossibile recuperare i dati in sessione.");
		}	
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
		
		bodyContainer.add(new CreateFormIntervalliOre());		
		bodyContainer.add(txtfldUsername);
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);			
	}
	
	
	public void selectLayout() {					
			BodyLayout_Dipendente lcDip = new BodyLayout_Dipendente();
			if (getParent().getParent().getParent().getParent().getClass().equals(lcDip.getClass())) {
				lcDip = (BodyLayout_Dipendente) getParent().getParent().getParent().getParent();
				txtfldUsername.setValue(lcDip.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("DIP");
			}			
	}


	public class CreateFormIntervalliOre extends FormPanel {
		
		private FormPanel frm= new FormPanel();
		public DateField giornoRiferimento= new DateField();
		private LayoutContainer left = new LayoutContainer();
		private LayoutContainer right = new LayoutContainer();	
		private LayoutContainer main = new LayoutContainer();	
				
		CreateFormIntervalliOre() {
			
			setLabelWidth(30);
			setFrame(true);
			setButtonAlign(HorizontalAlignment.CENTER);
			setHeading("Dettaglio Ore.");
			setHeaderVisible(false);
			setWidth(680);
			setHeight(480);
			setStyleAttribute("padding-left", "10px");
			setStyleAttribute("padding-top", "10px");
			setBorders(false);
			
			 Date retVal = null;
		        try
		        {
		            retVal = DateTimeFormat.getFormat( "dd-MM-yyyy" ).parse( 01+"-"+03+"-"+2013 );
		        }
		        catch ( Exception e )
		        {
		            retVal = null;
		        }
			
			giornoRiferimento.setValue(new Date());
			giornoRiferimento.setMinValue(retVal);
			giornoRiferimento.setItemId("data");
			dtfldGiorno.setValue(giornoRiferimento.getValue());
			
			ButtonBar buttonBarTop = new ButtonBar();
			buttonBarTop.setAlignment(HorizontalAlignment.CENTER);
			//buttonBarTop.setStyleAttribute("padding-bottom", "5px");
			buttonBarTop.setBorders(false);
			buttonBarTop.setWidth(570);
			buttonBarTop.setItemId("buttonBar");
				
			btnInviaCommenti.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.escl1()));
			btnInviaCommenti.setToolTip("Segnala eventuali problemi o anomalie.");
			btnInviaCommenti.setSize(26, 26);
			btnInviaCommenti.setIconAlign(IconAlign.TOP);
			btnInviaCommenti.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					Dialog d =new  DialogInvioCommenti(txtfldUsername.getValue().toString(), new Date());
					d.show();
					//Dialog d= new dlgProva();
					//d.show();			
				}
			});		
			
			btnRiepilogoCommesse.setEnabled(true);
			btnRiepilogoCommesse.setSize(26, 26);
			btnRiepilogoCommesse.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep_comm()));
			btnRiepilogoCommesse.setToolTip("Riepilogo Commesse");
			btnRiepilogoCommesse.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					Date data= dtfldGiorno.getValue();
					Dialog d= new Dialog();
					d.setHeaderVisible(true);
					d.setConstrain(false);
					d.setHeading("Riepilogo dettagliato (Commesse).");
					d.setSize(1500, 650);
					d.add(new PanelRiepilogoMeseGiornalieroCommesseHorizontal(0,txtfldUsername.getValue().toString(), data, "0.00", "0.00", "0.00", "0.00"));
					d.setButtons("");
					d.show();			
				}
			});			
			
			buttonBarTop.add(giornoRiferimento);
						
			frm.setHeaderVisible(false);
			frm.setBorders(false);
			frm.setItemId("formPanel");
			frm.setWidth(1060);
			frm.setHeight(890);
			frm.setStyleAttribute("padding-left", "0px");
			frm.setStyleAttribute("padding-top", "0px");
			frm.setScrollMode(Scroll.AUTO);
						
			main.setLayout(new ColumnLayout());
			main.setStyleAttribute("margin-top", "-10px");
			main.setBorders(false);
			main.setItemId("main");
	
			left.setStyleAttribute("padding-left", "10px");
			FormLayout layout = new FormLayout();
			left.setLayout(layout);
			left.setItemId("left");

			right.setStyleAttribute("padding-left", "30px");
			layout = new FormLayout();
			right.setLayout(layout);
			right.setItemId("right");
			
			ButtonBar btnBarOperazioni= new ButtonBar();
			btnBarOperazioni.setHeight(28);
			btnBarOperazioni.setAlignment(HorizontalAlignment.LEFT);
			btnBarOperazioni.setBorders(false);
			btnBarOperazioni.add(btnRiepilogoCommesse);
			btnBarOperazioni.add(btnInviaCommenti);
			btnBarOperazioni.add(btnConferma);
				
			
			left.add(btnBarOperazioni);
			left.add(buttonBarTop);			
			left.add(new FldsetIntervalliCommesse());

			main.add(left);
			main.add(right);
			
			frm.add(main);
			frm.layout(true);
			add(frm);
			
			//btnConferma.setSize(16, 46);
			btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.saveLittle()));
			btnConferma.setIconAlign(IconAlign.TOP);
			btnConferma.setToolTip("Conferma");
			btnConferma.setSize(26, 26);
			btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
				
				if(frm.isValid()){
					String username= new String();
					Date giorno=new Date();
					DateField dtfld= new DateField();
					
					username=txtfldUsername.getValue().toString();
					
					List<IntervalliCommesseModel> intervalliC= new ArrayList<IntervalliCommesseModel>();
															
					
					FldsetIntervalliCommesse fldSetIntervalliC;
					//FldsetGiustificativi fldSetGiustificativi;
					//FldsetRiepilogo fldSetRiepilogoTotale;
					LayoutContainer lc=new LayoutContainer();
					LayoutContainer lcR=new LayoutContainer();
					ButtonBar bttnBar= new ButtonBar();
					
					lc=(LayoutContainer) frm.getItemByItemId("main");
					//Intervalli IU
					lc=(LayoutContainer) lc.getItemByItemId("left");
								
					
					//Data
					bttnBar=(ButtonBar) lc.getItemByItemId("buttonBar");
					dtfld=(DateField) bttnBar.getItemByItemId("data");
					giorno=dtfld.getValue();
					
					//Intervalli Comm
					fldSetIntervalliC=(FldsetIntervalliCommesse) lc.getItemByItemId("fldSetIntervalliC");
					if(fldSetIntervalliC.getItemCount()>1)
						intervalliC=elaboraIntervalliC(fldSetIntervalliC);
					
					//Giustificativi
					lcR=(LayoutContainer)frm.getItemByItemId("main");
					lcR=(LayoutContainer)lcR.getItemByItemId("right");
					//fldSetGiustificativi=(FldsetGiustificativi) lcR.getItemByItemId("fldSetGiustificativi");
					
					
							AdministrationService.Util.getInstance().insertFoglioOreGiorno(username, giorno, intervalliC, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on insertFoglioOreGiorno()!");					
								}

								@Override
								public void onSuccess( Boolean result) {
									if(result){
										Window.alert("Caricamento avvenuto con successo.");
										reloadFoglioOre();
									}else{
										Window.alert("error: Impossibile salvare i dati!");
									}					
								}	
							});	
						
						
				 }			
			  }
			});
			
			giornoRiferimento.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {
				@Override
				public void handleEvent(DatePickerEvent be) {
					reloadFoglioOre();								
				}
			});
		}

		protected void reloadFoglioOre() {
			statoRevisione=0;
			dtfldGiorno.setValue(giornoRiferimento.getValue());
			
			ButtonBar buttonBarTop = new ButtonBar();
			buttonBarTop.setAlignment(HorizontalAlignment.CENTER);
			buttonBarTop.setStyleAttribute("padding-bottom", "5px");
			buttonBarTop.setBorders(false);
			buttonBarTop.setWidth(570);
			buttonBarTop.setItemId("buttonBar");

			buttonBarTop.add(giornoRiferimento);
			
			ButtonBar btnBarOperazioni= new ButtonBar();
			btnBarOperazioni.setAlignment(HorizontalAlignment.LEFT);
			btnBarOperazioni.setBorders(false);
			btnBarOperazioni.add(btnRiepilogoCommesse);
			btnBarOperazioni.add(btnInviaCommenti);
			btnBarOperazioni.add(btnConferma);
			
			left.removeAll();
			right.removeAll();
			
						
			left.add(btnBarOperazioni);
			left.add(buttonBarTop);
			left.add(new FldsetIntervalliCommesse());
			
			main.removeAll();
			main.add(left);
			main.add(right);
			main.layout(true);
			
			frm.layout(true);
		}			
	}
	
	
		
	
	public class FldsetIntervalliCommesse extends FieldSet {

		private FormInserimentoIntervalloCommessa frmInsCommesse = new FormInserimentoIntervalloCommessa("2");
		private Text txtNoCommesse = new Text();
		private ButtonBar buttonBar = new ButtonBar();
		private Date data= new Date();
		
		public FldsetIntervalliCommesse() {
			
			setItemId("fldSetIntervalliC");
			setBorders(true);
			setHeading("Dettaglio Commesse.");
			setHeight(365);
			setScrollMode(Scroll.AUTOY);
			setExpanded(true);
			setCollapsible(false);

			buttonBar.setAlignment(HorizontalAlignment.CENTER);
			buttonBar.setStyleAttribute("padding-top", "7px");
			buttonBar.setStyleAttribute("padding-bottom", "5px");
			buttonBar.setBorders(false);
			buttonBar.setWidth(570);
	
			txtNoCommesse.setVisible(false);

			buttonBar.add(txtNoCommesse);
			
			String u = new String();
			u = txtfldUsername.getValue();
			data= dtfldGiorno.getValue();			
			
			AdministrationService.Util.getInstance().getAssociazioniPersonaleCommessaByUsername(u, data,
							new AsyncCallback<List<IntervalliCommesseModel>>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on getAssociazioniPersonaleCommessaByUsername().");

								}

								@Override
								public void onSuccess(List<IntervalliCommesseModel> result) {
									
									caricaFieldSet(result);
			
								}				
							});
		}
		
		
		private void caricaFieldSet(List<IntervalliCommesseModel> result) {
			List<IntervalliCommesseModel> lista = new ArrayList<IntervalliCommesseModel>();
			String descrizioneCompleta= new String();
			Collections.sort(result, new Comparator<IntervalliCommesseModel>(){
				  public int compare(IntervalliCommesseModel s1, IntervalliCommesseModel s2) {
				    return s1.getNumeroCommessa().compareToIgnoreCase(s2.getNumeroCommessa());
				  }
				});
		
			lista.addAll(result);
			int size;
			size = lista.size();

			if (size < 0) {

				Window.alert("error: Impossibile accedere alla tabella AssociazioniDipendente;");

			} else {

				if (size == 0) {
					txtNoCommesse.setText("Nessuna Commessa Associata!");
					txtNoCommesse.setVisible(true);
					
				} else
					{
					removeAll();
			
					for (int i = 0; i < size; i++) {
					String num = String.valueOf(i);

					frmInsCommesse = new FormInserimentoIntervalloCommessa("2");
					frmInsCommesse.setItemId(num);

					frmInsCommesse.txtfldNumeroCommessa.setValue(result.get(i).getNumeroCommessa());
					frmInsCommesse.txtfldOreIntervallo.setValue(result.get(i).getOreLavoro());
					frmInsCommesse.txtfldOreViaggio.setValue(result.get(i).getOreViaggio());
					if(statoRevisione==1){
						frmInsCommesse.txtfldOreIntervallo.setEnabled(false);
						frmInsCommesse.txtfldOreViaggio.setEnabled(false);
					}
					
					frmInsCommesse.txtOreTotLavoro.setText("Totale nel Mese: "+result.get(i).getTotOreLavoro());
					frmInsCommesse.txtOreTotViaggio.setText("Totale nel Mese: "+result.get(i).getTotOreViaggio());
					
					descrizioneCompleta=result.get(i).getNumeroCommessa()+" ("+result.get(i).getDescrizione().toLowerCase()+") ";
					
					//frmInsCommesse.txtDescrizione.setText(result.get(i).getDescrizione().toLowerCase());
					frmInsCommesse.txtDescrizione.setText(descrizioneCompleta);
					add(frmInsCommesse);
			
					}
				}
				add(buttonBar);
				layout();
			}		
		}	
	}
	
	
	public List<IntervalliCommesseModel> elaboraIntervalliC(FldsetIntervalliCommesse fldSetIntervalliC) {
		
		//TextField<String> txtfldNumCommessa=new TextField<String>();
		TextField<String> txtfldOreLavoro=new TextField<String>();
		TextField<String> txtfldOreViaggio=new TextField<String>();
		TextField<String> txtfldOreStrao=new TextField<String>();
		Text txtDescrizione= new Text();
		FormInserimentoIntervalloCommessa frm=new FormInserimentoIntervalloCommessa("2");
		
		IntervalliCommesseModel intervallo;
		List<IntervalliCommesseModel>  intervalliC= new ArrayList<IntervalliCommesseModel>();
		String frmItemId= new String();
		
		String numeroCommessa;
		String descrizione;
		int i;
		
		i= fldSetIntervalliC.getItemCount();//numero di commesse registrate
		
		for(int j=1; j<i; j++)
		{
			frmItemId=String.valueOf(j-1);
			frm=(FormInserimentoIntervalloCommessa) fldSetIntervalliC.getItemByItemId(frmItemId);
						
			//txtfldNumCommessa=frm.txtfldNumeroCommessa;
			txtfldOreLavoro=frm.txtfldOreIntervallo;
			txtfldOreViaggio=frm.txtfldOreViaggio;
			txtfldOreStrao=frm.txtfldOreStrao;
			txtDescrizione=frm.txtDescrizione;
			
			descrizione=txtDescrizione.getText();
			numeroCommessa=descrizione.substring(0,descrizione.indexOf(" "));
			descrizione=descrizione.substring(descrizione.indexOf("(")+1, descrizione.indexOf(")"));
			
			intervallo= new IntervalliCommesseModel(numeroCommessa, txtfldOreLavoro.getValue().toString(), txtfldOreViaggio.getValue().toString(),
					txtfldOreStrao.getValue().toString(),"","", descrizione, "");
			intervalliC.add(intervallo);
		}
		
		return intervalliC;
	}
	
}





