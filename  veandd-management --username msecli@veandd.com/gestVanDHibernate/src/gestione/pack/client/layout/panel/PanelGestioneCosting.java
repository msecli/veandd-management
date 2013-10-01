package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelGestioneCosting extends LayoutContainer{

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<CostingModel> storeCosting=new ListStore<CostingModel>();
	private ColumnModel cmCosting;
	
	private ListStore<CostingRisorsaModel> storeCostingRisorsa=new ListStore<CostingRisorsaModel>();
	private ColumnModel cmCostingRisorsa;
	
	private EditorGrid<CostingModel> gridCosting;
	private EditorGrid<CostingRisorsaModel> gridCostingDipendente;
	 
	private ComboBox<CostingModel> cmbbxCosting;
	private Button btnConferma= new Button();
	private Button btnConfermaDip;
	private Button btnAddCosting;
	private Text txtCognome= new Text();
	private int idSelected;
	
	private String username= new String();
	
	public PanelGestioneCosting(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGridCosting= new ContentPanel();
		cpGridCosting.setHeaderVisible(true);
		cpGridCosting.setHeading("Selezione Costing.");
		cpGridCosting.setBorders(false);
		cpGridCosting.setFrame(true);
		cpGridCosting.setHeight((h-55)/2-80);
		cpGridCosting.setWidth(w-240);
		cpGridCosting.setScrollMode(Scroll.AUTO);
		cpGridCosting.setLayout(new FitLayout());
		cpGridCosting.setButtonAlign(HorizontalAlignment.CENTER);  
		//Resizable r=new Resizable(cpGrid);
		
		ContentPanel cpGridCostingRisorsa= new ContentPanel();
		cpGridCostingRisorsa.setHeaderVisible(true);
		cpGridCostingRisorsa.setHeading("Costing Dettagliato per Dipendente");
		cpGridCostingRisorsa.setBorders(false);
		cpGridCostingRisorsa.setFrame(true);
		cpGridCostingRisorsa.setHeight((h-55)/2+80);
		cpGridCostingRisorsa.setWidth(w-240);
		cpGridCostingRisorsa.setScrollMode(Scroll.AUTO);
		cpGridCostingRisorsa.setLayout(new FitLayout());
		cpGridCostingRisorsa.setButtonAlign(HorizontalAlignment.CENTER);  
		
		Resizable r= new Resizable(cpGridCostingRisorsa);
		
		ToolBar tlbCosting= new ToolBar();
		ToolBar tlbCostingRisorsa= new ToolBar();
					
		ListStore<CostingModel> store=new ListStore<CostingModel>();
		cmbbxCosting=new ComboBox<CostingModel>();		
		cmbbxCosting.setStore(store);
		cmbbxCosting.setFieldLabel("Costing");
		cmbbxCosting.setEnabled(true);
		cmbbxCosting.setEmptyText("Selezionare il Costing..");
		cmbbxCosting.setEditable(true);
		cmbbxCosting.setVisible(true);
		cmbbxCosting.setTriggerAction(TriggerAction.ALL);
		cmbbxCosting.setAllowBlank(false);
		cmbbxCosting.setDisplayField("commessa");
		cmbbxCosting.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {			
				getCosting();					
			}
		});
		cmbbxCosting.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				caricaTabellaDatiCosting();
			}		
		});
		
		txtCognome.setStyleAttribute("padding-left", "10px");
		txtCognome.setStyleAttribute("font-size", "15px");
		
		btnConferma.setEnabled(false);
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConferma.setIconAlign(IconAlign.TOP);
	  	btnConferma.setToolTip("Conferma Dati.");
	  	btnConferma.setSize(26, 26);
	  	btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {		
  		
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<CostiHwSwModel> lista=new ArrayList<CostiHwSwModel>();
		  		//lista.addAll(storeCostingRisorsa.getModels());
				
		  		for(CostiHwSwModel c:lista)
		  			AdministrationService.Util.getInstance().saveAssociaCostiHwSw(idSelected, c, new AsyncCallback<Boolean>() {
		  				@Override
		  				public void onFailure(Throwable caught) {
		  					Window.alert("Errore di connessione on saveAssociaCostiHwSw()");
						
		  				}

		  				@Override
		  				public void onSuccess(Boolean result) {
		  					
		  					if(!result)
		  						Window.alert("Impossibile effettuare le modifiche indicate!");
		  					else
		  						storeCostingRisorsa.commitChanges();					
		  				}
		  			});
			}
		});
	  	
	  	btnConfermaDip= new Button();
	  	btnConfermaDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConfermaDip.setIconAlign(IconAlign.TOP);
	  	btnConfermaDip.setToolTip("Conferma Dati.");
	  	btnConfermaDip.setSize(26, 26);
	  	btnConfermaDip.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				for(Record record: storeCosting.getModifiedRecords()){		    		  
		    		  GestioneCostiDipendentiModel g= new GestioneCostiDipendentiModel();
		    		  g=(GestioneCostiDipendentiModel) record.getModel();		    		  
		    		  AdministrationService.Util.getInstance().editDatiCostiAzienda(g, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on editDataCostiAzienda();");
								}

								@Override
								public void onSuccess(Void result) {
									caricaTabellaDatiCosting();
									storeCosting.commitChanges();
								}		    			  
						}); 	  
		    	  }
		    	  storeCosting.commitChanges();				
			}
		});
	
	  	
	  	btnAddCosting= new Button();
	  	btnAddCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.addList()));
	  	btnAddCosting.setIconAlign(IconAlign.TOP);
	  	btnAddCosting.setToolTip("Nuovo Costing");
	  	btnAddCosting.setSize(26, 26);
	  	btnAddCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				DialogNewCosting d= new DialogNewCosting();
				d.show();
				
			}
		});
			
		cmCosting = new ColumnModel(createColumnsCosting());		
		gridCosting= new EditorGrid<CostingModel>(storeCosting, cmCosting);  
		gridCosting.setBorders(false);  
		gridCosting.setItemId("grid");
	    gridCosting.setColumnLines(true);
	    gridCosting.setStripeRows(true);
	    gridCosting.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridCosting.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<GestioneCostiDipendentiModel>>() {  
	          public void handleEvent(SelectionChangedEvent<GestioneCostiDipendentiModel> be) {  
		        	
		            if (be.getSelection().size() > 0) { 
		            	btnConferma.setEnabled(true);
		            	idSelected=be.getSelectedItem().get("idPersonale");
		            	txtCognome.setText("Selezionato: "+(String)be.getSelectedItem().get("cognome"));
		            	caricaTabellaCostingRisorsa(idSelected);           
		            } else {  
		              
		            	txtCognome.setText("");
		            }	            
		          }		            
		}); 
	    
	    cmCostingRisorsa=new ColumnModel(createColumnsCostingRisorse());
	    gridCostingDipendente= new EditorGrid<CostingRisorsaModel>(storeCostingRisorsa, cmCostingRisorsa);
	    gridCostingDipendente.setBorders(false);
	    gridCostingDipendente.setItemId("grid");
	    gridCostingDipendente.setStripeRows(true); 
	    gridCostingDipendente.setColumnLines(true);
	    	   
	    cpGridCosting.add(gridCosting); 
	  //tlbCosting.add(btnConferma);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(cmbbxCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(btnAddCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    cpGridCosting.setTopComponent(tlbCosting);
	    
	    cpGridCostingRisorsa.add(gridCostingDipendente);
	    tlbCostingRisorsa.add(btnConfermaDip);
	    tlbCostingRisorsa.add(txtCognome);	    
	    cpGridCostingRisorsa.setTopComponent(tlbCostingRisorsa);
	    
	    vp.add(cpGridCosting);
	    vp.add(cpGridCostingRisorsa);
	   
	    layoutContainer.add(vp, new FitData(0, 3, 3, 0));
				
		add(layoutContainer);
	}
	

	private List<ColumnConfig> createColumnsCosting() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			
		ColumnConfig column = new ColumnConfig();  
	    
	    column = new ColumnConfig();  
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(140);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(140);  
	    configs.add(column);
		
	    column = new ColumnConfig();  
	    column.setId("descrizione");  
	    column.setHeader("Descrizione");  
	    column.setWidth(200);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("numeroRevisione");  
	    column.setHeader("#Revisione");  
	    column.setWidth(80);  
	    configs.add(column);
		
		return configs;
	}
	
	
	private List<ColumnConfig> createColumnsCostingRisorse() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
	       
	    column = new ColumnConfig();  
	    column.setId("area");  
	    column.setHeader("Area");  
	    column.setWidth(100);  
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(100);  
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("progetto");  
	    column.setHeader("Progetto");  
	    column.setWidth(100);  
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(100);  
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("risorsa");  
	    column.setHeader("Risorsa");  
	    column.setWidth(100);  
	    configs.add(column);
	    	
			    
	    column = new ColumnConfig();  
	    column.setId("costoOrario");  
	    column.setHeader("Costo Orario");  
	    column.setWidth(80);  
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("orePianificate");  
	    column.setHeader("Ore Pianificate");  
	    column.setWidth(120);  
	    TextField<String> txtfldOrePianificate= new TextField<String>();
	    txtfldOrePianificate.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldOrePianificate.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldOrePianificate));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("lc");  
	    column.setHeader("LC");  
	    column.setWidth(60);  
	    TextField<String> txtfldLc= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldLc));
	    //TODO settare di default 100%
	    configs.add(column);	    
	    
	    column = new ColumnConfig();  
	    column.setId("oreCorrette");  
	    column.setHeader("Ore Corrette");  
	    column.setWidth(140);  
	    configs.add(column);    
	    
	    column = new ColumnConfig();  
	    column.setId("costoRisorsa");  
	    column.setHeader("Costo Risorsa");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOrarioStruttura");  
	    column.setHeader("Costo Struttura (h)");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoRisorsaStruttura");  
	    column.setHeader("Costo Struttura Risorsa");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoTotaleAzienda");  
	    column.setHeader("CostoTotale");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("incidenzaCosti");  
	    column.setHeader("Incidenza");  
	    column.setWidth(80);  
	    configs.add(column);
	 
		column = new ColumnConfig();  
	    column.setId("costoHwSw");  
	    column.setHeader("Somma Costi Hw/Sw");  
	    column.setWidth(80);  
	    configs.add(column);
	  		
	    column = new ColumnConfig();  
	    column.setId("costoOneri");  
	    column.setHeader("Costo Oneri");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoSommaHwSwOneri");  
	    column.setHeader("Totale Costi");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoRisorsaSommaHwSwOneri");  
	    column.setHeader("Costo Risorsa");
	    column.setToolTip("Somma costi Hw/Sw/Oneri");
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("incidenzaCostiHwSw");  
	    column.setHeader("Incidenza");  
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoConsulenza");  
	    column.setHeader("Costo Consulenza");  
	    column.setWidth(80);  
	    TextField<String> txtfldCostoConsulenza= new TextField<String>();
	    txtfldCostoConsulenza.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldCostoConsulenza.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldCostoConsulenza));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("efficienza");  
	    column.setHeader("Efficienza");  
	    column.setWidth(80);  
	    configs.add(column);
	   	
		column = new ColumnConfig();  
	    column.setId("oreFatturare");  
	    column.setHeader("Ore da Fatturare");  
	    column.setWidth(120);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("tariffa");  
	    column.setHeader("Tariffa");  
	    column.setWidth(80);  
	    TextField<String> txtfldTariffa= new TextField<String>();
	    txtfldTariffa.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldTariffa.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldTariffa));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("tariffaDerivata");  
	    column.setHeader("Tariffa Derivata");  
	    column.setWidth(80);  
	    TextField<String> txtfldTariffaDerivata= new TextField<String>();
	    txtfldTariffaDerivata.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldTariffaDerivata.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldTariffaDerivata));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("fatturato");  
	    column.setHeader("Fatturato");    
	    column.setWidth(80);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("mol");  
	    column.setHeader("MOL");    
	    column.setWidth(50);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("molPerc");  
	    column.setHeader("MOL%");    
	    column.setWidth(50);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("ebit");  
	    column.setHeader("EBIT");    
	    column.setWidth(50);  
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("ebitPerc");  
	    column.setHeader("EBIT%");    
	    column.setWidth(50);  
	    configs.add(column);
	    
	    return configs;
	}
	
	
	private void caricaTabellaDatiCosting() {
		int costing=cmbbxCosting.getValue().get("idCosting");
		
		try {
			AdministrationService.Util.getInstance().getDatiCosting(costing, new AsyncCallback<List<CostingModel>>() {
					@Override
					public void onSuccess(List<CostingModel> result) {
						loadTableCosting(result);
					}
			
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione;");
						caught.printStackTrace();
					}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi durante il caricamento dei dati sui costi personale.");
		}	
	}
		
	
	private void caricaTabellaCostingRisorsa(int id) {
		/*AdministrationService.Util.getInstance().getDatiCostiHwSw(id, new AsyncCallback<List<CostingRisorsaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connession on getDatiCostiHwSw();");
				
			}

			@Override
			public void onSuccess(List<CostingRisorsaModel> result) {
				loadTableHwSw(result);
			}		
		});*/		
	} 
		
	private void loadTableCostingRisorse(List<CostingRisorsaModel> result) {
		storeCostingRisorsa.removeAll();
		storeCostingRisorsa.setStoreSorter(new StoreSorter<CostingRisorsaModel>());  
		storeCostingRisorsa.add(result);
	}
	
	
	private void getCosting() {
			
			//recupero il ruolo dalla sessione
			SessionManagementService.Util.getInstance().getRuolo(new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {						
					if(result.compareTo("AMM")==0)
						//recupero l'username dalla sessione
						SessionManagementService.Util.getInstance().getUserName(new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {			
								//carico i dati per username pm
								username=result;
								AdministrationService.Util.getInstance().getListaDatiCosting(username, new AsyncCallback<List<CostingModel>>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Error on getDatiCostingPerPM();");									
									}
									@Override
									public void onSuccess(List<CostingModel> result) {
										ListStore<CostingModel> lista= new ListStore<CostingModel>();
										lista.setStoreSorter(new StoreSorter<CostingModel>());  
										lista.setDefaultSort("cliente", SortDir.ASC);
										
										lista.add(result);				
										cmbbxCosting.clear();
										cmbbxCosting.setStore(lista);									
									}
									
								});								
							}
							
							@Override
							public void onFailure(Throwable caught) {				
								Window.alert("Error on getUserName();");
							}
						});	
					
					else
						//carico tutti i costing perchè username è ""
						AdministrationService.Util.getInstance().getListaDatiCosting("",new AsyncCallback<List<CostingModel>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error on getDatiCostingPerPM();");									
							}
							@Override
							public void onSuccess(List<CostingModel> result) {
								ListStore<CostingModel> lista= new ListStore<CostingModel>();
								lista.setStoreSorter(new StoreSorter<CostingModel>());  
								lista.setDefaultSort("cliente", SortDir.ASC);
								
								lista.add(result);				
								cmbbxCosting.clear();
								cmbbxCosting.setStore(lista);						
							}
						});
				}		
				@Override
				public void onFailure(Throwable caught) {				
					Window.alert("Error on getRuolo();");
				}
			});					
	}	
	
	
	private void loadTableCosting(List<CostingModel> result) {
		storeCosting.removeAll();
		storeCosting.setStoreSorter(new StoreSorter<CostingModel>());  
	    storeCosting.setDefaultSort("cognome", SortDir.ASC);
		storeCosting.add(result);
	}
	
}


