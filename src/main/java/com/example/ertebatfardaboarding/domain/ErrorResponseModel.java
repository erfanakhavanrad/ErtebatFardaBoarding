package com.example.ertebatfardaboarding.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ErrorResponseModel {
    private int status;
    private String error;
    private final int result = 0;
    private String timestamp;

    @Override
    public String toString() {
        return "ResponseModel{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public String toStringJson() {
        return "{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public void clear() {
        setTimestamp(null);
        setError(null);
        setStatus(0);
    }
}
