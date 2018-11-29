package com.wenba.web.controller;


import com.wenba.common.base.BaseResultUtil;
import com.wenba.common.exception.AccessException;
import com.wenba.common.exception.ParameterException;
import com.wenba.common.utils.base.BaseResult;
import com.wenba.common.utils.base.DataResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 统一异常处理
 * 通过切面拦截异常
 * Created by imiiot on 2018/7/11.
 */
@ControllerAdvice
public class AllExceptionHandler {

    static final Logger log = LoggerFactory.getLogger(AllExceptionHandler.class);

    /**
     * 应用到所有@RequestMapping注解方法
     * 初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 应用到所有@RequestMapping注解方法
     * 为 Model 设置全局属性
     *
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "wenshuai.zhang");
    }

    /**
     * 参数异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ParameterException.class)
    public BaseResult requestParamExceptionHandler(ParameterException ex) {
        log.info(ex.toString());
        BaseResult result = new BaseResult();
        result.setCode(BaseResultUtil.PARAMETER_FAIL_CODE);
        result.setMsg(BaseResultUtil.PARAMETER_FAIL_MSG);
        return result;
    }

    /**
     * 访问权限异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AccessException.class)
    public BaseResult noAccessExceptionHandler(AccessException ex) {
        log.info(ex.toString());
        BaseResult result = new BaseResult();
        result.setCode(BaseResultUtil.ACCESS_FAIL_CODE);
        result.setMsg(BaseResultUtil.ACCESS_FAIL_MSG);
        return result;
    }

    /**
     * 全局异常捕捉处理
     * 返回json数据
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public DataResult<String> exceptionHandler(Exception ex) {
        log.info(ex.toString());
        DataResult<String> result = new DataResult<>();
        result.setCode(BaseResultUtil.FAIL_CODE);
        result.setMsg(BaseResultUtil.FAIL_MSG);
        result.setData(ex.getMessage());
        return result;
    }

//    /**
//     * 全局异常捕捉处理
//     * 返回页面
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView mvExceptionHandler(Exception ex) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("error");
//        modelAndView.addObject("code", BaseResultUtil.FAIL_CODE);
//        modelAndView.addObject("msg", BaseResultUtil.FAIL_MSG);
//        modelAndView.addObject("data", ex.getMessage());
//        return modelAndView;
//    }
}
