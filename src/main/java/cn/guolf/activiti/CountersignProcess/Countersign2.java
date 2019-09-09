package cn.guolf.activiti.CountersignProcess;

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
 * @Date: 2019/9/5 18:28
 * @Description: 会签：多人会签，当超过1/2的人通过则通过流程,不超过则回退
 * 使用任务监听
 */
public class Countersign2 {
    @Test
    public void deploy() {
        RepositoryService repositoryService = ProcessUtills.repositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("Countersign_22.bpmn")
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
                .startProcessInstanceByKey("Countersign_22", "Key001");

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
    public void taskList() {
        Map<String, Object> variables = ProcessUtills.taskService().getVariables("185019");
        System.out.println(variables);
    }

    @Test
    public void completeOne() {
        ProcessUtills.identityService().setAuthenticatedUserId("xwy");

        // 收文登记，启动流程
        ProcessInstance processInstance = ProcessUtills.runtimeService()
                .startProcessInstanceByKey("Countersign_22", "Key001");

        List<Task> taskList = ProcessUtills.taskService().createTaskQuery()
                .taskAssignee("xwy").orderByTaskCreateTime().desc().list();
        System.out.println("taskList = " + taskList);
        //指定多实例节点的处理对象
        List<String> userList = Arrays.asList("xwy", "yh", "pp");
        Map vars = new HashMap();
        vars.put("userList", userList);
        ProcessUtills.taskService().complete(taskList.get(0).getId(), vars);

        String passflag = "yes";
        String taskId = "185011";
        String processId = "175001";
        Map var = new HashMap();

        List<Task> tasks = ProcessUtills.taskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
        boolean flag = true;
        for (Task task : tasks) {
//            if(flag){
//                flag = false;
//            }else{
//                passflag = "no";
//            }
            var.put("passflag", passflag);
            ProcessUtills.taskService().complete(task.getId(), var);
        }
    }
}
