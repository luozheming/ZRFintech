import e from"./DetailModal.0e438bce.js";import{_ as r,u as o}from"./useTable.11ffc95d.js";import{k as t}from"./useForm.8ff5ab6b.js";import{u as i}from"./useModal.79b8cbe8.js";import{e as a,p as s,j as n,aW as l,X as c,W as d,dM as m,r as f,o as u,m as p,F as j,aP as b,z as g,A as x,n as y,C as v,D as E,E as h}from"./index.924fa4e6.js";import{getColumns as w}from"./data.c39639f2.js";import"./responsiveObserve.c545f1cc.js";import"./get.2ee4fe2a.js";import"./index.4eee2fdb.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.a6973fc4.js";import"./index.7322ba8c.js";import"./RedoOutlined.ac536275.js";import"./_baseIteratee.aa449b77.js";import"./index.5e740141.js";import"./useWindowSizeFn.ac7612c4.js";import"./index.d270335e.js";import"./useSortable.25d75773.js";import"./FullscreenOutlined.4f92282e.js";import"./index.a26d33c2.js";import"./index.647ae7b2.js";import"./vendor.afa0338c.js";import"./index.7cf58f64.js";import"./index.29087760.js";import"./CountdownInput.2c69efcf.js";import"./download.426b0153.js";var C=a({name:"ErrorHandler",components:{DetailModal:e,BasicTable:r,TableAction:t},setup(){const e=s(),r=s([]),{t:t}=n(),[a,{setTableData:f}]=o({title:t("sys.errorLog.tableTitle"),columns:w(),actionColumn:{width:80,title:"Action",dataIndex:"action",slots:{customRender:"action"}}}),[u,{openModal:p}]=i();return l((()=>m.getErrorInfoState),(e=>{c((()=>{f(d(e))}))}),{immediate:!0}),{register:a,registerModal:u,handleDetail:function(r){e.value=r,p(!0)},fireVueError:function(){throw new Error("fire vue error!")},fireResourceError:function(){r.value.push(`${(new Date).getTime()}.png`)},fireAjaxError:function(){return e=this,r=null,o=function*(){},new Promise(((t,i)=>{var a=e=>{try{n(o.next(e))}catch(r){i(r)}},s=e=>{try{n(o.throw(e))}catch(r){i(r)}},n=e=>e.done?t(e.value):Promise.resolve(e.value).then(a,s);n((o=o.apply(e,r)).next())}));var e,r,o},imgList:r,rowInfo:e,t:t}}});const k={class:"p-4"};C.render=function(e,r,o,t,i,a){const s=f("DetailModal"),n=f("a-button"),l=f("TableAction"),c=f("BasicTable");return u(),p("div",k,[(u(!0),p(j,null,b(e.imgList,(e=>g((u(),p("img",{key:e,src:e},null,8,["src"])),[[x,!1]]))),128)),y(s,{info:e.rowInfo,onRegister:e.registerModal},null,8,["info","onRegister"]),y(c,{onRegister:e.register,class:"error-handle-table"},{toolbar:v((()=>[y(n,{onClick:e.fireVueError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireVueError")),1)])),_:1},8,["onClick"]),y(n,{onClick:e.fireResourceError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireResourceError")),1)])),_:1},8,["onClick"]),y(n,{onClick:e.fireAjaxError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireAjaxError")),1)])),_:1},8,["onClick"])])),action:v((({record:r})=>[y(l,{actions:[{label:e.t("sys.errorLog.tableActionDesc"),onClick:e.handleDetail.bind(null,r)}]},null,8,["actions"])])),_:1},8,["onRegister"])])};export default C;
