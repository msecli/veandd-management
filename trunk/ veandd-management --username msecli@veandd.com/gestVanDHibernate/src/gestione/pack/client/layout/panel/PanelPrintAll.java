package gestione.pack.client.layout.panel;


import gestione.pack.client.SessionManagementService;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.DatiComboBox;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelPrintAll extends LayoutContainer {
	
	public PanelPrintAll(){}
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private SimpleComboBox<String> smplcmbxAnno = new SimpleComboBox<String>();
    private SimpleComboBox<String> smplcmbxMese = new SimpleComboBox<String>();
	
	private static String url= "/gestvandhibernate/PrintDataServlet";
	
	protected void onRender(Element target, int index) {  
	    super.onRender(target, index);
	 
	    final FitLayout fl= new FitLayout();
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
		
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
	    
	    ContentPanel cp= new ContentPanel();
	    cp.setHeading("Stampa del riepilogo ore di tutti i Dipendenti");
	    cp.setSize(300, 200);
		
	    smplcmbxMese.setFieldLabel("Mese");
		smplcmbxMese.setName("mese");
		smplcmbxMese.setEmptyText("Mese..");
		smplcmbxMese.setAllowBlank(false);
		 for(String l : DatiComboBox.getMese()){
			 smplcmbxMese.add(l);}
		smplcmbxMese.setTriggerAction(TriggerAction.ALL);

		smplcmbxAnno.setFieldLabel("Anno");
		smplcmbxAnno.setName("anno");
		smplcmbxAnno.setWidth(75);
		smplcmbxAnno.setEmptyText("Anno..");
		smplcmbxAnno.setAllowBlank(false);
		 for(String l : DatiComboBox.getAnno()){
			 smplcmbxAnno.add(l);}
		smplcmbxAnno.setTriggerAction(TriggerAction.ALL);
	    
		cp.add(smplcmbxAnno);
		cp.add(smplcmbxMese);
		
	    com.google.gwt.user.client.ui.Button btnPrint = new com.google.gwt.user.client.ui.Button("Stampa");
	    
	    btnPrint.addClickHandler(new SubmitClickHandler());    
	    
    	//fp.setEncoding(FormPanel.ENCODING_MULTIPART);
	    fp.setMethod(FormPanel.METHOD_POST);
	    fp.setAction(url);
	    fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
	    fp.add(btnPrint);
	    
	    btnPrint.setWidth("100%");
	    cp.add(fp);     	    
	  	    
	    bodyContainer.add(cp);    
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);
	    
	}
	
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			
			if(event.getResults().isEmpty())
				Window.alert("Errore durante la creazione del file!");
			else{
				/*final Dialog d= new Dialog();
				d.setSize(250, 150);
				d.setButtons("");
				d.setStyleAttribute("margin", "10");
				d.setUrl(ConstantiMSG.URLDownloadFileRiepilogoOre);
				
				d.show();*/
				
				Window.open("FileStorage/RiepilogoTotali.pdf", "_blank", "1");
				
			}
		}
	}
	
	private final class SubmitClickHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			String mese="Mar2013";
			SessionManagementService.Util.getInstance().setDataInSession(mese, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Boolean result) {
					fp.submit();
					
				}
			});		
		}
	}
	
}
