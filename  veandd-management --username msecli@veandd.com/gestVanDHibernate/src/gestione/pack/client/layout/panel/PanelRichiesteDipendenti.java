package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRichiesteDipendenti extends LayoutContainer{

	private ListStore<CommentiModel>store = new ListStore<CommentiModel>();
	private Grid<CommentiModel> gridRiepilogo;
	private ColumnModel cmCommenti;
	private RowExpander expander;
	private Button btnDeleteRichiesta;
	private Button btnSetEdit;
	private SimpleComboBox<String >smplcmbxMese;
	
	private  CheckBoxSelectionModel<CommentiModel> sm = new CheckBoxSelectionModel<CommentiModel>();  
	
	public PanelRichiesteDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");	    
	    setLayout(new FitLayout());	
	    setBorders(false);
				    	    
		btnDeleteRichiesta=new Button();
		btnDeleteRichiesta.disable();
		btnDeleteRichiesta.setStyleAttribute("padding-left", "4px");
		btnDeleteRichiesta.setStyleAttribute("padding-bottom", "2px");
		btnDeleteRichiesta.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.elimina()));
		btnDeleteRichiesta.setIconAlign(IconAlign.TOP);
		btnDeleteRichiesta.setSize(26, 26);
		btnDeleteRichiesta.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<CommentiModel> listaC= new ArrayList<CommentiModel>();
				listaC.addAll(gridRiepilogo.getSelectionModel().getSelectedItems());
				for(CommentiModel c:listaC){
					int i= c.get("id");
					AdministrationService.Util.getInstance().deleteCommento(i, new AsyncCallback<Boolean>() {

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
			}
		});
		
		btnSetEdit= new Button();
		btnSetEdit.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnSetEdit.setIconAlign(IconAlign.TOP);
		btnSetEdit.setSize(26, 26);
		btnSetEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				List<CommentiModel> listaC= new ArrayList<CommentiModel>();
				listaC.addAll(gridRiepilogo.getSelectionModel().getSelectedItems());
				 				
 				for(CommentiModel c:listaC){
 					int i=c.get("id");
 					AdministrationService.Util.getInstance().confermaEditCommenti(i, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on confermaEditCommenti()");
							
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result==true)
								caricaTabella();
							else
								Window.alert("Errore caricamento dati su Commenti()");
							
						}
					});
 					
 				}			
			}
		});
		
		Date d= new Date();
		String data= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
		
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		smplcmbxMese.setEditable(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
		smplcmbxMese.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				ListStore<CommentiModel>storeApp = new ListStore<CommentiModel>();
				ListStore<CommentiModel>storeRes = new ListStore<CommentiModel>();
				String meseListStore= new String();
				String meseCmbx=smplcmbxMese.getRawValue().toString();
				meseCmbx=meseCmbx.substring(0, 3);
				storeRes.removeAll();
				storeApp.removeAll();
				storeApp.add(store.getModels());
				for(CommentiModel c:storeApp.getModels()){
					meseListStore=c.get("data");
					meseListStore=meseListStore.substring(0,3);
					meseListStore=ClientUtility.traduciMeseToIt(meseListStore);
					if(meseListStore.compareTo(meseCmbx)==0)
						storeRes.add(c);
				}
								
				gridRiepilogo.reconfigure(storeRes, cmCommenti); 
			}		
		});
			  	  	
		SeparatorToolItem sp= new SeparatorToolItem();
		sp.setWidth("20px");
		ToolBar toolBar= new ToolBar();
		toolBar.setBorders(false);
	  	toolBar.add(btnDeleteRichiesta);
	  	toolBar.add(sp);
	  	toolBar.add(btnSetEdit);
	  	toolBar.add(new SeparatorToolItem());
	  	toolBar.add(smplcmbxMese);
	  	
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setLayout(new FitLayout());  
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setWidth(580);
		cntpnlGrid.setHeight(840);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
							    
	    try {	    	
	    	cmCommenti = new ColumnModel(createColumns()); 
	    	caricaTabella();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    	        	  	    
	    gridRiepilogo= new Grid<CommentiModel>(store, cmCommenti);  
	    gridRiepilogo.setItemId("grid");
	    gridRiepilogo.setBorders(false);  
	    gridRiepilogo.setStripeRows(true);  
	    gridRiepilogo.setColumnLines(true);  
	    gridRiepilogo.setColumnReordering(true);
	    //gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridRiepilogo.getView().setShowDirtyCells(false); 
	    gridRiepilogo.addPlugin(expander);  
	    gridRiepilogo.addPlugin(sm);
	    gridRiepilogo.setSelectionModel(sm);
	   
	    gridRiepilogo.addListener(Events.CellClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				btnDeleteRichiesta.enable();
			}
		});
	    cntpnlGrid.add(gridRiepilogo);
	    cntpnlGrid.setTopComponent(toolBar);
	  		
		add(cntpnlGrid, new FitData(0, 0, 0, 10));
		    
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		XTemplate tpl = XTemplate.create("<p><b>Dipendente:</b> {username}</p><br>" +
				"<p><b>Data:</b> {data}</p><br>" +
				"<p><b>Richiesta:</b> {testo}</p>");  
		    
		expander = new RowExpander();
		expander.setTemplate(tpl); 
		expander.setWidth(20);
				
		configs.add(expander);
		
		
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());
		
		ColumnConfig column=new ColumnConfig();
		column.setId("username");  
		column.setHeader("Dipendente");  
		column.setWidth(110);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 		    
			
		column=new ColumnConfig();		
	    column.setId("data");  
	    column.setHeader("Data Richiesta");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  	      
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("testo");  
	    column.setHeader("Richiesta");  
	    column.setWidth(250);  
	    column.setRowHeader(true);  	   
	    configs.add(column);
	    
	    column=new ColumnConfig();
	    column.setId("editated");
	    column.setHeader("");
	    column.setWidth(30);
	    column.setRowHeader(true);
	    column.setToolTip("Se verde indica che la modifica è stata effettuata.");
	    column.setRenderer(new GridCellRenderer<CommentiModel>() {

			@Override
			public Object render(CommentiModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<CommentiModel> store, Grid<CommentiModel> grid) {
				
				Boolean mod=model.get("editated");
				if(mod==true){
					String color = "#90EE90";                    
					config.style = config.style + ";background-color:" + color + ";";
				}else
					config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
					
				return "";
			}
		});
	    
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
				if(result.size()>=0)
					load(result);					
			}		
		});		
	}
	
	private void load(List<CommentiModel> result) {		
		store.removeAll();
		store.setSortField("giorno");	  
		store.add(result);
		//gridRiepilogo.reconfigure(store, cmCommenti);
		
		ListStore<CommentiModel>storeApp = new ListStore<CommentiModel>();
		ListStore<CommentiModel>storeRes = new ListStore<CommentiModel>();
		String meseListStore= new String();
		String meseCmbx=smplcmbxMese.getRawValue().toString();
		meseCmbx=meseCmbx.substring(0, 3);
		storeRes.removeAll();
		storeApp.removeAll();
		storeApp.add(store.getModels());
		for(CommentiModel c:storeApp.getModels()){
			meseListStore=c.get("data");
			meseListStore=meseListStore.substring(0,3);
			meseListStore=ClientUtility.traduciMeseToIt(meseListStore);
			if(meseListStore.compareTo(meseCmbx)==0)
				storeRes.add(c);
		}
						
		gridRiepilogo.reconfigure(storeRes, cmCommenti); 
	}	
	
}
