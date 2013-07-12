package gestione.pack.client.layout;

import gestione.pack.client.model.RdoCompletaModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

public class CenterLayout_GestioneRdoCommesseAll extends LayoutContainer {

	
	public CenterLayout_GestioneRdoCommesseAll(){
	}
	
	private GroupingStore<RdoCompletaModel>store = new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeCompleto= new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeResult= new GroupingStore<RdoCompletaModel>();
	private List<RdoCompletaModel> listaStore= new ArrayList<RdoCompletaModel>();
	private Grid<RdoCompletaModel> gridRiepilogo;
	private ColumnModel cm;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
				
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-180, h-70);
		cpGrid.setPosition(8, 8);
		cpGrid.setScrollMode(Scroll.AUTO);
								
		Resizable r=new Resizable(cpGrid);
	    r.setMinWidth(w-180);
	    r.setMinHeight(h-70);
		  		
		try {
	    	cm = new ColumnModel(createColumns());	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}
		
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
		
		gridRiepilogo= new Grid<RdoCompletaModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);  
		gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
		gridRiepilogo.setView(view);
	    
		gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RdoCompletaModel>>() {  
		      public void handleEvent(SelectionChangedEvent<RdoCompletaModel> be) {  
		        	
		           
		      }     
		});
		
		cpGrid.add(gridRiepilogo);	
			
		layoutContainer.add(cpGrid, new FitData(5, 5, 5, 8));
		add(layoutContainer);
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("cliente");  
	    column.setHeader("Cliente");  
	    column.setWidth(140);  
	    column.setRowHeader(true); 
	    configs.add(column); 
		
	    column=new ColumnConfig();		
	    column.setId("pm");  
	    column.setHeader("PM");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("salIniziale");  
	    column.setHeader("SAL Iniziale");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("pclIniziale");  
	    column.setHeader("PCL Iniziale");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("salAttuale");  
	    column.setHeader("SAL Attuale");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("pclAttuale");  
	    column.setHeader("PCL Attuale");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
		column.setId("numeroRdo");  
		column.setHeader("N. Rdo");  
		column.setWidth(80);  
		column.setRowHeader(true); 
		column.setAlignment(HorizontalAlignment.RIGHT); 
		configs.add(column);
	    
		column=new ColumnConfig();		
		column.setId("numeroOfferta");  
		column.setHeader("N. Offerta");  
		column.setWidth(80);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		    
	    column=new ColumnConfig();		
	    column.setId("numeroOrdine");  
	    column.setHeader("N. Ordine");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("tariffaOraria");  
	    column.setHeader("Tariffa");  
	    column.setWidth(60);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	 	     
	    column=new ColumnConfig();		
	    column.setId("numeroOre");  
	    column.setHeader("Ore Ordine");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("numeroOreResidue");  
	    column.setHeader("Ore Res.");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    return configs;
	}
	
	
}
