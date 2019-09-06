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
 */
public class Countersign {
    @Test
    public void deploy() {
        RepositoryService repositoryService = ProcessUtills.repositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("Countersign_2.bpmn")
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
                .startProcessInstanceByKey("Countersign_2", "Key001");

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
    public void taskList(){
        Map<String, Object> variables = ProcessUtills.taskService().getVariables("185019");
        System.out.println(variables);
    }

    @Test
    public void completeOne() {
        String passflag = "yes";
        String taskId = "185011";
        String processId = "175001";
        int passCount = 0;
        int totalCount = 0;
        //当前任务
        Task task = ProcessUtills.taskService().createTaskQuery().taskId(taskId).singleResult();
        //获取通过记录数，这里不能使用nrOfCompletedInstances，因为与我们业务无关
        String tmpCount = ProcessUtills.taskService().getVariable(task.getId(), "passCount")+"";
        //获取记录总数
        String tmpTotal = ProcessUtills.taskService().getVariable(task.getId(), "totalCount")+"";
        if(!tmpCount.equals("null") && !tmpCount.trim().equals("")){
            passCount = Integer.parseInt(tmpCount);
        }
        if(!tmpTotal.equals("null") && !tmpTotal.trim().equals("")){
            totalCount = Integer.parseInt(tmpTotal);
        }else{
            totalCount = (int)ProcessUtills.taskService().createTaskQuery().taskName("开始会签")
                    .processInstanceId(processId).count();
        }
        if(passflag.equals("yes")){//选择通过则+1
            passCount++;
        }

        Map<String, Object> vars = new HashMap<String,Object>();
        //变量回写记录
        vars.put("passCount", passCount);
        vars.put("totalCount", totalCount);
        System.out.println(vars.toString());
        ProcessUtills.taskService().complete(taskId,vars);

        List<Task> tasks = ProcessUtills.taskService().createTaskQuery().taskName("开始会签")
                .processInstanceId(processId).list();
        for(Task t:tasks){
            ProcessUtills.taskService().setVariables(t.getId(),vars);
        }
        /*for(Task tmp:tasks){

            //获取通过记录数，这里不能使用nrOfCompletedInstances，因为与我们业务无关
            String tmpCount = ProcessUtills.taskService().getVariable(tmp.getId(), "passCount")+"";
            //获取记录总数
            String tmpTotal = ProcessUtills.taskService().getVariable(tmp.getId(), "totalCount")+"";
            if(!tmpCount.equals("null") && !tmpCount.trim().equals("")){
                count = Integer.parseInt(tmpCount);
            }
            if(!tmpTotal.equals("null") && !tmpTotal.trim().equals("")){
                totalCount = Integer.parseInt(tmpTotal);
            }
            System.out.println(tmp.getId()+"var = "+passflag);
            if(passflag.equals("yes")){//选择通过则+1
                count++;
            }
            totalCount++;
        }
        Map<String, Object> vars = new HashMap<String,Object>();
        //变量回写记录
        vars.put("passCount", count);
        vars.put("totalCount", totalCount);
        System.out.println(vars.toString());
        ProcessUtills.taskService().complete(taskId,vars);*/
    }
}
