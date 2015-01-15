package gestione.pack.client.layout;

//import gestione.pack.client.utility.RecuperoParametriSessione;

import java.util.List;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.UtilityService;
import gestione.pack.client.layout.panel.DialogRichiestaHardwareDipendente;
import gestione.pack.client.layout.panel.PanelAbilitazioneStraordinarioDip;
import gestione.pack.client.layout.panel.PanelAnagraficaHardware;
import gestione.pack.client.layout.panel.PanelEditPasswordUtenti;
import gestione.pack.client.layout.panel.PanelGestioneCosting;
import gestione.pack.client.layout.panel.PanelInsertRTV;
import gestione.pack.client.layout.panel.PanelMensileOrdini;
import gestione.pack.client.layout.panel.PanelPrintAll;
import gestione.pack.client.layout.panel.PanelProtocolloCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoAnnualeOreDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoCostiDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseGiornalieroHorizontal;
import gestione.pack.client.layout.panel.PanelRiepilogoOreDipendentiDettCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoOreDipendentiPerCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoRichiesteHardware;
import gestione.pack.client.layout.panel.PanelRiepilogoSituazioneMensileOreDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoStatoAvanzamentoOreCommesse;
import gestione.pack.client.layout.panel.PanelSaturazioneRisorsePerSede;
import gestione.pack.client.layout.panel.PanelStrumentiAmministrativi_TabellaFogliFatturazione;
import gestione.pack.client.layout.panel.PanelToolAmministrativi;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class BodyLayout_Administration extends LayoutContainer {
	
	private ContentPanel center;
	private Text txtUsername = new Text();
	private Text txtCheck= new Text();
	private String listaCheck= new String();
	
	private SimpleComboBox<String> smplcmbxPeriodo= new SimpleComboBox<String>();
	
	public TextField<String> txtfldUsername= new TextField<String>();
	public TextField<String> txtfldRuolo= new TextField<String>();
	
	private int w=Window.getClientWidth();
	protected Status status;
  	
	public BodyLayout_Administration() {
		setBorders(false);
	}

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	    
	    Viewport viewport = new Viewport();
		final BorderLayout layout = new BorderLayout();
		LayoutContainer container = new LayoutContainer();
				
		viewport.setLayout(new FitLayout());
		viewport.setBorders(false);
		container.setLayout(layout);
		container.setBorders(false);
		
		viewport.setStyleAttribute("padding", "3px");
		ContentPanel north = new ContentPanel();
		ContentPanel west = new ContentPanel();			
		center= new ContentPanel();
		
		center.setScrollMode(Scroll.AUTO);
		center.setHeaderVisible(false);
		center.setLayoutOnChange(true);
		
		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 30);
		northData.setCollapsible(true);
		north.setHeaderVisible(false);
		northData.setFloatable(true);
		northData.setHideCollapseTool(true);
		northData.setSplit(false);
		northData.setMargins(new Margins(0, 0, 5, 0));

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 184);
		westData.setSplit(false);
		westData.setCollapsible(true);
		west.setScrollMode(Scroll.AUTO);
		westData.setMargins(new Margins(0, 5, 0, 0));

		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0));

		ToolBar toolBar = new ToolBar();
		toolBar.setHeight("30px");
					
		txtUsername.setHeight("23px");
		txtUsername.setWidth(150);
		txtUsername.setStyleAttribute("font-size", "10px");
		txtUsername.setStyleAttribute("padding-top", "4px");
		txtUsername.setStyleAttribute("padding-left", "3px");
		txtUsername.setStyleAttribute("text-color", "#858585");
		
		txtfldUsername.setVisible(false);	
		txtfldRuolo.setVisible(false);
		
		String s= new String();
		s=txtfldUsername.getValue().toString();
		
		String nome=new String();
		String cognome=new String();
		int i=s.indexOf(".");
				
		nome=s.substring(0,i);
		cognome=s.substring(i+1,s.length());
		nome=nome.substring(0,1).toUpperCase()+nome.substring(1,nome.length());
		cognome=cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length());
		
		txtUsername.setText(" "+nome+" "+cognome);	
		
		Button btnSetting=new Button();
		btnSetting.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				final Dialog d= new Dialog();
				d.setSize(500, 500);
				d.setPosition(w-500, 0);
				d.setButtons("");
				d.setStyleAttribute("margin", "10");
				d.setUrl(ConstantiMSG.URLAggiornamenti);				
				d.show();
			}
		});
		btnSetting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.setting()));
		
		Button btnLogout= new Button();
		btnLogout.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.logout()));
		btnLogout.setToolTip("LogOut");
		btnLogout.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {     
	        	SessionManagementService.Util.getInstance().logOut( new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						Window.open(ClientUtility.getURLRedirect(),"_self","1");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on LogOut();");					
					}
				});
	        }
	    });
		
		ButtonBar buttonBar= new ButtonBar();
		buttonBar.setAlignment(HorizontalAlignment.RIGHT);
		buttonBar.setWidth(w-205);
		buttonBar.add(btnLogout);
		buttonBar.add(btnSetting);
		
		Button btnLoginIcon= new Button();
		btnLoginIcon.setSize(18, 18);
		btnLoginIcon.setEnabled(false);
		btnLoginIcon.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.login()));
		btnLoginIcon.setToolTip("LogOut");
		
		toolBar.add(txtfldUsername);
		toolBar.add(txtfldRuolo);
		toolBar.add(btnLoginIcon);
		toolBar.add(txtUsername);
				
		
		/*Menu menu = new Menu();  
		  
		MenuItem item1 = new MenuItem("Strumenti");  
		menu.add(item1);
		
		MenuBar bar = new MenuBar();  
		bar.setBorders(true);  
		bar.setStyleAttribute("borderTop", "none");  
		bar.add(new MenuBarItem("Strumenti", menu));  
		
		toolBar.add(bar);
		*/
		
				
		toolBar.add(buttonBar);
		north.setTopComponent(toolBar); 
		

//Menu laterale	-----------------------------------------------------------------------------------	
	    
	    ContentPanel panel = new ContentPanel();
	    panel.setHeaderVisible(false);
	    panel.setBodyBorder(false);	    
	    panel.setLayout(new AccordionLayout());
	    
	    ContentPanel cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Presenze");
	    cp.setExpanded(true);
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);     */          
            }
        });
	    
	    Button btnGestionePresenze = new Button();
	    btnGestionePresenze.setToolTip("Rilevazione Presenze");
	    btnGestionePresenze.setHeight(65);
	    btnGestionePresenze.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenze()));
	    btnGestionePresenze.setIconAlign(IconAlign.BOTTOM);
	    btnGestionePresenze.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);}      
	      });
	    btnGestionePresenze.setWidth("100%");
	    cp.add(btnGestionePresenze);
	    
	    Button btnPresenzeDipendenti = new Button();
	    btnPresenzeDipendenti.setToolTip("Rilevazione Presenze Dipendenti");
	    btnPresenzeDipendenti.setHeight(65);
	    btnPresenzeDipendenti.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenzeDip()));
	    btnPresenzeDipendenti.setIconAlign(IconAlign.BOTTOM);
	    btnPresenzeDipendenti.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FoglioOreSelectDipendenti());
	        	center.layout(true);}      
	      });
	    btnPresenzeDipendenti.setWidth("100%");
	    cp.add(btnPresenzeDipendenti);
	    
	    Button btnRilevColocation = new Button();
	    btnRilevColocation.setToolTip("Rilevazione Presenze Colocation/Collaboratori");
	    btnRilevColocation.setHeight(65);
	    btnRilevColocation.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.rilevColocation()));
	    btnRilevColocation.setIconAlign(IconAlign.BOTTOM);
	    btnRilevColocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_RilevazioneOreColocation());
	        	center.layout(true);}      
	      });
	    btnRilevColocation.setWidth("100%");
	    cp.add(btnRilevColocation);	    
	    
	    Button btnRiepilogoMeseDip = new Button();
	    btnRiepilogoMeseDip.setToolTip("Riepilogo Situazione Mensile Dipendenti");
	    btnRiepilogoMeseDip.setHeight(65);
	    btnRiepilogoMeseDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepilogoMeseDip.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMeseDip.setWidth("100%");
	    btnRiepilogoMeseDip.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoSituazioneMensileOreDipendenti());
	        	center.layout(true);}      
	      });
	   
	    cp.add(btnRiepilogoMeseDip);	    
	    
	    Button btnLoadTimbrature = new Button();
	    btnLoadTimbrature.setToolTip("Upload Timbrature");
	    btnLoadTimbrature.setHeight(64);
	    btnLoadTimbrature.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.upload()));
	    btnLoadTimbrature.setIconAlign(IconAlign.BOTTOM);
	    btnLoadTimbrature.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FileTimbrature());
	        	center.layout(true);}      
	      });
	    btnLoadTimbrature.setWidth("100%");
	    cp.add(btnLoadTimbrature);
	         
	    Button btnPrintRiepilogo = new Button();
	    btnPrintRiepilogo.setToolTip("Stampa");
	    btnPrintRiepilogo.setHeight(65);
	    btnPrintRiepilogo.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.printBig()));
	    btnPrintRiepilogo.setIconAlign(IconAlign.BOTTOM);
	    btnPrintRiepilogo.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelPrintAll());
	        	center.layout(true);}      
	      });
	    btnPrintRiepilogo.setWidth("100%");
	    cp.add(btnPrintRiepilogo);	   	    

	    Button btnRiepilogoMeseHorizontal = new Button();
	    btnRiepilogoMeseHorizontal.setToolTip("Riepilogo Dettagliato Compilazione");
	    btnRiepilogoMeseHorizontal.setHeight(65);
	    btnRiepilogoMeseHorizontal.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepilogoMeseHorizontal.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMeseHorizontal.setWidth("100%");
	    btnRiepilogoMeseHorizontal.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoMeseGiornalieroHorizontal());
	        	center.layout(true);}      
	      });
	   
	    cp.add(btnRiepilogoMeseHorizontal);
	    
	    cp.setButtonAlign(HorizontalAlignment.CENTER);
	    cp.setExpanded(true);	    	    
	    panel.add(cp);
	    
	    
	    cp = new ContentPanel();
	    cp.setExpanded(false);
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Personale");  
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new PanelGestioneCostiDipendenti());
	        	center.layout(true);*/               
            }
        });
	    Button btnAnagrP = new Button();
	    btnAnagrP.setToolTip("Anagrafica Personale");
	    btnAnagrP.setHeight(65);
	    btnAnagrP.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnAnagrP.setIconAlign(IconAlign.BOTTOM);
	    btnAnagrP.setWidth("100%");
	    btnAnagrP.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_AnagraficaPersonale());
	        	center.layout(true);}
	        
	      });
	    btnAnagrP.setWidth("100%");
	    cp.add(btnAnagrP);
	   
	   /* Button btnGestioneCostiDip = new Button();
	    btnGestioneCostiDip.setToolTip("Gestione Costi Dipendenti");
	    btnGestioneCostiDip.setHeight(65);
	    btnGestioneCostiDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenzeDip()));
	    btnGestioneCostiDip.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneCostiDip.setWidth("100%");
	    btnGestioneCostiDip.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelGestioneCostiDipendenti());
	        	center.layout(true);}
	        
	      });
	    btnAnagrP.setWidth("100%");
	    cp.add(btnGestioneCostiDip);*/
	    
	    Button btnRiepilogoCostiDip = new Button();
	    btnRiepilogoCostiDip.setToolTip("Riepilogo Costi");
	    btnRiepilogoCostiDip.setHeight(65);
	    btnRiepilogoCostiDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepilogoCostiDip.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoCostiDip.setWidth("100%");
	    btnRiepilogoCostiDip.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoCostiDipendenti());
	        	center.layout(true);}
	        
	      });
	    btnAnagrP.setWidth("100%");
	    cp.add(btnRiepilogoCostiDip);
	    
	    
	    Button btnAbilitazioneStrao = new Button();
	    btnAbilitazioneStrao.setToolTip("Abilitazione Straordinari");
	    btnAbilitazioneStrao.setHeight(65);
	    btnAbilitazioneStrao.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep1()));
	    btnAbilitazioneStrao.setIconAlign(IconAlign.BOTTOM);
	    btnAbilitazioneStrao.setWidth("100%");
	    btnAbilitazioneStrao.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelAbilitazioneStraordinarioDip());
	        	center.layout(true);}
	        
	      });
	    cp.add(btnAbilitazioneStrao);
	    
	    panel.add(cp);
   
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Clienti");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new CenterLayout_AnagraficaClienti());
	        	center.layout(true);  */             
            }
        });
	    Button btnAnagrC = new Button();
	    btnAnagrC.setToolTip("Anagrafica Clienti");
	    btnAnagrC.setHeight(65);
	    btnAnagrC.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnAnagrC.setIconAlign(IconAlign.BOTTOM);
	    btnAnagrC.setWidth("100%");
	    btnAnagrC.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	          center.add(new CenterLayout_AnagraficaClienti());
	          center.layout(true);}
	        
	      });
	    btnAnagrC.setWidth("100%");
	    cp.add(btnAnagrC);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Ordini");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCompleta());
	        	center.layout(true);*/
            }
        });
	    Button btnGestioneRdo = new Button();
	    btnGestioneRdo.setToolTip("Gestione Dati RdO");
	    btnGestioneRdo.setHeight(65);
	    btnGestioneRdo.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnGestioneRdo.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneRdo.setWidth("100%");
	    btnGestioneRdo.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCompleta());
	        	center.layout(true);}
	        
	      });
	    btnGestioneRdo.setWidth("100%");
	    cp.add(btnGestioneRdo);
	    	    
	    Button btnGestioneRdoAll = new Button();
	    btnGestioneRdoAll.setToolTip("All Toghether");
	    btnGestioneRdoAll.setHeight(65);
	    btnGestioneRdoAll.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnGestioneRdoAll.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneRdoAll.setWidth("100%");
	    btnGestioneRdoAll.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCommesseAll());
	        	center.layout(true);}
	        
	      });
	    cp.add(btnGestioneRdoAll);
	    
	    Button btnRiepilogoMensile = new Button();
	    btnRiepilogoMensile.setToolTip("Riepilogo Mensile");
	    btnRiepilogoMensile.setHeight(65);
	    btnRiepilogoMensile.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepilogoMensile.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMensile.setWidth("100%");
	    btnRiepilogoMensile.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-130, h-65);
	        	d.add(new PanelMensileOrdini("AMM"));
	        	d.setHeading("Mensile");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.AUTO);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        	
	        	}	        
	      });
	    cp.add(btnRiepilogoMensile);
	    
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Commesse");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new CenterLayout_GestioneCommessa());
	        	center.layout(true);   */            
            }
        });
	   
	    Button btnGestioneCommessa = new Button();
	    btnGestioneCommessa.setToolTip("Gestione Dati Commessa");
	    btnGestioneCommessa.setHeight(65);
	    btnGestioneCommessa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnGestioneCommessa.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneCommessa.setWidth("100%");
	    btnGestioneCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_GestioneCommessa());
	        	center.layout(true);}
	        
	      });
	    btnGestioneCommessa.setWidth("100%");
	    cp.add(btnGestioneCommessa);
	    
	    Button btnAssociaPersonale = new Button();
	    btnAssociaPersonale.setToolTip("Associazione Dipendenti Commessa");
	    btnAssociaPersonale.setHeight(65);
	    btnAssociaPersonale.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.associaPtoC()));
	    btnAssociaPersonale.setIconAlign(IconAlign.BOTTOM);
	    btnAssociaPersonale.setWidth("100%");
	    btnAssociaPersonale.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_AssociaPersonale());
	        	center.layout(true);}
	        
	      });
	    btnAssociaPersonale.setWidth("100%");
	    cp.add(btnAssociaPersonale);
	    
	    Button btnRiepiloghiSalPcl = new Button();
	    btnRiepiloghiSalPcl.setToolTip("Riepilogo Sal/Pcl/Non Fatturabili");
	    btnRiepiloghiSalPcl.setHeight(65);
	    btnRiepiloghiSalPcl.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepiloghiSalPcl.setIconAlign(IconAlign.BOTTOM);
	    btnRiepiloghiSalPcl.setWidth("100%");
	    btnRiepiloghiSalPcl.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	/*center.removeAll();
	        	center.add(new CenterLayout_RiepiloghiSalPcl());
	        	center.layout(true);*/
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-100, h-75);
	        	CenterLayout_RiepiloghiSalPcl cl= new CenterLayout_RiepiloghiSalPcl();
	        	cl.setRuolo("AMM");
	        	d.add(cl);
	        	d.setHeading("Riepilogo Sal/Pcl/Non Fatturabili");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.NONE);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        }        
	      });
	    cp.add(btnRiepiloghiSalPcl);
	    
	    Button btnGestioneCosting = new Button();
	    btnGestioneCosting.setToolTip("Costing");
	    btnGestioneCosting.setHeight(65);
	    btnGestioneCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep1()));
	    btnGestioneCosting.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneCosting.setWidth("100%");
	    btnGestioneCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelGestioneCosting());
	        	center.layout(true);
	        }
	    });
	    cp.add(btnGestioneCosting);
	    
	    Button btnRiepilogoSaturazione = new Button();
	    btnRiepilogoSaturazione.setToolTip("Riepilogo dati saturazione risorse");
	    btnRiepilogoSaturazione.setHeight(65);
	    btnRiepilogoSaturazione.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensPers()));
	    btnRiepilogoSaturazione.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoSaturazione.setWidth("100%");
	    btnRiepilogoSaturazione.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	Dialog d= new Dialog();
	        	d.setSize(1430, 980);
	        	d.add(new PanelSaturazioneRisorsePerSede());
	        	d.setHeading("Riepilogo dati saturazione risorse");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.NONE);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();	        	
	        }        
	      });
	    cp.add(btnRiepilogoSaturazione);
	    
	    Button btnProtocolloCommesse = new Button();
	    btnProtocolloCommesse.setToolTip("Protocollo commesse");
	    btnProtocolloCommesse.setHeight(65);
	    btnProtocolloCommesse.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.generate1()));
	    btnProtocolloCommesse.setIconAlign(IconAlign.BOTTOM);
	    btnProtocolloCommesse.setWidth("100%");
	    btnProtocolloCommesse.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	Dialog d= new Dialog();
	        	d.setSize(1430, 980);
	        	d.add(new PanelProtocolloCommesse());
	        	d.setHeading("Protocollo Commesse");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.NONE);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();	        	
	        }        
	    });
	    cp.add(btnProtocolloCommesse);
    
	    panel.add(cp);
	         
	    	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Riepilogo Ore");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new PanelRiepilogoOreDipendentiPerCommesse());
	        	center.layout(true);     */          
            }
        });
	    
	    Button btnRiepilogoMese = new Button();
	    btnRiepilogoMese.setToolTip("Riepilogo dei dati ore mensili dei dipendenti (con dettaglio commesse)");
	    btnRiepilogoMese.setHeight(65);
	    btnRiepilogoMese.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensPers()));
	    btnRiepilogoMese.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMese.setWidth("100%");
	    btnRiepilogoMese.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoOreDipendentiPerCommesse());
	        	center.layout(true);}      
	      });
	    btnRiepilogoMese.setWidth("100%");
	    cp.add(btnRiepilogoMese);
	    
	    Button btnRiepilogoAnnuale = new Button();
	    btnRiepilogoAnnuale.setToolTip("Report Annuale");
	    btnRiepilogoAnnuale.setHeight(65);
	    btnRiepilogoAnnuale.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepilogoAnnuale.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoAnnuale.setWidth("100%");
	    btnRiepilogoAnnuale.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoAnnualeOreDipendenti());
	        	center.layout(true);}      
	      });
	    btnRiepilogoAnnuale.setWidth("100%");
	    cp.add(btnRiepilogoAnnuale);
	    
	    Button btnRiepilogoMesePerCommessa = new Button();
	    btnRiepilogoMesePerCommessa.setToolTip("Riepilogo Ore Su Commesse");
	    btnRiepilogoMesePerCommessa.setHeight(65);
	    btnRiepilogoMesePerCommessa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenzeDip()));
	    btnRiepilogoMesePerCommessa.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMesePerCommessa.setWidth("100%");
	    btnRiepilogoMesePerCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoStatoAvanzamentoOreCommesse());
	        	center.layout(true);}      
	      });
	    cp.add(btnRiepilogoMesePerCommessa);
	    
	    Button btnRiepilogoOreDipPerCommessa = new Button();
	    btnRiepilogoOreDipPerCommessa.setToolTip("Riepilogo Ore Dipendenti");
	    btnRiepilogoOreDipPerCommessa.setHeight(65);
	    btnRiepilogoOreDipPerCommessa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenzeDip()));
	    btnRiepilogoOreDipPerCommessa.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoOreDipPerCommessa.setWidth("100%");
	    btnRiepilogoOreDipPerCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoOreDipendentiDettCommesse());
	        	center.layout(true);}      
	      });
	    cp.add(btnRiepilogoOreDipPerCommessa);
	    
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Fatturazione");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione());
	        	center.layout(true);   */            
            }
        });
	    Button btnFoglioFatturazione = new Button();
	    btnFoglioFatturazione.setToolTip("Gestione Dati Fatturazione");
	    btnFoglioFatturazione.setHeight(65);
	    btnFoglioFatturazione.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnFoglioFatturazione.setIconAlign(IconAlign.BOTTOM);
	    btnFoglioFatturazione.setWidth("100%");
	    btnFoglioFatturazione.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione("AMM"));
	        	center.layout(true);}      
	      });
	    btnFoglioFatturazione.setWidth("100%");
	    cp.add(btnFoglioFatturazione);
	    
	    Button btnReportDatiFatt = new Button();
	    btnReportDatiFatt.setToolTip("Report Dati Fatturazione");
	    btnReportDatiFatt.setHeight(65);
	    btnReportDatiFatt.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnReportDatiFatt.setIconAlign(IconAlign.BOTTOM);
	    btnReportDatiFatt.setWidth("100%");
	    btnReportDatiFatt.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	/*center.removeAll();
	        	center.add(new CenterLayout_RiepilogoDatiFatturazione());
	        	center.layout(true);*/
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-130, h-75);
	        	d.add(new CenterLayout_RiepilogoDatiFatturazione("AMM"));
	        	d.setHeading("Riepilogo Dati Fatturazione");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.AUTO);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        }      
	    });
	    cp.add(btnReportDatiFatt);
	    
	    Button btnElaboraFattura = new Button();
	    btnElaboraFattura.setToolTip("Elabora Fatture");
	    btnElaboraFattura.setHeight(65);
	    btnElaboraFattura.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.fattura()));
	    btnElaboraFattura.setIconAlign(IconAlign.BOTTOM);
	    btnElaboraFattura.setWidth("100%");
	    btnElaboraFattura.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_ElaboraFatture());
	        	center.layout(true);}      
	    });
	    cp.add(btnElaboraFattura);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Tools Amministrativi");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	/*center.removeAll();
	        	center.add(new PanelEditPasswordUtenti());
	        	center.layout(true);      */         
            }
        });
	    
	    Button btnGestionePassword = new Button();
	    btnGestionePassword.setToolTip("Gestione Password");
	    btnGestionePassword.setHeight(65);
	    btnGestionePassword.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.editPass()));
	    btnGestionePassword.setIconAlign(IconAlign.BOTTOM);
	    btnGestionePassword.setWidth("100%");
	    btnGestionePassword.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelEditPasswordUtenti());
	        	center.layout(true);}      
	      });
	    btnGestionePassword.setWidth("100%");
	    cp.add(btnGestionePassword);
	    
	    status = new Status();
	    status.setBusy("Please wait...");
	    status.hide();
	    status.setAutoWidth(true);
    
	    Button btnGestioneHwSw = new Button();
	    btnGestioneHwSw.setToolTip("Check I/U I commesse");
	    btnGestioneHwSw.setHeight(65);
	    btnGestioneHwSw.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.gestHwSw()));
	    btnGestioneHwSw.setIconAlign(IconAlign.BOTTOM);
	    btnGestioneHwSw.setWidth("100%");
	    btnGestioneHwSw.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	smplcmbxPeriodo.setFieldLabel("Mese");
	        	smplcmbxPeriodo.setName("mese");
	        	smplcmbxPeriodo.setEmptyText("Mese..");
	        	smplcmbxPeriodo.setAllowBlank(false);
	    		 for(String l : DatiComboBox.getPeriodo()){
	    			 smplcmbxPeriodo.add(l);}
	    		 smplcmbxPeriodo.setTriggerAction(TriggerAction.ALL);
	    		
	    	    Dialog d= new Dialog();
	    	    d.setSize(500, 500);
	    	    ContentPanel cp= new ContentPanel();
	    	    cp.setLayout(new FitLayout());
	    	    cp.setHeading("Check su valori giornalieri duplicati.");
	    	    cp.setSize(485, 450);
	    	    cp.setTopComponent(status);
	    	    //cp.add(smplcmbxPeriodo);
	    	    cp.add(txtCheck);
	    	    d.add(smplcmbxPeriodo);
	    	    d.add(cp);
	    	    d.show();
   	    
	    	    
	    	    smplcmbxPeriodo.addListener(Events.Select, new Listener<BaseEvent>(){
					@Override
					public void handleEvent(BaseEvent be) {					
							
						String scelta=smplcmbxPeriodo.getRawValue().toString();
						status.setBusy("Please wait...");
			    	    status.show();
						
						UtilityService.Util.getInstance().checkIntervallicommesse(scelta, 
				    	    		new AsyncCallback<List<String>>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on checkIntervallicommesse();");	
									status.hide();					
								}

								@Override
								public void onSuccess(List<String> result) {
									status.hide();
									if(result.size()==0)
										txtCheck.setText("Tutto OK!");
									else{
									for(String l :result)
										listaCheck=listaCheck+"<BR>"+l;
									txtCheck.setText(listaCheck);}
								}
							});
						
					}		
				});
	    	  
	        ;}        	
	    });
	    btnGestioneHwSw.setWidth("100%");
	    cp.add(btnGestioneHwSw);
	    
	    /*
	    Button btnTmp = new Button();
	    btnTmp.setToolTip("Generare tariffe Ordine");
	    btnTmp.setHeight(65);
	    btnTmp.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.gestHwSw()));
	    btnTmp.setIconAlign(IconAlign.BOTTOM);
	    btnTmp.setWidth("100%");
	    btnTmp.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	status.setBusy("Please wait...");
	    	    status.show();
	    	    Dialog d= new Dialog();
	    	    d.setSize(500, 500);
	    	    ContentPanel cp= new ContentPanel();
	    	    cp.setLayout(new FitLayout());
	    	    cp.setHeading("Check su valori giornalieri duplicati.");
	    	    cp.setSize(485, 450);
	    	    cp.setTopComponent(status);
	    	    cp.add(txtCheck);
	    	    d.add(cp);
	    	    d.show();
	    	    
	    	    UtilityService.Util.getInstance().generateAttivitaOrdine(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
							
							
						 UtilityService.Util.getInstance().insertIdAttivitaOrdineInFoglioFatturazione(new AsyncCallback<Void>() {

				    	    	@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on insertIdAttivitaOrdineInFoglioFatturazione();");	
														
								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("OK");	
									
								} 	    		
				    	    });
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
						Window.alert("Errore di connessione on generateAttivitaOrdine();");
					}
				});
	        ;}
	    });
	    btnTmp.setWidth("100%");*/
	    //cp.add(btnTmp);
	    	        
	    Button btnTools = new Button();
	    btnTools.setToolTip("Strumenti Utili");
	    btnTools.setHeight(65);
	    btnTools.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.tools()));
	    btnTools.setIconAlign(IconAlign.BOTTOM);
	    btnTools.setWidth("100%");
	    btnTools.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	center.removeAll();
	        	center.add(new PanelToolAmministrativi());
	        	center.layout(true);
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnTools);
	    
	    Button btnRichieste = new Button();
	    btnRichieste.setToolTip("Richieste");
	    btnRichieste.setHeight(65);
	    btnRichieste.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.tools()));
	    btnRichieste.setIconAlign(IconAlign.BOTTOM);
	    btnRichieste.setWidth("100%");
	    btnRichieste.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	    
	        	DialogRichiestaHardwareDipendente d= new DialogRichiestaHardwareDipendente(txtfldUsername.getValue());
	        	d.show();
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnRichieste);
	    
	    panel.add(cp);
	    
	    Button btnAnagraficaHardware = new Button();
	    btnAnagraficaHardware.setToolTip("Anagrafica Hardware");
	    btnAnagraficaHardware.setHeight(65);
	    btnAnagraficaHardware.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.tools()));
	    btnAnagraficaHardware.setIconAlign(IconAlign.BOTTOM);
	    btnAnagraficaHardware.setWidth("100%");
	    btnAnagraficaHardware.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	    
	        	center.removeAll();
	        	center.add(new PanelAnagraficaHardware());
	        	center.layout(true);
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnAnagraficaHardware);
	    
	    Button btnRiepRichiesteHardware = new Button();
	    btnRiepRichiesteHardware.setToolTip("Riepilogo Richieste");
	    btnRiepRichiesteHardware.setHeight(65);
	    btnRiepRichiesteHardware.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.tools()));
	    btnRiepRichiesteHardware.setIconAlign(IconAlign.BOTTOM);
	    btnRiepRichiesteHardware.setWidth("100%");
	    btnRiepRichiesteHardware.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	    
	        	center.removeAll();
	        	center.add(new PanelRiepilogoRichiesteHardware());
	        	center.layout(true);
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnRiepRichiesteHardware);
	    
	    
	    Button btnProcedure = new Button();
	    btnProcedure.setToolTip("Edit Tebelle");
	    btnProcedure.setHeight(65);
	    btnProcedure.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.generate1()));
	    btnProcedure.setIconAlign(IconAlign.BOTTOM);
	    btnProcedure.setWidth("100%");
	    btnProcedure.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelStrumentiAmministrativi_TabellaFogliFatturazione());
	        	center.layout(true);	        	
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnProcedure);
	    
	    
	    Button btnIns = new Button();
	    btnIns.setToolTip("Edit Tebelle");
	    btnIns.setHeight(65);
	    btnIns.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.generate1()));
	    btnIns.setIconAlign(IconAlign.BOTTOM);
	    btnIns.setWidth("100%");
	    btnIns.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelInsertRTV());
	        	center.layout(true);	        	
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnIns);
	    
	   /* Button btnProcedure = new Button();
	    btnProcedure.setToolTip("Procedure X");
	    btnProcedure.setHeight(65);
	    btnProcedure.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.tools()));
	    btnProcedure.setIconAlign(IconAlign.BOTTOM);
	    btnProcedure.setWidth("100%");
	    btnProcedure.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	    
	        	AdministrationService.Util.getInstance().copyCodOrdine(new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Boolean result) {
						Window.alert("Ok");
					}
				});
	       }      
	    });
	    btnTools.setWidth("100%");
	    cp.add(btnProcedure);
	    */
	    panel.add(cp);
	    	    
	    panel.setSize(180,Window.getClientHeight()-70);
	    panel.setBorders(false);
	    west.add(panel);
	    
//----------------------------------------------------------------------------------------------
	    
	    
	   center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());   
	   container.add(north, northData);
	   container.add(west, westData);
	   container.add(center, centerData);
	 	
	   viewport.add(container);
	   add(viewport);		
	}

}