package gestione.pack.client.utility;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyImages extends ClientBundle{

	public static final MyImages INSTANCE = GWT.create(MyImages.class);
	
	@Source("Icone/rilev24x24.png")
	ImageResource datiTimb();
	
	@Source("Icone/escl124x24.png")
	ImageResource escl1();
	
	@Source("Icone/logout.png")
	ImageResource logout();
	
	@Source("Icone/setting.png")
	ImageResource setting();
	
	@Source("Icone/idea.png")
	ImageResource idea();
	
	@Source("Icone/refresh1.png")
	ImageResource refresh();
	
	@Source("Icone/save24x24.png")
	ImageResource save();
	
	@Source("Icone/save24x24.png")
	ImageResource saveLittle();
	
	@Source("Icone/printer2_16x16.png")
	ImageResource print();
	
	@Source("Icone/printer24x24.png")
	ImageResource print24();

	@Source("Icone/winprogress.jpg")
	ImageResource workinprogress();
	
	@Source("Icone/riep_comm.png")
	ImageResource riep_comm();
	
	@Source("Icone/printer1.png")
	ImageResource printBig();
	
	@Source("Icone/upload3.png")
	ImageResource upload();
	
	@Source("Icone/rilevPresenze64x64.png")
	ImageResource presenze();
	
	@Source("Icone/rilevPresenzeDip.png")
	ImageResource presenzeDip();
	
	@Source("Icone/arrowdown.png")
	ImageResource arrowdown();
	
	@Source("Icone/azzera.png")
	ImageResource azzera();
	
	@Source("Icone/Close20x20.png")
	ImageResource chiudiCommessa();
	
	@Source("Icone/add.png")
	ImageResource add();
	
	@Source("Icone/elimina.png")
	ImageResource elimina();
	
	
}

