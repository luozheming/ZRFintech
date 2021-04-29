import{_ as e,u as t}from"./useTable.39c5afd8.js";import"./useForm.5df060fd.js";import{b as o}from"./useModal.2e3742fa.js";import s from"./Modal.a130f593.js";import{e as r,dx as a,r as i,o as d,m as n,n as c,C as l,D as p}from"./index.aa79050f.js";import"./get.f1b5c461.js";import"./index.46b91f8c.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.1da36230.js";import"./index.463bc14a.js";import"./RedoOutlined.318a41fc.js";import"./_baseIteratee.07fb58d9.js";import"./index.8ea6c21f.js";import"./useWindowSizeFn.72221960.js";import"./index.caf9d56d.js";import"./useSortable.01481f7c.js";import"./FullscreenOutlined.0bd6e6a7.js";import"./index.08be3b0e.js";import"./index.f0647021.js";import"./responsiveObserve.c545f1cc.js";import"./vendor.afa0338c.js";import"./index.a54964fc.js";import"./index.36e421a5.js";import"./CountdownInput.07177421.js";import"./download.0af5ee19.js";var m=r({components:{BasicTable:e,Modal:s},setup(){const[e,{openModal:s}]=o(),[r,{reload:i}]=t({title:"评论列表",api:a,columns:[{title:"投资人姓名",dataIndex:"investor",width:100},{title:"项目名称",dataIndex:"projectNm",width:200},{title:"评价内容",dataIndex:"content",width:150,slots:{customRender:"content"}},{title:"获得星级",width:50,dataIndex:"stars"},{title:"客户反馈",width:150,dataIndex:"reply",slots:{customRender:"feedback"}}],showIndexColumn:!1,bordered:!1});return{registerTable:r,viewDetails:(e,t)=>{s(!0,{record:e,statu:t})},registerModal:e,openModal:s,handleSuccess:function(){i()}}}});const u={class:"p-4"},f=p("查看详情"),j=p("查看详情");m.render=function(e,t,o,s,r,a){const p=i("a-button"),m=i("BasicTable"),b=i("Modal");return d(),n("div",u,[c(m,{showTableSetting:"",onRegister:e.registerTable},{content:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick:o=>e.viewDetails(t,0)},{default:l((()=>[f])),_:2},1032,["onClick"])])),feedback:l((({record:t})=>[c(p,{type:"primary",shape:"round",onClick:o=>e.viewDetails(t,1)},{default:l((()=>[j])),_:2},1032,["onClick"])])),_:1},8,["onRegister"]),c(b,{onRegister:e.registerModal,onSuccess:e.handleSuccess},null,8,["onRegister","onSuccess"])])};export default m;
