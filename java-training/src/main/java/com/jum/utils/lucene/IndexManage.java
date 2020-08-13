package com.jum.utils.lucene;


import javax.management.Query;

public interface IndexManage<E> {

    void creatIndex() throws Exception;

    void booleanQuery() throws Exception;

    void multiFieldQueryParser() throws Exception;

    String doSearch(Query query) throws Exception;

    void updateIndex(E e) throws Exception;

    void deleteIndex(String uuid) throws Exception;

}	