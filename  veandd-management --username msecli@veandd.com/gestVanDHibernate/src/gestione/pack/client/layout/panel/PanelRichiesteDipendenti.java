package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRichiesteDipendenti extends LayoutContainer{

	private GroupingStore<CommentiModel>store = new GroupingStore<CommentiModel>();
	private Grid<CommentiModel> gridRiepilogo;
	private ColumnModel cmCommenti;
	private Text id;
	
	private Button btnDeleteRichiesta;
	
	public PanelRichiesteDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    setLayout(new FitLayout());	
	    setBorders(false);
	    
	    id= new Text();
	    
	  	ToolBar toolBar= new ToolBar();
		
		btnDeleteRichiesta=new Button();
		btnDeleteRichiesta.disable();
		btnDeleteRichiesta.setStyleAttribute("padding-left", "4px");
		btnDeleteRichiesta.setStyleAttribute("padding-bottom", "2px");
		btnDeleteRichiesta.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.elimina()));
		btnDeleteRichiesta.setIconAlign(IconAlign.TOP);
		btnDeleteRichiesta.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				AdministrationService.Util.getInstance().deleteCommento(Integer.valueOf(id.getText()), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on deleteCommento();");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						caricaTabella();						
					}
				});			
			}
		});
	  	  	
	  	toolBar.add(btnDeleteRichiesta);
	  	
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBodyBorder(false);         
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(630);
		cntpnlGrid.setHeight(840);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
					    
	    try {	    	
	    	cmCommenti = new ColumnModel(createColumns()); 
	    	caricaTabella();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    store.groupBy("nome");
	    store.setSortField("giorno");
	    	        
	  	    
	    gridRiepilogo= new Grid<CommentiModel>(store, cmCommenti);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true);  
	    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridRiepilogo.getView().setShowDirtyCells(false);   
	 
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<CommentiModel>>() {  
	          public void handleEvent(SelectionChangedEvent<CommentiModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	            	btnDeleteRichiesta.enable();
	            	
	            	int idC=be.getSelectedItem().get("id");
	            	
	            	id.setText(String.valueOf(idC));
	             		      	              
	            } else {  
	                
	            	           	
	            }
	          }
	    }); 
	    
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(toolBar);
	    
	    ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setExpanded(true);		
		cntpnlLayout.setSize(630, 840);
		cntpnlLayout.setScrollMode(Scroll.AUTOX);
		cntpnlLayout.setFrame(false);
		cntpnlLayout.setBorders(true);
		
		cntpnlLayout.add(cntpnlGrid);
		
		add(cntpnlLayout, new FitData(0, 0, 0, 10));
		    
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();
		column=new SummaryColumnConfig<Double>();		
		column.setId("username");  
		column.setHeader("Dipendente");  
		column.setWidth(110);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 		    
			
		column=new SummaryColumnConfig<Double>();		
	    column.setId("data");  
	    column.setHeader("Data Richiesta");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  	      
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("testo");  
	    column.setHeader("Richiesta");  
	    column.setWidth(520);  
	    column.setRowHeader(true);  	   
	    configs.add(column);
	    	    	    	    	    
		return configs;
	}
	
	private void caricaTabella() {		
		AdministrationService.Util.getInstance().getAllCommenti(new AsyncCallback<List<CommentiModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getAllCommenti()");			
			}

			@Override
			public void onSuccess(List<CommentiModel> result) {
				if(result.size()>0)
					load(result);			
			}		
		});		
	}
	
	private void load(List<CommentiModel> result) {
		
		store.removeAll();
		store.add(result);
		gridRiepilogo.reconfigure(store, cmCommenti);
	}	
	
}
