package cn.guolf.activiti.ParallelProcess;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.*;

/**
 * @Auther: xwy
 * @Date: 2019/9/5 16:02
 * @Description: 并行任务,并行节点设置了完成条件
 */
public class Parallel1 {
    //创建流程引擎
    public ProcessEngine newEngine(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl").setJdbcUsername("DG").setJdbcPassword("Compass!23$.")
                .setJdbcDriver("oracle.jdbc.driver.OracleDriver")
                .setJobExecutorActivate(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        return processEngine;
    }
    public RepositoryService repositoryService(){
        return newEngine().getRepositoryService();
    }
    public IdentityService identityService(){
        return newEngine().getIdentityService();
    }
    public RuntimeService runtimeService(){
        return newEngine().getRuntimeService();
    }
    public TaskService taskService(){
        return newEngine().getTaskService();
    }

    /**
     * 发布
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = repositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("Parallel1.bpmn")
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println("流程名称 ： [" + processDefinition.getName() + "]， 流程ID ： ["
                + processDefinition.getId() + "], 流程KEY : " + processDefinition.getKey());
    }

    @Test
    public void start(){
// 设置流程创建人
        identityService().setAuthenticatedUserId("xwy");

        // 收文登记，启动流程
        ProcessInstance processInstance = runtimeService()
                .startProcessInstanceByKey("myProcess_1", "Key001");

        System.out.println("流程实例ID = " + processInstance.getId());
        System.out.println("正在活动的流程节点ID = " + processInstance.getActivityId());
        System.out.println("流程定义ID = " + processInstance.getProcessDefinitionId());
    }

    @Test
    public void complete(){
        List<Task> taskList = taskService().createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        List<String> userList = Arrays.asList("xwy","yh","pp");
        Map vars = new HashMap();
        vars.put("userList",userList);
        taskService().complete(taskList.get(0).getId(),vars);
    }

    @Test
    public void completeOne(){
//        List<Task> taskList = taskService().createTaskQuery()
//                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
//        List<Task> taskList = taskService().createTaskQuery()
//                .taskAssignee("yh").orderByTaskCreateTime().desc().list();
        List<Task> taskList = taskService().createTaskQuery()
                .taskAssignee("pp").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        taskService().complete(taskList.get(0).getId());
    }

}
