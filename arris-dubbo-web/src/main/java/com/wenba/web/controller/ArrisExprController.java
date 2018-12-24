package com.wenba.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wenba.api.ArrisExprServiceFacade;
import com.wenba.common.domain.ArrisExpr;
import com.wenba.common.utils.base.BaseResult;
import com.wenba.common.utils.base.DataResult;
import com.wenba.common.utils.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/config/arris")
public class ArrisExprController {

    @Reference(
            application = "${dubbo.application.id}",
            group = "${dubbo.consumer.group}"
    )

    private ArrisExprServiceFacade arrisExprServiceFacade;


    /**
     *  查询表达式详情列表
     */
    @RequestMapping(value = "/selArrisExprList", method = RequestMethod.GET)
    public PageResult<ArrisExpr> selArrisExprList(ArrisExpr ae){

        log.info("selArrisExprList 输入参数: " + ae.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        PageResult<ArrisExpr> dataResult = new PageResult<>();
        dataResult.setPage(ae.getPageNumber());
        ae.setPageNumber(ae.getPageNumber() - 1);
        dataResult.setSize(ae.getPageSize());

        //获取表达式详情列表总记录数量
        int total = arrisExprServiceFacade.selArrisExprListNum(ae);

        dataResult.setTotal(new Long((long)total));

        //查询表达式详情列表
        List<ArrisExpr> resultList = arrisExprServiceFacade.selArrisExprList(ae);

        if(null != resultList && resultList.size() > 0) {
            dataResult.setCode(1);
            dataResult.setMsg("success");
            dataResult.setData(resultList);
        }else {
            dataResult.setCode(1);
            dataResult.setMsg("未查询到结果");
            dataResult.setData(resultList);
        }

        long endTime = System.currentTimeMillis();
        log.info("selArrisExprList 输出参数: " + resultList.toString());
        log.info("selArrisExprList cost time:{} ms", endTime - startTime);
        return dataResult;
    }



    /**
     *  新增表达式详情
     */
    @RequestMapping(value = "/insArrisExprList", method = RequestMethod.POST)
    public DataResult insArrisExprList(@RequestBody List<ArrisExpr> aeList){

        log.info("insArrisExprList 输入参数: " + aeList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<Integer,BaseResult>>> result = new DataResult<>();
        List<Map<Integer,BaseResult>> list = new ArrayList<>();
        Map<Integer,BaseResult> map = null;

        if(null != aeList && aeList.size() > 0) {
            for(ArrisExpr ae : aeList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                /*//检验表达式详情版本
                int i = arrisExprServiceFacade.checkArrisVNExprV(ae);
                if(i == 0) {*/

                    //查询表达式详情版本
                    int v = arrisExprServiceFacade.selArrisExprV(ae);
                    ae.setExprVers(v + 1);
                    //新增表达式详情
                    int j = arrisExprServiceFacade.insArrisExprList(ae);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ae.getInfoId(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ae.getInfoId(),bResult);
                        list.add(map);
                    }
                /*}else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式详情版本已存在");
                    map.put(ae.getInfoId(),bResult);
                    list.add(map);
                }*/
            }
        }else {
            result.setCode(-4);
            result.setMsg("参数为空");
        }

        result.setCode(1);
        result.setMsg("success");
        result.setData(list);

        long endTime = System.currentTimeMillis();
        log.info("insArrisExprList 输出参数: " + result.toString());
        log.info("insArrisExprList cost time:{} ms", endTime - startTime);
        return result;
    }


    /**
     *  更新表达式详情
     */
    @RequestMapping(value = "/updArrisExprList", method = RequestMethod.POST)
    public DataResult updArrisExprList(@RequestBody List<ArrisExpr> aeList){

        log.info("updArrisExprList 输入参数: " + aeList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<Integer,BaseResult>>> result = new DataResult<>();
        List<Map<Integer,BaseResult>> list = new ArrayList<>();
        Map<Integer,BaseResult> map = null;

        if(null != aeList && aeList.size() > 0) {
            for(ArrisExpr ae : aeList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                //检验表达式版本详情
                int i = arrisExprServiceFacade.checkArrisExprID(ae.getId());
                if(i > 0) {
                    //更新表达式
                    int j = arrisExprServiceFacade.updArrisExprList(ae);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ae.getInfoId(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ae.getInfoId(),bResult);
                        list.add(map);
                    }
                }else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式详情版本不存在");
                    map.put(ae.getId(),bResult);
                    list.add(map);
                }
            }
        }else {
            result.setCode(-4);
            result.setMsg("参数为空");
        }

        result.setCode(1);
        result.setMsg("success");
        result.setData(list);

        long endTime = System.currentTimeMillis();
        log.info("updArrisExprList 输出参数: " + result.toString());
        log.info("updArrisExprList cost time:{} ms", endTime - startTime);
        return result;
    }


    /**
     *  删除表达式详情
     */
    @RequestMapping(value = "/delArrisExprList", method = RequestMethod.POST)
    public DataResult delArrisExprList(@RequestBody List<ArrisExpr> aeList){

        log.info("delArrisExprList 输入参数: " + aeList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<Integer,BaseResult>>> result = new DataResult<>();
        List<Map<Integer,BaseResult>> list = new ArrayList<>();
        Map<Integer,BaseResult> map = null;

        if(null != aeList && aeList.size() > 0) {
            for(ArrisExpr ae : aeList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                //检验表达式
                int i = arrisExprServiceFacade.checkArrisExprID(ae.getId());
                if(i > 0) {
                    //删除表达式
                    int j = arrisExprServiceFacade.delArrisExprList(ae);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ae.getId(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ae.getId(),bResult);
                        list.add(map);
                    }
                }else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式详情不存在");
                    map.put(ae.getId(),bResult);
                    list.add(map);
                }
            }
        }else {
            result.setCode(-4);
            result.setMsg("参数为空");
        }

        result.setCode(1);
        result.setMsg("success");
        result.setData(list);

        long endTime = System.currentTimeMillis();
        log.info("delArrisExprList 输出参数: " + result.toString());
        log.info("delArrisExprList cost time:{} ms", endTime - startTime);
        return result;
    }


    /**
     *  调用表达式详情
     */
    @RequestMapping(value = "/transArrisExprList", method = RequestMethod.POST)
    public DataResult transArrisExprList(@RequestBody List<Map<String,Object>> mapList){

        log.info("transArrisExprList 输入参数: " + mapList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<Integer, DataResult<String>>>> result = new DataResult<>();

        List<Map<Integer, DataResult<String>>> list = new ArrayList<>();

        Map<Integer, DataResult<String>> map = new HashMap<>();

        DataResult<String> dataResult = new DataResult<>();

        if(null != mapList && mapList.size() > 0) {
            int id = 0;
            int i = 0;
            for(Map<String,Object> m : mapList) {
                //校验表达式ID是否存在
                if(m.containsKey("ID")) {
                     id = Integer.parseInt(m.get("ID").toString());
                    //检验表达式详情是否存在
                    i = arrisExprServiceFacade.checkArrisExprID(id);
                    if(i > 0) {
                        //调用表达式详情
                        Map<Integer, DataResult<String>> dResult = arrisExprServiceFacade.transArrisExprList(m);
                        list.add(dResult);
                    }else{
                        dataResult.setCode(-1);
                        dataResult.setMsg("该表达式详情不存在");
                        map.put(id,dataResult);
                        list.add(map);
                    }
                }else {
                    dataResult.setCode(-1);
                    dataResult.setMsg("该表达式详情不存在!");
                    map.put(id,dataResult);
                    list.add(map);
                }
            }
        }else {
            result.setCode(-4);
            result.setMsg("参数为空");
        }

        result.setCode(1);
        result.setMsg("success");
        result.setData(list);

        long endTime = System.currentTimeMillis();
        log.info("transArrisExprList 输出参数: " + result.toString());
        log.info("transArrisExprList cost time:{} ms", endTime - startTime);
        return result;
    }

}
