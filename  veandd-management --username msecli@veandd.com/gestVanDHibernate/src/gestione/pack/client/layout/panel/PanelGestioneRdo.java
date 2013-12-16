package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

public class PanelGestioneRdo extends LayoutContainer{

	public PanelGestioneRdo(){
		
	}
	
	protected void onRender(Element target, int index) {  
		super.onRender(target, index);
		
		final FitLayout fl= new FitLayout();
			
		LayoutContainer layoutContainer= new LayoutContainer();
		layoutContainer.setBorders(false);
		layoutContainer.setLayout(fl);
	
		
		
		
		//layoutContainer.add(vp, new FitData(0, 3, 3, 0));
		
		add(layoutContainer);
	}
}
