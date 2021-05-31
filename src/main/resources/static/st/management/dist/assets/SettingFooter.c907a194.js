import{n as e,ar as t,as as a,e as o,g as n,j as r,ev as s,w as c,bp as l,bG as i,ek as u,el as d,bc as m,br as p,x as f,bi as b,z as g,r as y,o as v,m as C,E as h,F as O,bz as S}from"./index.e3ab3d33.js";import{R as j}from"./RedoOutlined.2b7b81e2.js";import"./vendor.afa0338c.js";import"./_baseIteratee.3a82d812.js";import"./get.92310d4b.js";var R={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M832 64H296c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h496v688c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V96c0-17.7-14.3-32-32-32zM704 192H192c-17.7 0-32 14.3-32 32v530.7c0 8.5 3.4 16.6 9.4 22.6l173.3 173.3c2.2 2.2 4.7 4 7.4 5.5v1.9h4.2c3.5 1.3 7.2 2 11 2H704c17.7 0 32-14.3 32-32V224c0-17.7-14.3-32-32-32zM350 856.2L263.9 770H350v86.2zM664 888H414V746c0-22.1-17.9-40-40-40H232V264h432v624z"}}]},name:"copy",theme:"outlined"};function k(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}var x=function(o,n){var r=function(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?Object(arguments[t]):{},o=Object.keys(a);"function"==typeof Object.getOwnPropertySymbols&&(o=o.concat(Object.getOwnPropertySymbols(a).filter((function(e){return Object.getOwnPropertyDescriptor(a,e).enumerable})))),o.forEach((function(t){k(e,t,a[t])}))}return e}({},o,n.attrs);return e(a,t(r,{icon:R}),null)};x.displayName="CopyOutlined",x.inheritAttrs=!1;var w=o({name:"SettingFooter",components:{CopyOutlined:x,RedoOutlined:j},setup(){const{getRootSetting:e}=b(),{prefixCls:t}=n("setting-footer"),{t:a}=r(),{createSuccessModal:o,createMessage:y}=g();return{prefixCls:t,t:a,handleCopy:function(){const{isSuccessRef:t}=s(JSON.stringify(c(e),null,2));c(t)&&o({title:a("layout.setting.operatingTitle"),content:a("layout.setting.operatingContent")})},handleResetSetting:function(){try{l.commitProjectConfigState(i);const{colorWeak:e,grayMode:t}=i;u(e),d(t),y.success(a("layout.setting.resetSuccess"))}catch(e){y.error(e)}},handleClearAndRedo:function(){localStorage.clear(),l.resumeAllState(),m.commitResetState(),p.commitResetState(),f.commitResetState(),location.reload()}}}});const M=S("data-v-68a367be"),z=M(((t,a,o,n,r,s)=>{const c=y("CopyOutlined"),l=y("a-button"),i=y("RedoOutlined");return v(),C("div",{class:t.prefixCls},[e(l,{type:"primary",block:"",onClick:t.handleCopy},{default:M((()=>[e(c,{class:"mr-2"}),h(" "+O(t.t("layout.setting.copyBtn")),1)])),_:1},8,["onClick"]),e(l,{color:"warning",block:"",onClick:t.handleResetSetting,class:"my-3"},{default:M((()=>[e(i,{class:"mr-2"}),h(" "+O(t.t("common.resetText")),1)])),_:1},8,["onClick"]),e(l,{color:"error",block:"",onClick:t.handleClearAndRedo},{default:M((()=>[e(i,{class:"mr-2"}),h(" "+O(t.t("layout.setting.clearBtn")),1)])),_:1},8,["onClick"])],2)}));w.render=z,w.__scopeId="data-v-68a367be";export default w;
