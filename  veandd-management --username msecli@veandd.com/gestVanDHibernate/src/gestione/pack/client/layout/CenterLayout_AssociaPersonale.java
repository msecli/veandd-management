package gestione.pack.client.layout;

import java.util.ArrayList;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.layout.panel.DialogAssociaPtoA;
import gestione.pack.client.model.*;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;

import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;

import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;

import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
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
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class CenterLayout_AssociaPersonale extends LayoutContainer{
	public CenterLayout_AssociaPersonale() {
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private Grid<PersonaleAssociatoModel> gridCommessa;
	private ColumnModel cmCommessa;
	
	private Button btnAssocia= new Button("Associa");
	private Button btnRemove= new Button();
	private Button btnRemoveAll;
	private Button btnReset= new Button("X");
	private Button btnAdd= new Button();
	private Button btnRefresh= new Button("R");
	private SimpleComboBox<String> smplcmbxCommessa= new SimpleComboBox<String>();
		
	private TextField<String> txtfldCommessa= new TextField<String>();
	private TextField<String> txtfldNome= new TextField<String>();
	private TextField<String> txtfldCognome= new TextField<String>();
	private TextField<String> txtfldIdAssociazione= new TextField<String>();
	private Text txtNome = new Text();
	private Text txtRuolo= new Text();
	private Text txtCognome = new Text();
	
	private ListView<PersonaleModel> list1 = new ListView<PersonaleModel>();//Lista sorgente
	ListView<PersonaleModel> list2 = new ListView<PersonaleModel>();//Lista destinazione
	private GroupingStore<PersonaleAssociatoModel>storeGrid = new GroupingStore<PersonaleAssociatoModel>();
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);   
	    
		final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		layoutContainer.setWidth(w-225);
		layoutContainer.setHeight(h-54);
		
		recuperoSessionUsername();
	
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);		
						
		/*ContentPanel cntpnlCommessa = new ContentPanel(); //pannello esterno
		cntpnlCommessa.setHeading("Gestione Dipendenti");
		cntpnlCommessa.setHeaderVisible(true);
		cntpnlCommessa.setCollapsible(false);
		cntpnlCommessa.setBorders(false);
		cntpnlCommessa.setWidth(w-215);
		cntpnlCommessa.setHeight(h-55);
		cntpnlCommessa.setScrollMode(Scroll.AUTO);*/
		
		ContentPanel cntpnlVista = new ContentPanel(); //Pannello contenente Form e Grid
		cntpnlVista.setHeading("Associazione Commesse Dipendenti.");
		cntpnlVista.setFrame(true);
		cntpnlVista.setHeaderVisible(false);
		cntpnlVista.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cntpnlVista.setWidth(1050);
		cntpnlVista.setHeight(870);
		//cntpnlVista.setStyleAttribute("padding", "10px");
		
		ContentPanel cntpnlSelezioni= new ContentPanel(); //Pannello contenente liste dati (Form)
		cntpnlSelezioni.setHeaderVisible(false);
		cntpnlSelezioni.setFrame(false);
		cntpnlSelezioni.setLayout(new FormLayout());
		cntpnlSelezioni.setBorders(false);
		cntpnlSelezioni.setStyleAttribute("padding-top", "10px");
		cntpnlSelezioni.setStyleAttribute("padding-left", "10px");
		cntpnlSelezioni.setWidth(520);
		cntpnlSelezioni.setHeight(800);
		
		ContentPanel cntpnlListe = new ContentPanel(); //pannello contenente le due liste
		cntpnlListe.setHeaderVisible(false);
		cntpnlListe.setSize(500, 690);
		cntpnlListe.setFrame(true);
		cntpnlListe.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cntpnlListe.setStyleAttribute("padding-top", "5px");
		
		ContentPanel cntpnlGrid = new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setLayout(new FitLayout()); 
		cntpnlGrid.setBodyBorder(false);
		cntpnlGrid.setFrame(false);
		cntpnlGrid.setSize(490, 800);
		cntpnlGrid.setStyleAttribute("padding-left", "10px");
		cntpnlGrid.setStyleAttribute("margin-top", "15px");
		cntpnlGrid.setScrollMode(Scroll.AUTOY);
		
		ButtonBar buttonBar= new ButtonBar();
		buttonBar.setAlignment(HorizontalAlignment.CENTER);
		buttonBar.setStyleAttribute("margin-top", "10px");
		buttonBar.setStyleAttribute("padding-top", "7px");
		buttonBar.setStyleAttribute("padding-bottom", "5px");
		buttonBar.setBorders(true);
		buttonBar.setWidth(500);
		
		btnAssocia.setWidth("65px");
		btnAssocia.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
			  if(list2.getElements().size()>0)
				if(smplcmbxCommessa.isValid()){
					List<String> listaDipendenti= new ArrayList<String>();
					String pm= new String();
					String commessa= new String();
					
					if(smplcmbxCommessa.getRawValue().isEmpty()){ commessa="";}
						else{commessa=smplcmbxCommessa.getRawValue().toString();}
					
					for (PersonaleModel p: list2.getStore().getModels()){
						listaDipendenti.add(p.getUsername());
					}
					
					AdministrationService.Util.getInstance().createAssociazioneCommessaDipendenti(pm, commessa, listaDipendenti, //pm non usato lato server
							new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							if(result==false)
								Window.alert("error: Impossibile efettuare l'associazione.");
							else {
								try {
									caricaTabellaDati();
								} catch (Exception e) {
									e.printStackTrace();
									Window.alert("error: Impossibile caricare i dati in tabella.");
								}
								getCommesseByPm(txtNome.getText(), txtCognome.getText());
							}
						}			

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore connessione on createAssociazioneCommessaDipendenti();");
							caught.printStackTrace();
						
						}
					});					
				}else Window.alert("E' necessario selezionare una commessa per effettuare l'associazione!");	
			  else Window.alert("E' necessario selezionare dei dipendenti per effettuare l'associazione!");
			}
		});
			
		
		btnRemove.setEnabled(false);
		btnRemove.setToolTip("Elimina Dipendente");
		btnRemove.setSize(26, 26);
		btnRemove.setIconAlign(IconAlign.TOP);
		btnRemove.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.delete()));
		btnRemove.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {			
				String commessa= new String();
				String nome= new String();
				String cognome= new String();
				int idAssociazione;
				
				commessa=txtfldCommessa.getValue().toString();
				nome=txtfldNome.getValue().toString();
				cognome=txtfldCognome.getValue().toString();
				idAssociazione=Integer.parseInt(txtfldIdAssociazione.getValue().toString());
				
				AdministrationService.Util.getInstance().deleteAssociazioneCommessaDipendenti(idAssociazione, commessa, nome, cognome, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						if(result==false)
							Window.alert("error: Impossibile efettuare l'eliminazione del record.");
						else {						
							try {
								caricaTabellaDati();
							} catch (Exception e) {
								e.printStackTrace();
								Window.alert("error: Impossibile caricare i dati in tabella.");
							}				
						}						
					}			

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on deleteAssociazioneCommessaDipendenti();");
						caught.printStackTrace();					
					}
				});							
			}
		});
		
		
		btnRemoveAll= new Button();
		btnRemoveAll.setEnabled(false);
		btnRemoveAll.setToolTip("Elimina Tutti i Dipendenti");
		btnRemoveAll.setSize(26, 26);
		btnRemoveAll.setIconAlign(IconAlign.TOP);
		btnRemoveAll.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.deleteAll()));
		btnRemoveAll.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {			
				String commessa= new String();
				//String nome= new String();
				//String cognome= new String();
				int idAssociazione;
				
				commessa=txtfldCommessa.getValue().toString();
				//nome=txtfldNome.getValue().toString();
				//cognome=txtfldCognome.getValue().toString();
				idAssociazione=Integer.parseInt(txtfldIdAssociazione.getValue().toString());
				
				AdministrationService.Util.getInstance().deleteAssociazioneCommessaDipendenti(idAssociazione, commessa, "ALL", "", new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						if(result==false)
							Window.alert("error: Impossibile efettuare l'eliminazione del record.");
						else {
							
							try {
								caricaTabellaDati();
							} catch (Exception e) {
								e.printStackTrace();
								Window.alert("error: Impossibile caricare i dati in tabella.");
							}				
						}						
					}			

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore connessione on deleteAssociazioneCommessaDipendenti();");
						caught.printStackTrace();					
					}
				});							
			}
		});
		
		btnReset.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {			
				list1.setEnabled(true);
            	list2.setEnabled(true);
            	list2.getStore().removeAll();
            	btnAssocia.setEnabled(true);
            	btnRemove.setEnabled(false);
            	btnRemoveAll.setEnabled(false);
            	btnRefresh.setEnabled(false);
            	smplcmbxCommessa.setEnabled(true);
            	btnAdd.setEnabled(false);
            	gridCommessa.getSelectionModel().deselectAll();
            	
            	txtfldCommessa.clear();
            	txtfldCognome.clear();
            	txtfldNome.clear();
            	smplcmbxCommessa.setEmptyText("Selezionare la commessa..");
            	//getCommesseByPm(txtNome.getText(), txtCognome.getText());
            	caricaTabellaDati();
            	getListaPersonale();
			}
		});
			
				
		btnAdd.setEnabled(false);
		btnAdd.setSize(26, 26);
		btnAdd.setToolTip("Aggiungi Dipendente.");
		btnAdd.setIconAlign(IconAlign.TOP);
		btnAdd.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.addUser()));
		btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				Dialog d =new  DialogAssociaPtoA(txtfldCommessa.getValue().toString());
				d.show();	
				
				d.addListener(Events.Hide, new Listener<ComponentEvent>() {
				     
					@Override
					public void handleEvent(ComponentEvent be) {
						try {
							caricaTabellaDati();
						} catch (Exception e) {
							e.printStackTrace();
							Window.alert("error: Impossibile caricare i dati in tabella.");
						}
						
				    }
				});
			}
		});
		
		
		ToolBar toolBar = new ToolBar();
		toolBar.add(btnRemove);
		toolBar.add(btnRemoveAll);
		toolBar.add(btnAdd);
		toolBar.setBorders(false);
		toolBar.setHeight("30px");
		toolBar.setAlignment(HorizontalAlignment.RIGHT);
		toolBar.setStyleAttribute("margin-bottom", "1px");		
		
		smplcmbxCommessa.setFieldLabel("Commessa");
		smplcmbxCommessa.setEnabled(true);
		smplcmbxCommessa.setEmptyText("Selezionare la commessa..");
		smplcmbxCommessa.setEditable(false);
		smplcmbxCommessa.setVisible(true);
		smplcmbxCommessa.setTriggerAction(TriggerAction.ALL);
		smplcmbxCommessa.setAllowBlank(false);
		smplcmbxCommessa.addListener(Events.OnClick, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				if(txtRuolo.getText()!=null)
					if(txtRuolo.getText().compareTo("PM")==0)//se è un PM vedrà le sue commesse aperte	
						getCommesseByPm(txtNome.getText(), txtCognome.getText());
					else getCommesseAperte();//altrimenti verranno selezionate tutte le commesse aperte
				else getCommesseAperte();
			}
			
		});
					
		txtfldCommessa.setVisible(false);
		txtfldCognome.setVisible(false);
		txtfldNome.setVisible(false);		
		
		txtNome.setVisible(false);
		txtCognome.setVisible(false);
		txtRuolo.setVisible(false);
		list1.setDisplayProperty("nomeCompleto");
		try {
			getListaPersonale();
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Caricamento lista nomi non effettuato.");
		}
		
		
		list2.setDisplayProperty("nomeCompleto");
		ListStore<PersonaleModel> store = new ListStore<PersonaleModel>();
		store.setStoreSorter(new StoreSorter<PersonaleModel>());
		list2.setStore(store);

		new ListViewDragSource(list1);
		new ListViewDragSource(list2);

		new ListViewDropTarget(list1);
		new ListViewDropTarget(list2);

			
//--------------------------------ISTRUZIONI CREAZIONE CARICAMENTO GRID---------------------------
			 	
	    try {
	    	cmCommessa = new ColumnModel(createColumns());	
		} catch (Exception e) {
			e.printStackTrace();
			Window.alert("error: Problema createColumns().");			
		}	
	    			
		//Vista per permettere il grouping
		GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(true);  
	    view.setStartCollapsed(true);
	    view.setGroupRenderer(new GridGroupRenderer() {  
	      public String render(GroupColumnData data) {  
	        String f = cmCommessa.getColumnById(data.field).getHeader();  
	       // String l = data.models.size() == 1 ? "Item" : "Items";  
	        return f + ": " + data.group ;//+ " (" + data.models.size() + " " + l + ")";  
	      }  
	    });  
		
	    storeGrid.groupBy("commessa");
		gridCommessa = new Grid<PersonaleAssociatoModel>(storeGrid, cmCommessa);   
	    gridCommessa.setBorders(false);  
	    gridCommessa.setStripeRows(true);  
	    gridCommessa.setColumnLines(true);  
	    gridCommessa.setColumnReordering(true);  
	    gridCommessa.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    gridCommessa.setView(view);
	  
	    gridCommessa.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<PersonaleAssociatoModel>>() {  
	          public void handleEvent(SelectionChangedEvent<PersonaleAssociatoModel> be) {  
	        	
	            if (be.getSelection().size() > 0) {      
	              
	            	list1.setEnabled(false);
	            	list2.setEnabled(false);
	            	btnAssocia.setEnabled(false);
	            	
	            	btnRemove.setEnabled(true);
	            	btnRemoveAll.setEnabled(true);
	            	btnAdd.setEnabled(true);
	            	btnRefresh.setEnabled(true);
	            	smplcmbxCommessa.setEnabled(false);
	            	
	            	txtfldCommessa.setValue(be.getSelectedItem().getCommessa());
	            	txtfldCognome.setValue(be.getSelectedItem().getCognome());
	            	txtfldNome.setValue(be.getSelectedItem().getNome());	
	            	txtfldIdAssociazione.setValue(String.valueOf(be.getSelectedItem().getIdAssociazione()));
	            	              
	            } else {  
	                
	            }
	          }
	    }); 
	    
	    		
//-------------------------------------------------------------------------------------------------			    
	    	    
		buttonBar.add(btnAssocia);
		buttonBar.add(btnReset);
		
		cntpnlSelezioni.add(smplcmbxCommessa);
		cntpnlSelezioni.add(txtfldCognome);
		cntpnlSelezioni.add(txtfldNome);
		cntpnlSelezioni.add(txtfldCommessa);
		
		cntpnlSelezioni.add(txtCognome);
		cntpnlSelezioni.add(txtNome);
		cntpnlSelezioni.add(txtRuolo);
		cntpnlSelezioni.add(cntpnlListe);
		cntpnlSelezioni.add(buttonBar);
		
		RowData data = new RowData(.5, 1);
		data.setMargins(new Margins(5));
		cntpnlListe.add(list1, data);
		cntpnlListe.add(list2, data);
		
		cntpnlGrid.add(gridCommessa);
		cntpnlGrid.setTopComponent(toolBar);
		
		cntpnlVista.add(cntpnlSelezioni,new RowData());
		cntpnlVista.add(cntpnlGrid, new RowData());
		
		//cntpnlCommessa.add(cntpnlVista);

		bodyContainer.add(cntpnlVista);

		layoutContainer.add(bodyContainer, new FitData(3, 3, 3, 3));
		add(layoutContainer);
				
	}
	

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("commessa");  
	    column.setHeader("Commessa");  
	    column.setWidth(150);  
	    column.setRowHeader(true);  
	    configs.add(column); 
		
	    column=new ColumnConfig();		
		column.setId("nome");  
		column.setHeader("Nome");  
		column.setWidth(80);  
		column.setRowHeader(true);  
		configs.add(column);
	    
		column=new ColumnConfig();		
		column.setId("cognome");  
		column.setHeader("Cognome");  
		column.setWidth(80);  
		column.setRowHeader(true);  
		configs.add(column);			
		return configs;
	}


	private void caricaTabellaDati() {
		String cognomeApp=new String();
		String ruolo=new String();
		cognomeApp=txtCognome.getText();
		ruolo=txtRuolo.getText();
		
		if(ruolo.compareTo("AMM")==0||ruolo.compareTo("UA")==0||ruolo.compareTo("DIR")==0) //se ad accedere è il DIR o UA o AMM allora vedrà tutte le associazioni
		
			AdministrationService.Util.getInstance().getAssociazioniPersonaleCommessa(new AsyncCallback<List<PersonaleAssociatoModel>>() {
			
				@Override
				public void onSuccess(List<PersonaleAssociatoModel> result) {
					if(result==null)
						Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
					else	
						loadTable(result);		
				}			

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore connessione on getModelAssociazionePersonaleCommessa();");
					caught.printStackTrace();		
				}
			});
		
		if(ruolo.compareTo("PM")==0	)
		AdministrationService.Util.getInstance().getAssociazioniPersonaleCommessaByPM(cognomeApp, new AsyncCallback<List<PersonaleAssociatoModel>>() {
			
			@Override
			public void onSuccess(List<PersonaleAssociatoModel> result) {
				if(result==null)
					Window.alert("error: Impossibile efettuare il caricamento dati in tabella.");
				else	
					loadTable(result);			
			}			

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getModelAssociazionePersonaleCommessa();");
				caught.printStackTrace();
			
			}
		});
	}


	private void loadTable(List<PersonaleAssociatoModel> lista) {

		try {
			storeGrid.removeAll();
			storeGrid.clearGrouping();
			storeGrid.add(lista);
			storeGrid.groupBy("commessa");
			gridCommessa.reconfigure(storeGrid, cmCommessa);
			list2.getElements().clear();		
	    		    	
		} catch (NullPointerException e) {
				e.printStackTrace();
		}			
	}
	
	
	private void getListaPersonale() {	
		AdministrationService.Util.getInstance().getAllPersonaleModel(new AsyncCallback<List<PersonaleModel>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getAllPersonaleModel();");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(List<PersonaleModel> result) {
				if(result!=null){
					
					loadList(result);
					
				}else Window.alert("error: Errore durante l'accesso ai dati Personale.");			
			}		
		});
	}
	
	
//caricamento liste view
	private void loadList(List<PersonaleModel> lista) {
		ListStore<PersonaleModel> store = new ListStore<PersonaleModel>();  
	    List<PersonaleModel> listaApp= new ArrayList<PersonaleModel>();
	    //String app= new String();
	    String app1=new String();
	    
		for(PersonaleModel p:lista){
		//	app=p.getTipologiaLavoratore();
			app1=p.getRuolo();
			
			boolean ruolo=(app1.compareTo("AU")==0);//||(app1.compareTo("PM")==0)||(app1.compareTo("DIR")==0);
			//se l'utente ha ruolo DIP PM o DIR comparirà nella lista associazioni
			if(!ruolo) 
				listaApp.add(p);					
		}
	    
	    store.setStoreSorter(new StoreSorter<PersonaleModel>());  
	    store.setDefaultSort("nomeCompleto", SortDir.ASC);
	    store.add(listaApp);  
	    list1.setStore(store);
	    list1.refresh();
	}
		
	
	private void getCommesseByPm(String nome, String cognome) {	
		AdministrationService.Util.getInstance().getCommesseByPM(nome, cognome, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){			
					smplcmbxCommessa.removeAll();
					java.util.Collections.sort(result);
					smplcmbxCommessa.add(result);
					smplcmbxCommessa.recalculate();
					smplcmbxCommessa.reset();
				}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");		
			}
		});
	}
	
	
	private void getCommesseAperte() {	
		AdministrationService.Util.getInstance().getCommesseAperte( new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore connessione on getCommesseByPM();");
				caught.printStackTrace();				
			}

			@Override
			public void onSuccess(List<String> result) {
				if(result!=null){
					smplcmbxCommessa.removeAll();
					java.util.Collections.sort(result);
					smplcmbxCommessa.add(result);
					smplcmbxCommessa.recalculate();
					smplcmbxCommessa.reset();
				}else Window.alert("error: Errore durante l'accesso ai dati Commesse.");
				
			}
		});
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
		
			if(txtRuolo.getText().compareTo("")!=0){
				caricaTabellaDati();//deve essere caricata qui in quanto dipende dalla restituzione della
									//chiamata asincrona per il ruolo
			}
	}
	
	
	private void recuperoSessionUsername() {
		//Il cognome è la seconda parte dello username e lo posso usare per il confronto con il cognome dei pm sulle commesse
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
		int i=result.indexOf(".");
				
		nome=result.substring(0,i);
		cognome=result.substring(i+1,result.length());
		
		txtNome.setText(nome);
		txtCognome.setText(cognome);
		recuperoSessionRuolo();
	}
	
}
