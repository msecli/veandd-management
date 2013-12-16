package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommentiModel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DialogInvioCommenti  extends Dialog {

	private TextArea txtCommenti= new TextArea();
	protected Button btnInvia;
	protected String utente= new String();
	
	private GroupingStore<CommentiModel>store = new GroupingStore<CommentiModel>();
	private Grid<CommentiModel> gridRiepilogo;
	private ColumnModel cmCommenti;
	private RowExpander expander;
	
	public DialogInvioCommenti(final String username){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(978);
		setHeight(450);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Invio Commenti.");
		setButtonAlign(HorizontalAlignment.CENTER);
		setModal(true);		
		
		utente=username;
		
	/*	LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FitLayout());
		bodyContainer.setBorders(false);*/
		
		HorizontalPanel hp= new HorizontalPanel();
		hp.setSpacing(5);
		
		ContentPanel cp = new ContentPanel(); //pannello contenente le due liste
		cp.setHeaderVisible(false);
		cp.setSize(420, 255);
		cp.setFrame(true);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
				
		ContentPanel cpGridCommenti= new ContentPanel();
		cpGridCommenti.setHeading("Richieste Inviate");
		cpGridCommenti.setBorders(false);
		cpGridCommenti.setFrame(true);
		cpGridCommenti.setBodyBorder(false);
		cpGridCommenti.setLayout(new FitLayout());  
		cpGridCommenti.setWidth(515);
		cpGridCommenti.setHeight(355);
		cpGridCommenti.setScrollMode(Scroll.AUTO);
		
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
		gridRiepilogo.getView().setShowDirtyCells(false); 
		gridRiepilogo.addPlugin(expander);  
		//gridRiepilogo.addPlugin(sm);
		cpGridCommenti.add(gridRiepilogo);
		
		txtCommenti.setAllowBlank(false);
		txtCommenti.setSize(420, 250);
		txtCommenti.setMaxLength(310);
		txtCommenti.setEmptyText("Segnalare eventuali problemi e anomalie riscontrate..");
		cp.add(txtCommenti);
		
		hp.add(cp);
		hp.add(cpGridCommenti);

		add(hp);
	}
	
	 private void caricaTabella() {
		 AdministrationService.Util.getInstance().getAllCommenti(utente, new AsyncCallback<List<CommentiModel>>() {

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
			gridRiepilogo.reconfigure(store, cmCommenti);
		}	
	 
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		XTemplate tpl = XTemplate.create("<p><b>Data:</b> {data}</p><br>" +
				"<p><b>Richiesta:</b> {testo}</p>");  
		    
		expander = new RowExpander();
		expander.setTemplate(tpl); 
		expander.setWidth(20);
				
		configs.add(expander);		
		
		ColumnConfig column=new ColumnConfig();
		/*column.setId("username");  
		column.setHeader("Dipendente");  
		column.setWidth(110);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);  
		configs.add(column); 		    
			*/
		column=new ColumnConfig();		
	    column.setId("data");  
	    column.setHeader("Data Richiesta");  
	    column.setWidth(90);  
	    column.setRowHeader(true);  	      
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("testo");  
	    column.setHeader("Richiesta");  
	    column.setWidth(350);  
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

	protected void createButtons() {
		    super.createButtons();
		    
		    getButtonBar().add(new FillToolItem());
		    btnInvia =new Button("Invia");
		    btnInvia.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					if(txtCommenti.isValid()){
						String testo= txtCommenti.getValue().toString();
						AdministrationService.Util.getInstance().invioCommenti(testo, utente, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on invioCommenti()");
								hide();
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result==true){
									Window.alert("Messaggio inviato!");
									hide();
								}else{
									Window.alert("error: Impossibile completare l'operazione.");
								}
								
							}
						
						});
					}else Window.alert("E' necessario inserire del testo!");
						
				}
			});

		    addButton(btnInvia);    
	}
}
