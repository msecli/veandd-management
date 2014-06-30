package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.AnagraficaHardwareModel;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.model.RiepilogoRichiesteModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DialogRichiestaHardwareDipendente extends Dialog{

	private ListStore<RiepilogoRichiesteModel>store = new ListStore<RiepilogoRichiesteModel>();
	private Grid<RiepilogoRichiesteModel> gridRiepilogo;
	private ColumnModel cm;
	private RowExpander expander;
	
	private DateField dtfldGiorno= new DateField();
	private TimeField tmfldOra= new TimeField();
	private ComboBox<AnagraficaHardwareModel> cmbxPc= new ComboBox<AnagraficaHardwareModel>();
	private TextArea txtrGuasto= new TextArea();
	private Button btnConferma= new Button();
	
	private final String username;
	
	public DialogRichiestaHardwareDipendente(final String username){
		this.username=username;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(1100);
		setHeight(550);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Richieste IT");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		HorizontalPanel hp= new HorizontalPanel();
		hp.setSpacing(2);
		
		ContentPanel cntpnlForm= new ContentPanel();
		cntpnlForm.setHeaderVisible(false);
		cntpnlForm.setHeight(490);
		cntpnlForm.setWidth(300);
		cntpnlForm.setFrame(true);
		cntpnlForm.setBorders(false);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(490);
		cntpnlGrid.setWidth(750);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setBorders(false);
		cntpnlGrid.setLayout(new FitLayout());
		
		cm=new ColumnModel(createColumns());
		caricaDatiTabella();
			          
		gridRiepilogo= new Grid<RiepilogoRichiesteModel>(store, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.getView().setShowDirtyCells(false);
		gridRiepilogo.addPlugin(expander);
		//gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		/*gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoRichiesteModel>>() {  
	          public void handleEvent(SelectionChangedEvent<RiepilogoRichiesteModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	            	String guasto = be.getSelectedItem().get("guasto");
	            	String pc= be.getSelectedItem().get("nodo");
	            	Date dataR=be.getSelectedItem().get("dataRichiesta");
	            	String ora= be.getSelectedItem().get("oraRichiesta");
	            	String stato= be.getSelectedItem().get("stato");
	            	
	            	txtrGuasto.setValue(guasto);
	            	smplcmbxPc.setRawValue(pc);
	            	dtfldGiorno.setValue(dataR);
					
	            } else {  
	              
	            }
	          }   
	    }); */
		
		dtfldGiorno.setValue(new Date());
		dtfldGiorno.setWidth(120);
		dtfldGiorno.setAllowBlank(false);
		
		tmfldOra.setValue(new Time(10, 00));
		tmfldOra.setWidth(80);
		tmfldOra.setEmptyText("Orario..");
		tmfldOra.setAllowBlank(false);
		
		ListStore<AnagraficaHardwareModel> listStoreCmbx= new ListStore<AnagraficaHardwareModel>();
		cmbxPc.setEmptyText("Selezionare PC..");
		cmbxPc.setAllowBlank(false);
		cmbxPc.setWidth(120);
		cmbxPc.setTriggerAction(TriggerAction.ALL);
		cmbxPc.setAllowBlank(false);
		cmbxPc.setStore(listStoreCmbx);
		cmbxPc.setDisplayField("nodo");
		cmbxPc.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				AdministrationService.Util.getInstance().getDatiAnagraficaHardware(new AsyncCallback<List<AnagraficaHardwareModel>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on getDatiAnagraficaHardware()");						
					}

					@Override
					public void onSuccess(List<AnagraficaHardwareModel> result) {
						if(result!=null)
							caricaDatiComboBox(result);
						else
							Window.alert("Problemi durante l'accesso ai dati dell'anagrafica hardware!");
						
					}				
				});
			}
		});
				
		txtrGuasto.setWidth(270);
		txtrGuasto.setHeight(200);
		txtrGuasto.setEmptyText("Inserire qui la richiesta..");
		txtrGuasto.setAllowBlank(false);
		
		btnConferma.setToolTip("Richieste");
		btnConferma.setHeight(35);
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirmBig()));
		btnConferma.setIconAlign(IconAlign.BOTTOM);
		btnConferma.setWidth("50%");
		btnConferma.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				Date dataR= dtfldGiorno.getValue();
				String ora= tmfldOra.getValue().getText();
				Integer pc= cmbxPc.getValue().get("idHardware");
				String richiesta=txtrGuasto.getValue();
				//tmfldOra.get			
				AdministrationService.Util.getInstance().insertRichiestaIt(username, dataR, ora, pc, richiesta, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore di connessione on getRiepilogoRichiesteItUtente();");
					}

					@Override
					public void onSuccess(Boolean result) {									
						if(result){
							Window.alert("Richiesta registrata!");
							caricaDatiTabella();
						}
						else 
							Window.alert("error: Errore durante la registrazione della richiesta");			
					}
				}); 	
			}
		});
		
		VerticalPanel vpForm= new VerticalPanel();
		vpForm.setSpacing(5);
		
		vpForm.add(dtfldGiorno);
		vpForm.add(tmfldOra);
		vpForm.add(cmbxPc);
		vpForm.add(txtrGuasto);
		vpForm.add(btnConferma);

		cntpnlForm.add(vpForm);	
		cntpnlGrid.add(gridRiepilogo);
		
		hp.add(cntpnlForm);
		hp.add(cntpnlGrid);
		
		bodyContainer.add(hp);
		
		add(bodyContainer);
	}

	private void caricaDatiTabella() {
		AdministrationService.Util.getInstance().getRiepilogoRichiesteItUtente(username, new AsyncCallback<List<RiepilogoRichiesteModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore di connessione on getRiepilogoRichiesteItUtente();");
			}

			@Override
			public void onSuccess(List<RiepilogoRichiesteModel> result) {									
				if(result!=null){
					loadTable(result);
				}				
				else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		}); 	  
	}
	
	private void caricaDatiComboBox(List<AnagraficaHardwareModel> result) {
		ListStore<AnagraficaHardwareModel> lista= new ListStore<AnagraficaHardwareModel>();
		lista.setStoreSorter(new StoreSorter<AnagraficaHardwareModel>());  
		lista.setDefaultSort("codiceModello", SortDir.ASC);
		
		lista.add(result);				
		cmbxPc.clear();
		cmbxPc.setStore(lista);
	}
	
	private void loadTable(List<RiepilogoRichiesteModel> result) {		
		store.removeAll();
		store.add(result);
		gridRiepilogo.reconfigure(store, cm);		
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		XTemplate tpl = XTemplate.create("<p><b>Richiesta:</b> {guasto}</p><br>");  	    
		expander = new RowExpander();
		expander.setTemplate(tpl); 
		expander.setWidth(20);
				
		configs.add(expander);		
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("dataRichiesta");  
	    column.setHeader("Data Richiesta");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("nodo");  
	    column.setHeader("PC");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    	    
	    column=new ColumnConfig();		
	    column.setId("oraRichiesta");  
	    column.setHeader("Ora Richiesta");  
	    column.setWidth(90);  
	    column.setRowHeader(true);
	    configs.add(column);
	    
	    column=new ColumnConfig();		
	    column.setId("dataEvasioneRichiesta");  
	    column.setHeader("Data Evasione");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    configs.add(column);
	    
	    /*column=new ColumnConfig();		
	    column.setId("guasto");  
	    column.setHeader("Richiesta");  
	    column.setWidth(220);  
	    column.setRowHeader(true);  
	    configs.add(column);*/
	    
	    column=new ColumnConfig();		
	    column.setId("stato");  
	    column.setHeader("Stato");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column);
		
		return configs;
	}	
}