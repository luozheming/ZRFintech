import{e,b3 as t,g as s,dz as a,dA as i,r as l,o as n,m as r,F as p,aP as c,n as o,bF as d}from"./index.07990433.js";import"./vendor.afa0338c.js";var f=e({name:"MenuTypePicker",components:{Tooltip:t},props:{menuTypeList:{type:Array,defualt:()=>[]},handler:{type:Function,default:()=>{}},def:{type:String,default:""}},setup(){const{prefixCls:e}=s("setting-menu-type-picker");return{prefixCls:e}}});const m=d("data-v-c5cf2698");a("data-v-c5cf2698");const u=o("div",{class:"mix-sidebar"},null,-1);i();const y=m(((e,t,s,a,i,d)=>{const f=l("Tooltip");return n(),r("div",{class:e.prefixCls},[(n(!0),r(p,null,c(e.menuTypeList||[],(t=>(n(),r(f,{key:t.title,title:t.title,placement:"bottom"},{default:m((()=>[o("div",{onClick:s=>e.handler(t),class:[`${e.prefixCls}__item`,`${e.prefixCls}__item--${t.type}`,{[`${e.prefixCls}__item--active`]:e.def===t.type}]},[u],10,["onClick"])])),_:2},1032,["title"])))),128))],2)}));f.render=y,f.__scopeId="data-v-c5cf2698";export default f;
