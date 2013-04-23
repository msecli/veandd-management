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

import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
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
import com.extjs.gxt.ui.client.widget.FramePanel;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
	private Button btnPrint= new Button();
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
						
		ContentPanel cntpnlFoglioOre = new ContentPanel(); //pannello esterno
		cntpnlFoglioOre.setHeading("Foglio Ore.");
		cntpnlFoglioOre.setHeaderVisible(true);
		cntpnlFoglioOre.setCollapsible(false);
		cntpnlFoglioOre.setBorders(false);
		cntpnlFoglioOre.setWidth(w-215);
		cntpnlFoglioOre.setHeight(h-55);
		cntpnlFoglioOre.setScrollMode(Scroll.AUTO);
		
		cntpnlFoglioOre.add(new CreateFormIntervalliOre());		
		cntpnlFoglioOre.add(txtfldUsername);
				
		bodyContainer.add(cntpnlFoglioOre);

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
		private Button btnSend= new Button("Load"); 
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
			
			btnMostraIntervalli.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
			ToolTipConfig config= new ToolTipConfig();
			config.setTitle("Dati rilevati dalla timbratrice:");
			config.setCloseable(true);
			config.setAnchor("left");
			config.setMouseOffset(new int[]{0, -5});
			//config.setMinWidth(150);
			btnMostraIntervalli.setToolTip(config);
			btnMostraIntervalli.setIconAlign(IconAlign.TOP);
			btnMostraIntervalli.setSize(26, 26);
			
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
						String controlloDati= new String(); //quando viene effettuato il controllo dei dati inseriti restituisce OK o errori
						
						username=cmbxDipendente.getValue().get("username");
						
						TextField<String> txtfldTotGenerale=new TextField<String>();
						TextField<String> txtfldDeltaGiorno=new TextField<String>();
						TextField<String> txtfldFerie=new TextField<String>();
						TextField<String> txtfldPermesso=new TextField<String>();
						TextField<String> txtfldStraordinario=new TextField<String>();
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
						String noteAggiuntive="";
						String oreRecuperoTot= txtfldRecuperoTotale.getValue().toString();
						String giustificativo=new String();
											
						if(!smplcmbxGiustificativo.getRawValue().isEmpty())
							giustificativo=smplcmbxGiustificativo.getRawValue().toString();
						else giustificativo="";
						if(!txtrNote.getRawValue().isEmpty())
							noteAggiuntive=txtrNote.getValue().toString();
						
						try {
							controlloDati=checkCoerenzaDatiInput(fldSetGiustificativi, fldSetIntervalliC);
						} catch (Exception e) {
							e.printStackTrace();
							controlloDati="error: Impossibile effettuare i controlli di correttezza sui dati inseriti.";
						}
						
						if(controlloDati.compareTo("OK")==0){
						 
						 AdministrationService.Util.getInstance().insertFoglioOreGiorno(username, giorno, totOreGenerale, delta, oreViaggio, oreAssRecupero, deltaOreViaggio, 
								 giustificativo, oreStraordinario, oreFerie, orePermesso, "0", intervalliIU, intervalliC, oreRecuperoTot, noteAggiuntive, new AsyncCallback<Boolean>() {

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
						else{
							Window.alert(controlloDati);
						}	
					 }				
			  }
			});
		}
		
		
		protected void reloadFoglioOre() {
			
			setHeight(915);
			setWidth(1060);
			
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
			
			pnlRiepilogo=new PanelRiepilogoMeseFoglioOre(cmbxDipendente.getValue().getUsername().toString(), giornoRiferimento.getValue(),"");
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
	

	private String checkCoerenzaDatiInput(FldsetGiustificativi fldSetGiustificativi, FldsetIntervalliCommesse fldSetIntervalliC) {
		String controllo= "OK";
		String totOreLavoro= "0.0";
		String totOreViaggio="0.0";
		String totOreGenerale=fldSetGiustificativi.txtfldTotGenerale.getValue();
		String delta=fldSetGiustificativi.txtfldOreDelta.getValue();
		String giustificativo = fldSetGiustificativi.smplcmbxAltroGiustificativo.getRawValue();
		String oreRecupero= fldSetGiustificativi.txtfldRecupero.getValue().toString();
		List<IntervalliCommesseModel> listaC= new ArrayList<IntervalliCommesseModel>();
		listaC= elaboraIntervalliC(fldSetIntervalliC);
		String oreStraordinario=fldSetGiustificativi.txtfldStraordinario.getValue();
		String orePermessi= fldSetGiustificativi.txtfldPermesso.getValue();
		String oreFerie= fldSetGiustificativi.txtfldFerie.getValue();
		String oreViaggio= fldSetGiustificativi.txtfldOreViaggio.getValue();
		//calcolo totale ore viaggio ore toali per tutte le commesse se ci sono

		if (listaC.size()>0){
			for(IntervalliCommesseModel c:listaC){
				totOreLavoro=ClientUtility.calcolaOreLavoro(totOreLavoro, c.getOreLavoro());		
			}
			if(totOreLavoro.compareTo(totOreGenerale)!=0)
				return controllo="Le ore indicate sulle commesse non sono coerenti con il numero di ore ricavate dagli intervalli I/U.";			
		}	
		
		
		if(oreViaggio.compareTo("0.00")==0){//Controlli di coerenza se non sono state inserite ore viaggio
			
			if(Float.valueOf(delta)>0){			
				String sommaStrRec= ClientUtility.sommaOreStrOreRec(oreStraordinario, oreRecupero);
				
				if(sommaStrRec.compareTo(delta)!=0 && giustificativo.compareTo("")==0)
					return controllo="E' stato indicato un numero di ore di straordinario o recupero diverso dal valore del delta giornaliero \n" +
							"ma non e' stato indicato un giustificativo.";
				else
				if(sommaStrRec.compareTo(delta)==0 && giustificativo.compareTo("")!=0)
					return controllo="E' stato indicato un numero di ore uguale al valore del delta giornaliero \n" +
							"ed e' anche stato indicato un giustificativo.";			
			}
			
			if(Float.valueOf(delta)<0){
				String sommaGiustificativi=ClientUtility.calcolaSommaGiustificativi(oreRecupero, orePermessi, oreFerie, "0.00");
				
				if(sommaGiustificativi.compareTo(delta)!=0 && giustificativo.compareTo("")==0)
					return controllo="E' stato indicato un numero di ore diverso dal valore del delta giornaliero \n" +
							"ma non e' stato indicato un giustificativo.";	
				else
					if(sommaGiustificativi.compareTo(delta)==0 && giustificativo.compareTo("")!=0)
						return controllo="E' stato indicato un numero di ore uguale al valore del delta giornaliero \n" +
								"ed e' anche stato indicato un giustificativo.";
			}		
		}
		
		return controllo;
	}
	
	
	public class FldsetIntervalliIU extends FieldSet {
						
		public TextField<String> txtfld1I = new TextField<String>();
		public TextField<String> txtfld1U = new TextField<String>();
		public TextField<String> txtfld2I = new TextField<String>();
		public TextField<String> txtfld2U = new TextField<String>();
		public TextField<String> txtfld3I = new TextField<String>();
		public TextField<String> txtfld3U = new TextField<String>();
		public TextField<String> txtfld4I = new TextField<String>();
		public TextField<String> txtfld4U = new TextField<String>();
		public TextField<String> txtfld5I = new TextField<String>();
		public TextField<String> txtfld5U = new TextField<String>();
		private TextField<String> txtfldSomma1= new TextField<String>();
		private TextField<String> txtfldSomma2= new TextField<String>();
		private TextField<String> txtfldSomma3= new TextField<String>();
		private TextField<String> txtfldSomma4= new TextField<String>();
		private TextField<String> txtfldSomma5= new TextField<String>();
		
		private String username= new String();
		private Date data= new Date();
		
		private Text txtErrore= new Text();
		
		public FldsetIntervalliIU() {

			caricaIntervalliTooTip();
			
			username=txtfldUsername.getValue().toString();//La ricerca la effettuo per username in quanto sicuramente univoco
			data=dtfldGiorno.getValue();
			
			AdministrationService.Util.getInstance().loadIntervalliIU(username, data, new AsyncCallback<List<IntervalliIUModel>>() {
				
				@Override
				public void onSuccess(List<IntervalliIUModel> result) {
					if(result.equals(null))
						Window.alert("error: Impossibile accedere ai dati sugli orari.");
					else
					if(result.isEmpty())
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
		 //   if(statoRevisione==1)txtfld1I.setEnabled(false);
			txtfld1I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {		    		   
			    	  //carico il fldSet riferito ai giustificativi
			    	  LayoutContainer lc= new LayoutContainer(); 
			    	  LayoutContainer right= new LayoutContainer();
		   			  FldsetGiustificativi fldsetGiustificativo;
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  right=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) right.getItemByItemId("fldSetGiustificativi");
			    	 		   			  
			    	  if(hasValue(txtfld1I)){ //se 1I � corretto
			    		  txtfld1I.clearInvalid();
		    			   txtfld1U.setAllowBlank(false); //1U deve essere compilato
		    			   if(hasValue(txtfld1U)){ //se 1U � corretto ricavo il parziale della differenza tra gli intervalli
		    				   String sommaIntervalli= new String();  		   
					    	   sommaIntervalli=ClientUtility.calcolaParzialeIntervalli(txtfld1I.getValue().toString(), txtfld1U.getValue().toString());
					    	   
					    	   if(sommaIntervalli.length()>5){//restituisce un errore se l'uscita � inferiore all'ingresso
					    		   txtErrore.setVisible(true);
					    		   txtErrore.setText(sommaIntervalli);
					    		   btnConferma.setEnabled(false);
					    		   disableField(txtfld1I);					    		   
					    	   }
					    	   else{	//se l'intervallo � corretto aggiorno il totale giornaliero   
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);		    		   
		    		   }
			      }
			 });		    
		    
			txtfld1U.setItemId("1U");
			txtfld1U.setFieldLabel("U");
			txtfld1U.setMaxLength(5);
			txtfld1U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld1U.getMessages().setRegexText("Deve essere nel formato 23:59");
		  //  if(statoRevisione==1)txtfld1U.setEnabled(false);
			txtfld1U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {		
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld1U)){
			    		   txtfld1U.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);    		   
		    		   }
			    	 }
				 });

			txtfld2I.setItemId("2I");
			txtfld2I.setFieldLabel("I");
			txtfld2I.setMaxLength(5);
			txtfld2I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld2I.getMessages().setRegexText("Deve essere nel formato 23:59");
		    //if(statoRevisione==1)txtfld2I.setEnabled(false);
			txtfld2I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld2I)){
			    		  txtfld2I.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   
		    		   }
		    	  }
			});
			
			txtfld2U.setItemId("2U");
			txtfld2U.setFieldLabel("U");
			txtfld2U.setMaxLength(5);
			txtfld2U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld2U.getMessages().setRegexText("Deve essere nel formato 23:59");
		  //  if(statoRevisione==1)txtfld2U.setEnabled(false);
			txtfld2U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld2U)){
		    			   txtfld2I.setAllowBlank(false);
		    			   txtfld2U.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    	  }
		    	   }
			 });

			txtfld3I.setItemId("3I");
			txtfld3I.setFieldLabel("I");
			txtfld3I.setMaxLength(5);
			txtfld3I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld3I.getMessages().setRegexText("Deve essere nel formato 23:59");
		  //  if(statoRevisione==1)txtfld3I.setEnabled(false);
			txtfld3I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  if(hasValue(txtfld3I)){	
			    		  txtfld3I.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		  
		    		   }
		    	   }
			 });

			txtfld3U.setItemId("3U");
			txtfld3U.setFieldLabel("U");
			txtfld3U.setMaxLength(5);
			txtfld3U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld3U.getMessages().setRegexText("Deve essere nel formato 23:59");
		  //  if(statoRevisione==1)txtfld3U.setEnabled(false);
			txtfld3U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			   lc=(LayoutContainer) getParent().getParent();
		   			   lc=(LayoutContainer) lc.getItemByItemId("right");
		   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld3U)){
			    		   txtfld3U.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);		    		   
		    		   }
		    	   }
			 });

			txtfld4I.setItemId("4I");
			txtfld4I.setFieldLabel("I");
			txtfld4I.setMaxLength(5);
			txtfld4I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld4I.getMessages().setRegexText("Deve essere nel formato 23:59");
		   // if(statoRevisione==1)txtfld4I.setEnabled(false);
			txtfld4I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld4I)){
			    		  txtfld4I.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		  
		    		   }
		    	   }
			 });

			txtfld4U.setItemId("4U");
			txtfld4U.setFieldLabel("U");
			txtfld4U.setMaxLength(5);
			txtfld4U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld4U.getMessages().setRegexText("Deve essere nel formato 23:59");
		   // if(statoRevisione==1)txtfld4U.setEnabled(false);
			txtfld4U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			   FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			   lc=(LayoutContainer) getParent().getParent();
		   			   lc=(LayoutContainer) lc.getItemByItemId("right");
		   			   fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld4U)){
			    		   txtfld4U.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		  
		    		   }		    	     
			      }
			 });

			txtfld5I.setItemId("5I");
			txtfld5I.setFieldLabel("I");
			txtfld5I.setMaxLength(5);
			txtfld5I.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld5I.getMessages().setRegexText("Deve essere nel formato 23:59");
		   // if(statoRevisione==1)txtfld5I.setEnabled(false);
			txtfld5I.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
			    	  
			    	  if(hasValue(txtfld5I)){
			    		  txtfld5I.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   
		    		   }
		    	   }
			 });

			txtfld5U.setItemId("5U");
			txtfld5U.setFieldLabel("U");
			txtfld5U.setMaxLength(5);
			txtfld5U.setRegex("[0-1][0-9]:[0-5][0-9]|[0-9]{1}:[0-5][0-9]|[2][0-3]:[0-5][0-9]");
		    txtfld5U.getMessages().setRegexText("Deve essere nel formato 23:59");
		   // if(statoRevisione==1)txtfld5U.setEnabled(false);
			txtfld5U.addKeyListener(new KeyListener() {
			      public void componentKeyUp(ComponentEvent event) {	
			    	  LayoutContainer lc= new LayoutContainer(); 
		   			  FldsetGiustificativi fldsetGiustificativo= new FldsetGiustificativi();
		   			  lc=(LayoutContainer) getParent().getParent();
		   			  lc=(LayoutContainer) lc.getItemByItemId("right");
		   			  fldsetGiustificativo=(FldsetGiustificativi) lc.getItemByItemId("fldSetGiustificativi");
		   			   
			    	  if(hasValue(txtfld5U)){
			    		  txtfld5U.clearInvalid();
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
			    		   fldsetGiustificativo.txtfldRecupero.setValue(delta);
			    		   fldsetGiustificativo.txtfldTotGenerale.setValue(totale);	
			    		   fldsetGiustificativo.txtfldOreDelta.setValue(delta);
			    		   
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
			}
			else{	
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
			
			int size= result.size();
			
			for(int i =0; i<size; i++){	
				final String movimento=result.get(i).getMovimento();
				
				if (movimento.compareTo("i1")==0){
					txtfld1I.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("u1")==0) {
					txtfld1U.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("i2")==0){
					txtfld2I.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("u2")==0) {
					txtfld2U.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("i3")==0) {
					txtfld3I.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("u3")==0) {
					txtfld3U.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("i4")==0) {
					txtfld4I.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("u4")==0) {
					txtfld4U.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("i5")==0) {
					txtfld5I.setValue(result.get(i).getOrario());
					//break;
				}
				
				if (movimento.compareTo("u5")==0) {
					txtfld5U.setValue(result.get(i).getOrario());
					//break;
				}			
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
			
			String toolTip="";
			for(int i=0; i<result.size(); i+=2){
				toolTip=(toolTip+result.get(i)+": ");
				toolTip=(toolTip+result.get(i+1)+" | ");
			}
			btnMostraIntervalli.setToolTip(toolTip);
		}
	}
	
	
	public class FldsetGiustificativi extends FieldSet {	
		public SimpleComboBox<String> smplcmbxAltroGiustificativo=new SimpleComboBox<String>();
		public TextField<String> txtfldFerie=new TextField<String>();
		public TextField<String> txtfldStraordinario=new TextField<String>();
		public TextField<String> txtfldPermesso=new TextField<String>();
		public TextField<String> txtfldRecupero=new TextField<String>();
		public TextField<String> txtfldExtFest=new TextField<String>();
		public TextField<String> txtfldOreDelta= new TextField<String>();
		public TextField<String> txtfldOrePreviste= new TextField<String>();
		public TextField<String> txtfldTotGenerale= new TextField<String>();
		public TextField<String> txtfldOreViaggio= new TextField<String>();
		public TextField<String> txtfldDeltaViaggio= new TextField<String>();
		public TextField<String> txtfldOreTotEffettive= new TextField<String>();
		public TextArea txtrNoteAggiuntive= new TextArea();
		
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
		}	
		
		private void load(GiustificativiModel result) {
			
			final NumberFormat number = NumberFormat.getFormat("0.00");
			
			if(result.getStatoRevisione().compareTo("0")!=0){
				
				btnConferma.setEnabled(false);
			}
			
			setBorders(true);
			setHeading("Giustificativo.");
			setItemId("fldSetGiustificativi");
			
			LayoutContainer layoutCol1=new LayoutContainer();
			LayoutContainer layoutCol2=new LayoutContainer();
			LayoutContainer layoutCol3=new LayoutContainer();
			LayoutContainer layoutCol4=new LayoutContainer();
			LayoutContainer layoutCol5=new LayoutContainer();
			
			txtfldOrePreviste.setName("orePreviste");
			txtfldOrePreviste.setEnabled(false);
			txtfldOrePreviste.setFieldLabel("Ore Previste");	
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
								//il delta viaggio � positivo e setto il giustificativo a ore viaggio
								smplcmbxAltroGiustificativo.setAllowBlank(false);
								smplcmbxAltroGiustificativo.setSimpleValue("27.Ore Viaggio");
								txtfldOreTotEffettive.setValue(orePreviste);
							}								
							if(Float.valueOf(oreViaggio)<=Math.abs(Float.valueOf(deltaGiorno))){
								oreTotGenerale=ClientUtility.aggiornaTotGenerale(oreTotGenerale, oreViaggio);
								//deltaGiorno=;
								//Resta un residuo di ore a recupero
								txtfldRecupero.setValue(number.format(Float.valueOf(ClientUtility.calcoloDelta(oreTotGenerale, orePreviste))));
								txtfldDeltaViaggio.setValue("0.00");//deltaViaggio � 0 in quanto non dovranno essere contabilizzate come ore viaggio
								txtfldOreTotEffettive.setValue(oreTotGenerale);
							}
						}else
						
						if(Float.valueOf(deltaGiorno)>=0){
							txtfldDeltaViaggio.setValue(oreViaggio);
							//il delta viaggio � positivo e setto il giustificativo a ore viaggio
							smplcmbxAltroGiustificativo.setAllowBlank(false);
							txtfldOreTotEffettive.setValue(oreTotGenerale);
						}
						
					}else{
						txtfldRecupero.setValue(oreRecuperoOld);
						txtfldDeltaViaggio.setValue("0.00");
						txtfldOreTotEffettive.setValue("0.00");
						smplcmbxAltroGiustificativo.setAllowBlank(true);
						smplcmbxAltroGiustificativo.clear();
						smplcmbxAltroGiustificativo.setEmptyText("Giustificativo..");
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
						//il delta viaggio � positivo e setto il giustificativo a ore viaggio
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
			
			//smplcmbxAltroGiustificativo.setEnabled(true);
			smplcmbxAltroGiustificativo.setEmptyText("Giustificativo..");
			smplcmbxAltroGiustificativo.setFieldLabel("Giustificativo");
			for(String l : DatiComboBox.getGiustificativi()){
		    	smplcmbxAltroGiustificativo.add(l);}
			smplcmbxAltroGiustificativo.setTriggerAction(TriggerAction.ALL);
			if(result.getGiustificativo().compareTo("")!=0)
				smplcmbxAltroGiustificativo.setSimpleValue(result.getGiustificativo());		    
			
			txtfldFerie.setValue(result.getOreFerie());
			txtfldFerie.setAllowBlank(false);
			txtfldFerie.setFieldLabel("Ferie");
			//chbxFerie.setLabelSeparator("");
			txtfldFerie.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldFerie.getMessages().setRegexText("Deve essere un numero nel formato -99.59");
			
			txtfldPermesso.setValue(result.getOrePermesso());
			txtfldPermesso.setAllowBlank(false);
			txtfldPermesso.setFieldLabel("Permesso(ROL)");
			//chbxPermesso.setLabelSeparator("");
			txtfldPermesso.setRegex("[-]{1}[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldPermesso.getMessages().setRegexText("Deve essere un numero nel formato -99.59");
			
			txtfldStraordinario.setValue(result.getOreStraordinario());
			txtfldStraordinario.setAllowBlank(false);
			txtfldStraordinario.setFieldLabel("Straordinario");		
			//chbxStraordinario.setLabelSeparator("");
			txtfldStraordinario.setRegex("[0-9]+[.]?[0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldStraordinario.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			
			txtrNoteAggiuntive.setEmptyText("Note Aggiuntive..");
			txtrNoteAggiuntive.setHeight(70);
			txtrNoteAggiuntive.setLabelSeparator("");
			txtrNoteAggiuntive.setMaxLength(250);
			txtrNoteAggiuntive.setValue(result.get("noteAggiuntive").toString());
			
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(320, 210);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setStyleAttribute("padding-top", "0px");
			cp.setStyleAttribute("padding-left", "15px");
			
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
			layoutCol1.setWidth(290);
					
			layout= new FormLayout();
			layout.setLabelWidth(10);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol2.setLayout(layout);
			layoutCol2.setWidth(55);
			
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
								
			layoutCol1.add(txtfldFerie, new FormData("50%"));
			layoutCol1.add(txtfldPermesso, new FormData("50%"));
			layoutCol1.add(txtfldStraordinario, new FormData("50%"));
			layoutCol1.add(txtfldRecupero, new FormData("50%"));
			layoutCol1.add(smplcmbxAltroGiustificativo, new FormData("100%"));
			layoutCol1.add(txtrNoteAggiuntive, new FormData("100%"));
			
		    layoutCol3.add(txtfldOrePreviste, new FormData("50%"));
			layoutCol3.add(txtfldOreViaggio, new FormData("50%"));
			
			layoutCol4.add(txtfldOreDelta, new FormData("50%"));
			layoutCol4.add(txtfldOreTotEffettive, new FormData("50%"));
			
			layoutCol5.add(txtfldTotGenerale, new FormData("50%"));
			layoutCol5.add(txtfldDeltaViaggio, new FormData("50%"));	
			
			RowData data = new RowData(.2, 1);
			data.setMargins(new Margins(5));
			
			cp1.add(layoutCol3, new RowData(.33,1));
			cp1.add(layoutCol5, new RowData(.33,1));
			cp1.add(layoutCol4, new RowData(.33,1));
			
			cp.add(layoutCol1, data);
			cp.add(layoutCol2, data);
			
			add(cp1);
			add(cp);
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
	

	private List<String> elaboraIntervalliIU(FldsetIntervalliIU fldSetIntervalliIU) {
		
		/*TextField<String> txtfld;		
		List<String> listaIntervalli= new ArrayList<String>(10);
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld1I;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld1U;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld2I;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld2U;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld3I;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld3U;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld4I;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld4U;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld5I;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		txtfld=new TextField<String>();
		txtfld=fldSetIntervalliIU.txtfld5U;
		if(!txtfld.getRawValue().isEmpty())
			listaIntervalli.add(txtfld.getValue().toString());
		else listaIntervalli.add("");
		
		return listaIntervalli;*/
		
				TextField<String> txtfld;		
				List<String> listaIntervalli= new ArrayList<String>(10);
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld1I;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM"); //varia a seconda di chi stia correggendo l'intervallo
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld1U;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld2I;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld2U;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld3I;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld3U;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld4I;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld4U;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld5I;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				txtfld=new TextField<String>();
				txtfld=fldSetIntervalliIU.txtfld5U;
				if(!txtfld.getRawValue().isEmpty()){
					listaIntervalli.add(txtfld.getValue().toString());
					if(txtfldRuolo.getValue().compareTo("PM")==0)
						listaIntervalli.add("PM");
					else
						listaIntervalli.add("GP");
				}
				else {
					listaIntervalli.add("");
					listaIntervalli.add("");
				}
				
				return listaIntervalli;
	}	
}