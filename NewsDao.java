package com.lucene.dao;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

import com.lucene.entity.News;
import com.lucene.util.LuceneUtils;
import com.lucene.util.Page;

public class NewsDao {

	public static void add(News news) throws Exception {
		IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getVersion(), LuceneUtils.getAnalyzer());
		IndexWriter wr = new IndexWriter(LuceneUtils.getDirectory(), config);
		Document document = LuceneUtils.javaBean2Document(news);
		wr.addDocument(document);
		wr.close();
	}

	public static void update(News news) throws Exception {
		IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getVersion(), LuceneUtils.getAnalyzer());
		IndexWriter wr = new IndexWriter(LuceneUtils.getDirectory(), config);
		Document doc = LuceneUtils.javaBean2Document(news);
		wr.deleteDocuments(new Term("id", news.getId().toString()));
		wr.addDocument(doc);
		wr.close();
	}

	public static void delete(Integer id) throws Exception {
		IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getVersion(), LuceneUtils.getAnalyzer());
		IndexWriter wr = new IndexWriter(LuceneUtils.getDirectory(), config);
		wr.deleteDocuments(new Term("id", id.toString()));
		wr.close();
	}

	/*
	 * public static List<News> getNewsList() throws Exception{ DirectoryReader
	 * dr = DirectoryReader.open(LuceneUtils.getDirectory()); IndexSearcher
	 * searcher = new IndexSearcher(dr); TermQuery query = new TermQuery(new
	 * Term("id", null)); ScoreDoc[] scoreDocList = searcher.search(query,
	 * 1).scoreDocs; if(scoreDocList.length>0){
	 * 
	 * Document doc = searcher.doc(scoreDocList[0].doc); return
	 * (News)LuceneUtils.decoment2JavaBean(doc, News.class); } return null;
	 * 
	 * }
	 */

	public static News get(Integer id) throws Exception {
		DirectoryReader dr = DirectoryReader.open(LuceneUtils.getDirectory());
		IndexSearcher searcher = new IndexSearcher(dr);
		TermQuery query = new TermQuery(new Term("id", id.toString()));
		ScoreDoc[] scoreDocList = searcher.search(query, 1).scoreDocs;
		if (scoreDocList.length > 0) {

			Document doc = searcher.doc(scoreDocList[0].doc);
			return (News) LuceneUtils.decoment2JavaBean(doc, News.class);
		}
		return null;

	}

	public static Page findAll(String keywords, int pageNo, int pageSize) throws Exception {
		// String keywords = "马";
		List<News> list = new ArrayList<News>();
		DirectoryReader dr = DirectoryReader.open(LuceneUtils.getDirectory());
		IndexSearcher searcher = new IndexSearcher(dr);
		// 单字段搜索
//		 QueryParser qp = new QueryParser(LuceneUtils.getVersion(), "content",
//		 LuceneUtils.getAnalyzer());
		// 多字段搜索
		QueryParser qp = new MultiFieldQueryParser(LuceneUtils.getVersion(), new String[] { "title", "content" },
				LuceneUtils.getAnalyzer());
		Query query = qp.parse(keywords);
		TopDocs topDocs = searcher.search(query, 100);
		ScoreDoc[] hitDoc = topDocs.scoreDocs;

		// 高亮处理搜索结果
		SimpleHTMLFormatter sf = new SimpleHTMLFormatter("<b><span style='color: red'>", "</span></b>");
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		Highlighter highlighter = new Highlighter(sf, scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		int count = topDocs.totalHits;
		System.out.println(count);
		for (int i = (pageNo - 1) * pageSize; i < (pageNo * pageSize > count ? count : pageNo * pageSize); i++) {
				Document doc = searcher.doc(i);
			
				String content = doc.get("content");
				doc.removeField("content");
				String title = doc.get("title");
				doc.removeField("title");
				
				if(content != null && content.length() > 0){
					TokenStream ts = LuceneUtils.getAnalyzer().tokenStream("content", new StringReader(content));
					String newContent = highlighter.getBestFragment(ts, content);
					if(newContent != null){
						doc.add(new Field("content" ,newContent,TextField.TYPE_STORED));
					} else {
						doc.add(new Field("content" ,content,TextField.TYPE_STORED));
					}
				}
				
				if(title != null && title.length() > 0){
					TokenStream ts = LuceneUtils.getAnalyzer().tokenStream("title", new StringReader(title));
					String newTitle = highlighter.getBestFragment(ts, title);
					if(newTitle != null){
						doc.add(new Field("title" ,newTitle,TextField.TYPE_STORED));
					} else {
						doc.add(new Field("title" ,title,TextField.TYPE_STORED));
					}
					News news = (News) LuceneUtils.decoment2JavaBean(doc, News.class);
					list.add(news);
				}
			
		}

		Page page = new Page(pageNo, count, pageSize, list);
		dr.close();
		return page;
	}

}
