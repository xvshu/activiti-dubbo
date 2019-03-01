package com.eloancn.framework.activiti.util.activiti;

import com.eloancn.framework.activiti.TaskResult;

import java.util.Comparator;

/**
 * Created by xvshu on 2018/2/27.
 */
public class TaskComparator implements Comparator<TaskResult> {
    @Override
    public int compare(TaskResult o1, TaskResult o2) {
        return -(o1.getTask().getCreateTime().compareTo(o2.getTask().getCreateTime()));
    }
}
