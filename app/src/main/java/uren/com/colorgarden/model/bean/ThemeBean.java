package uren.com.colorgarden.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeBean extends ResponseBean {
    @SerializedName("result")
    private List<Theme> themes;

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public static class Theme {
        private int categoryId;
        private String name;
        private int status;
        private String path;
        private String isLocked;
        private String productId;

        public Theme() {
        }

        public Theme(int categoryId, String name, int status, String path, String isLocked, String productId) {
            this.categoryId = categoryId;
            this.name = name;
            this.status = status;
            this.path = path;
            this.isLocked = isLocked;
            this.productId = productId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getIsLocked() {
            return isLocked;
        }

        public void setIsLocked(String isLocked) {
            this.isLocked = isLocked;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }
}
