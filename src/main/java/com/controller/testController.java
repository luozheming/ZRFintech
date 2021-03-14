package com.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pojo.Investor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

@RestController
public class    testController {

    @GetMapping(value = "/csvtest")
    public void getCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File("d:/test.csv");
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("text/csv");
        OutputStream stream = response.getOutputStream();
        stream.write(buffer);
        stream.flush();
        stream.close();
        }

    @GetMapping(value = "/contectMongo")
    public String getMongoData(HttpServletRequest request){
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Investor> collection = database.getCollection("investor", Investor.class);



        return "helloworld";
    }
}
