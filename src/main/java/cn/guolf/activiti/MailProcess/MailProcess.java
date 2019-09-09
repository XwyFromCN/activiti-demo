package cn.guolf.activiti.MailProcess;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

/**
 * @Auther: xwy
 * @Date: 2019/9/9 17:23
 * @Description:
 */
public class MailProcess extends AbstractActivitiTestCase {
    protected void initializeProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("DG").setJdbcPassword("xxx")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setMailServerHost("smtp.163.com")
                .setMailServerPort(25)
                .setMailServerDefaultFrom("xxx")
                .setMailServerUsername("xxx")
                .setMailServerPassword("xxx")
                ;
        ProcessEngine processEngine = cfg.buildProcessEngine();
        this.processEngine = processEngine;
    }

    @Deployment(resources = "mail.bpmn")
    @Test
    public void testMail(){
        runtimeService.startProcessInstanceByKey("mail");
    }
}
