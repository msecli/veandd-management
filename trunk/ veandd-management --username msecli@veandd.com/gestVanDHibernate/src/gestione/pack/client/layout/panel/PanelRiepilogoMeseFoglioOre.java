package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.js.JsonConverter;
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
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
	private String tLavoratore= new String();
	private Date data;
	//private Button btnPrint= new Button();
	private Button btnRiepilogoCommesse= new Button();
	private Button btnStampaRiepilogo= new Button();
	
	public PanelRiepilogoMeseFoglioOre(String user, Date dataRiferimento, String tipoLavoratore){
		username=user;
		data=dataRiferimento;
		tLavoratore=tipoLavoratore;
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
	  	
	  	/* 	
	  	btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print()));
		btnPrint.setToolTip("Stampa");
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				final String url="/gestvandhibernate/PrintDataServlet";
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

			    try {
			    	
			    	List<RiepilogoFoglioOreModel> list= new ArrayList<RiepilogoFoglioOreModel>();
			    	list.addAll(store.getModels());
			    	Map<String, Object> map = new HashMap<String, Object>();
			    	
			    	for (Object i : list){
			    		
			    		map.put(String.valueOf(list.indexOf(i)), (RiepilogoFoglioOreModel) i);
			    	}
			    	
			        String obj= JsonConverter.encode(map).toString();
			    	
			        Request request = builder.sendRequest(obj, new RequestCallback() {
			        public void onError(Request request, Throwable exception) {
			         // displayError("Couldn't retrieve JSON");
			        	
			        }

			        public void onResponseReceived(Request request, Response response) {
			          if (200 == response.getStatusCode()) {
			           // updateTable(asArrayOfStockData(response.getText()));
			          } else {
			            //displayError("Couldn't retrieve JSON (" + response.getStatusText()  + ")");
			          }
			        }
			      });
			    } catch (RequestException e) {
			    	 Window.alert("Failed to send the request: " + e.getMessage());
			    }
			}
		});*/
		
	  	btnStampaRiepilogo.setSize(26, 26);
	  	btnStampaRiepilogo.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print()));
	  	btnStampaRiepilogo.setToolTip("Stampa Riepilogo.");
	  	btnStampaRiepilogo.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				// TODO Auto-generated method stub
				
			}
		});
	  	
		btnRiepilogoCommesse.setEnabled(false);
		btnRiepilogoCommesse.setSize(26, 26);
		btnRiepilogoCommesse.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.riep_comm()));
		btnRiepilogoCommesse.setToolTip("Riepilogo Commesse");
		btnRiepilogoCommesse.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Dialog d= new Dialog();
				d.setHeaderVisible(true);
				d.setHeading("Riepilogo dettagliato (Commesse).");
				d.setSize(550, 605);
				d.add(new PanelRiepilogoGiornalieroCommesse(username, data));
				d.setButtons("");
				d.show();			
			}
		});
			
		btnBar.add(btnRiepilogoCommesse);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(975);
		cntpnlGrid.setHeight(290);
		cntpnlGrid.setScrollMode(Scroll.AUTOY);
				
		caricaTabellaDati();
	    
	    try {
	    	
	    	if(tLavoratore.compareTo("C")!=0)
	    		cmCommessa = new ColumnModel(createColumns());
	    	else
	    		cmCommessa = new ColumnModel(createColumnsCollaboratori());
	    	
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
	   
	    gridRiepilogo.getView().setViewConfig(new GridViewConfig(){
	    	@Override
	        public String getRowStyle(ModelData model, int rowIndex, ListStore<ModelData> ds) {
	            if (model != null) {
	                                    //TODO: put your conditions here
	                if (!(Boolean)model.get("compilato")) 
	                    return "red-row";
	                
	            }
				return "";            
	    	}
	    	
	    	
	    });
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(btnBar);
	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);
		cntpnlLayout.setHeading("Riepilogo Giornaliero.");
		cntpnlLayout.setSize(990, 300);
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

	private List<ColumnConfig> createColumnsCollaboratori() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setSummaryRenderer(new SummaryRenderer() {
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Totale Ore:";
	   			}     			
		});  
	    
	    configs.add(column); 
	    
	    SummaryColumnConfig<Double> columnOreTimbrature=new SummaryColumnConfig<Double>();		
	    columnOreTimbrature.setId("oreTimbrature");  
	    columnOreTimbrature.setHeader("Ore Lavoro");  
	    columnOreTimbrature.setWidth(100);    
	    columnOreTimbrature.setRowHeader(true); 
	    columnOreTimbrature.setSummaryType(SummaryType.SUM);  
	    columnOreTimbrature.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreTimbrature.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
			  	
		});
	    columnOreTimbrature.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreTimbrature()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreTimbrature);
	    	    
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(100);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setSummaryType(SummaryType.SUM);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				
				
				Float n=model.get(property);
				return number.format(n);
			}			  	
		});
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
	    
	            
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("giustificativo");  
	    column.setHeader("Giustificativo");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("note");  
	    column.setHeader("noteAggiuntive");  
	    column.setWidth(250);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    configs.add(column); 
	    	    
		return configs;
	}
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("giorno");  
	    column.setHeader("Giorno");  
	    column.setWidth(80);  
	    column.setRowHeader(true);  
	    column.setSummaryRenderer(new SummaryRenderer() {
			@Override
			public String render(Number value, Map<String, Number> data) {
				return "Totale Ore:";
	   			}     			
		});  
	    configs.add(column); 
	    
	    SummaryColumnConfig<Double> columnOreTimbrature=new SummaryColumnConfig<Double>();		
	    columnOreTimbrature.setId("oreTimbrature");  
	    columnOreTimbrature.setHeader("Ore Timb.");  
	    columnOreTimbrature.setWidth(55);    
	    columnOreTimbrature.setRowHeader(true); 
	    columnOreTimbrature.setSummaryType(SummaryType.SUM);  
	    columnOreTimbrature.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreTimbrature.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
			  	
		});
	    columnOreTimbrature.setSummaryRenderer(new SummaryRenderer() {  
	   			@Override
	   			public String render(Number value, Map<String, Number> data) {
	   				GroupingStore<RiepilogoFoglioOreModel>store1 = new GroupingStore<RiepilogoFoglioOreModel>();
	   				String tot="0.00";
	   				store1.add(store.getModels());
	   				for(RiepilogoFoglioOreModel riep: store1.getModels()){
	   					tot=ClientUtility.aggiornaTotGenerale(tot, number.format(riep.getOreTimbrature()));
	   				}
	   				
	   				Float n=Float.valueOf(tot);
					return number.format(n);
	   			}  
	      });  
	    configs.add(columnOreTimbrature);
	    	    
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(55);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setSummaryType(SummaryType.SUM);  
	    columnOreViaggio.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
			  	
		});
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
	    columnDeltaViaggio.setHeader("Delta Viaggio");  
	    columnDeltaViaggio.setWidth(55);    
	    columnDeltaViaggio.setRowHeader(true); 
	    columnDeltaViaggio.setSummaryType(SummaryType.SUM);  
	    columnDeltaViaggio.setAlignment(HorizontalAlignment.RIGHT);  	
	    columnDeltaViaggio.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}
			  	
		});
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
	    columnOreTotali.setHeader("Ore Totali");  
	    columnOreTotali.setWidth(55);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setSummaryType(SummaryType.SUM);  
	    columnOreTotali.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
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
	    columnOreFerie.setHeader("Ore Ferie");  
	    columnOreFerie.setWidth(55);    
	    columnOreFerie.setRowHeader(true); 
	    columnOreFerie.setSummaryType(SummaryType.SUM);  
	    columnOreFerie.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreFerie.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
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
	    columnOrePermesso.setHeader("Perm. (ROL)");  
	    columnOrePermesso.setWidth(55);    
	    columnOrePermesso.setRowHeader(true); 
	    columnOrePermesso.setSummaryType(SummaryType.SUM);  
	    columnOrePermesso.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOrePermesso.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
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
	    columnOreStraordinario.setHeader("Straordin.");  
	    columnOreStraordinario.setWidth(55);    
	    columnOreStraordinario.setRowHeader(true); 
	    columnOreStraordinario.setSummaryType(SummaryType.SUM);  
	    columnOreStraordinario.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreStraordinario.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
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
	    columnOreRecupero.setHeader("Ore a Recupero");  
	    columnOreRecupero.setWidth(55);    
	    columnOreRecupero.setRowHeader(true); 
	    columnOreRecupero.setSummaryType(SummaryType.SUM);  
	    columnOreRecupero.setAlignment(HorizontalAlignment.RIGHT);    
	    columnOreRecupero.setRenderer(new GridCellRenderer<RiepilogoFoglioOreModel>() {
			@Override
			public Object render(RiepilogoFoglioOreModel model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoFoglioOreModel> store,
					Grid<RiepilogoFoglioOreModel> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});
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
	        
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("giustificativo");  
	    column.setHeader("Giustificativo");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("note");  
	    column.setHeader("noteAggiuntive");  
	    column.setWidth(180);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);  
	    configs.add(column); 
	    	    
		return configs;
	}

}
