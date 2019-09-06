package cn.guolf.activiti.Listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: xwy
 * @Date: 2019/9/6 11:04
 * @Description:
 */
public class Countersign2Listener implements ExecutionListener {
    public void notify(DelegateExecution execution) throws Exception {
        List<String> list = Arrays.asList("passCount","totalCount");
        execution.removeVariables(list);
        System.out.println("清除："+list.toString());
    }
}
