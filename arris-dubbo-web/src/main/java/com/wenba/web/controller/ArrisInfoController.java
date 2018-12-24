package com.wenba.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wenba.api.ArrisInfoServiceFacade;
import com.wenba.common.domain.ArrisInfo;
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
public class ArrisInfoController {

    @Reference(
            application = "${dubbo.application.id}",
            group = "${dubbo.consumer.group}"
    )

    private ArrisInfoServiceFacade arrisServiceFacade;


    /**
     *  查询表达式列表
     */
    @RequestMapping(value = "/selArrisInfoList", method = RequestMethod.GET)
    public PageResult<ArrisInfo> selArrisInfoList(ArrisInfo ai){

        log.info("selArrisInfoList 输入参数: " + ai.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        PageResult<ArrisInfo> dataResult = new PageResult<>();
        dataResult.setPage(ai.getPageNumber());
        ai.setPageNumber(ai.getPageNumber() - 1);
        dataResult.setSize(ai.getPageSize());

        //获取表达式列表总记录数量
        int total = arrisServiceFacade.selArrisInfoListNum(ai);

        dataResult.setTotal(new Long((long)total));

        //查询表达式列表
        List<ArrisInfo> resultList = arrisServiceFacade.selArrisInfoList(ai);

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
        log.info("selArrisInfoList 输出参数: " + resultList.toString());
        log.info("selArrisInfoList cost time:{} ms", endTime - startTime);
        return dataResult;
    }



    /**
     *  新增表达式
     */
    @RequestMapping(value = "/insArrisInfoList", method = RequestMethod.POST)
    public DataResult insArrisInfoList(@RequestBody List<ArrisInfo> aiList){

        log.info("insArrisInfoList 输入参数: " + aiList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<String,BaseResult>>> result = new DataResult<>();
        List<Map<String,BaseResult>> list = new ArrayList<>();
        Map<String,BaseResult> map = null;

        if(null != aiList && aiList.size() > 0) {
            for(ArrisInfo ai : aiList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                //表达式的当前发布版本默认从1开始
                ai.setArrisVers(1);
                //检验表达式版本和表达式名称
                int i = arrisServiceFacade.checkArrisVN(ai);
                if(i == 0) {
                    //新增表达式
                    int j = arrisServiceFacade.insArrisInfoList(ai);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
                        list.add(map);
                    }
                }else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式版本和名称已存在");
                    map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
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
        log.info("insArrisInfoList 输出参数: " + result.toString());
        log.info("insArrisInfoList cost time:{} ms", endTime - startTime);
        return result;
    }


    /**
     *  更新表达式
     */
    @RequestMapping(value = "/updArrisInfoList", method = RequestMethod.POST)
    public DataResult updArrisInfoList(@RequestBody List<ArrisInfo> aiList){

        log.info("updArrisInfoList 输入参数: " + aiList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<String,BaseResult>>> result = new DataResult<>();
        List<Map<String,BaseResult>> list = new ArrayList<>();
        Map<String,BaseResult> map = null;

        if(null != aiList && aiList.size() > 0) {
            for(ArrisInfo ai : aiList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                //检验表达式版本和表达式名称
                int i = arrisServiceFacade.checkArrisID(ai.getId());
                if(i > 0) {
                    //更新表达式
                    int j = arrisServiceFacade.updArrisInfoList(ai);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
                        list.add(map);
                    }
                }else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式版本和名称不存在");
                    map.put(ai.getArrisName() + "-" + ai.getArrisVers(),bResult);
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
        log.info("updArrisInfoList 输出参数: " + result.toString());
        log.info("updArrisInfoList cost time:{} ms", endTime - startTime);
        return result;
    }


    /**
     *  删除表达式
     */
    @RequestMapping(value = "/delArrisInfoList", method = RequestMethod.POST)
    public DataResult delArrisInfoList(@RequestBody List<ArrisInfo> aiList){

        log.info("delArrisInfoList 输入参数: " + aiList.toString());
        long startTime = System.currentTimeMillis();

        //返回数据
        DataResult<List<Map<Integer,BaseResult>>> result = new DataResult<>();
        List<Map<Integer,BaseResult>> list = new ArrayList<>();
        Map<Integer,BaseResult> map = null;

        if(null != aiList && aiList.size() > 0) {
            for(ArrisInfo ai : aiList) {
                BaseResult bResult = new BaseResult();
                map = new HashMap<>();
                //检验表达式版本和表达式名称
                int i = arrisServiceFacade.checkArrisID(ai.getId());
                if(i > 0) {
                    //删除表达式
                    int j = arrisServiceFacade.delArrisInfoList(ai);
                    if(j > 0) {
                        bResult.setCode(1);
                        bResult.setMsg("success");
                        map.put(ai.getId(),bResult);
                        list.add(map);
                    }else{
                        bResult.setCode(-1);
                        bResult.setMsg("fail");
                        map.put(ai.getId(),bResult);
                        list.add(map);
                    }
                }else{
                    bResult.setCode(-1);
                    bResult.setMsg("该表达式版本和名称不存在");
                    map.put(ai.getId(),bResult);
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
        log.info("delArrisInfoList 输出参数: " + result.toString());
        log.info("delArrisInfoList cost time:{} ms", endTime - startTime);
        return result;
    }


}
