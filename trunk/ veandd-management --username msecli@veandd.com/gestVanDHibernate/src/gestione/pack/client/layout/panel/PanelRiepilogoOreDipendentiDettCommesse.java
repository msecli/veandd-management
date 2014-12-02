package gestione.pack.client.layout.panel;

import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


/*Visualizzazione di un riepilogo con le ore nel mese di un dipendente divise per commessa*/

public class PanelRiepilogoOreDipendentiDettCommesse extends LayoutContainer{
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();	
	
	private ListStore<PersonaleModel> storeCommesse;
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepCommesse = new GroupingStore<RiepilogoOreDipFatturazione>();
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepCommesseAll = new GroupingStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepCommesse;
	private ColumnModel cmRiepCommesse;
	private CellSelectionModel<RiepilogoOreDipFatturazione> cm;
	
	private GroupingStore<RiepilogoOreDipFatturazione>storeRiepMesi = new GroupingStore<RiepilogoOreDipFatturazione>();
	private Grid<RiepilogoOreDipFatturazione> gridRiepMesi;
	private ColumnModel cmRiepMesi;	
	
	private ComboBox<PersonaleModel> cmbxPersonale= new ComboBox<PersonaleModel>();
	private SimpleComboBox<String> smplcmbxPeriodo; //scegliere se anno o tutto
	private SimpleComboBox<String> smplcmbxAnnoInizio;
	private SimpleComboBox<String> smplcmbxMeseInizio;
	private SimpleComboBox<String> smplcmbxAnnoFine;
	private SimpleComboBox<String> smplcmbxMeseFine;
	private SimpleComboBox<String> smplcmbxGroupBy;
	private Button btnAggiorna;
	private Text txtCommessaSelezionata;
	
	
	public PanelRiepilogoOreDipendentiDettCommesse(){
		
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
		
				
		storeCommesse=new ListStore<PersonaleModel>();
		storeCommesse.setStoreSorter(new StoreSorter<PersonaleModel>());
		storeCommesse.setDefaultSort("numeroCommessa", SortDir.ASC);
		cmbxPersonale.setStore(storeCommesse);
		cmbxPersonale.setFieldLabel("Commessa");
		cmbxPersonale.setEmptyText("Selezionare i dipendenti..");
		cmbxPersonale.setEditable(true);
		cmbxPersonale.setTriggerAction(TriggerAction.ALL);
		cmbxPersonale.setAllowBlank(false);
		cmbxPersonale.setDisplayField("commessa");
		cmbxPersonale.setWidth(230);
		
		
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
					
					if(cmbxPersonale.isValid()&&smplcmbxPeriodo.isValid()){
											
												
						if(smplcmbxPeriodo.getRawValue().toString().compareTo("Anno")==0)
							if(smplcmbxAnnoInizio.isValid()){															
									String anno= smplcmbxAnnoInizio.getRawValue().toString();							
									
							}else
									Window.alert("Controllare i campi inseriti!");
						else
							if(smplcmbxAnnoFine.isValid()&&smplcmbxMeseFine.isValid()
										&&smplcmbxAnnoInizio.isValid()&&smplcmbxMeseInizio.isValid()){
									String annoI=smplcmbxAnnoInizio.getRawValue().toString();
									String meseI=smplcmbxMeseInizio.getRawValue().toString();
									String annoF=smplcmbxAnnoFine.getRawValue().toString();
									String meseF=smplcmbxMeseFine.getRawValue().toString();
									
									
								
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
		
		hrzpnl1.add(cmbxPersonale);
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

	
	private List<ColumnConfig> createColumnsDettaglioMese() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		return configs;
	}


	private List<ColumnConfig> createColumnsDettaglioCommesse() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		return configs;
	}
	
	
	
}
