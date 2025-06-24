package com.drcita.user.models.profile;

public class CheckUserResponse {
    private boolean success;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Nested class for "data"
    public static class Data {
        private int status;
        private int subUserId;
        private int self;
        private String userId;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubUserId() {
            return subUserId;
        }

        public void setSubUserId(int subUserId) {
            this.subUserId = subUserId;
        }

        public int getSelf() {
            return self;
        }

        public void setSelf(int self) {
            this.self = self;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "status=" + status +
                    ", subUserId=" + subUserId +
                    ", self=" + self +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }
}
