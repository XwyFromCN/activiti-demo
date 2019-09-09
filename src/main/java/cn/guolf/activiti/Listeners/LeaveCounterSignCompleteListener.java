package cn.guolf.activiti.Listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 请假会签任务监听器，当会签任务完成时统计同意的数量
 *
 * @author henryyan
 */
public class LeaveCounterSignCompleteListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    public void notify(DelegateTask delegateTask) {
        String approved = (String) delegateTask.getVariable("approved");
        if (approved.equals("true")) {
            Long agreeCounter = (Long) delegateTask.getVariable("approvedCounter");
            delegateTask.setVariable("approvedCounter", agreeCounter + 1);
        }
        System.out.println(delegateTask.getVariable("approvedCounter"));
    }

}
