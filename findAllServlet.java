package com.lucene.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lucene.entity.News;
import com.lucene.service.NewsService;
import com.lucene.util.Page;

/**
 * Servlet implementation class findAllServlet
 */
public class findAllServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int pageNo = 0 ;
	public findAllServlet() {
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String pageStr = request.getParameter("pageNo");
		
		if(pageStr == null || pageStr == ""){
			pageNo = 1;
			
		} else {
			pageNo = Integer.parseInt(pageStr);
		}
		
		Page page = new Page();
		
		String keywords = request.getParameter("keywords");
		
		List<News> list = new ArrayList<News>();
		if(keywords != null && keywords != ""){
			
			NewsService newsService = new NewsService();
			try {
				page = newsService.findAll(keywords, pageNo, 3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("page", page);
		request.setAttribute("keywords", keywords);
		request.getRequestDispatcher("/list.jsp").forward(request, response);
	}

}
