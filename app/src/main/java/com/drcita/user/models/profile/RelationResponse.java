package com.drcita.user.models.profile;

import java.util.List;

public class RelationResponse {

    private boolean success;
    private String message;
    private List<Relation> data;

    // Inner model class
    public static class Relation {
        private int id;
        private String relation;
        private int status;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Relation> getData() {
        return data;
    }

    public void setData(List<Relation> data) {
        this.data = data;
    }
}
