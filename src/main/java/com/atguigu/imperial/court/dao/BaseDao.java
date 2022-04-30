package com.atguigu.imperial.court.dao;

import com.atguigu.imperial.court.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * BaseDao类，所有 Dao实现类的 基类
 * @param <T> 实体类的类型
 */
public class BaseDao<T> {

    //DBUtils 工具包提供的 数据库操作对象
    private QueryRunner runner=new QueryRunner();




    /**
     * 查询单个对象
     * @param sql 执行查询的sql语句
     * @param entityClass 实体类对应的Class对象
     * @param parameters 传给sql语句的参数
     * @return 查询到的实体类对象
     *
     */
    public T getSingleBean(String sql, Class<T> entityClass, Object ... parameters){
        try {
            //获取数据库连接
            Connection connection= JDBCUtils.getConnection();
            return runner.query(connection, sql, new BeanHandler<>(entityClass), parameters);
        } catch (SQLException e) {
            e.printStackTrace();
            //如果真的抛出异常，则将编译时异常 封装为 运行时异常抛出
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询多个对象
     * @param sql 执行查询的sql语句
     * @param entityClass 实体类对应的Class对象
     * @param parameters 传给sql语句的参数
     * @return 查询到的实体类对象
     *
     */
    public List<T> getBeanList(String sql, Class<T> entityClass, Object ... parameters){
        try {
            //获取数据库连接
            Connection connection= JDBCUtils.getConnection();
            return runner.query(connection, sql, new BeanListHandler<>(entityClass), parameters);
        } catch (SQLException e) {
            e.printStackTrace();
            //如果真的抛出异常，则将编译时异常 封装为 运行时异常抛出
            throw new RuntimeException(e);
        }
    }


    /**
     * 通用的增删改方法，insert，update，delete 操作都可以用这个方法
     * @param sql 执行操作的SQL语句
     * @param parameters SQL语句的参数
     * @return 受影响的行数
     */
    public int update(String sql, Object... parameters){
        try{
            Connection connection = JDBCUtils.getConnection();
            int affectedRowNumbers = runner.update(connection, sql, parameters);
            return affectedRowNumbers;
        }catch(SQLException e){
            e.printStackTrace();
            //编译时异常封装为 运行时异常 抛出
            throw new RuntimeException(e);
        }
    }


}
