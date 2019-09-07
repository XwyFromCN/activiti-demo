package cn.guolf.activiti.Utills;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

/**
 * @Auther: xwy
 * @Date: 2019/9/5 17:37
 * @Description: 引擎工具类
 */
public class ProcessUtills {
    //创建流程引擎
    public static ProcessEngine newEngine(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("xx").setJdbcPassword("xxx")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        return processEngine;
    }
    public static RepositoryService repositoryService(){
        return newEngine().getRepositoryService();
    }
    public static IdentityService identityService(){
        return newEngine().getIdentityService();
    }
    public static RuntimeService runtimeService(){
        return newEngine().getRuntimeService();
    }
    public static TaskService taskService(){
        return newEngine().getTaskService();
    }
}
