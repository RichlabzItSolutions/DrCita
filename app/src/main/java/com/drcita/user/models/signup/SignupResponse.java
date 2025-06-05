package com.drcita.user.models.signup;

import com.google.gson.annotations.Expose;
import java.util.*;

public class SignupResponse {

    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
    private Data data;
    @Expose
    private Object errors;  // Accepts any format

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getRawErrors() {
        return errors;
    }

    // Safely parse errors as a list of strings
    public List<String> getErrors() {
        List<String> errorList = new ArrayList<>();

        if (errors instanceof String) {
            errorList.add((String) errors);

        } else if (errors instanceof List<?>) {
            for (Object item : (List<?>) errors) {
                if (item instanceof String) {
                    errorList.add((String) item);
                }
            }

        } else if (errors instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) errors).entrySet()) {
                if (entry.getValue() instanceof String) {
                    errorList.add((String) entry.getValue());
                } else if (entry.getValue() instanceof List<?>) {
                    for (Object val : (List<?>) entry.getValue()) {
                        if (val instanceof String) {
                            errorList.add((String) val);
                        }
                    }
                }
            }
        }

        return errorList;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
