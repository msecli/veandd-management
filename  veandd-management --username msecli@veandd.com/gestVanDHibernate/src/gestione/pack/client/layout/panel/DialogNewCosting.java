package gestione.pack.client.layout.panel;


import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;


public class DialogNewCosting extends Dialog{

	private SimpleComboBox<String> smplcmbxCliente;
	private SimpleComboBox<String> smplcmbxCommessa;
	private TextArea txtaDescrizione;
	
	public DialogNewCosting(){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(380);
		setHeight(400);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Dati Costing.");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);	
		
		ContentPanel cntpnlCosting= new ContentPanel();
		cntpnlCosting.setHeaderVisible(false);
		cntpnlCosting.setHeight(350);
		cntpnlCosting.setFrame(true);
		cntpnlCosting.setBorders(false);
				
		LayoutContainer layout1= new LayoutContainer();
		FormLayout layout= new FormLayout();
		layout.setLabelWidth(30);
		layout.setLabelSeparator("");
		layout.setLabelAlign(LabelAlign.LEFT);
		layout1.setLayout(layout);
		layout1.setWidth(360);
		
		smplcmbxCliente= new SimpleComboBox<String>();
		smplcmbxCliente.setEmptyText("Selezionare il cliente..");
		smplcmbxCliente.setAllowBlank(false);
		smplcmbxCliente.setStyleAttribute("padding-top", "15px");
		
		
		
		smplcmbxCommessa= new SimpleComboBox<String>();
		smplcmbxCommessa.setStyleAttribute("padding-top", "10px");
		
		txtaDescrizione= new TextArea();
		txtaDescrizione.setEmptyText("Descrizione..");
		txtaDescrizione.setStyleAttribute("padding-top", "10px");
		txtaDescrizione.setHeight(170);
		
		layout1.add(smplcmbxCliente,new FormData("70%"));
		layout1.add(smplcmbxCommessa,new FormData("70%"));
		layout1.add(txtaDescrizione,new FormData("80%"));
		
		cntpnlCosting.add(layout1);
		
		bodyContainer.add(cntpnlCosting);
		add(bodyContainer);
		
	}
	
}
