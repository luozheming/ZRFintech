import{i as t,c as r,t as n,a as o,b as e}from"./index.22b3842c.js";function s(s,a,c){return null==s?s:function(s,a,c,i){if(!t(s))return s;for(var u=-1,f=(a=r(a,s)).length,l=f-1,p=s;null!=p&&++u<f;){var d=n(a[u]),v=c;if("__proto__"===d||"constructor"===d||"prototype"===d)return s;if(u!=l){var b=p[d];void 0===(v=i?i(b,d,p):void 0)&&(v=t(b)?b:o(a[u+1])?[]:{})}e(p,d,v),p=p[d]}return s}(s,a,c)}function a(t,r="lang"){const n={};return Object.keys(t).forEach((o=>{const e=t[o].default;let a=o.replace(`./${r}/`,"").replace(/^\.\//,"");const c=a.lastIndexOf(".");a=a.substring(0,c);const i=a.split("/"),u=i.shift(),f=i.join(".");u&&(f?(s(n,u,n[u]||{}),s(n[u],f,e)):s(n,u,e||{}))})),n}export{a as g};
