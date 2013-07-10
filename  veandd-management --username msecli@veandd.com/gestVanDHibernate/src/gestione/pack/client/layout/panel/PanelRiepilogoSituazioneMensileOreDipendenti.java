package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoSituazioneMensileOreDipendenti extends LayoutContainer{	
	
	private GroupingStore<RiepilogoFoglioOreModel>store = new GroupingStore<RiepilogoFoglioOreModel>();
	private Grid<RiepilogoFoglioOreModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	private Date data;
	private String username;
	
	private GroupingStore<RiepilogoFoglioOreModel> storeCompleto= new GroupingStore<RiepilogoFoglioOreModel>();
	private GroupingStore<RiepilogoFoglioOreModel> storeResult= new GroupingStore<RiepilogoFoglioOreModel>();
	private List<RiepilogoFoglioOreModel> lista= new ArrayList<RiepilogoFoglioOreModel>();
	
	private SimpleComboBox<String> smplcmbxPM;
	private SimpleComboBox<String> smplcmbxSede;
	private TextField<String> txtfldSearch;
	private Button btnConferma;
	private Button btnConfermaTutti;
	private Button btnViewFoglioOre;
	private Button btnSearch;
	private DateField dtfldDataRiferimento;
	private Text txtRuolo;
	
	public PanelRiepilogoSituazioneMensileOreDipendenti(){
	
	}
			
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	    layoutContainer.setScrollMode(Scroll.NONE);
	  		
	    LayoutContainer bodyContainer = new LayoutContainer();
	    bodyContainer.setLayout(new FlowLayout());
	  	bodyContainer.setBorders(false);
	    
	  	ToolBar toolBar= new ToolBar();
	  	
	  	dtfldDataRiferimento= new DateField();
	  	dtfldDataRiferimento.setValue(new Date());
	  	
	  	txtRuolo=new Text();
	  	recuperoSessionRuolo();
	  	
	   	smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setEmptyText("Project Manager..");
		smplcmbxPM.setAllowBlank(false);
		getNomePM();
	  	
		smplcmbxSede=new SimpleComboBox<String>();
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setWidth(65);
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
		smplcmbxSede.setStyleAttribute("padding-left", "2px");
		smplcmbxSede.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				caricaTabellaDati();
			}		
		});
	  	
	  	txtfldSearch= new TextField<String>();
	  	txtfldSearch.setStyleAttribute("padding-left", "2px");
	  	txtfldSearch.setEmptyText("Cerca Dipendente..");
	  	txtfldSearch.addKeyListener(new KeyListener(){
	    	 public void componentKeyUp(ComponentEvent event) {
	    		 
	    		 if(txtfldSearch.getRawValue().isEmpty()){
	    			 storeResult.removeAll();
	    			 store.removeAll();
	    			 store.add(storeCompleto.getModels());
	    			 gridRiepilogo.reconfigure(store, cmCommessa);
	    		 }else{
	    		 	    		 	    		 
	    			 String campo= txtfldSearch.getValue().toString();	    			 	    			 
	    			 storeResult.removeAll();
	    			 for(RiepilogoFoglioOreModel r:lista){
	    				 if(r.getNome().contains(campo)){
	    					 storeResult.add(r);		    				 
	    				 }
	    			 }
	    			 lista.clear();
	    			 lista.addAll(store.getModels());
	    			 gridRiepilogo.reconfigure(storeResult, cmCommessa);			 
	    		 } 
	    	 }	    	  	 
	  	});	
	  	
	  	btnSearch= new Button();
	  	btnSearch.setStyleAttribute("padding-left", "4px");
	  	btnSearch.setStyleAttribute("padding-bottom", "2px");
	  	btnSearch.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
		btnSearch.setIconAlign(IconAlign.TOP);
		btnSearch.setEnabled(false);
	  	
	  	btnConferma= new Button();
	  	btnConferma.setStyleAttribute("padding-left", "2px");
	  	btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConferma.setIconAlign(IconAlign.TOP);
	  	btnConferma.setToolTip("Conferma mese per singolo dipendente.");
	  	btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				AdministrationService.Util.getInstance().confermaGiorniDipendente(username, data, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						
						Window.alert("Errore connessione on confermaGiorniDipendente();");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result==true)
							caricaTabellaDati();
						else
							Window.alert("Non è stato possibile effettuare la conferma!");
					}
				});
				
			}
		});
			  	
	  	btnConfermaTutti= new Button();
	  	btnConfermaTutti.setStyleAttribute("padding-left", "2px");
	  	btnConfermaTutti.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConfermaTutti.setIconAlign(IconAlign.TOP);
	  	btnConfermaTutti.setToolTip("Conferma mese per tutti i dipendenti.");
	  	btnConfermaTutti.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				AdministrationService.Util.getInstance().confermaGiorniTuttiDipendenti(smplcmbxSede.getRawValue().toString(), data, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						
						Window.alert("Errore connessione on confermaGiorniTuttiDipendenti();");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result==true)
							caricaTabellaDati();
						else
							Window.alert("Non è stato possibile effettuare la conferma!");
					}
				});			
			}
		});
	  	
	  	btnViewFoglioOre= new Button();
	  	btnViewFoglioOre.setStyleAttribute("padding-left", "2px");
	  	btnViewFoglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	  	btnViewFoglioOre.setIconAlign(IconAlign.TOP);
	  	btnViewFoglioOre.setToolTip("Modifica dati.");
	  	btnViewFoglioOre.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				Dialog d =new  DialogRilevazionePresenze(data,username);
				d.show();
								
				d.addListener(Events.Hide, new Listener<ComponentEvent>() {			     
					@Override
					public void handleEvent(ComponentEvent be) {
						caricaTabellaDati();			
				    }
				});				
			}
		});
	  	
	  	toolBar.add(dtfldDataRiferimento);
	  	//toolBar.add(smplcmbxPM);
	  	toolBar.add(smplcmbxSede);
	  	toolBar.add(txtfldSearch);
	  	toolBar.add(btnSearch);
	  	toolBar.add(btnConferma);
	  	toolBar.add(btnConfermaTutti);
	  	toolBar.add(btnViewFoglioOre);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(530);
		cntpnlGrid.setHeight(840);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
					    
	    try {	    	
	    	cmCommessa = new ColumnModel(createColumns());    	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    store.groupBy("nome");
	    store.setSortField("giorno");
	    	        
	    GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(false);  
	    view.setStartCollapsed(true);
	    view.setGroupRenderer(new GridGroupRenderer() {  
	      public String render(GroupColumnData data) {  
	        String f = cmCommessa.getColumnById(data.field).getHeader();  
	       // String l = data.models.size() == 1 ? "Item" : "Items";  
	        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
	      }  
	    });  
	    
	    gridRiepilogo= new Grid<RiepilogoFoglioOreModel>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true);  
	    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridRiepilogo.setView(view);
	    gridRiepilogo.getView().setShowDirtyCells(false);
	   
	    gridRiepilogo.getView().setViewConfig(new GridViewConfig(){
	    	@Override
	        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
	            if (model != null) {	                                    
	                if (!(Boolean)model.get("compilato")) 
	                    return "red-row";               
	            }
				return "";            
	    	}    	
	    });
	    
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoFoglioOreModel>>() {  
	          public void handleEvent(SelectionChangedEvent<RiepilogoFoglioOreModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	            	String d=be.getSelectedItem().get("giorno");
	            	String u=be.getSelectedItem().get("username");	  
	            	
	            	String giorno=d.substring(0,2);
	            	String anno=d.substring(7,d.length());
	            	String mese=ClientUtility.traduciMeseToNumber(d.substring(3, 6));
	            	
	            	Date retVal = null;
	 		        try
	 		        	{
	 		            retVal = DateTimeFormat.getFormat( "dd-MM-yyyy" ).parse(giorno+"-"+mese+"-"+anno);
	 		        	}
	 		        	catch ( Exception e )
	 		        	{
	 		            retVal = null;
	 		        	}
	 		        
	 		        data=new Date(retVal.getTime());
	 		        username=u;
	            	              
	            } else {  
	                
	            	           	
	            }
	          }
	    }); 
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(toolBar);
	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);
		cntpnlLayout.setHeading("Riepilogo Giornaliero.");
		cntpnlLayout.setSize(1190, 865);
		cntpnlLayout.setScrollMode(Scroll.AUTOX);
		cntpnlLayout.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cntpnlLayout.setFrame(true);
		
		cntpnlLayout.add(cntpnlGrid);
	    cntpnlLayout.add(new PanelRichiesteDipendenti());
		
		bodyContainer.add(cntpnlLayout); 
				
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);    
	}


	private void getNomePM() {
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxPM.add(result);
					//smplcmbxPM.add("Tutti");
					smplcmbxPM.recalculate();
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});				
	}
	

	private void caricaTabellaDati() {
		String pm= smplcmbxPM.getRawValue().toString();
		String sede= smplcmbxSede.getRawValue().toString();
		
		AdministrationService.Util.getInstance().getRiepilogoMeseFoglioOre(dtfldDataRiferimento.getValue(),
				pm, sede,  new AsyncCallback<List<RiepilogoFoglioOreModel>>() {	
			
			@Override
			public void onSuccess(List<RiepilogoFoglioOreModel> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						//Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");				
					}
					else loadTable(result);			
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  		
	}
	
	
	private void loadTable(List<RiepilogoFoglioOreModel> result) {
		try {
			
			store.removeAll();
			store.add(result);
			gridRiepilogo.reconfigure(store, cmCommessa);	
			
			storeResult.removeAll();
			storeCompleto.removeAll();
			storeResult.add(store.getModels());
			storeCompleto.add(store.getModels());
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}
		
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();
		column=new SummaryColumnConfig<Double>();		
		column.setId("nome");  
		column.setHeader("Dipendente");  
		column.setWidth(160);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 		    
			
		column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(100);  
	    column.setRowHeader(true);        
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();
	    column.setId("i1");  
		column.setHeader("I");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("u1");  
		column.setHeader("U");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("i2");  
		column.setHeader("I");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("u2");  
		column.setHeader("U");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("i3");  
		column.setHeader("I");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("u3");  
		column.setHeader("U");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);

		column=new SummaryColumnConfig<Double>();
	    column.setId("i4");  
		column.setHeader("I");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
		column=new SummaryColumnConfig<Double>();
	    column.setId("u4");  
		column.setHeader("U");  
		column.setWidth(40);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);
		
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("confermato");  
	    column.setHeader("Confermato");  
	    column.setWidth(45);  
	    column.setRowHeader(true);  
	    column.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {

			@Override
			public Object render(RiepilogoFoglioOreModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoFoglioOreModel> store, Grid<RiepilogoFoglioOreModel> grid) {
				
				String color = "#90EE90";
				String confermato=model.get("confermato");
				
				if(confermato.compareTo("0")!=0)
				{
					config.style = config.style + ";background-color:" + color + ";";									
				}
				else{
					color = "#F08080";  
					config.style = config.style + ";background-color:" + color + ";";
				}
				return "";
			}
		});
	      
	    configs.add(column);
	    	    	    	    	    
		return configs;
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
		txtRuolo.setText(result);		
		
			if(txtRuolo.getText().compareTo("PM")==0){
				btnConferma.disable();
				btnConfermaTutti.disable();
			}
	}
}
