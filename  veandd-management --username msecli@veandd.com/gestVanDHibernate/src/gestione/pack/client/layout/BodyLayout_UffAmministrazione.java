package gestione.pack.client.layout;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.PanelMensileOrdini;
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
	    /*cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);               
            }
        });*/
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
	    cp.setExpanded(true);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Clienti");
	    /*cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_AnagraficaClienti());
	        	center.layout(true);               
            }
        });*/
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
	    /*cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCompleta());
	        	center.layout(true);               
            }
        });*/
	    Button btnGestioneRdo = new Button();
	    btnGestioneRdo.setToolTip("Gestione Dati Rdo");
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
	        	d.add(new PanelMensileOrdini("UA"));
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
	    cp.setExpanded(true);
	    /*cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_GestioneCommessa());
	        	center.layout(true);               
            }
        });*/
	  	      
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
	    btnRiepiloghiSalPcl.setToolTip("Riepilogo SAL/PCL/Non Fatturabili");
	    btnRiepiloghiSalPcl.setHeight(65);
	    btnRiepiloghiSalPcl.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepiloghiSalPcl.setIconAlign(IconAlign.BOTTOM);
	    btnRiepiloghiSalPcl.setWidth("100%");
	    btnRiepiloghiSalPcl.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-100, h-75);
	        	d.add(new CenterLayout_RiepiloghiSalPcl());
	        	d.setHeading("Riepilogo Sal/Pcl/Non Fatturabili");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.NONE);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        }        
	      });
	    cp.add(btnRiepiloghiSalPcl);
	    
	    panel.add(cp);
	         
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Fatturazione");
	    /*cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione("UA"));
	        	center.layout(true);               
            }
        });*/
	    Button btnFoglioFatturazione = new Button();
	    btnFoglioFatturazione.setToolTip("Gestione Dati Fatturazione");
	    btnFoglioFatturazione.setHeight(65);
	    btnFoglioFatturazione.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.anagrafica()));
	    btnFoglioFatturazione.setIconAlign(IconAlign.BOTTOM);
	    btnFoglioFatturazione.setWidth("100%");
	    btnFoglioFatturazione.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione("UA"));
	        	center.layout(true);}      
	      });
	    btnFoglioFatturazione.setWidth("100%");
	    cp.add(btnFoglioFatturazione);
	    
	    Button btnReportDatiFatt = new Button();
	    btnReportDatiFatt.setToolTip("Riepilogo Dati Fatturazione");
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
	        	d.add(new CenterLayout_RiepilogoDatiFatturazione("UA"));
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
	    
	    Button btnRiepilogoMensile1 = new Button();
	    btnRiepilogoMensile1.setToolTip("Riepilogo Mensile");
	    btnRiepilogoMensile1.setHeight(65);
	    btnRiepilogoMensile1.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.presenzeDip()));
	    btnRiepilogoMensile1.setIconAlign(IconAlign.BOTTOM);
	    btnRiepilogoMensile1.setWidth("100%");
	    btnRiepilogoMensile1.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-130, h-65);
	        	d.add(new PanelMensileOrdini("UA"));
	        	d.setHeading("Mensile");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.AUTO);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        }	        
	    });
	    cp.add(btnRiepilogoMensile1);	
	    
	    Button btnRiepiloghiSalPcl1 = new Button();
	    btnRiepiloghiSalPcl1.setToolTip("Riepilogo SAL/PCL/Non Fatturabili");
	    btnRiepiloghiSalPcl1.setHeight(65);
	    btnRiepiloghiSalPcl1.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riepMensDip()));
	    btnRiepiloghiSalPcl1.setIconAlign(IconAlign.BOTTOM);
	    btnRiepiloghiSalPcl1.setWidth("100%");
	    btnRiepiloghiSalPcl1.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	/*center.removeAll();
	        	center.add(new CenterLayout_RiepiloghiSalPcl());
	        	center.layout(true);*/
	        	int h=Window.getClientHeight();
	        	int w=Window.getClientWidth();
	        	Dialog d= new Dialog();
	        	d.setSize(w-130, h-65);
	        	d.add(new CenterLayout_RiepiloghiSalPcl());
	        	d.setHeading("Riepilogo SAL/PCL/NonFatturabili");
	        	d.setCollapsible(true);
	        	d.setScrollMode(Scroll.AUTO);
	        	d.setButtons("");
	        	d.setConstrain(false);
	        	d.show();
	        	
	        }        
	    });
	    cp.add(btnRiepiloghiSalPcl1);
	    
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


