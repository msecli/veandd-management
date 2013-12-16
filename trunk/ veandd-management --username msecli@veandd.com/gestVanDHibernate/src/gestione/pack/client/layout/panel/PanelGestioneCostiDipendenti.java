package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelGestioneCostiDipendenti extends LayoutContainer{

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<GestioneCostiDipendentiModel> store=new ListStore<GestioneCostiDipendentiModel>();
	private ColumnModel cm;
	
	private ListStore<CostiHwSwModel> storeHwSw=new ListStore<CostiHwSwModel>();
	private ColumnModel cmHwSw;
	
	private EditorGrid<GestioneCostiDipendentiModel> gridRiepilogo;
	private EditorGrid<CostiHwSwModel> gridRiepilogoCostiHwSw;
	private CheckBoxSelectionModel<GestioneCostiDipendentiModel> sm = new CheckBoxSelectionModel<GestioneCostiDipendentiModel>();  
		
	private Button btnConferma= new Button();
	private Button btnConfermaDip;
	private Text txtCognome= new Text();
	private int idSelected;
	
	public PanelGestioneCostiDipendenti(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((h-55));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		//Resizable r=new Resizable(cpGrid);
		
		ContentPanel cpGridHwSw= new ContentPanel();
		cpGridHwSw.setHeaderVisible(true);
		cpGridHwSw.setHeading("Costi Hw e Sw per Dipendente selezionato.");
		cpGridHwSw.setBorders(false);
		cpGridHwSw.setFrame(true);
		cpGridHwSw.setHeight((h-55)/2-80);
		cpGridHwSw.setWidth(w-250);
		cpGridHwSw.setScrollMode(Scroll.AUTO);
		cpGridHwSw.setLayout(new FitLayout());
		cpGridHwSw.setButtonAlign(HorizontalAlignment.CENTER);  
		
		ToolBar tlbCostiHwSw= new ToolBar();
		ToolBar tlbCostiAzienda= new ToolBar();
					
		txtCognome.setStyleAttribute("padding-left", "10px");
		txtCognome.setStyleAttribute("font-size", "15px");
		
		btnConferma.setEnabled(false);
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConferma.setIconAlign(IconAlign.TOP);
	  	btnConferma.setToolTip("Conferma Dati.");
	  	btnConferma.setSize(26, 26);
	  	btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {		
  		
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<CostiHwSwModel> lista=new ArrayList<CostiHwSwModel>();
		  		lista.addAll(storeHwSw.getModels());
				
		  		for(CostiHwSwModel c:lista)
		  			AdministrationService.Util.getInstance().saveAssociaCostiHwSw(idSelected, c, new AsyncCallback<Boolean>() {
		  				@Override
		  				public void onFailure(Throwable caught) {
		  					Window.alert("Errore di connessione on saveAssociaCostiHwSw()");
						
		  				}

		  				@Override
		  				public void onSuccess(Boolean result) {
		  					
		  					if(!result)
		  						Window.alert("Impossibile effettuare le modifiche indicate!");
		  					else
		  						storeHwSw.commitChanges();					
		  				}
		  			});
			}
		});
	  	
	  	btnConfermaDip= new Button();
	  	btnConfermaDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	  	btnConfermaDip.setIconAlign(IconAlign.TOP);
	  	btnConfermaDip.setToolTip("Conferma Dati.");
	  	btnConfermaDip.setSize(26, 26);
	  	btnConfermaDip.addSelectionListener(new SelectionListener<ButtonEvent>() {		
  		
			@Override
			public void componentSelected(ButtonEvent ce) {
				for(Record record: store.getModifiedRecords()){		    		  
		    		  GestioneCostiDipendentiModel g= new GestioneCostiDipendentiModel();
		    		  g=(GestioneCostiDipendentiModel) record.getModel();		    		  
		    		  AdministrationService.Util.getInstance().editDatiCostiAzienda(g, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on editDataCostiAzienda();");
								}

								@Override
								public void onSuccess(Void result) {
									caricaTabellaDati();
									store.commitChanges();
								}		    			  
						}); 	  
		    	  }
		    	  store.commitChanges();				
			}
		});
	  	
		caricaTabellaDati();
			
		//sm.setSelectionMode(SelectionMode.SINGLE);
		cm = new ColumnModel(createColumns());		
		gridRiepilogo= new EditorGrid<GestioneCostiDipendentiModel>(store, cm);  
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setColumnLines(true);
	    gridRiepilogo.setStripeRows(true);
	    gridRiepilogo.addPlugin(sm);
	    gridRiepilogo.setSelectionModel(sm);
	    gridRiepilogo.getView().setShowDirtyCells(false);
	    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<GestioneCostiDipendentiModel>>() {  
	          public void handleEvent(SelectionChangedEvent<GestioneCostiDipendentiModel> be) {  
		        	
		            if (be.getSelection().size() > 0) { 
		            	btnConferma.setEnabled(true);
		            	idSelected=be.getSelectedItem().get("idPersonale");
		            	txtCognome.setText("Selezionato: "+(String)be.getSelectedItem().get("cognome"));
		            	caricaListaCostiHwSw(idSelected);           
		            } else {  
		              
		            	txtCognome.setText("");
		            }	            
		          }		            
		}); 
	    
	    cmHwSw=new ColumnModel(createColumnsHwSw());
	    gridRiepilogoCostiHwSw= new EditorGrid<CostiHwSwModel>(storeHwSw, cmHwSw);
	    gridRiepilogoCostiHwSw.setBorders(false);
	    gridRiepilogoCostiHwSw.setItemId("grid");
	    gridRiepilogoCostiHwSw.setStripeRows(true); 
	    gridRiepilogoCostiHwSw.setColumnLines(true);
	    gridRiepilogoCostiHwSw.addPlugin(sm);
	   
	    cpGrid.add(gridRiepilogo); 
	    tlbCostiAzienda.add(btnConfermaDip);
	    tlbCostiAzienda.add(txtCognome);
	    cpGrid.setTopComponent(tlbCostiAzienda);
	    
	    cpGridHwSw.add(gridRiepilogoCostiHwSw);
	    tlbCostiHwSw.add(btnConferma);
	   // tlbCostiHwSw.add(txtCognome);
	    cpGridHwSw.setTopComponent(tlbCostiHwSw);
	    
	    vp.add(cpGrid);
	   // vp.add(cpGridHwSw);
	   
	    layoutContainer.add(vp, new FitData(0, 3, 3, 0));
				
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
	    txtfldCostoAnnuo.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldCostoAnnuo.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldCostoAnnuo));
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("costoStruttura");  
	    column.setHeader("Costo Struttura");  
	    column.setWidth(100);  
	    TextField<String> txtfldCostoStruttura= new TextField<String>();
	    txtfldCostoStruttura.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldCostoStruttura.getMessages().setRegexText("Deve essere un numero!");
	    txtfldCostoStruttura.setWidth(40);
	    column.setEditor(new CellEditor(txtfldCostoStruttura));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("costoOneri");  
	    column.setHeader("Costo Oneri");  
	    column.setWidth(100);  
	    TextField<String> txtfldCostoOneri= new TextField<String>();
	    txtfldCostoOneri.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldCostoOneri.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldCostoOneri));
	    configs.add(column);
	        
	    column = new ColumnConfig();  
	    column.setId("tipoOrario");  
	    column.setHeader("Orario Giornaliero");  
	    column.setWidth(100);  
	    TextField<String> txtfldTipoOrario= new TextField<String>();
	    txtfldTipoOrario.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldTipoOrario.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldTipoOrario));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("oreCig");  
	    column.setHeader("Ore Cig");  
	    column.setWidth(140);  
	    TextField<String> txtfldOreCig= new TextField<String>();	
	    txtfldOreCig.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldOreCig.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldOreCig));
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("orePianificate");  
	    column.setHeader("Ore Pianificate");  
	    column.setWidth(140);  
	    TextField<String> txtfldOrePianificate= new TextField<String>();
	    txtfldOrePianificate.setRegex("[0-9]+[.]{1}[0-9]{2}|[0-9]");
	    txtfldOrePianificate.getMessages().setRegexText("Deve essere un numero!");
	    column.setEditor(new CellEditor(txtfldOrePianificate));
	    configs.add(column);
	        	    
	    return configs;
	}

	
	private List<ColumnConfig> createColumnsHwSw() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
		    
	    column = new ColumnConfig();  
	    column.setId("tipologia");  
	    column.setHeader("Tipologia");  
	    column.setWidth(80);  
	    //TextField<String> txtfldTipologia= new TextField<String>();	    
	    //column.setEditor(new CellEditor(txtfldTipologia));
	    configs.add(column);  
	    
	    column = new ColumnConfig();  
	    column.setId("descrizione");  
	    column.setHeader("Descrizione");  
	    column.setWidth(140);  
	   // TextField<String> txtfldDescrizione= new TextField<String>();	    
	    //column.setEditor(new CellEditor(txtfldDescrizione));
	    configs.add(column);
	      
	    
	    column = new ColumnConfig();  
	    column.setId("costo");  
	    column.setHeader("Costo");  
	    column.setWidth(140);  
	   // TextField<String> txtfldCosto= new TextField<String>();	    
	  //  column.setEditor(new CellEditor(txtfldCosto));
	    configs.add(column);
	        	      
	    CheckColumnConfig checkColumn = new CheckColumnConfig("utilizzato", "Utilizzato?", 155);  
	    CellEditor checkBoxEditor = new CellEditor(new CheckBox());  
	    checkColumn.setEditor(checkBoxEditor);  
	    configs.add(checkColumn);	   
	        	    
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
	
	
	private void caricaListaCostiHwSw(int id) {
		AdministrationService.Util.getInstance().getDatiCostiHwSw(id, new AsyncCallback<List<CostiHwSwModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connession on getDatiCostiHwSw();");
				
			}

			@Override
			public void onSuccess(List<CostiHwSwModel> result) {
				loadTableHwSw(result);
			}		
		});		
	} 
	
	
	private void loadTableHwSw(List<CostiHwSwModel> result) {
		storeHwSw.removeAll();
		storeHwSw.setStoreSorter(new StoreSorter<CostiHwSwModel>());  
		storeHwSw.add(result);
	}
}
