import e from"./DetailModal.46ca6abb.js";import{_ as r,u as o}from"./useTable.9db56aae.js";import{k as t}from"./useForm.e543e3b5.js";import{b as a}from"./useModal.138967e3.js";import{e as i,p as s,j as n,aW as l,X as c,W as d,dS as m,r as u,o as p,m as f,F as b,aP as j,z as g,A as x,n as y,C as v,D as E,E as h}from"./index.e5737575.js";import{getColumns as w}from"./data.be9eccb8.js";import"./responsiveObserve.c545f1cc.js";import"./get.d713ad9e.js";import"./index.0e4b6b68.js";import"./scrollTo.c1e0f75f.js";import"./onMountedOrActivated.9f5fb634.js";import"./index.ebc0e24c.js";import"./UpOutlined.7632f672.js";import"./index.be4ab8c9.js";import"./useWindowSizeFn.cd822dd7.js";import"./index.8d8da54b.js";import"./useSortable.4ac0939c.js";import"./RedoOutlined.87ae5366.js";import"./_baseIteratee.4a5bce24.js";import"./FullscreenOutlined.b2c49886.js";import"./index.dba09550.js";import"./index.8cefef3f.js";import"./vendor.afa0338c.js";import"./index.57a6dbfc.js";import"./index.a370f084.js";import"./CountdownInput.9c3f7a10.js";import"./download.c317d8b9.js";var C=i({name:"ErrorHandler",components:{DetailModal:e,BasicTable:r,TableAction:t},setup(){const e=s(),r=s([]),{t:t}=n(),[i,{setTableData:u}]=o({title:t("sys.errorLog.tableTitle"),columns:w(),actionColumn:{width:80,title:"Action",dataIndex:"action",slots:{customRender:"action"}}}),[p,{openModal:f}]=a();return l((()=>m.getErrorInfoState),(e=>{c((()=>{u(d(e))}))}),{immediate:!0}),{register:i,registerModal:p,handleDetail:function(r){e.value=r,f(!0)},fireVueError:function(){throw new Error("fire vue error!")},fireResourceError:function(){r.value.push(`${(new Date).getTime()}.png`)},fireAjaxError:function(){return e=this,r=null,o=function*(){},new Promise(((t,a)=>{var i=e=>{try{n(o.next(e))}catch(r){a(r)}},s=e=>{try{n(o.throw(e))}catch(r){a(r)}},n=e=>e.done?t(e.value):Promise.resolve(e.value).then(i,s);n((o=o.apply(e,r)).next())}));var e,r,o},imgList:r,rowInfo:e,t:t}}});const k={class:"p-4"};C.render=function(e,r,o,t,a,i){const s=u("DetailModal"),n=u("a-button"),l=u("TableAction"),c=u("BasicTable");return p(),f("div",k,[(p(!0),f(b,null,j(e.imgList,(e=>g((p(),f("img",{key:e,src:e},null,8,["src"])),[[x,!1]]))),128)),y(s,{info:e.rowInfo,onRegister:e.registerModal},null,8,["info","onRegister"]),y(c,{onRegister:e.register,class:"error-handle-table"},{toolbar:v((()=>[y(n,{onClick:e.fireVueError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireVueError")),1)])),_:1},8,["onClick"]),y(n,{onClick:e.fireResourceError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireResourceError")),1)])),_:1},8,["onClick"]),y(n,{onClick:e.fireAjaxError,type:"primary"},{default:v((()=>[E(h(e.t("sys.errorLog.fireAjaxError")),1)])),_:1},8,["onClick"])])),action:v((({record:r})=>[y(l,{actions:[{label:e.t("sys.errorLog.tableActionDesc"),onClick:e.handleDetail.bind(null,r)}]},null,8,["actions"])])),_:1},8,["onRegister"])])};export default C;