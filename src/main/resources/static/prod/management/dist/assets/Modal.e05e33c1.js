import{_ as e,a as t}from"./useModal.138967e3.js";import{_ as a,u as o}from"./useTable.9db56aae.js";import"./useForm.e543e3b5.js";import{e as r,p as n,dM as s,r as d,o as i,m as l,C as c,n as m,E as u,D as p,aq as h}from"./index.e5737575.js";function v(){return[{title:"项目名称",dataIndex:"projectNm",width:140},{title:"地址",dataIndex:"entProvince",width:80},{title:"团队人数",dataIndex:"teamSize",width:100},{title:"营收情况",dataIndex:"finSt",width:120},{title:"融资轮次",dataIndex:"finRound",width:70},{title:"期望融资金额",dataIndex:"quota",width:100},{title:"联系人",dataIndex:"proUser",width:80},{title:"联系方式",dataIndex:"proPhonum",width:100},{title:"联系人邮箱",dataIndex:"proEmail",width:160}]}var w=r({name:"Modal",components:{BasicModal:e,BasicTable:a},setup(){const e=n();let a=n([]);const[r]=o({dataSource:a,columns:[{title:"对接投资人",dataIndex:"investor",width:100},{title:"金额",width:150,dataIndex:"commentAmount",slots:{customRender:"pay"}},{title:"交易状态",width:200,dataIndex:"isDone",slots:{customRender:"isDone"}},{title:"对接标志",width:120,dataIndex:"favor",slots:{customRender:"favor"}}],showIndexColumn:!1,bordered:!1}),[d]=t((t=>{return o=this,r=null,n=function*(){e.value=t.record.projectNm;const o=yield s({projectNo:t.record.projectNo});a.value=o.projectCommentList},new Promise(((e,t)=>{var a=e=>{try{d(n.next(e))}catch(a){t(a)}},s=e=>{try{d(n.throw(e))}catch(a){t(a)}},d=t=>t.done?e(t.value):Promise.resolve(t.value).then(a,s);d((n=n.apply(o,r)).next())}));var o,r,n}));return{registerModal:d,registerTable:r,projceName:e,list:a,getFavor:e=>({1:"感兴趣",2:"未标记",3:"不感兴趣",4:"拒绝"}[e])}}});const x={class:"projce-name"};w.render=function(e,t,a,o,r,n){const s=d("BasicTable"),v=d("BasicModal");return i(),l(v,h(e.$attrs,{title:"项目详情",onRegister:e.registerModal,showCancelBtn:!1,showOkBtn:!1}),{default:c((()=>[m("div",x,"项目名称: "+u(e.projceName),1),m(s,{onRegister:e.registerTable,canResize:!1},{isDone:c((({record:e})=>[p(u(e.isDone?"完成":"未完成"),1)])),favor:c((({record:t})=>[p(u(e.getFavor(t.favor)),1)])),pay:c((({record:e})=>[p(" ￥"+u(e.commentAmount),1)])),_:1},8,["onRegister"])])),_:1},16,["onRegister"])};var f=Object.freeze({__proto__:null,[Symbol.toStringTag]:"Module",default:w});export{f as M,w as _,v as c};