package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.utility.ClientUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogRiepilogoDatiFoglioFatturazione extends Dialog {

	private GroupingStore<DatiFatturazioneCommessaModel>store = new GroupingStore<DatiFatturazioneCommessaModel>();
	private EditorGrid<DatiFatturazioneCommessaModel> gridRiepilogo;
	private ColumnModel cm;	
	
	private String commessaSelected= new String();
	
	public DialogRiepilogoDatiFoglioFatturazione(String commessa){
	
		commessaSelected=commessa;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(1000);
		setHeight(560);
		setResizable(true);
		setClosable(true);
		setButtons("");
		setScrollMode(Scroll.AUTO);
		setHeading("Riepilogo Dati Mensile Fogli Fatturazione.");
		setModal(false);
					
		try {
		    	cm = new ColumnModel(createColumnsRiepilogoTotale());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
		}
		
		store.groupBy("commessa");
		store.setSortField("numeroMese");
		//store.setSortDir(SortDir.ASC);
			    
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(false);  
		summary.setShowGroupedColumn(false);  
		
		gridRiepilogo= new EditorGrid<DatiFatturazioneCommessaModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setView(summary);  
		gridRiepilogo.getView().setShowDirtyCells(false);	    
		    
		caricaTabellaDatiFatturazioneCommesse();
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);  
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(950);
		cntpnlGrid.setHeight(510);
		cntpnlGrid.setScrollMode(Scroll.AUTOY);
		cntpnlGrid.add(gridRiepilogo);
		   	    
		VerticalPanel vp= new VerticalPanel();
		vp.setBorders(false);
		vp.add(cntpnlGrid);
		  	
		add(vp);
	}
	

	private List<ColumnConfig> createColumnsRiepilogoTotale() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number= NumberFormat.getFormat("0.00");
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	  /*  column=new SummaryColumnConfig<Double>();		
	    column.setId("estensione");  
		column.setHeader("Estens.");  
		column.setWidth(60);  
		column.setRowHeader(true);
		configs.add(column);*/
				
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("mese");  
		column.setHeader("Mese");  
		column.setWidth(60);  
		column.setRowHeader(true);
		column.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
		public String render(Number value, Map<String, Number> data) {
   				return "Totale:";
		}  
      });  
	    configs.add(column);
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreEseguite");  
	    columnOreLavoro.setHeader("Ore Eseguite");  
	    columnOreLavoro.setWidth(100);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setSummaryType(SummaryType.SUM);  
	    columnOreLavoro.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreLavoro.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   			/*	GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreEseguite()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);*/
					return number.format(value);
			}  
	      });  
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreFatturate=new SummaryColumnConfig<Double>();		
	    columnOreFatturate.setId("oreFatturate");  
	    columnOreFatturate.setHeader("Ore da Fatturare");  
	    columnOreFatturate.setWidth(100);    
	    columnOreFatturate.setRowHeader(true); 
	    columnOreFatturate.setSummaryType(SummaryType.SUM);  
	    columnOreFatturate.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreFatturate.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    columnOreFatturate.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				/*GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreFatturate()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);*/
					return number.format(value);
			}  
	      });      
	    configs.add(columnOreFatturate);
	    
	    SummaryColumnConfig<Double> columnImportoFatturare=new SummaryColumnConfig<Double>();		
	    columnImportoFatturare.setId("importoFatturare");  
	    columnImportoFatturare.setHeader("Importo da Fatturare");  
	    columnImportoFatturare.setWidth(140);    
	    columnImportoFatturare.setRowHeader(true); 
	    columnImportoFatturare.setSummaryType(SummaryType.SUM);  
	    columnImportoFatturare.setAlignment(HorizontalAlignment.RIGHT);    
	    columnImportoFatturare.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    columnImportoFatturare.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   			/*	GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format((Float)riep.get("importoFatturare")));
	   				}
	   				
	   				Float n=Float.valueOf(tot);*/
					return number.format(value);
			}  
	      });      
	    configs.add(columnImportoFatturare); 		
	      
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroOrdine");  
		column.setHeader("Ordine.");  
		column.setWidth(70);  
		column.setRowHeader(true);
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column);	    	    	    
	    
	    SummaryColumnConfig<Double> variazioneSal=new SummaryColumnConfig<Double>();		
	    variazioneSal.setId("variazioneSal");  
	    variazioneSal.setHeader("Variazione SAL");  
	    variazioneSal.setWidth(100);    
	    variazioneSal.setRowHeader(true); 
	    variazioneSal.setSummaryType(SummaryType.SUM);  
	    variazioneSal.setAlignment(HorizontalAlignment.RIGHT);  	
	    variazioneSal.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    variazioneSal.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				/*GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getVariazioneSal()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);*/
					return number.format(value);
			}  
	      });  
	    configs.add(variazioneSal); 	    
	    
	    SummaryColumnConfig<Double> variazionePcl=new SummaryColumnConfig<Double>();		
	    variazionePcl.setId("variazionePcl");  
	    variazionePcl.setHeader("Variazione PCL");  
	    variazionePcl.setWidth(100);    
	    variazionePcl.setRowHeader(true); 
	    variazionePcl.setSummaryType(SummaryType.SUM);  
	    variazionePcl.setAlignment(HorizontalAlignment.RIGHT);  	
	    variazionePcl.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    variazionePcl.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				/*GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getVariazionePcl()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);*/
					return number.format(value);
			}  
	      });  
	    configs.add(variazionePcl);     
	    
	    SummaryColumnConfig<Double> margine=new SummaryColumnConfig<Double>();		
	    margine.setId("margine");  
	    margine.setHeader("Margine");  
	    margine.setWidth(100);    
	    margine.setRowHeader(true); 
	    margine.setSummaryType(SummaryType.SUM);  
	    margine.setAlignment(HorizontalAlignment.RIGHT);  	
	    margine.setRenderer(new GridCellRenderer<DatiFatturazioneCommessaModel>() {
			@Override
			public Object render(DatiFatturazioneCommessaModel model,	String property, ColumnData config, int rowIndex, int colIndex, ListStore<DatiFatturazioneCommessaModel> store,
					Grid<DatiFatturazioneCommessaModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}  	
		});
	    margine.setSummaryRenderer(new SummaryRenderer() {		
			@Override
			public String render(Number value, Map<String, Number> data) {				
				GroupingStore<DatiFatturazioneCommessaModel>store1 = new GroupingStore<DatiFatturazioneCommessaModel>();
   				String tot="0.00";
   				store1.add(store.getModels());
   				for(DatiFatturazioneCommessaModel riep: store1.getModels()){
   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getMargine()));
   				}
   				
   				Float n=Float.valueOf(tot);
				return number.format(n);
			}
		});
	   configs.add(margine);
		
		return configs;
	}
	
	
	private void caricaTabellaDatiFatturazioneCommesse() {
		AdministrationService.Util.getInstance().getRiepilogoDatiFatturazioneCommessa(commessaSelected, new AsyncCallback<List<DatiFatturazioneCommessaModel>>() {
			
			@Override
			public void onSuccess(List<DatiFatturazioneCommessaModel> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati di fatturazione");
				else	
					if(result.size()==0)
						Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");
					loadTable(result);			
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoDatiFatturazioneCommessa();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	   
	}
	
	private void loadTable(List<DatiFatturazioneCommessaModel> result) {
		try {
			store.removeAll();
			store.add(result);
			//store.groupBy("numeroCommessa");
			gridRiepilogo.reconfigure(store, cm);
	    		    	
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}			
	}
	
}
