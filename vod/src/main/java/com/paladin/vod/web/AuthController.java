package com.paladin.vod.web;

import com.paladin.framework.common.R;
import com.paladin.framework.jwt.TokenProvider;
import com.paladin.vod.config.WebSecurityManager;
import com.paladin.vod.web.dto.OpenToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author TontoZhou
 * @since 2020/8/10
 */
@Controller
@RequestMapping("/vod/auth")
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;

    @RequestMapping(value = "/jwt", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getJwt() {
        long expires = System.currentTimeMillis() + tokenProvider.getExpireMilliseconds();
        String jwtToken = tokenProvider.createJWT(WebSecurityManager.getCurrentUser(), null, new Date(expires));
        return R.success(new OpenToken(jwtToken, expires));
    }
}
