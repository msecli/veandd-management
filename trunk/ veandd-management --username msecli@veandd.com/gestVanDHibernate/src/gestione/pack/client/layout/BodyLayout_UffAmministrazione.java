package gestione.pack.client.layout;

import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.UtilityService;
import gestione.pack.client.layout.panel.DialogRichiestaHardwareDipendente;
import gestione.pack.client.layout.panel.PanelAbilitazioneStraordinarioDip;
import gestione.pack.client.layout.panel.PanelAnagraficaHardware;
import gestione.pack.client.layout.panel.PanelEditPasswordUtenti;
import gestione.pack.client.layout.panel.PanelGestioneCosting;
import gestione.pack.client.layout.panel.PanelGestioneOfferte;
import gestione.pack.client.layout.panel.PanelMensileOrdini;
import gestione.pack.client.layout.panel.PanelPrintAll;
import gestione.pack.client.layout.panel.PanelProtocolloCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoAnnualeOreDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoCostiDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseGiornalieroHorizontal;
import gestione.pack.client.layout.panel.PanelRiepilogoOreCartellinoDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoOreDipendentiPerCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoRichiesteHardware;
import gestione.pack.client.layout.panel.PanelRiepilogoSituazioneMensileOreDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoStatoAvanzamentoOreCommesse;
import gestione.pack.client.layout.panel.PanelSaturazioneRisorsePerSede;
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


public class BodyLayout_UffAmministrazione extends LayoutContainer {
	
	private ContentPanel center = new ContentPanel();
	private Text txtUsername = new Text();
	
	public TextField<String> txtfldUsername= new TextField<String>();
	public TextField<String> txtfldRuolo= new TextField<String>();
	
	private int w=Window.getClientWidth();
	
	public BodyLayout_UffAmministrazione() {
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
/*
		viewport.setStyleAttribute("margin-left", "-5px");
		viewport.setStyleAttribute("margin-top", "-5px");
		viewport.setStyleAttribute("padding-right", "10px");
		viewport.setStyleAttribute("padding-bottom", "12px");
*/
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
		north.setTopComponent(toolBar); 
	    
	    
//--------------------------------Menu laterale	----------------------------------------------	
	    
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
		    btnRiepilogoCostiDip.setWidth("100%");
		    cp.add(btnRiepilogoCostiDip);
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
		    
		    Button btnGestioneOfferte = new Button();
		    btnGestioneOfferte.setToolTip("Gestione Offerte");
		    btnGestioneOfferte.setHeight(65);
		    btnGestioneOfferte.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
		    btnGestioneOfferte.setIconAlign(IconAlign.BOTTOM);
		    btnGestioneOfferte.setWidth("100%");
		    btnGestioneOfferte.addSelectionListener(new SelectionListener<ButtonEvent>() {
		        public void componentSelected(ButtonEvent ce) {
		        		center.removeAll();
		        		center.add(new PanelGestioneOfferte());
		        		center.layout(true);
		        	}
		      });
		    cp.add(btnGestioneOfferte);
		   	    
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
		    
		    Button btnRiepilogoOreCartellino = new Button();
		    btnRiepilogoOreCartellino.setToolTip("Riepilogo ore a cartellino");
		    btnRiepilogoOreCartellino.setHeight(65);
		    btnRiepilogoOreCartellino.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensPers()));
		    btnRiepilogoOreCartellino.setIconAlign(IconAlign.BOTTOM);
		    btnRiepilogoOreCartellino.setWidth("100%");
		    btnRiepilogoOreCartellino.addSelectionListener(new SelectionListener<ButtonEvent>() {
		        public void componentSelected(ButtonEvent ce) {
		        	int h=Window.getClientHeight();
		        	int w=Window.getClientWidth();
		        	Dialog d= new Dialog();
		        	d.setSize(w-130, h-75);
		        	d.add(new PanelRiepilogoOreCartellinoDipendenti());
		        	d.setHeading("Riepilogo Ore Dipendenti");
		        	d.setCollapsible(true);
		        	d.setScrollMode(Scroll.AUTO);
		        	d.setButtons("");
		        	d.setConstrain(false);
		        	d.show();	        
		        }
		    });
		    cp.add(btnRiepilogoOreCartellino);
		    
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
		    
	    
	    panel.setSize(180, Window.getClientHeight()-70);
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


