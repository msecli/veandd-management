package gestione.pack.client.layout;

//import gestione.pack.client.utility.RecuperoParametriSessione;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
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
import com.google.gwt.user.client.ui.FormPanel;


public class BodyLayout_Administration extends LayoutContainer {
	
	private ContentPanel center;
	private Text txtUsername = new Text();
	public TextField<String> txtfldUsername= new TextField<String>();
	public TextField<String> txtfldRuolo= new TextField<String>();
	
	private int w=Window.getClientWidth();
	private int h=Window.getClientHeight();
		
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
		txtUsername.setWidth(190);
		txtUsername.setStyleAttribute("font-size", "12px");
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
			
		txtUsername.setText("Welcome, "+nome+" "+cognome+".");	
		
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
		
		toolBar.add(txtfldUsername);
		toolBar.add(txtfldRuolo);
		toolBar.add(txtUsername);	
		toolBar.add(buttonBar);
		north.setTopComponent(toolBar); 
		

//Menu laterale	-----------------------------------------------------------------------------------	
	    
	    ContentPanel panel = new ContentPanel();
	    panel.setHeaderVisible(false);
	    panel.setBodyBorder(false);
	    
	    panel.setLayout(new AccordionLayout());
	    
	    ContentPanel cp = new ContentPanel();
	    cp.setExpanded(false);
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
	    Button btnAnagrP = new Button("Anagrafica");
	    btnAnagrP.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_AnagraficaPersonale());
	        	center.layout(true);}
	        
	      });
	    btnAnagrP.setWidth("100%");
	    cp.add(btnAnagrP);
	    panel.add(cp);
   
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Clienti");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_AnagraficaClienti());
	        	center.layout(true);               
            }
        });
	    Button btnAnagrC = new Button("Anagrafica");
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
	    cp.setHeading("Gestione Rdo");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCompleta());
	        	center.layout(true);               
            }
        });
	    Button btnGestioneRdo = new Button("Gestione Dati");
	    btnGestioneRdo.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_GestioneRdoCompleta());
	        	center.layout(true);}
	        
	      });
	    btnGestioneRdo.setWidth("100%");
	    cp.add(btnGestioneRdo);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Commesse");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_GestioneCommessa());
	        	center.layout(true);               
            }
        });
	    Button btnGestioneCommessa = new Button("Gestione Dati");
	    btnGestioneCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_GestioneCommessa());
	        	center.layout(true);}
	        
	      });
	    btnGestioneCommessa.setWidth("100%");
	    cp.add(btnGestioneCommessa);
	    
	    Button btnAssociaPersonale = new Button("Associazione Dipendenti");
	    btnAssociaPersonale.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_AssociaPersonale());
	        	center.layout(true);}
	        
	      });
	    btnAssociaPersonale.setWidth("100%");
	    cp.add(btnAssociaPersonale);
    
	    panel.add(cp);
	         
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Gestione Presenze");
	    cp.setExpanded(true);
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);               
            }
        });
	    Button btnGestionePresenze = new Button("Foglio Ore");
	    btnGestionePresenze.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FoglioOreGiornalieroAutoTimb());
	        	center.layout(true);}      
	      });
	    btnGestionePresenze.setWidth("100%");
	    cp.add(btnGestionePresenze);
	    
	    Button btnPresenzeDipendenti = new Button("Foglio Ore Dipendenti");
	    btnPresenzeDipendenti.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FoglioOreSelectDipendenti());
	        	center.layout(true);}      
	      });
	    btnPresenzeDipendenti.setWidth("100%");
	    cp.add(btnPresenzeDipendenti);
	    
	    Button btnLoadTimbrature = new Button("Load File");
	    btnLoadTimbrature.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_FileTimbrature());
	        	center.layout(true);}      
	      });
	    btnLoadTimbrature.setWidth("100%");
	    cp.add(btnLoadTimbrature);
	    
	    
	    //--------------------------------
	   
	    Button btnPrint = new Button("Stampa");
	    final FormPanel fp= new FormPanel();
	    String url= "/gestvandhibernate/PrintDataServlet";
    	fp.setAction(url);
    	fp.setEncoding(FormPanel.ENCODING_MULTIPART);
	    fp.setMethod(FormPanel.METHOD_POST);
	   
	    fp.add(btnPrint);
	    	   
	    btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {	         
	        	fp.submit();	        	
	        }	
	      });
	    btnPrint.setWidth("100%");
	    cp.add(btnPrint);    
	  	    
	    panel.add(cp);
	    
	    //-------------------------------------   
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Riepilogo Ore");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_RiepilogoOreDipendentiCommesse());
	        	center.layout(true);               
            }
        });
	    Button btnRiepilogoMese = new Button("Report Dett. Ore");
	    btnRiepilogoMese.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	          center.removeAll();
	        	center.add(new CenterLayout_RiepilogoOreDipendentiCommesse());
	        	center.layout(true);}      
	      });
	    btnRiepilogoMese.setWidth("100%");
	    cp.add(btnRiepilogoMese);
	    panel.add(cp);
	    
	    cp = new ContentPanel();
	    cp.setAnimCollapse(false);
	    cp.setBodyStyleName("pad-text");
	    cp.setHeading("Fatturazione");
	    cp.addListener(Events.Expand, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
            	center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione());
	        	center.layout(true);               
            }
        });
	    Button btnFoglioFatturazione = new Button("Foglio Fatturazione");
	    btnFoglioFatturazione.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_FoglioFatturazione());
	        	center.layout(true);}      
	      });
	    btnFoglioFatturazione.setWidth("100%");
	    cp.add(btnFoglioFatturazione);
	    Button btnReportDatiFatt = new Button("Riepilogo Fatturazione");
	    btnReportDatiFatt.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	center.removeAll();
	        	center.add(new CenterLayout_RiepilogoDatiFatturazione());
	        	center.layout(true);}      
	      });
	    btnReportDatiFatt.setWidth("100%");
	    cp.add(btnReportDatiFatt);
	    panel.add(cp);
	    
	   // panel.add(cp);    
	    panel.setSize(180,Window.getClientHeight()-70);
	    panel.setBorders(false);
	    west.add(panel);
	        
//----------------------------------------------------------------------------------------------
	    
	   
	   center.add(new CenterLayout_FoglioOreSelectDipendenti());
	    
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
	

	private void recuperoSessionRuolo() {
		
		SessionManagementService.Util.getInstance().getRuolo(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				setRuolo(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				Window.alert("Error on getRuolo();");
			}
		});				
	}
	
	
	public  void setRuolo(String result){
		
		txtfldRuolo.setValue(result);
			}
	*/

}