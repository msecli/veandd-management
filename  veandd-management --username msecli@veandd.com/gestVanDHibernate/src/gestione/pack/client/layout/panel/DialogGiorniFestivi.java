package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.GiorniFestiviModel;
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

public class DialogGiorniFestivi extends Dialog{

	private ListStore<GiorniFestiviModel> store=new ListStore<GiorniFestiviModel>();
	private ColumnModel cm;
	private Grid<GiorniFestiviModel> grid;
	private CellSelectionModel<GiorniFestiviModel> cellSel;
	
	private DateField dtfldGiorno= new DateField();
	private SimpleComboBox<String> smplcmbxSede;
	private Button btnConferma;
	private Button btnDelete;
	
	public DialogGiorniFestivi(){
		
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
			cpGrid.setHeading("Selezionare i giorni festivi.");
			cpGrid.setBorders(false);
			cpGrid.setFrame(true);
			cpGrid.setScrollMode(Scroll.AUTO);
			cpGrid.setLayout(new FitLayout());
			cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
			cpGrid.setHeight(480);
			
			caricaTabella();
			
			cellSel=new CellSelectionModel<GiorniFestiviModel>();
			cellSel.setSelectionMode(SelectionMode.SIMPLE);
			    
			cm = new ColumnModel(createColumnsCosting());		
			grid= new Grid<GiorniFestiviModel>(store, cm);  
			grid.setBorders(false);  
			grid.setItemId("grid");
		    grid.setColumnLines(true);
		    grid.setStripeRows(true);
		    grid.getView().setShowDirtyCells(false);	
		    grid.setSelectionModel(cellSel);
		   	    
		    dtfldGiorno.setEmptyText("Giorno..");
		    dtfldGiorno.setWidth(120);
		    
		    smplcmbxSede= new SimpleComboBox<String>();
		    smplcmbxSede.setFieldLabel("Sede");
			smplcmbxSede.setName("sede");
			smplcmbxSede.setEmptyText("Sede..");
			smplcmbxSede.setAllowBlank(false);
			smplcmbxSede.setEnabled(true);
			smplcmbxSede.add("T");
			smplcmbxSede.add("B");
			smplcmbxSede.add("Tutti");
			smplcmbxSede.setTriggerAction(TriggerAction.ALL);
			smplcmbxSede.setSimpleValue("Tutti");
			smplcmbxSede.setWidth(70);
		  	    
		    btnConferma= new Button();
		    btnConferma.setSize(26, 26);
		    btnConferma.setIconAlign(IconAlign.TOP);
		    btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		    btnConferma.setToolTip("Conferma periodo");
			btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
						Date giorno= (Date) dtfldGiorno.getValue();
						String sede=smplcmbxSede.getRawValue().toString();
						AdministrationService.Util.getInstance().inserisciGiornoFestivo(giorno, sede, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore di connessione on inserisciGiornoFestivo();");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result)
									caricaTabella();
								else
									Window.alert("Problema riscontrato durante l'accesso ai dati!");
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
					
					int idSel=cellSel.getSelectedItem().get("idGiorno");
					
					AdministrationService.Util.getInstance().eliminaGiornoFestivi(idSel, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on eliminaGiornoFestivi();");
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result)
								caricaTabella();
							else
								Window.alert("Problema riscontrato durante l'accesso ai dati!");
						}
					});
				}
			});		        
		    
		    ToolBar tlbrDate= new ToolBar();
		    tlbrDate.add(dtfldGiorno);
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
			AdministrationService.Util.getInstance().getGiorniFestivi(new AsyncCallback<List<GiorniFestiviModel>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on getGiorniFestivi();");
				}

				@Override
				public void onSuccess(List<GiorniFestiviModel> result) {
					if(result==null)
						Window.alert("Problema riscontrato durante l'accesso ai dati!");
					else
						loadTable(result);
				}
			});
		}


		private void loadTable(List<GiorniFestiviModel> result) {
			store.removeAll();
			store.add(result);
			grid.reconfigure(store, cm);	
		}
		
		
		private List<ColumnConfig> createColumnsCosting() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>();
			
			ColumnConfig column = new ColumnConfig();  
		    		   
		    column = new ColumnConfig();  
		    column.setId("giorno");  
		    column.setHeader("Giorno");  
		    column.setWidth(140);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column = new ColumnConfig();  
		    column.setId("sede");  
		    column.setHeader("Sede");  
		    column.setWidth(140);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    return configs;
		}
		
	
}
