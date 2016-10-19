package gmo;


import com.fasterxml.jackson.databind.ObjectMapper;
import gmo.Entity.NewEntity;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by QUANG MINH on 3/8/2016.
 */


public class MainApplication {



    public static void main(String[] args) throws IOException {
        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        ObjectMapper mapper=new ObjectMapper();

        
       String json;


        ArrayList<NewEntity> arrayList=new ArrayList<>();
        Document docMenuRss = Jsoup.connect("http://soha.vn/rss.htm").
                userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").
                get();
        Elements elementsMenuRss=docMenuRss.select("div#listrss li a");
        //System.out.println(elements.size());
        for(int i=1;i<elementsMenuRss.size();i++){
            Element e=elementsMenuRss.get(i);
            System.out.println(e.attr("abs:href"));
            Document docMiniRss = Jsoup.connect(e.attr("abs:href")).
                    userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").
                    get();
            Elements elementsMiniRss=docMiniRss.select("item link");
            for(Element e2:elementsMiniRss){
                URL url1 = new URL(e2.html());
                System.out.println(e2.html());
                Document docNew = Jsoup.connect("http://"+url1.getHost()+url1.getPath()).
                        userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").
                        get();
                try {
                    NewEntity newEntity=new NewEntity(docNew);
                    json=mapper.writeValueAsString(newEntity);
                    IndexResponse response = client.prepareIndex("news", "soha")
                            .setSource(json)
                            .get();
                }catch (Exception e1){
                    System.out.println("Time out");
                }


                //arrayList.add(newEntity);


            }
        }
    }


}
