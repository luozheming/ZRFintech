import{_ as e,b as t}from "./useModal.e875d135.js";import{_ as o,u as r}from "./useTable.2851cd85.js";import"./useForm.65eee8c3.js";import{e as s,p as a,dJ as i,r as n,o as d,m as c,C as m,n as l,E as p,D as u,aq as j}from "./index.2da64865.js";import"./useWindowSizeFn.be34850b.js";import"./FullscreenOutlined.e9192cae.js";import"./get.498bc099.js";import"./index.2ccd4c5a.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.d065d4ea.js";import"./index.da5aa414.js";import"./RedoOutlined.e4a50862.js";import"./_baseIteratee.aeb63f7c.js";import"./index.a825569d.js";import"./index.ca3908e8.js";import"./useSortable.1f87e7ef.js";import"./index.5b683308.js";import"./index.84a07778.js";import"./responsiveObserve.c545f1cc.js";import"./vendor.afa0338c.js";import"./index.a05075a5.js";import"./index.d89f2e53.js";import"./CountdownInput.f59ea897.js";import"./download.fd994412.js";var f=s({name:"Modal",components:{BasicModal:e,BasicTable:o},setup(){const e=a();let o=a([]);const[s]=r({dataSource:o,columns:[{title:"对接投资人",dataIndex:"investor",width:100},{title:"金额",width:150,dataIndex:"commentAmount",slots:{customRender:"pay"}},{title:"交易状态",width:200,dataIndex:"isDone",slots:{customRender:"isDone"}},{title:"对接标志",width:120,dataIndex:"favor",slots:{customRender:"favor"}}],showIndexColumn:!1,bordered:!1}),[n]=t((t=>{return r=this,s=null,a=function*(){e.value=t.record.projectNm;const r=yield i({projectNo:t.record.projectNo});o.value=r.projectCommentList},new Promise(((e, t)=>{var o= e=>{try{n(a.next(e))}catch(o){t(o)}},i= e=>{try{n(a.throw(e))}catch(o){t(o)}},n= t=>t.done?e(t.value):Promise.resolve(t.value).then(o,i);n((a=a.apply(r,s)).next())}));var r,s,a}));return{registerModal:n,registerTable:s,projceName:e,list:o,getFavor: e=>({1:"感兴趣",2:"未标记",3:"不感兴趣",4:"拒绝"}[e])}}});const v={class:"projce-name"};f.render=function(e, t, o, r, s, a){const i=n("BasicTable"),f=n("BasicModal");return d(),c(f,j(e.$attrs,{title:"项目详情",onRegister:e.registerModal,showCancelBtn:!1,showOkBtn:!1}),{default:m((()=>[l("div",v,"项目名称: "+p(e.projceName),1),l(i,{onRegister:e.registerTable,canResize:!1},{isDone:m((({record:e})=>[u(p(e.isDone?"完成":"未完成"),1)])),favor:m((({record:t})=>[u(p(e.getFavor(t.favor)),1)])),pay:m((({record:e})=>[u(" ￥"+p(e.commentAmount),1)])),_:1},8,["onRegister"])])),_:1},16,["onRegister"])};export default f;
