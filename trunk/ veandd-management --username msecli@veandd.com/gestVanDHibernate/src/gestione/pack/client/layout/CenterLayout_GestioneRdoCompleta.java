package gestione.pack.client.layout;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.RdoCompletaModel;
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
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
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
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class CenterLayout_GestioneRdoCompleta extends LayoutContainer{

	public CenterLayout_GestioneRdoCompleta(){}
	
	private TextField<String> txtfldNumeroRda;
	private TextField<String> txtfldIdRda=new TextField<String>();	
	private TextField<String> txtfldNumeroOfferta;
	private TextArea txtrDescrizione;
	private TextField<String> txtfldImporto;
	private DateField dtfldDataOfferta;	
	private TextField<String> txtfldNumeroOrdine;
	private TextField<String> txtfldTariffaOraria;
	private DateField dtfldDataInizioOrdine;
	private DateField dtfldDataFineOrdine;
	private TextField<String> txtfldNumeroRisorse;
	private TextField<String> txtfldNumeroOre;
	private TextField<String> txtfldNumeroOreResidue;
	private SimpleComboBox<String> smplcmbxCliente = new SimpleComboBox<String>();
	
	private VerticalPanel hpLayout;
	
	private GroupingStore<RdoCompletaModel>store = new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeCompleto= new GroupingStore<RdoCompletaModel>();
	private GroupingStore<RdoCompletaModel> storeResult= new GroupingStore<RdoCompletaModel>();
	private List<RdoCompletaModel> lista= new ArrayList<RdoCompletaModel>();
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
		
		ContentPanel cntpnlRdo = new ContentPanel();
		cntpnlRdo.setHeading("Gestione Rdo.");
		cntpnlRdo.setHeaderVisible(true);
		cntpnlRdo.setCollapsible(false);
		cntpnlRdo.setBorders(false);
		cntpnlRdo.setWidth(w-215);
		cntpnlRdo.setHeight(h-55);
		cntpnlRdo.setScrollMode(Scroll.AUTO);
		
		hpLayout= new VerticalPanel();
		hpLayout.setSpacing(10);
		hpLayout.setItemId("hpLayout");
				
		ContentPanel cntpnlLayout= new ContentPanel();
		cntpnlLayout.setHeading("Dati Rdo.");
		cntpnlLayout.setHeaderVisible(false);
		cntpnlLayout.setCollapsible(false);
		cntpnlLayout.setBorders(false);
		cntpnlLayout.setWidth(860);
		cntpnlLayout.setHeight(680);
		cntpnlLayout.setFrame(true);
		cntpnlLayout.setButtonAlign(HorizontalAlignment.CENTER);
		cntpnlLayout.setStyleAttribute("padding-left", "7px");
		cntpnlLayout.setStyleAttribute("margin-top", "15px");
		
		btnSave= new Button("Save");
		btnEdit=new Button("Edit");
		btnEdit.setEnabled(false);
		btnDelete=new Button("Delete");
		btnDelete.setEnabled(false);
		btnReset=new Button("X");
		
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
				Date dataInizio=null;
				Date dataFine=null;
				String tariffa="0.0";
				String numRisorse="0";
				String oreDisp="0.0";
				String oreRes="0.0";
				
				if(formIsValid()&&!arePresent()){
				
					cliente=smplcmbxCliente.getRawValue().toString();
					if(!txtfldNumeroRda.getRawValue().isEmpty())numRdo=txtfldNumeroRda.getValue().toString();
					
					if(!txtfldNumeroOfferta.getRawValue().isEmpty())numOfferta=txtfldNumeroOfferta.getValue().toString();
					if(!txtfldImporto.getRawValue().isEmpty())importo=txtfldImporto.getValue().toString();
					if(!dtfldDataOfferta.getRawValue().isEmpty())dataOfferte=dtfldDataOfferta.getValue();
					
					if(!txtfldNumeroOrdine.getRawValue().isEmpty())numOrdine=txtfldNumeroOrdine.getValue().toString();	
					if(!txtrDescrizione.getRawValue().isEmpty())descrizione=txtrDescrizione.getValue().toString();
					if(!dtfldDataInizioOrdine.getRawValue().isEmpty())dataInizio=dtfldDataInizioOrdine.getValue();
					if(!dtfldDataFineOrdine.getRawValue().isEmpty())dataFine=dtfldDataFineOrdine.getValue();
					if(!txtfldTariffaOraria.getRawValue().isEmpty())tariffa=txtfldTariffaOraria.getValue().toString();
					if(!txtfldNumeroRisorse.getRawValue().isEmpty())numRisorse=txtfldNumeroRisorse.getValue().toString();
					if(!txtfldNumeroOre.getRawValue().isEmpty())oreDisp=txtfldNumeroOre.getValue().toString();
					if(!txtfldNumeroOreResidue.getRawValue().isEmpty())oreRes=txtfldNumeroOreResidue.getValue().toString();
									
					AdministrationService.Util.getInstance().saveRdoCompleta(numRdo, cliente, numOfferta, dataOfferte, importo, numOrdine, descrizione
							, dataInizio, dataFine, tariffa, numRisorse, oreDisp, oreRes, new AsyncCallback<Boolean>() {
					
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
					Window.alert("Controllare i campi inseriti!");
				}
			}
		});
		
		
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
				Date dataInizio= null;
				Date dataFine=null;
				String tariffa="0.0";
				String numRisorse="0";
				String oreDisp="0.0";
				String oreRes="0.0";
				
				if(formIsValid()){
					idRdo=Integer.valueOf(txtfldIdRda.getValue());
					cliente=smplcmbxCliente.getRawValue().toString();
					if(!txtfldNumeroRda.getRawValue().isEmpty())numRdo=txtfldNumeroRda.getValue().toString();
					
					if(!txtfldNumeroOfferta.getRawValue().isEmpty())numOfferta=txtfldNumeroOfferta.getValue().toString();
					if(!txtfldImporto.getRawValue().isEmpty())importo=txtfldImporto.getValue().toString();
					if(!dtfldDataOfferta.getRawValue().isEmpty())dataOfferte=dtfldDataOfferta.getValue();
					
					if(!txtfldNumeroOrdine.getRawValue().isEmpty())numOrdine=txtfldNumeroOrdine.getValue().toString();	
					if(!txtrDescrizione.getRawValue().isEmpty())descrizione=txtrDescrizione.getValue().toString();
					if(!dtfldDataInizioOrdine.getRawValue().isEmpty())dataInizio=dtfldDataInizioOrdine.getValue();
					if(!dtfldDataFineOrdine.getRawValue().isEmpty())dataFine=dtfldDataFineOrdine.getValue();
					if(!txtfldTariffaOraria.getRawValue().isEmpty())tariffa=txtfldTariffaOraria.getValue().toString();
					if(!txtfldNumeroRisorse.getRawValue().isEmpty())numRisorse=txtfldNumeroRisorse.getValue().toString();
					if(!txtfldNumeroOre.getRawValue().isEmpty())oreDisp=txtfldNumeroOre.getValue().toString();
					if(!txtfldNumeroOreResidue.getRawValue().isEmpty())oreRes=txtfldNumeroOreResidue.getValue().toString();
									
					AdministrationService.Util.getInstance().editRdoCompleta(idRdo, numRdo, cliente, numOfferta, dataOfferte, importo, numOrdine, descrizione
							, dataInizio, dataFine, tariffa, numRisorse, oreDisp, oreRes, new AsyncCallback<Boolean>() {
					
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
					Window.alert("Controllare i campi inseriti!");
				}
			}
		});
		
		
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
		
		cntpnlLayout.addButton(btnSave);
		cntpnlLayout.addButton(btnEdit);
		cntpnlLayout.addButton(btnDelete);
		cntpnlLayout.addButton(btnReset);
		txtfldIdRda.setFieldLabel("id");
		hpLayout.add(new CntpnlFormRdo());
		hpLayout.add(new CntpnlGridRdo());
		
		cntpnlLayout.add(hpLayout);	
		
		//cntpnlRdo.add(cntpnlLayout);
		
	    bodyContainer.add(cntpnlLayout);    
	   				
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);
		
	}
	
	private class CntpnlFormRdo extends ContentPanel{
		
		public CntpnlFormRdo() {
		
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(800);
			setHeight(200);
			setFrame(false);
			setStyleAttribute("margin-top", "0px");
			
			ContentPanel cp= new ContentPanel();
			cp.setHeaderVisible(false);
			cp.setSize(790, 200);
			cp.setBorders(false);
			cp.setBodyBorder(false);
			cp.setFrame(false);
			cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
						
			layoutCol2.setStyleAttribute("padding-left", "20px");
			
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
			
			layoutCol1.add(smplcmbxCliente, new FormData("100%"));
			layoutCol1.add(txtfldNumeroRda, new FormData("80%"));
			
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
			
			txtrDescrizione=new TextArea();
			txtrDescrizione.setMaxLength(200);
			txtrDescrizione.setFieldLabel("Descrizione");
			txtrDescrizione.setName("descrizione");
			txtrDescrizione.setHeight("130");
			
			layoutCol2.add(txtfldNumeroOfferta, new FormData("85%"));
			layoutCol2.add(dtfldDataOfferta, new FormData("85%"));
			layoutCol2.add(txtfldImporto, new FormData("65%"));
			layoutCol1.add(txtrDescrizione, new FormData("100%"));
			
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
			txtfldNumeroOre.setFieldLabel("Num. Ore");
			txtfldNumeroOre.setName("numeroOre");
			txtfldNumeroOre.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
			txtfldNumeroOre.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
			txtfldNumeroOre.addKeyListener(new KeyListener(){
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
							}						
						}
				 }
			});
			
			txtfldNumeroOreResidue=new TextField<String>();
			txtfldNumeroOreResidue.setFieldLabel("Ore Residue");
			txtfldNumeroOreResidue.setName("numeroOreResidue");
			txtfldNumeroOreResidue.setRegex("[0-9]+[.]{1}[0-9]{1}[0-9]{1}|[0-9]+[.]{1}[0]{1}|0.00|0.0");
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
			
			layoutCol3.add(txtfldNumeroOrdine,new FormData("85%"));
			layoutCol3.add(dtfldDataInizioOrdine,new FormData("85%"));
			layoutCol3.add(dtfldDataFineOrdine, new FormData("85%"));
			layoutCol3.add(txtfldTariffaOraria, new FormData("60%"));
			layoutCol3.add(txtfldNumeroRisorse, new FormData("60%"));
			layoutCol3.add(txtfldNumeroOre, new FormData("60%"));
			layoutCol3.add(txtfldNumeroOreResidue, new FormData("60%"));			
			
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
	
	
	private class CntpnlGridRdo extends ContentPanel{
		
		public CntpnlGridRdo(){
		
			setHeaderVisible(false);
			setCollapsible(false);
			setBorders(false);
			setBodyBorder(false);
			setScrollMode(Scroll.NONE);	
			setWidth(820);
			//setHeight(360);
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
		                   
		            } else {  
		              
		            }
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
		    
		    
		    //TODO Ricerca
		    
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
		    			 for(RdoCompletaModel r:lista){
		    				 if(r.getNumeroCommessa().contains(campo) || r.getNumeroOfferta().compareTo(campo)==0 || 
		    						 r.getNumeroOrdine().contains(campo) || r.getNumeroRda().compareTo(campo)==0){
		    					 storeResult.add(r);		    				 
		    				 }
		    			 }
		    			 lista.clear();
		    			 lista.addAll(store.getModels());
		    			 gridRiepilogo.reconfigure(storeResult, cm);			 
		    		 } 
		    	 }	    	  	 
		    });		   
		    
		    tlbarSearchField.add(txtfldsearch);
		    tlbarSearchField.add(btnSearch);
		    
		    ContentPanel cntpnlGrid= new ContentPanel();
		    cntpnlGrid.setBodyBorder(false);  
		    cntpnlGrid.setBorders(false);
		    cntpnlGrid.setFrame(true);
		    cntpnlGrid.setLayout(new FitLayout());  
		    cntpnlGrid.setHeaderVisible(false);
		    cntpnlGrid.setWidth(820);
		    cntpnlGrid.setHeight(360);
		    cntpnlGrid.setScrollMode(Scroll.AUTOY);
		    cntpnlGrid.add(gridRiepilogo);
		    cntpnlGrid.setTopComponent(tlbarSearchField);
		    	    
		    add(cntpnlGrid);		  	
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
		    column.setWidth(90);  
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
			    
			column=new ColumnConfig();		
		    column.setId("importo");  
		    column.setHeader("Importo");  
		    column.setWidth(70);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);  
		       
		    column=new ColumnConfig();		
		    column.setId("numeroOrdine");  
		    column.setHeader("N. Ordine");  
		    column.setWidth(80);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column); 
		    
		    column=new ColumnConfig();		
		    column.setId("tariffaOraria");  
		    column.setHeader("Tariffa");  
		    column.setWidth(60);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
		    column=new ColumnConfig();		
		    column.setId("numeroRisorse");  
		    column.setHeader("N. Risorse");  
		    column.setWidth(55);  
		    column.setRowHeader(true);  
		    column.setAlignment(HorizontalAlignment.RIGHT);
		    configs.add(column);
		    
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
			storeResult.add(store.getModels());
			storeCompleto.add(store.getModels());
			lista.addAll(store.getModels());
						    		    	
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
		
		return true;
	}	
	
	private void resetForm() {
		
		smplcmbxCliente.clear();
		txtfldNumeroRda.clear();
		txtrDescrizione.clear();
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
		gridRiepilogo.getSelectionModel().deselectAll();
	}
	
}

