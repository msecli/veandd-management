package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.DialogAssociaOrdine;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.binding.SimpleComboBoxFieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.gwtext.client.widgets.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class PanelCommessa extends LayoutContainer {
	
	public PanelCommessa(){}
	
	private FormPanel frmpnlCommessa= new FormPanel();
	private GroupingStore<CommessaModel>store = new GroupingStore<CommessaModel>();
	private GroupingStore<CommessaModel> storeCompleto= new GroupingStore<CommessaModel>();
	private GroupingStore<CommessaModel> storeResult= new GroupingStore<CommessaModel>();
	private List<CommessaModel> listaStore= new ArrayList<CommessaModel>();
	
	private FormBinding formBindingsCommessa;
	private Grid<CommessaModel> gridCommessa;
	private ColumnModel cmCommessa;
	private TextField<String> txtfldIdCommessa;
	private TextField<String> txtfldNumeroCommessa;
	private TextField<String> txtfldEstensione;
	private TextField<String> txtfldOreLavoro;
	private TextField<String> txtfldOreLavoroResidue;
	private TextField<String> txtfldTariffa;
	private TextField<String> txtfldSalAttuale;
	private TextField<String> txtfldPclAttuale;
	private TextArea txtrDescrizione;
	private TextArea txtrNote;
	private Button btnSend;
	private Button btnDelete;
	private Button btnEdit;
	private Button btnReset;
	private CheckBox chbxEscludiDaPa;
	
	private Text txtRuolo=new Text();
	private Text txtNome=new Text();
	private Text txtCognome=new Text();
		
	private SimpleComboBox<String> smplcmbxPM;
	private SimpleComboBox<String> smplcmbxStatoCommessa;
	private SimpleComboBox<String> smplcmbxTipoCommessa;
	private SimpleComboBox<String> smplcmbxSelectStatoCommessa;
	private SimpleComboBox<String> smplcmbxCliente;
	private SimpleComboBox<String> smplcmbxOrderBy;
	
	private Button btnOrdine;
	private Button btnClose;
	private Button btnVariazioniMensili;
	
	protected void onRender(Element target, int index) {
	    super.onRender(target, index);
		
	    recuperoSessionUsername();
	   
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
	    
		HorizontalPanel horPanel= new HorizontalPanel(); //Conterr� form e pannello per vista dati
		horPanel.setStyleAttribute("padding", "5px");
		horPanel.setSpacing(5);
		
		btnOrdine = new Button();
		btnOrdine.setSize(26, 26);
		btnOrdine.setEnabled(false);
		btnOrdine.setToolTip("Associa un ordine.");
		btnOrdine.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));
		btnOrdine.setIconAlign(IconAlign.TOP);
		
		/*
		btnRefresh=new Button("R");
		btnRefresh.setEnabled(true);
		btnRefresh.setHeight("18px");
		btnRefresh.setWidth("18px");
		btnRefresh.setBorders(true);
		btnRefresh.setToolTip("Refresh dei dati in tabella.");
		btnRefresh.setStyleAttribute("font-size", "80%");
		btnRefresh.setStyleAttribute("margin-left", "5px");
		*/
		
		btnClose= new Button();
		btnClose.setSize(26, 26);
		btnClose.setEnabled(false);
		btnClose.setToolTip("Chiudi la commessa.");
		btnClose.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.chiudiCommessa()));	
		btnClose.setIconAlign(IconAlign.TOP);
		
		btnVariazioniMensili= new Button();
		btnVariazioniMensili.setSize(26, 26);
		btnVariazioniMensili.setEnabled(false);
		btnVariazioniMensili.setToolTip("Variazioni mensili");
		btnVariazioniMensili.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.datiTimb()));	
		btnVariazioniMensili.setIconAlign(IconAlign.TOP);
		btnVariazioniMensili.addSelectionListener(new SelectionListener<ButtonEvent>() {			
				@Override
				public void componentSelected(ButtonEvent ce) {
					String numeroCommessa=txtfldNumeroCommessa.getValue();
					String estensioneCommessa=txtfldEstensione.getValue();
					DialogRiepilogoDatiFoglioFatturazione d= new DialogRiepilogoDatiFoglioFatturazione(numeroCommessa+"."+estensioneCommessa);
					d.show();			
				}
			});
		
		
		final TextField<String> txtfldsearch= new TextField<String>();
		txtfldsearch.setEmptyText("Cerca...");
		
		smplcmbxSelectStatoCommessa= new SimpleComboBox<String>();
		smplcmbxSelectStatoCommessa.setFieldLabel("Stato Commessa");
		smplcmbxSelectStatoCommessa.setName("statoCommessa");
		smplcmbxSelectStatoCommessa.add("Aperta");
		smplcmbxSelectStatoCommessa.add("Conclusa");
		smplcmbxSelectStatoCommessa.add("Tutte");
		smplcmbxSelectStatoCommessa.setTriggerAction(TriggerAction.ALL);
		smplcmbxSelectStatoCommessa.setSimpleValue("Aperta");
		smplcmbxSelectStatoCommessa.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
					caricaTabellaDati("");
				else caricaTabellaDati(txtCognome.getText());
			}		
		});
		
		Text txtrderBy= new Text();
		txtrderBy.setText("OrderBy: ");
		
		smplcmbxOrderBy= new SimpleComboBox<String>();
		smplcmbxOrderBy.add("Project Manager");
		smplcmbxOrderBy.add("Cliente");
		smplcmbxOrderBy.setTriggerAction(TriggerAction.ALL);
		smplcmbxOrderBy.setSimpleValue("Cliente");
		smplcmbxOrderBy.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				
				String selezione=smplcmbxOrderBy.getRawValue().toString();
				if(selezione.compareTo("Project Manager")==0){
					store.groupBy("pm",true);
					gridCommessa.reconfigure(store, cmCommessa);
				}
				else{
					store.groupBy("ragioneSociale",true);
					gridCommessa.reconfigure(store, cmCommessa);
				}
			}		
		});
		
		
		ToolBar toolBar = new ToolBar();
		toolBar.setLayoutData(new FormLayout());
		toolBar.add(txtrderBy);
		toolBar.add(smplcmbxOrderBy);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(smplcmbxSelectStatoCommessa);
		toolBar.add(txtfldsearch);
		
		toolBar.add(btnClose);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(btnVariazioniMensili);
		toolBar.setBorders(true);
		toolBar.setHeight("30px");
		toolBar.setAlignment(HorizontalAlignment.RIGHT);
		toolBar.setStyleAttribute("margin-bottom", "1px");
		toolBar.setBorders(false);
	        
		txtfldsearch.addKeyListener(new KeyListener(){
		    	 public void componentKeyUp(ComponentEvent event) {
		    		 
		    		 if(txtfldsearch.getRawValue().isEmpty()){
		    			 storeResult.removeAll();
		    			 store.removeAll();
		    			 store.add(storeCompleto.getModels());
		    			 gridCommessa.reconfigure(store, cmCommessa);
		    		 }else{
		    		 	    		 	    		 
		    			 String campo= txtfldsearch.getValue().toString();	    			 	    			 
		    			 storeResult.removeAll();
		    			 for(CommessaModel r:listaStore){
		    				 if(r.getNumeroCommessa().contains(campo)){
		    					 storeResult.add(r);		    				 
		    				 }
		    			 }
		    			 listaStore.clear();
		    			 listaStore.addAll(store.getModels());
		    			 gridCommessa.reconfigure(storeResult, cmCommessa);			 
		    		 }
		    	 }
		});	
		
		ContentPanel cntpnlVistaDati= new ContentPanel();
		cntpnlVistaDati.setBorders(false);     
		cntpnlVistaDati.setFrame(true);  
	    cntpnlVistaDati.setLayout(new FitLayout());
	    cntpnlVistaDati.setHeaderVisible(false);
	    cntpnlVistaDati.setScrollMode(Scroll.AUTOY);
	    cntpnlVistaDati.setStyleAttribute("padding-left", "10px");
	    cntpnlVistaDati.setStyleAttribute("padding-top", "0px");
	    
		try {
			frmpnlCommessa= createForm();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema durante la generazione del FormCommessa.");
		}
			    
	    try {
	    	cmCommessa = new ColumnModel(createColumns());	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    
	    //Istruzioni per vista "raggruppata" commesse
		store.groupBy("ragioneSociale");
		
		//Vista per permettere il grouping
		GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(true);  
	    view.setGroupRenderer(new GridGroupRenderer() {
	      public String render(GroupColumnData data) {
	        String f = cmCommessa.getColumnById(data.field).getHeader();  
	        //String l = data.models.size() == 1 ? "Item" : "Items";  
	        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
	      }
	    });
	    
		//Definizione della griglia
	    gridCommessa = new Grid<CommessaModel>(store, cmCommessa);   
	    gridCommessa.setBorders(false);  
	    gridCommessa.setStripeRows(true);  
	    gridCommessa.setColumnLines(true);  
	    gridCommessa.setColumnReordering(true);  
	    gridCommessa.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridCommessa.setView(view);
	        
	    gridCommessa.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<CommessaModel>>() {  
	          public void handleEvent(SelectionChangedEvent<CommessaModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	            	formBindingsCommessa.bind((ModelData) be.getSelection().get(0));
	            	
	            	//txtfldNumeroCommessa.setEnabled(false);
	            	//txtfldEstensione.setEnabled(false);
	            	btnSend.setEnabled(false);
	            	btnDelete.setEnabled(true);
	            	btnEdit.setEnabled(true);
	            	btnOrdine.setEnabled(true);
	            	btnClose.setEnabled(true);
	            	btnVariazioniMensili.setEnabled(true);
	            	String stato= be.getSelectedItem().getStatoCommessa();
	            	if(stato.compareTo("Conclusa")==0){
	            		
	            		disableForm();
	            		
	            	}else{
	            		
	            		enableForm();       		
	            	}
	            	
	            	String cliente=be.getSelectedItem().getNumeroOrdine();
	            	if(cliente.compareTo("#")!=0)
	            	{
	            		btnOrdine.setEnabled(false);
	            	}
	            	              
	            } else {  
	              formBindingsCommessa.unbind();  
	            }
	          }       
	    }); 
			    
	    formBindingsCommessa = new FormBinding(frmpnlCommessa, true);
	    formBindingsCommessa.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxPM, "pm"));
	    formBindingsCommessa.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxStatoCommessa, "statoCommessa"));
	    formBindingsCommessa.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxTipoCommessa, "tipoCommessa"));
	    formBindingsCommessa.addFieldBinding(new SimpleComboBoxFieldBinding(smplcmbxCliente, "ragioneSociale"));
	    formBindingsCommessa.setStore((Store<CommessaModel>) gridCommessa.getStore());	    
		
	    ContentPanel cntpnlGrid= new ContentPanel();
	    cntpnlGrid.setBodyBorder(false);         
	    cntpnlGrid.setLayout(new FitLayout());  
	    cntpnlGrid.setHeaderVisible(false);
	    cntpnlGrid.setWidth(616);
	    cntpnlGrid.setHeight(760);
	    cntpnlGrid.setScrollMode(Scroll.AUTOY);
	    cntpnlGrid.add(gridCommessa);
	    cntpnlGrid.setTopComponent(toolBar);
	    
	    VerticalPanel vp= new VerticalPanel();
		vp.add(cntpnlGrid);
		
		cntpnlVistaDati.add(cntpnlGrid);
				
		horPanel.add(frmpnlCommessa);
		horPanel.add(cntpnlVistaDati);
		
		ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setHeading("Dati Commesse.");
		cntpnlLayout.setSize(1030, 850);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.add(horPanel);
		cntpnlLayout.setButtonAlign(HorizontalAlignment.CENTER);
		
		ToolBar tlbr= new ToolBar();
		tlbr.setAlignment(HorizontalAlignment.RIGHT);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnSend);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnEdit);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnDelete);
		tlbr.add(new SeparatorToolItem());
		tlbr.add(btnReset);
		tlbr.add(new SeparatorToolItem());
		cntpnlLayout.setTopComponent(tlbr);
		
		/*cntpnlLayout.addButton(btnSend);
		cntpnlLayout.addButton(btnEdit);
		cntpnlLayout.addButton(btnDelete);
		cntpnlLayout.addButton(btnReset);*/
		//cntpnlLayout.setStyleAttribute("padding-left", "7px");
		//cntpnlLayout.setStyleAttribute("margin-top", "15px");
		
		bodyContainer.add(cntpnlLayout);    
			
		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		add(layoutContainer);
		
		//add(cp);
			
		//Send	
		btnSend.addSelectionListener(new SelectionListener<ButtonEvent>() {
					        
							public void componentSelected(ButtonEvent ce) {
						    	 if(frmpnlCommessa.isValid()){
										
						    		//final NumberFormat number = NumberFormat.getFormat("0.00");
						    		
									String numCommessa = new String(); 	
									String estensione= new String();
									String tipoCommessa= new String();
									String pM= new String();
									String descrizione = new String();
									String statoCommessa= new String();
									String note= new String();
									String oreLavoro="0.00";
									String oreLavoroResidue="0.00";
									String tariffaSal="0.0";
									String salAttuale="0.0";
									String pclAttuale="0.0";
									String ragioneSociale="";
									Boolean escludiDaPa= chbxEscludiDaPa.getValue();
									//Date dataInizio=new Date();
																								
									if(txtfldNumeroCommessa.getRawValue().isEmpty()){ numCommessa="";}else{numCommessa=txtfldNumeroCommessa.getValue().toString();}
									if(txtfldEstensione.getRawValue().isEmpty()){ estensione="";}else{estensione=txtfldEstensione.getValue().toString();}
									//if(!txtfldOreLavoro.getRawValue().isEmpty()){oreLavoro=txtfldOreLavoro.getValue().toString();}
									//if(!txtfldOreLavoroResidue.getRawValue().isEmpty()){oreLavoroResidue=txtfldOreLavoroResidue.getValue().toString();}
									if(!txtfldTariffa.getRawValue().isEmpty()){tariffaSal=txtfldTariffa.getValue().toString();}
									if(!txtfldSalAttuale.getRawValue().isEmpty()){salAttuale=txtfldSalAttuale.getValue().toString();}
									if(!txtfldPclAttuale.getRawValue().isEmpty()){pclAttuale=txtfldPclAttuale.getValue().toString();}
									if(smplcmbxTipoCommessa.getRawValue().isEmpty()){ tipoCommessa="";}else{tipoCommessa=smplcmbxTipoCommessa.getRawValue().toString();}
									if(txtrDescrizione.getRawValue().isEmpty()){ descrizione="";}else{descrizione=txtrDescrizione.getValue().toString();}
									if(txtrNote.getRawValue().isEmpty()){ note="";}else{note=txtrNote.getValue().toString();}
									if(smplcmbxPM.getRawValue().isEmpty()){ pM="";}else{							
										pM=smplcmbxPM.getRawValue().toString();		
									}
									if(smplcmbxCliente.getRawValue().isEmpty()){ ragioneSociale="";}else{							
										ragioneSociale=smplcmbxCliente.getRawValue().toString();		
									}
									if(smplcmbxStatoCommessa.getRawValue().isEmpty()){ statoCommessa="";}else{statoCommessa=smplcmbxStatoCommessa.getRawValue().toString();}
														
									//dataInizio=dtfldData.getValue();
																		
									AdministrationService.Util.getInstance().insertDataCommessa(ragioneSociale, numCommessa, estensione, tipoCommessa, pM, statoCommessa, 
											/*dataInizio,*/oreLavoro, oreLavoroResidue, tariffaSal, salAttuale, pclAttuale, descrizione, note, escludiDaPa,  new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Errore connessione on InsertDataCommessa();");
										caught.printStackTrace();
										
									}

									@Override
									public void onSuccess(Boolean result) {
										if(result==true){
											if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
												caricaTabellaDati("");
											else caricaTabellaDati(txtCognome.getText());
											//Window.alert("OK Inserimento Corretto.");
										}
										else Window.alert("error: Inserimento sulla tabella Commessa non effettuato!\n\nAssicurarsi che i dati inseriti non siano presenti.");
											
									}
								});	    //AsyncCallback()    		        
					        
						      }else{Window.alert("Controllare i campi inseriti!");}				
					        }//Event
		});
		
		
		//Edit
		btnEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	          if(frmpnlCommessa.isValid()){
	        	 String numCommessa = new String(); 	
				String estensione= new String();
				String tipoCommessa= new String();
				String pM= new String();
				String descrizione = new String();
				String statoCommessa= new String();
				String note= new String();
				String oreLavoro="0";
				String oreLavoroResidue="0";
				String tariffaSal="0.0";
				String salAttuale="0.0";
				String pclAttuale="0.0";
				String ragioneSociale="";
				Boolean escludiDaPa= chbxEscludiDaPa.getValue();
				
				//Date dataInizio=new Date();
				/*
				if(txtfldIdCommessa.getRawValue().isEmpty()){ idCommessa="0";}else{idCommessa=txtfldIdCommessa.getValue().toString();}
				if(txtfldNumeroCommessa.getRawValue().isEmpty()){ numCommessa="";}else{numCommessa=txtfldNumeroCommessa.getValue().toString();}
				if(txtfldEstensione.getRawValue().isEmpty()){ estensione="";}else{estensione=txtfldEstensione.getValue().toString();}
				if(!txtfldOreLavoro.getRawValue().isEmpty()){oreLavoro=txtfldOreLavoro.getValue().toString();}
				if(!txtfldOreLavoroResidue.getRawValue().isEmpty()){oreLavoroResidue=txtfldOreLavoroResidue.getValue().toString();}
				if(!txtfldTariffaSal.getRawValue().isEmpty()){tariffaSal=txtfldTariffaSal.getValue().toString();}
				if(!txtfldSalAttuale.getRawValue().isEmpty()){salAttuale=txtfldSalAttuale.getValue().toString();}
				if(!txtfldPclAttuale.getRawValue().isEmpty()){pclAttuale=txtfldPclAttuale.getValue().toString();}
				if(smplcmbxTipoCommessa.getRawValue().isEmpty()){ tipoCommessa="";}else{tipoCommessa=smplcmbxTipoCommessa.getRawValue().toString();}
				if(txtrDescrizione.getRawValue().isEmpty()){ descrizione="";}else{descrizione=txtrDescrizione.getValue().toString();}
				if(txtrNote.getRawValue().isEmpty()){ note="";}else{note=txtrNote.getValue().toString();}
				if(smplcmbxPM.getRawValue().isEmpty()){ pM="";}else{pM=smplcmbxPM.getRawValue().toString();}
				if(smplcmbxStatoCommessa.getRawValue().isEmpty()){ statoCommessa="";}else{statoCommessa=smplcmbxStatoCommessa.getRawValue().toString();}
				*/
				List<Record> listaMod=store.getModifiedRecords();
				store.commitChanges();
				for(Record r:listaMod){
					CommessaModel c= (CommessaModel) r.getModel();
					int id=c.getIdCommessa();
					tariffaSal=c.getTariffaSal();
					salAttuale=c.getSalIniziale();
					pclAttuale=c.getPclIniziale();
					numCommessa=c.getNumeroCommessa();
					estensione=c.getEstensione();
					tipoCommessa=c.getTipoCommessa();
					pM=c.getPm();
					statoCommessa=c.getStatoCommessa();
					oreLavoro="0.00";//c.getOreLavoro();
					oreLavoroResidue="0.00";//c.getOreLavoroResidue();
					descrizione=c.getDescrizione();
					note=c.getNote();
					ragioneSociale=c.getRagioneSociale();
				
	        		AdministrationService.Util.getInstance().editDataCommessa(id, ragioneSociale,numCommessa, estensione, tipoCommessa, pM, statoCommessa, 
						/*dataInizio,*/oreLavoro,oreLavoroResidue,tariffaSal, salAttuale, pclAttuale, descrizione, note,  escludiDaPa, new AsyncCallback<Boolean>() {

	        			@Override
	        			public void onFailure(Throwable caught) {
	        				Window.alert("Errore connessione on EditData();");
	        				caught.printStackTrace();
	        			}

	        			@Override
	        			public void onSuccess(Boolean result) {
	        				if(result==true){
	        					if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
	        						caricaTabellaDati("");
	        					else caricaTabellaDati(txtCognome.getText());
	        				} else Window.alert("error: Modifica sulla tabella Ordini non effettuata.");
	        			}
	        		});	//Asynch
				}//for
	        	
	          }else
	        	  Window.alert("Controllare i campi inseriti!");	
	        }
	      });	
		
		
		//Delete
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
	        public void componentSelected(ButtonEvent ce) {
	        	
	        	String idCommessa;
	        	
	        	if(txtfldIdCommessa.getRawValue().isEmpty()){ idCommessa="0";}else{idCommessa=txtfldIdCommessa.getValue().toString();}
	        					
	        	AdministrationService.Util.getInstance().deleteDataCommessa(Integer.parseInt(idCommessa), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on deleteData();");
						caught.printStackTrace();		
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result == true){
							if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
								caricaTabellaDati("");
							else caricaTabellaDati(txtCognome.getText());
						}else Window.alert("error: Accesso alla tabella Commesse negato.");
					}
				});	        		        
	        }
	      });
		
		
		//Reset
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce){
				
				frmpnlCommessa.reset();
	    		btnSend.setEnabled(true);
	    		btnEdit.setEnabled(false);
    	    	btnDelete.setEnabled(false);
    	    	btnOrdine.setEnabled(false);
    	    	btnClose.setEnabled(false);
    	    	gridCommessa.getSelectionModel().deselectAll();
    	    	    	    	
    	    	enableForm();
    	    	txtfldNumeroCommessa.setEnabled(true);
    	    	txtfldEstensione.setEnabled(true);
    	    		    	
    	    	}
		
			});
		
		
		//Show Dialog per associazione ordine
		btnOrdine.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {		
				Dialog d =new  DialogAssociaOrdine(txtfldIdCommessa.getValue().toString());
				d.show();		
				
				d.addListener(Events.Hide, new Listener<ComponentEvent>() {
				     
					@Override
					public void handleEvent(ComponentEvent be) {
						try {
							if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
								caricaTabellaDati("");
							else caricaTabellaDati(txtCognome.getText());
						} catch (Exception e) {
							e.printStackTrace();
							Window.alert("error: Impossibile caricare i dati in tabella.");
						}
						
				    }
				});
				
			}		
		});
		
		/*	
		btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {			
				if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
					caricaTabellaDati("");
				else caricaTabellaDati(txtCognome.getText());
			}	
			
		});
		*/
		
		btnClose.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {		
				
				String idCommessa= new String();
				if(txtfldIdCommessa.getRawValue().isEmpty()){ idCommessa="0";}else{idCommessa=txtfldIdCommessa.getValue().toString();}
				
				AdministrationService.Util.getInstance().closeCommessa(Integer.parseInt(idCommessa), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on closeCommessa();");
						caught.printStackTrace();		
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result == true){
							if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
								caricaTabellaDati("");
							else caricaTabellaDati(txtCognome.getText());
						}else Window.alert("error: Accesso alla tabella Commesse negato.");
					}
				});	     				
				if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
					caricaTabellaDati("");
				else caricaTabellaDati(txtCognome.getText());
			}				
		});	
	}
	
	
	private List<ColumnConfig> createColumns() {
		
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("ragioneSociale");  
	    column.setHeader("Cliente");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    configs.add(column); 
		
	    column=new ColumnConfig();		
		column.setId("numeroCommessa");  
		column.setHeader("Commessa");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		    
		column=new ColumnConfig();		
	    column.setId("estensione");  
	    column.setHeader("Est.");  
	    column.setWidth(45);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column);  
	    
	    column=new ColumnConfig();		
		column.setId("rdo");  
		column.setHeader("Rdo");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		column=new ColumnConfig();		
		column.setId("numeroOfferta");  
		column.setHeader("N. Offerta");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
	    
	    column=new ColumnConfig();		
		column.setId("numeroOrdine");  
		column.setHeader("N. Ordine");  
		column.setWidth(70);  
		column.setRowHeader(true);  
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
	       
	    column=new ColumnConfig();		
	    column.setId("tipoCommessa");  
	    column.setHeader("Tipo.");  
	    column.setWidth(45);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    column=new ColumnConfig();		
	    column.setId("pm");  
	    column.setHeader("Project Manager");  
	    column.setWidth(120);  
	    column.setRowHeader(true);  
	    column.setAlignment(HorizontalAlignment.RIGHT);
	    configs.add(column); 
	    
	    return configs;
	}


	private FormPanel createForm() {
		
		FormPanel frmPanel= new FormPanel();
		
		frmPanel.setFrame(true);
		//frmPanel.setHeading("Dati Commessa");
		frmPanel.setHeaderVisible(false);
		frmPanel.setCollapsible(false);
		frmPanel.setLabelWidth(120);
		frmPanel.setWidth("350px");
		frmPanel.setHeight(500);
		frmPanel.setBodyBorder(false);
		frmPanel.setBorders(false);
		frmPanel.setFrame(false);
		
		txtfldIdCommessa=new TextField<String>();
		frmPanel.add(txtfldIdCommessa, new FormData("45%"));
		txtfldIdCommessa.setFieldLabel("ID Commessa");
		txtfldIdCommessa.setName("idCommessa");
		txtfldIdCommessa.setEnabled(false);
		txtfldIdCommessa.setMaxLength(10);
		txtfldIdCommessa.setVisible(false);
		
		txtfldNumeroCommessa=new TextField<String>();
		frmPanel.add(txtfldNumeroCommessa, new FormData("85%"));
		txtfldNumeroCommessa.setFieldLabel("Commessa");
		txtfldNumeroCommessa.setName("numeroCommessa");
		txtfldNumeroCommessa.setMaxLength(20);
		txtfldNumeroCommessa.setAllowBlank(false);
				
		txtfldEstensione=new TextField<String>();
		frmPanel.add(txtfldEstensione, new FormData("60%"));
		txtfldEstensione.setFieldLabel("Estensione");
		txtfldEstensione.setName("estensione");
		txtfldEstensione.setMaxLength(10);
		txtfldEstensione.setAllowBlank(false);
		txtfldEstensione.setValue("0");
		
		chbxEscludiDaPa= new CheckBox();
		chbxEscludiDaPa.setValue(false);
		chbxEscludiDaPa.setFieldLabel("Escludi da .PA");
		chbxEscludiDaPa.setName("escludiDaPa");
		frmPanel.add(chbxEscludiDaPa, new FormData("60%")); 
		
		smplcmbxTipoCommessa=new SimpleComboBox<String>();
		smplcmbxTipoCommessa.setFieldLabel("Tipo. Commessa");
		smplcmbxTipoCommessa.setName("tipoCommessa");
		smplcmbxTipoCommessa.setAllowBlank(false);
		 for(String l : DatiComboBox.getTipoCommessa()){
		    	smplcmbxTipoCommessa.add(l);}
		smplcmbxTipoCommessa.setTriggerAction(TriggerAction.ALL);
		frmPanel.add(smplcmbxTipoCommessa, new FormData("60%"));
				
		smplcmbxPM = new SimpleComboBox<String>();
		frmPanel.add(smplcmbxPM, new FormData("85%"));
		smplcmbxPM.setFieldLabel("Project Manager");
		smplcmbxPM.setName("pm");
		smplcmbxPM.setAllowBlank(false);
		smplcmbxPM.setTriggerAction(TriggerAction.ALL);
		getNomePM();
		
		smplcmbxCliente= new SimpleComboBox<String>();
		frmPanel.add(smplcmbxCliente, new FormData("85%"));
		smplcmbxCliente.setFieldLabel("Cliente");
		smplcmbxCliente.setName("ragioneSociale");
		//smplcmbxCliente.setAllowBlank(false);
		smplcmbxCliente.setTriggerAction(TriggerAction.ALL);
		getClienti();
				
		smplcmbxStatoCommessa= new SimpleComboBox<String>();
		smplcmbxStatoCommessa.setFieldLabel("Stato Commessa");
		smplcmbxStatoCommessa.setName("statoCommessa");
		 for(String l : DatiComboBox.getStatoCommessa()){
		    	smplcmbxStatoCommessa.add(l);}
		smplcmbxStatoCommessa.setTriggerAction(TriggerAction.ALL);
		smplcmbxStatoCommessa.setSimpleValue("Aperta");
		frmPanel.add(smplcmbxStatoCommessa, new FormData("85%"));
		
		txtfldOreLavoro=new TextField<String>();
		txtfldOreLavoro.setFieldLabel("Ore Lavoro");
		txtfldOreLavoro.setName("oreLavoro");
		txtfldOreLavoro.setVisible(false);
		//txtfldOreLavoro.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
		//txtfldOreLavoro.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
		frmPanel.add(txtfldOreLavoro, new FormData("60%"));
		txtfldOreLavoro.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) {
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){
						
						if(txtfldOreLavoro.getValue()==null)
							txtfldOreLavoro.setValue("0.00");
						else{
							String valore= txtfldOreLavoro.getValue().toString();
													
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
							txtfldOreLavoro.setValue(valore);
						}			
					}
			 }
		});
		
		txtfldOreLavoroResidue=new TextField<String>();
		txtfldOreLavoroResidue.setFieldLabel("Ore Residue");
		txtfldOreLavoroResidue.setName("oreLavoroResidue");
		txtfldOreLavoroResidue.setVisible(false);
		//txtfldOreLavoroResidue.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
		//txtfldOreLavoroResidue.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
		txtfldOreLavoroResidue.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldOreLavoroResidue.getValue()==null)
							txtfldOreLavoroResidue.setValue("0.00");
						else{
							String valore= txtfldOreLavoroResidue.getValue().toString();
													
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
							txtfldOreLavoroResidue.setValue(valore);
						}						
					}
			 }
		});
		frmPanel.add(txtfldOreLavoroResidue, new FormData("60%"));
		
		txtfldTariffa=new TextField<String>();
		txtfldTariffa.setFieldLabel("Tariffa (pre-Ordine)");
		txtfldTariffa.setName("tariffaSal");
		txtfldTariffa.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
		txtfldTariffa.getMessages().setRegexText("Deve essere un numero nel formato 99.99");
		txtfldTariffa.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldTariffa.getValue()==null)
							txtfldTariffa.setValue("0.00");
						else{
							String valore= txtfldTariffa.getValue().toString();
													
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
							txtfldTariffa.setValue(valore);
						}						
					}
			 }
		});
		
		frmPanel.add(txtfldTariffa, new FormData("60%"));
		
		txtfldSalAttuale=new TextField<String>();
		txtfldSalAttuale.setFieldLabel("SAL Iniziale");
		txtfldSalAttuale.setName("salAttuale");
		txtfldSalAttuale.setAllowBlank(false);
		txtfldSalAttuale.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");
		txtfldSalAttuale.getMessages().setRegexText("Inserire le ore in sessantesimi!");
		txtfldSalAttuale.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldSalAttuale.getValue()==null)
							txtfldSalAttuale.setValue("0.00");
						else{
							String valore= txtfldSalAttuale.getValue().toString();
													
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
							txtfldSalAttuale.setValue(valore);
						}						
					}
			 }
		});
		frmPanel.add(txtfldSalAttuale, new FormData("60%"));

		txtfldPclAttuale=new TextField<String>();
		txtfldPclAttuale.setFieldLabel("PCL Iniziale");
		txtfldPclAttuale.setName("pclAttuale");
		txtfldPclAttuale.setAllowBlank(false);
		txtfldPclAttuale.setRegex("^[-]{0,1}([0-9]+)[.]{1}(0{1,2}|15|30|45)$");
		txtfldPclAttuale.getMessages().setRegexText("Inserire le ore in sessantesimi!");
		txtfldPclAttuale.addKeyListener(new KeyListener(){
			 public void componentKeyDown(ComponentEvent event) { 	  
			    	int keyCode=event.getKeyCode();
					if(keyCode==9){			
						
						if(txtfldPclAttuale.getValue()==null)
							txtfldPclAttuale.setValue("0.00");
						else{
							String valore= txtfldPclAttuale.getValue().toString();
													
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
							txtfldPclAttuale.setValue(valore);
						}						
					}
			 }
		});
		frmPanel.add(txtfldPclAttuale, new FormData("60%"));
		
		txtrDescrizione = new TextArea();
		txtrDescrizione.setMaxLength(250);
		txtrDescrizione.setFieldLabel("Descrizione");
		txtrDescrizione.setName("descrizione");
		txtrDescrizione.setHeight(105);
		FormData fd_txtrDescrizione = new FormData("100%");
		fd_txtrDescrizione.setMargins(new Margins(10, 0, 0, 0));
		frmPanel.add(txtrDescrizione, fd_txtrDescrizione);

		txtrNote = new TextArea();
		txtrNote.setMaxLength(250);
		txtrNote.setFieldLabel("Note Aggiuntive");
		txtrNote.setName("note");
		txtrNote.setHeight(105);
		FormData fd_txtrNote = new FormData("100%");
		fd_txtrNote.setMargins(new Margins(5, 0, 0, 0));
		frmPanel.add(txtrNote, fd_txtrNote);
				
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setAlignment(HorizontalAlignment.CENTER);
		buttonBar.setStyleAttribute("margin-top", "40px");
		buttonBar.setStyleAttribute("padding-top", "5px");
		buttonBar.setStyleAttribute("padding-bottom", "5px");
		buttonBar.setBorders(true);
		
		//frmPanel.add(buttonBar, new FormData("100%"));
		
		btnSend = new Button("Save");
		btnSend.setToolTip("Salva i dati.");
		btnSend.setWidth("65px");
				
		btnDelete = new Button("Delete");
		btnDelete.setToolTip("Elimina i dati.");
		btnDelete.setWidth("65px");
		btnDelete.setEnabled(false);
				
		btnEdit = new Button("Edit");
		btnEdit.setToolTip("Modifica i dati.");
		btnEdit.setWidth("65px");
		btnEdit.setEnabled(false);
				
		btnReset= new Button("X");
		btnReset.setToolTip("Azzera i campi compilati.");
		btnReset.setWidth("25px");
						
		buttonBar.add(btnSend);
		buttonBar.add(btnEdit);
		buttonBar.add(btnDelete);
		buttonBar.add(btnReset);
		
		return frmPanel;		
	}
	

	private void getClienti() {
	
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


	private void caricaTabellaDati(String cognomePm) {
		String statoSelezionato=smplcmbxSelectStatoCommessa.getRawValue().toString();
		
		if(cognomePm.compareTo("")==0)
			AdministrationService.Util.getInstance().getAllCommesseModel("",statoSelezionato, new AsyncCallback<List<CommessaModel>>() {
			
				@Override
				public void onSuccess(List<CommessaModel> result) {
					if(result==null)
						Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
					else	
						loadTable(result);	
				}			

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getAllCommesseModel();");
					caught.printStackTrace();
				}
			}); //AsyncCallback	  
		else
			AdministrationService.Util.getInstance().getAllCommesseModelByPm(cognomePm, new AsyncCallback<List<CommessaModel>>() {
				
				@Override
				public void onSuccess(List<CommessaModel> result) {
					if(result==null)
						Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
					else	
						loadTable(result);
				}			

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getAllCommesseModel();");
					caught.printStackTrace();	
				}
			}); //AsyncCallback
	}
	
	
	private void loadTable(List<CommessaModel> lista) {
		frmpnlCommessa.reset();
		
		CommessaModel cm=lista.remove(lista.size()-1);
		txtfldNumeroCommessa.setValue(cm.getNumeroCommessa());
		txtfldEstensione.setValue(cm.getEstensione());	
		try {		
			store.removeAll();
			store.setStoreSorter(new StoreSorter<CommessaModel>());
		    store.setDefaultSort("numeroCommessa", SortDir.ASC);
			store.add(lista);	
			storeResult.removeAll();
			storeCompleto.removeAll();
			storeResult.add(store.getModels());
			storeCompleto.add(store.getModels());
			listaStore.addAll(store.getModels());
			
			store.groupBy("ragioneSociale");
			gridCommessa.reconfigure(store, cmCommessa);
			
			btnSend.setEnabled(true);
	    	btnEdit.setEnabled(false);
	    	btnDelete.setEnabled(false);
	    	btnOrdine.setEnabled(false);
	    	btnClose.setEnabled(false);   	
	    	enableForm();
	    	txtfldNumeroCommessa.setEnabled(true);
	    	txtfldEstensione.setEnabled(true);    		    	
		} catch (NullPointerException e) {
				
			e.printStackTrace();
		}	
	}
	
	
	private void getNomePM() {

		AdministrationService.Util.getInstance().getNomePM(new AsyncCallback<List<String>>() { //Viene restituito nella forma Cognome Nome

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
					//smplcmbxPM.add("Tutti");
				}else Window.alert("error: Errore durante l'accesso ai dati PM.");			
			}
		});	
	}
	
	private void disableForm() {
		//txtfldEstensione.setEnabled(false);
		smplcmbxCliente.setEnabled(false);
		txtfldIdCommessa.setEnabled(false);
		txtfldNumeroCommessa.setEnabled(false);
		smplcmbxPM.setEnabled(false);
		smplcmbxStatoCommessa.setEnabled(false);
		smplcmbxTipoCommessa.setEnabled(false);
		txtrDescrizione.setEnabled(false);
		txtrNote.setEnabled(false);
		btnSend.setEnabled(false);
    	btnEdit.setEnabled(false);
    	btnOrdine.setEnabled(false);
    	btnClose.setEnabled(false);	
	}
	
	private void enableForm(){	
		txtfldIdCommessa.setEnabled(true);
    	//txtfldNumeroCommessa.setEnabled(true);
    	//txtfldEstensione.setEnabled(true);
		smplcmbxCliente.setEnabled(true);
		smplcmbxPM.setEnabled(true);
		smplcmbxStatoCommessa.setEnabled(true);
		smplcmbxTipoCommessa.setEnabled(true);
		txtrDescrizione.setEnabled(true);
		txtrNote.setEnabled(true);		
	}
	
	
	private void recuperoSessionRuolo() {
		SessionManagementService.Util.getInstance().getRuolo(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				setRuolo(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getRuolo();");
			}
		});				
	}
	
	
	public  void setRuolo(String result){
		txtRuolo.setText(result);
		if(txtRuolo.getText().compareTo("AMM")==0 || txtRuolo.getText().compareTo("UA")==0 )
			caricaTabellaDati("");
		else 
			caricaTabellaDati(txtCognome.getText());	
	}
	
	
	private void recuperoSessionUsername() {
		//Il cognome � la seconda parte dello username e lo posso usare per il confronto con il cognome dei pm sulle commesse
		//in quanto deve essere nel formato "Cognome Nome"
		SessionManagementService.Util.getInstance().getUserName(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				setNomeCognome(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getUserName();");
			}
		});		
	}
	
	
	public  void setNomeCognome(String result){
		String nome=new String();
		String cognome=new String();
		int i=0;
		
		if(result.indexOf(".")!=-1){
		
			i=result.indexOf(".");
				
			nome=result.substring(0,i);
			cognome=result.substring(i+1,result.length());
		
			txtNome.setText(nome);
			txtCognome.setText(cognome);
			recuperoSessionRuolo();	
		}
		
	}
}
