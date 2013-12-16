package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;  
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelEditPasswordUtenti  extends LayoutContainer{

	private ListStore<PersonaleModel> store=new ListStore<PersonaleModel>();
	private ColumnModel cm;
	private EditorGrid<PersonaleModel> gridRiepilogo;
	
	protected Button btnGuida;
	protected Button btnConferma;
	protected Button btnReset;
	
	private int h=Window.getClientHeight();
	//private int w=Window.getClientWidth();	
		
	public PanelEditPasswordUtenti(){
		
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
		//Definizione colonne griglia    
		cm = new ColumnModel(createColumns());	
		store.setDefaultSort("cognome", SortDir.ASC);
		gridRiepilogo= new EditorGrid<PersonaleModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
	    cpGrid.add(gridRiepilogo);  
			
		btnGuida= new Button();
		btnGuida.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.question()));
		btnGuida.setToolTip("Effettuare la modifica delle password desiderate direttamente nella cella. Una volta effettuate le modifiche, premere sul pulsante ''Conferma Modifiche''");
		btnGuida.setIconAlign(IconAlign.TOP);
		btnGuida.setSize(26, 26);
		
		btnConferma= new Button();
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnConferma.setToolTip("Conferma Modifiche");
		btnConferma.setIconAlign(IconAlign.TOP);
		btnConferma.setSize(26, 26);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				 for(Record record: store.getModifiedRecords()){		    		  
		    		  PersonaleModel p = new PersonaleModel();
		    		  p=(PersonaleModel) record.getModel();
		    		  
		    		  AdministrationService.Util.getInstance().editDataPersonale(p.getIdPersonale(), "PSSWD", "", "", (String)p.get("password"), "", "", 
		    				  "", "", "", "", "", "", "", "", "", "", "", "", "", new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on editDataPersonale();");
								}

								@Override
								public void onSuccess(Void result) {
									caricaTabellaDati();								
								}		    			  
						}); 	  
		    	  }
		    	  store.commitChanges();				
			}
		});
		
		btnReset= new Button();
		btnReset.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.elimina()));
		btnReset.setToolTip("Reset");
		btnReset.setIconAlign(IconAlign.TOP);
		btnReset.setSize(26, 26);
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				 store.rejectChanges();			
			}
		});
				
		ToolBar tlbrButtons= new ToolBar();
		tlbrButtons.add(btnConferma);
		tlbrButtons.add(new SeparatorToolItem());
		tlbrButtons.add(btnReset);
		tlbrButtons.add(new SeparatorToolItem());
		tlbrButtons.add(btnGuida);
						
	    cpGrid.setTopComponent(tlbrButtons);
				
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		add(layoutContainer);	    
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
			    
	    column=new ColumnConfig();		
	    column.setId("nome");  
	    column.setHeader("Nome");  
	    column.setWidth(140);  
	    column.setRowHeader(true);  
	    configs.add(column);  
	  
	    column = new ColumnConfig();  
	    column.setId("cognome");  
	    column.setHeader("Cognome");  
	    column.setWidth(120);  
	    configs.add(column);  
	    
	    column = new ColumnConfig();  
	    column.setId("username");  
	    column.setHeader("Username");  
	    column.setWidth(160);  
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("password");  
	    column.setHeader("Password");  
	    column.setWidth(160);  
	    TextField<String> txtfldPassword= new TextField<String>();	    
	    column.setEditor(new CellEditor(txtfldPassword));  
	    configs.add(column);  
	
	    return configs;
	}

	private void caricaTabellaDati() {
		try {
			AdministrationService.Util.getInstance().getAllPersonaleModel(new AsyncCallback<List<PersonaleModel>>() {
					@Override
					public void onSuccess(List<PersonaleModel> result) {
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
			Window.alert("Problemi durante il caricamento dei dati Personale.");
		}		
	}
	
	private void loadTable(List<PersonaleModel> result) {
		store.removeAll();
		store.setStoreSorter(new StoreSorter<PersonaleModel>());  
	    store.setDefaultSort("cognome", SortDir.ASC);
		store.add(result);
		
	}
	
	
	
	
}
