package gestione.pack.client.layout;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.PanelAbilitazioneStraordinarioDip;
import gestione.pack.client.layout.panel.PanelPrintAll;
import gestione.pack.client.layout.panel.PanelRiepilogoAnnualeOreDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoCostiDipendenti;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseGiornalieroHorizontal;
import gestione.pack.client.layout.panel.PanelRiepilogoOreDipendentiPerCommesse;
import gestione.pack.client.layout.panel.PanelRiepilogoSituazioneMensileOreDipendenti;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
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


public class BodyLayout_GestionePersonale extends LayoutContainer {
	
	private ContentPanel center = new ContentPanel();
	private Text txtUsername = new Text();
	
	public TextField<String> txtfldRuolo= new TextField<String>();
	public TextField<String> txtfldUsername= new TextField<String>();
	
	
	private int w=Window.getClientWidth();
	
	public BodyLayout_GestionePersonale() {
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
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);               
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
	    btnRiepilogoMeseDip.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoSituazioneMensileOreDipendenti());
	        	center.layout(true);}      
	      });
	    btnRiepilogoMeseDip.setWidth("100%");
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
	    
	    cp.setExpanded(true);	    
	    panel.add(cp);
	    
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Personale");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_AnagraficaPersonale());
	        	center.layout(true);               
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
	    
	    Button btnRiepilogoAnnuale = new Button();
	    btnRiepilogoAnnuale.setToolTip("Report Annuale Ore");
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

	    Button btnAbilitazioneStrao = new Button();
	    btnAbilitazioneStrao.setToolTip("Abilitazione Straordinari");
	    btnAbilitazioneStrao.setHeight(65);
	    btnAbilitazioneStrao.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnAbilitazioneStrao.setIconAlign(IconAlign.BOTTOM);
	    btnAbilitazioneStrao.setWidth("100%");
	    btnAbilitazioneStrao.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelAbilitazioneStraordinarioDip());
	        	center.layout(true);}
	        
	      });
	    cp.add(btnAbilitazioneStrao);
	    
	    Button btnRiepilogoCostiDip = new Button();
	    btnRiepilogoCostiDip.setToolTip("Riepilogo Costi");
	    btnRiepilogoCostiDip.setHeight(65);
	    btnRiepilogoCostiDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.costi()));
	    btnRiepilogoCostiDip.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoCostiDip.setWidth("100%");
	    btnRiepilogoCostiDip.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new PanelRiepilogoCostiDipendenti("GP"));
	        	center.layout(true);}
	        
	      });
	    btnRiepilogoCostiDip.setWidth("100%");
	    cp.add(btnRiepilogoCostiDip);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Commesse");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_AssociaPersonale());
	        	center.layout(true);
            }
        });	    
	  
	    
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
	    
	    panel.add(cp);
	    
	 /*   cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Presenze");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);               
            }
        });*/
	    
	      
	    
	    panel.setSize(180,Window.getClientHeight()-70);
	    west.add(panel);
	        
//----------------------------------------------------------------------------------------------
	    
	   center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	    
	   container.add(north, northData);
	   container.add(west, westData);
	   container.add(center, centerData);
	 
	   viewport.add(container);
	   add(viewport);
		
	}
	/*
	private void recuperoSessionUsername() {
		
		SessionManagementService.Util.getInstance().getUserName(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				setUsername(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				
			}
		});
		
		
	}
	
	public  void setUsername(String result){
		String nome=new String();
		String cognome=new String();
		int i=result.indexOf(".");
				
		nome=result.substring(0,i);
		cognome=result.substring(i+1,result.length());
		
		txtUsername.setText("Welcome, "+nome+" "+cognome+".");
	}
*/

}
