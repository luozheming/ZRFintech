import{e as t,Q as n,a7 as e,a8 as a,R as i,S as o,a0 as r,d7 as c,n as l,cg as s,a4 as d,Y as u}from"./index.e5737575.js";import{T as f}from"./index.fcfaec8d.js";import{R as v,C as g}from"./index.57a6dbfc.js";function p(t,n,e){return n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t}var b=f.TabPane,h=t({name:"ACard",mixins:[n],props:{prefixCls:e.string,title:e.VNodeChild,extra:e.VNodeChild,bordered:e.looseBool.def(!0),bodyStyle:e.style,headStyle:e.style,loading:e.looseBool.def(!1),hoverable:e.looseBool.def(!1),type:e.string,size:e.oneOf(a("default","small")),actions:e.VNodeChild,tabList:{type:Array},tabBarExtraContent:e.VNodeChild,activeTabKey:e.string,defaultActiveTabKey:e.string,cover:e.VNodeChild,onTabChange:{type:Function}},setup:function(){return{configProvider:i("configProvider",o)}},data:function(){return{widerPadding:!1}},methods:{getAction:function(t){return t.map((function(n,e){return r(n)&&!c(n)||!r(n)?l("li",{style:{width:"".concat(100/t.length,"%")},key:"action-".concat(e)},[l("span",null,[n])]):null}))},triggerTabChange:function(t){this.$emit("tabChange",t)},isContainGrid:function(){var t,n=arguments.length>0&&void 0!==arguments[0]?arguments[0]:[];return n.forEach((function(n){n&&s(n.type)&&n.type.__ANT_CARD_GRID&&(t=!0)})),t}},render:function(){var t,n,e,a,i,o=this.$props,c=o.prefixCls,s=o.headStyle,h=void 0===s?{}:s,y=o.bodyStyle,C=void 0===y?{}:y,m=o.loading,x=o.bordered,P=void 0===x||x,k=o.size,A=void 0===k?"default":k,T=o.type,N=o.tabList,j=o.hoverable,V=o.activeTabKey,_=o.defaultActiveTabKey,B=this.$slots,R=d(this),w=(0,this.configProvider.getPrefixCls)("card",c),G=u(this,"tabBarExtraContent"),K=(p(n={},"".concat(w),!0),p(n,"".concat(w,"-loading"),m),p(n,"".concat(w,"-bordered"),P),p(n,"".concat(w,"-hoverable"),!!j),p(n,"".concat(w,"-contain-grid"),this.isContainGrid(R)),p(n,"".concat(w,"-contain-tabs"),N&&N.length),p(n,"".concat(w,"-").concat(A),"default"!==A),p(n,"".concat(w,"-type-").concat(T),!!T),n),O=0===C.padding||"0px"===C.padding?{padding:24}:void 0,S=l("div",{class:"".concat(w,"-loading-content"),style:O},[l(v,{gutter:8},{default:function(){return[l(g,{span:22},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}})]}}),l(v,{gutter:8},{default:function(){return[l(g,{span:8},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}}),l(g,{span:15},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}})]}}),l(v,{gutter:8},{default:function(){return[l(g,{span:6},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}}),l(g,{span:18},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}})]}}),l(v,{gutter:8},{default:function(){return[l(g,{span:13},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}}),l(g,{span:9},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}})]}}),l(v,{gutter:8},{default:function(){return[l(g,{span:4},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}}),l(g,{span:3},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}}),l(g,{span:16},{default:function(){return[l("div",{class:"".concat(w,"-loading-block")},null)]}})]}})]),$=void 0!==V,D=(p(e={size:"large"},$?"activeKey":"defaultActiveKey",$?V:_),p(e,"tabBarExtraContent",G),p(e,"onChange",this.triggerTabChange),p(e,"class","".concat(w,"-head-tabs")),e),E=N&&N.length?l(f,D,"function"==typeof(i=t=N.map((function(t){var n=t.tab,e=t.slots,a=null==e?void 0:e.tab,i=void 0!==n?n:B[a]?B[a](t):null;return l(b,{tab:i,key:t.key,disabled:t.disabled},null)})))||"[object Object]"===Object.prototype.toString.call(i)&&!r(i)?t:{default:function(){return[t]}}):null,z=u(this,"title"),I=u(this,"extra");(z||I||E)&&(a=l("div",{class:"".concat(w,"-head"),style:h},[l("div",{class:"".concat(w,"-head-wrapper")},[z&&l("div",{class:"".concat(w,"-head-title")},[z]),I&&l("div",{class:"".concat(w,"-extra")},[I])]),E]));var L=u(this,"cover"),M=L?l("div",{class:"".concat(w,"-cover")},[L]):null,F=l("div",{class:"".concat(w,"-body"),style:C},[m?S:R]),Q=u(this,"actions"),Y=Q&&Q.length?l("ul",{class:"".concat(w,"-actions")},[this.getAction(Q)]):null;return l("div",{class:K,ref:"cardContainerRef"},[a,M,R?F:null,Y])}});var y=t({name:"ACardMeta",props:{prefixCls:e.string,title:e.VNodeChild,description:e.VNodeChild,avatar:e.VNodeChild},setup:function(){return{configProvider:i("configProvider",o)}},render:function(){var t,n,e,a=this.$props.prefixCls,i=(0,this.configProvider.getPrefixCls)("card",a),o=(t={},n="".concat(i,"-meta"),e=!0,n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t),r=u(this,"avatar"),c=u(this,"title"),s=u(this,"description"),d=r?l("div",{class:"".concat(i,"-meta-avatar")},[r]):null,f=c?l("div",{class:"".concat(i,"-meta-title")},[c]):null,v=s?l("div",{class:"".concat(i,"-meta-description")},[s]):null,g=f||v?l("div",{class:"".concat(i,"-meta-detail")},[f,v]):null;return l("div",{class:o},[d,g])}});function C(t,n,e){return n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t}var m=t({name:"ACardGrid",__ANT_CARD_GRID:!0,props:{prefixCls:e.string,hoverable:e.looseBool},setup:function(){return{configProvider:i("configProvider",o)}},render:function(){var t,n=this.$props,e=n.prefixCls,a=n.hoverable,i=void 0===a||a,o=(0,this.configProvider.getPrefixCls)("card",e),r=(C(t={},"".concat(o,"-grid"),!0),C(t,"".concat(o,"-grid-hoverable"),i),t);return l("div",{class:r},[d(this)])}});h.Meta=y,h.Grid=m,h.install=function(t){return t.component(h.name,h),t.component(y.name,y),t.component(m.name,m),t};export{h as C};