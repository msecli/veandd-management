package gestione.pack.client.layout.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestione.pack.client.model.RiepilogoRichiesteModel;
import gestione.pack.client.utility.MyImages;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DialogRichiestaDipendente extends Dialog{

	private GroupingStore<RiepilogoRichiesteModel>store = new GroupingStore<RiepilogoRichiesteModel>();
	private Grid<RiepilogoRichiesteModel> gridRiepilogo;
	private ColumnModel cm;
	private DateField dtfldGiorno= new DateField();
	private TimeField tmfldOra= new TimeField();
	private SimpleComboBox<String> smplcmbxPc= new SimpleComboBox<String>();
	private TextArea txtrGuasto= new TextArea();
	private Button btnConferma= new Button();
	
	private String username;
	
	public DialogRichiestaDipendente(String username){
		this.username=username;
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(1100);
		setHeight(550);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Richieste IT");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		HorizontalPanel hp= new HorizontalPanel();
		hp.setSpacing(2);
		
		ContentPanel cntpnlForm= new ContentPanel();
		cntpnlForm.setHeaderVisible(false);
		cntpnlForm.setHeight(490);
		cntpnlForm.setWidth(300);
		cntpnlForm.setFrame(true);
		cntpnlForm.setBorders(false);
		
		ContentPanel cntpnlGrid= new ContentPanel();
		cntpnlGrid.setHeaderVisible(false);
		cntpnlGrid.setHeight(490);
		cntpnlGrid.setWidth(750);
		cntpnlGrid.setFrame(true);
		cntpnlGrid.setBorders(false);
		
		store.groupBy("numeroCommessa");
		cm=new ColumnModel(createColumns());
			          
		gridRiepilogo= new Grid<RiepilogoRichiesteModel>(store, cm);  
		gridRiepilogo.setItemId("grid");
		gridRiepilogo.setBorders(false);  
		gridRiepilogo.setStripeRows(true);  
		gridRiepilogo.setColumnLines(true);  
		gridRiepilogo.setColumnReordering(true);
		gridRiepilogo.getView().setShowDirtyCells(false);
		
		dtfldGiorno.setValue(new Date());
		
		tmfldOra.setValue(new Time(10, 00));
		
		smplcmbxPc.setEmptyText("Selezionare..");
		smplcmbxPc.setAllowBlank(false);
		smplcmbxPc.setWidth(110);
		smplcmbxPc.setTriggerAction(TriggerAction.ALL);
		
		txtrGuasto.setWidth(270);
		txtrGuasto.setHeight(200);
		
		btnConferma.setToolTip("Richieste");
		btnConferma.setHeight(35);
		btnConferma.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.confirmBig()));
		btnConferma.setIconAlign(IconAlign.BOTTOM);
		btnConferma.setWidth("50%");
		
		VerticalPanel vpForm= new VerticalPanel();
		vpForm.setSpacing(5);
		
		vpForm.add(dtfldGiorno);
		vpForm.add(tmfldOra);
		vpForm.add(smplcmbxPc);
		vpForm.add(txtrGuasto);
		vpForm.add(btnConferma);

		cntpnlForm.add(vpForm);	
		cntpnlGrid.add(gridRiepilogo);
		
		hp.add(cntpnlForm);
		hp.add(cntpnlGrid);
		
		bodyContainer.add(hp);
		
		add(bodyContainer);
	}

	private List<ColumnConfig> createColumns() {
		List <ColumnConfig> configs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column=new ColumnConfig();		
	    column.setId("sede");  
	    column.setHeader("Sede");  
	    column.setWidth(30);  
	    column.setRowHeader(true);  
	    configs.add(column); 
		
		return configs;
	}	
}
