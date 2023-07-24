package restaurants.information;

import java.util.List;

public class Restaurant {
    private String name;
    private String address;
    private String website;
    private String phone;
    private String rank;
    private String previewImg;
    private String businessID;
    private String priceRange;
    private List<String> timeList;
    private List<String> images;
    private List<Review> reviews;

    public Restaurant() {
    }

    public Restaurant(String name, String address, String website, String phone, String rank) {
        this.name = name;
        this.address = address;
        this.website = website;
        this.phone = phone;
        this.rank = rank;
    }

    public Restaurant(Restaurant copy) {
        this.name = copy.getName();
        this.address = copy.getAddress();
        this.website = copy.getWebsite();
        this.phone = copy.getPhone();
        this.rank = copy.getRank();
        this.previewImg = copy.getPreviewImg();
        this.businessID = copy.getBusinessID();
        this.priceRange = copy.getPriceRange();
        this.timeList = copy.getTimeList();
        this.images = copy.getImages();
        this.reviews = copy.getReviews();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPreviewImg() {
        return this.previewImg;
    }

    public void setPreviewImg(String previewImg) {
        this.previewImg = previewImg;
    }

    public String getBusinessID() {
        return this.businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getPriceRange() {
        return this.priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public List<String> getTimeList() {
        return this.timeList;
    }

    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
