package gestione.pack.client.layout.panel;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DialogConfermaPmFogliFatturazione extends Dialog{
	
	private ListStore<DatiFatturazioneMeseModel>store = new ListStore<DatiFatturazioneMeseModel>();
	private EditorGrid<DatiFatturazioneMeseModel> gridRiepilogo;
	private ColumnModel cm;	
	
	protected SimpleComboBox<String> smplcmbxPM;
	protected Button btnConferma;
	
	protected String anno;
	protected String mese;
	
	private CheckColumnConfig checkColumn;
	
	public DialogConfermaPmFogliFatturazione(String annoRif, String meseRif){
		
		this.anno=annoRif;
		this.mese=meseRif;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setResizable(true);
		setClosable(true);
		setConstrain(false);
		setButtons("");
		setHeading("Conferma Dati");
		setModal(false);
				
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(800);
		cntpnlGrid.setWidth(450);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setLayout(new FitLayout());
		
		cm=new ColumnModel(createColumns());
		store.setDefaultSort("numeroCommessa", SortDir.ASC);
		gridRiepilogo= new EditorGrid<DatiFatturazioneMeseModel>(store, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.getView().setShowDirtyCells(false);
		gridRiepilogo.addPlugin(checkColumn);
		
		btnConferma= new Button();
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnConferma.setToolTip("Conferma Dati");
		btnConferma.setIconAlign(IconAlign.TOP);
		btnConferma.setSize(26, 26);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					Integer idFoglioFatturazione;
					Boolean check;
					
					for(Record record: store.getModifiedRecords()){
						idFoglioFatturazione=(Integer)record.get("idFoglioFatturazione");
						check=(Boolean)record.get("confermaPm");
						AdministrationService.Util.getInstance().editConfermaPmFogliFatturazione(idFoglioFatturazione, check, new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore connessione on editConfermaPmFogliFatturazione();");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result){
									caricaDatiTabella(mese+anno, smplcmbxPM.getRawValue().toString());
								}else 
									Window.alert("Impossibile effettuare le modifiche richieste!");
							}
						});
					}
				}
		});
		
		smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setEmptyText("Project Manager..");
		smplcmbxPM.setAllowBlank(false);
		smplcmbxPM.setWidth(180);
		smplcmbxPM.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				String pm=smplcmbxPM.getRawValue().toString();
				caricaDatiTabella(mese+anno, pm);
			}
		});
		getNomePM();
		
		ToolBar tlbrRiepilogoOre= new ToolBar();
		tlbrRiepilogoOre.add(smplcmbxPM);
		tlbrRiepilogoOre.add(btnConferma);
		
		cntpnlGrid.setTopComponent(tlbrRiepilogoOre);
		cntpnlGrid.add(gridRiepilogo);
		
		bodyContainer.add(cntpnlGrid);
		
		add(bodyContainer);		
	}

	private void getNomePM() {
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() { //Viene restituito nella forma Cognome Nome

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxPM.add(result);
					smplcmbxPM.recalculate();
					//smplcmbxPM.add("Tutti");
				}else 
					Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});
	}

	private void caricaDatiTabella(String meseSel, String pm) {
		AdministrationService.Util.getInstance().getReportDatiFatturazioneMese(pm, meseSel, new AsyncCallback<List<DatiFatturazioneMeseModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getReportDatiFatturazioneMese();");
			}

			@Override
			public void onSuccess(List<DatiFatturazioneMeseModel> result) {
				if(result.size()>0)
					loadData(result);
				else
					Window.alert("Nessun dato trovato!");
			}
		});
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 

		checkColumn = new CheckColumnConfig("confermaPm", "Check", 50);
		final CheckBox chbxConferma= new CheckBox();
	    CellEditor checkBoxEditor = new CellEditor(chbxConferma){
	    	@Override  
	        public Object preProcessValue(Object value) {	
	          return chbxConferma.getValue();  
	        }
	        @Override
	        public Object postProcessValue(Object value) {
	            return chbxConferma.getValue(); 	          
	        }  
	    };
	    checkColumn.setEditor(checkBoxEditor);  
	    
	    configs.add(checkColumn);
		
		ColumnConfig column=new ColumnConfig();	
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(70);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("numeroOrdine");  
		column.setHeader("Ordine");  
		column.setWidth(100);  
		column.setRowHeader(true);
		configs.add(column);
	    
		column=new ColumnConfig();			
	    column.setId("oggettoAttivita");  
	    column.setHeader("Oggetto");  
	    column.setWidth(140);  
	    column.setRowHeader(true); 
	    configs.add(column);
	    		
	    return configs;
	}

	private void loadData(List<DatiFatturazioneMeseModel> result) {
			store.removeAll();
			store.add(result);
			store.setDefaultSort("numeroCommessa", SortDir.ASC);
			gridRiepilogo.reconfigure(store, cm);		
	}
	
}
