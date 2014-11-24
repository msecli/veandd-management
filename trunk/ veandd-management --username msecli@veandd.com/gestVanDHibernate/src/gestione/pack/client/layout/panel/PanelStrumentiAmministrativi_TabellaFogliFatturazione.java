package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelStrumentiAmministrativi_TabellaFogliFatturazione extends LayoutContainer{
	
	private ListStore<DatiFatturazioneMeseModel>store = new ListStore<DatiFatturazioneMeseModel>();
	private Grid<DatiFatturazioneMeseModel> gridRiepilogo;
	private ColumnModel cmRiepilogo;
	private  CheckBoxSelectionModel<DatiFatturazioneMeseModel> sm = new CheckBoxSelectionModel<DatiFatturazioneMeseModel>();  
	
	private SimpleComboBox<String> smplcmbxMese;
	private SimpleComboBox<String> smplcmbxAnno;
	
	private Button btnSelect;
	private Button btnDelete;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	public PanelStrumentiAmministrativi_TabellaFogliFatturazione(){
		
	}
	
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(false);
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setSize(w-230, h-65);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
	
		Resizable r=new Resizable(cpGrid);
	
		ToolBar tlbrOperazioni= new ToolBar();
		cpGrid.setTopComponent(tlbrOperazioni);
		
		Date d= new Date();
		String dt= d.toString();
		String anno= dt.substring(dt.length()-4, dt.length());
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(dt.substring(4, 7)));
	
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setEmptyText("Anno...");
		smplcmbxAnno.setAllowBlank(false);
		smplcmbxAnno.setEnabled(true);
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setWidth(100);
		for(String a:DatiComboBox.getAnno())
			smplcmbxAnno.add(a);
		smplcmbxAnno.setSimpleValue(anno);
		tlbrOperazioni.add(smplcmbxAnno);
		
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese...");
		smplcmbxMese.setAllowBlank(false);
		smplcmbxMese.setEnabled(true);
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setWidth(100);
		for(String m:DatiComboBox.getMese())
			smplcmbxMese.add(m);
		smplcmbxMese.setSimpleValue(mese);
		tlbrOperazioni.add(smplcmbxMese);
		
		btnSelect= new Button();
		btnSelect.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnSelect.setToolTip("Aggiorna");
		btnSelect.setIconAlign(IconAlign.TOP);
		btnSelect.setSize(26, 26);
		btnSelect.setEnabled(true);
		btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				caricaDatiTabella();
				
			}

		});
		tlbrOperazioni.add(btnSelect);
						 
		btnDelete=new Button();
		btnDelete.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.respingi()));
		btnDelete.setToolTip("Elimina record selezionati");
		btnDelete.setIconAlign(IconAlign.TOP);
		btnDelete.setSize(26, 26);
		btnDelete.setEnabled(true);
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<DatiFatturazioneMeseModel> listaSelected= new ArrayList<DatiFatturazioneMeseModel>();
				listaSelected.addAll(sm.getSelectedItems());
				if(listaSelected.size()>0)
					for(DatiFatturazioneMeseModel dm:listaSelected){
						AdministrationService.Util.getInstance().deleteFoglioFatturazione(dm, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on deleteFoglioFatturazione()!");
							}

							@Override
							public void onSuccess(Boolean result) {
								caricaDatiTabella();
							}						
							
						});				
					}
			}
		});
		tlbrOperazioni.add(btnDelete);
		
		cmRiepilogo = new ColumnModel(createColumns()); 
		store.setDefaultSort("numeroCommessa", SortDir.ASC);
		gridRiepilogo= new EditorGrid<DatiFatturazioneMeseModel>(store, cmRiepilogo);  
		gridRiepilogo.setBorders(false);
		gridRiepilogo.setStripeRows(true);
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.addPlugin(sm);
		gridRiepilogo.setSelectionModel(sm);
				
		cpGrid.setTopComponent(tlbrOperazioni);
		cpGrid.add(gridRiepilogo);
		
		layoutContainer.add(cpGrid, new FitData(3,3,3,3));
		add(layoutContainer);
	}
	
	
	private void caricaDatiTabella() {
		String anno=smplcmbxAnno.getRawValue().toString();
		String mese=smplcmbxMese.getRawValue().toString();
		
		AdministrationService.Util.getInstance().getDatiFogliFatturazioneMese(anno,mese,new AsyncCallback<List<DatiFatturazioneMeseModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getDatiFogliFatturazioneMese()!");
				
			}

			@Override
			public void onSuccess(List<DatiFatturazioneMeseModel> result) {
				if(result!=null){
					loadData(result);
				}else
					Window.alert("Errore durante il caricamento ");
			}
		
		});
	}
	
	
	private void loadData(List<DatiFatturazioneMeseModel> result) {
		store.removeAll();
		store.add(result);
		store.setDefaultSort("numeroCommessa", SortDir.ASC);
		gridRiepilogo.reconfigure(store, cmRiepilogo);
	}
	
	
	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		sm.setSelectionMode(SelectionMode.SIMPLE);
		configs.add(sm.getColumn());
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("numeroOrdine");  
	    column.setHeader("Ordine");  
	    column.setWidth(150);
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();
	    column.setId("oggettoAttivita");  
	    column.setHeader("Attivita'");
	    column.setWidth(150);
	    column.setRowHeader(true);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("oreEseguite");
	    column.setHeader("h/Eseguite");  
	    column.setWidth(150);
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("oreFatturate");  
	    column.setHeader("h/Fatturate");
	    column.setWidth(150);
	    column.setRowHeader(true);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("variazioneSal");  
	    column.setHeader("SAL");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("variazionePcl");  
	    column.setHeader("PCL");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    return configs;
	}
}
