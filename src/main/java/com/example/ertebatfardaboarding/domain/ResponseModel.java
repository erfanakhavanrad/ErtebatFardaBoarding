package com.example.ertebatfardaboarding.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class ResponseModel<T> {

    private int status;
    private int recordCount;
    private T content;
    private List<?> contents;
    private final int result = 1;

    @Override
    public String toString() {
        return "ResponseModel{" +
                "status=" + status +
                ", recordCount=" + recordCount +
                ", content=" + content +
                ", contents=" + contents +
                ", result='" + result + '\'' +
                '}';
    }

    public String toStringJson() {
        return "{" +
                "status=" + status +
                ", recordCount=" + recordCount +
                ", content=" + content +
                ", contents=" + contents +
                ", result='" + result + '\'' +
                '}';
    }

    public void clear() {
        setRecordCount(0);
        setStatus(0);
        setContents(null);
        setContent(null);
    }

}
