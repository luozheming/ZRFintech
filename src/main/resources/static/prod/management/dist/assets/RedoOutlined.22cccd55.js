import{b as e}from"./_baseIteratee.2a793644.js";import{b_ as t,b$ as r,c0 as n,c1 as a,c2 as c,c3 as s,n as o,aq as i,ar as u}from"./index.22b3842c.js";var l=t&&1/r(new t([,-0]))[1]==1/0?function(e){return new t(e)}:function(){};function f(e,t,o){var i=-1,u=a,f=e.length,b=!0,p=[],h=p;if(o)b=!1,u=c;else if(f>=200){var v=t?null:l(e);if(v)return r(v);b=!1,u=s,h=new n}else h=t?[]:p;e:for(;++i<f;){var g=e[i],m=t?t(g):g;if(g=o||0!==g?g:0,b&&m==m){for(var O=h.length;O--;)if(h[O]===m)continue e;t&&h.push(m),p.push(g)}else u(h,m,o)||(h!==p&&h.push(m),p.push(g))}return p}function b(t,r){return t&&t.length?f(t,e(r)):[]}var p={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M758.2 839.1C851.8 765.9 912 651.9 912 523.9 912 303 733.5 124.3 512.6 124 291.4 123.7 112 302.8 112 523.9c0 125.2 57.5 236.9 147.6 310.2 3.5 2.8 8.6 2.2 11.4-1.3l39.4-50.5c2.7-3.4 2.1-8.3-1.2-11.1-8.1-6.6-15.9-13.7-23.4-21.2a318.64 318.64 0 01-68.6-101.7C200.4 609 192 567.1 192 523.9s8.4-85.1 25.1-124.5c16.1-38.1 39.2-72.3 68.6-101.7 29.4-29.4 63.6-52.5 101.7-68.6C426.9 212.4 468.8 204 512 204s85.1 8.4 124.5 25.1c38.1 16.1 72.3 39.2 101.7 68.6 29.4 29.4 52.5 63.6 68.6 101.7 16.7 39.4 25.1 81.3 25.1 124.5s-8.4 85.1-25.1 124.5a318.64 318.64 0 01-68.6 101.7c-9.3 9.3-19.1 18-29.3 26L668.2 724a8 8 0 00-14.1 3l-39.6 162.2c-1.2 5 2.6 9.9 7.7 9.9l167 .8c6.7 0 10.5-7.7 6.3-12.9l-37.3-47.9z"}}]},name:"redo",theme:"outlined"};function h(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}var v=function(e,t){var r=function(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?Object(arguments[t]):{},n=Object.keys(r);"function"==typeof Object.getOwnPropertySymbols&&(n=n.concat(Object.getOwnPropertySymbols(r).filter((function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable})))),n.forEach((function(t){h(e,t,r[t])}))}return e}({},e,t.attrs);return o(u,i(r,{icon:p}),null)};v.displayName="RedoOutlined",v.inheritAttrs=!1;export{v as R,f as b,b as u};