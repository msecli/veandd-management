package gestione.pack.client.layout.panel;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class DialogCheckCommesseFatturate  extends Dialog{
	
	public DialogCheckCommesseFatturate(){
		
		setLayout(new FitLayout());
		setBodyBorder(true);
		setBodyStyle("padding: 8px; background: none");
		setWidth(900);
		setHeight(550);
		setResizable(false);
		setClosable(true);
		setButtons("");
		setHeading("Richieste IT");
		setModal(true);
		
		LayoutContainer bodyContainer = new LayoutContainer();
		bodyContainer.setLayout(new FlowLayout());
		bodyContainer.setBorders(false);
		
		
		
		
	}

}
