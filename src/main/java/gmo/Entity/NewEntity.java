package gmo.Entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by quangminh on 27/09/2016.
 */
public class NewEntity {
    String title;
    String body;
    String image;
    Date createTime;

    public NewEntity(Document doc) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Elements e=doc.select("h1");
        this.title=e.get(0).html();
        e=doc.select("main figure img");
        this.image=e.attr("abs:src");
        e=doc.select("div[data-field='body']");
        Element deleteE=e.get(0).getElementsByClass("link-content-footer").first();
        deleteE.remove();
        this.body=e.get(0).html();
        e=doc.select("time");
        try {
            this.createTime = formatter.parse(e.html());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



}
