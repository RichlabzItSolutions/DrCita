package com.drcita.user.models.doctorslots;

public class SlotResponse {
    String status;
    String message;
    Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public  String getMessage() {
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

    public class Data{
        int slotNumber;
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSlotNumber() {
            return slotNumber;
        }

        public void setSlotNumber(int slotNumber) {
            this.slotNumber = slotNumber;
        }
    }
}
