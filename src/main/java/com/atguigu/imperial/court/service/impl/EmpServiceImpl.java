package com.atguigu.imperial.court.service.impl;

import com.atguigu.imperial.court.dao.api.EmpDao;
import com.atguigu.imperial.court.dao.impl.EmpDaoImpl;
import com.atguigu.imperial.court.entity.Emp;
import com.atguigu.imperial.court.exception.LoginFailedException;
import com.atguigu.imperial.court.service.api.EmpService;
import com.atguigu.imperial.court.util.ImperialCourtConst;
import com.atguigu.imperial.court.util.MD5Util;

public class EmpServiceImpl implements EmpService {
    private EmpDao empDao=new EmpDaoImpl();

    @Override
    public Emp getEmpByLoginAccount(String loginAccount, String loginPasssword) {
        //1.对密码执行加密
        String encodedLoginPassword= MD5Util.encode(loginPasssword);
        //2.根据账户和加密密码查询数据库
        Emp emp=empDao.selectEmpByLoginAccount(loginAccount,encodedLoginPassword);
        //3.检查Emp对象是否为null
        if(emp!=null){
            //3.1 不为null，返回Emp
            return emp;
        }else{
            //3.2 为null，抛出登陆失败异常
            throw new LoginFailedException(ImperialCourtConst.LOGIN_FAILED_MESSAGE);
        }
    }
}
