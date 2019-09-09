package cn.guolf.activiti.CountersignProcess;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xwy
 * @Date: 2019/9/5 18:28
 * @Description: 会签：多人会签，当超过1/2的人通过则通过流程,不超过则回退
 * 使用任务监听
 */
public class Countersign3 extends AbstractActivitiTestCase {
    protected void initializeProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("DG").setJdbcPassword("Compass!23$.")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        this.processEngine = processEngine;
    }
    @Test
    public void deploy() {
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("Countersign_3.bpmn")
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println("流程名称 ： [" + processDefinition.getName() + "]， 流程ID ： ["
                + processDefinition.getId() + "], 流程KEY : " + processDefinition.getKey());
    }

    @Test
    public void start() {
// 设置流程创建人
        identityService.setAuthenticatedUserId("xwy");

        // 收文登记，启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("Countersign_3", "Key001");

        System.out.println("流程实例ID = " + processInstance.getId());
        System.out.println("正在活动的流程节点ID = " + processInstance.getActivityId());
        System.out.println("流程定义ID = " + processInstance.getProcessDefinitionId());
    }

    @Test
    public void complete() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        List<String> userList = Arrays.asList("xwy", "yh", "pp");
        Map vars = new HashMap();
        vars.put("userList", userList);
        taskService.complete(taskList.get(0).getId(), vars);
    }

    @Test
    public void taskList() {
        Map<String, Object> variables = taskService.getVariables("185019");
        System.out.println(variables);
    }

    @Test
    public void completeOne() {
        identityService.setAuthenticatedUserId("xwy");

        // 收文登记，启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("Countersign_3", "Key001");

        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        List<String> userList = Arrays.asList("xwy", "yh", "pp");
        Map vars = new HashMap();
        vars.put("userList", userList);
        taskService.complete(taskList.get(0).getId(), vars);

        String passflag = "no";
        Map var = new HashMap();

        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId()).list();
        boolean flag = true;
        for (Task task : tasks) {
//            if(flag){
//                flag = false;
//            }else{
//                passflag = "no";
//            }
            var.put("passflag", passflag);
            taskService.complete(task.getId(), var);
        }
    }
}
