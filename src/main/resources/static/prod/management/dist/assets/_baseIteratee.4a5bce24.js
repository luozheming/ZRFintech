import{ai as n,aj as r,i as t,J as u,c as e,t as a,ak as i,a as o,al as f,am as l,an as c,ao as s,ap as v}from"./index.e5737575.js";import{g as d}from"./get.d713ad9e.js";function g(n){return n==n&&!t(n)}function j(n,r){return function(t){return null!=t&&(t[n]===r&&(void 0!==r||n in Object(t)))}}function b(t){var e=function(n){for(var r=u(n),t=r.length;t--;){var e=r[t],a=n[e];r[t]=[e,a,g(a)]}return r}(t);return 1==e.length&&e[0][2]?j(e[0][0],e[0][1]):function(u){return u===t||function(t,u,e,a){var i=e.length,o=i,f=!a;if(null==t)return!o;for(t=Object(t);i--;){var l=e[i];if(f&&l[2]?l[1]!==t[l[0]]:!(l[0]in t))return!1}for(;++i<o;){var c=(l=e[i])[0],s=t[c],v=l[1];if(f&&l[2]){if(void 0===s&&!(c in t))return!1}else{var d=new n;if(a)var g=a(s,v,c,t,u,d);if(!(void 0===g?r(v,s,3,a,d):g))return!1}}return!0}(u,t,e)}}function p(n,r){return null!=n&&r in Object(n)}function h(n,r){return null!=n&&function(n,r,t){for(var u=-1,c=(r=e(r,n)).length,s=!1;++u<c;){var v=a(r[u]);if(!(s=null!=n&&t(n,v)))break;n=n[v]}return s||++u!=c?s:!!(c=null==n?0:n.length)&&i(c)&&o(v,c)&&(f(n)||l(n))}(n,r,p)}function m(n){return c(n)?(r=a(n),function(n){return null==n?void 0:n[r]}):function(n){return function(r){return s(r,n)}}(n);var r}function O(n){return"function"==typeof n?n:null==n?v:"object"==typeof n?f(n)?(t=n[0],u=n[1],c(t)&&g(u)?j(a(t),u):function(n){var e=d(n,t);return void 0===e&&e===u?h(n,t):r(u,e,3)}):b(n):m(n);var t,u}export{O as b};
