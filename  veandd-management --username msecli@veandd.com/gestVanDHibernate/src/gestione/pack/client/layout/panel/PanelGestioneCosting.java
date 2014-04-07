package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
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
	private CheckBoxSelectionModel<CostingModel> sm = new CheckBoxSelectionModel<CostingModel>();
	private CellSelectionModel<CostingRisorsaModel> cm;
	 
	private ComboBox<PersonaleModel> cmbxPersonale= new ComboBox<PersonaleModel>();
	private ComboBox<CostingModel> cmbbxCosting;
	private Button btnAddCosting;
	private Button btnConfermaCosting;
	private Button btnScartaCosting;
	private Button btnChiudiCosting;
	private Button btnAddRisorsa;
	private Button btnDelRisorsa;
	private Button btnConfermaDip;
	private Button btnAddDatitrasferta;
	private Button btnConfermaNewVersione;
	
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
		cpGridCosting.setHeight((h-55)/2-130);
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
		cpGridCostingRisorsa.setHeight((h-55)/2+130);
		cpGridCostingRisorsa.setWidth(w-240);
		cpGridCostingRisorsa.setScrollMode(Scroll.AUTO);
		cpGridCostingRisorsa.setLayout(new FitLayout());
		cpGridCostingRisorsa.setButtonAlign(HorizontalAlignment.CENTER);  
		
		Resizable r= new Resizable(cpGridCostingRisorsa);
		
		ToolBar tlbCosting= new ToolBar();
		ToolBar tlbCostingRisorsa= new ToolBar();
		getCosting();	
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
		cmbbxCosting.setDisplayField("displayField");
		cmbbxCosting.setWidth(350);
		cmbbxCosting.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				idSelected=cmbbxCosting.getValue().get("idCosting");
				caricaTabellaDatiCosting();
			}		
		});
		
		txtCognome.setStyleAttribute("padding-left", "10px");
		txtCognome.setStyleAttribute("font-size", "15px");	
	  	
	  	btnAddCosting= new Button();
	  	btnAddCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.addList()));
	  	btnAddCosting.setIconAlign(IconAlign.TOP);
	  	btnAddCosting.setToolTip("Nuovo Costing");
	  	btnAddCosting.setSize(26, 26);
	  	btnAddCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				DialogNewCosting d= new DialogNewCosting(username);
				d.show();
				d.addListener(Events.Hide, new Listener<ComponentEvent>() {
				     
					@Override
					public void handleEvent(ComponentEvent be) {
						try {
							cmbbxCosting.clear();
							caricaTabellaDatiCosting();
							getCosting();
							enableButton();
						} catch (Exception e) {
							e.printStackTrace();
							Window.alert("error: Impossibile caricare i dati in tabella.");
						}			
				    }
				});
			}
		});
	  	
	  	
	  	btnConfermaCosting=new Button();
	  	btnConfermaCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConfermaCosting.setIconAlign(IconAlign.BOTTOM);
	  	btnConfermaCosting.setToolTip("Conferma Costing");
	  	btnConfermaCosting.setSize(26, 26);
	  	btnConfermaCosting.setVisible(false);
	  	btnConfermaCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String operazione="C";				
				AdministrationService.Util.getInstance().editStatoCosting(idSelected, operazione, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on editStatoCosting();");						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result)
							Window.alert("Impossibile completare l'operazione selezionata!");
						else
							caricaTabellaDatiCosting();
						
					}
				});				
			}	  		
	  	});
	  	
	  	
	  	btnScartaCosting=new Button();
	  	btnScartaCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.respingi()));
	  	btnScartaCosting.setIconAlign(IconAlign.BOTTOM);
	  	btnScartaCosting.setToolTip("Respingi la versione del Costing");
	  	btnScartaCosting.setSize(26, 26);
	  	btnScartaCosting.setVisible(false);
	  	btnScartaCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String operazione="S";				
				AdministrationService.Util.getInstance().editStatoCosting(idSelected, operazione, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on editStatoCosting();");						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result)
							Window.alert("Impossibile completare l'operazione selezionata!");
						else
							caricaTabellaDatiCosting();
						
					}
				});	
			}  		
	  	});
	  	
	  	btnChiudiCosting=new Button();
	  	btnChiudiCosting.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.azzera()));
	  	btnChiudiCosting.setIconAlign(IconAlign.BOTTOM);
	  	btnChiudiCosting.setToolTip("Chiudi definitivamente il Costing");
	  	btnChiudiCosting.setSize(26, 26);
	  	btnChiudiCosting.setVisible(false);
	  	btnChiudiCosting.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String operazione="D";				
				AdministrationService.Util.getInstance().editStatoCosting(idSelected, operazione, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on editStatoCosting();");						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result)
							Window.alert("Impossibile completare l'operazione selezionata!");
						else{
							caricaTabellaDatiCosting();
							disableButton();
						}
					}
					
				});	
			}	  		
	  	});
	  
	    btnAddRisorsa= new Button();
	    btnAddRisorsa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.addUser()));
	    btnAddRisorsa.setIconAlign(IconAlign.TOP);
	    btnAddRisorsa.setToolTip("Aggiungi Risorsa");
	    btnAddRisorsa.setSize(26, 26);
	    btnAddRisorsa.setEnabled(false);
	    btnAddRisorsa.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				CostingRisorsaModel cm= new CostingRisorsaModel();  
		        
		        gridCostingDipendente.stopEditing();  
		        storeCostingRisorsa.insert(cm, 0);  
		        gridCostingDipendente.startEditing(storeCostingRisorsa.indexOf(cm), 0);  
			}
		});
	  
	    btnDelRisorsa= new Button();
	    btnDelRisorsa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.delete()));
	    btnDelRisorsa.setIconAlign(IconAlign.TOP);
	    btnDelRisorsa.setToolTip("Elimina Risorsa");
	    btnDelRisorsa.setSize(26, 26);
	    btnDelRisorsa.setEnabled(false);
	    btnDelRisorsa.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				int idRisorsa;
				idRisorsa=cm.getSelectedItem().get("idCostingRisorsa");
				
				AdministrationService.Util.getInstance().deleteRisorsaCosting(idRisorsa, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on deleteRisorsaCosting()");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							caricaTabellaCostingRisorsa(idSelected);
						else
							Window.alert("Impossibile accedere ai dati sul costing delle risorse!");
						
					}
				});
				
			}
		});	  	
	  			
	    btnConfermaDip=new Button();
	    btnConfermaDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	    btnConfermaDip.setIconAlign(IconAlign.TOP);
	    btnConfermaDip.setToolTip("Conferma Modifiche");
	    btnConfermaDip.setSize(26, 26);
	    btnConfermaDip.setEnabled(false);
	    btnConfermaDip.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<CostingRisorsaModel> lista=new ArrayList<CostingRisorsaModel>();
		  		lista.addAll(storeCostingRisorsa.getModels());
		  		String commessa;
		  		for(CostingRisorsaModel c:lista){
		  			commessa=c.get("commessa");
		  			if(commessa.compareTo("TOTALE")!=0)
		  			AdministrationService.Util.getInstance().confermaCostingDipendente(idSelected, c, new AsyncCallback<Boolean>() {
		  				@Override
		  				public void onFailure(Throwable caught) {
		  					Window.alert("Errore di connessione on saveAssociaCostiHwSw()");				
		  				}

		  				@Override
		  				public void onSuccess(Boolean result) {
		  					
		  					if(!result)
		  						Window.alert("Impossibile effettuare le modifiche indicate!");
		  					else{
		  						storeCostingRisorsa.commitChanges();
		  						caricaTabellaCostingRisorsa(idSelected);
		  					}
		  						
		  				}
		  			});
		  		}
			}
		});	  
	    
	    btnConfermaNewVersione=new Button();
	    btnConfermaNewVersione.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.newVersion()));
	    btnConfermaNewVersione.setIconAlign(IconAlign.TOP);
	    btnConfermaNewVersione.setToolTip("Salva come nuova versione");
	    btnConfermaNewVersione.setSize(26, 26);
	    btnConfermaNewVersione.setEnabled(false);
	    btnConfermaNewVersione.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				AdministrationService.Util.getInstance().saveNewVersionCosting(idSelected, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on saveAssociaCostiHwSw()");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(!result)
	  						Window.alert("Impossibile confermare la nuova versione!");
	  					else{
	  						//storeCostingRisorsa.commitChanges();
	  						caricaTabellaDatiCosting();
	  					}
						
					}
				});				
			}
		});	  
	  
	    
	    btnAddDatitrasferta= new Button();
	    btnAddDatitrasferta.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	    btnAddDatitrasferta.setIconAlign(IconAlign.TOP);
	    btnAddDatitrasferta.setToolTip("Aggiungi Dati Trasferta");
	    btnAddDatitrasferta.setSize(26, 26);
	    btnAddDatitrasferta.setEnabled(false);
	    btnAddDatitrasferta.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				int idRisorsa=0;
								
				if(cm.getSelectedItem()!=null){
					idRisorsa= cm.getSelectedItem().get("idRisorsa");
					Integer id=(Integer)cm.getSelectedItem().get("idCostingRisorsa");
					if(id!= null){	
						DialogDatiTrasferta dlgDatiTrasferta= new DialogDatiTrasferta(idRisorsa, id );
						dlgDatiTrasferta.setWidth(910);
						dlgDatiTrasferta.setHeading("Dati trasferta per la risorsa selezionata");
						dlgDatiTrasferta.setButtons("");
						dlgDatiTrasferta.show();
					}
				}
				
			}
		});
	    
	    
		cmCosting = new ColumnModel(createColumnsCosting());		
		gridCosting= new EditorGrid<CostingModel>(storeCosting, cmCosting);  
		gridCosting.setBorders(false);  
		gridCosting.setItemId("grid");
	    gridCosting.setColumnLines(true);
	    gridCosting.setStripeRows(true);
	    gridCosting.addPlugin(sm);
	    gridCosting.setSelectionModel(sm);
	    gridCosting.getView().setShowDirtyCells(false);	
	    //gridCosting.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
	    gridCosting.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<CostingModel>>() {  
	          public void handleEvent(SelectionChangedEvent<CostingModel> be) {  
		        	
		            if (be.getSelection().size() > 0) { 
		            	//btnAddRisorsa.enable();
		            	//btnDelRisorsa.enable();
		            	//btnConfermaDip.enable();
		            	//btnConfermaNewVersione.enable();
		            	idSelected=be.getSelectedItem().get("idCosting");
		            	caricaTabellaCostingRisorsa(idSelected);           
		            }             
		      }		            
		}); 
	    
	    cm=new CellSelectionModel<CostingRisorsaModel>();
	    cm.setSelectionMode(SelectionMode.SIMPLE);
   
	    cmCostingRisorsa=new ColumnModel(createColumnsCostingRisorse());
	    gridCostingDipendente= new EditorGrid<CostingRisorsaModel>(storeCostingRisorsa, cmCostingRisorsa);
	    gridCostingDipendente.setBorders(false);
	    gridCostingDipendente.setItemId("grid");
	    gridCostingDipendente.setStripeRows(true); 
	    gridCostingDipendente.setColumnLines(true);
	    gridCostingDipendente.setSelectionModel(cm);
	    gridCostingDipendente.setWidth(2600);
	    gridCostingDipendente.addListener(Events.CellClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				if(cm.getSelectedItem()!=null){
					Integer idCostingRis=(Integer) cm.getSelectedItem().get("idCostingRisorsa");
					if(idCostingRis!=null)
						if(idCostingRis!=0)
							btnAddDatitrasferta.setEnabled(true);
						else
							btnAddDatitrasferta.setEnabled(false);
					else
						btnAddDatitrasferta.setEnabled(false);
				}
			}
		});	  
	    
	    cpGridCosting.add(gridCosting); 
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(cmbbxCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(btnAddCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(btnConfermaCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(btnScartaCosting);
	    tlbCosting.add(new SeparatorToolItem());
	    tlbCosting.add(btnChiudiCosting);
	    cpGridCosting.setTopComponent(tlbCosting);
	    
	    cpGridCostingRisorsa.add(gridCostingDipendente);
	    tlbCostingRisorsa.add(new SeparatorToolItem());
	    tlbCostingRisorsa.add(btnAddRisorsa);
	    tlbCostingRisorsa.add(new SeparatorToolItem());
	    tlbCostingRisorsa.add(btnDelRisorsa);
	    tlbCostingRisorsa.add(new SeparatorToolItem());
	    tlbCostingRisorsa.add(btnConfermaDip);
	    tlbCostingRisorsa.add(new SeparatorToolItem());
	    tlbCostingRisorsa.add(btnAddDatitrasferta);
	    tlbCostingRisorsa.add(new SeparatorToolItem());
	    tlbCostingRisorsa.add(btnConfermaNewVersione);
	    
	    cpGridCostingRisorsa.setTopComponent(tlbCostingRisorsa);
	  	    
	    vp.add(cpGridCosting);
	    vp.add(cpGridCostingRisorsa);
	   
	    layoutContainer.add(vp, new FitData(0, 3, 3, 0));
				
		add(layoutContainer);
	}
	

	private List<ColumnConfig> createColumnsCosting() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			
		GridCellRenderer<CostingModel> renderer = new GridCellRenderer<CostingModel>() {
            public String render(CostingModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<CostingModel> store, Grid<CostingModel> grid) {
            	
            	String stato;
            	if((property.compareTo("stato")==0)){
            		stato=model.get("stato");
            		
            		if(stato.compareTo("A")==0){
            			config.style = config.style + ";background-color:#d2f5af;";//verde
            			return "";
            		}
            		else
            			if(stato.compareTo("R")==0||(stato.compareTo("C")==0)){
            				config.style = config.style + ";background-color:#f5afaf;";//rosso
            				return "";
            			}
            			else{
            				config.style = config.style + ";background-color:#fffb8c;";//giallo
            				return "";
            			}
            	}else
            	  	return model.get(property);       	
        }};
		
		ColumnConfig column = new ColumnConfig();  
	    
	    column = new ColumnConfig();  
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(250);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("area");  
	    column.setHeader("Area");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
	    column = new ColumnConfig();  
	    column.setId("descrizione");  
	    column.setHeader("Descrizione");  
	    column.setWidth(200);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("numeroRevisione");  
	    column.setHeader("#Revisione");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("stato");  
	    column.setHeader("Stato Approvazione");  
	    column.setWidth(60);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(renderer);
	    configs.add(column);
		
		return configs;
	}
	
	
	private List<ColumnConfig> createColumnsCostingRisorse() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		ColumnConfig column = new ColumnConfig();  
		
		CellEditor editorTxt;
		/*GridCellRenderer<CostingRisorsaModel> renderer = new GridCellRenderer<CostingRisorsaModel>() {
            public String render(CostingRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<CostingRisorsaModel> store, Grid<CostingRisorsaModel> grid) {
            	
            	String ncommessa=model.get("commessa");
            	if(ncommessa!=null)           	
            		if((property.compareTo("risorsa")==0 || property.compareTo("orePianificate")==0 || property.compareTo("lc")==0 || property.compareTo("costoConsulenza")==0
            			|| property.compareTo("efficienza")==0 || property.compareTo("tariffa")==0 || property.compareTo("costoTrasferta")==0 || property.compareTo("costoHw")==0 ||
            			property.compareTo("costoSw")==0) && ncommessa.compareTo("TOTALE")!=0){
            		config.style = config.style + ";background-color:#d2f5af;";//verde
            	 	return model.get(property);
            		}else
            	
            			if(ncommessa.compareTo("TOTALE")==0){
            				config.style = config.style + ";background-color:#f5afaf;" +"font-weight:bold;" ;//rosso
            				return model.get(property);
            	 	         
            			}else
            	          	return model.get(property);
            	else
            		if((property.compareTo("risorsa")==0 || property.compareTo("efficienza")==0 || property.compareTo("tariffa")==0)){
            			config.style = config.style + ";background-color:#d2f5af;";//verde
            			return model.get(property);
            		}
            		else
            			return model.get(property);
        }};*/
        
        GridCellRenderer<CostingRisorsaModel> rendererPerc = new GridCellRenderer<CostingRisorsaModel>() {
            public String render(CostingRisorsaModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<CostingRisorsaModel> store, Grid<CostingRisorsaModel> grid) {
				
				String val=model.get(property);
				String ncommessa=model.get("commessa");
				Float valf=(float)0;
				if(val!=null)
					if(val.compareTo("#")!=0){
						valf=Float.valueOf(val);	
						valf*=100;
					}					
            	if(ncommessa!=null)
            		if(ncommessa.compareTo("TOTALE")==0)
            			config.style = config.style + ";background-color:#f5afaf;" +"font-weight:bold;" ;//rosso
            	
				return number.format(valf)+"%";				
		}};
		
	    column = new ColumnConfig();  
	    column.setId("area");  
	    column.setHeader("Area");  
	    column.setWidth(100);
	    //column.setRenderer(renderer);
	    //column.setColumnStyleName("red-background");
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(100); 
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    //column.setColumnStyleName("red-background");
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("progetto");  
	    column.setHeader("Progetto");  
	    column.setWidth(100);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(80);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column = new ColumnConfig();  
	    column.setId("risorsa");  
	    column.setHeader("Risorsa");  
	    column.setWidth(150);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    ListStore<PersonaleModel> store= new ListStore<PersonaleModel>();
	    cmbxPersonale.setStore(store);
	    cmbxPersonale.setDisplayField("nomeCompleto");
	    cmbxPersonale.setEmptyText("Selezionare..");
	    cmbxPersonale.setEditable(true);
	    cmbxPersonale.setVisible(true);
	    cmbxPersonale.setTriggerAction(TriggerAction.ALL);
	    cmbxPersonale.setForceSelection(true);
	    cmbxPersonale.setAllowBlank(false);
	    cmbxPersonale.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					getAllDipendenti();					
			}		
		});
	    cmbxPersonale.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				int idPersonale=cmbxPersonale.getValue().get("idPersonale");
				int idCosting=cmbbxCosting.getValue().get("idCosting");
				final int index=cm.getSelectCell().row;
				
				AdministrationService.Util.getInstance().getDatiCostiDipendenteSelezionato(idPersonale, idCosting, new AsyncCallback<CostingRisorsaModel>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on getDatiCostiDipendenteSelezionato();");
					}

					@Override
					public void onSuccess(CostingRisorsaModel result) {
						if(result==null)
							Window.alert("Nessun dato di costo trovato per il dipendente selezionato.");
						else{
							gridCostingDipendente.stopEditing();
							storeCostingRisorsa.remove(index);
							storeCostingRisorsa.insert(result, index);
							gridCostingDipendente.startEditing(storeCostingRisorsa.indexOf(result), 0);
						}
					}
				});
			}		
		});    
	    CellEditor editor = new CellEditor(cmbxPersonale) {  
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return cmbxPersonale.getValue();  
	        } 
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return ((ModelData) value).get("nomeCompleto");  
	        }  
	    };
	    column.setEditor(editor);
	    configs.add(column);	    	
			
	    
	    column = new ColumnConfig();  
	    column.setId("costoOrario");  
	    column.setHeader("Costo Orario");  
	    column.setWidth(95);  
	    //column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    
	    column = new ColumnConfig();  
	    column.setId("oreLavoro");  
	    column.setHeader("Ore Lavoro");  
	    column.setWidth(95);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldOreLavoro= new TextField<String>();
	    //txtfldOreLavoro.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    //txtfldOreLavoro.getMessages().setRegexText("Deve essere un numero!");
	    txtfldOreLavoro.setValue("0.00");
	    editorTxt= new CellEditor(txtfldOreLavoro){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    //column.setRenderer(renderer);
	    configs.add(column);
	    
	    
	    column = new ColumnConfig();  
	    column.setId("oreViaggio");  
	    column.setHeader("Ore Viaggio");  
	    column.setWidth(95);  
	    //column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("giorniViaggio");  
	    column.setHeader("G/V");  
	    column.setWidth(35);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("diaria");  
	    column.setHeader("Diaria");  
	    column.setWidth(95);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoTotOre");  
	    column.setHeader("Costo Tot/h");  
	    column.setWidth(95);  
	  //  column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	  	    
	    column = new ColumnConfig();  
	    column.setId("costoTrasferta");  
	    column.setHeader("Costo Trasferta");  
	    column.setWidth(95);  
	  //  column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoTotale");  
	    column.setHeader("Costo Totale");  
	    column.setWidth(100);  
	   // column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("efficienza");  
	    column.setHeader("Efficienza");  
	    column.setWidth(65);  
	  //  column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldEfficienza= new TextField<String>();
	    txtfldEfficienza.setValue("1.0");
	    editorTxt= new CellEditor(txtfldEfficienza){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column);
	   	
		column = new ColumnConfig();  
	    column.setId("oreDaFatturare");  
	    column.setHeader("Ore da Fatturare");  
	    column.setWidth(100);  
	  //  column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("oreTrasferta");  
	    column.setHeader("Ore Trasferta");  
	    column.setWidth(100);  
	  //  column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);	    
	    
	    column = new ColumnConfig();  
	    column.setId("tariffa");  
	    column.setHeader("Tariffa");  
	    column.setWidth(65);  
	 //   column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldTariffa= new TextField<String>();
	    txtfldTariffa.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldTariffa.getMessages().setRegexText("Deve essere un numero!");
	    txtfldTariffa.setValue("0.00");
	    editorTxt= new CellEditor(txtfldTariffa){
	    	@Override  
	        public Object preProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    
	        @Override  
	        public Object postProcessValue(Object value) {  
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();  
	        }  
	    };	    
	    column.setEditor(editorTxt);
	    configs.add(column);
 
	    column = new ColumnConfig();  
	    column.setId("fatturatoTotale");  
	    column.setHeader("Fatturato");    
	    column.setWidth(80);  
	 //   column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	  	    
	    column = new ColumnConfig();  
	    column.setId("ebit");  
	    column.setHeader("EBIT");    
	    column.setWidth(80);  
	 //   column.setRenderer(renderer);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("ebitPerc");  
	    column.setHeader("EBIT%");    
	    column.setWidth(50);  
	//    column.setRenderer(rendererPerc);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    return configs;
	}
	
	private void getAllDipendenti() {	
		AdministrationService.Util.getInstance().getListaDipendentiModel("", new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getListaDipendentiModel();");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){		
					ListStore<PersonaleModel> lista= new ListStore<PersonaleModel>();
					lista.setStoreSorter(new StoreSorter<PersonaleModel>());  
					lista.setDefaultSort("nomeCompleto", SortDir.ASC);
					
					lista.add(result);				
					cmbxPersonale.clear();
					cmbxPersonale.setStore(lista);					
				}else Window.alert("error: Errore durante l'accesso ai dati Personale.");				
			}
		});
	}
	
	
	private void caricaTabellaDatiCosting() {
		int costing;
		if(cmbbxCosting.getValue()!=null)
			costing=cmbbxCosting.getValue().get("idCosting");
		else
			costing=0;
		
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
	
	
	private void loadTableCosting(List<CostingModel> result) {
		String stato= new String();
		if(result.size()>0)
			stato=result.iterator().next().get("stato");
		if(stato.compareTo("C")==0)
			disableButton();
		else
			enableButton();
		
		storeCosting.removeAll();
		storeCosting.setStoreSorter(new StoreSorter<CostingModel>());  
	    storeCosting.setDefaultSort("numeroRevisione", SortDir.DESC);
		storeCosting.add(result);
		caricaTabellaCostingRisorsa(idSelected);
	}
		
	
	private void caricaTabellaCostingRisorsa(int idCosting) {
		
		//btnAddRisorsa.enable();
		//btnDelRisorsa.enable();
		AdministrationService.Util.getInstance().getRiepilogoDatiCostingRisorse(idCosting, new AsyncCallback<List<CostingRisorsaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connession on getDatiCostiRisorse();");
				
			}

			@Override
			public void onSuccess(List<CostingRisorsaModel> result) {
				loadTableCostingRisorse(result);
			}		
		});		
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
					if(result.compareTo("PM")==0||result.compareTo("DIR")==0)				
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
					
					else{
						//carico tutti i costing perchè username è ""
						btnConfermaCosting.setVisible(true);
						btnScartaCosting.setVisible(true);
						btnChiudiCosting.setVisible(true);
						username="";//non è un PM che ha salvato il costing
						AdministrationService.Util.getInstance().getListaDatiCosting(username,new AsyncCallback<List<CostingModel>>() {

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
				}		
				@Override
				public void onFailure(Throwable caught) {				
					Window.alert("Error on getRuolo();");
				}
		});					
	}	
	
	
	private void disableButton() {
		btnAddRisorsa.disable();
		btnChiudiCosting.disable();
		btnConfermaCosting.disable();
		btnConfermaDip.disable();
		btnDelRisorsa.disable();
		btnScartaCosting.disable();	
		btnConfermaNewVersione.disable();
	}
	
	private void enableButton(){
		btnAddRisorsa.enable();
		btnChiudiCosting.enable();
		btnConfermaCosting.enable();
		btnConfermaDip.enable();
		btnDelRisorsa.enable();
		btnScartaCosting.enable();
		btnConfermaNewVersione.enable();
	}
	
}


