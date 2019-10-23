package com.guli.ucenter.controller.api;

import com.google.gson.Gson;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExceptionUtil;
import com.guli.common.vo.R;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.util.ConstantPropertiesUtil;
import com.guli.ucenter.util.HttpClientUtils;
import com.guli.ucenter.util.JwtUtils;
import com.guli.ucenter.vo.LoginInfoVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author Jay
 * @create 2019-10-16 16:36
 */
@CrossOrigin
@Controller
@Slf4j
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private MemberService memberService;

    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        System.out.println("sessionId = " + session.getId());
        // 微信开放平台授权baseURL
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址，获取业务服务器重定向地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;

        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        // 自己的ngrok的前置域名
        String state = "imjay";
        session.setAttribute("wx-open-state", state);

        // 生成qrcodeUrl
        String qrCodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:" + qrCodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {
        System.out.println("------------微信授权服务器回调-----------------");
        System.out.println("code = " + code);
        System.out.println("state = " + state);

        String stateSession = (String) session.getAttribute("wx-open-state");
        System.out.println("stateSession = " + stateSession);

        if (StringUtils.isEmpty(state) || !state.equals(stateSession)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        // 使用code和appid以及appsecret换取access_token
        String baseAccesssTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAccesssTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        System.out.println("使用code换取的access_token的结果：" + result);

        Gson gson = new Gson();
        HashMap resultMap = gson.fromJson(result, HashMap.class);
        
        if (resultMap.get("errcode") != null) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = (String)resultMap.get("access_token");
        String openId = (String)resultMap.get("openid");

        System.out.println("access_token = " + accessToken);
        System.out.println("openid = " + openId);

        // 先根据openid进行数据库查询，如果没有查到用户信息,那么调用微信个人信息获取的接口
        // 如果查询到个人信息，那么直接进行登录
        Member member = memberService.getByOpenid(openId);
        // 没有查到member的话，即为新用户
        if(null == member) {
            System.out.println("***********新用户注册：************");

            // 使用access_token换取受保护的资源：微信的个人信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);

            // 使用HttpClientUtils发送请求，获取结果
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                log.error(ExceptionUtil.getMessage(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            System.out.println("使用access_token获取用户信息的结果 = " + resultUserInfo);

            HashMap resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            if (resultUserInfoMap.get("errcode") != null) {
                log.error("获取用户信息失败：" + (String)resultMap.get("errcode") + (String)resultMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            // 解析用户信息
            String nickname = (String) resultUserInfoMap.get("nickname");
            String headimgurl = (String) resultUserInfoMap.get("headimgurl");

            member = new Member();
            member.setOpenid(openId);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            memberService.save(member);

        }

        String guliJwtToken = JwtUtils.genJWT(member);

        return "redirect:http://localhost:3000?token=" + guliJwtToken;
    }

    @PostMapping("parse-jwt")
    @ResponseBody
    public R getLoginInfoByJwtToken(@RequestBody String jwtToken){

        try {
            Claims claims = JwtUtils.checkJwt(jwtToken);

            String id = (String)claims.get("id");
            String nickname = (String)claims.get("nickname");
            String avatar = (String)claims.get("avatar");

            LoginInfoVo loginInfoVo = new LoginInfoVo();
            loginInfoVo.setId(id);
            loginInfoVo.setAvatar(avatar);
            loginInfoVo.setNickname(nickname);

            return R.ok().data("loginInfo", loginInfoVo);
        }catch(SignatureException e) {
            // 如果token被篡改，就会被捕获signatrueException
            log.error("JwtToken解析失败"+e.getMessage());
            throw new GuliException(ResultCodeEnum.LOGIN_ERROR);
        }

    }
}
