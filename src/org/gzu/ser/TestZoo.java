package org.gzu.ser;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gzu.zoo.watcher.ZooWatcherTest;

/**
 * Servlet implementation class TestZoo
 */
@WebServlet("/TestZoo")
public class TestZoo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	private static final int SESSION_TIMEOUT = 10000;
	
	private static final String CONNECTION_STRING = "127.0.0.1:2181";
	
//	private static final
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestZoo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		ZooWatcherTest zkWatcher = new ZooWatcherTest();
		zkWatcher.createConection(CONNECTION_STRING, SESSION_TIMEOUT);
		request.setAttribute("result", "Finish");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/NewFile.jsp");
		dispatcher.forward(request, response);
		
	}

}
