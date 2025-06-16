package com.drcita.user.models.userprofile;


    // DiseaseModel.java
    public class DiseaseModel {
        private int id;
        private int selection; // 0 = none, 1 = yes, 2 = no

        public DiseaseModel(int id, int selection) {
            this.id = id;
            this.selection = selection;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


