package com.qaqa.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qaqa.crud.bean.Employee;
import com.qaqa.crud.bean.Msg;
import com.qaqa.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//处理员工CRUD请求

//员工保存
//1. 支持JSR303校验
//2. 导入Hibernate-Validator


@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //单个批量二合一
    //批量删除1-2-3
    @ResponseBody
    @RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("ids") String ids) {
        if (ids.contains("-")) {
            String[] str_ids = ids.split("-");
            List<Integer> list = new ArrayList<>();
            for (String id : str_ids) {
                list.add(Integer.parseInt(id));
            }
            employeeService.deleteBatch(list);
        }else{
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }

//    @ResponseBody
//    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
//    public Msg deleteEmpById(@PathVariable("id") Integer id) {
//        employeeService.deleteEmp(id);
//        return Msg.success();
//    }


    //员工更新
    //如果直接发送ajax=PUT的请求,封装的数据,除了id会全是null
    /**
     * 问题：
     *  请求体中有数据,但是Employee封装不上
     *  update tb1_emp where emp_id=1014;
     *
     * 原因:
     *  1. tomcat中将请求体中的数据,封装成一个map
     *  2. request.getParameter("empName")就会从这个map中取值
     *  3. SpringMVC封装POJO对象的时候
     *      会把POJO中每个属性的值拿到, request.getParameter("email");
     *
     * AJAX发送PUT请求引发的血案:
     *  PUT请求，请求体中的数据,request.getParameter("empName")拿不到
     *  Tomcat一看是PUT就不会封装请求体中的数据为map,只有POST形式的请求才封装请求体为map
     *
     * Tomcat源码对应的位置！！！
     *      org.apache.catalina.connector.Request--parseParameters();
     *
     * 解决方案:
     *  我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
     *  1. 配置上HttpPutFormContentFilter;
     *  2. 它的作用: 将请求体中的数据解析包装成一个map
     *  3. request被重新包装,request.getParameter()被重写,就会从自己封装的map中取数据
     *
     *
     */
    @ResponseBody
    @RequestMapping(value="/emp/{empId}", method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee) {
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    @ResponseBody
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp", employee);
    }

    //检查用户名是否可用
    @ResponseBody
    @RequestMapping("/checkUser")
    public Msg checkUser(@RequestParam("empName") String empName) {
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{3,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "用户名可以为2-5位中文或者3-16为英文字母和数字的组合");
        }

        //数据库用户名重复校验
        boolean b = employeeService.checkUser(empName);
        if(b){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg", "用户名不可用");
        }
    }

    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败,返回失败,在模态框之后能该显示校验失败的错误信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println("错误字段名: " + fieldError.getField());
                System.out.println("错误信息: " + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        }else{

            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    //ResponseBody自动把格式转为json
    //ResponseBody需要jackson的包
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        //这不是一个分页查询
        //引入PageHelper分页插件
        //在查询之前只需要调用, 传入页码, 以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的这个查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();

        //使用pageInfo包装查询后的结果,只需要将pageInfo交给页面就行了
        //封装了详细的分页信息,包括有查询出来的数据
        //传入连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", page);
    }


    //查询员工数据
//    @RequestMapping("/emps")
//    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
//
//
//        //这不是一个分页查询
//        //引入PageHelper分页插件
//        //在查询之前只需要调用, 传入页码, 以及每页的大小
//        PageHelper.startPage(pn, 5);
//        //startPage后面紧跟的这个查询就是一个分页查询
//        List<Employee> emps = employeeService.getAll();
//
//        //使用pageInfo包装查询后的结果,只需要将pageInfo交给页面就行了
//        //封装了详细的分页信息,包括有查询出来的数据
//        //传入连续显示的页数
//        PageInfo page = new PageInfo(emps, 5);
//        model.addAttribute("pageInfo", page);
//
//
//        return "/list";
//    }
}




