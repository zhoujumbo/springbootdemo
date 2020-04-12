package com.jum.utils.lucene;

import javax.management.Query;

public class AbstractIndexManage<E> implements IndexManage<E> {


    @Override
    public void creatIndex() throws Exception {

    }

    @Override
    public void booleanQuery() {

    }

    @Override
    public void multiFieldQueryParser() throws Exception {

    }

    @Override
    public String doSearch(Query query) {
        return null;
    }

    @Override
    public void updateIndex(E e) throws Exception {

    }

    @Override
    public void deleteIndex(String uuid) throws Exception {

    }
}
