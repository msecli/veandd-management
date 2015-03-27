package gestione.pack.client.layout.panel;

import java.util.List;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.RiepilogoCostiDipSuCommesseFatturateModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelRiepilogoCostiSuCommesse  extends LayoutContainer{

	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private ListStore<RiepilogoCostiDipSuCommesseFatturateModel> store=new ListStore<RiepilogoCostiDipSuCommesseFatturateModel>();
	private ColumnModel cm;
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	protected SimpleComboBox<String> smplcmbxAnno;
	protected SimpleComboBox<String> smplcmbxMese;
	protected SimpleComboBox<String> smplcmbxPm;
	protected Button btnAggiorna;
	protected Button btnPrint;
	
	public PanelRiepilogoCostiSuCommesse(){
		
	}
	
	
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(3);
		
		ContentPanel cpGrid= new ContentPanel();
		cpGrid.setHeaderVisible(true);
		//cpGrid.setHeading("Lista Dipendenti.");
		cpGrid.setBorders(false);
		cpGrid.setFrame(true);
		cpGrid.setHeight((h-65));
		cpGrid.setWidth(w-250);
		cpGrid.setScrollMode(Scroll.AUTO);
		cpGrid.setLayout(new FitLayout());
		cpGrid.setButtonAlign(HorizontalAlignment.CENTER);  
		Resizable r=new Resizable(cpGrid);
		
		ToolBar tlbrOpzioni= new ToolBar();
	
		smplcmbxPm= new SimpleComboBox<String>();
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setName("pm");
		smplcmbxPm.setAllowBlank(true);
		smplcmbxPm.setTriggerAction(TriggerAction.ALL);
		smplcmbxPm.setEmptyText("Project Manager..");
		smplcmbxPm.setAllowBlank(false);
		getNomePM();
		
		smplcmbxAnno= new SimpleComboBox<String>();
		smplcmbxAnno.setWidth(80);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
		smplcmbxAnno.setVisible(false);
		smplcmbxAnno.setAllowBlank(false);
				
		smplcmbxMese= new SimpleComboBox<String>();
		smplcmbxMese.setWidth(100);
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setVisible(false);
		smplcmbxMese.setAllowBlank(false);
		
		btnAggiorna=new Button();
		btnAggiorna.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.reload()));
		btnAggiorna.setIconAlign(IconAlign.BOTTOM);
		btnAggiorna.setToolTip("Aggiorna");
		btnAggiorna.setSize(26, 26);
		btnAggiorna.setEnabled(false);
		btnAggiorna.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					
					
				}
		});	  
		
		btnPrint=new Button();
		btnPrint.setEnabled(true);
		btnPrint.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.print24()));
		btnPrint.setIconAlign(IconAlign.TOP);
		btnPrint.setToolTip("Stampa");
		btnPrint.setSize(26, 26);
		btnPrint.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				SessionManagementService.Util.getInstance().setDatiReportCostiCommesseFatturate("RIEP.COSTICOMMESSE", store.getModels(),
						new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error on setNomeReport()");					
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
							fp.submit();
						else
							Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
					}
				});
			}
		});
		
		
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setAction(url);
		fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
		fp.add(btnPrint);
		ContentPanel cp= new ContentPanel();
		cp.setHeaderVisible(false);
		cp.add(fp);
		
		tlbrOpzioni.add(smplcmbxAnno);
		tlbrOpzioni.add(smplcmbxMese);
		tlbrOpzioni.add(smplcmbxPm);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnAggiorna);
		tlbrOpzioni.add(new SeparatorToolItem());
		tlbrOpzioni.add(btnPrint);
		
		layoutContainer.add(cpGrid, new FitData(3, 3, 3, 3));
		
		add(layoutContainer);
	
	}
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			//Window.open("/FileStorage/RiepilogoAnnuale.pdf", "_blank", "1");
			
		}
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
}
