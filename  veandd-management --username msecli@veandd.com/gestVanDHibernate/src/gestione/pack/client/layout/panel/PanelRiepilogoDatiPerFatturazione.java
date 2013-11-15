package gestione.pack.client.layout.panel;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
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

public class PanelRiepilogoDatiPerFatturazione extends LayoutContainer{

	private GroupingStore<DatiFatturazioneMeseModel>store = new GroupingStore<DatiFatturazioneMeseModel>();
	private EditorGrid<DatiFatturazioneMeseModel> gridRiepilogo;
	private ColumnModel cm;
	
	private Button btnPrint;
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	
	List<DatiFatturazioneMeseModel> listaDati= new ArrayList<DatiFatturazioneMeseModel>();
	
	public PanelRiepilogoDatiPerFatturazione(List<DatiFatturazioneMeseModel> listaDati) {	
		for(DatiFatturazioneMeseModel d: listaDati)
			if((Float)d.get("importo")!=0)
				this.listaDati.add(d);		
	}

	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    final FitLayout fl= new FitLayout();
	    
	    try {			
			cm=new ColumnModel(createColumns());			
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
		cpGrid.setFrame(true);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setSize(1000, 750);
		
		
		btnPrint = new Button();
		btnPrint.setSize("55px","25px");	   
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setToolTip("Stampa");
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				SessionManagementService.Util.getInstance().setDataReportDatiFatturazioneInSession("RIEPDATIFATT", listaDati, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setDataInSession()");					
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
		
		Text txtGroup= new Text();
		txtGroup.setText("Group By: ");
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		ToolBar tlBar= new ToolBar();	
		tlBar.add(cp);
		cpGrid.setTopComponent(tlBar);
		
		store.groupBy("pm");
		store.add(listaDati);
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);  
			   
		gridRiepilogo= new EditorGrid<DatiFatturazioneMeseModel>(store, cm);  
		gridRiepilogo.setBorders(false); 
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setView(summary);  
		gridRiepilogo.getView().setShowDirtyCells(false);
		
		cpGrid.add(gridRiepilogo);
		layoutContainer.add(cpGrid);
	    add(layoutContainer);		
	}

	private List<ColumnConfig> createColumns() {
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
	    column.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {
				return model.get(property);
			}  	
		});
	    column.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "TOTALE";
			}
		});
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("cliente");  
		column.setHeader("Cliente");  
		column.setWidth(150);  
		column.setRowHeader(true); 
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("oggettoAttivita");  
	    column.setHeader("Oggetto");  
	    column.setWidth(140);  
	    column.setRowHeader(true); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("attivitaOrdine");  
	    column.setHeader("Attivita' ordine");  
	    column.setWidth(100);  
	    column.setRowHeader(true); 
	    configs.add(column);
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("tariffaOraria");  
	    column.setHeader("Tariffa");  
	    column.setWidth(80);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT); 
	    column.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    configs.add(column);
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreEseguite");  
	    columnOreLavoro.setHeader("Ore Eseguite");  
	    columnOreLavoro.setWidth(75);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreLavoro.setSummaryType(SummaryType.SUM);  
	    columnOreLavoro.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		}); 
	    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreFatturate=new SummaryColumnConfig<Double>();		
	    columnOreFatturate.setId("oreFatturate");  
	    columnOreFatturate.setHeader("Ore Fatturate");  
	    columnOreFatturate.setWidth(75);    
	    columnOreFatturate.setRowHeader(true);   
	    columnOreFatturate.setAlignment(HorizontalAlignment.RIGHT);  
	    columnOreFatturate.setSummaryType(SummaryType.SUM);  
	    columnOreFatturate.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});     
	    columnOreFatturate.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}
		});
	    configs.add(columnOreFatturate); 	
	      
	    SummaryColumnConfig<Double> columnImporto=new SummaryColumnConfig<Double>();		
	    columnImporto.setId("importo");  
	    columnImporto.setHeader("Importo");  
	    columnImporto.setWidth(100);    
	    columnImporto.setRowHeader(true); 
	    columnImporto.setAlignment(HorizontalAlignment.RIGHT);
	    columnImporto.setStyle("color:#e71d2b;");
	    columnImporto.setSummaryType(SummaryType.SUM);
	    columnImporto.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
			@Override
			public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
					Grid<DatiFatturazioneMeseModel> grid) {				
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}  	
		});  
	    columnImporto.setSummaryRenderer(new SummaryRenderer() {
			
			@Override
			public String render(Number value, Map<String, Number> data) {
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				//Float n=value;
				return num.format(value);
			}
		});
	    configs.add(columnImporto); 	
	    
	    SummaryColumnConfig<Double> columnImportoEffettivo=new SummaryColumnConfig<Double>();		
	     columnImportoEffettivo.setId("importoEffettivo");  
	     columnImportoEffettivo.setHeader("Importo Effettivo");  
	     columnImportoEffettivo.setWidth(100);    
	     columnImportoEffettivo.setRowHeader(true); 
	     columnImportoEffettivo.setAlignment(HorizontalAlignment.RIGHT);
	     columnImportoEffettivo.setStyle("color:#e71d2b; background-color:#d2f5af;");
	     columnImportoEffettivo.setSummaryType(SummaryType.SUM);  
	     columnImportoEffettivo.setRenderer(new GridCellRenderer<DatiFatturazioneMeseModel>() {
				@Override
				public Object render(DatiFatturazioneMeseModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneMeseModel> store,
						Grid<DatiFatturazioneMeseModel> grid) {				
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					Float n=model.get(property);
					return num.format(n);
				}  	
			});  
	     columnImportoEffettivo.setSummaryRenderer(new SummaryRenderer() {
				
				@Override
				public String render(Number value, Map<String, Number> data) {
					final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
					//Float n=value;
					return num.format(value);
				}
			});
		configs.add(columnImportoEffettivo); 
	    
	    return configs;
	}
	
}
