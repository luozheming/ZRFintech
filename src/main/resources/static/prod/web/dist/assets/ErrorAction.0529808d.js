import{B as t}from"./index.1c58f7d5.js";import{e as o,aM as e,b3 as r,j as a,aT as n,k as s,dM as i,av as u,r as d,o as c,m as l,C as m,n as p}from"./index.924fa4e6.js";import"./index.01c17792.js";import"./RedoOutlined.ac536275.js";import"./_baseIteratee.aa449b77.js";import"./get.2ee4fe2a.js";import"./useSortable.25d75773.js";import"./FullscreenOutlined.4f92282e.js";import"./index.3c1da694.js";import"./useWindowSizeFn.ac7612c4.js";import"./vendor.afa0338c.js";import"./index.e32b760b.js";var f=o({name:"ErrorAction",components:{Icon:e,Tooltip:r,Badge:t},setup(){const{t:t}=a(),{push:o}=n();return{t:t,getCount:s((()=>i.getErrorListCountState)),handleToErrorList:function(){o(u.ERROR_LOG_PAGE).then((()=>{i.commitErrorListCountState(0)}))}}}});f.render=function(t,o,e,r,a,n){const s=d("Icon"),i=d("Badge"),u=d("Tooltip");return c(),l(u,{title:t.t("layout.header.tooltipErrorLog"),placement:"bottom",mouseEnterDelay:.5,onClick:t.handleToErrorList},{default:m((()=>[p(i,{count:t.getCount,offset:[0,10],dot:"",overflowCount:99},{default:m((()=>[p(s,{icon:"ion:bug-outline"})])),_:1},8,["count"])])),_:1},8,["title","mouseEnterDelay","onClick"])};export default f;
