package com.dbcontrol;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pojo.Investor;
import com.pojo.Project;
import com.utils.CommonUtils;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Initdb {

    public static void main(String args[]) {
        initInvestorDB();
//        initProjectDB();
    }


    public static void initInvestorDB() {
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
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("企融星").phoneNm("12345678901").investor("平台投资人").introd("投资专家").invesPhotoRoute("E:\\investor\\0\\平台.jpg").invesOrgPhotoRoute("E:\\investor\\0\\平台.jpg").price(new BigDecimal("0")).disCountPrice(new BigDecimal("0")).isPlatform(true).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("高瓴资本").phoneNm("12345678901").investor("杨某").introd("团队长").invesPhotoRoute("E:\\investor\\1\\杨博.jpg").invesOrgPhotoRoute("E:\\investor\\1\\杨博.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("金沙江创投").phoneNm("12345678901").investor("武某").introd("资深运营。\n 15亿用户APP。\n值得你信赖").invesPhotoRoute("E:\\investor\\2\\武思宇.jpg").invesOrgPhotoRoute("E:\\investor\\2\\武思宇.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("高瓴资本").phoneNm("12345678901").investor("罗某").introd("资深运营。\n 15亿用户APP。\n值得你信赖").invesPhotoRoute("E:\\investor\\3\\罗哲明.jpg").invesOrgPhotoRoute("E:\\investor\\3\\罗哲明.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("红衫资本").phoneNm("12345678901").investor("彭某").introd("资深运营。\n 15亿用户APP。\n值得你信赖").invesEmail("pengxingyu@cmbchina.com").invesPhotoRoute("E:\\investor\\4\\彭星宇.jpg").invesOrgPhotoRoute("E:\\investor\\4\\彭星宇.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("红衫资本").phoneNm("12345678901").investor("唐某").introd("资深开发。\n15年经验\n值得你信赖\n中软国际\n精通java后端").invesEmail("tangpeixuan@cmbchina.com").invesPhotoRoute("E:\\investor\\5\\唐培轩.jpg").invesOrgPhotoRoute("E:\\investor\\5\\唐培轩.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("招银国际").phoneNm("12345678901").investor("一剑").introd("资深开发。\n15年经验\n值得你信赖\n中软国际\n精通java后端").invesEmail("yijian@cmbchina.com").invesPhotoRoute("E:\\investor\\6\\一剑.jpg").invesOrgPhotoRoute("E:\\investor\\6\\一剑.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("招银国际").phoneNm("12345678901").investor("董某").introd("资深开发。\n15年经验\n值得你信赖\n中软国际\n精通java后端").invesEmail("dongzhenyu@cmbchina.com").invesPhotoRoute("E:\\investor\\7\\董振宇.jpg").invesOrgPhotoRoute("E:\\investor\\7\\董振宇.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("易帆国际").phoneNm("12345678901").investor("黄某").introd("资深开发。\n15年经验\n值得你信赖").invesEmail("zhiyunhuang@cmbchina.com").invesPhotoRoute("E:\\investor\\8\\黄志云.jpg").invesOrgPhotoRoute("E:\\investor\\8\\黄志云.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build(),
                Investor.builder().investorId(new CommonUtils().getNumCode()).orgNm("易帆国际").phoneNm("12345678901").investor("张三").introd("资深数据分析。").invesEmail("zhiyunhuang@cmbchina.com").invesPhotoRoute("E:\\investor\\9\\张三.jpg").invesOrgPhotoRoute("E:\\investor\\9\\张三.jpg").price(new BigDecimal("500")).disCountPrice(new BigDecimal("98")).isPlatform(false).status(0).build()
        );

        collection.insertMany(Investors);
    }

    public static void initProjectDB() {
        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase database = mongoClient.getDatabase("zrfintech");
        /*
        在将POJO与驱动程序一起使用之前，您需要配置，CodecRegistry 以包括一个编解码器来处理bsonPOJO的来回转换。最简单的方法是使用 PojoCodecProvider.builder()来创建和配置CodecProvider。
         */
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        database = database.withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Project> collection = database.getCollection("project", Project.class);
        List<Project> projects = asList(
                Project.builder().projectNo("658").projectNm("智多星").openId("895").teamSize("0-50").expList(asList("65","69","73")).build(),
                Project.builder().projectNo("896").projectNm("腾讯").openId("628").teamSize("0-50").expList(asList("12","28","69")).build(),
                Project.builder().projectNo("358").projectNm("蚂蚁金服").openId("318").teamSize("50-100").expList(asList("2","36","72")).build(),
                Project.builder().projectNo("985").projectNm("阿里巴巴").openId("358").teamSize("50-100").expList(asList("65","69","73")).build(),
                Project.builder().projectNo("115").projectNm("微信").openId("185").teamSize("100-500").expList(asList("3")).build(),
                Project.builder().projectNo("562").projectNm("富途").openId("433").teamSize("100-500").expList(asList("5","38","51")).build(),
                Project.builder().projectNo("812").projectNm("货拉拉").openId("251").teamSize("1000以上").build()
        );
        collection.insertMany(projects);
    }
}



