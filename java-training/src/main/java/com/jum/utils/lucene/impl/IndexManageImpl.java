//package com.jum.utils.lucene.impl;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.jum.utils.lucene.AbstractIndexManage;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.Field.Store;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
//import org.apache.lucene.search.BooleanClause.Occur;
//import org.apache.lucene.search.BooleanQuery;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.NumericRangeQuery;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TermQuery;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.highlight.Fragmenter;
//import org.apache.lucene.search.highlight.Highlighter;
//import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
//import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
//import org.springframework.stereotype.Service;
//
//@Service
//public class IndexManageImpl<E> extends AbstractIndexManage<E>{
//
//
//
//	@Override
//	public void creatIndex() throws Exception{
//		//采集数据
//		List<VersionHis> list = outService.indexInfo();
//		List<Document> docList = new ArrayList<>();
//		Document document;
////		FieldType type = new FieldType(TextField.TYPE_STORED);
////		type.setStoreTermVectorOffsets(true);
////		type.setStoreTermVectorPositions(true);
////		type.setStoreTermVectors(true);
////		type.freeze();
//
//		for(VersionHis versionHis:list){
//			document = new Document();
//			Field id = new StringField("id",versionHis.getId().toString() , Store.YES);
//			Field syscode = new StringField("syscode",versionHis.getSyscode().toString() , Store.YES);
//			Field sysname = new TextField("sysname",versionHis.getSysname().toString() , Store.YES);
//			Field sysenglishname = new TextField("sysenglishname",versionHis.getSysenglishname().toString() , Store.YES);
//			Field versioncode = new StringField("versioncode",versionHis.getVersioncode().toString() , Store.YES);
//			Field date = new StringField("date",versionHis.getDate().toString() , Store.YES);
//			Field content = new TextField("content",versionHis.getContent().toString() , Store.YES);
//
//			document.add(id);
//			document.add(syscode);
//			document.add(sysname);
//			document.add(sysenglishname);
//			document.add(versioncode);
//			document.add(date);
//			document.add(content);
//
//			docList.add(document);
//		}
//
//		//创建分词器
//		Analyzer analyzer = new IKAnalyzer();
//
//		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
//
//		String _path = System.getProperty("catalina.home");
//
//		File indexFile = new File(_path+"\\webapps\\indexDir\\sysOldVersion\\");
//		Directory dirctory = FSDirectory.open(indexFile);
//		IndexWriter writer = new IndexWriter(dirctory,cfg);
//		writer.deleteAll();
//		for(Document doc:docList){
//			writer.addDocument(doc);
//		}
//		System.out.println("创建成功");
//		writer.close();
//	}
//
//	/**
//	 * 更新索引
//	 */
//	@Override
//	public void updateIndex(VersionHis versionHis) throws Exception {
//		// 创建分词器，标准分词器
//		Analyzer analyzer = new IKAnalyzer();
//		// 创建IndexWriter
//		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3,
//				analyzer);
//
//		String _path = System.getProperty("catalina.home");
//		File indexFile = new File(_path+"\\webapps\\indexDir\\sysOldVersion\\");
//		Directory directory = FSDirectory
//				.open(indexFile);
//		// 创建IndexWriter
//		IndexWriter writer = new IndexWriter(directory, cfg);
//
//		// 第一个参数：指定查询条件
//		// 第二个参数：修改之后的对象
//		// 修改时如果根据查询条件，可以查询出结果，则将以前的删掉，然后覆盖新的Document对象，如果没有查询出结果，则新增一个Document
//		// 修改流程即：先查询，再删除，在添加
//		Document document = new Document();
//		Field id = new StringField("id",versionHis.getId().toString() , Store.YES);
//		Field syscode = new StringField("syscode",versionHis.getSyscode().toString() , Store.YES);
//		Field sysname = new TextField("sysname",versionHis.getSysname().toString() , Store.YES);
//		Field sysenglishname = new TextField("sysenglishname",versionHis.getSysenglishname().toString() , Store.YES);
//		Field versioncode = new StringField("versioncode",versionHis.getVersioncode().toString() , Store.YES);
//		Field date = new StringField("date",versionHis.getDate().toString() , Store.YES);
//		Field content = new TextField("content",versionHis.getContent().toString() , Store.YES);
//
//		document.add(id);
//		document.add(syscode);
//		document.add(sysname);
//		document.add(sysenglishname);
//		document.add(versioncode);
//		document.add(date);
//		document.add(content);
//
//		writer.updateDocument(new Term("id", versionHis.getId().toString()), document);
//		writer.close();
//	}
//
//	/**
//	 * 删除索引
//	 */
//	@Override
//	public void deleteIndex(String uuid) throws Exception {
//		// 创建分词器，标准分词器
//		Analyzer analyzer = new IKAnalyzer();
//
//		// 创建IndexWriter
//		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3,
//				analyzer);
//		String _path = System.getProperty("catalina.home");
//		File indexFile = new File(_path+"\\webapps\\indexDir\\sysOldVersion\\");
//		Directory directory = FSDirectory
//				.open(indexFile);
//		// 创建IndexWriter
//		IndexWriter writer = new IndexWriter(directory, cfg);
//
//		// Terms
//		 writer.deleteDocuments(new Term("id", uuid));
//
//		// 删除全部（慎用）
////		writer.deleteAll();
//
//		writer.close();
//	}
//
//
//	/**
//	 * boolean查询
//	 * 2018年1月31日 上午10:16:13
//	 */
//	@Override
//	public void booleanQuery(){
//		BooleanQuery query = new BooleanQuery();
//
//		Query q1 = new TermQuery(new Term("",""));
//		Query q2 = NumericRangeQuery.newFloatRange("id", 55f, 60f, true, false);
//
//		query.add(q1, Occur.MUST);
//		query.add(q2, Occur.MUST_NOT);
//
//		doSearch(query);
//	}
//	@Override
//	public void multiFieldQueryParser() throws Exception{
//		String[] fields = {"dete","versioncode","content"};
//		Analyzer analyzer = new IKAnalyzer();
//		Map<String,Float> boots = new HashMap<String, Float>();
//		boots.put("content", 200f);
//		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer,boots);
//
//		Query query = parser.parse("测试");
//
//		doSearch(query);
//	}
//
//	/**
//	 * doSearcher
//	 * 2018年1月31日 上午10:15:00
//	 * @param query
//	 */
//	@Override
//	public String doSearch(Query query){
//		JSONArray resultJArray = new JSONArray();
//		JSONObject resultObj;
//		int count = 0;
//		//创建IndexSearch
//		//指定索引地址
//		try {
//			String _path = System.getProperty("catalina.home");
//			File indexFile = new File(_path+"\\webapps\\indexDir\\sysOldVersion\\");
//			Directory directory = FSDirectory.open(indexFile);
//			IndexReader reader = DirectoryReader.open(directory);
//			IndexSearcher searcher = new IndexSearcher(reader);
//
//			TopDocs topDocs = searcher.search(query, 10000);//查询对象，插叙前N条结果
//
//
//			count = topDocs.totalHits;//总结果数
//			System.out.println("记录结果总条数：："+count);
//
//			ScoreDoc[] scoreDocs = topDocs.scoreDocs;//前N条信息得分
//
//			for(ScoreDoc scoreDoc:scoreDocs){
//
//				int docId = scoreDoc.doc;//文档 ID
//
//				//通过ID获取文档
//				Document doc = searcher.doc(docId);
//				resultObj = new JSONObject();
////				resultObj.put("id", doc.get("id"));
////				resultObj.put("syscode", doc.get("syscode"));
////				resultObj.put("sysname", doc.get("sysname"));
////				resultObj.put("sysenglishname", doc.get("sysenglishname"));
////				resultObj.put("date", doc.get("date"));
////				resultObj.put("versioncode", doc.get("versioncode"));
////				resultObj.put("content", doc.get("content"));
//				resultObj.put("id",lightStr(query,new IKAnalyzer(),"id",doc.get("id").toString()));
//				resultObj.put("syscode",lightStr(query,new IKAnalyzer(),"syscode",doc.get("syscode").toString()));
//				resultObj.put("sysname",lightStr(query,new IKAnalyzer(),"sysname",doc.get("sysname").toString()));
//				resultObj.put("sysenglishname",lightStr(query,new IKAnalyzer(),"sysenglishname",doc.get("sysenglishname").toString()));
//				resultObj.put("date",lightStr(query,new IKAnalyzer(),"date",doc.get("date").toString()));
//				resultObj.put("versioncode",lightStr(query,new IKAnalyzer(),"versioncode",doc.get("versioncode").toString()));
//				resultObj.put("content",lightStr(query,new IKAnalyzer(),"content",doc.get("content").toString()));
//
//				resultJArray.add(resultObj);
//			}
//			reader.close();
//		} catch (Exception e) {
////			throw new RuntimeException();
//			e.printStackTrace();
//		}
//		JSONObject resultObject = new JSONObject();
//		resultObject.put("total", count);
//		resultObject.put("root", resultJArray);
//
//		return resultObject.toString();
//	}
//
//	private String lightStr(Query q,Analyzer a,String fieldName,String txt) throws Exception{
//		//highlight
//		String str = null;
//		QueryScorer scorel = new QueryScorer(q);//可传评分
//		SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<font style=\"color:red\">", "</font>");
//		Highlighter highlighter = new Highlighter(fors,scorel);
//		Fragmenter frag = new SimpleSpanFragmenter(scorel,1000);
//		highlighter.setTextFragmenter(frag);
//		str = highlighter.getBestFragment(a, fieldName,txt);
//		if(str==null){
//			return txt;
//		}
//		return str;
//	}
//
//
//}
