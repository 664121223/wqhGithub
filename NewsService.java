package com.lucene.service;

import java.util.List;

import com.lucene.dao.NewsDao;
import com.lucene.entity.News;
import com.lucene.util.Page;

public class NewsService {
	private NewsDao newsDao;

	public void add(News news) throws Exception {
		newsDao.add(news);
	}

	public void update(News news) throws Exception {
		newsDao.update(news);
	}

	public void delete(Integer id) throws Exception {
		newsDao.delete(id);
	}

	public News get(Integer id) throws Exception {
		return newsDao.get(id);
	}

	public Page findAll(String keywords, int pageNo, int pageSize) throws Exception {
		return newsDao.findAll(keywords, pageNo, pageSize);
	}

}
