import{_ as e,u as t}from"./useTable.11ffc95d.js";import"./useForm.8ff5ab6b.js";import{u as o}from"./useModal.79b8cbe8.js";import i from"./Modal.9a1ef11c.js";import{e as a,dC as d,dI as s,r as n,o as r,m as c,n as l,C as p,D as m}from"./index.924fa4e6.js";import{d as u}from"./download.426b0153.js";import"./get.2ee4fe2a.js";import"./index.4eee2fdb.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.a6973fc4.js";import"./index.7322ba8c.js";import"./RedoOutlined.ac536275.js";import"./_baseIteratee.aa449b77.js";import"./index.5e740141.js";import"./useWindowSizeFn.ac7612c4.js";import"./index.d270335e.js";import"./useSortable.25d75773.js";import"./FullscreenOutlined.4f92282e.js";import"./index.a26d33c2.js";import"./index.647ae7b2.js";import"./responsiveObserve.c545f1cc.js";import"./vendor.afa0338c.js";import"./index.7cf58f64.js";import"./index.29087760.js";import"./CountdownInput.2c69efcf.js";const f=[{title:"项目名称",dataIndex:"projectNm",width:200},{title:"地址",dataIndex:"entProvince",width:100},{title:"团队人数",dataIndex:"teamSize",width:70},{title:"营收情况",dataIndex:"finSt",width:120},{title:"融资轮次",dataIndex:"finRound",width:70},{title:"期望融资金额",dataIndex:"quota",width:120},{title:"项目简介",dataIndex:"proDes",width:100},{title:"联系人",dataIndex:"proUser",width:100},{title:"联系方式",dataIndex:"proPhonum",width:120}];var j=a({components:{BasicTable:e,Modal:i},setup(){const{VITE_GLOB_API_URL:e}=d(),[i,{openModal:a}]=o(),[n,{reload:r}]=t({title:"项目列表",api:s,columns:f,showIndexColumn:!0,bordered:!1,size:"default",actionColumn:{width:200,title:"操作",dataIndex:"action",slots:{customRender:"action"},fixed:void 0}});return{registerTable:n,viewDetails:function(e){a(!0,{record:e})},registerModal:i,openModal:a,handleSuccess:function(){r()},download:t=>{u({url:`${e}/project/downLoadBP?projectNo=${t.projectNo}`,fileName:t.projectNm})}}}});const x={class:"p-4"},b=m("下载BP"),w=m("查看详情");j.render=function(e,t,o,i,a,d){const s=n("a-button"),m=n("BasicTable"),u=n("Modal");return r(),c("div",x,[l(m,{showTableSetting:"",onRegister:e.registerTable},{action:p((({record:t})=>[l(s,{class:"mr-2",shape:"round",onClick:o=>e.download(t)},{default:p((()=>[b])),_:2},1032,["onClick"]),l(s,{type:"primary",shape:"round",onClick:o=>e.viewDetails(t)},{default:p((()=>[w])),_:2},1032,["onClick"])])),_:1},8,["onRegister"]),l(u,{onRegister:e.registerModal,onSuccess:e.handleSuccess},null,8,["onRegister","onSuccess"])])};export default j;
