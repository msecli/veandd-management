package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PanelRiepilogoSalPclMese  extends LayoutContainer{

	private GroupingStore<RiepilogoSALPCLModel>store = new GroupingStore<RiepilogoSALPCLModel>();
	private Grid<RiepilogoSALPCLModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private String tabSelected="";
	private String data;
	
	private SimpleComboBox<String> smplcmbxScelta= new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxOrderBy;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoSalPclMese(String tabSelected, String data){
		this.tabSelected=tabSelected;
		this.data=data;
	}
			
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    final FitLayout fl= new FitLayout();
		
		try {
			if(tabSelected.compareTo("riassunto")==0){
				cmRiepilogo=new ColumnModel(createColumnsRiassunto());
				caricaTabellaRiass();
			}
			else{
				cmRiepilogo = new ColumnModel(createColumnsSalPcl());
				caricaTabella();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}
		
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(false);
		cpGrid.setSize(w-225, h-140);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
								
		Resizable r=new Resizable(cpGrid);
	   	    		
		smplcmbxScelta.setFieldLabel("SAL/PCL");
		smplcmbxScelta.setEmptyText("Selezionare..");
		smplcmbxScelta.setAllowBlank(false);
		smplcmbxScelta.setVisible(false);
		smplcmbxScelta.add("SAL");
		smplcmbxScelta.add("PCL");
		smplcmbxScelta.setWidth(80);
		if(tabSelected.compareTo("riassunto")==0)
			smplcmbxScelta.setVisible(true);
		
		smplcmbxOrderBy= new SimpleComboBox<String>();
		smplcmbxOrderBy.setFieldLabel("Group By");
		smplcmbxOrderBy.setEmptyText("Selezionare..");
		smplcmbxOrderBy.setAllowBlank(false);
		smplcmbxOrderBy.setVisible(true);
		smplcmbxOrderBy.add("PM");
		smplcmbxOrderBy.add("Cliente");
		smplcmbxOrderBy.setWidth(80);
		smplcmbxOrderBy.setSimpleValue("PM");
		
		cpGrid.setTopComponent(smplcmbxScelta);
		
		
	    store.groupBy("pm");
	    
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(false);  
	    summary.setShowGroupedColumn(false);  
		      		    
	    gridRiepilogo= new EditorGrid<RiepilogoSALPCLModel>(store, cmRiepilogo);  
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
		    
	    cpGrid.add(gridRiepilogo);
	    
	    layoutContainer.add(cpGrid);
	    
	    add(layoutContainer);
	}

	private void caricaTabellaRiass() {
		tabSelected="sal";
		 
		AdministrationService.Util.getInstance().getRiepilogoSalPclRiassunto(data, tabSelected, new AsyncCallback<List<RiepilogoSALPCLModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoSalPcl();");
				
			}

			@Override
			public void onSuccess(List<RiepilogoSALPCLModel> result) {
				loadTableRiass(result);		
			}


		 });		
	}

	private List<ColumnConfig> createColumnsRiassunto() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("cliente");  
		column.setHeader("Cliente");  
		column.setWidth(150);  
		column.setRowHeader(true); 
	    configs.add(column); 
	    
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
		column.setHeader("Commessa");  
		column.setWidth(150);  
		column.setRowHeader(true); 
	    configs.add(column); 
	    
	    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
	    columnImporto.setId("importoComplessivo");  
	    columnImporto.setHeader("Tot.Euro");  
	    columnImporto.setWidth(80);    
	    columnImporto.setRowHeader(true); 
	    columnImporto.setAlignment(HorizontalAlignment.RIGHT);
	    columnImporto.setStyle("color:#e71d2b;");
	    columnImporto.setSummaryType(SummaryType.SUM);  
	    columnImporto.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {	
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}  	
		});  
	    columnImporto.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   				final NumberFormat number= NumberFormat.getFormat("#,##0.0#;-#"); 				
   				
				return number.format(value);
   			}  
      });  
	    configs.add(columnImporto); 
	    
		return configs;
	}

	private void caricaTabella() {
				
		AdministrationService.Util.getInstance().getRiepilogoSalPcl(data, tabSelected, new AsyncCallback<List<RiepilogoSALPCLModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoSalPcl();");
				
			}

			@Override
			public void onSuccess(List<RiepilogoSALPCLModel> result) {
				loadTable(result);		
			}

		});
	}

	private List<ColumnConfig> createColumnsSalPcl() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
		
		column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("estensione");  
	    column.setHeader("Est");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("cliente");  
		column.setHeader("Cliente");  
		column.setWidth(150);  
		column.setRowHeader(true); 
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attivita");  
	    column.setHeader("Oggetto Att.");  
	    column.setWidth(140);  
	    column.setRowHeader(true); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("precedente");  
	    column.setHeader("Mese Prec.");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT); 
	    column.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("variazione");  
	    column.setHeader("Variazione Mese");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT); 
	    column.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attuale");  
	    column.setHeader("Attuale");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT); 
	    column.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("tariffa");  
	    column.setHeader("Tariffa");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT); 
	    configs.add(column);
	    
	    
	    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
	    columnImporto.setId("importoComplessivo");  
	    columnImporto.setHeader("Tot.Euro");  
	    columnImporto.setWidth(80);    
	    columnImporto.setRowHeader(true); 
	    columnImporto.setAlignment(HorizontalAlignment.RIGHT);
	    columnImporto.setStyle("color:#e71d2b;");
	    columnImporto.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {	
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}  	
		});  
	    configs.add(columnImporto); 	
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreEseguite");  
	    columnOreLavoro.setHeader("Ore Eseguite");  
	    columnOreLavoro.setWidth(75);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    configs.add(columnOreLavoro); 	
	    
	      
	    SummaryColumnConfig<Double> columnImportoMese=new SummaryColumnConfig<Double>();		
	    columnImportoMese.setId("importoMese");  
	    columnImportoMese.setHeader("Euro Var.");  
	    columnImportoMese.setWidth(80);    
	    columnImportoMese.setRowHeader(true); 
	    columnImportoMese.setAlignment(HorizontalAlignment.RIGHT);
	    columnImportoMese.setStyle("color:#e71d2b;");
	    
	    columnImportoMese.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {				
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				
				return num.format(n);
			}  	
		});  
	    configs.add(columnImportoMese); 
	  	    	    
	   
	 /*   
	    SummaryColumnConfig<Double> margine=new SummaryColumnConfig<Double>();		
	    margine.setId("margine");  
	    margine.setHeader("Margine");  
	    margine.setWidth(80);    
	    margine.setRowHeader(true);  
	    margine.setAlignment(HorizontalAlignment.RIGHT);  	
	    margine.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	   configs.add(margine);	
	   */
	   
	  return configs;
	}
	
	private void loadTableRiass(List<RiepilogoSALPCLModel> result) {
		String raggruppamento=new String();
		raggruppamento=smplcmbxOrderBy.getRawValue().toString();
		raggruppamento="pm";
		try {
			store.removeAll();
			store.setStoreSorter(new StoreSorter<RiepilogoSALPCLModel>());  
		    store.setDefaultSort("numeroCommessa", SortDir.ASC);
			store.add(result);
			store.groupBy(raggruppamento);
			gridRiepilogo.reconfigure(store, cmRiepilogo);	    		    	
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}
	
	private void loadTable(List<RiepilogoSALPCLModel> result) {
		try {
			store.removeAll();
			store.setStoreSorter(new StoreSorter<RiepilogoSALPCLModel>());  
		    store.setDefaultSort("numeroCommessa", SortDir.ASC);
			store.add(result);
			store.groupBy("pm");
			gridRiepilogo.reconfigure(store, cmRiepilogo);	    		    	
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
		
	}
	
}
