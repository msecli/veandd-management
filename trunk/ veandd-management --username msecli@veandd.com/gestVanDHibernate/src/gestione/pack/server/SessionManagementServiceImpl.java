/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package gestione.pack.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;

import org.hibernate.Session;
import org.hibernate.Transaction;

import gestione.pack.client.SessionManagementService;
import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.RiepilogoCostiDipSuCommesseFatturateModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoMensileOrdiniModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.model.RiferimentiRtvModel;
import gestione.pack.client.model.RtvModel;
import gestione.pack.shared.Personale;


public class SessionManagementServiceImpl extends PersistentRemoteService implements SessionManagementService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SessionManagementServiceImpl(){
		
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
	    gileadHibernateUtil.setSessionFactory(MyHibernateUtil.getSessionFactory());

	    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
	    persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
	    persistentBeanManager.setProxyStore(new StatelessProxyStore());

	    setBeanManager(persistentBeanManager);	
	}
	

	@Override
	public void setUserName(String userName) throws IllegalArgumentException {
		
		HttpSession httpSession = getThreadLocalRequest().getSession(true);  
	    httpSession.setAttribute("userName", userName);
	}

	@Override
	public String getUserName() throws IllegalArgumentException {
		
		HttpSession session = getThreadLocalRequest().getSession(true);
	    if (session.getAttribute("userName") != null)
	    {
	        return (String) session.getAttribute("userName");
	    }
	    else 
	    {
	        return "";
	    }
	}
	
	public String getRuolo() throws IllegalArgumentException{
		
		HttpSession session=getThreadLocalRequest().getSession(true);
		 if (session.getAttribute("ruolo") != null)
		    {
		        return (String) session.getAttribute("ruolo");
		    }
		 else 
		    {
		        return "";
		    }
	}
	
	
	public String getSede() throws IllegalArgumentException{
		
		HttpSession session=getThreadLocalRequest().getSession(true);
		 if (session.getAttribute("sede") != null)
		    {
		        return (String) session.getAttribute("sede");
		    }
		 else 
		    {
		        return "";
		    }
	}
	

	public String login(String username, String password){
	       
		   String ruolo= new String();
		   String descrizione= new String(); //contiene ruolo;tipoLavoratore
	       Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		   Transaction tx= null;
		   //Invalidare la sessione al momento del Login nel caso in cui ci fosse una sessione http ancora aperta 
		   HttpServletRequest request = this.getThreadLocalRequest();
	   	   HttpSession httpSession = request.getSession();	   
	   	   
	   	   httpSession.setMaxInactiveInterval(180*60);//durata dati in sessione di 3 ora
	   	   
	   	   if(!httpSession.isNew()){
	   		   
	   		   //httpSession.invalidate(); 
	   	   }
	   	   
	       try{
	    	    tx=	session.beginTransaction();

				Personale p = new Personale();
				p=(Personale)session.createQuery("from Personale where username=:username and password=:password").setParameter("username", username).
						setParameter("password", password).uniqueResult();
				if(p!=null){
					descrizione=p.getRuolo()+";"+p.getTipologiaLavoratore()+":"+p.getSede();// metto anche la sede per differenziare il foglio ore
					ruolo= p.getRuolo();
					httpSession.setAttribute("ruolo", ruolo);
					httpSession.setAttribute("userName", username);
					httpSession.setAttribute("idUtente", p.getId_PERSONALE());
					httpSession.setAttribute("sede", p.getSede());
				}else {ruolo=null;}
				          
				tx.commit();
				
	       }catch(Exception e){
	    	   ruolo = null;
	    	   if (tx!=null)
					tx.rollback();
	    	   e.printStackTrace();
	       }             
	       return descrizione;
	    }
	
	
	@Override
	public void logOut() throws IllegalArgumentException{
		HttpServletRequest request = this.getThreadLocalRequest();
    	HttpSession session = request.getSession();
		session.invalidate();
	}


	@Override
	public boolean setDataInSession(String mese, String sede, String username, String operazione) throws IllegalArgumentException{
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("mese", mese);
		   	 httpSession.setAttribute("username", username);
		   	 httpSession.setAttribute("sede", sede);
		   	 httpSession.setAttribute("operazione", operazione);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 		
	}


	@Override
	public boolean setDataInSession(String dataRif, String username, String operazione, 
			String totOreCommesse, String totOreIU) throws IllegalArgumentException{
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("mese", dataRif);
		   	 httpSession.setAttribute("username", username);
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("totOreCommesse", totOreCommesse);
		     httpSession.setAttribute("totOreIU", totOreIU);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}


	@Override
	public boolean setDataReportAnnualeInSession(List<RiepilogoOreAnnualiDipendente> listaRiep, String operazione) throws IllegalArgumentException{
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("listaRiep", listaRiep);
		   	
		     httpSession.setAttribute("operazione", operazione);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}


	@Override
	public boolean setDataReportDatiFatturazioneInSession(String anno,
			String mese, List<DatiFatturazioneMeseModel> listaD, String operazione) throws IllegalArgumentException {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("anno", anno);
		   	 httpSession.setAttribute("mese", mese);
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("listaDati", listaD);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}


	@Override
	public boolean setNomeReport(String tipoReport, List<RiepilogoOreNonFatturabiliModel> lista) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("operazione", tipoReport);
		   	 httpSession.setAttribute("lista", lista);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDatiReportCostiDip(String operazione,	List<RiepilogoCostiDipendentiModel> lista)
			throws IllegalArgumentException {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("operazione", operazione);
		   	 httpSession.setAttribute("lista", lista);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDataFileTmbInSession(String data, String sede)
			throws IllegalArgumentException {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("data", data);
		   	 httpSession.setAttribute("sede", sede);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDatiReportSalPcl(String operazione,
			List<RiepilogoSALPCLModel> lista) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("operazione", operazione);
		   	 httpSession.setAttribute("lista", lista);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDatiReportSalPclRiassunto(String operazione,
			List<RiepilogoSALPCLModel> models) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("operazione", operazione);
		   	 httpSession.setAttribute("lista", models);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDataReportDatiFatturazioneInSession(String operazione,
			List<DatiFatturazioneMeseModel> listaDati) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("operazione", operazione);
		   	 httpSession.setAttribute("lista", listaDati);
		     
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDataRiepilogoCommesseInSession(String dataR,
			String usernameR, String operazione, String totOreCommesse,
			String totOreIU, List<RiepilogoMeseGiornalieroModel> models, RiepilogoFoglioOreModel riepOreTot) {
		
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("mese", dataR);
		   	 httpSession.setAttribute("username", usernameR);
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("totOreCommesse", totOreCommesse);
		     httpSession.setAttribute("totOreIU", totOreIU);
		     httpSession.setAttribute("listaM", models);
		     httpSession.setAttribute("riepilogoTotali", riepOreTot);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDataFattura(String numeroOrdine,
			int idFoglioFatturazione, FatturaModel fM, List<AttivitaFatturateModel> listaA, String operazione) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	   
		   	 httpSession.setAttribute("numeroOrdine", numeroOrdine);
		   	 httpSession.setAttribute("idFoglioFatturazione", idFoglioFatturazione);
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("fatturaModel", fM);
		     httpSession.setAttribute("listaA", listaA);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDatiMensileInSession(String operazione, String anno, String pm, String stato,
			List<RiepilogoMensileOrdiniModel> models) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("anno", anno);
		     httpSession.setAttribute("listaM", models);
		     httpSession.setAttribute("pm", pm);
		     httpSession.setAttribute("stato", stato);
		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean setDataRtv(RtvModel rtv, RiferimentiRtvModel rifM,
			String tipoModulo, String operazione) throws IllegalArgumentException {
		
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	
		   	 httpSession.setAttribute("rtv", rtv);
		   	 httpSession.setAttribute("rifM", rifM);
		     httpSession.setAttribute("operazione", operazione);
		     httpSession.setAttribute("tipoModulo", tipoModulo);
		    		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
				
	}


	@Override
	public boolean setDatiReportCostiCommesseFatturate(String operazione,
			List<RiepilogoCostiDipSuCommesseFatturateModel> listaDati) {
		try {
			 HttpServletRequest request = this.getThreadLocalRequest();
		   	 HttpSession httpSession = request.getSession();
		   	
		   	 httpSession.setAttribute("lista", listaDati);
		   	
		     httpSession.setAttribute("operazione", operazione);
		    		   	 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
