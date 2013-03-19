package gestione.pack.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrintDataServlet extends HttpServlet  {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String url="http://127.0.0.1:8888/RiepilogoOreMeseDipendente.jsp";
		//response.sendRedirect(url);
		request.getRequestDispatcher("JSPage/RiepilogoOreMeseDipendente.jsp").forward(request, response);

	}
	
}