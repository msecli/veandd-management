package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.PeriodoSbloccoModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DialogSceltaPeriodoSbloccoCompilazione extends Dialog {

	private ListStore<PeriodoSbloccoModel> store=new ListStore<PeriodoSbloccoModel>();
	private ColumnModel cm;
	private Grid<PeriodoSbloccoModel> grid;
	private CellSelectionModel<PeriodoSbloccoModel> cellSel;
	
	private DateField dtfldInizio= new DateField();
	private DateField dtfldFine= new DateField();
	private Button btnConferma;
	private Button btnDelete;
	private SimpleComboBox<String> smplcmbxSede;
	
	public DialogSceltaPeriodoSbloccoCompilazione() {			
		
	    setLayout(new FitLayout());
		setBodyBorder(true);
		//setBodyStyle("padding: 8px; background: none");
		setWidth(600);
		setHeight(510);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setButtonAlign(HorizontalAlignment.CENTER);
		setModal(true);	
				
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setLayout(new FlowLayout());
		layoutContainer.setBorders(false);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		cpGrid.setHeading("Selezionare il periodo.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		cpGrid.setHeight(480);
		
		caricaTabella();
		
		cellSel=new CellSelectionModel<PeriodoSbloccoModel>();
		cellSel.setSelectionMode(SelectionMode.SIMPLE);
		    
		cm = new ColumnModel(createColumnsCosting());		
		grid= new Grid<PeriodoSbloccoModel>(store, cm);  
		grid.setBorders(false);  
		grid.setItemId("grid");
	    grid.setColumnLines(true);
	    grid.setStripeRows(true);
	    grid.getView().setShowDirtyCells(false);	
	    grid.setSelectionModel(cellSel);
	   	    
	    dtfldInizio.setEmptyText("Inizio Periodo..");
	    dtfldInizio.setWidth(120);
	    dtfldFine.setEmptyText("Fine Periodo..");
	    dtfldFine.setWidth(120);	
	    
	    smplcmbxSede= new SimpleComboBox<String>();
	    smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setName("sede");
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.setEnabled(true);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
		smplcmbxSede.setTriggerAction(TriggerAction.ALL);
		smplcmbxSede.setSimpleValue("T");
		smplcmbxSede.setWidth(70);
	    
	    
	    btnConferma= new Button();
	    btnConferma.setSize(26, 26);
	    btnConferma.setIconAlign(IconAlign.TOP);
	    btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
	    btnConferma.setToolTip("Conferma periodo");
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
					Date dataInizio= (Date) dtfldInizio.getValue();
					Date dataFine= (Date) dtfldFine.getValue();
					String sede= smplcmbxSede.getRawValue().toString();
					
					AdministrationService.Util.getInstance().confermaPeriodoSblocco(dataInizio, dataFine, sede, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on confermaPeriodoSblocco();");
							
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result)
								caricaTabella();
							else
								Window.alert("Problemi durante l'accesso ai dati!");
						}
					});
			}
		});
	    
	    btnDelete= new Button();
	    btnDelete.setSize(26, 26);
	    btnDelete.setIconAlign(IconAlign.TOP);
	    btnDelete.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.elimina()));
	    btnDelete.setToolTip("Elimina periodo");
	    btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				
				int idSel=cellSel.getSelectedItem().get("idPeriodo");
				
				AdministrationService.Util.getInstance().eliminaPeriodoSblocco(idSel, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on eliminaPeriodoSblocco();");
						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							caricaTabella();
						else
							Window.alert("Problemi durante l'accesso ai dati!");
					}
				});
			}
		});
	        
	    
	    ToolBar tlbrDate= new ToolBar();
	    tlbrDate.add(dtfldInizio);
	    tlbrDate.add(dtfldFine);
	    tlbrDate.add(new SeparatorToolItem());
	    tlbrDate.add(smplcmbxSede);
	    tlbrDate.add(new SeparatorToolItem());
	    tlbrDate.add(btnConferma);
	    tlbrDate.add(new SeparatorToolItem());
	    tlbrDate.add(btnDelete);
	    
	    cpGrid.setTopComponent(tlbrDate);
	    cpGrid.add(grid);	
	    
	    layoutContainer.add(cpGrid);
	    add(layoutContainer);
	}
	

	private void caricaTabella() {
		
		AdministrationService.Util.getInstance().getDatiPeriodoSblocco(new AsyncCallback<List<PeriodoSbloccoModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getDatiPeriodoSblocco();");
			}

			@Override
			public void onSuccess(List<PeriodoSbloccoModel> result) {
				if(result==null)
					Window.alert("Problema riscontrato durante l'accesso ai dati!");
				else
					loadTable(result);
			}
		});
	}


	private void loadTable(List<PeriodoSbloccoModel> result) {
		store.removeAll();
		store.add(result);
		grid.reconfigure(store, cm);	
	}
	
	
	private List<ColumnConfig> createColumnsCosting() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();  
	    
	    column = new ColumnConfig();  
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(60);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("dataInizio");  
	    column.setHeader("Inizio");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("dataFine");  
	    column.setHeader("Fine");  
	    column.setWidth(140);  
	    column.setRowHeader(true);
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);
	    
	    return configs;
	}
	
}
