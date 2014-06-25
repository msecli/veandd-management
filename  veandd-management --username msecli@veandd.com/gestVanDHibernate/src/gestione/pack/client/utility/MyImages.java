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
	
	@Source("Icone/printer64x64.png")
	ImageResource print64();

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
	
	@Source("Icone/search.png")
	ImageResource search();	
	
	@Source("Icone/confirm.png")
	ImageResource confirm();
	
	@Source("Icone/rilevColocation64x64.png")
	ImageResource rilevColocation();
	
	@Source("Icone/riepMensDip.png")
	ImageResource riepMensDip();
	
	@Source("Icone/reload20x20.png")
	ImageResource reload();
	
	@Source("Icone/Login16x16.png")
	ImageResource login();
	
	@Source("Icone/associa.png")
	ImageResource associaPtoC();
	
	@Source("Icone/anagrafica.png")
	ImageResource anagrafica();
	
	@Source("Icone/logoEnzo.png")
	ImageResource logoEnzo();
	
	@Source("Icone/save1.png")
	ImageResource save1();
	
	@Source("Icone/riepMensDipPers.png")
	ImageResource riepMensPers();
	
	@Source("Icone/legend24x24.png")
	ImageResource legenda();
	
	@Source("Icone/delete.png")
	ImageResource delete();
	
	@Source("Icone/deleteAll.png")
	ImageResource deleteAll();
	
	@Source("Icone/addUser.png")
	ImageResource addUser();
	
	@Source("Icone/tool1.png")
	ImageResource tool();
	
	@Source("Icone/editPass.png")
	ImageResource editPass();
	
	@Source("Icone/question2.png")
	ImageResource question();
	
	@Source("Icone/gestHwSw.png")
	ImageResource gestHwSw();
	
	@Source("Icone/warning.png")
	ImageResource warning();
	
	@Source("Icone/addList.png")
	ImageResource addList();
	
	@Source("Icone/newVersion.png")
	ImageResource newVersion();
	
	@Source("Icone/generate.png")
	ImageResource generate();
	
	@Source("Icone/delete1.png")
	ImageResource respingi();
	
	@Source("Icone/fattura.png")
	ImageResource fattura();
	
	@Source("Icone/edit.png")
	ImageResource edit();
	
	@Source("Icone/repair.png")
	ImageResource tools();
	
	@Source("Icone/confirmBig.png")
	ImageResource confirmBig();
	
	@Source("Icone/riepSaturazione.png")
	ImageResource riepSaturazione();
	
	@Source("Icone/riep1.png")
	ImageResource riep1();
	
	@Source("Icone/generate1.png")
	ImageResource generate1();
	
	@Source("Icone/save.png")
	ImageResource saveBig();
	
	@Source("Icone/check.png")
	ImageResource check();
}

