package com.yaroslav.news;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yaroslav.news.json.BotResponse;
import com.yaroslav.news.json.BotWebhook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring Boot Hello案例
 * <p>
 * Created by bysocket on 26/09/2017.
 */
@RestController
@RequestMapping(value = "/app")
public class AppController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BotResponse postBot(@RequestBody BotWebhook webhook) throws IOException {
        System.out.println(webhook);
        BotResponse response = new BotResponse();
        if (webhook != null && webhook.getQueryResult() != null && webhook.getQueryResult().getParameters() != null) {
            String subject = webhook.getQueryResult().getParameters().getSubject();
            if ((subject != null) && !(subject.equals(""))) {
                response.setFulfillmentText(process(subject) );
                response.setSource("something");
            }
        };
        return response;
    }

    private String doQuery(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        System.out.println("query for " + keyword);
        Request request = new Request.Builder()
                .url("https://www.bbc.co.uk/search?q=" + keyword + "&page=1")
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:78.0) Gecko/20100101 Firefox/78.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("DNT", "1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "ckns_explicit=0; ckns_policy=111; ckns_policy_exp=1627510732200")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("TE", "Trailers")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String process(String keyword) throws IOException {
        String res = "";
        String text = doQuery(keyword);
        Pattern title = Pattern.compile("class=\"css-johpve-PromoLink ett16tt7\"><span aria-hidden=\"false\">([A-Za-z 0-9&#;:,]+)");
//      Pattern price = Pattern.compile("<span class=\"a-offscreen\">([0-9.$]+)</span><span aria-hidden=\"true\"><span class=\"a-price-symbol\">");
        System.out.println(text.replace("\n", ""));
        String[] prods = (text.replace("\n", "").split("<div class=\"css-14rwwjy-Promo ett16tt11\">"));
        for (String p : prods) {
            System.out.println(p);
            Matcher m = title.matcher(p);
//            Matcher pr = price.matcher(p);
            if (m.find()) {
                System.out.println(m.group(1));
                res += m.group(1) + " / ";
            }

        }
        return res;
    }
}