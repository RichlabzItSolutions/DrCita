package com.drcita.user.models.newProviderlist;

import java.util.List;

public class ProviderResponse {

        private boolean success;
        private String message;
        private List<NewProviderList> data;
        private Pagination pagination;  // Optional - if your API returns pagination info

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public List<NewProviderList> getData() { return data; }
        public void setData(List<NewProviderList> data) { this.data = data; }

        public Pagination getPagination() { return pagination; }
        public void setPagination(Pagination pagination) { this.pagination = pagination; }
    }






