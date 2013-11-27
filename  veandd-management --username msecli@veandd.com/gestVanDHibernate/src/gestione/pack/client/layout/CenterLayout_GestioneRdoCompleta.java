package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.layout.panel.DialogAssociaCommessaToOrdine;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.TariffaOrdineModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.ToolbarSeparator;

public class CenterLayout_GestioneRdoCompleta extends LayoutContainer{

	public CenterLayout_GestioneRdoCompleta(){}
	
	private TextField<String> txtfldNumeroRda;
	private TextField<String> txtfldIdRda=new TextField<String>();	
	private TextField<String> txtfldNumeroOfferta;
	private TextArea txtrDescrizione;
	private TextField<String> txtfldElementoWbs;
	private TextField<String> txtfldConto;
	private TextField<String> txtfldPrCenter;
	private TextField<String> txtfldBem;
	
	private TextField<String> txtfldImporto;
	private DateField dtfldDataOfferta;	
	private TextField<String> txtfldNumeroOrdine;
	private TextField<String> txtfldTariffaOraria;
	private DateField dtfldDataInizioOrdine;
	private DateField dtfldDataFineOrdine;
	private TextField<String> txtfldNumeroRisorse;
	private TextField<String> txtfldNumeroOre;
	private TextField<String> txtfldNumeroOreResidue;
	private TextField<String> txtfldImportoOrdine;
	private TextField<String> txtfldImportoResiduoOrdine;
	private SimpleComboBox<String> smplcmbxCliente = new SimpleComboBox<String>();
	
	private VerticalPanel hpLayout;
	
	private GroupingStore<RdoCompletaModel>store = new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeCompleto= new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeResult= new GroupingStore<RdoCompletaModel>();
	private List<RdoCompletaModel> listaStore= new ArrayList<RdoCompletaModel>();
	private Grid<RdoCompletaModel> gridRiepilogo;
	private ColumnModel cm;
	
	private LayoutContainer layoutCol1=new LayoutContainer();
	private LayoutContainer layoutCol2=new LayoutContainer();
	private LayoutContainer layoutCol3=new LayoutContainer();
	
	private Button btnSave;
	private Button btnEdit;
	private Button btnReset;
	private Button btnDelete;
	
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
				
		hpLayout= new VerticalPanel();
		hpLayout.setSpacing(10);
		hpLayout.setItemId("hpLayout");
				
		ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeading("Dati Rdo.");
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setWidth(970);
		cntpnlLayout.setHeight(980);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setButtonAlign(HorizontalAlignment.CENTER);
		//cntpnlLayout.setStyleAttribute("padding-left", "7px");
		//cntpnlLayout.setStyleAttribute("margin-top", "15px");
		
		btnSave= new Button("Save");
		btnEdit=new Button("Edit");
		btnEdit.setEnabled(false);
		btnDelete=new Button("Delete");
		btnDelete.setEnabled(false);
		btnReset=new Button("X");
		
		btnSave.setToolTip("Salva i dati.");
		btnSave.setWidth("65px");
		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				String numRdo="#";
				String cliente= new String();
				
				String numOfferta="#";
				Date dataOfferte=null;
				String importo="0.0";
				
				String numOrdine="#";
				String descrizione="#";
				String elementoWbs="";
				String conto="";
				String prCenter="";
				String bem="";
				Date dataInizio=null;
				Date dataFine=null;
				String tariffa="0.0";
				String numRisorse="0";
				String oreDisp="0.0";
				String oreRes="0.0";
				String importoOrdine="0.00";
				String importoResiduoOrdine="0.00";
				
				
				CntpnlFormRdo cpfrm=(CntpnlFormRdo) hpLayout.getItemByItemId("cpfrm");
				ContentPanel cp=(ContentPanel) cpfrm.getItemByItemId("cp");
				LayoutContainer layout=(LayoutContainer) cp.getItemByItemId("layout");
				CntpnlGridTariffeOrdine cpTariffa=(CntpnlGridTariffeOrdine) layout.getItemByItemId("cpTariffa");
				List<TariffaOrdineModel> listaTar= (List<TariffaOrdineModel>) cpTariffa.storeTariffe.getModels();
				
				if(formIsValid()&&!arePresent()&&areValid(listaTar)){
				
					cliente=smplcmbxCliente.getRawValue().toString();
					if(!txtfldNumeroRda.getRawValue().isEmpty())numRdo=txtfldNumeroRda.getValue().toString();
					
					if(!txtfldNumeroOfferta.getRawValue().isEmpty())numOfferta=txtfldNumeroOfferta.getValue().toString();
					if(!txtfldImporto.getRawValue().isEmpty())importo=txtfldImporto.getValue().toString();
					if(!dtfldDataOfferta.getRawValue().isEmpty())dataOfferte=dtfldDataOfferta.getValue();
					
					if(!txtfldNumeroOrdine.getRawValue().isEmpty())numOrdine=txtfldNumeroOrdine.getValue().toString();	
					if(!txtrDescrizione.getRawValue().isEmpty())descrizione=txtrDescrizione.getValue().toString();
					if(!txtfldElementoWbs.getRawValue().isEmpty())elementoWbs=txtfldElementoWbs.getValue().toString();
					if(!txtfldConto.getRawValue().isEmpty())conto=txtfldConto.getValue().toString();
					if(!txtfldPrCenter.getRawValue().isEmpty())prCenter=txtfldPrCenter.getValue().toString();
					if(!txtfldBem.getRawValue().isEmpty())bem=txtfldBem.getValue().toString();
					if(!dtfldDataInizioOrdine.getRawValue().isEmpty())dataInizio=dtfldDataInizioOrdine.getValue();
					if(!dtfldDataFineOrdine.getRawValue().isEmpty())dataFine=dtfldDataFineOrdine.getValue();
					if(!txtfldTariffaOraria.getRawValue().isEmpty())tariffa=txtfldTariffaOraria.getValue().toString();
					if(!txtfldNumeroRisorse.getRawValue().isEmpty())numRisorse=txtfldNumeroRisorse.getValue().toString();
					if(!txtfldNumeroOre.getRawValue().isEmpty())oreDisp=txtfldNumeroOre.getValue().toString();
					if(!txtfldNumeroOreResidue.getRawValue().isEmpty())oreRes=txtfldNumeroOreResidue.getValue().toString();
					if(!txtfldImportoOrdine.getRawValue().isEmpty())importoOrdine=txtfldImportoOrdine.getValue().toString();
					if(!txtfldImportoResiduoOrdine.getRawValue().isEmpty())importoResiduoOrdine=txtfldImportoResiduoOrdine.getValue().toString();
					
					AdministrationService.Util.getInstance().saveRdoCompleta(numRdo, cliente, numOfferta, dataOfferte, importo, numOrdine, descrizione,
							elementoWbs, conto, prCenter, bem
							, dataInizio, dataFine, tariffa, numRisorse, oreDisp, oreRes, listaTar, importoOrdine, importoResiduoOrdine, new AsyncCallback<Boolean>() {
					
						@Override
						public void onSuccess(Boolean result) {
							if(result){
								Window.alert("Inserimento avvenuto con successo.");
								caricaTabellaDati();
								resetForm();
							}else{
								Window.alert("error: Impossibile inserire i dati dell'Rdo!");
							}		
						}			

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione on saveRdoCompleta();");
							caught.printStackTrace();
					
						}
					}); //AsyncCallback	   
				}else{
					Window.alert("Controllare i campi inseriti ed eliminare eventuali record vuoti nella tabella Tariffe.");
				}
			}

		});
		
		btnEdit.setToolTip("Modifica i dati.");
		btnEdit.setWidth("65px");
		btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				String numRdo="#";
				String cliente= new String();
				int idRdo;
				String numOfferta="#";
				Date dataOfferte= null;
				String importo="0.0";
				
				String numOrdine="#";
				String descrizione="#";
				String elementoWbs="";
				String conto="";
				String prCenter="";
				String bem="";
				
				Date dataInizio= null;
				Date dataFine=null;
				String tariffa="0.0";
				String numRisorse="0";
				String oreDisp="0.0";
				String oreRes="0.0";
				String importoOrdine="0.00";
				String importoResiduoOrdine="0.00";
				
				CntpnlFormRdo cpfrm=(CntpnlFormRdo) hpLayout.getItemByItemId("cpfrm");
				ContentPanel cp=(ContentPanel) cpfrm.getItemByItemId("cp");
				LayoutContainer layout=(LayoutContainer) cp.getItemByItemId("layout");
				CntpnlGridTariffeOrdine cpTariffa=(CntpnlGridTariffeOrdine) layout.getItemByItemId("cpTariffa");
				//List<TariffaOrdineModel> listaTar= elaboroStoreTariffe((List<TariffaOrdineModel>) cpTariffa.storeTariffe.getModels());
				List<TariffaOrdineModel> listaTar=(List<TariffaOrdineModel>) cpTariffa.storeTariffe.getModels();
			
				if(formIsValid()&&areValid(listaTar)){
					idRdo=Integer.valueOf(txtfldIdRda.getValue());
					cliente=smplcmbxCliente.getRawValue().toString();
					if(!txtfldNumeroRda.getRawValue().isEmpty())numRdo=txtfldNumeroRda.getValue().toString();
					
					if(!txtfldNumeroOfferta.getRawValue().isEmpty())numOfferta=txtfldNumeroOfferta.getValue().toString();
					if(!txtfldImporto.getRawValue().isEmpty())importo=txtfldImporto.getValue().toString();
					if(!dtfldDataOfferta.getRawValue().isEmpty())dataOfferte=dtfldDataOfferta.getValue();
					
					if(!txtfldNumeroOrdine.getRawValue().isEmpty())numOrdine=txtfldNumeroOrdine.getValue().toString();	
					if(!txtrDescrizione.getRawValue().isEmpty())descrizione=txtrDescrizione.getValue().toString();
					if(!txtfldElementoWbs.getRawValue().isEmpty())elementoWbs=txtfldElementoWbs.getValue().toString();
					if(!txtfldConto.getRawValue().isEmpty())conto=txtfldConto.getValue().toString();
					if(!txtfldPrCenter.getRawValue().isEmpty())prCenter=txtfldPrCenter.getValue().toString();
					if(!txtfldBem.getRawValue().isEmpty())bem=txtfldBem.getValue().toString();
					
					if(!dtfldDataInizioOrdine.getRawValue().isEmpty())dataInizio=dtfldDataInizioOrdine.getValue();
					if(!dtfldDataFineOrdine.getRawValue().isEmpty())dataFine=dtfldDataFineOrdine.getValue();
					if(!txtfldTariffaOraria.getRawValue().isEmpty())tariffa=txtfldTariffaOraria.getValue().toString();
					if(!txtfldNumeroRisorse.getRawValue().isEmpty())numRisorse=txtfldNumeroRisorse.getValue().toString();
					if(!txtfldNumeroOre.getRawValue().isEmpty())oreDisp=txtfldNumeroOre.getValue().toString();
					if(!txtfldNumeroOreResidue.getRawValue().isEmpty())oreRes=txtfldNumeroOreResidue.getValue().toString();
					if(!txtfldImportoOrdine.getRawValue().isEmpty())importoOrdine=txtfldImportoOrdine.getValue().toString();
					if(!txtfldImportoResiduoOrdine.getRawValue().isEmpty())importoResiduoOrdine=txtfldImportoResiduoOrdine.getValue().toString();
									
					AdministrationService.Util.getInstance().editRdoCompleta(idRdo, numRdo, cliente, numOfferta, dataOfferte, importo, numOrdine, descrizione,
							elementoWbs, conto, prCenter, bem
							, dataInizio, dataFine, tariffa, numRisorse, oreDisp, oreRes, listaTar, importoOrdine, importoResiduoOrdine, new AsyncCallback<Boolean>() {
					
						@Override
						public void onSuccess(Boolean result) {
							if(result){
								Window.alert("Modifica effettuata.");
								caricaTabellaDati();
								resetForm();
							
							}else{
								Window.alert("error: Impossibile effettuare la modifica!");
							}				
						}			

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione on editRdoCompleta();");
							caught.printStackTrace();
					
						}
					}); //AsyncCallback	   
				}else{
					Window.alert("Controllare i campi inseriti ed eliminare eventuali record vuoti nella tabella Tariffe.");
				}
			}
/*
			private List<TariffaOrdineModel> elaboroStoreTariffe(List<TariffaOrdineModel> models) {
				
				List<TariffaOrdineModel> listaT= new ArrayList<TariffaOrdineModel>();
				TariffaOrdineModel tar=new TariffaOrdineModel();
				
				Integer id;
				String tariffa;
				String descrizione;
				
				for(TariffaOrdineModel t:models){
					
					id=t.get("idAttivitaOrdine");
					tariffa=t.get("tariffaAttivita");
					descrizione=t.get("descrizione");
					
					if(id==null)
						id=0;
					
					tar=new TariffaOrdineModel(String.valueOf(id), tariffa, descrizione);
					
				}
				
				return listaT;
			}
			
			*/
		});
		
		btnDelete.setToolTip("Elimina i dati.");
		btnDelete.setWidth("65px");
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				int idRdo;
				idRdo=Integer.valueOf(txtfldIdRda.getValue().toString());
				AdministrationService.Util.getInstance().deleteRdoCompleta(idRdo, new AsyncCallback<Boolean>() {
				
					@Override
					public void onSuccess(Boolean result) {
						if(result){
							Window.alert("Eliminazione avvenuta con successo.");
							caricaTabellaDati();
							resetForm();
						
						}else{
							Window.alert("error: Impossibile eliminare i dati dell'Rdo!");
						}				
					}			

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on deleteRdoCompleta();");
						caught.printStackTrace();			
					}
				}); //AsyncCallback	 
			}
		});
		
		btnReset.setToolTip("Azzera i campi compilati.");
		btnReset.setWidth("25px");
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {				
				btnSave.setEnabled(true);
            	smplcmbxCliente.setEnabled(true);
            	btnEdit.setEnabled(false);
            	btnDelete.setEnabled(false);
            	resetForm();
			}
		});
		
		ToolBar tlbr= new ToolBar();
		tlbr.setAlignment(HorizontalAlignment.RIGHT);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnSave);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnEdit);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnDelete);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnReset);
		tlbr.add(new SeparatorToolItem());
		cntpnlLayout.setTopComponent(tlbr);
		/*
		cntpnlLayout.addButton(btnSave);
		cntpnlLayout.addButton(btnEdit);
		cntpnlLayout.addButton(btnDelete);
		cntpnlLayout.addButton(btnReset);
		*/
		txtfldIdRda.setFieldLabel("id");
		hpLayout.add(new CntpnlFormRdo());
		hpLayout.add(new CntpnlGridRdo());
		
		cntpnlLayout.add(hpLayout);	
		
	    bodyContainer.add(cntpnlLayout);    
	   				
		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		add(layoutContainer);		
	}
	
	private boolean areValid(List<TariffaOrdineModel> listaTar) {
		String tariffa;
		for(TariffaOrdineModel t:listaTar){
			
			tariffa=(String)t.get("tariffaAttivita");			
			if(tariffa==null)
				return false;
		}
		
		return true;
	}
	
	
	private class CntpnlFormRdo extends ContentPanel{
		
		public CntpnlFormRdo() {
		
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(940);
			setHeight(315);
			setFrame(false);
			setStyleAttribute("margin-top", "0px");
			setItemId("cpfrm");
						
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(920, 330);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setItemId("cp");
						
			layoutCol2.setStyleAttribute("padding-left", "20px");
			
			layoutCol3.setItemId("layout");
			layoutCol3.setStyleAttribute("padding-left", "5px");
			
			FormLayout layout= new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol1.setLayout(layout);
					
			layout= new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol2.setLayout(layout);
			
			layout= new FormLayout();
			layout.setLabelWidth(90);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol3.setLayout(layout);
						
			smplcmbxCliente.setFieldLabel("Cliente");
			smplcmbxCliente.setName("cliente");
			smplcmbxCliente.setAllowBlank(false);
			try {
				getClienti();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			txtfldNumeroRda= new TextField<String>();
			txtfldNumeroRda.setFieldLabel("Numero RDO");
			txtfldNumeroRda.setName("numeroRdo");
			txtfldNumeroRda.setMaxLength(20);
			txtfldNumeroRda.setAllowBlank(true);
			
			txtrDescrizione=new TextArea();
			txtrDescrizione.setMaxLength(200);
			txtrDescrizione.setFieldLabel("Descrizione");
			txtrDescrizione.setName("descrizione");
			txtrDescrizione.setHeight("130");
			
			txtfldElementoWbs= new TextField<String>();
			txtfldElementoWbs.setFieldLabel("Elemento WBS");
			txtfldElementoWbs.setName("elementoWbs");
			txtfldElementoWbs.setMaxLength(35);
			txtfldElementoWbs.setAllowBlank(true);
			
			txtfldConto= new TextField<String>();
			txtfldConto.setFieldLabel("Conto");
			txtfldConto.setName("conto");
			txtfldConto.setMaxLength(25);
			txtfldConto.setAllowBlank(true);
			
			txtfldPrCenter= new TextField<String>();
			txtfldPrCenter.setFieldLabel("Pr Center");
			txtfldPrCenter.setName("prCenter");
			txtfldPrCenter.setMaxLength(25);
			txtfldPrCenter.setAllowBlank(true);
			
			txtfldBem= new TextField<String>();
			txtfldBem.setFieldLabel("BEM");
			txtfldBem.setName("bem");
			txtfldBem.setMaxLength(25);
			txtfldBem.setAllowBlank(true);
			
			layoutCol1.add(smplcmbxCliente, new FormData("100%"));
			layoutCol1.add(txtfldNumeroRda, new FormData("80%"));
			layoutCol1.add(txtrDescrizione, new FormData("100%"));
			layoutCol1.add(txtfldElementoWbs, new FormData("80%"));
			layoutCol1.add(txtfldConto, new FormData("80%"));
			layoutCol1.add(txtfldPrCenter, new FormData("80%"));
			layoutCol1.add(txtfldBem, new FormData("80%"));
			
			txtfldNumeroOfferta= new TextField<String>();
			txtfldNumeroOfferta.setFieldLabel("Numero Offerta");
			txtfldNumeroOfferta.setName("numeroOfferta");
			txtfldNumeroOfferta.setMaxLength(20);
			txtfldNumeroOfferta.setAllowBlank(true);
			
			dtfldDataOfferta=new DateField();
			dtfldDataOfferta.setFieldLabel("Data Offerta");
			dtfldDataOfferta.setAllowBlank(true);
			
			txtfldImporto=new TextField<String>();
			txtfldImporto.setFieldLabel("Importo(\u20AC)");
			txtfldImporto.setName("importo");
			txtfldImporto.setRegex("[0-9]+[.][0-9]+|[0-9]+");
			txtfldImporto.getMessages().setRegexText("Deve essere un numero, eventualmente nel formato 99.99");		
			
			layoutCol2.add(txtfldNumeroOfferta, new FormData("85%"));
			layoutCol2.add(dtfldDataOfferta, new FormData("85%"));
			layoutCol2.add(txtfldImporto, new FormData("65%"));
			
			
			txtfldNumeroOrdine=new TextField<String>();
			txtfldNumeroOrdine.setFieldLabel("Numero Ordine");
			txtfldNumeroOrdine.setName("numeroOrdine");
			txtfldNumeroOrdine.setMaxLength(25);
			txtfldNumeroOrdine.setAllowBlank(true);
			txtfldNumeroOrdine.setEnabled(true);
			
			dtfldDataInizioOrdine=new DateField();
			dtfldDataInizioOrdine.setFieldLabel("Data Inizio");
			dtfldDataInizioOrdine.setAllowBlank(true);
			
			dtfldDataFineOrdine=new DateField();
			dtfldDataFineOrdine.setFieldLabel("Data Fine");
			dtfldDataFineOrdine.setAllowBlank(true);
			
			txtfldTariffaOraria=new TextField<String>();
			txtfldTariffaOraria.setFieldLabel("Tariffa Oraria");
			txtfldTariffaOraria.setName("tariffaOraria");
			txtfldTariffaOraria.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldTariffaOraria.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldTariffaOraria.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldTariffaOraria.getValue()==null)
								txtfldTariffaOraria.setValue("0.00");
							else{
								String valore= txtfldTariffaOraria.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldTariffaOraria.setValue(valore);
							}						
						}
				 }
			});
			
			txtfldNumeroRisorse=new TextField<String>();
			txtfldNumeroRisorse.setFieldLabel("Num. Risorse");
			txtfldNumeroRisorse.setName("numeroRisorse");
			txtfldNumeroRisorse.setRegex("[0-9]*");
			txtfldNumeroRisorse.getMessages().setRegexText("Deve essere un numero");
			
			txtfldNumeroOre=new TextField<String>();
			txtfldNumeroOre.setWidth(120);
			txtfldNumeroOre.setToolTip("Numero Ore");
			txtfldNumeroOre.setEmptyText("Ore...");
			txtfldNumeroOre.setName("numeroOre");
			txtfldNumeroOre.setRegex("[0-9]+[.][0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldNumeroOre.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldNumeroOre.addKeyListener(new KeyListener(){
				
			/*	public void componentKeyUp(ComponentEvent event) {				
					txtfldNumeroOreResidue.setValue(txtfldNumeroOre.getValue());		
				}
				*/
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldNumeroOre.getValue()==null)
								txtfldNumeroOre.setValue("0.00");
							else{
								String valore= txtfldNumeroOre.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldNumeroOre.setValue(valore);
								txtfldNumeroOreResidue.setValue(valore);
							}						
						}
				 }
			});
			
			txtfldNumeroOreResidue=new TextField<String>();
			txtfldNumeroOreResidue.setWidth(120);
			txtfldNumeroOreResidue.setStyleAttribute("padding-left", "5px");
			txtfldNumeroOreResidue.setEmptyText("Ore Residue...");
			txtfldNumeroOreResidue.setName("numeroOreResidue");
			txtfldNumeroOreResidue.setToolTip("Ore Residue");
			txtfldNumeroOreResidue.setRegex("[0-9]+[.][0-5]{1}[0-9]{1}|0.00|0.0");
			txtfldNumeroOreResidue.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldNumeroOreResidue.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldNumeroOreResidue.getValue()==null)
								txtfldNumeroOreResidue.setValue("0.00");
							else{
								String valore= txtfldNumeroOreResidue.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldNumeroOreResidue.setValue(valore);
							}						
						}
				 }
			});
			
			txtfldImportoOrdine= new TextField<String>();
			txtfldImportoOrdine.setWidth(120);
			txtfldImportoOrdine.setEmptyText("Importo...");
			txtfldImportoOrdine.setToolTip("Importo Ordine");
			txtfldImportoOrdine.setRegex("[0-9]+[.][0-9]{1}[0-9]{1}|0.00|0.0");
			//txtfldImportoOrdine.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldImportoOrdine.addKeyListener(new KeyListener(){
				
					 public void componentKeyDown(ComponentEvent event) { 	  
					    	int keyCode=event.getKeyCode();
							if(keyCode==9){			
								
								if(txtfldImportoOrdine.getValue()==null)
									txtfldImportoOrdine.setValue("0.00");
								else{
									String valore= txtfldImportoOrdine.getValue().toString();
															
									if(valore.compareTo("")==0)
										valore ="0.00";
									else
										if(valore.indexOf(".")==-1)
											valore=valore+".00";
										else{
											int index=valore.indexOf(".");
											int length=valore.length();
											
											if(valore.substring(index+1, length).length()==1)
												valore=valore+"0";		
											else if(valore.substring(index+1, length).length()==0)
												valore=valore+"00";
										}
									txtfldImportoOrdine.setValue(valore);
									txtfldImportoResiduoOrdine.setValue(valore);
								}						
							}
					 }
				});
			
			txtfldImportoResiduoOrdine=new TextField<String>();
			txtfldImportoResiduoOrdine.setWidth(120);
			txtfldImportoResiduoOrdine.setEmptyText("Importo Residuo...");
			txtfldImportoResiduoOrdine.setStyleAttribute("padding-left", "5px");
			txtfldImportoResiduoOrdine.setToolTip("Importo Residuo");
			txtfldImportoResiduoOrdine.setRegex("[0-9]+[.][0-9]{1}[0-9]{1}|0.00|0.0");
			txtfldImportoResiduoOrdine.addKeyListener(new KeyListener(){
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldImportoResiduoOrdine.getValue()==null)
								txtfldImportoResiduoOrdine.setValue("0.00");
							else{
								String valore= txtfldImportoResiduoOrdine.getValue().toString();
														
								if(valore.compareTo("")==0)
									valore ="0.00";
								else
									if(valore.indexOf(".")==-1)
										valore=valore+".00";
									else{
										int index=valore.indexOf(".");
										int length=valore.length();
										
										if(valore.substring(index+1, length).length()==1)
											valore=valore+"0";		
										else if(valore.substring(index+1, length).length()==0)
											valore=valore+"00";
									}
								txtfldImportoResiduoOrdine.setValue(valore);
							}						
						}
				 }
			});
			
			HorizontalPanel hp1= new HorizontalPanel();
			hp1.setSpacing(2);
			hp1.add(txtfldImportoOrdine);
			hp1.add(txtfldImportoResiduoOrdine);
			
			HorizontalPanel hp2=new HorizontalPanel();
			hp2.setSpacing(2);
			hp2.add(txtfldNumeroOre);
			hp2.add(txtfldNumeroOreResidue);
			
			layoutCol3.add(txtfldNumeroOrdine,new FormData("85%"));
			layoutCol3.add(dtfldDataInizioOrdine,new FormData("85%"));
			layoutCol3.add(dtfldDataFineOrdine, new FormData("85%"));
			//layoutCol3.add(txtfldTariffaOraria, new FormData("60%"));
			layoutCol3.add(txtfldNumeroRisorse, new FormData("60%"));
			layoutCol3.add(hp2);
			layoutCol3.add(hp1);			
			layoutCol3.add(new CntpnlGridTariffeOrdine(),new FormData("85%"));
			
			RowData data = new RowData(.35, 1);
			data.setMargins(new Margins(5));
			
			cp.add(layoutCol1, data);
			cp.add(layoutCol2, data);
			cp.add(layoutCol3, data);
			
			add(cp);			
		}
		
		public void getClienti(){
			
			AdministrationService.Util.getInstance().getRagioneSociale(new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione;");
					caught.printStackTrace();				
				}
				@Override
				public void onSuccess(List<String> result) {
					if(result!=null){
						java.util.Collections.sort(result);
						smplcmbxCliente.add(result);
						smplcmbxCliente.recalculate();}
					else Window.alert("error: Errorre durante l'accesso ai dati Clienti.");					
				}
			});		
		}	
	}
	
	
	private class CntpnlGridTariffeOrdine extends ContentPanel{
		
		private Button btnAddTariffa;
		private Button btnDelTariffa;
		//private Button btnConferma;
		private TextField<String> txtfldTariffa;
		
		private EditorGrid<TariffaOrdineModel> gridTariffa;
		private CellSelectionModel<TariffaOrdineModel> cs;
		private ListStore<TariffaOrdineModel> storeTariffe=new ListStore<TariffaOrdineModel>();
		private ColumnModel cmTariffe;
		
		public CntpnlGridTariffeOrdine(){
			
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.AUTO);	
			setWidth(150);
			setHeight(150);
			setFrame(true);
			setLayout(new FitLayout());
			setItemId("cpTariffa");
			setStyleAttribute("padding-top", "8px");
			
			cs=new CellSelectionModel<TariffaOrdineModel>();
		    cs.setSelectionMode(SelectionMode.SIMPLE);
	   
		    cmTariffe=new ColumnModel(createColumns());
		    gridTariffa= new EditorGrid<TariffaOrdineModel>(storeTariffe, cmTariffe);
		    gridTariffa.setBorders(false);
		    gridTariffa.setItemId("grid");
		    gridTariffa.setStripeRows(true); 
		    gridTariffa.setColumnLines(true);
		    gridTariffa.setSelectionModel(cs);
		    //TariffaOrdineModel tm=new TariffaOrdineModel();
		    //storeTariffe.insert(tm, 0);//inserisco una riga vuota pronta per l'inserimento
		    //gridTariffa.startEditing(storeTariffe.indexOf(tm), 0);
		    		    
		    btnAddTariffa= new Button();
		    btnAddTariffa.setSize(26, 26);
		    btnAddTariffa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.add()));
		    btnAddTariffa.setIconAlign(IconAlign.BOTTOM);
		    btnAddTariffa.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					TariffaOrdineModel tm=new TariffaOrdineModel();
					gridTariffa.stopEditing();
				    storeTariffe.insert(tm, 0);//inserisco una riga vuota per l'inserimento
				    gridTariffa.startEditing(storeTariffe.indexOf(tm), 0);
				}
			});
		    
		    btnDelTariffa= new Button();
		    btnDelTariffa.setSize(26, 26);
		    btnDelTariffa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.respingi()));
		    btnDelTariffa.setIconAlign(IconAlign.BOTTOM);
		    btnDelTariffa.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					TariffaOrdineModel trf=	cs.getSelectedItem();
					storeTariffe.remove(trf);					
				}
			});
		    
		    		    				
			ToolBar tlbGrid= new ToolBar();
			tlbGrid.add(btnAddTariffa);
			tlbGrid.add(new SeparatorToolItem());
			tlbGrid.add(btnDelTariffa);
			tlbGrid.add(new SeparatorToolItem());
			//tlbGrid.add(btnConferma);	    
			//tlbGrid.add(new SeparatorToolItem());
			
			setTopComponent(tlbGrid);
			add(gridTariffa);		    
		}
		
		
		public void caricaTabella(int idRdo){
			
			AdministrationService.Util.getInstance().loadTariffePerOrdine(idRdo, new AsyncCallback<List<TariffaOrdineModel>>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore di connessione on loadTariffePerOrdine()");			
				}

				@Override
				public void onSuccess(List<TariffaOrdineModel> result) {
					loadTable(result);					
				}			
			});		
		}
		
		private void loadTable(List<TariffaOrdineModel> result) {
			try {
				storeTariffe.removeAll();
				storeTariffe.add(result);
				gridTariffa.reconfigure(storeTariffe, cmTariffe);	    		    	
			} catch (NullPointerException e) {
				Window.alert("error: Impossibile effettuare il caricamento dati nella tabella Tariffe.");
					e.printStackTrace();
			}
		}		
		
		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			CellEditor editorTxt;
			
			ColumnConfig column = new ColumnConfig();  
		    column.setId("tariffaAttivita");  
		    column.setHeader("Tariffa");  
		    column.setWidth(60);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);	    
		    txtfldTariffa= new TextField<String>();
		    txtfldTariffa.setRegex("^([0-9]{1}|[0-9][0-9])[.]{1}(0|00|[0-9]{2})$");
		    txtfldTariffa.getMessages().setRegexText("Deve essere un numero!");
		    txtfldTariffa.setValue("0.00");
		    txtfldTariffa.setAllowBlank(false);		    
		    editorTxt= new CellEditor(txtfldTariffa){
		    	@Override  
		        public Object preProcessValue(Object value) {  
		          if (value == null) {  
		            return value;  
		          }  
		          return value.toString();  
		        }  
		    
		        @Override  
		        public Object postProcessValue(Object value) {  
		          if (value == null) {  
		            return value;  
		          }  
		          return value.toString();  
		        }  
		    };	    
		    column.setEditor(editorTxt);
		    configs.add(column);
		    
		    
		    column = new ColumnConfig();  
		    column.setId("descrizione");  
		    column.setHeader("Descrizione Attiv.");  
		    column.setWidth(180);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    TextField<String> txtfldDescrizione= new TextField<String>();
		    editorTxt= new CellEditor(txtfldDescrizione){
		    	@Override  
		        public Object preProcessValue(Object value) {  
		          if (value == null) {  
		            return value;  
		          }  
		          return value.toString();  
		        }  
		    
		        @Override  
		        public Object postProcessValue(Object value) {  
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
	}
	
	
	private class CntpnlGridRdo extends ContentPanel{
		
		private Button btnChiudiOrdine;
		private Button btnAssociaCommessa;
		
		public CntpnlGridRdo(){
		
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(940);
			setFrame(false);
			setStyleAttribute("margin-top", "10px");
		
			caricaTabellaDati();
			try {
		    	cm = new ColumnModel(createColumns());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
			}	
					
			store.groupBy("cliente");
		 
			//Vista per permettere il grouping
			GroupingView view = new GroupingView();  
		    view.setShowGroupedColumn(false);  
		    view.setForceFit(true);  
		    view.setGroupRenderer(new GridGroupRenderer() {  
		      public String render(GroupColumnData data) {  
		        String f = cm.getColumnById(data.field).getHeader();  
		        //String l = data.models.size() == 1 ? "Item" : "Items";  
		        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
		      }  
		    });  
			    
		    gridRiepilogo= new Grid<RdoCompletaModel>(store, cm);  
		    gridRiepilogo.setBorders(false);  
		    gridRiepilogo.setStripeRows(true);  
		    gridRiepilogo.setColumnLines(true);  
		    gridRiepilogo.setColumnReordering(true);  
		    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  
		    gridRiepilogo.setView(view);
	        
		    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RdoCompletaModel>>() {  
		          public void handleEvent(SelectionChangedEvent<RdoCompletaModel> be) {  
		        	
		            if (be.getSelection().size() > 0) {      
		            	Date dataOff = null, dataInizio=null, dataFine=null;
		            	String statoOrdine=be.getSelectedItem().get("statoOrdine");
		            	btnChiudiOrdine.setEnabled(true);
		            	btnAssociaCommessa.setEnabled(true);
		            	
		            	String data=be.getSelectedItem().getData();
		            	String dataI=be.getSelectedItem().getDataInizio();
		            	String dataF=be.getSelectedItem().getDataFine();
		            	try {	
		            		if(data.compareTo("#")!=0){
		            			dataOff=DateTimeFormat.getFormat("yyyy-MM-dd").parse(data);
		            			dtfldDataOfferta.setValue(dataOff);
		            		}else dtfldDataOfferta.clear();
		            		if(dataI.compareTo("#")!=0){
		            			dataInizio=DateTimeFormat.getFormat("yyyy-MM-dd").parse(dataI);
		            			dtfldDataInizioOrdine.setValue(dataInizio);
		            		}else dtfldDataInizioOrdine.clear();
		            		if(dataF.compareTo("#")!=0){
		            			dataFine=DateTimeFormat.getFormat("yyyy-MM-dd").parse(dataF); 
		            			dtfldDataFineOrdine.setValue(dataFine);
		            		}else dtfldDataFineOrdine.clear();
		            	} catch (Exception e)
		            	  { e.printStackTrace();}  	  
		            	
		            	btnSave.setEnabled(false);
		            	btnDelete.setEnabled(true);
		            	btnEdit.setEnabled(true);
		            	smplcmbxCliente.setEnabled(false);
		            	
		            	txtfldIdRda.setValue(String.valueOf(be.getSelectedItem().getIdRdo()));	
		            	txtfldNumeroRda.setValue(String.valueOf(be.getSelectedItem().getNumeroRda()));
						smplcmbxCliente.setSimpleValue(be.getSelectedItem().getCliente());
		            	
						txtfldNumeroOfferta.setValue(String.valueOf(be.getSelectedItem().getNumeroOfferta()));
						txtfldImporto.setValue(String.valueOf(be.getSelectedItem().getImporto()));
										
						txtfldNumeroOrdine.setValue(String.valueOf(be.getSelectedItem().getNumeroOrdine()));	
						txtrDescrizione.setValue(String.valueOf(be.getSelectedItem().getDescrizione()));
									
						txtfldTariffaOraria.setValue(String.valueOf(be.getSelectedItem().getTariffa()));
						txtfldNumeroRisorse.setValue(String.valueOf(be.getSelectedItem().getNumeroRisorse()));
						txtfldNumeroOre.setValue(String.valueOf(be.getSelectedItem().getNumeroOre()));
						txtfldNumeroOreResidue.setValue(String.valueOf(be.getSelectedItem().getNumeroOreResidue()));
						
						txtfldImportoOrdine.setValue((String)be.getSelectedItem().get("importoOrdine"));
						txtfldImportoResiduoOrdine.setValue((String)be.getSelectedItem().get("importoResiduo"));
		               
						VerticalPanel vp= new VerticalPanel();
						vp=(VerticalPanel) getParent();
						CntpnlFormRdo cpfrm=(CntpnlFormRdo) vp.getItemByItemId("cpfrm");
						ContentPanel cp=(ContentPanel) cpfrm.getItemByItemId("cp");
						LayoutContainer layout=(LayoutContainer) cp.getItemByItemId("layout");
						CntpnlGridTariffeOrdine cpTariffa=(CntpnlGridTariffeOrdine) layout.getItemByItemId("cpTariffa");
						
						cpTariffa.caricaTabella(be.getSelectedItem().getIdRdo());
												
						if(statoOrdine.compareTo("C")==0){
							disableField();
						}else{
							enableField();
						}
							
						
		            } else {  
		              
		            }
		          }   
		    }); 
				    	
		    btnChiudiOrdine= new Button();
		    btnChiudiOrdine.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.chiudiCommessa()));
		    btnChiudiOrdine.setIconAlign(IconAlign.TOP);
		    btnChiudiOrdine.setToolTip("Chiudi Ordine");
		    btnChiudiOrdine.setEnabled(false);
		    btnChiudiOrdine.addSelectionListener(new SelectionListener<ButtonEvent>() {			
				@Override
				public void componentSelected(ButtonEvent ce) {
					String numeroOrdine= txtfldNumeroOrdine.getValue().toString();
					AdministrationService.Util.getInstance().closeOrdine(numeroOrdine, new AsyncCallback<Boolean>(){

						@Override
						public void onFailure(Throwable caught) {						
							System.out.println("Errore di connessione oncloseOrdine()");
						}

						@Override
						public void onSuccess(Boolean result) {
							if(!result)
								System.out.println("Impossibile effettuare la chiusura dell'ordine!");							
						}
					});
				}
			});
		    
		    btnAssociaCommessa= new Button();
		    btnAssociaCommessa.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
		    btnAssociaCommessa.setIconAlign(IconAlign.TOP);
		    btnAssociaCommessa.setToolTip("Associa una commessa all'ordine");
		    btnAssociaCommessa.setEnabled(false);
		    btnAssociaCommessa.addSelectionListener(new SelectionListener<ButtonEvent>() {			
				@Override
				public void componentSelected(ButtonEvent ce) {
					String numeroOrdine= txtfldNumeroOrdine.getValue().toString();
					
					DialogAssociaCommessaToOrdine d= new DialogAssociaCommessaToOrdine(numeroOrdine);
					d.show();					
					d.addListener(Events.Hide, new Listener<ComponentEvent>() {
						@Override
						public void handleEvent(ComponentEvent be) {						
							caricaTabellaDati();
						}
					});
				}
			});
		    
		    
		    //barra per casella ricerca
		    ToolBar tlbarSearchField= new ToolBar();
		    final TextField<String> txtfldsearch= new TextField<String>();
		    Button btnSearch= new Button();
		    
		    btnSearch.setSize(26, 26);
		    btnSearch.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.search()));
		    btnSearch.setIconAlign(IconAlign.TOP);
		    btnSearch.setEnabled(false);
		    	    
		    txtfldsearch.addKeyListener(new KeyListener(){
		    	 public void componentKeyUp(ComponentEvent event) {
		    		 
		    		 if(txtfldsearch.getRawValue().isEmpty()){
		    			 storeResult.removeAll();
		    			 store.removeAll();
		    			 store.add(storeCompleto.getModels());
		    			 gridRiepilogo.reconfigure(store, cm);
		    		 }else{
		    		 	    		 	    		 
		    			 String campo= txtfldsearch.getValue().toString();	    			 
		    			 
		    			 storeResult.removeAll();
		    			 for(RdoCompletaModel r:listaStore){
		    				 if(r.getNumeroCommessa().contains(campo) || r.getNumeroOfferta().compareTo(campo)==0 || 
		    						 r.getNumeroOrdine().contains(campo) || r.getNumeroRda().compareTo(campo)==0){
		    					 storeResult.add(r);		    				 
		    				 }
		    			 }
		    			 listaStore.clear();
		    			 listaStore.addAll(store.getModels());
		    			 gridRiepilogo.reconfigure(storeResult, cm);			 
		    		 } 
		    	 }	    	  	 
		    });		   
		    
		    tlbarSearchField.add(txtfldsearch);
		    tlbarSearchField.add(btnSearch);
		    //tlbarSearchField.add(btnChiudiOrdine);
		    tlbarSearchField.add(new SeparatorToolItem());
		    tlbarSearchField.add(btnAssociaCommessa);
		    
		    ContentPanel cntpnlGrid= new ContentPanel();
		    cntpnlGrid.setBodyBorder(false);  
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setFrame(true);
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(920);
		    cntpnlGrid.setHeight(560);
		    cntpnlGrid.setScrollMode(Scroll.AUTOY);
		    cntpnlGrid.add(gridRiepilogo);
		    cntpnlGrid.setTopComponent(tlbarSearchField);
		    	    
		    add(cntpnlGrid);		  	
		}

		private void enableField() {
			        	
        	txtfldIdRda.setEnabled(true);
        	txtfldNumeroRda.setEnabled(true);
			smplcmbxCliente.setEnabled(true);
        	
			txtfldNumeroOfferta.setEnabled(true);
			txtfldImporto.setEnabled(true);
							
			txtfldNumeroOrdine.setEnabled(true);	
			txtrDescrizione.setEnabled(true);
			txtfldBem.setEnabled(true);
			txtfldConto.setEnabled(true);
			txtfldElementoWbs.setEnabled(true);
			txtfldPrCenter.setEnabled(true);
						
			txtfldTariffaOraria.setEnabled(true);
			txtfldNumeroRisorse.setEnabled(true);
			txtfldNumeroOre.setEnabled(true);
			txtfldNumeroOreResidue.setEnabled(true);
			txtfldImportoOrdine.setEnabled(true);
			txtfldImportoResiduoOrdine.setEnabled(true);
			
			dtfldDataFineOrdine.setEnabled(true);
			dtfldDataInizioOrdine.setEnabled(true);
			dtfldDataOfferta.setEnabled(true);
		}
		

		private void disableField() {
			txtfldIdRda.setEnabled(false);
        	txtfldNumeroRda.setEnabled(false);
			smplcmbxCliente.setEnabled(false);
        	
			txtfldNumeroOfferta.setEnabled(false);
			txtfldImporto.setEnabled(false);
							
			txtfldNumeroOrdine.setEnabled(false);	
			txtrDescrizione.setEnabled(false);
			txtfldBem.setEnabled(false);
			txtfldConto.setEnabled(false);
			txtfldElementoWbs.setEnabled(false);
			txtfldPrCenter.setEnabled(false);
						
			txtfldTariffaOraria.setEnabled(false);
			txtfldNumeroRisorse.setEnabled(false);
			txtfldNumeroOre.setEnabled(false);
			txtfldNumeroOreResidue.setEnabled(false);	
			txtfldImportoOrdine.setEnabled(false);
			txtfldImportoResiduoOrdine.setEnabled(false);
			
			dtfldDataFineOrdine.setEnabled(true);
			dtfldDataInizioOrdine.setEnabled(true);
			dtfldDataOfferta.setEnabled(true);
		}  
		
		
		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			
			ColumnConfig column=new ColumnConfig();		
		    column.setId("cliente");  
		    column.setHeader("Cliente");  
		    column.setWidth(140);  
		    column.setRowHeader(true); 
		    configs.add(column); 
			
		    column=new ColumnConfig();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
			column.setId("numeroRdo");  
			column.setHeader("N. Rdo");  
			column.setWidth(80);  
			column.setRowHeader(true); 
			column.setAlignment(HorizontalAlignment.RIGHT); 
			configs.add(column);
		    
			column=new ColumnConfig();		
			column.setId("numeroOfferta");  
			column.setHeader("N. Offerta");  
			column.setWidth(60);  
			column.setRowHeader(true);  
			column.setAlignment(HorizontalAlignment.RIGHT);
			configs.add(column);
			    
			/*column=new ColumnConfig();		
		    column.setId("importo");  
		    column.setHeader("Importo");  
		    column.setWidth(70);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);  */
		       
		    column=new ColumnConfig();		
		    column.setId("numeroOrdine");  
		    column.setHeader("N. Ordine");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column); 
		    
		    /*column=new ColumnConfig();		
		    column.setId("tariffaOraria");  
		    column.setHeader("Tariffa");  
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);*/
		    
		   /* column=new ColumnConfig();		
		    column.setId("numeroRisorse");  
		    column.setHeader("N. Risorse");  
		    column.setWidth(55);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);*/
		    
		    column=new ColumnConfig();		
		    column.setId("numeroOre");  
		    column.setHeader("N. Ore");  
		    column.setWidth(55);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("numeroOreResidue");  
		    column.setHeader("N. Ore Res.");  
		    column.setWidth(55);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("importoOrdine");  
		    column.setHeader("Importo Ordine");  
		    column.setWidth(75);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("importoResiduo");  
		    column.setHeader("Importo Res.");  
		    column.setWidth(75);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    return configs;
		}	
	}
	
	private void caricaTabellaDati() {	
		AdministrationService.Util.getInstance().getAllRdoCompletaModel(new AsyncCallback<List<RdoCompletaModel>>() {
			
			@Override
			public void onSuccess(List<RdoCompletaModel> result) {
				if(result==null)
					Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
				else	
					loadTable(result);			
			}			

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getAllRdoCompletaModel();");
				caught.printStackTrace();
			
			}
		}); //AsyncCallback	      
	}

	
	private void loadTable(List<RdoCompletaModel> lista) {
		
		try {
			store.removeAll();
			store.setStoreSorter(new StoreSorter<RdoCompletaModel>());  
		    store.setDefaultSort("numeroCommessa", SortDir.ASC);
			store.add(lista);
			storeResult.removeAll();
			storeCompleto.removeAll();
			storeResult.add(store.getModels());
			storeCompleto.add(store.getModels());
			listaStore.addAll(store.getModels());
						    		    	
		} catch (NullPointerException e) {
				e.printStackTrace();
		}	
	}
	
	private boolean arePresent() {
		String numRdo="";
		String numOff="";
		String numOrd="";
		
		if(!txtfldNumeroRda.getRawValue().isEmpty())numRdo=txtfldNumeroRda.getValue().toString();
		if(!txtfldNumeroOfferta.getRawValue().isEmpty())numOff=txtfldNumeroOfferta.getValue().toString();
		if(!txtfldNumeroOrdine.getRawValue().isEmpty())numOrd=txtfldNumeroOrdine.getValue().toString();	
		
		List<RdoCompletaModel> lista= new ArrayList<RdoCompletaModel>();
		lista.addAll(store.getModels());
		
		for(RdoCompletaModel r:lista){
			if(r.getNumeroRda().compareTo(numRdo)==0){
				Window.alert("Numero Rdo presente!");
				return true;
			}
			if(r.getNumeroOfferta().compareTo(numOff)==0){
				Window.alert("Numero Offerta presente!");
				return true;
			}
			if(r.getNumeroOrdine().compareTo(numOrd)==0){
				Window.alert("Numero Ordine presente!");
				return true;
			}
		}		
		return false;
	}
	
	private boolean formIsValid() {
		
		if(!smplcmbxCliente.isValid())
			return false;
		if(!txtfldNumeroRda.isValid())
			return false;
		if(!txtfldNumeroOfferta.isValid())
			return false;		
		if(!txtfldImporto.isValid())
			return false;
		if(!dtfldDataOfferta.isValid())
			return false;
		if(!txtfldNumeroOrdine.isValid())
			return false;
		if(!txtrDescrizione.isValid())
			return false;				
		if(!txtfldBem.isValid())
			return false;
		if(!txtfldConto.isValid())
			return false;
		if(!txtfldElementoWbs.isValid())
			return false;
		if(!txtfldPrCenter.isValid())
			return false;
		if(!dtfldDataInizioOrdine.isValid())
			return false;
		if(!dtfldDataFineOrdine.isValid())
			return false;
		if(!txtfldTariffaOraria.isValid())
			return false;
		if(!txtfldNumeroRisorse.isValid())
			return false;
		if(!txtfldNumeroOre.isValid())
			return false;
		if(!txtfldNumeroOreResidue.isValid())
			return false;
		if(!txtfldImportoOrdine.isValid())
			return false;
		if(!txtfldImportoResiduoOrdine.isValid())
			return false;					
		
		return true;
	}	
	
	private void resetForm() {
		
		smplcmbxCliente.clear();
		txtfldNumeroRda.clear();
		txtrDescrizione.clear();
		txtfldBem.clear();
		txtfldConto.clear();
		txtfldElementoWbs.clear();
		txtfldPrCenter.clear();
		txtfldNumeroOfferta.clear();
		txtfldImporto.clear();
		dtfldDataOfferta.clear();
		txtfldNumeroOrdine.clear();
		txtfldTariffaOraria.clear();
		dtfldDataFineOrdine.clear();
		dtfldDataInizioOrdine.clear();
		txtfldNumeroRisorse.clear();
		txtfldNumeroOre.clear();
		txtfldNumeroOreResidue.clear();
		txtfldImportoOrdine.clear();
		txtfldImportoResiduoOrdine.clear();
		gridRiepilogo.getSelectionModel().deselectAll();
		txtfldIdRda.setEnabled(true);
    	txtfldNumeroRda.setEnabled(true);
		smplcmbxCliente.setEnabled(true);
    	
		txtfldNumeroOfferta.setEnabled(true);
		txtfldImporto.setEnabled(true);
						
		txtfldNumeroOrdine.setEnabled(true);	
		txtrDescrizione.setEnabled(true);
					
		txtfldTariffaOraria.setEnabled(true);
		txtfldNumeroRisorse.setEnabled(true);
		txtfldNumeroOre.setEnabled(true);
		txtfldNumeroOreResidue.setEnabled(true);
		txtfldImportoOrdine.setEnabled(true);
		txtfldImportoResiduoOrdine.setEnabled(true);
	}
	
}

