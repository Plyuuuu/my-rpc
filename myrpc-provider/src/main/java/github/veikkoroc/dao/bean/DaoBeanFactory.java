package github.veikkoroc.dao.bean;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

/**
 * 获取 Dao 对应的对象
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/12 9:54
 */
@Repository
public class DaoBeanFactory<T> {

    private static SqlSessionFactory sqlSessionFactory;
    /**
     * 初始化sqlSessionFactory
     */
    static {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis/config/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
            resourceAsStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Dao 接口对应的对象
     * @param clazz
     * @return
     */
    public T getDaoBean(Class<T> clazz) {
        T daoBean = null;
        SqlSession sqlSession = null;
        try{
            // true 默认提交事务
            sqlSession = sqlSessionFactory.openSession(true);
            daoBean = sqlSession.getMapper(clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return daoBean;
    }
}
