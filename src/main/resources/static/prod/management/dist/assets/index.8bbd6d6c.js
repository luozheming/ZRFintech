import{_ as e,u as t}from"./useTable.41282832.js";import"./useForm.e3cb60e7.js";import{b as o}from"./useModal.f7817ea0.js";import s from"./Modal.eaa4fe2a.js";import{e as i,dx as r,r as a,o as n,m as d,n as c,C as l,D as p}from"./index.22b3842c.js";import"./get.29e7fdfb.js";import"./index.4507acc2.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.14eacd0c.js";import"./index.6d15acf2.js";import"./UpOutlined.42537489.js";import"./index.54d27705.js";import"./useWindowSizeFn.84bb5680.js";import"./index.c0f1a3c5.js";import"./useSortable.4ce3961e.js";import"./RedoOutlined.22cccd55.js";import"./_baseIteratee.2a793644.js";import"./FullscreenOutlined.c2732f78.js";import"./index.47947cc7.js";import"./index.315fd84a.js";import"./responsiveObserve.c545f1cc.js";import"./vendor.afa0338c.js";import"./index.bcd11ebc.js";import"./index.c5b9a475.js";import"./CountdownInput.e8b0e229.js";import"./download.107083c5.js";var m=i({components:{BasicTable:e,Modal:s},setup(){const[e,{openModal:s}]=o(),[i,{reload:a}]=t({title:"评论列表",api:r,columns:[{title:"投资人姓名",dataIndex:"investor",width:100},{title:"项目名称",dataIndex:"projectNm",width:200},{title:"评价内容",dataIndex:"content",width:150,slots:{customRender:"content"}},{title:"获得星级",width:50,dataIndex:"stars"},{title:"客户反馈",width:150,dataIndex:"reply",slots:{customRender:"feedback"}}],showIndexColumn:!1,bordered:!1});return{registerTable:i,viewDetails:(e,t)=>{s(!0,{record:e,statu:t})},registerModal:e,openModal:s,handleSuccess:function(){a()}}}});const u={class:"p-4"},j=p("查看详情"),b=p("查看详情");m.render=function(e,t,o,s,i,r){const p=a("a-button"),m=a("BasicTable"),f=a("Modal");return n(),d("div",u,[c(m,{showTableSetting:"",onRegister:e.registerTable},{content:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick:o=>e.viewDetails(t,0)},{default:l((()=>[j])),_:2},1032,["onClick"])])),feedback:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick:o=>e.viewDetails(t,1)},{default:l((()=>[b])),_:2},1032,["onClick"])])),_:1},8,["onRegister"]),c(f,{onRegister:e.registerModal,onSuccess:e.handleSuccess},null,8,["onRegister","onSuccess"])])};export default m;
