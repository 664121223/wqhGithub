package com.lucene.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lucene.entity.News;
import com.lucene.service.NewsService;

/**
 * Servlet implementation class EditServlet
 */
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public EditServlet() {
    }

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		News news = new News();
		news.setId(Integer.parseInt(request.getParameter("id")));
		news.setTitle(request.getParameter("title"));
		news.setContent(request.getParameter("content"));
		NewsService newsService = new NewsService();
		
		try {
			newsService.update(news);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("/lucene-day01/findAllServlet");
	}

}
