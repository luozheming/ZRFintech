import{L as e}from"./index.fe367ee4.js";import{n as t,aq as o,ar as r,e as a,j as n,aT as s,g as i,k as l,v as c,bq as d,bo as u,dz as f,dA as p,r as m,o as b,m as j,E as v,aN as g,bF as y}from"./index.07990433.js";import{D as h,G as w,S as C}from"./siteSetting.c485f07c.js";import"./RedoOutlined.fd8cf209.js";import"./_baseIteratee.683af660.js";import"./get.a1614c7c.js";import"./useSortable.a7af846f.js";import"./FullscreenOutlined.c95c55ff.js";import"./index.9553f2a0.js";import"./useWindowSizeFn.ca89b1f4.js";import"./vendor.afa0338c.js";import"./usePageContext.beec4946.js";import"./index.714b5199.js";import"./UpOutlined.d7cd4b3d.js";var F={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M511.6 76.3C264.3 76.2 64 276.4 64 523.5 64 718.9 189.3 885 363.8 946c23.5 5.9 19.9-10.8 19.9-22.2v-77.5c-135.7 15.9-141.2-73.9-150.3-88.9C215 726 171.5 718 184.5 703c30.9-15.9 62.4 4 98.9 57.9 26.4 39.1 77.9 32.5 104 26 5.7-23.5 17.9-44.5 34.7-60.8-140.6-25.2-199.2-111-199.2-213 0-49.5 16.3-95 48.3-131.7-20.4-60.5 1.9-112.3 4.9-120 58.1-5.2 118.5 41.6 123.2 45.3 33-8.9 70.7-13.6 112.9-13.6 42.4 0 80.2 4.9 113.5 13.9 11.3-8.6 67.3-48.8 121.3-43.9 2.9 7.7 24.7 58.3 5.5 118 32.4 36.8 48.9 82.7 48.9 132.3 0 102.2-59 188.1-200 212.9a127.5 127.5 0 0138.1 91v112.5c.8 9 0 17.9 15 17.9 177.1-59.7 304.6-227 304.6-424.1 0-247.2-200.4-447.3-447.5-447.3z"}}]},name:"github",theme:"filled"};function O(e,t,o){return t in e?Object.defineProperty(e,t,{value:o,enumerable:!0,configurable:!0,writable:!0}):e[t]=o,e}var _=function(e,a){var n=function(e){for(var t=1;t<arguments.length;t++){var o=null!=arguments[t]?Object(arguments[t]):{},r=Object.keys(o);"function"==typeof Object.getOwnPropertySymbols&&(r=r.concat(Object.getOwnPropertySymbols(o).filter((function(e){return Object.getOwnPropertyDescriptor(o,e).enumerable})))),r.forEach((function(t){O(e,t,o[t])}))}return e}({},e,a.attrs);return t(r,o(n,{icon:F}),null)};_.displayName="GithubFilled",_.inheritAttrs=!1;var x=a({name:"LayoutFooter",components:{Footer:e.Footer,GithubFilled:_},setup(){const{t:e}=n(),{getShowFooter:t}=u(),{currentRoute:o}=s(),{prefixCls:r}=i("layout-footer");return{getShowLayoutFooter:l((()=>{var e;return c(t)&&!(null==(e=c(o).meta)?void 0:e.hiddenFooter)})),prefixCls:r,t:e,DOC_URL:h,GITHUB_URL:w,SITE_URL:C,openWindow:d}}});const S=y("data-v-183dcd01");f("data-v-183dcd01");const L=t("div",null,"Copyright ©2020 Vben Admin",-1);p();const U=S(((e,o,r,a,n,s)=>{const i=m("GithubFilled"),l=m("Footer");return e.getShowLayoutFooter?(b(),j(l,{key:0,class:e.prefixCls},{default:S((()=>[t("div",{class:`${e.prefixCls}__links`},[t("a",{onClick:o[1]||(o[1]=t=>e.openWindow(e.SITE_URL))},v(e.t("layout.footer.onlinePreview")),1),t(i,{onClick:o[2]||(o[2]=t=>e.openWindow(e.GITHUB_URL)),class:`${e.prefixCls}__github`},null,8,["class"]),t("a",{onClick:o[3]||(o[3]=t=>e.openWindow(e.DOC_URL))},v(e.t("layout.footer.onlineDocument")),1)],2),L])),_:1},8,["class"])):g("",!0)}));x.render=U,x.__scopeId="data-v-183dcd01";export default x;
