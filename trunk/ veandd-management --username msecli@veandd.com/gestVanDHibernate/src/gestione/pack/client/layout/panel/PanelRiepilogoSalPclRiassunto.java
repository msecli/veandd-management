package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.AggregationRenderer;
import com.extjs.gxt.ui.client.widget.grid.AggregationRowConfig;
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

public class PanelRiepilogoSalPclRiassunto  extends LayoutContainer{

	private GroupingStore<RiepilogoSALPCLModel>store = new GroupingStore<RiepilogoSALPCLModel>();
	private Grid<RiepilogoSALPCLModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	
	private String tabSelected;
	private String data;
	
	private SimpleComboBox<String> smplcmbxScelta;
	private SimpleComboBox<String> smplcmbxOrderBy;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private Button btnPrint;
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	public PanelRiepilogoSalPclRiassunto(String tabSelected, String data){
		this.tabSelected=tabSelected;
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
								
		//Resizable r=new Resizable(cpGrid);
	   	    		
		smplcmbxScelta=new SimpleComboBox<String>();
		smplcmbxScelta.setFieldLabel("SAL/PCL");
		smplcmbxScelta.setEmptyText("Selezionare..");
		smplcmbxScelta.setAllowBlank(false);
		smplcmbxScelta.add("SAL");
		smplcmbxScelta.add("PCL");
		smplcmbxScelta.setWidth(80);
		smplcmbxScelta.setTriggerAction(TriggerAction.ALL);
		smplcmbxScelta.setSimpleValue("SAL");
		
		smplcmbxOrderBy= new SimpleComboBox<String>();
		smplcmbxOrderBy.setFieldLabel("Group By");
		smplcmbxOrderBy.setEmptyText("Selezionare..");
		smplcmbxOrderBy.setAllowBlank(false);
		smplcmbxOrderBy.add("PM");
		smplcmbxOrderBy.add("Cliente");
		smplcmbxOrderBy.setWidth(80);
		smplcmbxOrderBy.setTriggerAction(TriggerAction.ALL);
		smplcmbxOrderBy.setSimpleValue("PM");
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
	
		btnPrint= new Button();
		btnPrint.setEnabled(true);
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setToolTip("Stampa");
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManagementService.Util.getInstance().setDatiReportSalPclRiassunto("RIEP.SALPCLRIASS", store.getModels(),
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
		//fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		tlBar.add(cp);	
		
		AggregationRowConfig<RiepilogoSALPCLModel> agrTotale= new AggregationRowPersonale();		
		cmRiepilogo.addAggregationRow(agrTotale);
		cpGrid.setTopComponent(tlBar);
				
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
				
		AdministrationService.Util.getInstance().getRiepilogoSalPclRiassunto(data, "", new AsyncCallback<List<RiepilogoSALPCLModel>>() {
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
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("variazione");  //usata come appoggio la variabile indicata
		column.setHeader("Ore SAL");  
		column.setWidth(100);  
		column.setRowHeader(true); 
		column.setAlignment(HorizontalAlignment.RIGHT);
		//column.setStyle("color:#e71d2b;");
		column.setSummaryType(SummaryType.SUM);  
		column.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
				Grid<RiepilogoSALPCLModel> grid) {	
					final NumberFormat num= NumberFormat.getFormat("0.00");
					Float n=model.get(property);
					return num.format(n);
				}  	
		});  
		column.setSummaryRenderer(new SummaryRenderer() {  
	   		@Override
	   		public String render(Number value, Map<String, Number> data) {
	   				final NumberFormat number= NumberFormat.getFormat("0.00"); 				
	   				
					return number.format(value);
	   			}  
	        });  
		configs.add(column);    	    
	     
	    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
	    columnImporto.setId("importoComplessivo");  //usata come appoggio la variabile indicata
	    columnImporto.setHeader("Tot.Euro Sal");  
	    columnImporto.setWidth(100);    
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
   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				return num.format(value);
   			}  
        });  
	    configs.add(columnImporto);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attuale");  //usata come appoggio la variabile indicata
		column.setHeader("Ore PCL");  
		column.setWidth(100);  
		column.setRowHeader(true); 
		column.setAlignment(HorizontalAlignment.RIGHT);
		//column.setStyle("color:#e71d2b;");
		column.setSummaryType(SummaryType.SUM);  
		column.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
				Grid<RiepilogoSALPCLModel> grid) {	
					final NumberFormat num= NumberFormat.getFormat("0.00");
					Float n=model.get(property);
					return num.format(n);
				}  	
		});  
		column.setSummaryRenderer(new SummaryRenderer() {  
	   		@Override
	   		public String render(Number value, Map<String, Number> data) {
	   				final NumberFormat number= NumberFormat.getFormat("0.00"); 				
	   				
					return number.format(value);
	   			}  
	        });  
		configs.add(column);		    
	    
	    SummaryColumnConfig<Double> columnImportoPcl=new SummaryColumnConfig<Double>();		
	    columnImportoPcl.setId("oreEseguite");  //usata come appoggio la variabile indicata
	    columnImportoPcl.setHeader("Tot.Euro Pcl");  
	    columnImportoPcl.setWidth(100);    
	    columnImportoPcl.setRowHeader(true); 
	    columnImportoPcl.setAlignment(HorizontalAlignment.RIGHT);
	    columnImportoPcl.setStyle("color:#e71d2b;");
	    columnImportoPcl.setSummaryType(SummaryType.SUM);  
	    columnImportoPcl.setRenderer(new GridCellRenderer<RiepilogoSALPCLModel>() {
			@Override
			public Object render(RiepilogoSALPCLModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoSALPCLModel> store,
					Grid<RiepilogoSALPCLModel> grid) {	
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}  	
		});  
	    columnImportoPcl.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
   			public String render(Number value, Map<String, Number> data) {
   				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");			
				return num.format(value);
   			}  
      });  
	    configs.add(columnImportoPcl);	    
		return configs;
	}

	
		
	private void loadTableRiass(List<RiepilogoSALPCLModel> result) {
		String raggruppamento=new String();
		raggruppamento=smplcmbxOrderBy.getRawValue().toString().toLowerCase();
			
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
		
	
	private class AggregationRowPersonale extends AggregationRowConfig<RiepilogoSALPCLModel>{
		
		public AggregationRowPersonale(){
			final NumberFormat number= NumberFormat.getFormat("#,##0.0#;-#");
			AggregationRenderer<RiepilogoSALPCLModel> aggrRender= new AggregationRenderer<RiepilogoSALPCLModel>() {			
				@Override
				public Object render(Number value, int colIndex, Grid<RiepilogoSALPCLModel> grid, ListStore<RiepilogoSALPCLModel> store) {
					 if(value!=null)		    		  
			    		  return number.format(value.doubleValue());
			    	  else
			    		  return number.format((float) 0) ;
				}
			};			
						
			setHtml("estensione", "<p style=\"font-size:15px; color:#000000; font-weight:bold;\">TOTALE</p>");	
			setSummaryType("variazione", SummaryType.SUM);
			setRenderer("variazione", aggrRender);
			
			setSummaryType("importoComplessivo", SummaryType.SUM);  
			setRenderer("importoComplessivo", aggrRender);
			
			setSummaryType("attuale", SummaryType.SUM);
			//setSummaryFormat("importoMese", NumberFormat.getCurrencyFormat("EUR"));
			setRenderer("attuale", aggrRender);
			
			setSummaryType("oreEseguite", SummaryType.SUM);
			setRenderer("oreEseguite", aggrRender);
			setCellStyle("oreEseguite", "font-size:15px; color:#000000; font-weight:bold;");
		}
	}
	
}
