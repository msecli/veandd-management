package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PanelRiepilogoOreNonFatturabili extends LayoutContainer{

	private GroupingStore<RiepilogoOreNonFatturabiliModel>store = new GroupingStore<RiepilogoOreNonFatturabiliModel>();
	private Grid<RiepilogoOreNonFatturabiliModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private String data;
	
	private SimpleComboBox<String> smplcmbxOrderBy;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelRiepilogoOreNonFatturabili(String data) {	
		this.data=data;
	}

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    final FitLayout fl= new FitLayout();
		
		try {			
			cmRiepilogo=new ColumnModel(createColumnsRiassunto());
			caricaTabellaRiass();			
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
		cpGrid.setSize(w-300, h-140);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
								
		Resizable r=new Resizable(cpGrid);
	   	    		
		smplcmbxOrderBy= new SimpleComboBox<String>();
		smplcmbxOrderBy.setFieldLabel("Group By");
		smplcmbxOrderBy.setEmptyText("Selezionare..");
		smplcmbxOrderBy.setAllowBlank(false);
		smplcmbxOrderBy.add("PM");
		smplcmbxOrderBy.add("Sede");
		smplcmbxOrderBy.add("Attivita'");
		smplcmbxOrderBy.setWidth(80);
		smplcmbxOrderBy.setTriggerAction(TriggerAction.ALL);
		smplcmbxOrderBy.setSimpleValue("Sede");
		smplcmbxOrderBy.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				String raggruppamento=smplcmbxOrderBy.getRawValue().toString().toLowerCase();
				store.groupBy(raggruppamento,true);
				gridRiepilogo.reconfigure(store, cmRiepilogo);
			}		
		});
		
			
		ToolBar tlBar= new ToolBar();
		//tlBar.add(smplcmbxScelta);
		tlBar.add(smplcmbxOrderBy);
		
		cpGrid.setTopComponent(tlBar);
				
	    store.groupBy("pm");
	    
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(false);  
	    summary.setShowGroupedColumn(false);  
		      		    
	    gridRiepilogo= new EditorGrid<RiepilogoOreNonFatturabiliModel>(store, cmRiepilogo);  
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
		    
	    cpGrid.add(gridRiepilogo);
	    
	    layoutContainer.add(cpGrid);
	    
	    add(layoutContainer);
	}
	
	
	private void caricaTabellaRiass() {
		
		//String selezione="riassunto";		
		
		//Ricaricata anche in base al group by che scelgo in modo tale da poter calcolare il totale in modo adeguato
		
		String groupBy= new String();
		
		AdministrationService.Util.getInstance().getRiepilogoOreNonFatturate(data, groupBy, new AsyncCallback<List<RiepilogoOreNonFatturabiliModel>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoSalPcl();");			
			}

			@Override
			public void onSuccess(List<RiepilogoOreNonFatturabiliModel> result) {
				loadTableRiass(result);		
			}
		 });		
	}

	
	private List<ColumnConfig> createColumnsRiassunto() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(40);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attivita");  
	    column.setHeader("Attivita");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("risorsa");  
	    column.setHeader("Risorsa");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m1");  
	    column.setHeader("Gennaio");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m2");  
	    column.setHeader("Febbraio");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m3");  
	    column.setHeader("Marzo");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m4");  
	    column.setHeader("Aprile");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m5");  
	    column.setHeader("Maggio");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m6");  
	    column.setHeader("Giugno");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m7");  
	    column.setHeader("Luglio");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m8");  
	    column.setHeader("Agosto");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m9");  
	    column.setHeader("Settembre");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m10");  
	    column.setHeader("Ottobre");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m11");  
	    column.setHeader("Novembre");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m12");  
	    column.setHeader("Dicembre");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    	    
		return configs;
	}

		
	private void loadTableRiass(List<RiepilogoOreNonFatturabiliModel> result) {
		/*String raggruppamento=new String();
		raggruppamento=smplcmbxOrderBy.getRawValue().toString().toLowerCase();*/
			
		try {
			store.removeAll();
			
			store.add(result);
			//store.groupBy(raggruppamento);
			gridRiepilogo.reconfigure(store, cmRiepilogo);	    		    	
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}
	
}
