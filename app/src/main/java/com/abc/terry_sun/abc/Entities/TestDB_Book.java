package com.abc.terry_sun.abc.Entities;

import com.orm.SugarRecord;

/**
 * Created by terry_sun on 2015/4/22.
 */
public class TestDB_Book extends SugarRecord<TestDB_Book> {
    String title;
    String edition;

    public TestDB_Book(){
    }

    public TestDB_Book(String title, String edition){
        this.title = title;
        this.edition = edition;
    }
}
