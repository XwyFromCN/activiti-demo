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
public class Countersign3Listener implements TaskListener {
    public void notify(DelegateTask delegateTask) {
        String passflag = delegateTask.getVariable("passflag").toString();
        int passCount = 0;
        int refuseCount = 0;
        String tmpCount = delegateTask.getVariable( "passCount")+"";
        String tmpReCount = delegateTask.getVariable( "refuseCount")+"";
        if(!tmpCount.equals("null") && !tmpCount.trim().equals("")){
            passCount = Integer.parseInt(tmpCount);
        }
        if(!tmpReCount.equals("null") && !tmpReCount.trim().equals("")){
            refuseCount = Integer.parseInt(tmpReCount);
        }
        if(passflag.equals("yes")){//选择通过则+1
            passCount++;
        }else if(passflag.equals("no")){
            refuseCount++;
        }
        Map m = new HashMap();
        m.put("passCount",passCount);
        m.put("refuseCount",refuseCount);
        delegateTask.setVariables(m);
        System.out.println(m);
    }
}
