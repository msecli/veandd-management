package gestione.pack.client.layout;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.GestioneRdoCommesse;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_GestioneRdoCommesseAll extends LayoutContainer {

	
	public CenterLayout_GestioneRdoCommesseAll(){
	}
	
	private GroupingStore<GestioneRdoCommesse>store = new GroupingStore<GestioneRdoCommesse>();
	private GroupingStore<GestioneRdoCommesse> storeCompleto= new GroupingStore<GestioneRdoCommesse>();
	private GroupingStore<GestioneRdoCommesse> storeResult= new GroupingStore<GestioneRdoCommesse>();
	private List<GestioneRdoCommesse> listaStore= new ArrayList<GestioneRdoCommesse>();
	private Grid<GestioneRdoCommesse> gridRiepilogo;
	private RowEditor<GestioneRdoCommesse> re ; 
	private ColumnModel cm;
	
	private Button btnRiepilogoSalPcl;
	private Button btnRiepilogoOreOrdine;
	
	//private String commessa;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
		
		try {
	    	cm = new ColumnModel(createColumns());	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}
		caricaTabella();
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
			
		/*LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		*/
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-180, h-55);
		//cpGrid.setPosition(3, 3);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
								
		Resizable r=new Resizable(cpGrid);
	    r.setMinWidth(w-200);
	    r.setMinHeight(h-80);
		  	
	    ToolBar tlbGrid=new ToolBar();
	    tlbGrid.setBorders(false);
	    
	    final TextField<String> txtfldsearch= new TextField<String>();
	    Button btnSearch= new Button();
	    
	    btnSearch.setSize(26, 26);
	    btnSearch.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
	    btnSearch.setIconAlign(IconAlign.TOP);
	    btnSearch.setEnabled(false);
	    	    
	    txtfldsearch.addKeyListener(new KeyListener(){
	    	 public void componentKeyUp(ComponentEvent event) {
	    		 
	    		 if(txtfldsearch.getRawValue().isEmpty()){
	    			 storeResult.removeAll();
	    			 store.removeAll();
	    			 store.add(storeCompleto.getModels());
	    			 gridRiepilogo.reconfigure(store, cm);
	    		 }else{
	    		 	    		 	    		 
	    			 String campo= txtfldsearch.getValue().toString();	    			 
	    			 
	    			 storeResult.removeAll();
	    			 for(GestioneRdoCommesse r:listaStore){
	    				 if(r.getNumeroCommessa().contains(campo) || r.getNumeroOfferta().compareTo(campo)==0 || 
	    						 r.getNumeroOrdine().contains(campo) || r.getNumeroRdo().compareTo(campo)==0){
	    					 storeResult.add(r);		    				 
	    				 }
	    			 }
	    			 listaStore.clear();
	    			 listaStore.addAll(store.getModels());
	    			 gridRiepilogo.reconfigure(storeResult, cm);			 
	    		 } 
	    	 }	    	  	 
	    });		   
	    		
		GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(false);  
	    view.setGroupRenderer(new GridGroupRenderer() {  
	      public String render(GroupColumnData data) {  
	        String f = cm.getColumnById(data.field).getHeader();  
	        //String l = data.models.size() == 1 ? "Item" : "Items";  
	        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
	      }  
	    }); 
	    
	    re= new RowEditor<GestioneRdoCommesse>();
		
		gridRiepilogo= new Grid<GestioneRdoCommesse>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);  
		gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
		gridRiepilogo.setView(view);
	    gridRiepilogo.addPlugin(re);
		gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RdoCompletaModel>>() {  
		      public void handleEvent(SelectionChangedEvent<RdoCompletaModel> be) {  
		        	
		           
		      }     
		});
		
		btnRiepilogoOreOrdine=new Button();
		btnRiepilogoOreOrdine.setSize(26, 26);
		btnRiepilogoOreOrdine.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
		btnRiepilogoOreOrdine.setIconAlign(IconAlign.TOP);
		btnRiepilogoOreOrdine.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
			}
		});
		
		btnRiepilogoSalPcl=new Button();
		btnRiepilogoSalPcl.setSize(26, 26);
		btnRiepilogoSalPcl.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
		btnRiepilogoSalPcl.setIconAlign(IconAlign.TOP);
		
		
		tlbGrid.add(txtfldsearch);
		tlbGrid.add(btnSearch);
		
		cpGrid.add(gridRiepilogo);	
		cpGrid.setTopComponent(tlbGrid);
		
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		add(layoutContainer);
			
	}

	
	private void caricaTabella() {
		AdministrationService.Util.getInstance().getAllRdoCommesse( new AsyncCallback<List<GestioneRdoCommesse>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getAllRdoCommesse()");			
			}
	
			@Override
			public void onSuccess(List<GestioneRdoCommesse> result) {
				if(result.size()>0)
					loadTable(result);
				else
					Window.alert("Nessun Dato.");
			}
		});		
	}

	
	private void loadTable(List<GestioneRdoCommesse> lista) {
		try {
			store.removeAll();
			store.setStoreSorter(new StoreSorter<GestioneRdoCommesse>());  
		    store.setDefaultSort("pm", SortDir.ASC);
			store.add(lista);
			storeResult.removeAll();
			storeCompleto.removeAll();
			storeResult.add(store.getModels());
			storeCompleto.add(store.getModels());
			listaStore.addAll(store.getModels());
						    		    	
		} catch (NullPointerException e) {
				e.printStackTrace();
		}
	}	
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("pm");  
	    column.setHeader("PM");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
		
		column=new ColumnConfig();		
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(140);  
	    column.setRowHeader(true); 
	    configs.add(column); 
			    	    
	    column=new ColumnConfig();		
	    column.setId("numeroOrdine");  
	    column.setHeader("N. Ordine");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldNumeroOrdine= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldNumeroOrdine));  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("dataOrdine");  
	    column.setHeader("Data Ordine");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("statoOrdine");  
	    column.setHeader("Stato Ordine");  
	    column.setWidth(50);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldNumeroCommessa= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldNumeroCommessa));  
	    configs.add(column);
	          
	    column=new ColumnConfig();		
	    column.setId("estensioneCommessa");
	    column.setHeader("Est.");  
	    column.setWidth(40);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldNumeroEst= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldNumeroEst));
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("statocommesa");  
	    column.setHeader("Stato C.");  
	    column.setWidth(50);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("tipologiaCommessa");  
	    column.setHeader("Tipo C.");  
	    column.setWidth(50);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();
	    column.setId("tariffaSalPcl");  
	    column.setHeader("Tariffa Sal/Pcl");  
	    column.setWidth(70);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldTariffaSalPcl= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldTariffaSalPcl));
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("oreLavoroCommessa");  
	    column.setHeader("Ore Lav.");  
	    column.setWidth(60);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldOreLAvoroComm= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldOreLAvoroComm));
	    configs.add(column);
	          	    
	    column=new ColumnConfig();		
		column.setId("numeroRdo");  
		column.setHeader("N. Rda");  
		column.setWidth(80);  
		column.setRowHeader(true); 
		column.setAlignment(HorizontalAlignment.RIGHT); 
		TextField<String> txtfldnumeroRdo= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldnumeroRdo));
		configs.add(column);
	    
		column=new ColumnConfig();		
		column.setId("descrizione");  
		column.setHeader("Descrizione");  
		column.setWidth(180);  
		column.setRowHeader(true); 
		column.setAlignment(HorizontalAlignment.RIGHT); 
		TextField<String> txtfldDescrizione= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldDescrizione));
		configs.add(column);
		
		column=new ColumnConfig();		
		column.setId("numeroOfferta");  
		column.setHeader("N. Offerta");  
		column.setWidth(80);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		TextField<String> txtfldNumeroOfferta= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldNumeroOfferta));
		configs.add(column);
	      	    
	    column=new ColumnConfig();		
	    column.setId("tariffaOraria");  
	    column.setHeader("Tariffa Ord.");  
	    column.setWidth(50);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldTariffaOraria= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldTariffaOraria));
	    configs.add(column);
	 	     
	    column=new ColumnConfig();		
	    column.setId("numeroOre");  
	    column.setHeader("Ore Ordine");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldNumeroOreOrdine= new TextField<String>();
	    column.setEditor(new CellEditor(txtfldNumeroOreOrdine));
	    configs.add(column);
	    	  	    
	    return configs;
	}
	
}
