package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.layout.panel.DialogCheckCommesseFatturate;
import gestione.pack.client.layout.panel.DialogRiepilogoDatiFoglioFatturazione;
import gestione.pack.client.layout.panel.PanelAttribuzioneOreColocationCollaboratori;
import gestione.pack.client.layout.panel.PanelRiepilogoMeseGiornalieroCommesseHorizontal;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;
import gestione.pack.client.model.FoglioFatturazioneModel;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_FoglioFatturazione extends LayoutContainer {

public CenterLayout_FoglioFatturazione(){}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	private SimpleComboBox<String> smplcmbxMese= new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxAnno=new SimpleComboBox<String>();
	private SimpleComboBox<String> smplcmbxPM;
	private Button btnSelect;
	
	private TextField<String> txtfldUsername= new TextField<String>();
	//private Text txtRuolo= new Text("AMM");
	private HorizontalPanel hpLayout;
	private Button btnSalva;
	private boolean trovato=true;
	private String numCommessa= "";
	private String numEstensione= "";
	private int idAttivita;
	private String ruolo="";
	
	public CenterLayout_FoglioFatturazione(String ruolo){
		this.ruolo=ruolo;
	}
		
	protected void onRender(Element target, int index) {
	    super.onRender(target, index);

	    try {
			selectLayout(); //Se l'accesso è stato effettuato da un PM ci sarà la precompilazione del nome PM (successivamente anche mese e dati relativi)		
		} catch (Exception e) {
			Window.alert("error: Impossibile recuperare i dati in sessione.");
		}
	    
		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
		
		hpLayout= new HorizontalPanel();
		hpLayout.setSpacing(0);
		hpLayout.setItemId("hpLayout");
		hpLayout.setStyleAttribute("margin-left", "5px");
				
		/*ContentPanel cntpnlLayout = new ContentPanel(); //pannello esterno
		cntpnlLayout.setHeading("Fatturazione.");
		cntpnlLayout.setHeaderVisible(true);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setWidth(w-215);
		cntpnlLayout.setHeight(h-55);
		cntpnlLayout.setScrollMode(Scroll.AUTO);
		*/
		
		ContentPanel cntpnlFoglioFatturazione= new ContentPanel();
		cntpnlFoglioFatturazione.setHeading("Foglio di Fatturazione.");
		cntpnlFoglioFatturazione.setHeaderVisible(false);
		cntpnlFoglioFatturazione.setCollapsible(false);
		cntpnlFoglioFatturazione.setBorders(false);
		cntpnlFoglioFatturazione.setWidth(1390);
		cntpnlFoglioFatturazione.setHeight(900);
		cntpnlFoglioFatturazione.setFrame(true);
		cntpnlFoglioFatturazione.setButtonAlign(HorizontalAlignment.CENTER);
		//cntpnlFoglioFatturazione.setStyleAttribute("padding-left", "7px");
		//cntpnlFoglioFatturazione.setStyleAttribute("margin-top", "15px");		
		//cntpnlFoglioFatturazione.addButton(btnSalva);
					
		Date d= new Date();
		String data= d.toString();
		String mese= ClientUtility.meseToLong(ClientUtility.traduciMeseToIt(data.substring(4, 7)));
		String anno= data.substring(data.length()-4, data.length());
			
		txtfldUsername.setVisible(false);		
		//txtRuolo.setVisible(false);
		
		smplcmbxMese.setWidth(110);
		smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setSimpleValue(mese);
			
		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setWidth(75);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);
			 smplcmbxAnno.select(1);
		}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setSimpleValue(anno);
		
		smplcmbxPM = new SimpleComboBox<String>();
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(true);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		smplcmbxPM.setEmptyText("Project Manager..");
		smplcmbxPM.setAllowBlank(false);
		getNomePM();	
		
		hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
		hpLayout.add(new CntpnlDatiFatturazioneOrdine());
		hpLayout.layout();

		btnSelect= new Button();
		btnSelect.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnSelect.setToolTip("Load");
		btnSelect.setIconAlign(IconAlign.TOP);
		btnSelect.setSize(26, 26);
		btnSelect.addSelectionListener(new SelectionListener<ButtonEvent>() {		
			@Override
			public void componentSelected(ButtonEvent ce) {		
				if(smplcmbxMese.isValid()&&smplcmbxPM.isValid()&&smplcmbxAnno.isValid()){			
					hpLayout.removeAll();
					hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
					hpLayout.add(new CntpnlDatiFatturazioneOrdine());
					hpLayout.layout();
				}else Window.alert("Controllare i dati selezionati!");
			}
		});		
			
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.add(smplcmbxAnno);
		horizontalPanel.add(smplcmbxMese);
		horizontalPanel.add(smplcmbxPM);
		horizontalPanel.add(btnSelect);
		
		cntpnlFoglioFatturazione.setTopComponent(horizontalPanel);
		cntpnlFoglioFatturazione.add(hpLayout);
		 
		//cntpnlLayout.add(cntpnlFoglioFatturazione);
	    
		bodyContainer.add(cntpnlFoglioFatturazione);
	   				
		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		add(layoutContainer);
	}	
	
	
	private void reloadFoglioFatt1() {
		CntpnlDatiFatturazioneOrdine cp=new CntpnlDatiFatturazioneOrdine();
		cp=(CntpnlDatiFatturazioneOrdine) hpLayout.getItemByItemId("panelDatiFatturazione");		
		hpLayout.remove(cp);
		hpLayout.add(new CntpnlDatiFatturazioneOrdine());
		hpLayout.layout();		
	}
	
	
	private void selectLayout() {
		BodyLayout_PersonalManager lcPM = new BodyLayout_PersonalManager();	
		
		if (getParent().getParent().getParent().getParent().getClass().equals(lcPM.getClass())) {
			lcPM = (BodyLayout_PersonalManager) getParent().getParent()	.getParent().getParent();
			txtfldUsername.setValue(lcPM.txtfldUsername.getValue().toString());
			//txtRuolo.setText("PM");
		}		
		else txtfldUsername.setValue("a.b");
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
					smplcmbxPM.add(result);
					smplcmbxPM.recalculate();
					
					if(txtfldUsername.getValue().toString().compareTo("a.b")!=0){
						smplcmbxPM.setEnabled(false);
						String cognome=txtfldUsername.getValue().toString();
						String nome=cognome.substring(0,cognome.indexOf("."));
						cognome=cognome.substring(cognome.indexOf(".")+1, cognome.length());
					
						nome=(nome.substring(0,1).toUpperCase()+nome.substring(1,nome.length()));
						cognome=(cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length()));
					
						smplcmbxPM.setSimpleValue(cognome+" "+nome);
						if(smplcmbxMese.isValid()&&smplcmbxPM.isValid()&&smplcmbxAnno.isValid()){			
							hpLayout.removeAll();
							hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
							hpLayout.add(new CntpnlDatiFatturazioneOrdine());
							hpLayout.layout();
						}						
					}				
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});				
	}
	
	
	private class CntpnlRiepilogoOreDipFatturazione extends ContentPanel{
		
		private Text txtOreTotali= new Text();
		private Text txtOreLavoro= new Text();
		private Text txtOreViaggio= new Text();
		private Text txtOreStrao= new Text();
		private GroupingStore<RiepilogoOreDipFatturazione>store = new GroupingStore<RiepilogoOreDipFatturazione>();		
		private Grid<RiepilogoOreDipFatturazione> gridRiepilogo;
		private ColumnModel cm;
		private boolean nuovo=true;
		private int idDip;
		
		private Button btnShowDettaglioOre;
		private Button btnEditOreDip;
		private Button btnCheckOreFatturate;
		
		CntpnlRiepilogoOreDipFatturazione(){		
			setHeading("Riepilogo Ore (Mensile).");
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(460);
			setFrame(false);
			setScrollMode(Scroll.AUTOY);
			setItemId("panelRiepilogoOre");
			
		    try {
		    	cm = new ColumnModel(createColumns());	
			} catch (Exception e) {
				e.printStackTrace();
				Window.alert("error: Problema createColumns().");			
			}	

			store.groupBy("numeroCommessa");
			
		    GroupSummaryView summary = new GroupSummaryView();  
		    summary.setForceFit(true);  
		    summary.setShowGroupedColumn(false);
		    summary.setStartCollapsed(true);
		    summary.addListener(Events.BeforeExpand, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent be) {
					
				}
			});
		    		   	
		    gridRiepilogo= new Grid<RiepilogoOreDipFatturazione>(store, cm);  
		    gridRiepilogo.setItemId("grid");
		    gridRiepilogo.setBorders(false);
		    
		    gridRiepilogo.setStripeRows(true);  
		    gridRiepilogo.setColumnLines(true);  
		    gridRiepilogo.setColumnReordering(true);  
		    gridRiepilogo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		    gridRiepilogo.setView(summary);
		    gridRiepilogo.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreDipFatturazione>>() {  
		          public void handleEvent(SelectionChangedEvent<RiepilogoOreDipFatturazione> be) {
		        	
		            if (be.getSelection().size() > 0) {
		            	String commessa= new String();
		            	commessa=be.getSelectedItem().getNumeroCommessa();
		            		
		            	idDip=be.getSelectedItem().get("idDip");
		            	numCommessa=commessa.substring(0,commessa.indexOf("."));
		            	numEstensione=commessa.substring(commessa.indexOf(".")+1, commessa.indexOf("(")-1);		
		            		            	
		            	//hpLayout.removeAll();
		        		//hpLayout.add(new CntpnlRiepilogoOreDipFatturazione());
		        		CntpnlDatiFatturazioneOrdine cp=new CntpnlDatiFatturazioneOrdine();
		        		cp=(CntpnlDatiFatturazioneOrdine) hpLayout.getItemByItemId("panelDatiFatturazione");
		        		
		        		hpLayout.remove(cp);
		        		hpLayout.add(new CntpnlDatiFatturazioneOrdine());
		        		hpLayout.layout();	
		               		            	
		            } else {
		            	
		            }
		         }
		    });		    
		   		    
		    ToolBar tlbrRiepilogoOre= new ToolBar();
		    
		    btnShowDettaglioOre= new Button();
		    btnShowDettaglioOre.setEnabled(false);
		    btnShowDettaglioOre.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
		    btnShowDettaglioOre.setToolTip("Riepilogo Giornaliero Dettagliato");
		    btnShowDettaglioOre.setIconAlign(IconAlign.TOP);
		    btnShowDettaglioOre.setSize(26, 26);
		    btnShowDettaglioOre.addSelectionListener(new SelectionListener<ButtonEvent>() {	
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					String meseRif= new String(); 
					String anno=smplcmbxAnno.getRawValue().toString();		
					meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
															
					Dialog d= new Dialog();
					d.setSize(1500, 650);
					d.setButtons("");
					//d.add(new PanelRiepilogoGiornalieroCommesse(idDip, anno, meseRif, "1")); //tipo indica da quale layout creo la classe
					d.add(new PanelRiepilogoMeseGiornalieroCommesseHorizontal(idDip, anno, meseRif));
					
					d.show();
				}
			});
		    
		    btnEditOreDip= new Button();
		    btnEditOreDip.setEnabled(false);
		    btnEditOreDip.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.edit()));
		    btnEditOreDip.setToolTip("Assegna le ore a collaboratori e dipendenti fuori sede");
		    btnEditOreDip.setIconAlign(IconAlign.TOP);
		    btnEditOreDip.setSize(26, 26);
		    btnEditOreDip.addSelectionListener(new SelectionListener<ButtonEvent>() {	
		    	private String pm;
		    	private String periodo;
				@Override
				public void componentSelected(ButtonEvent ce) {	
					pm=smplcmbxPM.getRawValue().toString();
					String anno=smplcmbxAnno.getRawValue().toString();
					String meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					periodo=meseCorrente+anno;
					
					Dialog d= new Dialog();
					d.setSize(550, 870);
					d.setButtons("");
					d.setScrollMode(Scroll.AUTOY);
					d.setHeading("Assegna le ore a collaboratori e dipendenti fuori sede.");
					d.add(new PanelAttribuzioneOreColocationCollaboratori(pm));
					d.show();
					d.addListener(Events.Hide, new Listener<ComponentEvent>() {			     
						@Override
						public void handleEvent(ComponentEvent be) {
							caricaTabellaRiepOreDipFatturazione(periodo, pm);			
					    }
					});
				}
			});
		    
		    btnCheckOreFatturate= new Button();
		    btnCheckOreFatturate.disable();
		    btnCheckOreFatturate.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.check()));
		    btnCheckOreFatturate.setToolTip("Controllo ore Lavorate/Lavorate Registrate");
		    btnCheckOreFatturate.setIconAlign(IconAlign.TOP);
		    btnCheckOreFatturate.setSize(26, 26);
		    btnCheckOreFatturate.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					String meseRif= new String(); 
					String anno=smplcmbxAnno.getRawValue().toString();		
					meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
					String pm=smplcmbxPM.getRawValue().toString();
					
					List <RiepilogoOreDipFatturazione> listaCommesse= new ArrayList<RiepilogoOreDipFatturazione>();
					
					for(RiepilogoOreDipFatturazione r: store.getModels()){
						if(r.getDipendente().compareTo("_TOTALE")==0)
							listaCommesse.add(r);
					}
					
					DialogCheckCommesseFatturate d= new DialogCheckCommesseFatturate(pm, anno, meseRif, listaCommesse);
					d.setSize(550, 870);
					d.setButtons("");
										
					d.show();
					
				}	    	
			});
		    
		    tlbrRiepilogoOre.add(btnShowDettaglioOre);
		    tlbrRiepilogoOre.add(btnEditOreDip);
		    tlbrRiepilogoOre.add(btnCheckOreFatturate);
		    
		    txtOreTotali.setWidth(435);
		    txtOreTotali.setHeight(23);
		    txtOreTotali.setStyleAttribute("font-size", "14px");
		    txtOreLavoro.setWidth(250);	
		    txtOreLavoro.setHeight(23);
		    txtOreLavoro.setStyleAttribute("font-size", "14px");
		    txtOreStrao.setWidth(250);	
		    txtOreStrao.setHeight(23);
		    txtOreStrao.setStyleAttribute("font-size", "14px");
		    txtOreViaggio.setWidth(250);	
		    txtOreViaggio.setHeight(23);
		    txtOreViaggio.setStyleAttribute("font-size", "14px");		    
		    
		    store.setSortField("dipendente");
		    store.setSortDir(SortDir.ASC);
		    ContentPanel cntpnlGrid= new ContentPanel();
		    cntpnlGrid.setBodyBorder(false);  
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setFrame(true);
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(445);
		    cntpnlGrid.setHeight(840);
		    cntpnlGrid.setScrollMode(Scroll.AUTOY);
		    
		    cntpnlGrid.setTopComponent(tlbrRiepilogoOre);
		    cntpnlGrid.add(gridRiepilogo);
		    
		    VerticalPanel vp= new VerticalPanel();
		    vp.setWidth(440);
		    vp.setHeight(135);
		    vp.setBorders(true);
		    vp.setSpacing(3);
		    vp.setStyleAttribute("padding-top", "5px");
		    vp.add(txtOreLavoro);
		    vp.add(txtOreStrao);
		    vp.add(txtOreViaggio);
		    vp.add(txtOreTotali);		    
		    
		    cntpnlGrid.setBottomComponent(vp);
		    
		    if(!smplcmbxMese.getRawValue().isEmpty()&&!smplcmbxPM.getRawValue().isEmpty()&&!smplcmbxAnno.getRawValue().isEmpty()){
		    	String meseRif= new String(); 
		    	String anno= new String();
		    	String data= new String();
		    	anno= smplcmbxAnno.getRawValue().toString();
				meseRif=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseRif+anno;
		    	caricaTabellaRiepOreDipFatturazione(data, smplcmbxPM.getRawValue().toString());
		    }	  
		  	add(cntpnlGrid);
		  	layout();
		}
		
			
		private List<ColumnConfig> createColumns() {
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number = NumberFormat.getFormat("0.00");
			
			SummaryColumnConfig<Double> column=new SummaryColumnConfig<Double>();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(140);  
		    column.setRowHeader(true);  
		    configs.add(column); 
		    
		    column=new SummaryColumnConfig<Double>();		
		    column.setId("dipendente");  
			column.setHeader("Dipendente");  
			column.setWidth(90);  
			column.setRowHeader(true); 
		    configs.add(column); 
		    	    
		    SummaryColumnConfig<Double> columnOreLavoro=new SummaryColumnConfig<Double>();		
		    columnOreLavoro.setId("oreLavoro");  
		    columnOreLavoro.setHeader("h/Lavoro");  
		    columnOreLavoro.setWidth(55);    
		    columnOreLavoro.setRowHeader(true); 
		    columnOreLavoro.setAlignment(HorizontalAlignment.LEFT);  
		    columnOreLavoro.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
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
		    configs.add(columnOreLavoro); 	
		    
		    SummaryColumnConfig<Double> columnOreViaggio=new SummaryColumnConfig<Double>();		
		    columnOreViaggio.setId("oreViaggio");  
		    columnOreViaggio.setHeader("h/Viaggio");  
		    columnOreViaggio.setWidth(55);    
		    columnOreViaggio.setRowHeader(true); 
		    columnOreViaggio.setAlignment(HorizontalAlignment.LEFT);    
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
		    configs.add(columnOreViaggio); 
		    
		    SummaryColumnConfig<Double> columnOreStrao=new SummaryColumnConfig<Double>();		
		    columnOreStrao.setId("oreStraordinario");  
		    columnOreStrao.setHeader("h/Strao");  
		    columnOreStrao.setWidth(55);    
		    columnOreStrao.setRowHeader(true); 
		    columnOreStrao.setAlignment(HorizontalAlignment.LEFT);    
		    columnOreStrao.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
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
		    configs.add(columnOreStrao); 
		    
		    SummaryColumnConfig<Double> columnOreTotali=new SummaryColumnConfig<Double>();		
		    columnOreTotali.setId("oreTotali");  
		    columnOreTotali.setHeader("h/Tot");  
		    columnOreTotali.setWidth(55);    
		    columnOreTotali.setRowHeader(true); 
		    columnOreTotali.setAlignment(HorizontalAlignment.LEFT);    
		    columnOreTotali.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
				@Override
				public Object render(RiepilogoOreDipFatturazione model,
						String property, ColumnData config, int rowIndex,
						int colIndex,
						ListStore<RiepilogoOreDipFatturazione> store,
						Grid<RiepilogoOreDipFatturazione> grid) {
					Float n=model.get(property);
					
					String dipendente=model.get("dipendente");
					if(dipendente.compareTo("_TOTALE")==0)
						config.style = config.style + "font-weight:bold;";
					else
						config.style = config.style + "font-weight:normal;";
					
					return number.format(n);
				}			
			});    
		    configs.add(columnOreTotali); 	
		    
		    /*SummaryColumnConfig<Double> columnOreTotaliIU=new SummaryColumnConfig<Double>();		
		    columnOreTotaliIU.setId("oreTotaliIU");  
		    columnOreTotaliIU.setHeader("Totale I/U");  
		    columnOreTotaliIU.setWidth(63);    
		    columnOreTotaliIU.setRowHeader(true); 
		    columnOreTotaliIU.setAlignment(HorizontalAlignment.LEFT);    
		    columnOreTotaliIU.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {
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
		    configs.add(columnOreTotaliIU);
		    */
		    
		    column=new SummaryColumnConfig<Double>();
		    column.setId("checkOre");
			column.setHeader("Check");
			column.setWidth(30);
			column.setRowHeader(true);
			column.setRenderer(new GridCellRenderer<RiepilogoOreDipFatturazione>() {

					@Override
					public Object render(RiepilogoOreDipFatturazione model, String property,
							ColumnData config, int rowIndex, int colIndex,
							ListStore<RiepilogoOreDipFatturazione> store, Grid<RiepilogoOreDipFatturazione> grid) {
						
						String t= model.getDipendente();
						
						if(t.compareTo("_TOTALE")==0){
							config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
						}
						else
						if(t.compareTo("_TOTALE")!=0){
							Boolean check=model.get("checkOre");
							if(check!=null)
							if(check)
								config.style = config.style + ";background-color:" + "#90EE90" + ";";									
							else
								config.style = config.style + ";background-color:" + "#F08080" + ";";
						}//else 
							//config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
						return "";
					}
				});
		    configs.add(column);  
		    
			return configs;
		}
	
		private void caricaTabellaRiepOreDipFatturazione(String meseRif, String pm) {
			
			AdministrationService.Util.getInstance().getRiepilogoOreDipFatturazione(meseRif, pm, new AsyncCallback<List<RiepilogoOreDipFatturazione>>() {	
				@Override
				public void onSuccess(List<RiepilogoOreDipFatturazione> result) {
					if(result==null)
						Window.alert("error: Problemi durante l'accesso ai dati del riepilogo ore.");
					else	
						if(result.size()==0){
							Window.alert("Nessun dato (ore lavoro) rilevato in base ai criteri di ricerca selezionati.");
							trovato=false;
						}
						else loadTable(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getRiepilogoOreDipFatturazione();");
					caught.printStackTrace();		
				}
			}); //AsyncCallback	  		
		}
		
		
		private void loadTable(List<RiepilogoOreDipFatturazione> result) {
		
			try {
				btnEditOreDip.enable();
				btnShowDettaglioOre.enable();
				btnCheckOreFatturate.enable();
				store.removeAll();
				store.add(result);
				store.groupBy("numeroCommessa");
				gridRiepilogo.reconfigure(store, cm);	
				
				String totale="0.00";
				String orel="0.00";
				String orev="0.00";
				String oreS="0.00";
				Float totVariato=(float)0; //è il totale con le maggiorazioni/diminuzioni ore strao viaggio
				
				NumberFormat number = NumberFormat.getFormat("0.00");
				
				for(RiepilogoOreDipFatturazione r:result){
					if(r.getDipendente().compareTo("_TOTALE")==0){
						totale=ClientUtility.aggiornaTotGenerale(totale, number.format(r.getOreTotali()));
						orel=ClientUtility.aggiornaTotGenerale(orel, number.format(r.getOreLavoro()));
						orev=ClientUtility.aggiornaTotGenerale(orev, number.format(r.getOreViaggio()));
						oreS=ClientUtility.aggiornaTotGenerale(oreS, number.format((Float) r.get("oreStraordinario")));
					}
				}
				
				totVariato=(float) (Float.valueOf(ClientUtility.getOreCentesimi(orel))+
						(Float.valueOf(ClientUtility.getOreCentesimi(orev))*0.85)+(Float.valueOf(ClientUtility.getOreCentesimi(oreS))*1.2));
				
				//txtOreTotali.setText("Totale: "+totale +" (Con oreViaggio/Ore Strao. corrette: "+ number.format(totVariato) +")");
				txtOreTotali.setText("Totale: "+totale);
				txtOreViaggio.setText("Tot.Ore viaggio: "+ orev);
				txtOreLavoro.setText("Tot.Ore ordinarie lavoro: "+orel);
				txtOreStrao.setText("Tot.Ore strao. lavoro: "+oreS);
				
			} catch (NullPointerException e) {
				Window.alert("error: Impossibile effettuare il caricamento dati in tabella.");
					e.printStackTrace();
			}
		}
	}
	
	
	private class CntpnlDatiFatturazioneOrdine extends ContentPanel{	
		private boolean nuovo =true;
		private String commessaSelezionata=new String();
		private TextField<String> txtfldSALIniziale= new TextField<String>();
		private TextField<String> txtfldPCLIniziale= new TextField<String>();
		private TextField<String> txtfldTotOre= new TextField<String>();
		private TextField<String> txtfldOreOrdine= new TextField<String>();
		private TextField<String> txtfldOreResiduoOrdine= new TextField<String>();
		private TextField<String> txtfldCostoOrario= new TextField<String>();		
		private TextField<String> txtfldOreDaFatturare= new TextField<String>();
		private TextField<String> txtfldImportoDaFatturare= new TextField<String>();
		private TextField<String> txtfldVariazioneSAL= new TextField<String>();
		private TextField<String> txtfldVariazionePCL= new TextField<String>();
		private TextField<String> txtfldOreScaricate= new TextField<String>();
		private TextField<String> txtfldDiffScaricateEseguite= new TextField<String>();
		private TextField<String> txtfldTotFatturato= new TextField<String>();
		private TextField<String> txtfldOreEseguiteRegistrate= new TextField<String>();
		private TextField<String> txtSalTotale= new TextField<String>();
		private TextField<String> txtPclTotale= new TextField<String>();
		private TextField<String> txtfldImportoOrdine= new TextField<String>();
		private TextField<String> txtfldImportoResiduo= new TextField<String>();
		private TextField<String> txtfldOreRimborsoSpese= new TextField<String>();
		
		private Text txtVuoto1= new Text();
		private Text txtTotCompensato= new Text();
		private CheckBox chbxSalButtare;
		private TextField<String> txtfldImportoRtv= new TextField<String>();
		
		private Text txtOreDaFatturare= new Text();
		private Text txtVariazioneSal= new Text();
		private Text txtVariazionePcl=new Text();
		private Text txtOreScaricate = new Text();
		private Text txtMargine= new Text();
		private Text txtEfficienza= new Text();
		//private TextField<String> txtfldOreEseguiteEffettive= new TextField<String>();
		
		private TextArea txtaNote=new TextArea();
		private Button btnRiep= new Button();
		private Button btnAssegnaImporto;
		
		private ColumnModel cmOrdine;
		private Grid<RiepilogoOreTotaliCommesse> gridOrdine; 
		
		CntpnlDatiFatturazioneOrdine(){
			
			NumberFormat number= NumberFormat.getFormat("0.00");
			
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(1000);
			setHeight(780);
			setFrame(false);
			setItemId("panelDatiFatturazione");
			
			VerticalPanel vp=new VerticalPanel();
			vp.setItemId("vp");
			vp.setBorders(false);
			vp.setSpacing(4);
			vp.setStyleAttribute("margin-top", "-5px");
			
			if(smplcmbxMese.getRawValue().toString().compareTo("")!=0)
				caricaTabellaDati();
			
			//Tabella contenente elenco ordini
			ContentPanel cntpnlGrid = new ContentPanel();  
		    cntpnlGrid.setBodyBorder(false);
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setHeading("Dati Ordini");  
		    cntpnlGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(880);
		    cntpnlGrid.setHeight(320);
		    cntpnlGrid.setScrollMode(Scroll.AUTO);
		
		    ToolBar tlbRiepiloghi= new ToolBar();
		    
		    btnSalva= new Button();
		    btnSalva.setEnabled(false);
		    btnSalva.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.save()));
		    btnSalva.setToolTip("Salva");
		    btnSalva.setIconAlign(IconAlign.TOP);
		    btnSalva.setSize(26, 26);
		    btnSalva.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					float i=Float.valueOf(txtfldImportoDaFatturare.getValue().toString());
					float a=Float.valueOf(txtfldImportoResiduo.getValue().toString());
					float oreFatturate=Float.valueOf(txtfldOreDaFatturare.getValue().toString());
				
					/*if(!checkOreImportoOrdine())
						Window.alert("I valori di ore residue e importo residuo sull'ordine sono discordanti! Effettuare eventuali modifiche se necessario.");
					else*/	
					if(oreFatturate==0 && i>0)
						Window.alert("E' stato inserito un importo da fatturare ma non sono state inserite le ore!");
					else
					if(i>a)
						Window.alert("L'importo da fatturare non puo' essere maggiore dell' importo residuo!");
					else
					if(Float.valueOf(txtfldOreDaFatturare.getValue().toString())!=0 && Float.valueOf(txtfldImportoDaFatturare.getValue().toString())==0
							&& Float.valueOf(txtfldImportoOrdine.getValue().toString())!=0)
						Window.alert("L'importo da fatturare non puo' essere 0!");
					else
						//controllo per impedire caricamento ore sal se ordine esaurito
					if(!ClientUtility.checkOreOrdineOreSal(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString(), 
							txtfldOreResiduoOrdine.getValue().toString())&&(txtfldOreOrdine.getValue().toString().compareTo("0.0")!=0))
						Window.alert("Non e' possibile inserire una variazione di SAL a chiusura ordine!");
					else
					if(txtfldOreDaFatturare.isValid()&&txtfldVariazioneSAL.isValid()&&txtfldVariazionePCL.isValid()
							&&txtfldImportoDaFatturare.isValid()){
						
						String oreEseguite= txtfldOreEseguiteRegistrate.getValue().toString();		
						String salIniziale= txtfldSALIniziale.getValue().toString();
						String pclIniziale= txtfldPCLIniziale.getValue().toString();
						String oreFatturare=txtfldOreDaFatturare.getValue().toString();
						String variazioneSAL= txtfldVariazioneSAL.getValue().toString();
						String variazionePCL= txtfldVariazionePCL.getValue().toString();
						String tariffaUtilizzata=txtfldCostoOrario.getValue().toString();
						String importoDaFatturare=txtfldImportoDaFatturare.getValue().toString();
						String oreRimborsoSpese= txtfldOreRimborsoSpese.getValue().toString();
						String salDaButtare= new String();
						boolean salButtato= chbxSalButtare.getValue();
																		
						String meseCorrente=new String();
						String anno=new String();
						String data= new String();
						String note= new String();
						String statoElaborazione="1"; //se differenziare salvataggio PM da salvataggioUA allora usare 1 o 2
						
						if(txtaNote.getValue()==null)
							note="";
						else note=txtaNote.getValue().toString();
						
						if(salButtato)
							salDaButtare="S";
						else
							salDaButtare="N";
						
						anno=smplcmbxAnno.getRawValue().toString();
						meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
						data=meseCorrente+anno;
											
						if(nuovo&&(Float.valueOf(oreFatturare)>Float.valueOf(txtfldOreResiduoOrdine.getValue().toString())))
							Window.alert("E' stato indicato un numero di ore da fatturare maggiore del numero di ore residue sull'ordine!");
						else
							AdministrationService.Util.getInstance().insertDatiFoglioFatturazione(oreEseguite,salIniziale,pclIniziale, oreFatturare, importoDaFatturare, variazioneSAL,
								variazionePCL, data, note, statoElaborazione, commessaSelezionata, tariffaUtilizzata, salDaButtare, idAttivita, oreRimborsoSpese, new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Problema di connessione on insertDatiFoglioFatturazione();");
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(Boolean result) {
										if(result){
											Window.alert("Inserimento avvenuto con successo.");
											reloadFoglioFatt1();
											
										}else{
											Window.alert("error: Impossibile inserire i dati del foglio fatturazione!");
										}
									}
							});	
					}			
				}

				private boolean checkOreImportoOrdine() {
					
					float oreRes=Float.valueOf(txtfldOreResiduoOrdine.getValue().toString()) - Float.valueOf(txtfldOreDaFatturare.getValue().toString());
					float importoRes=Float.valueOf(txtfldImportoResiduo.getValue().toString()) - Float.valueOf(txtfldImportoDaFatturare.getValue().toString());
					
					if(((oreRes!=0 && importoRes==0)||(oreRes==0 && importoRes!=0)) && Float.valueOf(txtfldImportoOrdine.getValue().toString())!=0)					
						return false;
					else 
						return true;
				}
			});		    
		    
		    btnRiep.setEnabled(false);
		    btnRiep.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
		    btnRiep.setToolTip("Riepilogo Mesi Precedenti");
		    btnRiep.setIconAlign(IconAlign.TOP);
		    btnRiep.setSize(26, 26);
		    
			final ListStore<RiepilogoOreTotaliCommesse> store = new ListStore<RiepilogoOreTotaliCommesse>();  
			cmOrdine = new ColumnModel(createColumns());		
		    		         
			gridOrdine = new Grid<RiepilogoOreTotaliCommesse>(store, cmOrdine);   
		    gridOrdine.setBorders(false);  
		    gridOrdine.setStripeRows(true);  
		    gridOrdine.setColumnLines(true);  
		    gridOrdine.setColumnReordering(true);  
		    gridOrdine.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		    
		    gridOrdine.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<RiepilogoOreTotaliCommesse>>() {  
		          public void handleEvent(SelectionChangedEvent<RiepilogoOreTotaliCommesse> be) {  		        	
		            if (be.getSelection().size() > 0) {  
		            	if(be.getSelectedItem().getNumeroCommessa().compareTo("TOTALE")!=0){
		            		String numeroC= new String();
		            		String estensione=new String();
		            		idAttivita=be.getSelectedItem().get("idAttivita");
		            		numeroC=be.getSelectedItem().getNumeroCommessa();
		            		estensione=be.getSelectedItem().getEstensione();
		            		try{		            			
		            			caricaDatiFatturazioneOrdine(numeroC+"."+estensione, idAttivita);
		            		}catch (Exception e) {
		            			e.printStackTrace();
		        				System.out.println("errore carica dati fatturazione");
							}	            		
		            		commessaSelezionata=(numeroC+"."+be.getSelectedItem().getEstensione());
		            		btnSalva.setEnabled(true);
		            		btnRiep.setEnabled(true);
		            		
		            	}
		             }
		          }
		    });
					   
		    btnRiep.addSelectionListener(new SelectionListener<ButtonEvent>() {			
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					DialogRiepilogoDatiFoglioFatturazione d= new DialogRiepilogoDatiFoglioFatturazione(commessaSelezionata);
					d.show();			
				}
			});
		    	    
		    tlbRiepiloghi.add(btnRiep);
		    tlbRiepiloghi.add(btnSalva);
		    
		    cntpnlGrid.setTopComponent(tlbRiepiloghi);
		    cntpnlGrid.add(gridOrdine);
			    	    
			FieldSet fldsetRiepOreOrdine=new FieldSet();
			fldsetRiepOreOrdine.setCollapsible(false);
			fldsetRiepOreOrdine.setExpanded(true);
			fldsetRiepOreOrdine.setHeading("Ordine da Fatturare:");
			
			vp.add(fldsetRiepOreOrdine);
			
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(575, 110);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
			cp.setStyleAttribute("padding-left", "5px");
			cp.setStyleAttribute("padding-top", "5px");
			
			LayoutContainer layoutCol1=new LayoutContainer();
			LayoutContainer layoutCol2=new LayoutContainer();
			LayoutContainer layoutCol3=new LayoutContainer();
			
			FormLayout layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol1.setLayout(layout);
					
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol2.setLayout(layout);
			
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutCol3.setLayout(layout);
			
			txtfldSALIniziale.setFieldLabel("SAL iniziale");
			txtfldSALIniziale.setEnabled(false);
			
			txtfldPCLIniziale.setFieldLabel("PCL iniziale");
			txtfldPCLIniziale.setEnabled(false);
			
			txtfldTotOre.setFieldLabel("Totale");
			txtfldTotOre.setEnabled(false);
			
			txtfldOreOrdine.setFieldLabel("Ore Ordine");
			txtfldOreOrdine.setEnabled(false);
			
			txtfldOreResiduoOrdine.setFieldLabel("Ore Residue");
			txtfldOreResiduoOrdine.setEnabled(false);
			
			txtfldImportoOrdine.setFieldLabel("Importo Ord.");
			txtfldImportoOrdine.setEnabled(false);
			
			txtfldImportoResiduo.setFieldLabel("Importo Res.");
			txtfldImportoResiduo.setEnabled(false);
			
			txtTotCompensato.setStyleAttribute("padding-top", "10px");
			
			txtfldCostoOrario.setFieldLabel("Tariffa Oraria");
			txtfldCostoOrario.setEnabled(false);
			txtfldCostoOrario.setAllowBlank(false);
			//txtfldCostoOrario.setRegex("[1-9]{1}[0-9]+[.]{1}[0-9]+|[1-9]{1}[.]{1}[0-9]+");
			txtfldCostoOrario.setValue("0.00");
			txtfldCostoOrario.addKeyListener(new KeyListener(){
				public void componentKeyDown(ComponentEvent event) {
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){
						
						if(txtfldCostoOrario.getValue()==null)
							txtfldCostoOrario.setValue("0.00");
						else{
							String valore= txtfldCostoOrario.getValue().toString();
													
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
							txtfldCostoOrario.setValue(valore);
						}						
					}
				}
			});
			
			txtfldOreEseguiteRegistrate.setFieldLabel("Ore Eseguite");
			txtfldOreEseguiteRegistrate.setEnabled(true);
			txtfldOreEseguiteRegistrate.setValue("0.00");
			txtfldOreEseguiteRegistrate.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");
			//txtfldOreEseguiteRegistrate.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldOreEseguiteRegistrate.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldOreEseguiteRegistrate.addKeyListener(new KeyListener(){
				
				public void componentKeyUp(ComponentEvent event) {
		    	  	if(hasValue(txtfldOreEseguiteRegistrate)&&txtfldOreEseguiteRegistrate.getValue()!=null){
		    	  		txtfldOreEseguiteRegistrate.clearInvalid();
		    	  		String scaricate= new String();
		    	  		String delta= new String();
		    	  		String totaleEuro= new String();
		    	  		String variazionePCL= new String();
		    	  		Float efficienza= (float)0.00;
		    	  		NumberFormat number = NumberFormat.getFormat("0.00");
		    	  		//number=NumberFormat.getCurrencyFormat("EUR");
		    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
		    	  		
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
		    	  		txtfldOreScaricate.setValue(scaricate);
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
		    	  		//totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreScaricate.getValue().toString()));
		    	  		txtOreScaricate.setText("("+totaleEuro+")");
		    	  		
		    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
		    	  		txtfldDiffScaricateEseguite.setValue(delta);
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
		    	  		//totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldDiffScaricateEseguite.getValue().toString()));
		    	  		txtMargine.setText("("+totaleEuro+")");
						efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
		    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
		    	  		
		    	  		//totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldOreDaFatturare.getValue().toString()));	
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreDaFatturare.getValue().toString()));
						txtfldTotFatturato.setValue(totaleEuro);
						txtOreDaFatturare.setText("("+totaleEuro+")");
		    	  	}
				}
				
				public void componentKeyDown(ComponentEvent event) {
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){
							
							if(txtfldOreEseguiteRegistrate.getValue()==null)
								txtfldOreEseguiteRegistrate.setValue("0.00");
							else{
								String valore= txtfldOreEseguiteRegistrate.getValue().toString();
														
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
								txtfldOreEseguiteRegistrate.setValue(valore);
							}
							
							if(hasValue(txtfldOreEseguiteRegistrate)&& 
									(Float.valueOf(txtfldOreEseguiteRegistrate.getValue().toString())<=Float.valueOf(txtfldOreResiduoOrdine.getValue().toString())))
								txtfldOreDaFatturare.setValue(txtfldOreEseguiteRegistrate.getValue().toString());
								
						}
				 }
			});
								
			layoutCol1.add(txtfldImportoOrdine, new FormData("95%"));
			layoutCol1.add(txtfldImportoResiduo, new FormData("95%"));
			layoutCol1.add(txtVuoto1, new FormData("95%"));
			layoutCol1.add(txtTotCompensato, new FormData("95%"));
						
			layoutCol2.add(txtfldOreOrdine, new FormData("95%"));
			layoutCol2.add(txtfldOreResiduoOrdine, new FormData("95%"));
			layoutCol2.add(txtfldCostoOrario, new FormData("95%"));
			layoutCol2.add(txtfldOreEseguiteRegistrate, new FormData("95%"));		
			
			layoutCol3.add(txtfldSALIniziale, new FormData("95%"));
			layoutCol3.add(txtfldPCLIniziale, new FormData("95%"));
			layoutCol3.add(txtSalTotale, new FormData("95%"));
			layoutCol3.add(txtPclTotale, new FormData("95%"));
			
			RowData data = new RowData(.33, 1);
			data.setMargins(new Margins(5));
			
			cp.add(layoutCol2, data);
			cp.add(layoutCol1, data);
			cp.add(layoutCol3, data);
			
			VerticalPanel hp1=new VerticalPanel();
			hp1.add(cntpnlGrid);
			hp1.add(cp);
						
			fldsetRiepOreOrdine.add(hp1);
				
			FieldSet fldsetFattura=new FieldSet();
			fldsetFattura.setCollapsible(false);
			fldsetFattura.setExpanded(true);
			fldsetFattura.setHeading("Dati Fatturazione:");
			
			vp.add(fldsetFattura);
			
			ContentPanel cp1=new ContentPanel();
			cp1.setHeaderVisible(false);
			cp1.setSize(855, 90);
			cp1.setBorders(false);
			cp1.setBodyBorder(false);
			cp1.setFrame(false);
			cp1.setLayout(new RowLayout(Orientation.HORIZONTAL));
							
			txtSalTotale.setValue("0.00");
			txtSalTotale.setFieldLabel("SAL Totale");
			txtSalTotale.setEnabled(false);
						
			txtPclTotale.setValue("0.00");
			txtPclTotale.setFieldLabel("PCL Totale");
			txtPclTotale.setEnabled(false);
			
			txtfldImportoDaFatturare.setAllowBlank(false);
			txtfldImportoDaFatturare.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			//txtfldImportoDaFatturare.setValue("0.00");
			txtfldImportoDaFatturare.setFieldLabel("Importo da Fatturare");
			txtfldImportoDaFatturare.setEmptyText("Importo da Fatturare...");
			txtfldImportoDaFatturare.setWidth(140);
			
			txtfldOreDaFatturare.setFieldLabel("Ore da Fatturare");
			txtfldOreDaFatturare.setEnabled(true);
			txtfldOreDaFatturare.setAllowBlank(false);
			txtfldOreDaFatturare.setValue("0.00");
			txtfldOreDaFatturare.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");		
			//.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldOreDaFatturare.getMessages().setRegexText("Minuti accettati: 15,30,45");
			txtfldOreDaFatturare.addKeyListener(new KeyListener(){
			      public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldOreDaFatturare)&&txtfldOreDaFatturare.getValue()!=null){
			    	  		txtfldOreDaFatturare.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String totaleEuro= new String();
			    	  		String variazionePCL= new String();
			    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
			    	  		
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		//number=NumberFormat.getCurrencyFormat("EUR");
			    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
							
			    	  		Float efficienza=(float)0.00;
			    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
			    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
			    	  		
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreDaFatturare.getValue().toString()));				
							txtfldTotFatturato.setValue(totaleEuro);
							txtOreDaFatturare.setText("("+totaleEuro+")");
			    	  	}
			      }
			      
			      public void componentKeyDown(ComponentEvent event) {
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){
							
							if(txtfldOreDaFatturare.getValue()==null)
								txtfldOreDaFatturare.setValue("0.00");
							else{
								String valore= txtfldOreDaFatturare.getValue().toString();
								
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
								txtfldOreDaFatturare.setValue(valore);
							}
							if(hasValue(txtfldOreDaFatturare)&&txtfldOreDaFatturare.getValue()!=null){
				    	  		txtfldOreDaFatturare.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String totaleEuro= new String();
				    	  		String variazionePCL= new String();
				    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
				    	  		
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		//number=NumberFormat.getCurrencyFormat("EUR");
				    	  		variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
								
				    	  		Float efficienza=(float)0.00;
				    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
				    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
				    	  		
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreDaFatturare.getValue().toString()));				
								txtfldTotFatturato.setValue(totaleEuro);
								txtOreDaFatturare.setText("("+totaleEuro+")");
				    	  	}
						}
			      }
			});
			
			txtfldVariazioneSAL.setFieldLabel("Variazione SAL");
			txtfldVariazioneSAL.setEnabled(true);
			txtfldVariazioneSAL.setAllowBlank(false);
			txtfldVariazioneSAL.setValue("0.00");
			txtfldVariazioneSAL.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");		
			//.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			//txtfldVariazioneSAL.setRegex("[0-9]+[.]{1}(0|15|30|45)|[-]{1}[0-9]+[.]{1}(0|15|30|45)|[0-9]+[.]{1}[0]{2}|[-]{1}[0-9]+[.]{1}[0]{2}");
			txtfldVariazioneSAL.addKeyListener(new KeyListener(){
				 public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldVariazioneSAL)&&txtfldVariazioneSAL.getValue()!=null){
			    	  		txtfldVariazioneSAL.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String variazionePCL= new String();
			    	  		String totaleEuro= new String();
			    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
			    	  		
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		
			    	  		txtSalTotale.setValue("0.00");
			    	  		
			    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		Float efficienza=(float)0.00;
			    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
			    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
			    	  		
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
			    	  		txtVariazioneSal.setText("("+totaleEuro+")");
			    	  		
			    	  		txtSalTotale.setValue(ClientUtility.aggiornaTotGenerale(txtfldSALIniziale.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
			    	  	}
			      }
				 
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldVariazioneSAL.getValue()==null)
								txtfldVariazioneSAL.setValue("0.00");
							else{
								String valore= txtfldVariazioneSAL.getValue().toString();
														
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
								txtfldVariazioneSAL.setValue(valore);
							}
							
							if(hasValue(txtfldVariazioneSAL)&&txtfldVariazioneSAL.getValue()!=null){
				    	  		txtfldVariazioneSAL.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String variazionePCL= new String();
				    	  		String totaleEuro= new String();
				    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
				    	  		
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		
				    	  		txtSalTotale.setValue("0.00");
				    	  		
				    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
				    	  		
				    	  		Float efficienza=(float)0.00;
				    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
				    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
				    	  		
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
				    	  		txtVariazioneSal.setText("("+totaleEuro+")");
				    	  		
				    	  		txtSalTotale.setValue(ClientUtility.aggiornaTotGenerale(txtfldSALIniziale.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
				    	  	}
						}	    		
				 }				 
			});
			
			txtfldVariazionePCL.setFieldLabel("Variazione PCL");
			txtfldVariazionePCL.setEnabled(true);
			txtfldVariazionePCL.setAllowBlank(false);
			txtfldVariazionePCL.setValue("0.00");
			txtfldVariazionePCL.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");
			//txtfldVariazionePCL.setRegex("[0-9]+[.]{1}(0|15|30|45)|[-]{1}[0-9]+[.]{1}(0|15|30|45)|[0-9]+[.]{1}[0]{2}|[-]{1}[0-9]+[.]{1}[0]{2}");	
			//.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldVariazionePCL.getMessages().setRegexText("Minuti accettati: 0,15,30,45");
			txtfldVariazionePCL.addKeyListener(new KeyListener(){
				 public void componentKeyUp(ComponentEvent event) {
			    	  	if(hasValue(txtfldVariazioneSAL)&&txtfldVariazionePCL.getValue()!=null){
			    	  		txtfldVariazionePCL.clearInvalid();
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String variazionePCL= new String();
			    	  		String totaleEuro=new String();
			    	  		//String decimali= new String();
			    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
			    	  		
			    	  		txtPclTotale.setValue("0.00");
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		//number=NumberFormat.getCurrencyFormat("EUR");
			    	  		
			    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		Float efficienza=(float)0.00;
			    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
			    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
			    	  		
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
			    	  		//totaleEuro=number.format(Float.valueOf(txtfldCostoOrario.getValue().toString())*Float.valueOf(txtfldVariazionePCL.getValue().toString()));
			    	  		txtVariazionePcl.setText("("+totaleEuro+")");
			    	  		
			    	  		txtPclTotale.setValue(ClientUtility.aggiornaTotGenerale(txtfldPCLIniziale.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
			    	  	}
			      }
				 
				 public void componentKeyDown(ComponentEvent event) { 	  
				    	int keyCode=event.getKeyCode();
						if(keyCode==9){			
							
							if(txtfldVariazionePCL.getValue()==null)
								txtfldVariazionePCL.setValue("0.00");
							else{
								String valore= txtfldVariazionePCL.getValue().toString();
														
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
								txtfldVariazionePCL.setValue(valore);
							}
							
							if(hasValue(txtfldVariazioneSAL)&&txtfldVariazionePCL.getValue()!=null){
				    	  		txtfldVariazionePCL.clearInvalid();
				    	  		String scaricate= new String();
				    	  		String delta= new String();
				    	  		String variazionePCL= new String();
				    	  		String totaleEuro=new String();
				    	  		//String decimali= new String();
				    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();
				    	  		
				    	  		txtPclTotale.setValue("0.00");
				    	  		NumberFormat number = NumberFormat.getFormat("0.00");
				    	  		//number=NumberFormat.getCurrencyFormat("EUR");
				    	  		
				    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
				    	  					    	  		
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
				    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
				    	  		txtfldOreScaricate.setValue(scaricate);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
				    	  		txtOreScaricate.setText("("+totaleEuro+")");
				    	  		
				    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
				    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
				    	  		
				    	  		txtfldDiffScaricateEseguite.setValue(delta);
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
				    	  		txtMargine.setText("("+totaleEuro+")");
				    	  		
				    	  		Float efficienza=(float)0.00;
				    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
				    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
				    	  		
				    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
				    	  		txtVariazionePcl.setText("("+totaleEuro+")");
				    	  		
				    	  		txtPclTotale.setValue(ClientUtility.aggiornaTotGenerale(txtfldPCLIniziale.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
				    	  					    	  					    	  		
				    	  	}
						}	    		
				 }
			});
			
			
			txtfldOreRimborsoSpese.setFieldLabel("h/Rimborso Spese");
			txtfldOreRimborsoSpese.setEnabled(true);
			txtfldOreRimborsoSpese.setAllowBlank(false);
			txtfldOreRimborsoSpese.setValue("0.00");
			txtfldOreRimborsoSpese.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");
			//txtfldOreRimborsoSpese.setRegex("[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[-]{1}[0-9]+[.]{1}[0-5]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|[-]{1}[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldOreRimborsoSpese.getMessages().setRegexText("Deve essere un numero nel formato 99.59 o -99.59");
			txtfldOreRimborsoSpese.addKeyListener(new KeyListener(){
				 public void componentKeyUp(ComponentEvent event) {
					 if(hasValue(txtfldOreRimborsoSpese)){
			    	  		
			    	  		String scaricate= new String();
			    	  		String delta= new String();
			    	  		String variazionePCL= new String();
			    	  		String totaleEuro=new String();
			    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();	    	  		
			    	  		
			    	  		txtPclTotale.setValue("0.00");
			    	  		NumberFormat number = NumberFormat.getFormat("0.00");
			    	  		//number=NumberFormat.getCurrencyFormat("EUR");
			    	  		
			    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
			    	  					    	  		
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
			    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
			    	  		txtfldOreScaricate.setValue(scaricate);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
			    	  		txtOreScaricate.setText("("+totaleEuro+")");
			    	  		
			    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
			    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
			    	  		txtfldDiffScaricateEseguite.setValue(delta);
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		Float efficienza=(float)0.00;
			    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
			    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
			    	  				    	  		
			    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
			    	  		txtMargine.setText("("+totaleEuro+")");
			    	  		
			    	  		efficienza=(float)0.00;
			    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
			    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
			    	  		
			    	  	}
				 }	
				 
				 public void componentKeyDown(ComponentEvent event){ 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){										
						if(txtfldOreRimborsoSpese.getValue()==null)
							txtfldOreRimborsoSpese.setValue("0.00");
						else{
							String valore= txtfldOreRimborsoSpese.getValue().toString();
							
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
							txtfldOreRimborsoSpese.setValue(valore);
						}
					}
					
					if(hasValue(txtfldOreRimborsoSpese)){
		    	  		
		    	  		String scaricate= new String();
		    	  		String delta= new String();
		    	  		String variazionePCL= new String();
		    	  		String totaleEuro=new String();
		    	  		String oreRimbSpese=txtfldOreRimborsoSpese.getRawValue().toString();	    	  		
		    	  		
		    	  		txtPclTotale.setValue("0.00");
		    	  		NumberFormat number = NumberFormat.getFormat("0.00");
		    	  		//number=NumberFormat.getCurrencyFormat("EUR");
		    	  		
		    	  		//variazionePCL=String.valueOf(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
		    	  		variazionePCL=number.format(Float.valueOf(txtfldVariazionePCL.getValue().toString())*(-1));
		    	  					    	  		
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(txtfldOreDaFatturare.getValue().toString(), txtfldVariazioneSAL.getValue().toString());
		    	  		scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
		    	  		txtfldOreScaricate.setValue(scaricate);
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
		    	  		txtOreScaricate.setText("("+totaleEuro+")");
		    	  		
		    	  		delta=ClientUtility.calcoloDelta(scaricate, txtfldOreEseguiteRegistrate.getValue().toString());
		    	  		delta=ClientUtility.calcoloDelta(delta, oreRimbSpese);
		    	  		txtfldDiffScaricateEseguite.setValue(delta);
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
		    	  		txtMargine.setText("("+totaleEuro+")");
		    	  		
		    	  		Float efficienza=(float)0.00;
		    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
		    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
		    	  				    	  		
		    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
		    	  		txtMargine.setText("("+totaleEuro+")");
		    	  		
		    	  		efficienza=(float)0.00;
		    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
		    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
		    	  		
		    	  	}					
				}				
			});			
			
			txtfldOreScaricate.setFieldLabel("Ore Scaricate");
			txtfldOreScaricate.setEnabled(false);
			
			txtfldDiffScaricateEseguite.setFieldLabel("Diff. Scar./Eseg.");
			txtfldDiffScaricateEseguite.setEnabled(false);
			txtfldDiffScaricateEseguite.setValue("0.00");
			
			txtfldTotFatturato.setFieldLabel("Totale(Euro)");
			txtfldTotFatturato.setEnabled(false);
			
			txtaNote.setFieldLabel("Note per la fatturazione");
			txtaNote.setHeight(105);
			txtaNote.setMaxLength(500);
			
			chbxSalButtare= new CheckBox();
			chbxSalButtare.setValue(false);
			//chbxSalButtare.setStyleAttribute("padding-top", "15px");
			chbxSalButtare.setFieldLabel("Sal da scartare");
			
			String r= new String();
			r=ruolo;
			if(r.compareTo("PM")==0)
				chbxSalButtare.setVisible(false);
			chbxSalButtare.addListener(Events.OnClick, new Listener<ComponentEvent>() {
	
				@Override
				public void handleEvent(ComponentEvent be) {
					String salTotale= new String();
					//String varSal= new String();
					
					boolean check= chbxSalButtare.getValue();
					if(check){
						salTotale=txtSalTotale.getValue();
						txtfldVariazioneSAL.setValue("-"+salTotale);
						txtfldVariazioneSAL.fireEvent(Events.KeyUp);
						
					}else{
						txtfldVariazioneSAL.setValue("0.00");
						txtfldVariazioneSAL.fireEvent(Events.KeyUp);
					}
				}
			});
			
			btnAssegnaImporto= new Button();
			btnAssegnaImporto.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.arrowdown()));
			btnAssegnaImporto.setToolTip("Assegna Totale Importo");
			btnAssegnaImporto.setStyleAttribute("padding-left", "3px");
			btnAssegnaImporto.setIconAlign(IconAlign.TOP);
			btnAssegnaImporto.setSize(25, 15);
			btnAssegnaImporto.addSelectionListener(new SelectionListener<ButtonEvent>() {
					
					@Override
					public void componentSelected(ButtonEvent ce) {
						
						String importo=new String();
						
						if(txtfldImportoRtv.getValue().toString().compareTo("#")!=0)
							importo=txtfldImportoRtv.getValue().toString();	
						else
						if(!txtfldTotFatturato.getRawValue().isEmpty()) 
							importo=txtfldTotFatturato.getValue().toString();
						else 
							importo="0.00";
						
						txtfldImportoDaFatturare.setValue(importo);						
					}
			});
			
			txtfldImportoRtv.setFieldLabel("Importo da RTV");
			txtfldImportoRtv.setEnabled(false);
			
			RowData data1 = new RowData(.15, 1);
			data.setMargins(new Margins(5));
						
			LayoutContainer layoutColumn=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn.setLayout(layout);
			layoutColumn.add(txtfldOreDaFatturare, new FormData("46%"));
			layoutColumn.add(txtOreDaFatturare);
			cp1.add(layoutColumn, data1);
					
			LayoutContainer layoutColumn1=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn1.setLayout(layout);
			layoutColumn1.add(txtfldVariazioneSAL, new FormData("46%"));
			layoutColumn1.add(txtVariazioneSal);					
			cp1.add(layoutColumn1, data1);
						
			LayoutContainer layoutColumn2=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn2.setLayout(layout);
			layoutColumn2.add(txtfldVariazionePCL, new FormData("46%"));
			layoutColumn2.add(txtVariazionePcl);
			cp1.add(layoutColumn2, data1);
			
			LayoutContainer layoutColumn3=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn3.setLayout(layout);
			layoutColumn3.add(txtfldOreScaricate, new FormData("46%"));
			layoutColumn3.add(txtOreScaricate);
			cp1.add(layoutColumn3, data1);
			
			LayoutContainer layoutColumn4=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn4.setLayout(layout);
			layoutColumn4.add(txtfldDiffScaricateEseguite, new FormData("46%"));
			layoutColumn4.add(txtMargine);
			cp1.add(layoutColumn4, data1);
			
			LayoutContainer layoutColumnOreRimborso=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumnOreRimborso.setLayout(layout);
			layoutColumnOreRimborso.add(txtfldOreRimborsoSpese, new FormData("46%"));
			cp1.add(layoutColumnOreRimborso, data1);
			
			LayoutContainer layoutColumn6=new LayoutContainer();
			layout= new FormLayout();
			//layout.setLabelWidth(80);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumn6.setLayout(layout);
			//layoutColumn4.add(txtfldDiffScaricateEseguite, new FormData("46%"));
			layoutColumn4.add(txtEfficienza);
			cp1.add(layoutColumn6, data1);
			
			fldsetFattura.add(cp1);
			
			ContentPanel cp2=new ContentPanel();
			cp2.setHeaderVisible(false);
			cp2.setSize(575, 160);
			cp2.setBorders(false);
			cp2.setBodyBorder(false);
			cp2.setFrame(false);
			
			cp2.setLayout(new RowLayout(Orientation.HORIZONTAL));
			
			RowData data2 = new RowData(.55, 1);
						
			LayoutContainer layoutColumnTxtArea=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(150);
			layout.setLabelAlign(LabelAlign.TOP);
			layoutColumnTxtArea.setLayout(layout);
			layoutColumnTxtArea.add(txtaNote, new FormData("90%"));
			cp2.add(layoutColumnTxtArea, data2);			
				
			LayoutContainer layoutColumn5=new LayoutContainer();
			layout= new FormLayout();
			layout.setLabelWidth(100);
			layout.setLabelAlign(LabelAlign.LEFT);
			layoutColumn5.setLayout(layout);
			layoutColumn5.add(chbxSalButtare, new FormData("20%"));
			layoutColumn5.add(txtfldTotFatturato, new FormData("20%"));
			layoutColumn5.add(txtfldImportoRtv, new FormData("20%"));
			HorizontalPanel hp= new HorizontalPanel();
			hp.setSpacing(2);
			hp.add(txtfldImportoDaFatturare);			
			hp.add(btnAssegnaImporto);
			layoutColumn5.add(hp, new FormData("85%"));
			cp2.add(layoutColumn5, data1);						
			
			fldsetFattura.add(cp2);
			
			add(vp);
			layout();		
		}
		
		
		//Caricamento record estensioni commesse/ordini della commessa selezionata
		private void caricaTabellaDati() {
			String data= new String(); //Formato Feb2013
			data=smplcmbxMese.getRawValue().toString().substring(0,3)+smplcmbxAnno.getRawValue().toString();
			
	    	AdministrationService.Util.getInstance().getElencoCommesseSuFoglioFatturazione(numCommessa, numEstensione,  data, new AsyncCallback<List<RiepilogoOreTotaliCommesse>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getElencoCommesseSuFoglioFatturazione();");
    				caught.printStackTrace();
				}

				@Override
				public void onSuccess(List<RiepilogoOreTotaliCommesse> result) {
					if(result==null)
		    			Window.alert("error: Impossibile efettuare il caricamento dei dati Ordini in tabella.");
		    		else	
		    			loadTable(result);					
				}
			});
		}
		
		private void loadTable(List<RiepilogoOreTotaliCommesse> lista) {
			try {
				ListStore<RiepilogoOreTotaliCommesse>store = new ListStore<RiepilogoOreTotaliCommesse>();
				store.add(lista);
				store.setDefaultSort("numeroCommessa", SortDir.ASC);
				gridOrdine.reconfigure(store, cmOrdine);
				
			} catch (NullPointerException e) {
					e.printStackTrace();
			}	
		}
		

		private List<ColumnConfig> createColumns() {
			
			List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
			final NumberFormat number = NumberFormat.getFormat("0.00");
			
			ColumnConfig column=new ColumnConfig();		
		    column.setId("numeroCommessa");  
		    column.setHeader("Commessa");  
		    column.setWidth(70);  
		    column.setRowHeader(true); 
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("estensione");  
		    column.setHeader("Est.");  
		    column.setWidth(35);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("attivitaCommessa");  
		    column.setHeader("Att.Commessa"); 
		    column.setWidth(60);  
		    column.setRowHeader(true);
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);		   
		    
		    column=new ColumnConfig();		
		    column.setId("sal");  
		    column.setHeader("Var.SAL");
		    column.setToolTip("Variazione mensile del SAL");
		    column.setWidth(60);  
		    column.setRowHeader(true); 
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
					
				}
			});
		    configs.add(column);
		    
		    
		    column=new ColumnConfig();		
		    column.setId("totaleSal");  
		    column.setHeader("SAL su Est."); 
		    column.setToolTip("Totale delle variazioni di SAL sull'estenzione");
		    column.setWidth(70);  
		    column.setRowHeader(true); 
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
				}
			});
		    configs.add(column);
		    
		    /*column= new ColumnConfig();
		    column.setId("salDaButtare");
		    column.setHeader("B.");  
		    column.setToolTip("Se verde indica Sal buttato/scartato");
		    column.setWidth(20);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					String numeroCommessa=model.get("numeroCommessa");
					if(numeroCommessa.compareTo("TOTALE")!=0)
					{
						String color = "#FFFFFF";
						String flag=model.get("salDaButtare");
						if(flag.compareTo("S")==0)
							color = "#90EE90";                    
						config.style = config.style + ";background-color:" + color + ";";									
					}
					else{
						config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
					}
					return "";
				}
			});
		    configs.add(column);*/	
		    
		    column=new ColumnConfig();		
		    column.setId("pcl");  
		    column.setHeader("Var.PCL");  
		    column.setToolTip("Variazione mensile del PCL");
		    column.setWidth(60);  
		    column.setRowHeader(true); 
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
				}
			});
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("totalePcl");  
		    column.setHeader("PCL su Est.");
		    column.setToolTip("Totale delle variazioni di PCL sull'estenzione");
		    column.setWidth(70);
		    column.setRowHeader(true); 
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {
				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					Float n=model.get(property);
					return number.format(n);
				}
			});
		    configs.add(column);
		    
			column=new ColumnConfig();		
		    column.setId("numeroOrdine");
		    column.setHeader("Ordine");  
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model,
						String property, ColumnData config, int rowIndex,
						int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store,
						Grid<RiepilogoOreTotaliCommesse> grid) {
					
					/*Boolean importoRes=model.get("importoOrdineRes");
					String numeroCommessa=model.getNumeroCommessa();
					
					if(!importoRes)
					{
						String color = "#90EE90";					                    
						config.style = config.style + ";background-color:" + color + ";";
					}else{
						String color = "#F08080";					                    
						config.style = config.style + ";background-color:" + color + ";";
					}
					
					if(numeroCommessa.compareTo("TOTALE")==0)
						config.style = config.style + ";background-color:#FFFFFF;";*/
					
					return model.get(property);
				}
			});
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("numeroRda");  
		    column.setHeader("RDA");  
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model,
						String property, ColumnData config, int rowIndex,
						int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store,
						Grid<RiepilogoOreTotaliCommesse> grid) {
					
					return model.get(property);
				}
			});
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("oggettoOrdine");  
		    column.setHeader("Oggetto");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("descrizioneAttivita");  
		    column.setHeader("Attivita");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("oreOrdine");
		    column.setHeader("h/Eseg.");
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("oreLavoro");
		    column.setHeader("h/Fatt.");
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("importoFatturato");
		    column.setHeader("Importo");  
		    column.setWidth(65);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("compilato");//flag per indicare se è già presente il foglio ore del mese  
		    column.setHeader("C.");  
		    column.setWidth(20);  
		    column.setRowHeader(true);
		    column.setToolTip("Se verde indica che i dati del mese sono stati confermati.");
		    column.setRenderer(new GridCellRenderer<RiepilogoOreTotaliCommesse>() {

				@Override
				public Object render(RiepilogoOreTotaliCommesse model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<RiepilogoOreTotaliCommesse> store, Grid<RiepilogoOreTotaliCommesse> grid) {
					
					String numeroCommessa=model.get("numeroCommessa");
					if(numeroCommessa.compareTo("TOTALE")!=0)
					{
						String color = "#F08080";
						String flag=model.get("compilato");
						if(flag.compareTo("Si")==0)
							color = "#90EE90";                    
						config.style = config.style + ";background-color:" + color + ";";									
					}
					else{
						config.style = config.style + ";background-color:" + "#FFFFFF" + ";";
					}
					return "";
				}
			});
		    configs.add(column);	    
		    return configs;
		}
		
		
		private void caricaDatiFatturazioneOrdine(String numeroCommessa, int idAttivita) {	
			String meseR= new String();
			String anno= new String();
			String data= new String();
			
			final String commessa=numeroCommessa;
			
			if(smplcmbxMese.isValid()&&smplcmbxAnno.isValid()){
				anno=smplcmbxAnno.getRawValue().toString();
				meseR=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
				data=meseR+anno;
				
				AdministrationService.Util.getInstance().getDatiFatturazionePerOrdine(commessa, data, idAttivita, new AsyncCallback<FoglioFatturazioneModel>() {
					@Override
					public void onSuccess(FoglioFatturazioneModel result) {
						if(result==null)
							Window.alert("error: Problemi durante l'accesso ai dati di fatturazione dell'ordine selezionato.");
						else	
							if(result.getStato().compareTo("0")==0) {
								nuovo=true;
								loadFormFatturazione(result, commessa);
							}							
							else{
								nuovo=false;
								loadFormFatturazione(result, commessa);
							}							
					}
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on getDatiFatturazionePerOrdine();");
						caught.printStackTrace();		
					}
				}); //AsyncCallback	  		
			}else
				Window.alert("error: Dati selezionati non corretti.");
			
		}
		
		protected boolean hasValue(TextField<String> field) {
		    return field.getValue() != null && field.isValid();
		}
		
		private void loadFormFatturazione(FoglioFatturazioneModel result, String numeroCommessa) {								
			ListStore<RiepilogoOreDipFatturazione> store= new ListStore<RiepilogoOreDipFatturazione>();
			List<RiepilogoOreDipFatturazione> lista= new ArrayList<RiepilogoOreDipFatturazione>();
			String ncommessa= new String();
			String totOre=new String();
			String delta= new String();
			String scaricate= "0.00";
			String totaleEuro= new String();
			String variazionePCL=new String();
			
			Float oreViaggioC=(float)0.0;
			Float oreStraoC=(float)0.0;
			Float oreLavoro=(float)0.0;
			
			NumberFormat number= NumberFormat.getFormat("0.00");
			
			HorizontalPanel hp= new HorizontalPanel();
			CntpnlRiepilogoOreDipFatturazione cp;
			
			try {
				totOre= "0.0";			
				hp=(HorizontalPanel) getParent();//se non è presente una lista faccio comunque apparire gli ordini da "fatturare" in modo tale da permettere l'inserimento di ore di sal
				cp=(CntpnlRiepilogoOreDipFatturazione) hp.getItemByItemId("panelRiepilogoOre");
				store=cp.gridRiepilogo.getStore();
				
				//prelevo la lista di 
				lista.addAll(store.getModels());
					
				//prendo il numero di ore eseguite dalla commessa selezionata
				for(RiepilogoOreDipFatturazione riep: lista){
					ncommessa=riep.getNumeroCommessa().substring(0,riep.getNumeroCommessa().indexOf("(")-1).toLowerCase();
					String c=ncommessa.substring(ncommessa.indexOf(".")+1, ncommessa.length());
					if(ncommessa.compareTo(numeroCommessa)==0 &&
							(ncommessa.substring(ncommessa.indexOf(".")+1, ncommessa.length()).compareTo("pa")!=0) &&
								(riep.getDipendente().compareTo("_TOTALE")==0)){
						String numeroFormattato= new String();
						numeroFormattato=number.format(riep.getOreTotali());
						totOre=	ClientUtility.aggiornaTotGenerale(totOre, numeroFormattato);
						
						oreViaggioC=(float) (Float.valueOf(ClientUtility.getOreCentesimi(number.format(riep.getOreViaggio())))*0.85)+oreViaggioC;					
						oreStraoC=(float)((Float.valueOf(ClientUtility.getOreCentesimi(number.format((Float)riep.get("oreStraordinario"))))*1.2))+oreStraoC;
						oreLavoro=(Float.valueOf(ClientUtility.getOreCentesimi(number.format(riep.getOreLavoro()))))+oreLavoro;
					}
				}				
				
				oreLavoro=oreLavoro+oreStraoC+oreViaggioC;
				
				if(!nuovo){	
					//String ruolo=txtRuolo.getText();
					String statoElaborazione=result.getStato();
					if (statoElaborazione.compareTo("2")==0 && ruolo.compareTo("AMM")!=0)
						btnSalva.disable();
					else
						btnSalva.enable();
					
	    	  		variazionePCL=number.format(Float.valueOf(result.getVariazionePcl())*(-1));
					scaricate=ClientUtility.aggiornaTotGenerale(result.getOreFatturate(), result.getVariazioneSal());
					scaricate=ClientUtility.aggiornaTotGenerale(scaricate, variazionePCL);
									    
					totaleEuro=number.format(result.getTariffaOraria()*Float.valueOf(ClientUtility.getOreCentesimi(result.getOreFatturate())));
						
					txtSalTotale.setValue(ClientUtility.aggiornaTotGenerale(result.getsalAttuale(), result.getVariazioneSal()));
					txtPclTotale.setValue(ClientUtility.aggiornaTotGenerale(result.getPclAttuale(), result.getVariazionePcl()));
					
					txtfldOreOrdine.setValue(result.getOreOrdine());
					txtfldOreResiduoOrdine.setValue(result.getResiduoOre());
					txtfldCostoOrario.setValue(number.format(result.getTariffaOraria()));
					txtfldSALIniziale.setValue(result.getsalAttuale());
					txtfldPCLIniziale.setValue(result.getPclAttuale());
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
					txtfldVariazioneSAL.setValue(result.getVariazioneSal());
					txtfldVariazionePCL.setValue(result.getVariazionePcl());
					txtaNote.setValue(result.getNote());
					txtfldImportoOrdine.setValue((String)result.get("importo"));
					txtfldImportoResiduo.setValue((String)result.get("importoResiduo"));
					txtfldImportoDaFatturare.setValue((String)result.get("importoDaFatturare"));
					txtfldImportoRtv.setValue((String)result.get("importoRtv"));
					txtfldOreRimborsoSpese.setValue((String)result.get("oreRimborsoSpese"));
					
					txtfldOreEseguiteRegistrate.setValue(result.getOreEseguiteRegistrate());
					
					txtfldTotFatturato.setValue(totaleEuro);
					
					delta=ClientUtility.calcoloDelta(scaricate, result.getOreEseguiteRegistrate());//se uso l'inserimento manuale
					delta=ClientUtility.calcoloDelta(delta, txtfldOreRimborsoSpese.getRawValue().toString());
					txtfldDiffScaricateEseguite.setValue(delta);
					
					txtfldOreScaricate.setValue(scaricate);
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
	    	  		
					totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreDaFatturare.getValue().toString()));
	    	  		txtOreDaFatturare.setText("("+totaleEuro+")");
					
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
	    	  		txtOreScaricate.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
	    	  		txtMargine.setText("("+totaleEuro+")");
	    	  		
	    	  		Float efficienza=(float)0.00;
	    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
	    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
	    	  		txtVariazioneSal.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
	    	  		txtVariazionePcl.setText("("+totaleEuro+")");
	    	  		
	    	  		String chbxValue= result.get("flagSalDaButtare");
	    	  		
	    	  		if(chbxValue!=null)
	    	  		if(chbxValue.compareTo("S")==0)
	    	  			chbxSalButtare.setValue(true);
	    	  		else
	    	  			chbxSalButtare.setValue(false);
	    	  		
	    	  		if(Float.valueOf(result.getResiduoOre())<0)
	    	  			Window.alert("Il numero di ore residue sull'ordine è negativo! Effettuare le modifiche opportune!");
				
				}else{
									
					txtSalTotale.setValue(ClientUtility.aggiornaTotGenerale(result.getsalAttuale(), result.getVariazioneSal()));
					txtPclTotale.setValue(ClientUtility.aggiornaTotGenerale(result.getPclAttuale(), result.getVariazionePcl()));
					
					txtfldOreOrdine.setValue(result.getOreOrdine());
					txtfldOreResiduoOrdine.setValue(result.getResiduoOre());
					txtfldImportoOrdine.setValue((String)result.get("importo"));
					txtfldImportoResiduo.setValue((String)result.get("importoResiduo"));
					txtfldCostoOrario.setValue(number.format(result.getTariffaOraria()));
					txtfldSALIniziale.setValue(result.getsalAttuale());
					txtfldPCLIniziale.setValue(result.getPclAttuale());
					txtfldOreDaFatturare.setValue(result.getOreFatturate());
					txtfldVariazioneSAL.setValue(result.getVariazioneSal());
					txtfldVariazionePCL.setValue(result.getVariazionePcl());
					txtfldOreScaricate.setValue("0.00");
					txtfldImportoRtv.setValue((String)result.get("importoRtv"));
					txtfldOreRimborsoSpese.setValue((String)result.get("oreRimborsoSpese"));
					
					txtfldOreEseguiteRegistrate.setValue(totOre);
					//txtfldOreEseguiteRegistrate.setValue(result.getOreEseguiteRegistrate());
	    	  		delta=ClientUtility.calcoloDelta("0.00", txtfldOreEseguiteRegistrate.getValue().toString());
	    	  		txtfldDiffScaricateEseguite.setValue(delta);
	    	  		
					txtfldTotFatturato.setValue("0.00");
					//txtfldDiffScaricateEseguite.setValue("0.00");
					txtfldOreScaricate.setValue("0.00");
					txtfldOreDaFatturare.setValue("0.00");
					txtfldImportoDaFatturare.setValue("0.00");
					
					totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreDaFatturare.getValue().toString()));
	    	  		txtOreDaFatturare.setText("("+totaleEuro+")");
					
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldOreScaricate.getValue().toString()));
	    	  		txtOreScaricate.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldDiffScaricateEseguite.getValue().toString()));
	    	  		txtMargine.setText("("+totaleEuro+")");
	    	  		
	    	  		Float efficienza=(float)0.00;
	    	  		efficienza=Float.valueOf(ClientUtility.getOreCentesimi(scaricate))/Float.valueOf(ClientUtility.getOreCentesimi(txtfldOreEseguiteRegistrate.getValue().toString()));
	    	  		txtEfficienza.setText("("+number.format(efficienza)+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazioneSAL.getValue().toString()));
	    	  		txtVariazioneSal.setText("("+totaleEuro+")");
	    	  		
	    	  		totaleEuro=number.format(ClientUtility.calcolaImporto(txtfldCostoOrario.getValue().toString(), txtfldVariazionePCL.getValue().toString()));
	    	  		txtVariazionePcl.setText("("+totaleEuro+")");	
	    	  		
	    	  		chbxSalButtare.setValue(false);
	    	  		
	    	  		//txtTotCompensato.setText("Corrette--> ("+oreLavoro+") ");
				}
				
				String r=ruolo;
				
				float oreRes=Float.valueOf(txtfldOreResiduoOrdine.getValue().toString());
				float importoRes=Float.valueOf(txtfldImportoResiduo.getValue().toString());
				
				if(((oreRes!=0 && importoRes==0)||(oreRes==0 && importoRes!=0)) && Float.valueOf(txtfldImportoOrdine.getValue().toString())!=0)					
					Window.alert("I valori di ore residue e importo residuo sull'ordine sono incoerenti. Effettuare eventuali modifiche.");
				
				if(txtfldOreResiduoOrdine.getValue().toString().compareTo("0.00")==0 && r.compareTo("PM")==0)
					txtfldOreDaFatturare.setEnabled(false);
				else
					txtfldOreDaFatturare.setEnabled(true);						
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errore load FormFatturazione.");
			}
			
		}
	}
}
