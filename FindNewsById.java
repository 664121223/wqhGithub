package com.lucene.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lucene.entity.News;
import com.lucene.service.NewsService;

/**
 * Servlet implementation class FindNewsById
 */
public class FindNewsById extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FindNewsById() {
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		int id = Integer.parseInt(request.getParameter("id"));
		News news = new News();
		NewsService newsService = new NewsService();
		try {
			news = newsService.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(news);
		request.setAttribute("news", news);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);;
	}

}
