package cn.guolf.activiti.Listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: xwy
 * @Date: 2019/9/6 11:04
 * @Description:
 */
public class Countersign2Listener2 implements TaskListener {
    public void notify(DelegateTask delegateTask) {
        String passflag = delegateTask.getVariable("passflag").toString();
        int passCount = 0;
        //获取通过记录数，这里不能使用nrOfCompletedInstances，因为与我们业务无关
        String tmpCount = delegateTask.getVariable( "passCount")+"";
        if(!tmpCount.equals("null") && !tmpCount.trim().equals("")){
            passCount = Integer.parseInt(tmpCount);
        }
        if(passflag.equals("yes")){//选择通过则+1
            passCount++;
        }
        Map m = new HashMap();
        m.put("passCount",passCount);
        delegateTask.setVariables(m);
        System.out.println(m);
    }
}
