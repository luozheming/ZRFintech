import{e,cy as t,g as s,k as a,r as i,o as n,m as d,n as r,E as l,aq as o,bF as p}from"./index.aa79050f.js";import{b as f}from"./index.c9a832cf.js";import"./vendor.afa0338c.js";import"./index.9dee77c2.js";import"./RedoOutlined.318a41fc.js";import"./_baseIteratee.07fb58d9.js";import"./get.f1b5c461.js";import"./useSortable.01481f7c.js";import"./FullscreenOutlined.0bd6e6a7.js";import"./index.95354c08.js";import"./useWindowSizeFn.72221960.js";import"./index.e6f818f2.js";import"./ArrowLeftOutlined.da8f47d2.js";import"./index.caf9d56d.js";var c=e({name:"SelectItem",components:{Select:t},props:{event:{type:Number,default:()=>{}},disabled:{type:Boolean},title:{type:String},def:{type:[String,Number]},initValue:{type:[String,Number]},options:{type:Array,default:[]}},setup(e){const{prefixCls:t}=s("setting-select-item");return{prefixCls:t,handleChange:function(t){e.event&&f(e.event,t)},getBindValue:a((()=>e.def?{value:e.def,defaultValue:e.initValue||e.def}:{}))}}});const m=p("data-v-e2504774")(((e,t,s,a,p,f)=>{const c=i("Select");return n(),d("div",{class:e.prefixCls},[r("span",null,l(e.title),1),r(c,o(e.getBindValue,{class:`${e.prefixCls}-select`,onChange:e.handleChange,disabled:e.disabled,size:"small",options:e.options}),null,16,["class","onChange","disabled","options"])],2)}));c.render=m,c.__scopeId="data-v-e2504774";export default c;
