package com.atguigu.imperial.court.servlet.module;

import com.atguigu.imperial.court.entity.Emp;
import com.atguigu.imperial.court.exception.LoginFailedException;
import com.atguigu.imperial.court.service.api.EmpService;
import com.atguigu.imperial.court.service.impl.EmpServiceImpl;
import com.atguigu.imperial.court.servlet.base.ModelBaseServlet;
import com.atguigu.imperial.court.util.ImperialCourtConst;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends ModelBaseServlet {

    private EmpService empService=new EmpServiceImpl();

    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //1.获取请求参数
            String loginAccount=request.getParameter("loginAccount");
            String loginPasssword=request.getParameter("loginPassword");
            //2.调用EmpService方法执行登录逻辑
            Emp emp=empService.getEmpByLoginAccount(loginAccount,loginPasssword);
            //3.通过request获取HttpSession对象
            HttpSession session = request.getSession();
            //4.将查询到的Emp对象存入Session域
            session.setAttribute(ImperialCourtConst.LOGIN_EMP_ATTR_NAME,emp);
            //5.前往指定页面
            //临时的页面
//            String templateName="temp";
//            processTemplate(templateName,request,response);

            //前往正式的 目标地址
            response.sendRedirect(request.getContextPath()+"/work?method=showMemorialsDigestList");

        } catch (Exception e) {
            e.printStackTrace();
            //6.判断此处 捕获到的异常
            if(e instanceof LoginFailedException){
                //7.1 如果是登陆失败异常则跳转回 登陆页面, 将异常信息存入请求域
                request.setAttribute("message",e.getMessage());
                //7.2 处理视图： index
                processTemplate("index",request,response);
            }else {
                //8.如果不是登录异常则封装为运行时异常继续抛出
                throw new RuntimeException(e);
            }

        }
    }


    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.通过request对象获取HttpSession对象
        HttpSession session = request.getSession();
        //2.将HttpSession对象强制失效
        session.invalidate();
        //3.回到首页
        String templateName="index";
        processTemplate(templateName,request,response);












    }
}


























