import{a7 as t,G as e,e as n,Q as r,R as o,S as i,X as s,ck as a,a2 as l,n as c,$ as u,a0 as f,a1 as g,bd as d,be as p,g as v,k as h,v as m,bt as b,bo as T,bu as k,r as S,o as y,m as w,aN as x,F as O}from"./index.924fa4e6.js";import{s as C,g as j}from"./scrollTo.c1e0f75f.js";import"./vendor.afa0338c.js";function E(){return(E=Object.assign||function(t){for(var e=1;e<arguments.length;e++){var n=arguments[e];for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&(t[r]=n[r])}return t}).apply(this,arguments)}function P(){return window}var _=n({name:"ABackTop",mixins:[r],inheritAttrs:!1,props:E(E({},{visibilityHeight:t.number,target:t.func,prefixCls:t.string,onClick:t.func}),{visibilityHeight:t.number.def(400)}),emits:["click"],setup:function(){return{configProvider:o("configProvider",i)}},data:function(){return{visible:!1,scrollEvent:null}},mounted:function(){var t=this;s((function(){var e=t.target||P;t.scrollEvent=a(e(),"scroll",t.handleScroll),t.handleScroll()}))},activated:function(){var t=this;s((function(){t.handleScroll()}))},beforeUnmount:function(){this.scrollEvent&&this.scrollEvent.remove()},methods:{getCurrentScrollTop:function(){var t=(this.target||P)();return t===window?window.pageYOffset||document.body.scrollTop||document.documentElement.scrollTop:t.scrollTop},scrollToTop:function(t){var e=this.target;C(0,{getContainer:void 0===e?P:e}),this.$emit("click",t)},handleScroll:function(){var t=this.visibilityHeight,e=this.target,n=j((void 0===e?P:e)(),!0);this.setState({visible:n>t})}},render:function(){var t,e,n=this.prefixCls,r=this.$slots,o=(0,this.configProvider.getPrefixCls)("back-top",n),i=l(o,this.$attrs.class),s=c("div",{class:"".concat(o,"-content")},[c("div",{class:"".concat(o,"-icon")},null)]),a=E(E({},this.$attrs),{onClick:this.scrollToTop,class:i}),d=this.visible?c("div",a,[(null===(t=r.default)||void 0===t?void 0:t.call(r))||s]):null,p=u("fade");return c(g,p,"function"==typeof(e=d)||"[object Object]"===Object.prototype.toString.call(e)&&!f(e)?d:{default:function(){return[d]}})}}),B=n({name:"LayoutFeatures",components:{BackTop:e(_),LayoutLockPage:d((()=>p((()=>__import__("./index.0f320a94.js")),["./assets/index.0f320a94.js","./assets/LockPage.12693638.js","./assets/LockPage.4e79694f.css","./assets/index.924fa4e6.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/header.d801b988.js"]))),SettingDrawer:d((()=>p((()=>__import__("./index.886d1b01.js").then((function(t){return t.i}))),["./assets/index.886d1b01.js","./assets/index.6b2f87f3.css","./assets/index.01c17792.js","./assets/index.693c4e12.css","./assets/index.924fa4e6.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/RedoOutlined.ac536275.js","./assets/_baseIteratee.aa449b77.js","./assets/get.2ee4fe2a.js","./assets/useSortable.25d75773.js","./assets/FullscreenOutlined.4f92282e.js","./assets/index.3c1da694.js","./assets/index.a9fdc7b4.css","./assets/useWindowSizeFn.ac7612c4.js","./assets/index.e32b760b.js","./assets/index.aad873ea.css","./assets/ArrowLeftOutlined.67ee00a4.js","./assets/index.d270335e.js","./assets/index.2b38113f.css"])))},setup(){const{getUseOpenBackTop:t,getShowSettingButton:e,getSettingButtonPosition:n,getFullContent:r}=T(),{prefixCls:o}=v("setting-drawer-fearure"),{getShowHeader:i}=k();return{getTarget:()=>document.body,getUseOpenBackTop:t,getIsFixedSettingDrawer:h((()=>{if(!m(e))return!1;const t=m(n);return t===b.AUTO?!m(i)||m(r):t===b.FIXED})),prefixCls:o}}});B.render=function(t,e,n,r,o,i){const s=S("LayoutLockPage"),a=S("BackTop"),l=S("SettingDrawer");return y(),w(O,null,[c(s),t.getUseOpenBackTop?(y(),w(a,{key:0,target:t.getTarget},null,8,["target"])):x("",!0),t.getIsFixedSettingDrawer?(y(),w(l,{key:1,class:t.prefixCls},null,8,["class"])):x("",!0)],64)};export default B;
