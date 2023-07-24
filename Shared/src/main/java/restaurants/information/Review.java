package restaurants.information;

public class Review {
    private String name;
    private String text;
    private String rank;

    public Review() {
    }

    public Review(String name, String text, String rank) {
        this.name = name;
        this.text = text;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
