package cn.guolf.activiti.ServiceProcess;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Auther: xwy
 * @Date: 2019/9/10 09:18
 * @Description:
 */
public class ServiceTask implements JavaDelegate{
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("我开始做事了...");
        Thread.sleep(1000);
        System.out.println("我做完了!!!");
    }
}
