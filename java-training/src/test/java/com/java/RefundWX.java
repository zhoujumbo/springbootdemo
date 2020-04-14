package com.java;

import com.alibaba.fastjson.JSONObject;
import com.customer.basic.support.commons.business.http.HttpUtil;
import com.customer.basic.support.commons.business.logger.LogUtil;
import com.customer.basic.support.commons.business.util.DateUtil;
import com.customer.blindbox.core.refund.util.RefundUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RefundWX {

    @Test
    public void testRefundHttp1() throws IOException {

        String service = "refund";
        String partner = "900747";
        String name  = new String("chinajoy_refund".getBytes("iso-8859-1"),"utf-8");
        String money  =  RefundUtil.formatAmount(new BigDecimal("0.5"));
        String serverUrl  = "http://test.chinajoy.net/refund/callback";
        String orderNo  = "156558157700000008";//156506636400000010
        String time = DateUtil.ymdhmsFormatEasy();
        String md5key = "0020206964449433";

        Map<String, String[]> paramMap2 = new HashMap<>();
        paramMap2.put("service",new String[]{service});
        paramMap2.put("partner",new String[]{partner});
        paramMap2.put("name",new String[]{name});
        paramMap2.put("money",new String[]{money});
        paramMap2.put("serverUrl",new String[]{serverUrl});
        paramMap2.put("orderNo",new String[]{orderNo});
        paramMap2.put("time",new String[]{time});

        String sign = "";
        try {
            sign = RefundUtil.paramSign2(paramMap2, md5key);
        } catch (Exception e) {
            LogUtil.error("sign生成失败",e);
        }
        LogUtil.info(sign);
        LogUtil.info(time);

        Request request = Request.Post("https://pay.kedou.com/gateway.do");
        request.setHeader("Accept", "application/json");
        request.bodyForm(
                new BasicNameValuePair("service",service),
                new BasicNameValuePair("partner",partner),
                new BasicNameValuePair("name",name),
                new BasicNameValuePair("money",money),
                new BasicNameValuePair("serverUrl",serverUrl),
                new BasicNameValuePair("orderNo",orderNo),
                new BasicNameValuePair("time",time),
                new BasicNameValuePair("sign",sign)
        );
        Response response = null;
        try {
            response = request.execute();
        } catch (IOException e) {
            LogUtil.info("退款请求异常",e.getStackTrace());
            throw new RuntimeException();
        }
        //{"result":{"msg":"退款请求已接受","code":0},"refundState":"1","refundNo":"TK201908061336590343287","refundMoney":"0.50"}
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(response.returnContent().asString());
            JSONObject result = json.getJSONObject("result");

            LogUtil.info("退款信息 json" + json);
            if(result!=null && result.getString("code").equals("0")){
                if(StringUtils.isNotEmpty(json.getString("refundNo"))
                        && StringUtils.isNotEmpty(json.getString("refundState"))){
                    LogUtil.info("");
                }else{
                    LogUtil.warn("退款信息 msg" +json.get("msg"));
                }
            }else{
                LogUtil.error("微信退款请求失败:code:{}|msg:{}",json.getInteger("code"),json.getInteger("msg"));
                LogUtil.error("微信退款请求失败:refundNo:{}|refundMoney:{}",json.getInteger("refundNo"),json.getInteger("refundMoney"));
                LogUtil.error("微信退款请求失败:refundState:{}",json.getInteger("refundState"));
            }
        } catch (IOException e) {
            LogUtil.info("退款请求异常","IOException",e.getStackTrace());
        }

//退款信息 json{"result":{"msg":"退款请求已接受","code":0},"refundState":"1","refundNo":"TK201908080956237413738","refundMoney":"0.50"}

    }
    @Test
    public void testSearchRefundHttp() throws IOException {
        String service = "refund_order_query";
        String partner = "900747";
        String orderNo  = "TK201908080956237413738";
        String time = DateUtil.ymdhmsFormatEasy();
        String md5key = "0020206964449433";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("service",service);
        paramMap.put("partner",partner);
        paramMap.put("orderNo",orderNo);
        paramMap.put("time",time);

        String sign = "";
        try {
            sign = RefundUtil.paramSign(paramMap, md5key);
        } catch (Exception e) {
            LogUtil.error("sign生成失败",e);
        }
        LogUtil.info(sign);
        LogUtil.info(time);

        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("service",service);
        urlParam.put("partner",partner);
        urlParam.put("orderNo",orderNo);
        urlParam.put("time",time);
        urlParam.put("sign",sign);


        String url = HttpUtil.buildGetParam("https://pay.kedou.com/gateway.do",urlParam);
        Request request = Request.Get(url);
        request.setHeader("Accept", "application/json");
        Response response = null;
        try {
            response = request.execute();
        } catch (IOException e) {
            LogUtil.info("退款请求异常",e.getStackTrace());
            throw new RuntimeException();
        }
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(response.returnContent().asString());
            LogUtil.info("退款信息 json" + json);
        } catch (IOException e) {
            LogUtil.info("退款请求异常","IOException",e.getStackTrace());
        }



    }



}
