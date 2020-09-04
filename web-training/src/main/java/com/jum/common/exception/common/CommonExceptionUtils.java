package com.jum.common.exception.common;

/**
 * @ClassName CommonExceptionUtils
 * @Description Copy from  NestedExceptionUtils
 * * Helper class for implementing exception classes which are capable of
 * holding nested exceptions. Necessary because we can't share a base
 * class among different exception types.
 *
 * <p>Mainly for use within the framework.
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 * @see CommonRuntimeException
 * @see CommonCheckedException
 */
public abstract class CommonExceptionUtils {
    /**
     * Build a message for the given base message and root cause.
     * @param message the base message
     * @param cause the root cause
     * @return the full exception message
     */
    public static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("nested exception is ").append(cause);
            return sb.toString();
        }
        else {
            return message;
        }
    }
}
