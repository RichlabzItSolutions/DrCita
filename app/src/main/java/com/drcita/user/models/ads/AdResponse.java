package com.drcita.user.models.ads;

import java.util.ArrayList;
import java.util.List;

public class AdResponse {
    String status;
    String message;
    ArrayList<Ad> data;

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

    public List<Ad> getData() {
        return data;
    }

    public void setData(ArrayList<Ad> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AdResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Ad {
        int id;
        String image;
        String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Ad{" +
                    "id=" + id +
                    ", image='" + image + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
