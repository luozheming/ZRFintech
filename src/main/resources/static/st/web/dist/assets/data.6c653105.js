import{T as t}from "./index.d89f2e53.js";import{dQ as e,n as a,j as r}from "./index.2da64865.js";import"./vendor.afa0338c.js";const{t:d}=r();function n(){return[{dataIndex:"type",title:d("sys.errorLog.tableColumnType"),width:80,customRender:({text:r})=>{const d=r===e.VUE?"green":r===e.RESOURCE?"cyan":r===e.PROMISE?"blue":e.AJAX?"red":"purple";return a(t,{color:d},{default:()=>r})}},{dataIndex:"url",title:"URL",width:200},{dataIndex:"time",title:d("sys.errorLog.tableColumnDate"),width:160},{dataIndex:"file",title:d("sys.errorLog.tableColumnFile"),width:200},{dataIndex:"name",title:"Name",width:200},{dataIndex:"message",title:d("sys.errorLog.tableColumnMsg"),width:300},{dataIndex:"stack",title:d("sys.errorLog.tableColumnStackMsg")}]}function o(){return n().map((t=>({field:t.dataIndex,label:t.title})))}export{n as getColumns,o as getDescSchema};
