package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;


public class PanelToolAmministrativi extends LayoutContainer{

	public PanelToolAmministrativi(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
	
		VerticalPanel vp= new VerticalPanel();
		vp.setSpacing(5);
		
		Button btnPeriodoSblocco= new Button("Seleziona periodo sblocco.");
		//btnPeriodoSblocco.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.newVersion()));
		btnPeriodoSblocco.setIconAlign(IconAlign.TOP);
		btnPeriodoSblocco.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					DialogSceltaPeriodoSbloccoCompilazione d= new DialogSceltaPeriodoSbloccoCompilazione();
					d.show();					
				}
		});	  
		
		Button btnGiorniFestivi= new Button("Seleziona giorni festivi.");
		//btnPeriodoSblocco.setIcon(AbstractImagePrototype.create(MyImages.INSTANCE.newVersion()));
		btnGiorniFestivi.setIconAlign(IconAlign.TOP);
		btnGiorniFestivi.addSelectionListener(new SelectionListener<ButtonEvent>() {		
				@Override
				public void componentSelected(ButtonEvent ce) {
					DialogGiorniFestivi d= new DialogGiorniFestivi();
					d.show();					
				}
		});	  
				
		vp.add(btnPeriodoSblocco);
		vp.add(btnGiorniFestivi);				
		
		layoutContainer.add(vp, new FitData(0, 3, 3, 0));
			
		add(layoutContainer);
		
	}
}
