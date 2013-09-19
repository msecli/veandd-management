package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.model.PersonaleModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PanelGestioneCostiDipendenti extends LayoutContainer{

	private int h=Window.getClientHeight();
	
	private ListStore<GestioneCostiDipendentiModel> store=new ListStore<GestioneCostiDipendentiModel>();
	private ColumnModel cm;
	private EditorGrid<GestioneCostiDipendentiModel> gridRiepilogo;
	private EditorGrid<CostiHwSwModel> gridRiepilogoCostiHwSw;
	private  CheckBoxSelectionModel<GestioneCostiDipendentiModel> sm = new CheckBoxSelectionModel<GestioneCostiDipendentiModel>();  
		
	public PanelGestioneCostiDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight(h-55);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r=new Resizable(cpGrid);
		
		caricaTabellaDati();
	
		
		sm.setSelectionMode(SelectionMode.SINGLE);
		cm = new ColumnModel(createColumns());		
		gridRiepilogo= new EditorGrid<GestioneCostiDipendentiModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.addPlugin(sm);
	    gridRiepilogo.setSelectionModel(sm);
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<PersonaleModel>>() {  
	          public void handleEvent(SelectionChangedEvent<PersonaleModel> be) {  
		        	
		            if (be.getSelection().size() > 0) {      
		            	//TODO ad ogni riga selezionata carico la lista di costi che ci sono con checkbox per la selezione deselezione           
		            } else {  
		              
		            }	            
		          }             
		}); 
	   	
	    cpGrid.add(gridRiepilogo);  
				
	    //TODO implementare il salvataggio delle modifiche per i campi modificati dello store
	    
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}
	

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
		    
	    column = new ColumnConfig();  
	    column.setId("cognome");  
	    column.setHeader("Cognome");  
	    column.setWidth(140);  
	    configs.add(column);  
	    
	    column = new ColumnConfig();  
	    column.setId("costoAnnuo");  
	    column.setHeader("Costo Annuo");  
	    column.setWidth(140);  
	    TextField<String> txtfldCostoAnnuo= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldCostoAnnuo));
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("costoStruttura");  
	    column.setHeader("Costo Struttura");  
	    column.setWidth(140);  
	    TextField<String> txtfldCostoStruttura= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldCostoStruttura));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOneri");  
	    column.setHeader("Costo Oneri");  
	    column.setWidth(140);  
	    TextField<String> txtfldCostoOneri= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldCostoOneri));
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("oreCig");  
	    column.setHeader("Ore Cig");  
	    column.setWidth(140);  
	    TextField<String> txtfldOreCig= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldOreCig));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("orePianificate");  
	    column.setHeader("Ore Pianificate");  
	    column.setWidth(140);  
	    TextField<String> txtfldOrePianificate= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldOrePianificate));
	    configs.add(column);
	        	    
	    return configs;
	}

	
	private void caricaTabellaDati() {
		
		int idPersonale=20;
		
		try {
			AdministrationService.Util.getInstance().getDatiCostiPersonale(idPersonale, new AsyncCallback<List<GestioneCostiDipendentiModel>>() {
					@Override
					public void onSuccess(List<GestioneCostiDipendentiModel> result) {
						loadTable(result);
					}

					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione;");
						caught.printStackTrace();
					}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("Problemi durante il caricamento dei dati sui costi personale.");
		}	
	}
	
	
	private void loadTable(List<GestioneCostiDipendentiModel> result) {
				
		store.removeAll();
		store.setStoreSorter(new StoreSorter<GestioneCostiDipendentiModel>());  
	    store.setDefaultSort("cognome", SortDir.ASC);
		store.add(result);
	}
}
