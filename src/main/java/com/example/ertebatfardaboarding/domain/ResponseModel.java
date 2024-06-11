package com.example.ertebatfardaboarding.domain;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ResponseModel<T>{

    private int status;
    private int recordCount;
    private T content;
    private List<?> contents;
    private String error;
    private int result;
    private String systemError;


    public <T> T merge(T local, T remote) {
        try {
            Class<?> localClass = local.getClass();
            Object merged = localClass.newInstance();
            Field[] declaredFields = localClass.getDeclaredFields();
            Field[] superClassFields = localClass.getSuperclass().getDeclaredFields();
            Field[] abstractSuperClassFields  = localClass.getSuperclass().getSuperclass().getDeclaredFields();
            Field[] fields = Stream.concat(Arrays.stream(declaredFields) , Arrays.stream(superClassFields)).toArray(Field[]::new);
            Field[] allFields = Stream.concat(Arrays.stream(fields) , Arrays.stream(abstractSuperClassFields)).toArray(Field[]::new);
            for (Field field : allFields) {
                field.setAccessible(true);
                Object localValue = field.get(local);
                Object remoteValue = field.get(remote);

                field.set(merged, ((remoteValue != null && remoteValue != "") && !remoteValue.toString().equalsIgnoreCase("")
                        && !remoteValue.toString().equalsIgnoreCase("0")) ? remoteValue : localValue);
            }
            return (T) merged;
        } catch (Exception e) {
            System.out.println("error in merge function : " + e.toString());
            return local;
        }
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "status=" + status +
                ", recordCount=" + recordCount +
                ", content=" + content +
                ", contents=" + contents +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", systemError='" + systemError + '\'' +
                '}';
    }

    public String toStringJson() {
        return "{" +
                "status=" + status +
                ", recordCount=" + recordCount +
                ", content=" + content +
                ", contents=" + contents +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", systemError='" + systemError + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public List<?> getContents() {
        return contents;
    }

    public void setContents(List<?> contents) {
        this.contents = contents;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getSystemError() {
        return systemError;
    }

    public void setSystemError(String systemError) {
        this.systemError = systemError;
    }

    public void clear() {
        setSystemError(null);
        setError(null);
        setRecordCount(0);
        setResult(0);
        setStatus(0);
        setContents(null);
        setContent(null);
    }


}
