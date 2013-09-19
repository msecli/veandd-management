package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
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
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoOreNonFatturabili extends LayoutContainer{

	private GroupingStore<RiepilogoOreNonFatturabiliModel>store = new GroupingStore<RiepilogoOreNonFatturabiliModel>();
	private Grid<RiepilogoOreNonFatturabiliModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private String data;
	
	private SimpleComboBox<String> smplcmbxOrderBy;
	private Button btnPrint;
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	protected Status status;
	
	public PanelRiepilogoOreNonFatturabili(String data) {	
		this.data=data;
	}

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    final FitLayout fl= new FitLayout();
		
	    status = new Status();
	    status.setBusy("Please wait...");
	    status.hide();
	    status.setAutoWidth(true);
	    
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
		smplcmbxOrderBy.add("GruppoLavoro");
		smplcmbxOrderBy.add("Sede");
		smplcmbxOrderBy.add("Attivita");
		smplcmbxOrderBy.setWidth(110);
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
				
		btnPrint = new Button();
		btnPrint.setSize("55px","25px");	   
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setToolTip("Stampa");
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setSize(26, 26);
		btnPrint.setEnabled(true);
		btnPrint.setEnabled(false);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				SessionManagementService.Util.getInstance().setNomeReport("RIEP.NON.FATT", store.getModels(),
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setNomeReport()");					
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							fp.submit();
						else
							Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
					}
				});				
			}
		});
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		ToolBar tlBar= new ToolBar();
		tlBar.add(smplcmbxOrderBy);
		tlBar.add(cp);
		tlBar.add(status);
		
		cpGrid.setTopComponent(tlBar);
						
	    store.groupBy("sede");
	    
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
	
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
			
		}
	}
	
	private void caricaTabellaRiass() {
		
		//String selezione="riassunto";				
		//Ricaricata anche in base al group by che scelgo in modo tale da poter calcolare il totale in modo adeguato	
		//String groupBy= new String();
		status.setBusy("Please wait...");
	    status.show();
		AdministrationService.Util.getInstance().getRiepilogoOreNonFatturate(data, "", new AsyncCallback<List<RiepilogoOreNonFatturabiliModel>>() {
			@Override
			public void onFailure(Throwable caught) {
				status.hide();
				Window.alert("Errore di connessione on getRiepilogoSalPcl();");			
			}

			@Override
			public void onSuccess(List<RiepilogoOreNonFatturabiliModel> result) {
				status.hide();
				btnPrint.setEnabled(true);
				loadTableRiass(result);		
			}
		 });		
	}

	
	private List<ColumnConfig> createColumnsRiassunto() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("gruppolavoro");  
	    column.setHeader("Gruppo di Lavoro");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attivita");  
	    column.setHeader("Attivita");  
	    column.setWidth(120);  
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
	    column.setHeader("Gen");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m2");  
	    column.setHeader("Feb");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m3");  
	    column.setHeader("Mar");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m4");  
	    column.setHeader("Apr");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m5");  
	    column.setHeader("Mag");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m6");  
	    column.setHeader("Giu");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m7");  
	    column.setHeader("Lug");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m8");  
	    column.setHeader("Ago");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m9");  
	    column.setHeader("Set");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {   				  				
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m10");  
	    column.setHeader("Ott");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m11");  
	    column.setHeader("Nov");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   				return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("m12");  
	    column.setHeader("Dic");  
	    column.setWidth(55);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("totOre");  
	    column.setHeader("Totale Ore");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					return number.format(value);
   			}  
      	});  
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("costoOrario");  
	    column.setHeader("Costo/h");  
	    column.setWidth(100);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("costoEffettivo");  
	    column.setHeader("Costo Tot.");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    column.setSummaryType(SummaryType.SUM);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoOreNonFatturabiliModel>() {
			@Override
			public Object render(RiepilogoOreNonFatturabiliModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreNonFatturabiliModel> store,
					Grid<RiepilogoOreNonFatturabiliModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   					GroupingStore<RiepilogoOreNonFatturabiliModel>store1 = new GroupingStore<RiepilogoOreNonFatturabiliModel>();
   				  				
   					return number.format(value);
   			}  
      	});  
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
