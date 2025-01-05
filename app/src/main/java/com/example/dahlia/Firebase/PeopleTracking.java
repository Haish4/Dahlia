package com.example.dahlia.Firebase;

public class PeopleTracking {
        private String name;
        private String location;
        private String imageUrl;

        public PeopleTracking() {}


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public PeopleTracking(String name, String location, String imageUrl) {
            this.name = name;
            this.location = location;
            this.imageUrl = imageUrl;
        }

}
