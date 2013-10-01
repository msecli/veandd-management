package gestione.pack.client.layout;

import gestione.pack.client.AdministrationService;

import gestione.pack.client.layout.panel.DialogInvioCommenti;
import gestione.pack.client.layout.panel.FormInserimentoIntervalloCommessa;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseFoglioOre;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.TimbraturaModel;

import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_FoglioOreSelectDipendenti extends LayoutContainer {
	public CenterLayout_FoglioOreSelectDipendenti() {
	
	}
		
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private LayoutContainer layoutContainer= new LayoutContainer();
	
	private TextField<String> txtfldUsername= new TextField<String>();
	private TextField<String> txtfldRuolo= new TextField<String>();
	
	private Button btnConferma= new Button();		
	private Button btnMostraIntervalli= new Button();
	private Button btnInviaCommenti= new Button();
	//private Button btnPrint= new Button();
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

		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
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
		private PanelRiepilogoMeseFoglioOre pnlRiepilogo;//= new PanelRiepilogoMeseFoglioOre(txtfldUsername.getValue().toString(), giornoRiferimento.getValue());
		
		CreateFormIntervalliOre() {
			
			setLabelWidth(30);
			setFrame(true);
			setButtonAlign(HorizontalAlignment.CENTER);
			setHeading("Dettaglio Ore.");
			setHeaderVisible(false);
			setWidth(700);
			setHeight(90);
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
			cmbxDipendente.setEditable(true);
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
			
			btnMostraIntervalli.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
			ToolTipConfig config= new ToolTipConfig();
			config.setTitle("Dati rilevati dalla timbratrice:");
			config.setCloseable(true);
			config.setAnchor("left");
			config.setMouseOffset(new int[]{0, -5});
			btnMostraIntervalli.setToolTip(config);
			btnMostraIntervalli.setSize(26, 26);
			btnMostraIntervalli.setIconAlign(IconAlign.TOP);
			
			
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
			
			frm.setHeaderVisible(false);
			frm.setBorders(false);
			frm.setItemId("formPanel");
			frm.setWidth(1160);
			frm.setHeight(1050);
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
			btnBarOperazioni.add(btnMostraIntervalli);
			btnBarOperazioni.add(btnInviaCommenti);
			btnBarOperazioni.add(btnConferma);
			//btnBarOperazioni.add(frmSubmit);
			
			left.add(btnBarOperazioni);
			left.add(buttonBarTop);

			main.add(left);
			frm.add(main);	
			frm.add(new PanelRiepilogoMeseFoglioOre(txtfldUsername.getValue().toString(), giornoRiferimento.getValue(),""));
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
						//String controlloDati= new String(); //quando viene effettuato il controllo dei dati inseriti restituisce OK o errori
						
						username=cmbxDipendente.getValue().get("username");
						
						TextField<String> txtfldTotGenerale=new TextField<String>();
						TextField<String> txtfldDeltaGiorno=new TextField<String>();
						TextField<String> txtfldFerie=new TextField<String>();
						TextField<String> txtfldPermesso=new TextField<String>();
						TextField<String> txtfldStraordinario=new TextField<String>();
						TextField<String> txtfldAbbuono= new TextField<String>();
						TextField<String> txtfldRecupero=new TextField<String>();
						TextField<String> txtfldRecuperoTotale= new TextField<String>();
						TextField<String> txtfldOreViaggio= new TextField<String>();
						TextField<String> txtfldDeltaOreViaggio= new TextField<String>();
						//TextField<String> txtfldOreTotali= new TextField<String>();
						SimpleComboBox<String> smplcmbxGiustificativo=new SimpleComboBox <String>();
						TextArea txtrNote= new TextArea();
											
						List<String> intervalliIU= new ArrayList<String>();
						List<IntervalliCommesseModel> intervalliC= new ArrayList<IntervalliCommesseModel>();
																
						FldsetIntervalliIU fldSetIntervalliIU;
						FldsetIntervalliCommesse fldSetIntervalliC;
						FldsetGiustificativi fldSetGiustificativi;
						FldsetRiepilogo fldSetRiepilogoTotale;
						LayoutContainer lc=new LayoutContainer();
						LayoutContainer lcR=new LayoutContainer();
						ButtonBar bttnBar= new ButtonBar();
						
						lc=(LayoutContainer) frm.getItemByItemId("main");
						//Intervalli IU
						lc=(LayoutContainer) lc.getItemByItemId("left");
						fldSetIntervalliIU=(FldsetIntervalliIU) lc.getItemByItemId("fldSetIntervalliIU");							
						intervalliIU=elaboraIntervalliIU(fldSetIntervalliIU);
						
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
						fldSetGiustificativi=(FldsetGiustificativi) lcR.getItemByItemId("fldSetGiustificativi");
						
						fldSetRiepilogoTotale=(FldsetRiepilogo) lcR.getItemByItemId("fldSetRiepilogo");
						
						txtfldTotGenerale=fldSetGiustificativi.txtfldTotGenerale;
						txtfldDeltaGiorno=fldSetGiustificativi.txtfldOreDelta;
						txtfldFerie=fldSetGiustificativi.txtfldFerie;
						txtfldPermesso=fldSetGiustificativi.txtfldPermesso;
						txtfldStraordinario=fldSetGiustificativi.txtfldStraordinario;
						txtfldAbbuono=fldSetGiustificativi.txtfldAbbuono;
						txtfldRecupero=fldSetGiustificativi.txtfldRecupero;
						smplcmbxGiustificativo=fldSetGiustificativi.smplcmbxAltroGiustificativo;
						txtfldOreViaggio=fldSetGiustificativi.txtfldOreViaggio;
						txtfldDeltaOreViaggio=fldSetGiustificativi.txtfldDeltaViaggio;
						txtfldRecuperoTotale=fldSetRiepilogoTotale.txtfldOreRecuperoMonteTotale;
						txtrNote=fldSetGiustificativi.txtrNoteAggiuntive;
						//txtfldOreTotali=fldSetGiustificativi.txtfldOreTotEffettive;
						
						String totOreGenerale=txtfldTotGenerale.getValue().toString();
						String delta=txtfldDeltaGiorno.getValue().toString();
						String oreViaggio=txtfldOreViaggio.getValue().toString();
						String deltaOreViaggio=txtfldDeltaOreViaggio.getValue().toString();
						String oreStraordinario=txtfldStraordinario.getValue().toString();
						String oreAssRecupero=txtfldRecupero.getValue().toString();
						String oreFerie=txtfldFerie.getValue().toString();
						String orePermesso=txtfldPermesso.getValue().toString();
						String oreAbbuono=txtfldAbbuono.getValue().toString();
						String noteAggiuntive="";
						String oreRecuperoTot= txtfldRecuperoTotale.getValue().toString();
						String giustificativo=new String();
											
						if(!smplcmbxGiustificativo.getRawValue().isEmpty())
							giustificativo=smplcmbxGiustificativo.getRawValue().toString();
						else giustificativo="";
						if(!txtrNote.getRawValue().isEmpty())
							noteAggiuntive=txtrNote.getValue().toString();
						
						 
						 AdministrationService.Util.getInstance().insertFoglioOreGiorno(username, giorno, totOreGenerale, delta, oreViaggio, oreAssRecupero, deltaOreViaggio, 
								 giustificativo, oreStraordinario, oreFerie, orePermesso, "0", oreAbbuono, intervalliIU, intervalliC, oreRecuperoTot, noteAggiuntive, new AsyncCallback<Boolean>() {

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
			
			setHeight(1080);
			setWidth(1170);
			
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
			
			btnMostraIntervalli.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
			ToolTipConfig config= new ToolTipConfig();
			config.setTitle("Dati rilevati dalla timbratrice:");
			config.setCloseable(true);
			config.setAnchor("left");
			config.setMouseOffset(new int[]{0, -5});
			config.setMinWidth(150);
			btnMostraIntervalli.setToolTip(config);		

			ButtonBar btnBarOperazioni= new ButtonBar();
			btnBarOperazioni.setAlignment(HorizontalAlignment.LEFT);
			btnBarOperazioni.setBorders(false);
			btnBarOperazioni.add(btnMostraIntervalli);
			btnBarOperazioni.add(btnInviaCommenti);
			btnBarOperazioni.add(btnConferma);
			
			left.removeAll();
			right.removeAll();
			
			left.add(btnBarOperazioni);
			left.add(buttonBarTop);
			left.add(new FldsetIntervalliIU());
			left.add(new FldsetIntervalliCommesse());

			right.add(new FldsetGiustificativi());
			right.add(new FldsetRiepilogo());
			
			main.removeAll();
			main.add(left);
			main.add(right);
			main.layout(true);
			
			String user=cmbxDipendente.getValue().getUsername();
			
			pnlRiepilogo=new PanelRiepilogoMeseFoglioOre(user, giornoRiferimento.getValue(),"");
			frm.getItemByItemId("pnlRiepilogo").removeFromParent();
			frm.add(pnlRiepilogo);
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
	
	public class FldsetIntervalliIU extends FieldSet {
						
		private TextField<String> txtfld1I = new TextField<String>();
		private TextField<String> txtfld1U = new TextField<String>();
		private TextField<String> txtfld2I = new TextField<String>();
		private TextField<String> txtfld2U = new TextField<String>();
		private TextField<String> txtfld3I = new TextField<String>();
		private TextField<String> txtfld3U = new TextField<String>();
		private TextField<String> txtfld4I = new TextField<String>();
		private TextField<String> txtfld4U = new TextField<String>();
		private TextField<String> txtfld5I = new TextField<String>();
		private TextField<String> txtfld5U = new TextField<String>();
		private TextField<String> txtfldSomma1= new TextField<String>();
		private TextField<String> txtfldSomma2= new TextField<String>();
		private TextField<String> txtfldSomma3= new TextField<String>();
		private TextField<String> txtfldSomma4= new TextField<String>();
		private TextField<String> txtfldSomma5= new TextField<String>();
		
		private String isNew= new String();
		
		private String username= new String();
		private Date data= new Date();
		private String d= new String();
		
		private Text txtErrore= new Text();
		
		public FldsetIntervalliIU() {		
			username=txtfldUsername.getValue().toString();
			data=dtfldGiorno.getValue();
			d=data.toString().substring(0,3);
			
			AdministrationService.Util.getInstance().loadIntervalliIU(username, data, new AsyncCallback<List<IntervalliIUModel>>() {
				
				@Override
				public void onSuccess(List<IntervalliIUModel> result) {
					if(result.equals(null))
						Window.alert("error: Impossibile accedere ai dati sugli orari.");
					else
					if(result.size()==1)
						load("new",result);
						else load("old",result);
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on loadIntervalliIU();");
				}
			});
		}
		
		private void load(String tipo, List<IntervalliIUModel> result) {
			
			setBorders(true);
			setHeading("Dettaglio Giornaliero.");
			setItemId("fldSetIntervalliIU");
			
			isNew=tipo;
			
			LayoutContainer layoutCol1 = new LayoutContainer();
			layoutCol1.setItemId("lay1");
			LayoutContainer layoutCol2 = new LayoutContainer();
			LayoutContainer layoutCol3 = new LayoutContainer();
			LayoutContainer layoutCol4 = new LayoutContainer();
			LayoutContainer layoutCol5 = new LayoutContainer();

			ContentPanel cp = new ContentPanel();
			cp.setItemId("cp");
			cp.setHeaderVisible(false);
			cp.setSize(580, 85);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setStyleAttribute("padding-top", "15px");
			
			txtErrore.setVisible(false);					
				
			txtfld1I.setItemId("1I");
			txtfld1I.setFieldLabel("I");
			txtfld1I.setMaxLength(5);
			txtfld1I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld1I.getMessages().setRegexText("Deve essere nel formato 23:59.");
		    txtfld1I.addKeyListener(new KeyListener() {
				
				  public void componentKeyUp(ComponentEvent event) {		    		   
			    	  //carico il fldSet riferito ai giustificativi
			    	  LayoutContainer lc= new LayoutContainer(); 
			    	  LayoutContainer right= new LayoutContainer();
		   			  FldsetGiustificativi fldsetGiustificativo;
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  right=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) right.getItemByItemId("fldSetGiustificativi");
			    	 		   			  
			    	  if(hasValue(txtfld1I)){ //se 1I è corretto
			    		   txtfld1I.clearInvalid();
			    		   txtfld1I.setData("sorgente", "DIP");
		    			   txtfld1U.setAllowBlank(false); //1U deve essere compilato
		    			   if(hasValue(txtfld1U)){ //se 1U è corretto ricavo il parziale della differenza tra gli intervalli
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString());
					    	   
					    	   if(sommaIntervalli.length()>5){//restituisce un errore se l'uscita è inferiore all'ingresso
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld1I);					    		   
					    	   }
					    	   else{	//se l'intervallo è corretto aggiorno il totale giornaliero 
					    		   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		   
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		  
					    		   txtfldSomma1.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);    		   
					    	   }
		    			   } 
		    		   } else { //se modifico 1I aggiorno il valore del totale generale azzerando il valore dell'intervallo
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
			    		   txtfldSomma1.setValue("0.0");
			    		  
			    		   txtfld1U.setAllowBlank(true);
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    		   
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		  
			    		   String delta=new String();
		    			   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   			    		   
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);	   
		    		   }
			      }
			      
			      //Uso il TAB per un auto completamento dei campi ore
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld1I.getValue()==null)
							txtfld1I.setValue("0:00");
						else{
							String valore= txtfld1I.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
									txtfld1I.setValue(valore);		
							}	
							LayoutContainer lc= new LayoutContainer(); 
					    	  LayoutContainer right= new LayoutContainer();
				   			  FldsetGiustificativi fldsetGiustificativo;
				   			  lc=(LayoutContainer) getParent().getParent();
				   			  right=(LayoutContainer) lc.getItemByItemId("right");
				   			  fldsetGiustificativo=(FldsetGiustificativi) right.getItemByItemId("fldSetGiustificativi");
					    	 		   			  
					    	  if(hasValue(txtfld1I)){ //se 1I è corretto
					    		   txtfld1I.clearInvalid();
					    		   txtfld1I.setData("sorgente", "DIP");
				    			   txtfld1U.setAllowBlank(false); //1U deve essere compilato
				    			   if(hasValue(txtfld1U)){ //se 1U è corretto ricavo il parziale della differenza tra gli intervalli
				    				   String sommaIntervalli= new String();  		   
							    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString());
							    	   
							    	   if(sommaIntervalli.length()>5){//restituisce un errore se l'uscita è inferiore all'ingresso
							    		   txtErrore.setVisible(true);
							    		   txtErrore.setText(sommaIntervalli);
							    		   btnConferma.setEnabled(false);
							    		   disableField(txtfld1I);					    		   
							    	   }
							    	   else{	//se l'intervallo è corretto aggiorno il totale giornaliero 
							    		   
							    		   String totale= new String();
							    		   String delta= new String();
							    		   List<String> listaParziali= new ArrayList<String>();
							    		   
							    		   btnConferma.setEnabled(true);
							    		   txtErrore.setVisible(false);
							    		   enableField();
							    		  
							    		   txtfldSomma1.setValue(sommaIntervalli);
							    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
							    		   listaParziali.add(txtfldSomma2.getValue().toString());
							    		   listaParziali.add(txtfldSomma3.getValue().toString());
							    		   listaParziali.add(txtfldSomma4.getValue().toString());
							    		   listaParziali.add(txtfldSomma5.getValue().toString());
							    	   
							    		   totale=ClientUtility.calcolaTempo(listaParziali);
							    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
							    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
							    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
							    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
							    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
							    		   else
							    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
							    		   
							    		   setFieldGiustificativo(fldsetGiustificativo, delta);    		   
							    	   }
				    			   } 
				    		   } else { //se modifico 1I aggiorno il valore del totale generale azzerando il valore dell'intervallo
				    			   List<String> listaParziali= new ArrayList<String>();
				    			   String totale= new String();
					    		   txtfldSomma1.setValue("0.0");
					    		  
					    		   txtfld1U.setAllowBlank(true);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    		   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		  
					    		   String delta=new String();
				    			   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
					    		   if(totale.compareTo("0.00")!=0){
					    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    			   setFieldGiustificativo(fldsetGiustificativo, delta);
					    		   }else enableFieldGiustificativo(fldsetGiustificativo);
					    		   			    		   
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);	   
				    		   }
						}
			      }    			      
			 });		  
			
		    
			txtfld1U.setItemId("1U");
			txtfld1U.setFieldLabel("U");
			txtfld1U.setMaxLength(5);
			txtfld1U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld1U.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld1U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {		
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld1U)){
			    		  txtfld1U.clearInvalid();
			    		  txtfld1U.setData("sorgente", "DIP");
		    			   txtfld1I.setAllowBlank(false);
		    			   if(hasValue(txtfld1I)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld1U);				    		   
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		   
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();					    		   
					    		   txtfldSomma1.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);	  	
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	   
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			   } 
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld1I.setAllowBlank(true);
			    		   txtfldSomma1.setValue("0.0");
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    		   
			    		   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());

			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   
		    		   }
			    	 }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld1U.getValue()==null)
							txtfld1U.setValue("0:00");
						else{
							String valore= txtfld1U.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld1U.setValue(valore);
							
						}
						 LayoutContainer lc= new LayoutContainer(); 
			   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			  lc=(LayoutContainer) getParent().getParent();
			   			  lc=(LayoutContainer) lc.getItemByItemId("right");
			   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
				    	  
				    	  if(hasValue(txtfld1U)){
				    		  txtfld1U.clearInvalid();
				    		  txtfld1U.setData("sorgente", "DIP");
			    			   txtfld1I.setAllowBlank(false);
			    			   if(hasValue(txtfld1I)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld1U);				    		   
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		   
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();					    		   
						    		   txtfldSomma1.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);	  	
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	   
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			   } 
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld1I.setAllowBlank(true);
				    		   txtfldSomma1.setValue("0.0");
				    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    		   
				    		   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());

				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    		   
			    		   }
					}	    		
			      }
				 });

			txtfld2I.setItemId("2I");
			txtfld2I.setFieldLabel("I");
			txtfld2I.setMaxLength(5);
			txtfld2I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld2I.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld2I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld2I)){
			    		  txtfld2I.clearInvalid();
			    		  txtfld2I.setData("sorgente", "DIP");
			    		  //controllo che il valore di 2I sia maggiore dell'intervallo precedente
			    		  if(!isMax(txtfld2I, txtfld1I)|| !isMax(txtfld2I, txtfld1U)){
			    			  txtErrore.setVisible(true);
			    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
			    			  btnConferma.setEnabled(false);
			    			  disableField(txtfld2I);
			    			  
			    		  }else{
			    			  
			    		  txtErrore.setVisible(false);  		    			  
			    		  txtfld2U.setAllowBlank(false);
			    		  enableField();
			    		  
		    			  if(hasValue(txtfld2U)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld2I);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		   
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma2.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	 
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    		   
					    	   }
		    			     } 
		    			  }
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
			    		   txtfldSomma2.setValue("0.0");
			    		   txtfld2U.setAllowBlank(true);
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());

			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   
		    		   }
		    	  }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld2I.getValue()==null)
							txtfld2I.setValue("0:00");
						else{
							String valore= txtfld2I.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld2I.setValue(valore);
						}
						 LayoutContainer lc= new LayoutContainer(); 
			   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			  lc=(LayoutContainer) getParent().getParent();
			   			  lc=(LayoutContainer) lc.getItemByItemId("right");
			   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
				    	  
				    	  if(hasValue(txtfld2I)){
				    		  txtfld2I.clearInvalid();
				    		  txtfld2I.setData("sorgente", "DIP");
				    		  //controllo che il valore di 2I sia maggiore dell'intervallo precedente
				    		  if(!isMax(txtfld2I, txtfld1I)|| !isMax(txtfld2I, txtfld1U)){
				    			  txtErrore.setVisible(true);
				    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
				    			  btnConferma.setEnabled(false);
				    			  disableField(txtfld2I);
				    			  
				    		  }else{
				    			  
				    		  txtErrore.setVisible(false);  		    			  
				    		  txtfld2U.setAllowBlank(false);
				    		  enableField();
				    		  
			    			  if(hasValue(txtfld2U)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld2I);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		   
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma2.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	 
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    		   
						    	   }
			    			     } 
			    			  }
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
				    		   txtfldSomma2.setValue("0.0");
				    		   txtfld2U.setAllowBlank(true);
				    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());

				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    		   
			    		   }
					}	    		
			      }
			});
			
			txtfld2U.setItemId("2U");
			txtfld2U.setFieldLabel("U");
			txtfld2U.setMaxLength(5);
			txtfld2U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld2U.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld2U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld2U)){
			    		  txtfld2U.clearInvalid();
			    		  txtfld2U.setData("sorgente", "DIP");
		    			   txtfld2I.setAllowBlank(false);
		    			   if(hasValue(txtfld2I)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld2U);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma2.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	  
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			   } 
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld2I.setAllowBlank(true);
		    			   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    		   txtfldSomma2.setValue("0.0");    		   
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());

			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    	  }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld2U.getValue()==null)
							txtfld2U.setValue("0:00");
						else{
							String valore= txtfld2U.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld2U.setValue(valore);
						}
						LayoutContainer lc= new LayoutContainer(); 
			   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			  lc=(LayoutContainer) getParent().getParent();
			   			  lc=(LayoutContainer) lc.getItemByItemId("right");
			   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			   			   
				    	  if(hasValue(txtfld2U)){
				    		  txtfld2U.clearInvalid();
				    		  txtfld2U.setData("sorgente", "DIP");
			    			   txtfld2I.setAllowBlank(false);
			    			   if(hasValue(txtfld2I)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld2U);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		  
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma2.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	  
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			   } 
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld2I.setAllowBlank(true);
			    			   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    		   txtfldSomma2.setValue("0.0");    		   
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());

				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    	  }
					}	    		
			      }
			 });

			txtfld3I.setItemId("3I");
			txtfld3I.setFieldLabel("I");
			txtfld3I.setMaxLength(5);
			txtfld3I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld3I.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld3I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  if(hasValue(txtfld3I)){	
			    		  txtfld3I.clearInvalid();
			    		  txtfld3I.setData("sorgente", "DIP");
			    		  if(!isMax(txtfld3I, txtfld2I)|| !isMax(txtfld3I, txtfld2U)){
			    			  txtErrore.setVisible(true);
			    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
			    			  btnConferma.setEnabled(false);
			    			  disableField(txtfld3I);
			    		  }else{		    		  
			    		  txtfld3U.setAllowBlank(false);
			    		  txtErrore.setVisible(false);
			    		  enableField();
		    			   if(hasValue(txtfld3U)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld3I);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma3.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	   
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			     } 
		    			   }
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld3U.setAllowBlank(true);
		    		       txtfldSomma3.setValue("0.0");
		    		       delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());

			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);			    		  
		    		   }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld3I.getValue()==null)
							txtfld3I.setValue("0:00");
						else{
							String valore= txtfld3I.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld3I.setValue(valore);
						}
						
						LayoutContainer lc= new LayoutContainer(); 
			   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			  lc=(LayoutContainer) getParent().getParent();
			   			  lc=(LayoutContainer) lc.getItemByItemId("right");
			   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
				    	  if(hasValue(txtfld3I)){	
				    		  txtfld3I.clearInvalid();
				    		  txtfld3I.setData("sorgente", "DIP");
				    		  if(!isMax(txtfld3I, txtfld2I)|| !isMax(txtfld3I, txtfld2U)){
				    			  txtErrore.setVisible(true);
				    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
				    			  btnConferma.setEnabled(false);
				    			  disableField(txtfld3I);
				    		  }else{		    		  
				    		  txtfld3U.setAllowBlank(false);
				    		  txtErrore.setVisible(false);
				    		  enableField();
			    			   if(hasValue(txtfld3U)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld3I);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma3.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	   
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			     } 
			    			   }
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld3U.setAllowBlank(true);
			    		       txtfldSomma3.setValue("0.0");
			    		       delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());

				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		  
			    		   }
					}	    		
			      }
			 });

			txtfld3U.setItemId("3U");
			txtfld3U.setFieldLabel("U");
			txtfld3U.setMaxLength(5);
			txtfld3U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld3U.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld3U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			   lc=(LayoutContainer) getParent().getParent();
		   			   lc=(LayoutContainer) lc.getItemByItemId("right");
		   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld3U)){
			    		   txtfld3U.clearInvalid();
			    		   txtfld3U.setData("sorgente", "DIP");
		    			   txtfld3I.setAllowBlank(false);
		    			   if(hasValue(txtfld3I)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld3U);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma3.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta); 
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			   } 
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld3I.setAllowBlank(true);
			    		   txtfldSomma3.setValue("0.0");
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    	   
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);		    		   
		    		   }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld3U.getValue()==null)
							txtfld3U.setValue("0:00");
						else{
							String valore= txtfld3U.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld3U.setValue(valore);
						}
						 LayoutContainer lc= new LayoutContainer(); 
			   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			   lc=(LayoutContainer) getParent().getParent();
			   			   lc=(LayoutContainer) lc.getItemByItemId("right");
			   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			   			   
				    	  if(hasValue(txtfld3U)){
				    		   txtfld3U.clearInvalid();
				    		   txtfld3U.setData("sorgente", "DIP");
			    			   txtfld3I.setAllowBlank(false);
			    			   if(hasValue(txtfld3I)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld3U);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		  
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma3.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta); 
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			   } 
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld3I.setAllowBlank(true);
				    		   txtfldSomma3.setValue("0.0");
				    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());
				    	   
				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);		    		   
			    		   }
					}	    		
			      }
			 });

			txtfld4I.setItemId("4I");
			txtfld4I.setFieldLabel("I");
			txtfld4I.setMaxLength(5);
			txtfld4I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld4I.getMessages().setRegexText("Deve essere nel formato 23:59");
		   txtfld4I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld4I)){
			    		  txtfld4I.clearInvalid();
			    		  txtfld4I.setData("sorgente", "DIP");
			    		  if(!isMax(txtfld4I, txtfld3I)|| !isMax(txtfld4I, txtfld3U)){
			    			  txtErrore.setVisible(true);
			    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
			    			  btnConferma.setEnabled(false);
			    			  disableField(txtfld4I);
			    		  }else{
			    		  txtErrore.setVisible(false); 
		    			  txtfld4U.setAllowBlank(false);
		    			  enableField();
		    			   if(hasValue(txtfld4U)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld4I);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma4.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);   
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			     } 
		    			   }
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld4U.setAllowBlank(true);
			    		   txtfldSomma4.setValue("0.0");
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    	   
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	    		  
		    		   }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld4I.getValue()==null)
							txtfld4I.setValue("0:00");
						else{
							String valore= txtfld4I.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld4I.setValue(valore);
						}
						 LayoutContainer lc= new LayoutContainer(); 
			   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			  lc=(LayoutContainer) getParent().getParent();
			   			  lc=(LayoutContainer) lc.getItemByItemId("right");
			   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			   			   
				    	  if(hasValue(txtfld4I)){
				    		  txtfld4I.clearInvalid();
				    		  txtfld4I.setData("sorgente", "DIP");
				    		  if(!isMax(txtfld4I, txtfld3I)|| !isMax(txtfld4I, txtfld3U)){
				    			  txtErrore.setVisible(true);
				    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
				    			  btnConferma.setEnabled(false);
				    			  disableField(txtfld4I);
				    		  }else{
				    		  txtErrore.setVisible(false); 
			    			  txtfld4U.setAllowBlank(false);
			    			  enableField();
			    			   if(hasValue(txtfld4U)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld4I);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		  
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma4.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);   
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			     } 
			    			   }
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld4U.setAllowBlank(true);
				    		   txtfldSomma4.setValue("0.0");
				    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());
				    	   
				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	    		  
			    		   }
					}	    		
			      }
			 });

			txtfld4U.setItemId("4U");
			txtfld4U.setFieldLabel("U");
			txtfld4U.setMaxLength(5);
			txtfld4U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld4U.getMessages().setRegexText("Deve essere nel formato 23:59");
		    txtfld4U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			   lc=(LayoutContainer) getParent().getParent();
		   			   lc=(LayoutContainer) lc.getItemByItemId("right");
		   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld4U)){
			    		   txtfld4U.clearInvalid();
			    		   txtfld4U.setData("sorgente", "DIP");
		    			   txtfld4I.setAllowBlank(false);
		    			   if(hasValue(txtfld4I)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld4U);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma4.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);   
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			   } 
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   String delta=new String();
		    			   txtfld4I.setAllowBlank(true);
			    		   txtfldSomma4.setValue("0.0");
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    	   
			    		   
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		  
		    		   }		    	     
			      }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld4U.getValue()==null)
							txtfld4U.setValue("0:00");
						else{
							String valore= txtfld4U.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld4U.setValue(valore);
						}
						LayoutContainer lc= new LayoutContainer(); 
			   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
			   			   lc=(LayoutContainer) getParent().getParent();
			   			   lc=(LayoutContainer) lc.getItemByItemId("right");
			   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			   			   
				    	  if(hasValue(txtfld4U)){
				    		   txtfld4U.clearInvalid();
				    		   txtfld4U.setData("sorgente", "DIP");
			    			   txtfld4I.setAllowBlank(false);
			    			   if(hasValue(txtfld4I)){
			    				   String sommaIntervalli= new String();  		   
						    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString());
						    	   if(sommaIntervalli.length()>5){
						    		   txtErrore.setVisible(true);
						    		   txtErrore.setText(sommaIntervalli);
						    		   btnConferma.setEnabled(false);
						    		   disableField(txtfld4U);
						    	   }else{	   
						    		   String totale= new String();
						    		   String delta= new String();
						    		   List<String> listaParziali= new ArrayList<String>();
						    		  
						    		   btnConferma.setEnabled(true);
						    		   txtErrore.setVisible(false);
						    		   enableField();
						    		   txtfldSomma4.setValue(sommaIntervalli);
						    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
						    		   listaParziali.add(txtfldSomma2.getValue().toString());
						    		   listaParziali.add(txtfldSomma3.getValue().toString());
						    		   listaParziali.add(txtfldSomma4.getValue().toString());
						    		   listaParziali.add(txtfldSomma5.getValue().toString());
						    	   
						    		   totale=ClientUtility.calcolaTempo(listaParziali);
						    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
						    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
						    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);   
						    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
						    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
						    		   else
						    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
						    		   setFieldGiustificativo(fldsetGiustificativo, delta);
						    	   }
			    			   } 
			    		   } else {
			    			   List<String> listaParziali= new ArrayList<String>();
			    			   String totale= new String();
			    			   String delta=new String();
			    			   txtfld4I.setAllowBlank(true);
				    		   txtfldSomma4.setValue("0.0");
				    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
				    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
				    		   listaParziali.add(txtfldSomma2.getValue().toString());
				    		   listaParziali.add(txtfldSomma3.getValue().toString());
				    		   listaParziali.add(txtfldSomma4.getValue().toString());
				    		   listaParziali.add(txtfldSomma5.getValue().toString());
				    	   
				    		   
				    		   totale=ClientUtility.calcolaTempo(listaParziali);
				    		   if(totale.compareTo("0.00")!=0){
				    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
				    			   setFieldGiustificativo(fldsetGiustificativo, delta);
				    		   }else enableFieldGiustificativo(fldsetGiustificativo);
				    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
				    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
				    		   else
				    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
				    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
				    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
				    		  
			    		   }	
					}	    		
			      }
			 });

			txtfld5I.setItemId("5I");
			txtfld5I.setFieldLabel("I");
			txtfld5I.setMaxLength(5);
			txtfld5I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld5I.getMessages().setRegexText("Deve essere nel formato 23:59");
		   	txtfld5I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld5I)){
			    		  txtfld5I.clearInvalid();
			    		  txtfld5I.setData("sorgente", "DIP");
			    		  if(!isMax(txtfld5I, txtfld4I)|| !isMax(txtfld5I, txtfld4U)){
			    			  txtErrore.setVisible(true);
			    			  txtErrore.setText("errore: Orario inferiore a quelle dell'intervallo precedente!");
			    			  btnConferma.setEnabled(false);
			    			  disableField(txtfld5I);			    			  
			    		  }else{
			    		  txtErrore.setVisible(false); 
			    		  txtfld5U.setAllowBlank(false);
			    		  enableField();
		    			   if(hasValue(txtfld5U)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld5I.getValue().toString(), txtfld5U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld5I);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma5.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);  
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			     } 
		    			   }
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   
		    			   txtfld5U.setAllowBlank(true);
			    		   txtfldSomma5.setValue("0.0");
			    		   
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    	   
			    		   String delta=new String();
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   
		    		   }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld5I.getValue()==null)
							txtfld5I.setValue("0:00");
						else{
							String valore= txtfld5I.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld5I.setValue(valore);
						}
					}	    		
			      }
			 });

			txtfld5U.setItemId("5U");
			txtfld5U.setFieldLabel("U");
			txtfld5U.setMaxLength(5);
			txtfld5U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld5U.getMessages().setRegexText("Deve essere nel formato 23:59");
		   
			txtfld5U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld5U)){
			    		   txtfld5U.clearInvalid();
			    		   txtfld5U.setData("sorgente", "DIP");
		    			   txtfld5I.setAllowBlank(false);
		    			   if(hasValue(txtfld5I)){
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld5I.getValue().toString(), txtfld5U.getValue().toString());
					    	   if(sommaIntervalli.length()>5){
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld5U);
					    	   }else{	   
					    		   String totale= new String();
					    		   String delta= new String();
					    		   List<String> listaParziali= new ArrayList<String>();
					    		  
					    		   btnConferma.setEnabled(true);
					    		   txtErrore.setVisible(false);
					    		   enableField();
					    		   txtfldSomma5.setValue(sommaIntervalli);
					    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
					    		   listaParziali.add(txtfldSomma2.getValue().toString());
					    		   listaParziali.add(txtfldSomma3.getValue().toString());
					    		   listaParziali.add(txtfldSomma4.getValue().toString());
					    		   listaParziali.add(txtfldSomma5.getValue().toString());
					    	   
					    		   totale=ClientUtility.calcolaTempo(listaParziali);
					    		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
					    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
					    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);	
					    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
					    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
					    		   else
					    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
					    		   setFieldGiustificativo(fldsetGiustificativo, delta);
					    	   }
		    			   } 
		    		   } else {
		    			   List<String> listaParziali= new ArrayList<String>();
		    			   String totale= new String();
		    			   
		    			   txtfld5I.setAllowBlank(true);
			    		   txtfldSomma5.setValue("0.0");
			    		   
			    	   	   listaParziali.add(txtfldSomma1.getValue().toString());
			    		   listaParziali.add(txtfldSomma2.getValue().toString());
			    		   listaParziali.add(txtfldSomma3.getValue().toString());
			    		   listaParziali.add(txtfldSomma4.getValue().toString());
			    		   listaParziali.add(txtfldSomma5.getValue().toString());
			    	   
			    		   String delta=new String();
			    		   delta=("-"+ fldsetGiustificativo.txtfldOrePreviste.getValue()+".00");
			    		   totale=ClientUtility.calcolaTempo(listaParziali);
			    		   if(totale.compareTo("0.00")!=0){
			    			   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
			    			   setFieldGiustificativo(fldsetGiustificativo, delta);
			    		   }else enableFieldGiustificativo(fldsetGiustificativo);
			    		   if(d.compareTo("Sat")==0 || d.compareTo("Sun")==0)
			    			   fldsetGiustificativo.txtfldStraordinario.setValue(delta);
			    		   else
			    			   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);		    		   
		    		   }
		    	   }
			      
			      @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfld5U.getValue()==null)
							txtfld5U.setValue("0:00");
						else{
							String valore= txtfld5U.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0:00";
							else
								if(valore.indexOf(":")==-1)
									valore=valore+":00";
								else{
									int index=valore.indexOf(":");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfld5U.setValue(valore);
						}
					}	    		
			      }
			 });

			FormLayout layout = new FormLayout();
			layout.setLabelWidth(25);
			layoutCol1.setLayout(layout);
			layoutCol1.add(txtfld1I, new FormData("80%"));
			layoutCol1.add(txtfld1U, new FormData("80%"));

			layout = new FormLayout();
			layout.setLabelWidth(25);
			layoutCol2.setLayout(layout);
			layoutCol2.add(txtfld2I, new FormData("80%"));
			layoutCol2.add(txtfld2U, new FormData("80%"));

			layout = new FormLayout();
			layout.setLabelWidth(25);
			layoutCol3.setLayout(layout);
			layoutCol3.add(txtfld3I, new FormData("80%"));
			layoutCol3.add(txtfld3U, new FormData("80%"));

			layout = new FormLayout();
			layout.setLabelWidth(25);
			layoutCol4.setLayout(layout);
			layoutCol4.add(txtfld4I, new FormData("80%"));
			layoutCol4.add(txtfld4U, new FormData("80%"));

			layout = new FormLayout();
			layout.setLabelWidth(25);
			layoutCol5.setLayout(layout);
			layoutCol5.add(txtfld5I, new FormData("80%"));
			layoutCol5.add(txtfld5U, new FormData("80%"));

			RowData data = new RowData(.2, 1);
			data.setMargins(new Margins(5));
			cp.add(layoutCol1, data);
			cp.add(layoutCol2, data);
			cp.add(layoutCol3, data);
			cp.add(layoutCol4, data);
			cp.add(layoutCol5, data);

			ButtonBar buttonBar = new ButtonBar();
			buttonBar.setAlignment(HorizontalAlignment.CENTER);
			buttonBar.setStyleAttribute("padding-bottom", "5px");
			buttonBar.setBorders(false);
			buttonBar.setWidth(570);
			buttonBar.add(txtErrore);
			
							
			if(tipo=="new"){
				
				txtfldSomma1.setValue("0.0");
				txtfldSomma2.setValue("0.0");
				txtfldSomma3.setValue("0.0");
				txtfldSomma4.setValue("0.0");
				txtfldSomma5.setValue("0.0");	
				
				txtfld1I.setEmptyText("#");
				txtfld1U.setEmptyText("#");
				txtfld2I.setEmptyText("#");
				txtfld2U.setEmptyText("#");
				txtfld3I.setEmptyText("#");
				txtfld3U.setEmptyText("#");
				txtfld4I.setEmptyText("#");
				txtfld4U.setEmptyText("#");
				txtfld5I.setEmptyText("#");
				txtfld5U.setEmptyText("#");
				
				txtfld1I.setData("sorgente", "");
				txtfld1U.setData("sorgente", "");
				txtfld2I.setData("sorgente", "");
				txtfld2U.setData("sorgente", "");
				txtfld3I.setData("sorgente", "");
				txtfld3U.setData("sorgente", "");
				txtfld4I.setData("sorgente", "");
				txtfld4U.setData("sorgente", "");
				txtfld5I.setData("sorgente", "");
				txtfld5U.setData("sorgente", "");
				
				
				caricaIntervalliTooTip();
				/*String s= txtSede.getText();
				if((statoRevisione==2)||(txtSede.getText().compareTo("S")==0))
					disableField();*/							
			}
			else{	
				
				IntervalliIUModel i = new IntervalliIUModel();
				i=result.remove(0); //elimino il primo elemento dalla lista in quanto contiene lo stato di revisione del mese
				
				setIntervalli(result);			
			}		
			add(cp);
			add(buttonBar);
			layout(true);
		}
		
		
		private boolean isMax(TextField<String> txtfld1,	TextField<String> txtfld2) {
			
			String orario1=new String();
			String orario2=new String();
			String ore1=new String();
			String min1=new String();
			String ore2=new String();
			String min2=new String();
			
			orario1=txtfld1.getValue().toString();
			orario2=txtfld2.getValue().toString();
			
			ore1=orario1.substring(0, orario1.indexOf(":"));
			min1=orario1.substring(orario1.indexOf(":")+1, orario1.length());
			
			ore2=orario2.substring(0, orario2.indexOf(":"));
			min2=orario2.substring(orario2.indexOf(":")+1, orario2.length());
			
			if((Integer.valueOf(ore1)*60+Integer.valueOf(min1))>(Integer.valueOf(ore2)*60+Integer.valueOf(min2)))
				return true;
			else 
				return false;
		}
		
		
		private void setFieldGiustificativo(FldsetGiustificativi fldsetGiustificativo, String delta) {
			
			if(Float.valueOf(delta)>0){
				fldsetGiustificativo.txtfldFerie.setEnabled(false);
				fldsetGiustificativo.txtfldPermesso.setEnabled(false);	
				fldsetGiustificativo.txtfldStraordinario.setEnabled(true);
				fldsetGiustificativo.txtfldRecupero.clearInvalid();
				fldsetGiustificativo.txtfldRecupero.setRegex("[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
				fldsetGiustificativo.txtfldRecupero.getMessages().setRegexText("Deve essere un numero nel formato 99.59");		
				fldsetGiustificativo.txtfldRecupero.setEnabled(true);
				//fldsetGiustificativo.smplcmbxAltroGiustificativo.setAllowBlank(false);
			}
			
			if(Float.valueOf(delta)<0){			
				fldsetGiustificativo.txtfldFerie.setEnabled(true);
				fldsetGiustificativo.txtfldPermesso.setEnabled(true);
				fldsetGiustificativo.txtfldStraordinario.setEnabled(false);
				fldsetGiustificativo.txtfldRecupero.clearInvalid();
				fldsetGiustificativo.txtfldRecupero.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
				fldsetGiustificativo.txtfldRecupero.getMessages().setRegexText("Deve essere un numero nel formato -99.59");			
				fldsetGiustificativo.txtfldRecupero.setEnabled(true);
				//fldsetGiustificativo.smplcmbxAltroGiustificativo.setAllowBlank(false);
			}	
			
			if(Float.valueOf(delta)==0){			
				fldsetGiustificativo.txtfldRecupero.setEnabled(false);
				fldsetGiustificativo.smplcmbxAltroGiustificativo.setAllowBlank(true);
				fldsetGiustificativo.txtfldFerie.setEnabled(false);
				fldsetGiustificativo.txtfldPermesso.setEnabled(false);	
				fldsetGiustificativo.txtfldStraordinario.setEnabled(false);
			}	
		}		
		
		
		private void enableFieldGiustificativo(FldsetGiustificativi fldsetGiustificativo){	
			fldsetGiustificativo.txtfldFerie.setEnabled(true);
			fldsetGiustificativo.txtfldPermesso.setEnabled(true);
			fldsetGiustificativo.txtfldStraordinario.setEnabled(true);
			fldsetGiustificativo.txtfldRecupero.setEnabled(true);
			fldsetGiustificativo.smplcmbxAltroGiustificativo.setEnabled(true);
		}
		
		
		private void setIntervalli(List<IntervalliIUModel> result) {

String movimento= new String();
			
			txtfld1I.setData("sorgente", "");
			txtfld1U.setData("sorgente", "");
			txtfld2I.setData("sorgente", "");
			txtfld2U.setData("sorgente", "");
			txtfld3I.setData("sorgente", "");
			txtfld3U.setData("sorgente", "");
			txtfld4I.setData("sorgente", "");
			txtfld4U.setData("sorgente", "");
			txtfld5I.setData("sorgente", "");
			txtfld5U.setData("sorgente", "");
			
			txtfld1I.setValue("");
			txtfld1U.setValue("");
			txtfld2I.setValue("");
			txtfld2U.setValue("");
			txtfld3I.setValue("");
			txtfld3U.setValue("");
			txtfld4I.setValue("");
			txtfld4U.setValue("");
			txtfld5I.setValue("");
			txtfld5U.setValue("");
						
			for(int i=0; i<result.size(); i++){
				movimento= result.get(i).getMovimento();
				
				if (movimento.compareTo("i1")==0){
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld1I.setEnabled(false);
								txtfld1I.setData("sorgente", result.get(i).getSorgente());
							}
					else
							txtfld1I.setData("sorgente", "");
					txtfld1I.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("u1")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld1U.setEnabled(false);
								txtfld1U.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld1U.setData("sorgente", "");
					txtfld1U.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("i2")==0){
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld2I.setEnabled(false);
								txtfld2I.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld2I.setData("sorgente", "");
					txtfld2I.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("u2")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld2U.setEnabled(false);
								txtfld2U.setData("sorgente", result.get(i).getSorgente());					
							}
					else
							txtfld2U.setData("sorgente", "");
					txtfld2U.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("i3")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld3I.setEnabled(false);
								txtfld3I.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld3I.setData("sorgente", "");
					txtfld3I.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("u3")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld3U.setEnabled(false);
								txtfld3U.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld3U.setData("sorgente", "");
					txtfld3U.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("i4")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld4I.setEnabled(false);
								txtfld4I.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld4I.setData("sorgente", "");
					txtfld4I.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("u4")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld4U.setEnabled(false);
								txtfld4U.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld4U.setData("sorgente", "");
					txtfld4U.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("i5")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld5I.setEnabled(false);
								txtfld5I.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld5I.setData("sorgente", "");
					txtfld5I.setValue(result.get(i).getOrario());
					continue;
				}
				
				if (movimento.compareTo("u5")==0) {
					if((result.get(i).getSorgente().compareTo("PM")==0) || (result.get(i).getSorgente().compareTo("GP")==0 || 
							result.get(i).getSorgente().compareTo("TMB")==0)){
								txtfld5U.setEnabled(false);
								txtfld5U.setData("sorgente", result.get(i).getSorgente());
								
							}
					else
							txtfld5U.setData("sorgente", "");
					txtfld5U.setValue(result.get(i).getOrario());
					continue;
				}
			}			
			
			if(txtfldRuolo.getValue().compareTo("PM")==0 || txtfldRuolo.getValue().compareTo("GP")==0 || txtfldRuolo.getValue().compareTo("AMM")==0
					|| txtfldRuolo.getValue().compareTo("DIR")==0){			
				enableField();				
			}
					
		    if(txtfld1I.getValue().toString().compareTo("")==0||txtfld1U.getValue().toString().compareTo("")==0)
				txtfldSomma1.setValue("0.0");
			else txtfldSomma1.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString()));
			
			if(txtfld2I.getValue().toString().compareTo("")==0||txtfld2U.getValue().toString().compareTo("")==0)
				txtfldSomma2.setValue("0.0");
			else txtfldSomma2.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString()));
			
			if(txtfld3I.getValue().toString().compareTo("")==0||txtfld3U.getValue().toString().compareTo("")==0)
				txtfldSomma3.setValue("0.0");
			else txtfldSomma3.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString()));
			
			if(txtfld4I.getValue().toString().compareTo("")==0||txtfld4U.getValue().toString().compareTo("")==0)
				txtfldSomma4.setValue("0.0");
			else txtfldSomma4.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString()));
			
			if(txtfld5I.getValue().toString().compareTo("")==0||txtfld5U.getValue().toString().compareTo("")==0)
				txtfldSomma5.setValue("0.0");
			else txtfldSomma5.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld5I.getValue().toString(), txtfld5U.getValue().toString()));
		
			caricaIntervalliTooTip();
		}
		
		
		protected boolean hasValue(TextField<String> field) {
		    return field.getValue() != null && field.isValid();
		}
		
		
		private void disableField(TextField<String> txtfld) {
			String itemId= new String();
			itemId=txtfld.getItemId();
			
			if(itemId.compareTo("1I")!=0) txtfld1I.setEnabled(false);
			if(itemId.compareTo("1U")!=0) txtfld1U.setEnabled(false);
			if(itemId.compareTo("2I")!=0) txtfld2I.setEnabled(false);
			if(itemId.compareTo("2U")!=0) txtfld2U.setEnabled(false);
			if(itemId.compareTo("3I")!=0) txtfld3I.setEnabled(false);
			if(itemId.compareTo("3U")!=0) txtfld3U.setEnabled(false);
			if(itemId.compareTo("4I")!=0) txtfld4I.setEnabled(false);
			if(itemId.compareTo("4U")!=0) txtfld4U.setEnabled(false);
			if(itemId.compareTo("5I")!=0) txtfld5I.setEnabled(false);
			if(itemId.compareTo("5U")!=0) txtfld5U.setEnabled(false);
		}
		
		private void enableField() {
			
			txtfld1I.setEnabled(true);
			txtfld1U.setEnabled(true);
			txtfld2I.setEnabled(true);
			txtfld2U.setEnabled(true);
			txtfld3I.setEnabled(true);
			txtfld3U.setEnabled(true);
			txtfld4I.setEnabled(true);
			txtfld4U.setEnabled(true);
			txtfld5I.setEnabled(true);
			txtfld5U.setEnabled(true);
		}		
		
		public void caricaIntervalliTooTip(){
			String username=txtfldUsername.getValue().toString();
			Date giorno=dtfldGiorno.getValue();
			
			AdministrationService.Util.getInstance().loadIntervalliToolTip(username, giorno, new AsyncCallback<List<String>>() {		
				@Override
				public void onSuccess(List<String> result) {
					if(result==null)
						Window.alert("error: Impossibile accedere ai dati della Timbratrice.");
					else
						loadToolTip(result);
				}
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on loadIntervalliToolTip();");
				}
			});
		}
		
		private void loadToolTip(List<String> result) {
			List<TimbraturaModel> orari= new ArrayList<TimbraturaModel>();		
			TimbraturaModel tmb;
			
			String toolTip="";
			
			for(int i=0; i<result.size(); i+=2){
				toolTip=(toolTip+result.get(i)+": ");//movimento
				toolTip=(toolTip+result.get(i+1)+" | ");//orario
				
				tmb=new TimbraturaModel("", String.valueOf(i) , result.get(i), result.get(i+1)); 
				orari.add(tmb);
			}
			
			btnMostraIntervalli.setToolTip(toolTip);
			
			//PreCompilo le caselle intervalloIU con i dati della bollatrice
			if(isNew.compareTo("new")==0){
						
				String posizione=new String();//posizione va da 0 a 18 e solo i pari
				
				for(int j=0; j<orari.size(); j++){
					
					tmb=orari.get(j);
					//ho usato il getData() come appoggio per la posizione del dato
					posizione=tmb.getData();
					
					if(posizione.compareTo("0")==0){
						if(tmb.getMovimento().compareTo("I")==0){
							txtfld1I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld1I.setData("sorgente", "TMB");
							txtfld1I.setEnabled(false);
							continue;
							
						}else{
							txtfld1U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld1U.setData("sorgente", "TMB");
							txtfld1U.setEnabled(false);	
							if(!txtfld1I.getRawValue().isEmpty()){
								txtfldSomma1.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString()));
								aggiornaTotaleIntervalli();
							}
							continue;
						}
					}
					if(posizione.compareTo("2")==0){
						if(tmb.getMovimento().compareTo("U")==0){
							txtfld1U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld1U.setData("sorgente", "TMB");
							txtfld1U.setEnabled(false);
							txtfldSomma1.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString()));
							aggiornaTotaleIntervalli();
							continue;
							
						}else{
							txtfld2I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld2I.setData("sorgente", "TMB");
							txtfld2I.setEnabled(false);
							continue;
						}
					}
					if(posizione.compareTo("4")==0){
						if(tmb.getMovimento().compareTo("I")==0){
							txtfld2I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld2I.setData("sorgente", "TMB");
							txtfld2I.setEnabled(false);
							continue;
						}else{
							txtfld2U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld2U.setData("sorgente", "TMB");
							txtfld2U.setEnabled(false);	
							if(!txtfld2I.getRawValue().isEmpty()){
								txtfldSomma2.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString()));
								aggiornaTotaleIntervalli();
							}
							continue;
						}
					}
					if(posizione.compareTo("6")==0){
						if(tmb.getMovimento().compareTo("U")==0){
							txtfld2U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld2U.setData("sorgente", "TMB");
							txtfld2U.setEnabled(false);
							txtfldSomma2.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld2I.getValue().toString(), txtfld2U.getValue().toString()));
							aggiornaTotaleIntervalli();
							continue;
						}else{
							txtfld3I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld3I.setData("sorgente", "TMB");
							txtfld3I.setEnabled(false);
							continue;
						}
					}
					if(posizione.compareTo("8")==0){
						if(tmb.getMovimento().compareTo("I")==0){
							txtfld3I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld3I.setData("sorgente", "TMB");
							txtfld3I.setEnabled(false);
							continue;
						}else{
							txtfld3U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld3U.setData("sorgente", "TMB");
							txtfld3U.setEnabled(false);
							if(!txtfld3I.getRawValue().isEmpty()){
								txtfldSomma3.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString()));
								aggiornaTotaleIntervalli();
							}
							continue;
						}
					}
					if(posizione.compareTo("10")==0){
						if(tmb.getMovimento().compareTo("U")==0){
							txtfld3U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld3U.setData("sorgente", "TMB");
							txtfld3U.setEnabled(false);
							txtfldSomma3.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld3I.getValue().toString(), txtfld3U.getValue().toString()));
							aggiornaTotaleIntervalli();
							continue;
						}else{
							txtfld4I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld4I.setData("sorgente", "TMB");
							txtfld4I.setEnabled(false);
							continue;
						}
					}
					if(posizione.compareTo("12")==0){
						if(tmb.getMovimento().compareTo("I")==0){
							txtfld4I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld4I.setData("sorgente", "TMB");
							txtfld4I.setEnabled(false);
							continue;
						}else{
							txtfld4U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld4U.setData("sorgente", "TMB");
							txtfld4U.setEnabled(false);
							if(!txtfld4I.getRawValue().isEmpty()){
								txtfldSomma4.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString()));
								aggiornaTotaleIntervalli();
							}
							continue;
						}
					}
					if(posizione.compareTo("14")==0){
						if(tmb.getMovimento().compareTo("U")==0){
							txtfld4U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld4U.setData("sorgente", "TMB");
							txtfld4U.setEnabled(false);
							txtfldSomma4.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld4I.getValue().toString(), txtfld4U.getValue().toString()));
							aggiornaTotaleIntervalli();
							continue;
						}else{
							txtfld5I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld5I.setData("sorgente", "TMB");
							txtfld5I.setEnabled(false);
							continue;
						}
					}
					if(posizione.compareTo("16")==0){
						if(tmb.getMovimento().compareTo("I")==0){
							txtfld5I.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld5I.setData("sorgente", "TMB");
							txtfld5I.setEnabled(false);
							continue;
						}else{
							txtfld5U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld5U.setData("sorgente", "TMB");
							txtfld5U.setEnabled(false);
							if(!txtfld5I.getRawValue().isEmpty()){
								txtfldSomma5.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld5I.getValue().toString(), txtfld5U.getValue().toString()));
								aggiornaTotaleIntervalli();
							}
							continue;
						}
					}
					if(posizione.compareTo("18")==0){
							txtfld5U.setValue(ClientUtility.arrotondaIntervallo(tmb.getOrario(), tmb.getMovimento()));
							txtfld5U.setData("sorgente", "TMB");
							txtfld5U.setEnabled(false);
							txtfldSomma5.setValue(ClientUtility.calcolaParzialeIntervalli(txtfld5I.getValue().toString(), txtfld5U.getValue().toString()));
							aggiornaTotaleIntervalli();
					}				
				}	
				
				if(txtfldRuolo.getValue().compareTo("PM")==0 || txtfldRuolo.getValue().compareTo("GP")==0 || txtfldRuolo.getValue().compareTo("AMM")==0
						|| txtfldRuolo.getValue().compareTo("DIR")==0){
					txtfld1I.setEnabled(true);
					txtfld1U.setEnabled(true);
					txtfld2I.setEnabled(true);
					txtfld2U.setEnabled(true);
					txtfld3I.setEnabled(true);
					txtfld3U.setEnabled(true);
					txtfld4I.setEnabled(true);
					txtfld4U.setEnabled(true);
					txtfld5I.setEnabled(true);
					txtfld5U.setEnabled(true);					
				}				
			}	
			else 
				aggiornaTotaleIntervalli();
		}

		private void aggiornaTotaleIntervalli() {
			
			   List<String> listaParziali= new ArrayList<String>();
			   String totale=new String();
			   String delta=new String();
			   String abbuono=new String();
			   
			   LayoutContainer lc= new LayoutContainer(); 
		       LayoutContainer right= new LayoutContainer();
			   FldsetGiustificativi fldsetGiustificativo;
			  
			   if((LayoutContainer) getParent().getParent()!=null)
				   lc=(LayoutContainer) getParent().getParent();
			   right=(LayoutContainer) lc.getItemByItemId("right");
			   fldsetGiustificativo=(FldsetGiustificativi) right.getItemByItemId("fldSetGiustificativi");
			   		   
			   listaParziali.add(txtfldSomma1.getValue().toString());
	  		   listaParziali.add(txtfldSomma2.getValue().toString());
	  		   listaParziali.add(txtfldSomma3.getValue().toString());
	  		   listaParziali.add(txtfldSomma4.getValue().toString());
	  		   listaParziali.add(txtfldSomma5.getValue().toString());
	  	   
	  		   abbuono="-"+fldsetGiustificativo.txtfldAbbuono.getValue();
	  		   totale=ClientUtility.calcolaTempo(listaParziali);
	  		   totale=ClientUtility.aggiornaTotGenerale(totale, abbuono);
	  		   
	  		   delta=ClientUtility.calcoloDelta(totale, fldsetGiustificativo.txtfldOrePreviste.getValue());
	  		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
	  		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
	  		  
	  		   if(isNew.compareTo("new")==0){
	  			 fldsetGiustificativo.txtfldRecupero.setValue(delta);
	  		   }
	  		   	   
	  		   setFieldGiustificativo(fldsetGiustificativo, delta);			
			}
	}
	
	
	public class FldsetGiustificativi extends FieldSet {	
		public SimpleComboBox<String> smplcmbxAltroGiustificativo=new SimpleComboBox<String>();
		public TextField<String> txtfldFerie=new TextField<String>();
		public TextField<String> txtfldStraordinario=new TextField<String>();
		public TextField<String> txtfldPermesso=new TextField<String>();
		public TextField<String> txtfldRecupero=new TextField<String>();
		public TextField<String> txtfldAbbuono=new TextField<String>();
		
		public TextField<String> txtfldOreDelta= new TextField<String>();
		public TextField<String> txtfldOrePreviste= new TextField<String>();
		public TextField<String> txtfldTotGenerale= new TextField<String>();
		public TextField<String> txtfldOreViaggio= new TextField<String>();
		public TextField<String> txtfldDeltaViaggio= new TextField<String>();
		public TextField<String> txtfldOreTotEffettive= new TextField<String>();
		public TextArea txtrNoteAggiuntive= new TextArea();
		
		public Button btnAssegnaOreStraordinario= new Button();
		public Button btnAssegnaOreFerie= new Button();
		public Button btnAssegnaOrePermesso= new Button();
		public Button btnAssegnaRecupero= new Button();
		
		
		private String username= new String();
		private Date data= new Date();
		
		public FldsetGiustificativi(){
			
			username=txtfldUsername.getValue().toString();
			data=dtfldGiorno.getValue();
			
			AdministrationService.Util.getInstance().loadGiustificativi(username, data, new AsyncCallback<GiustificativiModel>() {
				
				@Override
				public void onSuccess(GiustificativiModel result) {
					if(result==null)
						Window.alert("error: Impossibile accedere ai dati sui giustificativi.");
					else
						load(result);		
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on loadGiustificativi()");
				}
			});		
			
			btnAssegnaOreStraordinario.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
			btnAssegnaOreStraordinario.setToolTip("Assegna delta giornaliero.");
			btnAssegnaOreStraordinario.setStyleAttribute("padding-top", "2px");
			btnAssegnaOreStraordinario.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					txtfldRecupero.setValue("0.00");
					txtfldFerie.setValue("0.00");
					txtfldPermesso.setValue("0.00");
					txtfldStraordinario.setValue(txtfldOreDelta.getValue());			
				}
			});
			
			btnAssegnaOreFerie.setToolTip("Assegna delta giornaliero.");
			btnAssegnaOreFerie.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
			btnAssegnaOreFerie.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					txtfldRecupero.setValue("0.00");
					txtfldStraordinario.setValue("0.00");
					txtfldPermesso.setValue("0.00");
					txtfldFerie.setValue(txtfldOreDelta.getValue());				
				}
			});
			
			btnAssegnaOrePermesso.setToolTip("Assegna delta giornaliero.");
			btnAssegnaOrePermesso.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
			btnAssegnaOrePermesso.setStyleAttribute("padding-top", "2px");
			btnAssegnaOrePermesso.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					txtfldRecupero.setValue("0.00");
					txtfldFerie.setValue("0.00");
					txtfldStraordinario.setValue("0.00");
					txtfldPermesso.setValue(txtfldOreDelta.getValue());				
				}
			});
			
			btnAssegnaRecupero.setToolTip("Assegna delta giornaliero.");
			btnAssegnaRecupero.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
			btnAssegnaRecupero.setStyleAttribute("padding-top", "2px");
			btnAssegnaRecupero.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					txtfldStraordinario.setValue("0.00");
					txtfldFerie.setValue("0.00");
					txtfldPermesso.setValue("0.00");
					txtfldRecupero.setValue(txtfldOreDelta.getValue());			
				}
			});
		}	
		
		private void load(GiustificativiModel result) {
			
			final NumberFormat number = NumberFormat.getFormat("0.00");
					
			setBorders(true);
			setHeading("Giustificativo.");
			setItemId("fldSetGiustificativi");
			
			LayoutContainer layoutCol1=new LayoutContainer();
			LayoutContainer layoutCol2=new LayoutContainer();
			LayoutContainer layoutCol3=new LayoutContainer();
			LayoutContainer layoutCol4=new LayoutContainer();
			LayoutContainer layoutCol5=new LayoutContainer();
			LayoutContainer layoutCol6=new LayoutContainer();
			
			String d=data.toString();
			txtfldOrePreviste.setName("orePreviste");
			txtfldOrePreviste.setEnabled(false);
			txtfldOrePreviste.setFieldLabel("Ore Previste");	
			if(d.substring(0, 3).compareTo("Sat")==0 || d.substring(0, 3).compareTo("Sun")==0)
				txtfldOrePreviste.setValue("0.00");
			else	
				txtfldOrePreviste.setValue(result.getOrePreviste());
			
			txtfldTotGenerale.setEnabled(false);
			txtfldTotGenerale.setFieldLabel("Tot.Generale");
			txtfldTotGenerale.setValue(result.getTotGenerale());
			
			txtfldOreDelta.setEnabled(false);
			txtfldOreDelta.setFieldLabel("Delta Giornaliero");
			txtfldOreDelta.setValue(result.getDelta());
			
			txtfldOreViaggio.setEnabled(true);
			txtfldOreViaggio.setAllowBlank(false);
			txtfldOreViaggio.setValue(result.getOreViaggio());
			txtfldOreViaggio.setFieldLabel("Ore Viaggio");
			txtfldOreViaggio.setRegex("[0-9]+[.][0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldOreViaggio.getMessages().setRegexText("Deve essere un numero nel formato 99.59");		
			txtfldOreViaggio.addKeyListener(new KeyListener(){
				public void componentKeyUp(ComponentEvent event) {
					String oreRecuperoOld=txtfldRecupero.getValue().toString();
					if(hasValueOreViaggio(txtfldOreViaggio)){
						txtfldOreViaggio.clearInvalid();
						String oreTotGenerale= new String();
						String deltaGiorno= new String();
						String oreViaggio= new String();
						String deltaViaggio= new String();
						String orePreviste=(txtfldOrePreviste.getValue().toString()+".00");
										
						oreTotGenerale=txtfldTotGenerale.getValue().toString();
						deltaGiorno=txtfldOreDelta.getValue().toString();
						oreViaggio=txtfldOreViaggio.getValue().toString();
						
						if(Float.valueOf(deltaGiorno)<0){
							if(Float.valueOf(oreViaggio)>Math.abs(Float.valueOf(deltaGiorno))){
								deltaViaggio=ClientUtility.aggiornaTotGenerale(oreViaggio, deltaGiorno);	
								
								txtfldDeltaViaggio.setValue(deltaViaggio);
								txtfldRecupero.setValue("0.00");
								//il delta viaggio è positivo e setto il giustificativo a ore viaggio
								smplcmbxAltroGiustificativo.setAllowBlank(false);
								smplcmbxAltroGiustificativo.setSimpleValue("27.Ore Viaggio");
								txtfldOreTotEffettive.setValue(orePreviste);
							}								
							if(Float.valueOf(oreViaggio)<=Math.abs(Float.valueOf(deltaGiorno))){
								oreTotGenerale=ClientUtility.aggiornaTotGenerale(oreTotGenerale, oreViaggio);
								//deltaGiorno=;
								//Resta un residuo di ore a recupero
								txtfldRecupero.setValue(number.format(Float.valueOf(ClientUtility.calcoloDelta(oreTotGenerale, orePreviste))));
								txtfldDeltaViaggio.setValue("0.00");//deltaViaggio è 0 in quanto non dovranno essere contabilizzate come ore viaggio
								txtfldOreTotEffettive.setValue(oreTotGenerale);
							}
						}else
						
						if(Float.valueOf(deltaGiorno)>=0){
							txtfldDeltaViaggio.setValue(oreViaggio);
							//il delta viaggio è positivo e setto il giustificativo a ore viaggio
							smplcmbxAltroGiustificativo.setAllowBlank(false);
							txtfldOreTotEffettive.setValue(oreTotGenerale);
						}
						
					}else{
						txtfldRecupero.setValue(oreRecuperoOld);
						txtfldDeltaViaggio.setValue("0.00");
						txtfldOreTotEffettive.setValue("0.00");
						smplcmbxAltroGiustificativo.clear();
						smplcmbxAltroGiustificativo.setAllowBlank(true);
						smplcmbxAltroGiustificativo.setEmptyText("Giustificativo..");
					}
				}	
				
				 @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreViaggio.getValue()==null)
							txtfldOreViaggio.setValue("0.00");
						else{
							String valore= txtfldOreViaggio.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldOreViaggio.setValue(valore);
						}
						
						String oreRecuperoOld=txtfldRecupero.getValue().toString();
						if(hasValueOreViaggio(txtfldOreViaggio)){
							txtfldOreViaggio.clearInvalid();
							String oreTotGenerale= new String();
							String deltaGiorno= new String();
							String oreViaggio= new String();
							String deltaViaggio= new String();
							String orePreviste=(txtfldOrePreviste.getValue().toString()+".00");
											
							oreTotGenerale=txtfldTotGenerale.getValue().toString();
							deltaGiorno=txtfldOreDelta.getValue().toString();
							oreViaggio=txtfldOreViaggio.getValue().toString();
							
							if(Float.valueOf(deltaGiorno)<0){
								if(Float.valueOf(oreViaggio)>Math.abs(Float.valueOf(deltaGiorno))){
									deltaViaggio=ClientUtility.aggiornaTotGenerale(oreViaggio, deltaGiorno);	
									
									txtfldDeltaViaggio.setValue(deltaViaggio);
									txtfldRecupero.setValue("0.00");
									//il delta viaggio è positivo e setto il giustificativo a ore viaggio
									smplcmbxAltroGiustificativo.setAllowBlank(false);
									smplcmbxAltroGiustificativo.setSimpleValue("27.Ore Viaggio");
									txtfldOreTotEffettive.setValue(orePreviste);
								}								
								if(Float.valueOf(oreViaggio)<=Math.abs(Float.valueOf(deltaGiorno))){
									oreTotGenerale=ClientUtility.aggiornaTotGenerale(oreTotGenerale, oreViaggio);
									//deltaGiorno=;
									//Resta un residuo di ore a recupero
									txtfldRecupero.setValue(number.format(Float.valueOf(ClientUtility.calcoloDelta(oreTotGenerale, orePreviste))));
									txtfldDeltaViaggio.setValue("0.00");//deltaViaggio è 0 in quanto non dovranno essere contabilizzate come ore viaggio
									txtfldOreTotEffettive.setValue(oreTotGenerale);
								}
							}else
							
							if(Float.valueOf(deltaGiorno)>=0){
								txtfldDeltaViaggio.setValue(oreViaggio);
								//il delta viaggio è positivo e setto il giustificativo a ore viaggio
								smplcmbxAltroGiustificativo.setAllowBlank(false);
								txtfldOreTotEffettive.setValue(oreTotGenerale);
							}
							
						}else{
							txtfldRecupero.setValue(oreRecuperoOld);
							txtfldDeltaViaggio.setValue("0.00");
							txtfldOreTotEffettive.setValue("0.00");
							smplcmbxAltroGiustificativo.clear();
							smplcmbxAltroGiustificativo.setAllowBlank(true);
							smplcmbxAltroGiustificativo.setEmptyText("Giustificativo..");
						}					
					}	    		
			      }
			});
			
			txtfldDeltaViaggio.setEnabled(false);
			txtfldDeltaViaggio.setValue(result.getOreDeltaViaggio());
			txtfldDeltaViaggio.setFieldLabel("Delta Viaggio");
			
			txtfldOreTotEffettive.setEnabled(false);
			txtfldOreTotEffettive.setFieldLabel("Ore Totali");
			txtfldOreTotEffettive.setValue("0.00");
			if(txtfldOreViaggio.getValue().toString().compareTo("0.00")!=0){
				if(Float.valueOf(txtfldOreDelta.getValue().toString())<0){
					if(Float.valueOf(txtfldOreViaggio.getValue().toString())>Math.abs(Float.valueOf(txtfldOreDelta.getValue().toString()))){
						//il delta viaggio è positivo e setto il giustificativo a ore viaggio
						txtfldOreTotEffettive.setValue(txtfldOrePreviste.getValue().toString()+".00");
					}								
					if(Float.valueOf(txtfldOreViaggio.getValue().toString())<=Math.abs(Float.valueOf(txtfldOreDelta.getValue().toString()))){
						String oreTotGenerale=ClientUtility.aggiornaTotGenerale(txtfldTotGenerale.getValue().toString(), txtfldOreViaggio.getValue().toString());			
						txtfldOreTotEffettive.setValue(oreTotGenerale);
					}
				}else					
				if(Float.valueOf(txtfldOreDelta.getValue().toString())>=0){			
					txtfldOreTotEffettive.setValue(txtfldTotGenerale.getValue().toString());
				}	
			}
				
			if(Float.valueOf(result.getDelta())<0){		
				txtfldStraordinario.setEnabled(false);	
				smplcmbxAltroGiustificativo.setEnabled(true);
				//smplcmbxAltroGiustificativo.setAllowBlank(false);
				txtfldRecupero.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
				txtfldRecupero.getMessages().setRegexText("Deve essere un numero nel formato -99.59");			
			}
			if(Float.valueOf(result.getDelta())>0){		
				txtfldFerie.setEnabled(false);
				txtfldPermesso.setEnabled(false);
				smplcmbxAltroGiustificativo.setEnabled(true);	
				//smplcmbxAltroGiustificativo.setAllowBlank(false);
				txtfldRecupero.setRegex("[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
				txtfldRecupero.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
				
			}
			if(Float.valueOf(result.getDelta())==0){	
				txtfldFerie.setEnabled(false);
				txtfldPermesso.setEnabled(false);	
				txtfldStraordinario.setEnabled(false);	
				txtfldRecupero.setEnabled(false);
			}
						
			txtfldRecupero.setValue(result.getOreRecupero());
			txtfldRecupero.setAllowBlank(false);
			//txtfldRecupero.setEnabled(true);
			txtfldRecupero.setFieldLabel("Ore a Recupero");
			//chbxRecupero.setLabelSeparator("");
			txtfldRecupero.addKeyListener(new KeyListener(){
				 @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldFerie.getValue()==null)
							txtfldFerie.setValue("0.00");
						else{
							String valore= txtfldFerie.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldFerie.setValue(valore);
						}
					}	    		
			      }
			});
						
			smplcmbxAltroGiustificativo.setEmptyText("Giustificativo..");
			smplcmbxAltroGiustificativo.setFieldLabel("Giustificativo");
			for(String l : DatiComboBox.getGiustificativi()){
		    	smplcmbxAltroGiustificativo.add(l);}
			smplcmbxAltroGiustificativo.setTriggerAction(TriggerAction.ALL);
			if(result.getGiustificativo().compareTo("")!=0)
				smplcmbxAltroGiustificativo.setSimpleValue(result.getGiustificativo());		    
						
			smplcmbxAltroGiustificativo.addListener(Events.Select, new Listener<BaseEvent>(){
				@Override
				public void handleEvent(BaseEvent be) {	
					if(smplcmbxAltroGiustificativo.getRawValue().compareTo("23.Abbuono")==0){
						txtfldAbbuono.setEnabled(true);
						
						txtfldRecupero.setEnabled(false);
						txtfldRecupero.setValue("0.00");
						txtfldStraordinario.setEnabled(false);
						btnAssegnaOreStraordinario.setEnabled(false);
						btnAssegnaRecupero.setEnabled(false);
					}
					else{//TODO
						txtfldAbbuono.setEnabled(false);
						txtfldAbbuono.setValue("0.00");
						
						txtfldRecupero.setEnabled(true);
						txtfldRecupero.setValue("0.00");
						txtfldStraordinario.setEnabled(true);
						btnAssegnaOreStraordinario.setEnabled(true);
						btnAssegnaRecupero.setEnabled(true);
					}
				}		
			});
			
			
			txtfldFerie.setValue(result.getOreFerie());
			txtfldFerie.setAllowBlank(false);
			txtfldFerie.setFieldLabel("Ferie");
			//chbxFerie.setLabelSeparator("");
			txtfldFerie.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldFerie.getMessages().setRegexText("Deve essere un numero nel formato -99.59");
			txtfldFerie.addKeyListener(new KeyListener(){
				 @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldFerie.getValue()==null)
							txtfldFerie.setValue("0.00");
						else{
							String valore= txtfldFerie.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldFerie.setValue(valore);
						}
					}	    		
			      }
			});
			
			txtfldPermesso.setValue(result.getOrePermesso());
			txtfldPermesso.setAllowBlank(false);
			txtfldPermesso.setFieldLabel("Permesso(ROL)");
			//chbxPermesso.setLabelSeparator("");
			txtfldPermesso.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldPermesso.getMessages().setRegexText("Deve essere un numero nel formato -99.59");
			txtfldPermesso.addKeyListener(new KeyListener(){
				 @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldPermesso.getValue()==null)
							txtfldPermesso.setValue("0.00");
						else{
							String valore= txtfldPermesso.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldPermesso.setValue(valore);
						}
					}	    		
			      }
			});
			
			txtfldStraordinario.setValue(result.getOreStraordinario());
			txtfldStraordinario.setAllowBlank(false);
			txtfldStraordinario.setFieldLabel("Straordinario");		
			//chbxStraordinario.setLabelSeparator("");
			txtfldStraordinario.setRegex("[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldStraordinario.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldStraordinario.setWidth(50);
			txtfldStraordinario.addKeyListener(new KeyListener(){
				 @Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldStraordinario.getValue()==null)
							txtfldStraordinario.setValue("0.00");
						else{
							String valore= txtfldStraordinario.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldStraordinario.setValue(valore);
						}
					}	    		
			      }
			});
			
			
			txtfldAbbuono.setValue((String) result.get("oreAbbuono"));
			txtfldAbbuono.setAllowBlank(false);
			txtfldAbbuono.setFieldLabel("Abbuono/Scarto");		
			txtfldAbbuono.setRegex("[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldAbbuono.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldAbbuono.setWidth(50);
			if(txtfldAbbuono.getValue().compareTo("0.00")==0)
				txtfldAbbuono.setEnabled(false);
			else
				txtfldAbbuono.setEnabled(true);			
			txtfldAbbuono.addKeyListener(new KeyListener(){
				
				@Override
				public void componentKeyUp(ComponentEvent event) {
					
					String oreTot=txtfldTotGenerale.getValue().toString();
					String oreAbbuono=txtfldAbbuono.getValue().toString();
					String delta=new String();					
					List<String> listaParziali= new ArrayList<String>();
	    										
					LayoutContainer lc= new LayoutContainer(); 
		   			FldsetIntervalliIU fldsetIntervalliIU= new FldsetIntervalliIU();
		   			lc=(LayoutContainer) getParent().getParent();
		   			lc=(LayoutContainer) lc.getItemByItemId("left");
		   			fldsetIntervalliIU=(FldsetIntervalliIU) lc.getItemByItemId("fldSetIntervalliIU");
					
		    		delta=("-"+ txtfldOrePreviste.getValue()+".00");
		    	   	
		    		listaParziali.add(fldsetIntervalliIU.txtfldSomma1.getValue().toString());
		    		listaParziali.add(fldsetIntervalliIU.txtfldSomma2.getValue().toString());
		    		listaParziali.add(fldsetIntervalliIU.txtfldSomma3.getValue().toString());
		    		listaParziali.add(fldsetIntervalliIU.txtfldSomma4.getValue().toString());
		    		listaParziali.add(fldsetIntervalliIU.txtfldSomma5.getValue().toString());
					
					oreAbbuono="-" + oreAbbuono;					
					
					oreTot=ClientUtility.calcolaTempo(listaParziali);
					oreTot=ClientUtility.aggiornaTotGenerale(oreTot, oreAbbuono);
					
					txtfldTotGenerale.setValue(oreTot);
						    			    			    		
		    		delta=ClientUtility.calcoloDelta(oreTot, txtfldOrePreviste.getValue().toString());
		    		txtfldOreDelta.setValue(delta);
		    		txtfldRecupero.setValue(delta);
		    		
				}				
				
				@Override
			      public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldAbbuono.getValue()==null)
							txtfldAbbuono.setValue("0.00");
						else{
							String valore= txtfldAbbuono.getValue().toString();
													
							if(valore.compareTo("")==0)
								valore ="0.00";
							else
								if(valore.indexOf(".")==-1)
									valore=valore+".00";
								else{
									int index=valore.indexOf(".");
									int length=valore.length();
									
									if(valore.substring(index+1, length).length()==1)
										valore=valore+"0";		
									else if(valore.substring(index+1, length).length()==0)
										valore=valore+"00";
								}
							txtfldAbbuono.setValue(valore);
							String oreTot=txtfldTotGenerale.getValue().toString();
							String oreAbbuono=txtfldAbbuono.getValue().toString();
							String delta=new String();					
							List<String> listaParziali= new ArrayList<String>();
			    										
							LayoutContainer lc= new LayoutContainer(); 
				   			FldsetIntervalliIU fldsetIntervalliIU= new FldsetIntervalliIU();
				   			lc=(LayoutContainer) getParent().getParent();
				   			lc=(LayoutContainer) lc.getItemByItemId("left");
				   			fldsetIntervalliIU=(FldsetIntervalliIU) lc.getItemByItemId("fldSetIntervalliIU");
							
				    		delta=("-"+ txtfldOrePreviste.getValue()+".00");
				    	   	
				    		listaParziali.add(fldsetIntervalliIU.txtfldSomma1.getValue().toString());
				    		listaParziali.add(fldsetIntervalliIU.txtfldSomma2.getValue().toString());
				    		listaParziali.add(fldsetIntervalliIU.txtfldSomma3.getValue().toString());
				    		listaParziali.add(fldsetIntervalliIU.txtfldSomma4.getValue().toString());
				    		listaParziali.add(fldsetIntervalliIU.txtfldSomma5.getValue().toString());
							
							oreAbbuono="-" + oreAbbuono;					
							
							oreTot=ClientUtility.calcolaTempo(listaParziali);
							oreTot=ClientUtility.aggiornaTotGenerale(oreTot, oreAbbuono);
							
							txtfldTotGenerale.setValue(oreTot);
								    			    			    		
				    		delta=ClientUtility.calcoloDelta(oreTot, txtfldOrePreviste.getValue().toString());
				    		txtfldOreDelta.setValue(delta);
				    		txtfldRecupero.setValue(delta);
						
						}
												
					}	    		
			      }
				
				
			});

			
			txtrNoteAggiuntive.setEmptyText("Note Aggiuntive..");
			txtrNoteAggiuntive.setHeight(70);
			txtrNoteAggiuntive.setLabelSeparator("");
			txtrNoteAggiuntive.setMaxLength(250);
			txtrNoteAggiuntive.setValue(result.get("noteAggiuntive").toString());
					
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(320, 110);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setStyleAttribute("padding-top", "0px");
			cp.setStyleAttribute("padding-left", "15px");
			
			ContentPanel cp2= new ContentPanel();
			cp2.setHeaderVisible(false);
			cp2.setSize(320, 140);
			cp2.setBorders(false);
			cp2.setBodyBorder(false);
			cp2.setFrame(false);
			cp2.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp2.setStyleAttribute("padding-top", "0px");
			cp2.setStyleAttribute("padding-left", "15px");
			
			ContentPanel cp1= new ContentPanel();
			cp1.setHeaderVisible(false);
			cp1.setSize(320, 120);
			cp1.setBorders(false);
			cp1.setBodyBorder(false);
			cp1.setFrame(false);
			cp1.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp1.setStyleAttribute("padding-top", "5px");
			cp1.setStyleAttribute("padding-left", "15px");
					
			FormLayout layout= new FormLayout();
			layout.setLabelWidth(95);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol1.setLayout(layout);
			layoutCol1.setWidth(150);
					
			layout= new FormLayout();
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol2.setLayout(layout);
			layoutCol2.setWidth(55);
			
			layout= new FormLayout();
			layout.setLabelWidth(95);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol6.setLayout(layout);
			layoutCol6.setWidth(290);
			
			layout=new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutCol3.setLayout(layout);
						
			layout=new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutCol4.setLayout(layout);
			
			layout=new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutCol5.setLayout(layout);			
			/*
			Label lblStraordinario= new Label();
			lblStraordinario.setText("Straordinario: ");
			HorizontalPanel hpStr= new HorizontalPanel();
			hpStr.setSpacing(2);
			hpStr.add(lblStraordinario);
			hpStr.add(txtfldStraordinario);
			hpStr.add(btnScambioOre);
			
			Label lblFerie= new Label();
			lblFerie.setText("Ferie: ");
			HorizontalPanel hpFerie= new HorizontalPanel();
			hpFerie.setSpacing(2);
			hpFerie.add(lblFerie);
			hpFerie.add(txtfldFerie);
			hpFerie.add(btnAssegnaOreFerie);*/
						
			layoutCol1.add(txtfldFerie, new FormData("100%"));
			layoutCol1.add(txtfldPermesso, new FormData("100%"));
			layoutCol1.add(txtfldStraordinario, new FormData("100%"));		
			layoutCol1.add(txtfldRecupero, new FormData("100%"));
			
			layoutCol6.add(smplcmbxAltroGiustificativo, new FormData("100%"));
			layoutCol6.add(txtfldAbbuono, new FormData("20%"));			
			layoutCol6.add(txtrNoteAggiuntive, new FormData("100%"));
			
			layoutCol2.add(btnAssegnaOreFerie);
			layoutCol2.add(btnAssegnaOrePermesso);
			layoutCol2.add(btnAssegnaOreStraordinario);
			layoutCol2.add(btnAssegnaRecupero);
						
		    layoutCol3.add(txtfldOrePreviste, new FormData("50%"));
			layoutCol3.add(txtfldOreViaggio, new FormData("50%"));
			
			layoutCol4.add(txtfldOreDelta, new FormData("50%"));
			layoutCol4.add(txtfldOreTotEffettive, new FormData("50%"));
			
			layoutCol5.add(txtfldTotGenerale, new FormData("50%"));
			layoutCol5.add(txtfldDeltaViaggio, new FormData("50%"));			
			
			RowData data = new RowData(.50, 1);
			data.setMargins(new Margins(5));
					
			cp1.add(layoutCol3, new RowData(.33,1));
			cp1.add(layoutCol5, new RowData(.33,1));
			cp1.add(layoutCol4, new RowData(.33,1));
			
			cp.add(layoutCol1, data);
			cp.add(layoutCol2, data);
			
			cp2.add(layoutCol6,  new RowData(.2,1, new Margins(5)));
			
			add(cp1);
			add(cp);
			add(cp2);
			layout(true);
		}	
		
		protected boolean hasValueOreViaggio(TextField<String> field) {
		    return field.getValue() != null && field.isValid();
		}
	}

	
	public class FldsetRiepilogo extends FieldSet {
					
		private TextField<String> txtfldOreStraordinarioMese= new TextField<String>();
		private TextField<String> txtfldOreAssenzaMese=new TextField<String>();
		private TextField<String> txtfldOreLavorateMese= new TextField<String>();
		private TextField<String> txtfldOreRecuperoMonteMese= new TextField<String>();
		private TextField<String> txtfldOreRecuperoMonteTotale= new TextField<String>();
		
		private String username= new String();
		private Date data= new Date();
		
		public FldsetRiepilogo() {
			
			username=txtfldUsername.getValue().toString();
			data=dtfldGiorno.getValue();

			AdministrationService.Util.getInstance().loadRiepilogoMese(username, data, new AsyncCallback<RiepilogoOreModel>() {
				
				@Override
				public void onSuccess(RiepilogoOreModel result) {
					if(result==null)
						Window.alert("error: Impossibile accedere ai dati di riepilogo mensile.");
					else
						load(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on loadRiepilogoMese();");
				}
			});		
		}
		
		private void load(RiepilogoOreModel result) {
			
			setBorders(true);
			setHeading("Riepilogo Ore (Mensile).");
			setItemId("fldRiepilogo");
			setItemId("fldSetRiepilogo");
			
			LayoutContainer layoutCol1 = new LayoutContainer();

			FormLayout layout = new FormLayout();
			layout.setLabelWidth(135);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol1.setLayout(layout);

			ContentPanel cp = new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(320, 140);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			//cp.setStyleAttribute("padding-top", "10px");
			cp.setStyleAttribute("padding-left", "25px");

			txtfldOreStraordinarioMese.setFieldLabel("Ore Straordinario");
			txtfldOreStraordinarioMese.setEnabled(false);
			txtfldOreStraordinarioMese.setValue(result.getOreStraordinario());
			
			txtfldOreAssenzaMese.setFieldLabel("Ore Assenza");
			txtfldOreAssenzaMese.setEnabled(false);
			txtfldOreAssenzaMese.setValue(result.getOreAssenza());
			
			txtfldOreLavorateMese.setFieldLabel("Tot. Generale");
			txtfldOreLavorateMese.setEnabled(false);
			txtfldOreLavorateMese.setValue(result.getTotOreGenerale());

			txtfldOreRecuperoMonteMese.setFieldLabel("Ore a Recupero(Mese)");
			txtfldOreRecuperoMonteMese.setEnabled(false);
			txtfldOreRecuperoMonteMese.setValue(result.getOreRecupero());
			
			txtfldOreRecuperoMonteTotale.setFieldLabel("Ore a Recupero(Totale)");
			txtfldOreRecuperoMonteTotale.setEnabled(false);
			txtfldOreRecuperoMonteTotale.setValue(result.getOreRecuperoTotale());

			layoutCol1.add(txtfldOreLavorateMese, new FormData("95%"));
			layoutCol1.add(txtfldOreAssenzaMese, new FormData("95%"));
			//layoutCol1.add(txtfldOreStraordinarioMese, new FormData("95%"));
			layoutCol1.add(txtfldOreRecuperoMonteMese, new FormData("95%"));
			layoutCol1.add(txtfldOreRecuperoMonteTotale, new FormData("95%"));

			RowData data = new RowData(.8, 1);
			data.setMargins(new Margins(5));

			cp.add(layoutCol1, data);
			add(cp);
			layout(true);
		}
	}
	
	
	public class FldsetIntervalliCommesse extends FieldSet {

		private FormInserimentoIntervalloCommessa frmInsCommesse;
		private Text txtNoCommesse = new Text();
		private ButtonBar buttonBar = new ButtonBar();
		private Date data= new Date();
		
		public FldsetIntervalliCommesse() {
			
			setItemId("fldSetIntervalliC");
			setBorders(true);
			setHeading("Dettaglio Commesse.");
			setHeight(390);
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
			frmInsCommesse= new FormInserimentoIntervalloCommessa("2");
			
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
		FormInserimentoIntervalloCommessa frm=new FormInserimentoIntervalloCommessa("2");
		
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
	

	private List<String> elaboraIntervalliIU(FldsetIntervalliIU fldSetIntervalliIU) {
		
		TextField<String> txtfld;		
		List<String> listaIntervalli= new ArrayList<String>(10);
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld1I;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld1U;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld2I;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld2U;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld3I;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld3U;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld4I;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld4U;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld5I;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld5U;
		if(!txtfld.getRawValue().isEmpty()){
			listaIntervalli.add(txtfld.getValue().toString());
			listaIntervalli.add(txtfld.getData("sorgente").toString());
		}
		else {
			listaIntervalli.add("");
			listaIntervalli.add("");
		}
		
		return listaIntervalli;

	}	
}
