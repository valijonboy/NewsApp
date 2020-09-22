package pop.uz.newsapp.model;

public class Article{

    private String title;
    private String nameSection;
    private String webUrl;
    private String publicDate;

    public Article(String title, String nameSection, String webUrl, String publicDate) {
        this.title = title;
        this.nameSection = nameSection;
        this.webUrl = webUrl;
        this.publicDate = publicDate;
    }

    public String getTitle() {
        return title;
    }

    public String getNameSection() {
        return nameSection;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getPublicDate() {
        return publicDate;
    }
}
