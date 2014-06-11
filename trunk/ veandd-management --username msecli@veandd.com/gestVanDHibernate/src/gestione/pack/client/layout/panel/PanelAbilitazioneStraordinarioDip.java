package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelAbilitazioneStraordinarioDip  extends LayoutContainer{
	
	private ListStore<PersonaleModel>storeP = new ListStore<PersonaleModel>();
	private EditorGrid<PersonaleModel> gridRiepilogo;
	private ColumnModel cm;
	private CheckBoxSelectionModel<PersonaleModel> sm = new CheckBoxSelectionModel<PersonaleModel>();
	private Button btnConferma;
	private ComboBox<PersonaleModel> cmbxPm;
	
	private int h=Window.getClientHeight();
	
	public PanelAbilitazioneStraordinarioDip(){
		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    setItemId("pnlRiepilogo");
	    
	    final FitLayout fl= new FitLayout();
	    LayoutContainer layoutContainer= new LayoutContainer();
	    layoutContainer.setBorders(false);
	    layoutContainer.setLayout(fl);	
	    
	    ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setLayout(new FitLayout());
		cntpnlGrid.setHeaderVisible(false);
		//cntpnlGrid.setWidth(580);
		cntpnlGrid.setHeight(h-55);
		cntpnlGrid.setScrollMode(Scroll.AUTO);
	       
		btnConferma=new Button();
	    btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnConferma.setIconAlign(IconAlign.TOP);
		btnConferma.setSize(26, 26);
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<PersonaleModel> listaC= new ArrayList<PersonaleModel>();
				listaC.addAll(gridRiepilogo.getSelectionModel().getSelectedItems());
				PersonaleModel p = new PersonaleModel();  
				
				for(Record record: storeP.getModifiedRecords()){
					p=(PersonaleModel) record.getModel();
					
					AdministrationService.Util.getInstance().setDatiAutorizzazioneStraordinario((Integer)p.get("idPersonale"), 
							(Boolean)p.get("abilitazioneStraordinario"), (Date)p.get("dataInizioAbilitazioneStrao"),
							(String)p.get("notaCommesseAbilitate"),new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore di connessione on setDatiAutorizzazioneStraordinario();");
								}

								@Override
								public void onSuccess(Boolean result) {
									if(result){
										Window.alert("Modifiche effettuate!");
										caricaDatiTabella();
									}else
										Window.alert("Impossibile accedere ai dati Dipendenti!");
								}
							});			
				}
			}
		});
		
		ListStore<PersonaleModel> storePm= new ListStore<PersonaleModel>();		
		storePm.setSortField("nomeCompleto");
		storePm.setSortDir(SortDir.ASC);
		cmbxPm= new ComboBox<PersonaleModel>();
		cmbxPm.setEmptyText("Project Manager..");
		cmbxPm.setName("pm");
		cmbxPm.setStore(storePm);
		cmbxPm.setEditable(true);
		cmbxPm.setVisible(true);	    
		cmbxPm.setForceSelection(true);
		cmbxPm.setTriggerAction(TriggerAction.ALL);
		cmbxPm.setEmptyText("Project Manager..");
		cmbxPm.setDisplayField("nomeCompleto");
		cmbxPm.setAllowBlank(false);
		cmbxPm.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				caricaDatiTabella();			
			}		
		});
		getNomePM();
		
		cm = new ColumnModel(createColumns()); 
		gridRiepilogo= new EditorGrid<PersonaleModel>(storeP, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		//gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridRiepilogo.getView().setShowDirtyCells(true); 
		//gridRiepilogo.addPlugin(sm);
		//gridRiepilogo.setSelectionModel(sm);
		
		ToolBar tlbrButton= new ToolBar();
		tlbrButton.add(cmbxPm);
		tlbrButton.add(btnConferma);
				
		cntpnlGrid.add(gridRiepilogo);
		cntpnlGrid.setTopComponent(tlbrButton);
		
		add(cntpnlGrid, new FitData(3, 3, 3, 3));
	    
	}
	
	
	private void caricaDatiTabella() {
		String pm= cmbxPm.getValue().getUsername();
		AdministrationService.Util.getInstance().getListaDipendentiPerPm(pm, new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getListaDipendentiPerPm();");
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){
					loadDataDipendenti(result);
				}else
					Window.alert("Impossibile accedere ai dati Dipendenti!");
			}		
		});	
	}		
	
	
	private void loadDataDipendenti(List<PersonaleModel> result) {
		storeP.removeAll();
		storeP.setDefaultSort("cognome", SortDir.ASC);
		storeP.add(result);
		gridRiepilogo.reconfigure(storeP, cm);
	}

	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		CellEditor editorTxt;
		//sm.setSelectionMode(SelectionMode.SIMPLE);
		//configs.add(sm.getColumn());
		
		ColumnConfig column=new ColumnConfig();
		column.setId("cognome");  
		column.setHeader("Cognome");  
		column.setWidth(150);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.LEFT);
		configs.add(column); 		    
			
		column=new ColumnConfig();		
	    column.setId("nome");  
	    column.setHeader("Nome");  
	    column.setWidth(150);  
	    column.setRowHeader(true);
	    configs.add(column); 
	    
	    CheckColumnConfig checkColumn = new CheckColumnConfig("abilitazioneStraordinario", "Abilitato", 55);  
	    CellEditor checkBoxEditor = new CellEditor(new CheckBox());  
	    checkColumn.setEditor(checkBoxEditor);
	    configs.add(checkColumn);
	    
	    column= new SummaryColumnConfig<Double>();
	    column.setId("dataInizioAbilitazioneStrao");
	    column.setHeader("Dal");
	    column.setToolTip("Data Inizio Abilitazione");
	    column.setWidth(150);
	    column.setAlignment(HorizontalAlignment.LEFT);
	    DateField dtfldInizio= new DateField();
	    column.setEditor(new CellEditor(dtfldInizio));  
	    column.setDateTimeFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
	    configs.add(column);
	    		
	    column = new SummaryColumnConfig<Double>();  
	    column.setId("notaCommesseAbilitate");  
	    column.setHeader("Nota");
	    column.setWidth(450);
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    TextField<String> txtfldNote= new TextField<String>();
	    txtfldNote.setValue("#");
	    editorTxt= new CellEditor(txtfldNote){
	    	@Override  
	        public Object preProcessValue(Object value) {
	          if (value == null) {  
	            return value;  
	          }  
	          return value.toString();
	        }
	        @Override 
	        public Object postProcessValue(Object value){
	          if (value == null) {
	            return value;  
	          }
	          return value.toString();
	        }  
	    };   
	    column.setEditor(editorTxt);
	    configs.add(column);
	    
	    return configs;
	}
	

	private void getNomePM() {
		AdministrationService.Util.getInstance().getPersonalManager(new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){
					ListStore<PersonaleModel> storePm= new ListStore<PersonaleModel>();
					storePm.add(result);
					//cmbxPm.clear();
					storePm.setSortField("nomeCompleto");
					storePm.setSortDir(SortDir.ASC);
					cmbxPm.setStore(storePm);
					cmbxPm.setDisplayField("nomeCompleto");
												
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});	
	}

}
