package com.example.listener;

import cn.dev33.satoken.listener.SaTokenListenerForSimple;
import cn.dev33.satoken.stp.SaLoginModel;
import org.springframework.stereotype.Component;

@Component
public class MySaTokenListener extends SaTokenListenerForSimple {
    /*
     * SaTokenListenerForSimple 对所有事件提供了空实现，通过继承此类，你只需重写一部分方法即可实现一个可用的侦听器。
     */

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        System.out.println("---------- 自定义侦听器实现 doLogin");
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doLogout");
    }

}

