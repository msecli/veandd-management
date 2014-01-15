package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class PanelRiepilogoStatoAvanzamentoOreCommesse extends LayoutContainer{

	private ListStore<CommessaModel> storeCommesse;
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepCommesse = new GroupingStore<RiepilogoOreDipFatturazione>();
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepCommesseAll = new GroupingStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepCommesse;
	private ColumnModel cmRiepCommesse;
	private CellSelectionModel<RiepilogoOreDipFatturazione> cm;
	
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepMesi = new GroupingStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepMesi;
	private ColumnModel cmRiepMesi;	
	
	private SimpleComboBox<String> smplcmbxPm;
	private ComboBox<CommessaModel> cmbxCommessa= new ComboBox<CommessaModel>();
	private SimpleComboBox<String> smplcmbxPeriodo; //scegliere se anno o tutto
	private SimpleComboBox<String> smplcmbxAnnoInizio;
	private SimpleComboBox<String> smplcmbxMeseInizio;
	private SimpleComboBox<String> smplcmbxAnnoFine;
	private SimpleComboBox<String> smplcmbxMeseFine;
	private SimpleComboBox<String> smplcmbxGroupBy;
	private Button btnAggiorna;
	private Text txtCommessaSelezionata;
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();	
	
	public PanelRiepilogoStatoAvanzamentoOreCommesse(){
		
	}
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		//VerticalPanel vp= new VerticalPanel();
		HorizontalPanel vp= new HorizontalPanel();
		vp.setSpacing(3);
		
		ContentPanel cntpnlGroupPerCommesse= new ContentPanel();
		cntpnlGroupPerCommesse.setHeaderVisible(false);
		cntpnlGroupPerCommesse.setBorders(false);
		cntpnlGroupPerCommesse.setFrame(true);
		//cntpnlGroupPerCommesse.setSize(w-220, (h-55)/2+90);
		cntpnlGroupPerCommesse.setSize((w-224)/2, h-68);
		cntpnlGroupPerCommesse.setScrollMode(Scroll.AUTO);
		cntpnlGroupPerCommesse.setLayout(new FitLayout());
		
		ContentPanel cntpnlGroupPerMese= new ContentPanel();
		cntpnlGroupPerMese.setHeaderVisible(false);
		cntpnlGroupPerMese.setBorders(false);
		cntpnlGroupPerMese.setFrame(true);	
		//cntpnlGroupPerMese.setSize(w-220, (h-55)/2-90);
		cntpnlGroupPerMese.setSize((w-224)/2, h-68);
		cntpnlGroupPerMese.setScrollMode(Scroll.AUTO);
		cntpnlGroupPerMese.setLayout(new FitLayout());	
		
		cm=new CellSelectionModel<RiepilogoOreDipFatturazione>();
		cm.setSelectionMode(SelectionMode.SIMPLE);	   
		cmRiepCommesse=new ColumnModel(createColumnsDettaglioCommesse());
		storeRiepCommesse.groupBy("numeroCommessa");
		storeRiepCommesse.setSortField("dipendente");
		storeRiepCommesse.setSortDir(SortDir.ASC);
		
		GroupSummaryView summary = new GroupSummaryView();  
		summary.setForceFit(true);  
		summary.setShowGroupedColumn(false);
		summary.setStartCollapsed(false);
		
		gridRiepCommesse= new EditorGrid<RiepilogoOreDipFatturazione>(storeRiepCommesse, cmRiepCommesse);
		gridRiepCommesse.setBorders(true);
		gridRiepCommesse.setItemId("grid");
		gridRiepCommesse.setStripeRows(true); 
		gridRiepCommesse.setColumnLines(true);
		gridRiepCommesse.setSelectionModel(cm);
		gridRiepCommesse.setView(summary);
		gridRiepCommesse.addListener(Events.CellClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent be) {
		            List<RiepilogoOreDipFatturazione>listaDati=new ArrayList<RiepilogoOreDipFatturazione>();
					RiepilogoOreDipFatturazione r=cm.getSelectedItem();	
					String numeroCommessa=r.get("numeroCommessa");
					txtCommessaSelezionata.setText(" Commessa selezionata: "+numeroCommessa);	
					storeRiepMesi.removeAll();
					listaDati=storeRiepCommesseAll.getModels();
					for(RiepilogoOreDipFatturazione r1:listaDati){
						String commessa=r1.get("numeroCommessa");
						String mese=r1.get("meseRif");
						if(commessa.compareTo(numeroCommessa)==0 && mese.compareTo("Totale")!=0)
							storeRiepMesi.add(r1);
					}								
					gridRiepMesi.reconfigure(storeRiepMesi, cmRiepMesi);
				}
		});	  
		
		cmRiepMesi= new ColumnModel(createColumnsDettaglioMese());	
		storeRiepMesi.groupBy("indicativoMese");
		storeRiepMesi.setSortField("dipendente");
		storeRiepMesi.setSortDir(SortDir.ASC);
		
		GroupSummaryView summaryM = new GroupSummaryView();  
		summaryM.setForceFit(true);  
		summaryM.setShowGroupedColumn(false);
		summaryM.setStartCollapsed(false);
		
		gridRiepMesi= new EditorGrid<RiepilogoOreDipFatturazione>(storeRiepMesi, cmRiepMesi);
		gridRiepMesi.setBorders(true);
		gridRiepMesi.setItemId("gridMesi");
		gridRiepMesi.setStripeRows(true); 
		gridRiepMesi.setColumnLines(true);
		gridRiepMesi.setView(summaryM);
		
		smplcmbxPm= new SimpleComboBox<String>();
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setName("pm");
		smplcmbxPm.setAllowBlank(true);
		smplcmbxPm.setTriggerAction(TriggerAction.ALL);
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setAllowBlank(false);
		getNomePM();
		
		storeCommesse=new ListStore<CommessaModel>();
		storeCommesse.setStoreSorter(new StoreSorter<CommessaModel>());
		storeCommesse.setDefaultSort("numeroCommessa", SortDir.ASC);
		cmbxCommessa.setStore(storeCommesse);
		cmbxCommessa.setFieldLabel("Commessa");
		cmbxCommessa.setEmptyText("Selezionare la commessa..");
		cmbxCommessa.setEditable(true);
		cmbxCommessa.setTriggerAction(TriggerAction.ALL);
		cmbxCommessa.setAllowBlank(false);
		cmbxCommessa.setDisplayField("commessa");
		cmbxCommessa.setWidth(230);
		cmbxCommessa.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {	
				
				String nome=new String();
				String cognome=new String();
				nome=smplcmbxPm.getRawValue().toString();
							
				if(nome.indexOf(" ")>0){
					cognome=nome.substring(0, nome.indexOf(" "));
					nome=nome.substring(nome.indexOf(" ")+1, nome.length());
					
					getCommesseByPm(nome, cognome);
					
				}else {
					nome="Tutti";
					cognome="";
					getCommesseByPm(nome, cognome);
				}							
			}			
		});		
		
		smplcmbxPeriodo=new SimpleComboBox<String>();
		smplcmbxPeriodo.setEmptyText("Periodo..");
		smplcmbxPeriodo.setTriggerAction(TriggerAction.ALL);
		smplcmbxPeriodo.setAllowBlank(false);
		smplcmbxPeriodo.add("Anno");
		smplcmbxPeriodo.add("Periodo");
		smplcmbxPeriodo.setWidth(80);
		smplcmbxPeriodo.addListener(Events.SelectionChange, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				String periodo = smplcmbxPeriodo.getRawValue().toString();
				if(periodo.compareTo("Anno")==0){
					smplcmbxAnnoInizio.setVisible(true);
					smplcmbxMeseInizio.setVisible(false);	
					smplcmbxAnnoFine.setVisible(false);
					smplcmbxMeseFine.setVisible(false);
				}else 
					if(periodo.compareTo("Periodo")==0){
						smplcmbxAnnoInizio.setVisible(true);
						smplcmbxMeseInizio.setVisible(true);
						smplcmbxAnnoFine.setVisible(true);
						smplcmbxMeseFine.setVisible(true);
					}
					
				btnAggiorna.setEnabled(true);
			}		
		});
		
		btnAggiorna= new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnAggiorna.setIconAlign(IconAlign.BOTTOM);
		btnAggiorna.setToolTip("Aggiorna");
		btnAggiorna.setSize(26, 26);
		btnAggiorna.setEnabled(false);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					if(smplcmbxPm.isValid()&&cmbxCommessa.isValid()&&smplcmbxPeriodo.isValid()){
						
						List<CommessaModel> listaCommesseSel= new ArrayList<CommessaModel>();
						String pm= smplcmbxPm.getRawValue().toString();
						String commessa=cmbxCommessa.getValue().get("numeroCommessa");
						//commessa=commessa.substring(0,commessa.indexOf("(")-1);
						if(commessa.compareTo("Tutte")==0)
							listaCommesseSel.addAll(storeCommesse.getModels());
						else
							listaCommesseSel.add(cmbxCommessa.getValue());
						
						if(smplcmbxPeriodo.getRawValue().toString().compareTo("Anno")==0)
							if(smplcmbxAnnoInizio.isValid()){															
									String anno= smplcmbxAnnoInizio.getRawValue().toString();												
									caricaTabellaDati(anno,"","","", pm, listaCommesseSel);		
							}else
									Window.alert("Controllare i campi inseriti!");
						else
							if(smplcmbxAnnoFine.isValid()&&smplcmbxMeseFine.isValid()
										&&smplcmbxAnnoInizio.isValid()&&smplcmbxMeseInizio.isValid()){
									String annoI=smplcmbxAnnoInizio.getRawValue().toString();
									String meseI=smplcmbxMeseInizio.getRawValue().toString();
									String annoF=smplcmbxAnnoFine.getRawValue().toString();
									String meseF=smplcmbxMeseFine.getRawValue().toString();
									caricaTabellaDati(annoI,meseI,annoF,meseF, pm, listaCommesseSel);
								
							}else
								Window.alert("Controllare i campi inseriti!");						
					}else
						Window.alert("Controllare i campi inseriti!");
				}
		});	  
				
		Date d= new Date();
		String data= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
		String anno= data.substring(data.length()-4, data.length());
		
		smplcmbxAnnoInizio= new SimpleComboBox<String>();
		smplcmbxAnnoInizio.setWidth(80);
		smplcmbxAnnoInizio.setEmptyText("Anno..");
		smplcmbxAnnoInizio.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnnoInizio.setVisible(false);
		smplcmbxAnnoInizio.setAllowBlank(false);
				
		smplcmbxMeseInizio= new SimpleComboBox<String>();
		smplcmbxMeseInizio.setWidth(100);
		smplcmbxMeseInizio.setTriggerAction(TriggerAction.ALL);
		smplcmbxMeseInizio.setEmptyText("Mese..");
		smplcmbxMeseInizio.setVisible(false);
		smplcmbxMeseInizio.setAllowBlank(false);
				
		smplcmbxAnnoFine=new SimpleComboBox<String>();
		smplcmbxAnnoFine.setWidth(80);
		smplcmbxAnnoFine.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnnoFine.setEmptyText("Anno..");
		smplcmbxAnnoFine.setVisible(false);
		smplcmbxAnnoFine.setAllowBlank(false);
		for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnnoInizio.add(l);
			 smplcmbxAnnoFine.add(l);
		}
		smplcmbxAnnoInizio.setSimpleValue(anno);
		smplcmbxAnnoFine.setSimpleValue(anno);
		
		smplcmbxMeseFine=new SimpleComboBox<String>();
		smplcmbxMeseFine.setWidth(100);
		smplcmbxMeseFine.setTriggerAction(TriggerAction.ALL);
		smplcmbxMeseFine.setEmptyText("Mese..");
		smplcmbxMeseFine.setVisible(false);
		smplcmbxMeseFine.setAllowBlank(false);
		for(String l : DatiComboBox.getMese()){
			smplcmbxMeseInizio.add(l);
			smplcmbxMeseFine.add(l);			 
		}
		smplcmbxMeseFine.setSimpleValue(mese);
		
		smplcmbxGroupBy= new SimpleComboBox<String>();	
		smplcmbxGroupBy.setWidth(120);
		smplcmbxGroupBy.setTriggerAction(TriggerAction.ALL);
		smplcmbxGroupBy.setEmptyText("Group By..");
		smplcmbxGroupBy.add("Dipendente");
		smplcmbxGroupBy.add("Mese");
		smplcmbxGroupBy.addListener(Events.SelectionChange, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				String selezione=smplcmbxGroupBy.getRawValue().toString();
				if(selezione.compareTo("Dipendente")==0){
					storeRiepMesi.groupBy("dipendente");
					storeRiepMesi.setSortField("numeroMese");
					storeRiepMesi.setSortDir(SortDir.ASC);
				}else{
					storeRiepMesi.groupBy("indicativoMese");
					storeRiepMesi.setSortField("dipendente");
					storeRiepMesi.setSortDir(SortDir.ASC);					
				}			
			}		
		});
		
		txtCommessaSelezionata=new Text();	
		txtCommessaSelezionata.setStyleAttribute("font-weight", "bold");
				
		HorizontalPanel hrzpnl1=new HorizontalPanel();
		hrzpnl1.setSpacing(2);		
		HorizontalPanel hrzpnl2=new HorizontalPanel();
		hrzpnl2.setSpacing(2);
		VerticalPanel vrtclpnl1= new VerticalPanel();
		//vrtclpnl1.setSpacing(1);
		
		hrzpnl1.add(smplcmbxPm);
		hrzpnl1.add(cmbxCommessa);
		hrzpnl1.add(smplcmbxPeriodo);
		hrzpnl1.add(btnAggiorna);
		hrzpnl2.add(smplcmbxAnnoInizio);
		hrzpnl2.add(smplcmbxMeseInizio);
		hrzpnl2.add(smplcmbxAnnoFine);
		hrzpnl2.add(smplcmbxMeseFine);
		
		vrtclpnl1.add(hrzpnl1);
		vrtclpnl1.add(hrzpnl2);
		
		ToolBar tlbrGroupCommesse= new ToolBar();
		tlbrGroupCommesse.add(vrtclpnl1);
		cntpnlGroupPerCommesse.setTopComponent(tlbrGroupCommesse);
		cntpnlGroupPerCommesse.add(gridRiepCommesse);
		
		HorizontalPanel hrpnl3= new HorizontalPanel();
		hrpnl3.setSpacing(3);
		
		hrpnl3.add(smplcmbxGroupBy);
		hrpnl3.add(txtCommessaSelezionata);
		
		ToolBar tlbrGroup=new ToolBar();
		tlbrGroup.add(hrpnl3);	
		//tlbrGroup.add(new SeparatorMenuItem());
		//tlbrGroup.add(txtCommessaSelezionata);
		cntpnlGroupPerMese.setTopComponent(tlbrGroup);
		cntpnlGroupPerMese.add(gridRiepMesi);
		
		vp.add(cntpnlGroupPerCommesse);
		vp.add(cntpnlGroupPerMese);
		
		layoutContainer.add(vp, new FitData(1,1,1,1));
		 
		add(layoutContainer);	
	}

	
	private void caricaTabellaDati(String annoI, String meseI,
			String annoF, String meseF , String pm, List<CommessaModel> listaCommesseSel) {
		
		AdministrationService.Util.getInstance().getRiepilogoOreCommesseDettDipendentiPeriodo(
				annoI, meseI, annoF, meseF, pm, listaCommesseSel, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {	
			@Override
			public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						Window.alert("Nessun dato (ore lavoro) rilevato in base ai criteri di ricerca selezionati.");
					}
					else loadTable(result);			
			}
			
			private void loadTable(List<RiepilogoOreDipFatturazione> result) {
				String meseRif= new String();
				storeRiepCommesseAll.removeAll();
				storeRiepCommesseAll.add(result);
				storeRiepCommesse.removeAll();
				
				for(RiepilogoOreDipFatturazione r:result){
					meseRif=r.get("meseRif");					
					if(meseRif.compareTo("Totale")==0)
						storeRiepCommesse.add(r);
				}				
				
				//storeRiepCommesse.add(result);
				storeRiepCommesse.groupBy("numeroCommessa");
				gridRiepCommesse.reconfigure(storeRiepCommesse, cmRiepCommesse);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  	
	}

	/*
	private void caricaTabellaDati(String anno, String pm, List<CommessaModel> listaCommesseSel) {
		
		AdministrationService.Util.getInstance().getRiepilogoOreCommesseDettDipendentiPeriodo(
				anno, "", "", "", pm, listaCommesseSel, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {	
			@Override
			public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
				if(result==null)
					Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
				else	
					if(result.size()==0){
						Window.alert("Nessun dato (ore lavoro) rilevato in base ai criteri di ricerca selezionati.");
					}
					else loadTable(result);			
			}
			
			private void loadTable(List<RiepilogoOreDipFatturazione> result) {
				storeRiepCommesseAll.removeAll();
				storeRiepCommesseAll.add(result);
				storeRiepCommesse.removeAll();
				storeRiepCommesse.add(result);
				storeRiepCommesse.groupBy("numeroCommessa");
				gridRiepCommesse.reconfigure(storeRiepCommesse, cmRiepCommesse);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
				caught.printStackTrace();		
			}
		}); //AsyncCallback	  	
	}
	*/
	private void getCommesseByPm(String nome, String cognome) {
		if(nome.compareTo("Tutti")!=0)
		  AdministrationService.Util.getInstance().getCommesseByPmConAssociazioni(nome, cognome, new AsyncCallback<List<CommessaModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<CommessaModel> result) {
				if(result!=null){
					storeCommesse.removeAll();
					storeCommesse.add(result);
					storeCommesse.add(new CommessaModel(0, "Tutte", "", ""));
					cmbxCommessa.clear();									
					cmbxCommessa.setStore(storeCommesse);					
				}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");		
			}
		  });
		else
			AdministrationService.Util.getInstance().getCommesseAperteConAssociazioni( new AsyncCallback<List<CommessaModel>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getCommesseByPM();");
					caught.printStackTrace();				
				}

				@Override
				public void onSuccess(List<CommessaModel> result) {
					if(result!=null){
						storeCommesse.removeAll();
						storeCommesse.add(result);	
						storeCommesse.add(new CommessaModel(0, "Tutte", "", ""));
						cmbxCommessa.clear();				
						cmbxCommessa.setStore(storeCommesse);						
					}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");
					
				}
			});		
	}
	
	
	private void getNomePM() {
		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getNomePM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxPm.add(result);
					smplcmbxPm.add("Tutti");
					smplcmbxPm.recalculate();
					//smplcmbxPm.setSimpleValue("Tutti");
												
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});				
	}

	private List<ColumnConfig> createColumnsDettaglioMese() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("indicativoMese");  
	    column.setHeader("Mese");  
	    column.setWidth(100);  
	    column.setRowHeader(true);  
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("dipendente");  
		column.setHeader("Dipendente");  
		column.setWidth(200);  
		column.setRowHeader(true); 
	    configs.add(column); 
	    	    
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(100);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  
	    columnOreLavoro.setSummaryType(SummaryType.SUM);
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
		
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			} 	
		});
	    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(100);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
	    columnOreViaggio.setSummaryType(SummaryType.SUM);
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});   
	    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreViaggio); 
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("oreTotali");  
	    columnOreTotali.setHeader("Totale C.");  
	    columnOreTotali.setWidth(100);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);    
	    columnOreTotali.setSummaryType(SummaryType.SUM);
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				Float n=model.get(property);
				return number.format(n);
			}			
		});    
	    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    }); 
	    configs.add(columnOreTotali); 	
	    
	    return configs;
	}
	

	private List<ColumnConfig> createColumnsDettaglioCommesse() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		final NumberFormat number = NumberFormat.getFormat("0.00");
		
		
		SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
	    column.setId("numeroCommessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(120);  
	    column.setRowHeader(true);
	    //column.setRenderer(renderer);
	    configs.add(column); 
	    
	    column=new SummaryColumnConfig<Double>();		
	    column.setId("dipendente");  
		column.setHeader("Dipendente");  
		column.setWidth(120);  
		column.setRowHeader(true); 
		//column.setRenderer(renderer);
	    configs.add(column); 
	    
	   
	    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
	    columnOreLavoro.setId("oreLavoro");  
	    columnOreLavoro.setHeader("Ore Lavoro");  
	    columnOreLavoro.setWidth(80);    
	    columnOreLavoro.setRowHeader(true); 
	    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  
	    columnOreLavoro.setSummaryType(SummaryType.SUM);
	    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
		
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				
				Float n=model.get(property);
				return number.format(n);		
			} 	
		});
	    columnOreLavoro.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreLavoro); 	
	    
	    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
	    columnOreViaggio.setId("oreViaggio");  
	    columnOreViaggio.setHeader("Ore Viaggio");  
	    columnOreViaggio.setWidth(80);    
	    columnOreViaggio.setRowHeader(true); 
	    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);   
	    columnOreViaggio.setSummaryType(SummaryType.SUM);
	    columnOreViaggio.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				
				Float n=model.get(property);
				return number.format(n);
			}			
		});   
	    columnOreViaggio.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    });  
	    configs.add(columnOreViaggio); 
	    
	    
	    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
	    columnOreTotali.setId("oreTotali");  
	    columnOreTotali.setHeader("Totale C.");  
	    columnOreTotali.setWidth(80);    
	    columnOreTotali.setRowHeader(true); 
	    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);  
	    columnOreTotali.setSummaryType(SummaryType.SUM);
	    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
			@Override
			public Object render(RiepilogoOreDipFatturazione model,
					String property, ColumnData config, int rowIndex,
					int colIndex,
					ListStore<RiepilogoOreDipFatturazione> store,
					Grid<RiepilogoOreDipFatturazione> grid) {
				
				Float n=model.get(property);
				return number.format(n);
			}			
		});    
	    columnOreTotali.setSummaryRenderer(new SummaryRenderer() {  
   			@Override
			public String render(Number value, Map<String, Number> data) {
				return number.format(value);
			}  
	    }); 
	    configs.add(columnOreTotali); 	
	    
	    return configs;
	}
}
