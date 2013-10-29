package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.i18n.client.NumberFormat;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoMeseFoglioOre extends LayoutContainer{	
	
	private GroupingStore<RiepilogoFoglioOreModel>store = new GroupingStore<RiepilogoFoglioOreModel>();
	private Grid<RiepilogoFoglioOreModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	private String username= new String();
	private Date data;
	//private Button btnPrint= new Button();
	private Button btnRiepilogoCommesse= new Button();
	private Button btnLegenda= new Button();
	
	public PanelRiepilogoMeseFoglioOre(String user, Date dataRiferimento, String tipoLavoratore){
		username=user;
		data=dataRiferimento;
		//tLavoratore=tipoLavoratore;
	}
			
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	  		
	    LayoutContainer bodyContainer = new LayoutContainer();
	    bodyContainer.setLayout(new FlowLayout());
	  	bodyContainer.setBorders(false);
	    
	  	ButtonBar btnBar= new ButtonBar();
	  	btnBar.setStyleAttribute("margin-bottom", "5px");
	  		  			
	  	btnLegenda.setSize(26, 26);
	  	btnLegenda.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.legenda()));
	  	btnLegenda.setIconAlign(IconAlign.TOP);
	  	btnLegenda.setToolTip("Legenda colori:\n" +
	  			"VERDE: compilazione corretta;\n" +
	  			"ROSSO: Non compilato/Festivo;\n" +
	  			"GRIGIO: Puo' indicare la mancata compilazione degli intervalli commesse o un giorno completamente coperto da giustificativi.");
	  		  	
		btnRiepilogoCommesse.setEnabled(false);
		btnRiepilogoCommesse.setSize(26, 26);
		btnRiepilogoCommesse.setIconAlign(IconAlign.TOP);
		btnRiepilogoCommesse.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep_comm()));
		btnRiepilogoCommesse.setToolTip("Riepilogo Commesse");
		btnRiepilogoCommesse.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Dialog d= new Dialog();
				d.setHeaderVisible(true);
				d.setHeading("Riepilogo dettagliato (Commesse).");
				d.setSize(545, 795);
				d.add(new PanelRiepilogoGiornalieroCommesse(username, data));
				d.setButtons("");
				d.show();			
			}
		});
			
		btnBar.add(btnRiepilogoCommesse);
		btnBar.add(btnLegenda);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(1100);
		cntpnlGrid.setHeight(385);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
						
		caricaTabellaDati();
	    
	    try {
	    	
	    	cmCommessa = new ColumnModel(createColumns());
	    	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    store.groupBy("mese");
	    store.setSortField("giorno");
	    
	    GroupSummaryView summary = new GroupSummaryView();  
	    summary.setForceFit(true);  
	    summary.setShowGroupedColumn(false);
	    summary.setStartCollapsed(false);
		    
	    gridRiepilogo= new EditorGrid<RiepilogoFoglioOreModel>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
	 	 /*   
	    gridRiepilogo.getView().setViewConfig(new GridViewConfig(){
	    	@Override
	        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
	    		if (model != null) {	    
	            	String stato= new String();
	            	stato= model.get("compilato");
	                if (stato.compareTo("1")==0) 
	                    return "red-row";               
	                else if (model.get("compilato").toString().compareTo("2")==0) 
	                    return "grey-row";
	                else
	                	return "green-row";
	            }
				return "";            
	    	}    	
	    });*/
	    
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(btnBar);
	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);
		cntpnlLayout.setHeading("Riepilogo Giornaliero.");
		cntpnlLayout.setSize(1120, 415);
		cntpnlLayout.setScrollMode(Scroll.NONE);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.add(cntpnlGrid);
	    
		bodyContainer.add(cntpnlLayout);    
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);    
	}


	private void caricaTabellaDati() {
		AdministrationService.Util.getInstance().getRiepilogoMeseFoglioOre(username, data,  new AsyncCallback<List<RiepilogoFoglioOreModel>>() {	
			@Override
			public void onSuccess(List<RiepilogoFoglioOreModel> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						//Window.alert("Nessun dato rilevato in base ai criteri di ricerca selezionati.");				
					}
					else loadTable(result);			
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  		
	}
	
	
	private void loadTable(List<RiepilogoFoglioOreModel> result) {
		try {
			btnRiepilogoCommesse.setEnabled(true);
			store.removeAll();
			store.add(result);
			gridRiepilogo.reconfigure(store, cmCommessa);				
		} catch (NullPointerException e) {
			Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
				e.printStackTrace();
		}
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		GridCellRenderer<RiepilogoFoglioOreModel> renderer = new GridCellRenderer<RiepilogoFoglioOreModel>() {
            public String render(RiepilogoFoglioOreModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoFoglioOreModel> store, Grid<RiepilogoFoglioOreModel> grid) {
                
            	if (model != null) {	    
	            	String stato= new String();
	            	stato= model.get("compilato");
	                if (stato.compareTo("1")==0) 
	                    return "<span style='font-weight:bold; color:#f10000'>" + model.get(property) + "</span>";              
	                else if (model.get("compilato").toString().compareTo("2")==0) 
	                	return "<span style='font-weight:bold; color:grey'>" + model.get(property) + "</span>";
	                else
	                	return "<span style='font-weight:bold; color:#139213'>" + model.get(property) + "</span>";         
	                
	            }
				return ""; 
        }};
           
        GridCellRenderer<RiepilogoFoglioOreModel> rendererNumber = new GridCellRenderer<RiepilogoFoglioOreModel>() {
            public String render(RiepilogoFoglioOreModel model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<RiepilogoFoglioOreModel> store, Grid<RiepilogoFoglioOreModel> grid) {
            	Float n=model.get(property);
				
            	if (model != null) {	    
	            	String stato= new String();
	            	stato= model.get("compilato");
	                if (stato.compareTo("1")==0) 
	                    return "<span style='font-weight:bold; color:#f10000'>" + number.format(n) + "</span>";              
	                else if (model.get("compilato").toString().compareTo("2")==0) 
	                	return "<span style='font-weight:bold; color:grey'>" + number.format(n) + "</span>";
	                else
	                	return "<span style='font-weight:bold; color:#139213'>" + number.format(n) + "</span>";         
	                
	            }else
	            	
            	return "";
        }};      		
				
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(65);  
	    column.setRowHeader(true);  
	    column.setRenderer(renderer);
	    column.setSummaryRenderer(new SummaryRenderer() {
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Totale Ore:";
	   			}     			
		});  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("letteraGiorno");  
	    column.setHeader("");  
	    column.setWidth(15);  
	    column.setRowHeader(true);  
	    column.setRenderer(renderer);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    SummaryColumnConfig<Double> columnOreTimbrature=new SummaryColumnConfig<Double>();		
	    columnOreTimbrature.setId("oreTimbrature");  
	    columnOreTimbrature.setHeader("Timbrature");  
	    columnOreTimbrature.setWidth(50);    
	    columnOreTimbrature.setRowHeader(true); 
	    columnOreTimbrature.setSummaryType(SummaryType.SUM);  
	    columnOreTimbrature.setAlignment(HorizontalAlignment.RIGHT);  
	    columnOreTimbrature.setRenderer(rendererNumber);	   
	    columnOreTimbrature.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					
	   					//CALCOLO IL TOTALE
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreTimbrature()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreTimbrature);
	    	    
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Viaggio");  
	    columnOreViaggio.setWidth(50);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setSummaryType(SummaryType.SUM);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);
	    columnOreViaggio.setRenderer(rendererNumber);
	    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreViaggio()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreViaggio);
	    
	    
	    SummaryColumnConfig<Double> columnDeltaViaggio=new SummaryColumnConfig<Double>();		
	    columnDeltaViaggio.setId("deltaViaggio");  
	    columnDeltaViaggio.setHeader("D.Viaggio");  
	    columnDeltaViaggio.setWidth(50);    
	    columnDeltaViaggio.setRowHeader(true); 
	    columnDeltaViaggio.setSummaryType(SummaryType.SUM);  
	    columnDeltaViaggio.setAlignment(HorizontalAlignment.RIGHT); 
	    columnDeltaViaggio.setRenderer(rendererNumber);
	    columnDeltaViaggio.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getDeltaViaggio()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      }); 
	    configs.add(columnDeltaViaggio); 		    
	  
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("oreTotali");  
	    columnOreTotali.setHeader("Totali");  
	    columnOreTotali.setWidth(50);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setSummaryType(SummaryType.SUM);  
	    columnOreTotali.setAlignment(HorizontalAlignment.RIGHT); 
	    columnOreTotali.setRenderer(rendererNumber);
	    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreTotali()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });
	    configs.add(columnOreTotali); 	
	    
	    SummaryColumnConfig<Double> columnOreFerie=new SummaryColumnConfig<Double>();		
	    columnOreFerie.setId("oreFerie");  
	    columnOreFerie.setHeader("Ferie");  
	    columnOreFerie.setWidth(50);    
	    columnOreFerie.setRowHeader(true); 
	    columnOreFerie.setSummaryType(SummaryType.SUM);  
	    columnOreFerie.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreFerie.setRenderer(rendererNumber);
	    columnOreFerie.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreFerie()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });
	    configs.add(columnOreFerie);	    
	    
	    SummaryColumnConfig<Double> columnOrePermesso=new SummaryColumnConfig<Double>();		
	    columnOrePermesso.setId("orePermesso");  
	    columnOrePermesso.setHeader("Perm.(ROL)");  
	    columnOrePermesso.setWidth(50);    
	    columnOrePermesso.setRowHeader(true); 
	    columnOrePermesso.setSummaryType(SummaryType.SUM);  
	    columnOrePermesso.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOrePermesso.setRenderer(rendererNumber);
	    columnOrePermesso.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOrePermesso()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });      
	    configs.add(columnOrePermesso);    
	    
	    SummaryColumnConfig<Double> columnOreStraordinario=new SummaryColumnConfig<Double>();		
	    columnOreStraordinario.setId("oreStraordinario");  
	    columnOreStraordinario.setHeader("Strao.");  
	    columnOreStraordinario.setWidth(50);    
	    columnOreStraordinario.setRowHeader(true); 
	    columnOreStraordinario.setSummaryType(SummaryType.SUM);  
	    columnOreStraordinario.setAlignment(HorizontalAlignment.RIGHT);  
	    columnOreStraordinario.setRenderer(rendererNumber);
	    columnOreStraordinario.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreStraordinario()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });      
	    configs.add(columnOreStraordinario);	    
	    
	    SummaryColumnConfig<Double> columnOreRecupero=new SummaryColumnConfig<Double>();		
	    columnOreRecupero.setId("oreRecupero");  
	    columnOreRecupero.setHeader("Recupero");  
	    columnOreRecupero.setWidth(50);    
	    columnOreRecupero.setRowHeader(true); 
	    columnOreRecupero.setSummaryType(SummaryType.SUM);  
	    columnOreRecupero.setAlignment(HorizontalAlignment.RIGHT);
	    columnOreRecupero.setRenderer(rendererNumber);
	    columnOreRecupero.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreRecupero()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });      
	    configs.add(columnOreRecupero); 
	    
	    SummaryColumnConfig<Double> columnOreAbbuono=new SummaryColumnConfig<Double>();		
	    columnOreAbbuono.setId("oreAbbuono");  
	    columnOreAbbuono.setHeader("Abbuono");  
	    columnOreAbbuono.setWidth(50);    
	    columnOreAbbuono.setRowHeader(true); 
	    columnOreAbbuono.setSummaryType(SummaryType.SUM);  
	    columnOreAbbuono.setAlignment(HorizontalAlignment.RIGHT); 
	    columnOreAbbuono.setRenderer(rendererNumber);
	    columnOreAbbuono.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreAbbuono()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
			}  
	    });      
	    configs.add(columnOreAbbuono); 
	        
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("giustificativo");  
	    column.setHeader("Giustificativo");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    column.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {

			@Override
			public Object render(RiepilogoFoglioOreModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<RiepilogoFoglioOreModel> store, Grid<RiepilogoFoglioOreModel> grid) {
				
				String g= model.get(property);
				if(g.length()==2)
					return "";
				else
					if (model != null) {	    
		            	String stato= new String();
		            	stato= model.get("compilato");
		            	String giustificativo=model.get(property);
		            	//elimino dalla stringa giustificativo, l'ultima lettera che rappresenta il numero di giustificativi registrati
		                if (stato.compareTo("1")==0) 
		                    return "<span style='font-weight:bold; color:#f10000'>" + giustificativo + "</span>";              
		                else if (model.get("compilato").toString().compareTo("2")==0) 
		                	return "<span style='font-weight:bold; color:grey'>" + giustificativo + "</span>";
		                else
		                	return "<span style='font-weight:bold; color:#139213'>" + giustificativo + "</span>";         
		                
		            }else
		            	return "";		
			}
		});
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("note");  
	    column.setHeader("noteAggiuntive");  
	    column.setWidth(200);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    column.setRenderer(renderer);
	    configs.add(column); 
	    	    
		return configs;
	}

}
