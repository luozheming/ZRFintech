# -*- coding: utf-8 -*-  
from pymongo import MongoClient
import os
import uuid

try:
    client = MongoClient('localhost', 27017)
    db = client["zrfintech"]
    db.drop_collection("investor")
    collection = db.investor
    investors = [
        {
            "investorId":str(uuid.uuid1()),
            "orgNm":"企融星",
            "phoneNm":"19876788693",
            "investor":"平台投资人",
            "introd":"反馈率100%。\n平均反馈时间24h内。\n专注金融、AI、智能制造、企业服务、电商、新消费、硬件融资赛道。\n",
            "invesEmail":"270065736@qq.com",
            "price":0,
            "disCountPrice":0,
            "isPlatform":True
        },
        {
            "investorId":str(uuid.uuid1()),
            "orgNm":"招商银行",
            "phoneNm":"13922828056",
            "investor":"王峻杨",
            "introd":"招商银行金融科技办公室主管，CFA。\n聚焦企业服务赛道。\n专注天使轮、Pre-A轮企业。\n",
            "invesEmail":"13922828056@139.com",
            "price":599,
            "disCountPrice":99,
            "isPlatform":False
        },
        {
            "investorId":str(uuid.uuid1()),
            "orgNm":"招商银行",
            "phoneNm":"15820787157",
            "investor":"廖廖",
            "introd":"招商银行就职。\n聚焦企业服务、金融科技赛道。\n专注种子轮到A轮企业。\n",
            "invesEmail":"liaozhifangs@cmbchina.com",
            "price":599,
            "disCountPrice":99,
            "isPlatform":False
        },
        {
            "investorId":str(uuid.uuid1()),
            "orgNm":"恒旭资本",
            "phoneNm":"19925250754",
            "investor":"张若聪",
            "introd":"上汽母基金董事。\n聚焦工业软件、数据安全赛道。\n专注B+轮企业。\n",
            "invesEmail":"ruocongz@hengxucapital.com",
            "price":599,
            "disCountPrice":99,
            "isPlatform":False
        }
    ]

    ## 初始化创建文件目录
    for person in investors:
        dirpath = "./data/investor/"+ person["investorId"]
        orgdirpath = "./data/org/" + person["orgNm"]
        if not os.path.isdir(dirpath):
            os.makedirs(dirpath)
        if not os.path.isdir(orgdirpath):
            os.makedirs(orgdirpath)
        person["invesPhotoRoute"] = dirpath
        person["invesOrgPhotoRoute"] = orgdirpath
    ## 批量插入数据
    collection.insert_many(investors)
except:
    print("something wrong with the database")
finally:
    client.close()
