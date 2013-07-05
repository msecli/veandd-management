package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoSituazioneMensileOreDipendenti extends LayoutContainer{	
	
	private GroupingStore<RiepilogoFoglioOreModel>store = new GroupingStore<RiepilogoFoglioOreModel>();
	private Grid<RiepilogoFoglioOreModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	private Date data;
	
	private SimpleComboBox<String> smplcmbxPM;
	private SimpleComboBox<String> smplcmbxSede;
	private TextField<String> txtfldSearch;
	private Button btnConferma;
	private Button btnConfermaTutti;
	private Button btnViewFoglioOre;
	private Button btnSearch;
	private DateField dtfldDataRiferimento;
	
	public PanelRiepilogoSituazioneMensileOreDipendenti(){
	
	}
			
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	  		
	    LayoutContainer bodyContainer = new LayoutContainer();
	    bodyContainer.setLayout(new FlowLayout());
	  	bodyContainer.setBorders(false);
	    
	  	ToolBar toolBar= new ToolBar();
	  	
	  	dtfldDataRiferimento= new DateField();
	  	dtfldDataRiferimento.setValue(new Date());
	  	
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
		smplcmbxSede.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				caricaTabellaDati();
			}		
		});
	  	
	  	txtfldSearch= new TextField<String>();
	  	txtfldSearch.setEmptyText("Cerca Dipendente..");
	  	
	  	btnSearch= new Button();
	  	btnSearch.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
		btnSearch.setIconAlign(IconAlign.TOP);
		btnSearch.setEnabled(false);
	  	
	  	btnConferma= new Button();
	  	btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConferma.setIconAlign(IconAlign.TOP);
			  	
	  	btnConfermaTutti= new Button();
	  	btnConfermaTutti.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConfermaTutti.setIconAlign(IconAlign.TOP);
	  	
	  	btnViewFoglioOre= new Button();
	  	btnViewFoglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
	  	btnViewFoglioOre.setIconAlign(IconAlign.TOP);
	  	
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
		cntpnlGrid.setWidth(550);
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
	    view.setForceFit(true);  
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
	    
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<PersonaleAssociatoModel>>() {  
	          public void handleEvent(SelectionChangedEvent<PersonaleAssociatoModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	            	              
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
		cntpnlLayout.setSize(570, 860);
		cntpnlLayout.setScrollMode(Scroll.AUTOX);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.add(cntpnlGrid);
	    
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
		column.setWidth(120);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 		    
			
		column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	      
	    configs.add(column); 
	    	    	    	    	    
		return configs;
	}

}
