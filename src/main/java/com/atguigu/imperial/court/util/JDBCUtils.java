package com.atguigu.imperial.court.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**1.从数据源获取 数据库连接*/
/*2.从数据库获取到数据库连接后，绑定到 本地线程（借助ThreadLocal）*/
/**3.释放线程时，和本地线程解除绑定*/
public class JDBCUtils {

    //数据源 成员变量设置为静态资源，保证大对象的单例性,static保证静态方法可以访问
    private static DataSource datasource;

    //由于ThreadLocal对象需要作为绑定数据时k-v对 中的key，所以要保证唯一性
    //加static 声明为静态资源即可保证唯一性
    private static ThreadLocal<Connection> threadLocal=new ThreadLocal<>();

    //在静态代码块中 初始化数据源
    static{
        /**操作思路分析*/
        //从jdbc.properties文件中读取连接数据库的信息
        /*为了保证程序代码的可移植性，需要基于一个确定的基准来读取这个文件*/
        /**确定的基准，类路径的根目录，resources目录下的内容经过 构建操作中的打包操作后，会确定放在WEB-INF/classes目录下*/
        // WEB-INF/classes目录存放编译好的 *.class 字节码文件，所以这个目录称之为 类路径
        /**类路径，无论是在本地运行，还是在服务器端运行都是一个确定的基准*/
        /*具体代码*/
        //1.创建一个用于存储外部属性文件信息的Properties对象
        Properties properties=new Properties();
        //2.使用当前类的类加载器加载外部属性文件：jdbc.properties,从类路径根目录下
        ClassLoader classLoader = JDBCUtils.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream("jdbc.properties");
        try{
            //3.使用Properties类封装属性文件中的数据
            properties.load(stream);
            //4.根据Properties对象（已封装了数据库连接信息）来创建数据源对象
            datasource= DruidDataSourceFactory.createDataSource(properties);
        }catch(Exception e) {
            e.printStackTrace();
            //为了避免在真正抛出异常后，catch块捕获到异常从而掩盖问题
            /**这里将所捕获到的异常封装为运行时异常继续抛出*/

            throw new RuntimeException(e);

        }

    }

    /*1.工具方法：获取数据库连接并返回*/
    public static Connection getConnection(){

        Connection connection=null;

        try {
            //1.尝试从当前线程检查是否存在已经绑定的Connection 对象
            connection = threadLocal.get();
            //2.检查Connection对象是否为null
            if(connection==null){
                //3.如果为null，则从 数据源获取 数据库连接
                connection=datasource.getConnection();
                //4.获取到数据库连接后绑定到当前线程
                threadLocal.set(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            /*为了调用工具方法方便，编译时异常 不往外抛
            * 为了不掩盖问题，捕获到的编译时异常封装为 运行时异常*/
            throw new RuntimeException(e);
        }


        return connection;
    }


    /*2.释放数据库连接*/
    public static void releaseConnection(Connection connection){
        if(connection!=null){
            try {
                //在数据库连接池中将当前连接对象标记为空闲
                connection.close();

                //将当前数据库连接从当前线程上移除
                threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }



}














