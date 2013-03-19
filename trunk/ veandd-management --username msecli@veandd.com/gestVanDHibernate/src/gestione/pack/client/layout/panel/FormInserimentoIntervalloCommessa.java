package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

import com.google.gwt.user.client.Element;

public class FormInserimentoIntervalloCommessa extends LayoutContainer {

	public LayoutContainer layoutCol1 = new LayoutContainer();
	public LayoutContainer layoutCol2 = new LayoutContainer();
	public LayoutContainer layoutCol3 = new LayoutContainer();

	public TextField<String> txtfldNumeroCommessa= new TextField<String>();
	public TextField<String> txtfldOreIntervallo = new TextField<String>();
	public TextField<String> txtfldOreViaggio = new TextField<String>();
	
	public TextField<String> txtfldTotOreLavoro = new TextField<String>();
	public TextField<String> txtfldTotOreViaggio = new TextField<String>();
	public Text txtDescrizione = new Text();

	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		
		txtfldNumeroCommessa.setFieldLabel("Commessa");
		txtfldNumeroCommessa.setItemId("numeroCommessa");
		txtfldNumeroCommessa.setEnabled(false);

		txtfldOreIntervallo.setFieldLabel("Ore Lavoro");
		txtfldOreIntervallo.setMaxLength(5);
		txtfldOreIntervallo.setItemId("oreLavoro");
		txtfldOreIntervallo.setAllowBlank(false);
		txtfldOreIntervallo.setRegex("[0-9]*[.]?[0-5]{1}[0-9]{1}|0.0");
		txtfldOreIntervallo.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
		
		txtfldOreViaggio.setFieldLabel("Ore Viaggio");
		txtfldOreViaggio.setMaxLength(5);
		txtfldOreViaggio.setItemId("oreViaggio");
		txtfldOreViaggio.setAllowBlank(false);
		txtfldOreIntervallo.setRegex("[0-9]*[.]?[0-5]{1}[0-9]{1}|0.0");
		txtfldOreIntervallo.getMessages().setRegexText("Deve essere un numero nel formato 99.59");
		
		txtfldTotOreLavoro.setFieldLabel("Tot.Mese");
		txtfldTotOreLavoro.setMaxLength(10);
		txtfldTotOreLavoro.setItemId("totOreLavoro");
		txtfldTotOreLavoro.setEnabled(false);
		
		txtfldTotOreViaggio.setFieldLabel("Tot.Mese");
		txtfldTotOreViaggio.setMaxLength(10);
		txtfldTotOreViaggio.setItemId("totOreViaggio");
		txtfldTotOreViaggio.setEnabled(false);
		//txtfldTotOreViaggio.setStyleAttribute("padding-top", "10px");
		
		txtDescrizione.setItemId("descrizione");
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(75);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol1.setLayout(layout);
		layoutCol1.setItemId("lc1");

		layout = new FormLayout();
		layout.setLabelWidth(75);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol2.setLayout(layout);
		layoutCol2.setItemId("lc2");
		
		layout = new FormLayout();
		layout.setLabelWidth(75);
		layout.setLabelAlign(LabelAlign.LEFT);
		layoutCol3.setLayout(layout);
		layoutCol3.setItemId("lc3");

		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		cp.setSize(580, 80);
		cp.setBorders(false);
		cp.setBodyBorder(false);
		cp.setFrame(false);
		cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		cp.setStyleAttribute("padding-top", "10px");
		cp.setItemId("cp");		

		layoutCol1.add(txtfldNumeroCommessa, new FormData("80%"));
		layoutCol1.add(txtDescrizione, new FormData("100%"));

		layoutCol2.add(txtfldOreIntervallo, new FormData("74%"));
		layoutCol2.add(txtfldOreViaggio, new FormData("74%"));
		
		layoutCol3.add(txtfldTotOreLavoro, new FormData("74%"));
		layoutCol3.add(txtfldTotOreViaggio,new FormData("74%"));

		cp.add(layoutCol1, new RowData(.40, 1));
		cp.add(layoutCol2, new RowData(.30, 1));
		cp.add(layoutCol3, new RowData(.30, 1));
		add(cp);

	}

}
