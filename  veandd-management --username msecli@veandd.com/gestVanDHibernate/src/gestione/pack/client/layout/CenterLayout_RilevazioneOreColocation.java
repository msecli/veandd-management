package gestione.pack.client.layout;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.layout.panel.DialogInvioCommenti;
import gestione.pack.client.layout.panel.FormInserimentoIntervalloCommessa;
import gestione.pack.client.layout.panel.PanelRiepilogoGiornalieroCommesse;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
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

public class CenterLayout_RilevazioneOreColocation extends LayoutContainer{
	
	public CenterLayout_RilevazioneOreColocation(){
		
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private LayoutContainer layoutContainer= new LayoutContainer();
	
	private TextField<String> txtfldUsername= new TextField<String>();
	private TextField<String> txtfldRuolo= new TextField<String>();
	
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
		
		selectLayout();
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
						
		bodyContainer.add(new CreateFormIntervalliOre());
		bodyContainer.add(txtfldUsername);

		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);	
	}
	
	
	public void selectLayout() {
			
			BodyLayout_PersonalManager lcPM = new BodyLayout_PersonalManager();		
			if (getParent().getParent().getParent().getParent().getClass().equals(lcPM.getClass())) {
				lcPM = (BodyLayout_PersonalManager) getParent().getParent()	.getParent().getParent();
				txtfldUsername.setValue(lcPM.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("PM");
			}
			
			BodyLayout_Administration lcAMM = new BodyLayout_Administration();
			if (getParent().getParent().getParent().getParent().getClass().equals(lcAMM.getClass())) {
				lcAMM = (BodyLayout_Administration) getParent().getParent().getParent().getParent();
				txtfldUsername.setValue(lcAMM.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("AMM");
			}
			
			BodyLayout_GestionePersonale lcGP = new BodyLayout_GestionePersonale();
			if (getParent().getParent().getParent().getParent().getClass().equals(lcGP.getClass())) {
				lcGP = (BodyLayout_GestionePersonale) getParent().getParent().getParent().getParent();
				txtfldUsername.setValue(lcGP.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("GP");
			}
			/*
			BodyLayout_UffAmministrazione lcUA = new BodyLayout_UffAmministrazione();
			if (getParent().getParent().getParent().getParent().getClass().equals(lcUA.getClass())) {
				lcUA = (BodyLayout_UffAmministrazione) getParent().getParent().getParent().getParent();
				txtfldUsername.setValue(lcUA.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("UA");
			}
			*/
			BodyLayout_Direzione lcDir = new BodyLayout_Direzione();
			if (getParent().getParent().getParent().getParent().getClass().equals(lcDir.getClass())) {
				lcDir = (BodyLayout_Direzione) getParent().getParent().getParent().getParent();
				txtfldUsername.setValue(lcDir.txtfldUsername.getValue().toString());
				txtfldRuolo.setValue("DIP");
			}
	}


	public class CreateFormIntervalliOre extends FormPanel {
		
		private FormPanel frm= new FormPanel();
		public DateField giornoRiferimento= new DateField();
		//private SimpleComboBox<String> smplcmbxDipendente= new SimpleComboBox<String>();
		private ComboBox<PersonaleModel> cmbxDipendente= new ComboBox<PersonaleModel>();
		private Button btnSend= new Button(); 
		private LayoutContainer left = new LayoutContainer();
		private LayoutContainer right = new LayoutContainer();	
		private LayoutContainer main = new LayoutContainer();	
				
		CreateFormIntervalliOre() {
			
			setLabelWidth(30);
			setFrame(true);
			setButtonAlign(HorizontalAlignment.CENTER);
			setHeading("Dettaglio Ore.");
			setHeaderVisible(false);
			setWidth(700);
			setHeight(95);
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
					
			ListStore<PersonaleModel> store=new ListStore<PersonaleModel>();
			cmbxDipendente.setStore(store);
			cmbxDipendente.setFieldLabel("Dipendente");
			cmbxDipendente.setEnabled(true);
			cmbxDipendente.setEmptyText("Selezionare il dipendente..");
			cmbxDipendente.setEditable(false);
			cmbxDipendente.setVisible(true);
			cmbxDipendente.setTriggerAction(TriggerAction.ALL);
			cmbxDipendente.setAllowBlank(false);
			cmbxDipendente.setDisplayField("nomeCompleto");
			cmbxDipendente.addListener(Events.OnClick, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {					
						getAllDipendenti();					
				}		
			});
						
			ButtonBar buttonBarTop = new ButtonBar();
			buttonBarTop.setAlignment(HorizontalAlignment.CENTER);
			buttonBarTop.setStyleAttribute("padding-bottom", "5px");
			buttonBarTop.setBorders(false);
			buttonBarTop.setWidth(570);
			buttonBarTop.setItemId("buttonBar");

			//buttonBarTop.add(btnPrev);
			buttonBarTop.add(giornoRiferimento);
			buttonBarTop.add(cmbxDipendente);
			buttonBarTop.add(btnSend);
			//buttonBarTop.add(btnNext);
			
								
			btnInviaCommenti.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.escl1()));
			btnInviaCommenti.setToolTip("Segnala eventuali problemi o anomalie.");
			btnInviaCommenti.setSize(26, 26);
			btnInviaCommenti.setIconAlign(IconAlign.TOP);
			btnInviaCommenti.addSelectionListener(new SelectionListener<ButtonEvent>() {			
				@Override
				public void componentSelected(ButtonEvent ce) {
					Dialog d =new  DialogInvioCommenti(txtfldUsername.getValue().toString());
					d.show();
					
				}
			});
			
			btnRiepilogoCommesse.setEnabled(true);
			btnRiepilogoCommesse.setSize(26, 26);
			btnRiepilogoCommesse.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep_comm()));
			btnRiepilogoCommesse.setToolTip("Riepilogo Commesse");
			btnRiepilogoCommesse.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					Dialog d= new Dialog();
					d.setHeaderVisible(true);
					d.setHeading("Riepilogo dettagliato (Commesse).");
					d.setSize(550, 605);
					d.add(new PanelRiepilogoGiornalieroCommesse(txtfldUsername.getValue().toString(), giornoRiferimento.getValue()));
					d.setButtons("");
					d.show();			
				}
			});
			
			
			frm.setHeaderVisible(false);
			frm.setBorders(false);
			frm.setItemId("formPanel");
			frm.setWidth(1060);
			frm.setHeight(890);
			frm.setStyleAttribute("padding-left", "0px");
			frm.setStyleAttribute("padding-top", "0px");
			frm.setScrollMode(Scroll.AUTO);
						
			main.setLayout(new ColumnLayout());
			main.setBorders(false);
			main.setStyleAttribute("margin-top", "-10px");
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

			main.add(left);
			frm.add(main);	
			frm.layout(true);
			add(frm);
			
			btnSend.setSize(26, 26);
			btnSend.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
			btnSend.setToolTip("Carica Dati");
			btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {				
				@Override
				public void componentSelected(ButtonEvent ce) {
						txtfldUsername.setValue(cmbxDipendente.getValue().get("username").toString()); //setto il valore globale dell'username per il caricamento dei vari fieldset
						dtfldGiorno.setValue(giornoRiferimento.getValue());//setto il valore globale della data con la data selezionata
						reloadFoglioOre();													
				}
			});		

			//btnConferma.setSize(46, 46);
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
						
						username=cmbxDipendente.getValue().get("username");
						List<IntervalliCommesseModel> intervalliC= new ArrayList<IntervalliCommesseModel>();
																
						
						FldsetIntervalliCommesse fldSetIntervalliC;
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
						
						//txtfldOreTotali=fldSetGiustificativi.txtfldOreTotEffettive;
						
						 
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
		}
		
		
		protected void reloadFoglioOre() {
			
			setHeight(480);
			setWidth(680);
			
			dtfldGiorno.setValue(giornoRiferimento.getValue());
			
			cmbxDipendente.setFieldLabel("Dipendente");
			cmbxDipendente.setEnabled(true);
			cmbxDipendente.setEmptyText("Selezionare il dipendente..");
			cmbxDipendente.setEditable(false);
			cmbxDipendente.setVisible(true);
			cmbxDipendente.setTriggerAction(TriggerAction.ALL);
			cmbxDipendente.setAllowBlank(false);
			cmbxDipendente.setDisplayField("nomeCompleto");
			cmbxDipendente.addListener(Events.OnClick, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {					
						getAllDipendenti();					
				}		
			});
			
			ButtonBar buttonBarTop = new ButtonBar();
			buttonBarTop.setAlignment(HorizontalAlignment.CENTER);
			buttonBarTop.setStyleAttribute("padding-bottom", "5px");
			buttonBarTop.setBorders(false);
			buttonBarTop.setWidth(570);
			buttonBarTop.setItemId("buttonBar");

			buttonBarTop.add(giornoRiferimento);
			buttonBarTop.add(cmbxDipendente);
			buttonBarTop.add(btnSend);
			
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
			//left.add(new FldsetIntervalliIU());
			left.add(new FldsetIntervalliCommesse());

			//right.add(new FldsetGiustificativi());
			//right.add(new FldsetRiepilogo());
			
			main.removeAll();
			main.add(left);
			main.add(right);
			main.layout(true);
					
			frm.layout(true);			
		}			
		
		
		private void getAllDipendenti() {	
			AdministrationService.Util.getInstance().getListaDipendentiModel(txtfldRuolo.getValue().toString(), new AsyncCallback<List<PersonaleModel>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getCommesseByPM();");
					caught.printStackTrace();		
				}

				@Override
				public void onSuccess(List<PersonaleModel> result) {
					if(result!=null){		
						ListStore<PersonaleModel> lista= new ListStore<PersonaleModel>();
						lista.setStoreSorter(new StoreSorter<PersonaleModel>());  
						lista.setDefaultSort("nomeCompleto", SortDir.ASC);
						
						lista.add(result);				
						cmbxDipendente.clear();
						cmbxDipendente.setStore(lista);
						
					}else Window.alert("error: Errore durante l'accesso ai dati Personale.");				
				}
			});
		}	
	}
	
	
		
	public class FldsetIntervalliCommesse extends FieldSet {

		private FormInserimentoIntervalloCommessa frmInsCommesse = new FormInserimentoIntervalloCommessa();
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
									Window.alert("Errore di connessione on getAssociazioniPersonaleCommessa().");

								}

								@Override
								public void onSuccess(List<IntervalliCommesseModel> result) {
									caricaFieldSet(result);
			
								}				
							});
		}
		
		
		private void caricaFieldSet(List<IntervalliCommesseModel> result) {
			List<IntervalliCommesseModel> lista = new ArrayList<IntervalliCommesseModel>();
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

					frmInsCommesse = new FormInserimentoIntervalloCommessa();
					frmInsCommesse.setItemId(num);

					frmInsCommesse.txtfldNumeroCommessa.setValue(result.get(i).getNumeroCommessa());
					frmInsCommesse.txtfldOreIntervallo.setValue(result.get(i).getOreLavoro());
					frmInsCommesse.txtfldOreViaggio.setValue(result.get(i).getOreViaggio());
					frmInsCommesse.txtfldTotOreLavoro.setValue(result.get(i).getTotOreLavoro());
					frmInsCommesse.txtfldTotOreViaggio.setValue(result.get(i).getTotOreViaggio());
					frmInsCommesse.txtDescrizione.setText("("+result.get(i).getDescrizione()+")");
					add(frmInsCommesse);
			
					}
				}

				add(buttonBar);
				layout();
			}		
		}	
	}
	
	
	public List<IntervalliCommesseModel> elaboraIntervalliC(FldsetIntervalliCommesse fldSetIntervalliC) {
		
		TextField<String> txtfldNumCommessa=new TextField<String>();
		TextField<String> txtfldOreLavoro=new TextField<String>();
		TextField<String> txtfldOreViaggio=new TextField<String>();
		Text txtDescrizione= new Text();
		FormInserimentoIntervalloCommessa frm=new FormInserimentoIntervalloCommessa();
		
		IntervalliCommesseModel intervallo;
		List<IntervalliCommesseModel>  intervalliC= new ArrayList<IntervalliCommesseModel>();
		String frmItemId= new String();
		
		int i;
		
		i= fldSetIntervalliC.getItemCount();//numero di commesse registrate
		
		for(int j=1; j<i; j++)
		{
			frmItemId=String.valueOf(j-1);
			frm=(FormInserimentoIntervalloCommessa) fldSetIntervalliC.getItemByItemId(frmItemId);
			
			txtfldNumCommessa=frm.txtfldNumeroCommessa;
			txtfldOreLavoro=frm.txtfldOreIntervallo;
			txtfldOreViaggio=frm.txtfldOreViaggio;
			txtDescrizione=frm.txtDescrizione;
			intervallo= new IntervalliCommesseModel(txtfldNumCommessa.getValue().toString(), txtfldOreLavoro.getValue().toString(), txtfldOreViaggio.getValue().toString()
					,"","", txtDescrizione.toString());
			intervalliC.add(intervallo);
		}
		
		return intervalliC;
	}
		
}
