import{q as t,dy as n,a$ as e,k as a,ae as u,b1 as s,p as o,v as r,bX as l,e as i,B as c,aI as d,j as v,aY as f,r as p,o as m,m as C,C as S,D as b,E as g,aq as h,I as x,g as y,n as w}from"./index.924fa4e6.js";function z(o,r="value",l="change"){const i=s(),c=null==i?void 0:i.emit,d=t({value:o[r]}),v=n(d);e((()=>{d.value=o[r]}));return[a({get:()=>d.value,set(t){u(t,v.value)||(d.value=t,null==c||c(l,t))}}),t=>{d.value=t},v]}var B=i({name:"CountButton",components:{Button:c},props:{value:d.string,count:d.number.def(60),beforeStartFunc:{type:Function,default:null}},setup(t){const n=o(!1),{currentCount:a,isStart:u,start:s,reset:i}=function(t){const n=o(t),e=o(!1);let a;function u(){a&&window.clearInterval(a)}function s(){e.value=!1,u(),a=null}function i(){r(e)||a||(e.value=!0,a=setInterval((()=>{1===r(n)?(s(),n.value=t):n.value-=1}),1e3))}function c(){n.value=t,s()}return l((()=>{c()})),{start:i,reset:c,restart:function(){c(),i()},clear:u,stop:s,currentCount:n,isStart:e}}(t.count),{t:c}=v();return e((()=>{void 0===t.value&&i()})),{handleStart:function(){return e=this,a=null,u=function*(){const{beforeStartFunc:e}=t;if(e&&f(e)){n.value=!0;try{(yield e())&&s()}finally{n.value=!1}}else s()},new Promise(((t,n)=>{var s=t=>{try{r(u.next(t))}catch(e){n(e)}},o=t=>{try{r(u.throw(t))}catch(e){n(e)}},r=n=>n.done?t(n.value):Promise.resolve(n.value).then(s,o);r((u=u.apply(e,a)).next())}));var e,a,u},isStart:u,currentCount:a,loading:n,t:c}}});B.render=function(t,n,e,a,u,s){const o=p("Button");return m(),C(o,h(t.$attrs,{disabled:t.isStart,onClick:t.handleStart,loading:t.loading}),{default:S((()=>[b(g(t.isStart?t.t("component.countdown.sendText",[t.currentCount]):t.t("component.countdown.normalText")),1)])),_:1},16,["disabled","onClick","loading"])};var F=i({name:"CountDownInput",components:{[x.name]:x,CountButton:B},inheritAttrs:!1,props:{value:d.string,size:d.oneOf(["default","large","small"]),count:d.number.def(60),sendCodeApi:{type:Function,default:null}},setup(t){const{prefixCls:n}=y("countdown-input"),[e]=z(t);return{prefixCls:n,state:e}}});F.render=function(t,n,e,a,u,s){const o=p("CountButton"),r=p("AInput");return m(),C(r,h(t.$attrs,{class:t.prefixCls,size:t.size,value:t.state}),{addonAfter:S((()=>[w(o,{size:t.size,count:t.count,value:t.state,beforeStartFunc:t.sendCodeApi},null,8,["size","count","value","beforeStartFunc"])])),_:1},16,["class","size","value"])};export{F as _,z as u};
