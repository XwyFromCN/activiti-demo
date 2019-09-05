package cn.guolf.activiti.ParallelProcess;

import cn.guolf.activiti.Utills.ProcessUtills;
import org.activiti.engine.RepositoryService;
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
 * @Date: 2019/9/5 16:02
 * @Description: 并行任务, 并行节点设置了完成条件
 */
public class Parallel1 {

    /**
     * 发布
     */
    @Test
    public void deploy() {
        RepositoryService repositoryService = ProcessUtills.repositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("Parallel1.bpmn")
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println("流程名称 ： [" + processDefinition.getName() + "]， 流程ID ： ["
                + processDefinition.getId() + "], 流程KEY : " + processDefinition.getKey());
    }

    @Test
    public void start() {
// 设置流程创建人
        ProcessUtills.identityService().setAuthenticatedUserId("xwy");

        // 收文登记，启动流程
        ProcessInstance processInstance = ProcessUtills.runtimeService()
                .startProcessInstanceByKey("myProcess_1", "Key001");

        System.out.println("流程实例ID = " + processInstance.getId());
        System.out.println("正在活动的流程节点ID = " + processInstance.getActivityId());
        System.out.println("流程定义ID = " + processInstance.getProcessDefinitionId());
    }

    @Test
    public void complete() {
        List<Task> taskList = ProcessUtills.taskService().createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        List<String> userList = Arrays.asList("xwy", "yh", "pp");
        Map vars = new HashMap();
        vars.put("userList", userList);
        ProcessUtills.taskService().complete(taskList.get(0).getId(), vars);
    }

    @Test
    public void completeOne() {
        List<Task> taskList = ProcessUtills.taskService().createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
//        List<Task> taskList = taskService().createTaskQuery()
//                .taskAssignee("yh").orderByTaskCreateTime().desc().list();
//        List<Task> taskList = taskService().createTaskQuery()
//                .taskAssignee("pp").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        ProcessUtills.taskService().complete(taskList.get(0).getId());
    }

}
