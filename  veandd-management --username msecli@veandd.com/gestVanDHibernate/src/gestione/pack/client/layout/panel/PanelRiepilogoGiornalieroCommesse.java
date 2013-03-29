package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RiepilogoCommesseGiornalieroModel;
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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.js.JsonConverter;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
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

public class PanelRiepilogoGiornalieroCommesse extends LayoutContainer{	
	
	private GroupingStore<RiepilogoCommesseGiornalieroModel>store = new GroupingStore<RiepilogoCommesseGiornalieroModel>();
	private Grid<RiepilogoCommesseGiornalieroModel> gridRiepilogo;
	private ColumnModel cmCommessa;
	private String username= new String();
	private String tLavoratore= new String();
	private Date data;
	private Button btnPrint= new Button();
	
	public PanelRiepilogoGiornalieroCommesse(String user, Date dataRiferimento){
		username=user;
		data=dataRiferimento;
		
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
	    
	  	ButtonBar btnBarPrint= new ButtonBar();
	  	  	
	  	btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print()));
		btnPrint.setToolTip("Stampa");
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
							
				final String url="/gestvandhibernate/PrintDataServlet";
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

			    try {
			    	
			    	List<RiepilogoCommesseGiornalieroModel> list= new ArrayList<RiepilogoCommesseGiornalieroModel>();
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
		});
	  	
		//btnBarPrint.add(btnPrint);
		
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
		    
	    gridRiepilogo= new EditorGrid<RiepilogoCommesseGiornalieroModel>(store, cmCommessa);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setView(summary);  
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(btnBarPrint);
	    
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
		AdministrationService.Util.getInstance().getRiepilogoGiornalieroCommesse(username, data,  new AsyncCallback<List<RiepilogoCommesseGiornalieroModel>>() {	
			@Override
			public void onSuccess(List<RiepilogoCommesseGiornalieroModel> result) {
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
	
	
	private void loadTable(List<RiepilogoCommesseGiornalieroModel> result) {
		try {
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
		
		return configs;
	}
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
	    	    
		return configs;
	}

}
