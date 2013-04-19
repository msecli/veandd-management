package gestione.pack.client.layout;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class CenterLayout_FileTimbrature extends LayoutContainer{
	public CenterLayout_FileTimbrature() {
		
	}
	
	private int h=Window.getClientHeight();
	private int w=Window.getClientWidth();
	
	private static final String URL = "/gestvandhibernate/UploadServlet";
	private Button submit;
	private FileUpload file;
	private FormPanel form;
	
	
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
		
		ContentPanel cp= new ContentPanel();
		cp.setFrame(true);
		cp.setWidth(330);
		cp.setHeading("Selezionare il file timbrature della settimana corrente.");

	    Panel uploadPanel = new FlowPanel();
	    uploadPanel.setStyleName("FileSubmit");
	    
	    HorizontalPanel horizontalPanel = new HorizontalPanel();
	    horizontalPanel.setSpacing(10);
	    horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	    uploadPanel.add(horizontalPanel);
	    
	    file = new FileUpload();
	    horizontalPanel.add(file);
	    file.setName("file");
	    file.setTitle("select a file");
	    
	    submit = new Button("Invia");
	    horizontalPanel.add(submit);
	    horizontalPanel.setCellVerticalAlignment(submit, HasVerticalAlignment.ALIGN_MIDDLE);
	    submit.setSize("60px", "25px");
	    submit.setTitle("upload file");
	    
	    submit.addClickHandler(new SubmitClickHandler());

	    form = new FormPanel();
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    form.setAction(URL);
	    form.setWidget(uploadPanel);
	    form.addSubmitHandler(new FormSubmitHandler());
	    form.addSubmitCompleteHandler(new FormSubmitCompleteHandler());
		
	    cp.add(form);
	    bodyContainer.add(cp);
		
		layoutContainer.add(bodyContainer, new FitData(5, 5, 5, 8));
		add(layoutContainer);		
	}

	private class FormSubmitHandler implements SubmitHandler {

		@Override
		public void onSubmit(final SubmitEvent event) {
			submit.setEnabled(false);
		}
	}

	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {

		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			form.reset();
			submit.setEnabled(true);
			if(event.getResults().isEmpty())
				Window.alert("Errore durante il caricamento!");
			else
				Window.alert("Caricamento avvenuto con successo!");
			System.out.println(event.getResults());//TODO provare
		}
	}

	private final class SubmitClickHandler implements ClickHandler {

		@Override
		public void onClick(final ClickEvent event) {
			String filename = file.getFilename();

			if (filename.isEmpty()) {
				Window.alert("Select a file");
				return;
			}

			form.submit();
		}
	}

}
	
