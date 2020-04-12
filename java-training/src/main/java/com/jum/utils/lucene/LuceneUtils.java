package com.jum.utils.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * lucene工具类
 */
public class LuceneUtils {

    private static final String INDEX_DIR = "indexdir";//索引文件存放目录
    private static Directory directory;//索引文件存放目录对象
    private static Analyzer analyzer;//分词器
    private static IndexWriter indexWriter;//索引写对象,单例
    private static IndexReader indexReader;//索引读对象


    static {
        try {
            //初始化索引文件存放目录对象
            directory = FSDirectory.open(Paths.get(INDEX_DIR));
            //初始化IK分词器
            analyzer = new IKAnalyzer();
            //初始化索引的写配置对象
//            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            //初始化索引的写对象
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            // 虚拟机退出时关闭
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("--------关闭IndexWriter中....");
                    try {
                        //关闭写对象
                        indexWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("--------关闭IndexWriter成功....");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取IndexWriter
     *
     * @return
     */
    public static IndexWriter getIndexWriter() {
        return indexWriter;
    }

    /**
     * 获取IndexReader，重用一些旧的 IndexReader
     *
     * @return
     * @throws Exception
     */
    public static IndexReader getIndexReader() throws Exception {
        if (indexReader == null) {
            indexReader = DirectoryReader.open(directory);
        } else {
            // 如果 IndexReader 不为空，就使用 DirectoryReader 打开一个索引变更过的 IndexReader 类
            //如果改变会返回一个新的reader，没有改变返回null
            IndexReader tr = DirectoryReader.openIfChanged((DirectoryReader) indexReader);
            if (tr != null) {
                //此时要记得把旧的索引Reader对象关闭
                indexReader.close();
                indexReader = tr;
            }
        }
        return indexReader;
    }

    /**
     * 获取IndexSearcher
     *
     * @return
     * @throws Exception
     */
    public static IndexSearcher getIndexSearcher() throws Exception {
        return new IndexSearcher(getIndexReader());
    }

    /**
     * 根据查询条件，在控制台打印符合条件的n个结果
     *
     * @param query
     * @param n
     * @throws Exception
     */
    public static void printTopDocsByQuery(Query query, int n) throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）
        TopDocs topDocs = indexSearcher.search(query, n);
        // 打印命中的总条数
        System.out.println("本次搜索共" + topDocs.totalHits + "条数据,最高分：" + topDocs.getMaxScore());

        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //循环
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            System.out.println("=========文档的编号是：" + docID);
            // 取出文档得分
            System.out.println("当前文档得分： " + scoreDoc.score);
            // 根据编号去找文档
//   Document document = indexReader.document(docID);//方法1
            Document document = indexSearcher.doc(docID);//方法2
            //获取文档的所有字段对象
            List<IndexableField> fieldList = document.getFields();
            //遍历列表
            for (IndexableField field : fieldList) {
                //打印出所有的字段的名字和值（必须存储了的）
                System.out.println(field.name() + ":" + field.stringValue());
            }

        }
    }

    public static void printTopDocsByQueryForHighlighter(Query query, int n) throws Exception {
        //=======高亮相关
        Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
        //得分对象
        Scorer scorer = new QueryScorer(query);
        //准备高亮工具
        Highlighter highlighter = new Highlighter(formatter, scorer);
        //=======搜索相关
        IndexSearcher indexSearcher = getIndexSearcher();
        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）
        TopDocs topDocs = indexSearcher.search(query, n);
        // 打印命中的总条数
        System.out.println("本次搜索共" + topDocs.totalHits + "条数据,最高分：" + topDocs.getMaxScore());

        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //循环
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            System.out.println("=========文档的编号是：" + docID);
            // 取出文档得分
            System.out.println("当前文档得分： " + scoreDoc.score);
            // 根据编号去找文档
//   Document document = indexReader.document(docID);//方法1
            Document document = indexSearcher.doc(docID);//方法2
            //获取文档的所有字段对象
            List<IndexableField> fieldList = document.getFields();
            //遍历列表
            for (IndexableField field : fieldList) {
                String highlighterValue = highlighter.getBestFragment(analyzer, field.name(), field.stringValue());

                //打印出所有的字段的名字和值（必须存储了的）
                System.out.println(field.name() + ":" + highlighterValue);
            }

        }
    }

    /**
     * 打印搜索结果文档
     *
     * @param topDocs
     * @param indexSearcher
     * @throws Exception
     */
    public static void printTopDocs(TopDocs topDocs, IndexSearcher indexSearcher) throws Exception {
        // 打印命中的总条数
        System.out.println("本次搜索共" + topDocs.totalHits + "条数据,最高分：" + topDocs.getMaxScore());

        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //循环
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            System.out.println("=========文档的编号是：" + docID);
            // 取出文档得分
            System.out.println("当前文档得分： " + scoreDoc.score);

            // 根据编号去找文档
//   Document document = indexReader.document(docID);//方法1
            Document document = indexSearcher.doc(docID);//方法2
            //获取文档的所有字段对象
            List<IndexableField> fieldList = document.getFields();
            //遍历列表
            for (IndexableField field : fieldList) {
                //打印出所有的字段的名字和值（必须存储了的）
                System.out.println(field.name() + ":" + field.stringValue());
            }

        }
    }
}
