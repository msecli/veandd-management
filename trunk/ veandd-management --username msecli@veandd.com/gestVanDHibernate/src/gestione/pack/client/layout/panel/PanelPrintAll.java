package gestione.pack.client.layout.panel;


import gestione.pack.client.SessionManagementService;
import gestione.pack.client.utility.ClientUtility;
import gestione.pack.client.utility.ConstantiMSG;
import gestione.pack.client.utility.DatiComboBox;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class PanelPrintAll extends LayoutContainer {
	
	public PanelPrintAll(){}
	
	private com.google.gwt.user.client.ui.FormPanel fp= new com.google.gwt.user.client.ui.FormPanel();
	private SimpleComboBox<String> smplcmbxAnno = new SimpleComboBox<String>();
    private SimpleComboBox<String> smplcmbxMese = new SimpleComboBox<String>();
    private SimpleComboBox<String> smplcmbxSede= new SimpleComboBox<String>();
	
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
	    cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
	    cp.setHeading("Stampa del riepilogo ore di tutti i Dipendenti");
	    cp.setFrame(true);
	    cp.setSize(500, 80);
	    		
	    HorizontalPanel vp= new HorizontalPanel();
	    vp.setSpacing(6);
	    
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
		
		smplcmbxSede.setFieldLabel("Sede");
		smplcmbxSede.setWidth(65);
		smplcmbxSede.setEmptyText("Sede..");
		smplcmbxSede.setAllowBlank(false);
		smplcmbxSede.add("T");
		smplcmbxSede.add("B");
	    		
	    com.google.gwt.user.client.ui.Button btnPrint = new com.google.gwt.user.client.ui.Button("Stampa");
	    btnPrint.setSize("55px","25px");	   
	    btnPrint.addClickHandler(new SubmitClickHandler());    
	    
    	//fp.setEncoding(FormPanel.ENCODING_MULTIPART);
	    fp.setMethod(FormPanel.METHOD_POST);
	    fp.setAction(url);
	    fp.addSubmitCompleteHandler(new FormSubmitCompleteHandler());  
	    fp.add(btnPrint);
	    
	    btnPrint.setWidth("100%");
	    
	    vp.add(smplcmbxAnno);
	  	vp.add(smplcmbxMese);
	  	vp.add(smplcmbxSede);
	  	vp.add(fp);
	  	
	  	cp.add(vp);
	    
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
				//Window.open("FileStorage/RiepilogoTotali.pdf", "_blank", "1");
				Window.open("/var/lib/tomcat7/webapps/ROOT/FileStorage/RiepilogoTotali.pdf", "_blank", "1");	
			}
		}
	}
	
	private final class SubmitClickHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			
			String data= new String();
			String anno=smplcmbxAnno.getRawValue().toString();
			String meseCorrente=ClientUtility.traduciMese(smplcmbxMese.getRawValue().toString());
			data=meseCorrente+anno;
			String sedeOperativa=smplcmbxSede.getRawValue().toString();
			String username="";
			
			if(smplcmbxAnno.isValid()&&smplcmbxMese.isValid())
			SessionManagementService.Util.getInstance().setDataInSession(data, sedeOperativa ,username, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on setDataInSession()");					
				}

				@Override
				public void onSuccess(Boolean result) {
					if(result)
						fp.submit();
					else
						Window.alert("Problemi durante il settaggio dei parametri in Sessione (http)");
				}
			});		
			
			else 
				Window.alert("Controllare i dati inseriti!");
		}
	}
	
}
