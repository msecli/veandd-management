package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.SortDir;
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
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class DialogNewCosting extends Dialog{

	private ComboBox<ClienteModel> cmbxCliente;
	private ComboBox<PersonaleModel> cmbxPM;
	private SimpleComboBox<String> smplcmbxCommessa;
	private SimpleComboBox<String> smplcmbxArea;
	private SimpleComboBox<String> smplcmbxPM;
	private TextArea txtaDescrizione;
	private Button btnHelp;
	private Button btnSave;
	private Label lblCommessa;
	private String username;
		
	public DialogNewCosting(String u){
		this.username=u;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(380);
		setHeight(500);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Dati Costing.");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		ContentPanel cntpnlCosting= new ContentPanel();
		cntpnlCosting.setHeaderVisible(false);
		cntpnlCosting.setHeight(450);
		cntpnlCosting.setFrame(true);
		cntpnlCosting.setBorders(false);
				
		LayoutContainer layout1= new LayoutContainer();
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(30);
		layout.setLabelSeparator("");
		layout.setLabelAlign(LabelAlign.TOP);
		layout1.setLayout(layout);
		layout1.setWidth(360);
		
		HorizontalPanel vp= new HorizontalPanel();
		vp.setSpacing(5);
		vp.setStyleAttribute("padding-top", "10px");
		
		ToolBar tlbTop= new ToolBar();
		tlbTop.setBorders(false);
		/*
		ListStore<PersonaleModel> storeP= new ListStore<PersonaleModel>();
		cmbxPM= new ComboBox<PersonaleModel>();
		cmbxPM.setEmptyText("Selezionare il cliente..");
		cmbxPM.setAllowBlank(false);
		cmbxPM.setStore(storeP);
		cmbxPM.setEnabled(true);
		cmbxPM.setEditable(true);
		cmbxPM.setVisible(true);
		cmbxPM.setTriggerAction(TriggerAction.ALL);
		cmbxPM.setDisplayField("nomeCompleto");
		cmbxPM.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					getNomePm();					
			}

		});*/
		
		
		smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setEmptyText("Project Manager...");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(false);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
				getNomePm();				
			}	
		});
				
		
		ListStore<ClienteModel> store=new ListStore<ClienteModel>();
		cmbxCliente= new ComboBox<ClienteModel>();
		cmbxCliente.setEmptyText("Selezionare il cliente..");
		cmbxCliente.setAllowBlank(false);
		cmbxCliente.setStore(store);
		cmbxCliente.setEnabled(true);
		cmbxCliente.setEditable(true);
		cmbxCliente.setVisible(true);
		cmbxCliente.setTriggerAction(TriggerAction.ALL);
		cmbxCliente.setDisplayField("ragioneSociale");
		cmbxCliente.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {					
					getAllClienti();					
			}	
		});
			
		lblCommessa= new Label();
		lblCommessa.setText("Commessa: ");
		
		smplcmbxArea=new SimpleComboBox<String>();
		smplcmbxArea.setAllowBlank(false);
		smplcmbxArea.setEditable(true);
		smplcmbxArea.setTriggerAction(TriggerAction.ALL);
		for(String l : DatiComboBox.getGruppoLavoro()){
			smplcmbxArea.add(l);}
		smplcmbxArea.setEmptyText("Area di Lavoro..");		
			
		smplcmbxCommessa= new SimpleComboBox<String>();
		smplcmbxCommessa.setAllowBlank(false);
		smplcmbxCommessa.setEnabled(true);
		smplcmbxCommessa.setEditable(true);
		smplcmbxCommessa.setVisible(true);
		smplcmbxCommessa.setTriggerAction(TriggerAction.ALL);
		getCommesseAperte();
				
		txtaDescrizione= new TextArea();
		txtaDescrizione.setEmptyText("Descrizione..");
		//txtaDescrizione.setStyleAttribute("padding-top", "10px");
		txtaDescrizione.setHeight(170);
		txtaDescrizione.setAllowBlank(false);
	
		btnHelp= new Button();
		btnHelp.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.question()));
		btnHelp.setIconAlign(IconAlign.TOP);
		btnHelp.setToolTip("Il numero di commessa e' generato automaticamente, ma e' possibile scegliere una commessa eventualmente gia' registrata.");
		btnHelp.setSize(24, 24);

		btnSave= new Button();
		btnSave.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirm()));
		btnSave.setIconAlign(IconAlign.TOP);
		btnSave.setToolTip("Salva");
		btnSave.setSize(24, 24);
		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
					
			@Override
			public void componentSelected(ButtonEvent ce) {							
								
				if(smplcmbxCommessa.isValid()&&cmbxCliente.isValid()&&txtaDescrizione.isValid()
						&&smplcmbxArea.isValid() && smplcmbxPM.isValid()){
					
					String commessa=smplcmbxCommessa.getRawValue().toString();
					int idCliente=cmbxCliente.getValue().get("idCliente");
					String descrizione=txtaDescrizione.getValue().toString();
					String area=smplcmbxArea.getRawValue().toString();
					String pm=smplcmbxPM.getRawValue().toString();
					
					AdministrationService.Util.getInstance().insertDataCosting(commessa, area, idCliente, descrizione, pm, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore di connessione on insertDataCosting();");
						}

						@Override
						public void onSuccess(Boolean result) {
							hide();
						}
					});		
				}else
					Window.alert("Controllare i campi inseriti!");
			}
		});
		
		tlbTop.add(btnSave);
		
		vp.add(lblCommessa);
		vp.add(smplcmbxCommessa);
		
		vp.add(btnHelp);
		
		layout1.add(smplcmbxPM, new FormData("80%"));
		layout1.add(cmbxCliente,new FormData("80%"));
		layout1.add(smplcmbxArea, new FormData("80%"));
		layout1.add(vp,new FormData("70%"));
		layout1.add(txtaDescrizione,new FormData("80%"));
		
		cntpnlCosting.add(layout1);
		cntpnlCosting.setTopComponent(tlbTop);
		
		bodyContainer.add(cntpnlCosting);
		add(bodyContainer);	
	}	
	
	private void getNomePm() {
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() { //Viene restituito nella forma Cognome Nome

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					
					smplcmbxPM.removeAll();
					smplcmbxPM.add(result);
					smplcmbxPM.recalculate();
					//smplcmbxPM.add("Tutti");
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});	
	}	
	
	private void getAllClienti() {
		AdministrationService.Util.getInstance().getListaClientiModel(new AsyncCallback<List<ClienteModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getListaClientiModel();");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(List<ClienteModel> result) {
				if(result!=null){		
					ListStore<ClienteModel> lista= new ListStore<ClienteModel>();
					lista.setStoreSorter(new StoreSorter<ClienteModel>());  
					lista.setDefaultSort("ragioneSociale", SortDir.ASC);
					
					lista.add(result);				
					cmbxCliente.clear();
					cmbxCliente.setStore(lista);
					
				}else Window.alert("error: Errore durante l'accesso ai dati Cliente.");				
			}
		});
	}
	
	
	private void getCommesseAperte() {
		AdministrationService.Util.getInstance().getCommesseAperte( new AsyncCallback<List<CommessaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();				
			}

			@Override
			public void onSuccess(List<CommessaModel> result) {
				
				List<String> listaCommesse= new ArrayList<String>();
				for(CommessaModel c:result){
					listaCommesse.add(c.getNumeroCommessa()+"."+c.getEstensione());					
				}
				//cm=result.get(result.size()-1);
				String max=listaCommesse.get(result.size()-1);
				if(result!=null){
					smplcmbxCommessa.removeAll();
					java.util.Collections.sort(listaCommesse);
					smplcmbxCommessa.add(listaCommesse);
					smplcmbxCommessa.recalculate();
					//smplcmbxCommessa.reset();
					smplcmbxCommessa.setSimpleValue(max);
				}
			}
		});
	}
}
