package com.pattern.template;

import com.pattern.template.dao.MemberDao;

/**
 * Created by Tom.
 */
public class MemberDaoTest {

    public static void main(String[] args) {

        MemberDao memberDao = new MemberDao(null);
        memberDao.query();
    }
}
