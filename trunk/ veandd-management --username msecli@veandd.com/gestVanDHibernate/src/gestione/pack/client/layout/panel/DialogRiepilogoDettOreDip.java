package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.RiepilogoOreDipCommesse;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogRiepilogoDettOreDip extends Dialog{

	private GroupingStore<RiepilogoOreDipCommesse>store = new GroupingStore<RiepilogoOreDipCommesse>();
	private EditorGrid<RiepilogoOreDipCommesse> gridRiepilogo;
	private ColumnModel cm;
	
	private String dataRif;
	private String pmRif;
	
	public DialogRiepilogoDettOreDip(String data, String pm){
		
		dataRif=data;
		pmRif=pm;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(520);
		setHeight(830);
		setResizable(true);
		setClosable(true);
		setButtons("");
		setScrollMode(Scroll.AUTO);
		setHeading("Riepilogo Dati Mensile Fogli Fatturazione.");
		setModal(false);
		
		caricaTabellaDati(dataRif, pmRif);
		
	    try {
	    	cm = new ColumnModel(createColumns());	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	

		store.groupBy("numeroCommessa");
		    
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(true);  
	    summary.setShowGroupedColumn(false);  
		    
	    gridRiepilogo= new EditorGrid<RiepilogoOreDipCommesse>(store, cm);  
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);	 	    
	    
	    ContentPanel cntpnlGrid= new ContentPanel();
	    cntpnlGrid.setBodyBorder(false);  
	    cntpnlGrid.setBorders(false);
	    cntpnlGrid.setFrame(true);
	    cntpnlGrid.setLayout(new FitLayout());  
	    cntpnlGrid.setHeaderVisible(false);
	    cntpnlGrid.setWidth(480);
	    cntpnlGrid.setHeight(800);
	    cntpnlGrid.setScrollMode(Scroll.AUTOY);
	    cntpnlGrid.add(gridRiepilogo);
	   	    
	    VerticalPanel vp= new VerticalPanel();
	    vp.setBorders(false);
	  	vp.add(cntpnlGrid);
	  	add(vp);		
	}

	private void caricaTabellaDati(String data, String pm) {
		
		AdministrationService.Util.getInstance().getRiepilogoOreDipCommesse(data, pm, new AsyncCallback<List<RiepilogoOreDipCommesse>>() {		
			@Override
			public void onSuccess(List<RiepilogoOreDipCommesse> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0)
						Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
					loadTable(result);			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipCommesse();");
				caught.printStackTrace();		
			}
		});		
	}
	
	private void loadTable(List<RiepilogoOreDipCommesse> result) {
		try {
			store.removeAll();
			store.add(result);
			store.groupBy("numeroCommessa");
			gridRiepilogo.reconfigure(store, cm);	    		    	
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}		
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("dipendente");  
		column.setHeader("Dipendente");  
		column.setWidth(120);  
		column.setRowHeader(true); 
		
	    configs.add(column); 
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(80);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
			@Override
			public Object render(RiepilogoOreDipCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesse> store,
					Grid<RiepilogoOreDipCommesse> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	   
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(80);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
			@Override
			public Object render(RiepilogoOreDipCommesse model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<RiepilogoOreDipCommesse> store,
					Grid<RiepilogoOreDipCommesse> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    
	    configs.add(columnOreViaggio); 	
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("totOre");  
	    columnOreTotali.setHeader("Totale");  
	    columnOreTotali.setWidth(85);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);   
	    columnOreTotali.setStyle("color:#e71d2b;");
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipCommesse>() {
			@Override
			public Object render(RiepilogoOreDipCommesse model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipCommesse> store,
					Grid<RiepilogoOreDipCommesse> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
	  
	    configs.add(columnOreTotali); 
		
		return configs;
	}
	
	
	
	
	
}
