package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoCostiDipSuCommesseFatturateModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogRiepilogoMarginiTotaliCommesse extends Dialog {

	private GroupingStore<RiepilogoCostiDipSuCommesseFatturateModel> store=new GroupingStore<RiepilogoCostiDipSuCommesseFatturateModel>();
	private ColumnModel cm;
	private EditorGrid<RiepilogoCostiDipSuCommesseFatturateModel> gridRiepilogo;
	
	private String data= new String();
	
	public DialogRiepilogoMarginiTotaliCommesse(String data){
	
		this.data=data;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(1000);
		setHeight(800);
		setResizable(true);
		setClosable(true);
		setButtons("");
		setScrollMode(Scroll.AUTO);
		setHeading("Riepilogo Dati Mensile Fogli Fatturazione.");
		setModal(false);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		//cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);
	
		store.groupBy("numeroCommessa");
		store.setSortField("dipendente");
		store.setSortDir(SortDir.ASC);	
		
		GroupingView summary = new GroupingView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);
				
		cm = new ColumnModel(createColumns());		
		caricaDatiTabella();
		//AggregationRowConfig<RiepilogoCostiDipSuCommesseFatturateModel> agrTotale= new AggregationRowCosti();		
		//cm.addAggregationRow(agrTotale);
		
		gridRiepilogo= new EditorGrid<RiepilogoCostiDipSuCommesseFatturateModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    
	    cpGrid.add(gridRiepilogo);
				
		add(cpGrid, new FitData(3, 3, 3, 3));
			
	}
	
	
	private void caricaDatiTabella() {
		
		AdministrationService.Util.getInstance().riepilogoTotaleMarginiSuCommesse(data, data, new AsyncCallback<List<RiepilogoCostiDipSuCommesseFatturateModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on riepilogoTotaleMarginiSuCommesse()");
			}

			@Override
			public void onSuccess(
					List<RiepilogoCostiDipSuCommesseFatturateModel> result) {
				
				if(result!=null)
					loadData(result);
				else
					Window.alert("Impossibile accedere ai dati sui margini!");
			}		
		});		
		
	}
	
	private void loadData(List<RiepilogoCostiDipSuCommesseFatturateModel> result) {
		store.removeAll();
		store.setStoreSorter(new StoreSorter<RiepilogoCostiDipSuCommesseFatturateModel>());  
		store.setSortField("dipendente");
		store.setSortDir(SortDir.ASC);	
		store.add(result);
	}
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel> renderer= new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				final NumberFormat num= NumberFormat.getFormat("#,##0.0#;-#");
				Float n=model.get(property);
				return num.format(n);
			}
		};
			
		
		GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel> percRenderer= new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				
				final NumberFormat num= NumberFormat.getPercentFormat();			
				Float n=model.get(property);
				return num.format(n);
			}
		};
		
				
		SummaryColumnConfig<Double> column;
		
		/*column = new SummaryColumnConfig<Double>();
	    column.setId("pm");
	    column.setHeader("Project Manager");
	    column.setWidth(140);
	    configs.add(column);*/
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("numeroCommessa");
	    column.setHeader("Numero Commessa");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("estensione");
	    column.setHeader("Estensione");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("attivita");
	    column.setHeader("Attivita'");
	    column.setWidth(140);
	    column.setRenderer(new GridCellRenderer<RiepilogoCostiDipSuCommesseFatturateModel>() {
			@Override
			public Object render(RiepilogoCostiDipSuCommesseFatturateModel model,
					String property, ColumnData config, int rowIndex,int colIndex, ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store,
					Grid<RiepilogoCostiDipSuCommesseFatturateModel> grid) {
				if (model != null) {
	            	String attivita=model.get(property);
	            	if((attivita.compareTo("TOTALE")==0)||(attivita.compareTo("TOTALE-PREC")==0))
	            		return "<span style='font-weight:bold; color:#139213'>" + model.get(property) + "</span>";
	            	else
	            		return "<span style='font-weight:normal; color:#000000'>" + model.get(property) + "</span>";
	            }
				return "";
			}
		});
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("dipendente");
	    column.setHeader("Dipendente");
	    column.setWidth(140);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("oreEseguite");
	    column.setHeader("Ore Eseguite");
	    column.setWidth(80);
	    //column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    //column.setRenderer(render);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("costoOrario");
	    column.setHeader("Costo Orario");
	    column.setWidth(80);
	    column.setRenderer(renderer);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("costoTotale");
	    column.setHeader("Costo Totale");
	    column.setWidth(100);
	    //column.setSummaryRenderer(summaryRenderer);
	    column.setRenderer(renderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoScaricato");
	    column.setHeader("euro/Scaricato");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	  //  column.setSummaryRenderer(summaryRenderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoFatturato");
	    column.setHeader("euro/Fatturato");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	  //  column.setSummaryRenderer(summaryRenderer);
	  //  column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);	
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("margine");
	    column.setHeader("euro/Margine");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	 //   column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("rapporto");
	    column.setHeader("Margine/Scaricate");
	    column.setWidth(60);
	    column.setRenderer(percRenderer);
	    
	 // column.setSummaryRenderer(summaryRenderer);
	   // column.setSummaryType(SummaryType.SUM);  
	    configs.add(column);
		
	    column = new SummaryColumnConfig<Double>();
	    column.setId("importoBilancio");
	    column.setHeader("Bilancio");
	    column.setWidth(100);
	    column.setRenderer(renderer); 
	    configs.add(column);
	    
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("differenzaBilancioScaricato");
	    column.setHeader("Delta");
	    column.setWidth(100);
	    column.setRenderer(renderer);
	    configs.add(column);
	    
	    column = new SummaryColumnConfig<Double>();
	    column.setId("differenzaPerc");
	    column.setHeader("Delta %");
	    column.setWidth(60);
	    column.setRenderer(percRenderer);
	    configs.add(column);
	    
		return configs;
	}
	
}
