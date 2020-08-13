package com.base;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName UserInfoTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/6/13
 * @Version 1.0
 */
@WebAppConfiguration
@Transactional //支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest
//配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
public class ControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeClass
    public static void init() {
        System.setProperty("spring.profiles.active", "dev");
        System.setProperty("BLINDBOX-HOME-CONFIG", "D:\\temp\\swoa2_config\\blindbox\\");
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /*******************************
     * GoodsOrderController
     * @throws Exception
     */

    // grid
    @Test
    public void orderGridTest() throws Exception {

//        GoodsOrderQuery query = new GoodsOrderQuery();
//        String result = mockMvc.perform(
//                get("/order/grid")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(FastJsonUtil.bean2JsonStr(query))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串

//        System.out.println(">>>>>>>>>> orderGridTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 发货
    @Test
    public void orderShipTest() throws Exception {

//        GoodsOrder order1 = new GoodsOrder()
//                .setOrderId(1)
//                .setOrderType(OrderType.GOODS.code())
//                .setOrderNo("123456789")
//                .setOrderStatus(OrderStatus.RECEIVE.status());
//        List<GoodsOrder> GoodsOrders =  Lists.newArrayList(order1);
//        String result = mockMvc.perform(
//                put("/order/ship")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(GoodsOrders))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//
//        System.out.println(">>>>>>>>>> orderShipTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 添加备注
    @Test
    public void orderOrderRemarkTest() throws Exception {

//        GoodsOrder order1 = new GoodsOrder()
//                .setOrderId(1)
//                .setOrderNo("bbbbbbb")
//                .setOrderStatus(OrderStatus.TRADE_SUCCESS.status())
//                .setOrderType(OrderType.GOODS.code())
//                .setRemark("test");
//        GoodsOrder order2 = new GoodsOrder()
//                .setOrderId(2)
//                .setOrderNo("aaaaaaaaa")
//                .setOrderStatus(OrderStatus.TRADE_SUCCESS.status())
//                .setOrderType(OrderType.GOODS.code())
//                .setRemark("test");
//        List<GoodsOrder> GoodsOrders =  Lists.newArrayList(order1,order2);
//        String result = mockMvc.perform(
//                put("/order/orderRemark")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(GoodsOrders))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andExpect(jsonPath("$.code").value(0))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//
//        System.out.println(">>>>>>>>>> orderOrderRemarkTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 修改地址
    @Test
    public void orderChangeAddressTest() throws Exception {

//        GoodsOrder order1 = new GoodsOrder()
//                .setOrderId(1)
//                .setOrderType(OrderType.GOODS.code())
//                .setAddressInfo("test");
//        GoodsOrder order2 = new GoodsOrder()
//                .setOrderId(2)
//                .setOrderType(OrderType.GOODS.code())
//                .setAddressInfo("test");
//        List<GoodsOrder> GoodsOrders =  Lists.newArrayList(order1,order2);
//        String result = mockMvc.perform(
//                put("/order/changeAddress")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(GoodsOrders))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderChangeAddressTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 修改物流
    @Test
    public void orderChangeLogisticsTest() throws Exception {

//        GoodsOrder order1 = new GoodsOrder()
//                .setOrderId(1)
//                .setOrderType(OrderType.GOODS.code())
//                .setLogisticsNo("test");
//        GoodsOrder order2 = new GoodsOrder()
//                .setOrderId(2)
//                .setOrderType(OrderType.GOODS.code())
//                .setLogisticsNo("test");
//        List<GoodsOrder> GoodsOrders =  Lists.newArrayList(order1,order2);
//        String result = mockMvc.perform(
//                put("/order/changeLogistics")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(GoodsOrders))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderChangeLogisticsTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 确认售后
    @Test
    public void orderAsFinishTest() throws Exception {

//        AfterSale obj1 = new AfterSale()
//                .setOrderNo("111111")
//                .setActionStatus("1")
//                .setId(1);
//        List<AfterSale> param =  Lists.newArrayList(obj1);
//        String result = mockMvc.perform(
//                put("/order/asFinish")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderAsFinishTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 拒绝售后
    @Test
    public void orderAfterSaleTest() throws Exception {

//        AfterSale obj1 = new AfterSale()
//                .setOrderNo("111111")
//                .setActionStatus("1")
//                .setId(1);
//        List<AfterSale> param =  Lists.newArrayList(obj1);
//        String result = mockMvc.perform(
//                put("/order/afterSale")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderAfterSaleTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 退款余额
    @Test
    public void orderBalandTest() throws Exception {

//        AfterSale obj1 = new AfterSale()
//                .setOrderNo("111111")
//                .setActionStatus("1")
//                .setId(1);
//        List<AfterSale> param =  Lists.newArrayList(obj1);
//        String result = mockMvc.perform(
//                put("/order/baland")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderBalandTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 退款weixin
    @Test
    public void orderWeiXinTest() throws Exception {

//        AfterSale obj1 = new AfterSale()
//                .setOrderNo("111111")
//                .setActionStatus("1")
//                .setId(1);
//        List<AfterSale> param =  Lists.newArrayList(obj1);
//        String result = mockMvc.perform(
//                put("/order/weixin")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderWeiXinTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 下载
    @Test
    public void orderDownloadTest() throws Exception {

//        GoodsOrderQuery query = new GoodsOrderQuery();
//        String result = mockMvc.perform(
//                post("/order/download.json")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(query))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> orderDownloadTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }


    /*****************************************************************
     * 库存
     *****************************/
    // 系列集合 /series /list
    @Test
    public void seriesListTest() throws Exception {

//        SeriesQuery obj1 = new SeriesQuery();
//        String result = mockMvc.perform(
//                post("/series/list")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(obj1))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> seriesListTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品表格
    @Test
    public void goodsGridTest() throws Exception {

//        GoodsQuery obj1 = new GoodsQuery();
//        String result = mockMvc.perform(
//                post("/goods/grid")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(obj1))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(">>>>>>>>>> goodsGridTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品集合
    @Test
    public void goodsListTest() throws Exception {

//        GoodsQuery obj1 = new GoodsQuery();
//        String result = mockMvc.perform(
//                post("/goods/list")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(obj1))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(">>>>>>>>>> goodsListTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品新增
    @Test
    public void goodsSaveTest() throws Exception {

//        Goods goods = new Goods()
//                .setGoodsCode("00000")
//                .setGoodsName("00000");
//        Series series = new Series()
//                .setSeriesId(1)
//                .setSeriesCode("00000");
//        Inventory param = new Inventory()
//                .setGoods(goods)
//                .setSeries(series);
//        String result = mockMvc.perform(
//                post("/goods/save")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> goodsSaveTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品更新
    @Test
    public void goodsUpdateTest() throws Exception {

//        Goods goods = new Goods()
//                .setGoodsId(1)
//                .setGoodsCode("111111")
//                .setGoodsName("11111");
//        Series series = new Series()
//                .setSeriesId(2)
//                .setSeriesCode("11111");
//        Inventory param = new Inventory()
//                .setGoods(goods)
//                .setSeries(series);
//        String result = mockMvc.perform(
//                put("/goods/update")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(param))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> goodsUpdateTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品删除
    @Test
    public void goodsDelTest() throws Exception {

//        String result = mockMvc.perform(
//                delete("/goods/del/1")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        ).andExpect(status().isOk())//返回的状态是200
//                .andExpect(jsonPath("$.isError").value(false))
//                .andReturn().getResponse().getContentAsString();  //将相应的数据转换为字符串
//        System.out.println(">>>>>>>>>> goodsUpdateTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }

    // 商品下载
    @Test
    public void goodsDownloadTest() throws Exception {

//        GoodsQuery obj1 = new GoodsQuery();
//        String result = mockMvc.perform(
//                post("/goods/download")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(obj1))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();
    }

    // 商品同步
    @Test
    public void goodsSyncTest() throws Exception {

//        GoodsQuery obj1 = new GoodsQuery();
//        String result = mockMvc.perform(
//                post("/goods/sync")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(FastJsonUtil.bean2JsonStr(obj1))
//        ).andExpect(status().isOk())//返回的状态是200
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(">>>>>>>>>> goodsSyncTest >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"+result);
    }


}
