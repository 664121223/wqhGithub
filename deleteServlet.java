package com.lucene.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lucene.service.NewsService;

/**
 * Servlet implementation class deleteServlet
 */
public class deleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public deleteServlet() {
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		NewsService newsService = new NewsService();
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			newsService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("删除成功");
		response.sendRedirect("/lucene-day01/findAllServlet");
	}

}
