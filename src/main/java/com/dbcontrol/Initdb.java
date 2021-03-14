package com.dbcontrol;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import com.pojo.Investor;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.List;

public class Initdb {
    public static void main(String args[]){
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase database = mongoClient.getDatabase("zrfintech");
        /*
        在将POJO与驱动程序一起使用之前，您需要配置，CodecRegistry 以包括一个编解码器来处理bsonPOJO的来回转换。最简单的方法是使用 PojoCodecProvider.builder()来创建和配置CodecProvider。
         */
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        database = database.withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Investor> collection = database.getCollection("investor", Investor.class);
        List<Investor> Investors = asList(
                Investor.builder().orgNm("高瓴资本").pthoneNm("19991967502").investor("杨博").introd("团队长").build(),
                Investor.builder().orgNm("金沙江创投").pthoneNm("13585688902").investor("武思宇").build(),
                Investor.builder().orgNm("高瓴资本").pthoneNm("18696148635").investor("罗哲明").build(),
                Investor.builder().orgNm("红衫资本").pthoneNm("18392181272").investor("彭星宇").invesEmail("pengxingyu@cmbchina.com").build()
        );

        collection.insertMany(Investors);
    }
}
