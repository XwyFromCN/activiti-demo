package cn.guolf.activiti.activitiinactioncodes;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xwy
 * @Date: 2019/9/7 16:52
 * @Description:
 */
public class LeaveCoutersignTest extends AbstractActivitiTestCase {
    /**
     * 全部通过
     */
    @Test
    @Deployment(resources = {"activitiinactioncodes/leave-countersign.bpmn"})
    public void testAllApproved() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> users = Arrays.asList("groupLeader", "deptLeader", "hr");
        variables.put("users", users);
        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-countersign", variables);
        for (String user : users) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(user).singleResult();
            Map<String, Object> taskVariables = new HashMap<String, Object>();
            taskVariables.put("approved", "true");
            taskService.complete(task.getId(), taskVariables);
        }

        Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertNotNull(task);
        assertEquals("销假", task.getName());
    }

    /**
     * 部分通过
     */
    @Test
    @Deployment(resources = {"activitiinactioncodes/leave-countersign.bpmn"})
    public void testNotAllApproved() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> users = Arrays.asList("groupLeader", "deptLeader", "hr");
        variables.put("users", users);
        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-countersign", variables);
        boolean flag = true;
        for (String user : users) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(user).singleResult();
            Map<String, Object> taskVariables = new HashMap<String, Object>();
            if(flag){
                taskVariables.put("approved", "false");
                flag = false;
            } else{
                taskVariables.put("approved", "true");
            }
            taskService.complete(task.getId(), taskVariables);
        }

        Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        log.info("task==>"+task.toString());
        assertNotNull(task);
        assertEquals("调整申请", task.getName());

        Task task2 = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("reApply", "true");
        taskService.complete(task2.getId(), taskVariables);
        for (String user : users) {
            Task t = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(user).singleResult();
            Map<String, Object> taskVar = new HashMap<String, Object>();
            taskVar.put("approved", "false");
            taskService.complete(t.getId(), taskVar);
        }
        Task t = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        log.info("task==>"+t.toString());
        assertNotNull(t);
        assertEquals("调整申请", t.getName());
    }

    protected void initializeProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("DG").setJdbcPassword("Compass!23$.")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        this.processEngine = processEngine;
    }
}
