package com.eloancn.framework.activiti.util.error;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xvshu on 2017/10/30.
 */
public final class Preconditions {

    private Preconditions() {
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if(!expression) {
            throw new ActivitiException(String.valueOf(errorMessage));
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if(reference == null) {
            throw new ActivitiException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static  void checkNotFalse(Boolean reference, String errorMessage) {
        if(reference == false) {
            throw new ActivitiException(String.valueOf(errorMessage));
        }
    }


    public static <T> T checkNotNullOrEmpty(T reference, String errorMessage) {
        if(reference == null) {
            throw new ActivitiException(String.valueOf(errorMessage));
        } else if(reference instanceof Collection && ((Collection) reference).isEmpty()){
            throw new ActivitiException(String.valueOf(errorMessage));
        }else if(reference instanceof Map && ((Map) reference).isEmpty()){
            throw new ActivitiException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static void checkArgumentOutOfRange(boolean expression, String errorMessage) {
        if(!expression) {
            throw new ActivitiException(String.valueOf(errorMessage));
        }
    }


}
