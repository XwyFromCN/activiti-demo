package cn.guolf.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

/**
 * @Auther: xwy
 * @Date: 2019/9/9 21:00
 * @Description:
 */
public class ServiceTaskTest extends AbstractActivitiTestCase {

    @Test
    @Deployment(resources = "ServiceTask.bpmn")
    public void testServiceTask(){
        identityService.setAuthenticatedUserId("kermit");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ServiceTask");
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        System.out.println(task.toString());
        taskService.complete(task.getId());
        Task task2 = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        assertEquals("任务2",task2.getName());
    }

    protected void initializeProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("xx").setJdbcPassword("xx")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        this.processEngine = processEngine;
    }
}
