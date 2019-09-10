package cn.guolf.activiti.TimerEvent;

import cn.guolf.activiti.Utills.ProcessUtills;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Auther: xwy
 * @Date: 2019/9/10 11:46
 * @Description:
 */
public class TestTimerEvent {
    @Test
    public void testDeploy() {
        // 流程部署
        org.activiti.engine.repository.Deployment deployment =
                ProcessUtills.repositoryService().createDeployment().addClasspathResource("TimerEvent.bpmn")
                        .deploy();
        ProcessDefinition processDefinition = ProcessUtills.repositoryService().createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println("流程名称 ： [" + processDefinition.getName() + "]， 流程ID ： ["
                + processDefinition.getId() + "], 流程KEY : " + processDefinition.getKey());
    }

    @Test
    public void start() {
        // 设置流程创建人
        ProcessUtills.identityService().setAuthenticatedUserId("kermit");
        // 收文登记，启动流程
        ProcessInstance processInstance = ProcessUtills.runtimeService()
                .startProcessInstanceByKey("TimerEvent", "Key001");
        System.out.println("流程实例ID = " + processInstance.getId());
        System.out.println("正在活动的流程节点ID = " + processInstance.getActivityId());
        System.out.println("流程定义ID = " + processInstance.getProcessDefinitionId());
    }

    @Test
    @Deployment(resources = "TimerEvent.bpmn")
    public void testTimerEvent() {
        ProcessUtills.identityService().setAuthenticatedUserId("kermit");
        ProcessInstance instance = ProcessUtills.runtimeService().startProcessInstanceByKey("ServiceTask");
        Task task = ProcessUtills.taskService().createTaskQuery().processInstanceId(instance.getId()).singleResult();
        System.out.println(task.toString());
        ProcessUtills.taskService().complete(task.getId());
        Task task2 = ProcessUtills.taskService().createTaskQuery().processInstanceId(instance.getId()).singleResult();
        assertEquals("任务2", task2.getName());
    }
}
