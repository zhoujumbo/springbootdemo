package com.pattern.decorator.passport;

import com.pattern.decorator.passport.old.SigninService;
import com.pattern.decorator.passport.upgrade.ISiginForThirdService;
import com.pattern.decorator.passport.upgrade.SiginForThirdService;

/**
 * Created by Tom on 2019/3/17.
 */
public class DecoratorTest {

    public static void main(String[] args) {

        //满足一个is-a
        ISiginForThirdService siginForThirdService = new SiginForThirdService(new SigninService());
        siginForThirdService.loginForQQ("sdfasfdasfsf");

    }


}
