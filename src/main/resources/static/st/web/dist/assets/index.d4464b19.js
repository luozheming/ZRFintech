import{_ as e,u as t}from "./useTable.2851cd85.js";import"./useForm.65eee8c3.js";import{u as o}from "./useModal.e875d135.js";import s from "./Modal.bd606c95.js";import{e as a,c4 as r,r as i,o as d,m as n,n as c,C as l,D as p}from "./index.2da64865.js";import"./get.498bc099.js";import"./index.2ccd4c5a.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.d065d4ea.js";import"./index.da5aa414.js";import"./RedoOutlined.e4a50862.js";import"./_baseIteratee.aeb63f7c.js";import"./index.a825569d.js";import"./useWindowSizeFn.be34850b.js";import"./index.ca3908e8.js";import"./useSortable.1f87e7ef.js";import"./FullscreenOutlined.e9192cae.js";import"./index.5b683308.js";import"./index.84a07778.js";import"./responsiveObserve.c545f1cc.js";import"./vendor.afa0338c.js";import"./index.a05075a5.js";import"./index.d89f2e53.js";import"./CountdownInput.f59ea897.js";import"./download.fd994412.js";var m=a({components:{BasicTable:e,Modal:s},setup(){const[e,{openModal:s}]=o(),[a,{reload:i}]=t({title:"评论列表",api:r,columns:[{title:"投资人姓名",dataIndex:"investor",width:100},{title:"项目名称",dataIndex:"projectNm",width:200},{title:"评价内容",dataIndex:"content",width:150,slots:{customRender:"content"}},{title:"获得星级",width:50,dataIndex:"stars"},{title:"客户反馈",width:150,dataIndex:"reply",slots:{customRender:"feedback"}}],showIndexColumn:!1,bordered:!1});return{registerTable:a,viewDetails:(e, t)=>{s(!0,{record:e,statu:t})},registerModal:e,openModal:s,handleSuccess:function(){i()}}}});const u={class:"p-4"},j=p("查看详情"),f=p("查看详情");m.render=function(e, t, o, s, a, r){const p=i("a-button"),m=i("BasicTable"),b=i("Modal");return d(),n("div",u,[c(m,{showTableSetting:"",onRegister:e.registerTable},{content:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick: o=>e.viewDetails(t,0)},{default:l((()=>[j])),_:2},1032,["onClick"])])),feedback:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick: o=>e.viewDetails(t,1)},{default:l((()=>[f])),_:2},1032,["onClick"])])),_:1},8,["onRegister"]),c(b,{onRegister:e.registerModal,onSuccess:e.handleSuccess},null,8,["onRegister","onSuccess"])])};export default m;