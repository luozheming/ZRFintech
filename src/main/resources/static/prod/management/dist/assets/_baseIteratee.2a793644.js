import{ai as n,aj as r,i as t,J as u,c as e,t as a,ak as i,a as o,al as f,am as c,an as l,ao as s,ap as v}from"./index.22b3842c.js";import{g as b}from"./get.29e7fdfb.js";function d(n){return n==n&&!t(n)}function g(n,r){return function(t){return null!=t&&(t[n]===r&&(void 0!==r||n in Object(t)))}}function j(t){var e=function(n){for(var r=u(n),t=r.length;t--;){var e=r[t],a=n[e];r[t]=[e,a,d(a)]}return r}(t);return 1==e.length&&e[0][2]?g(e[0][0],e[0][1]):function(u){return u===t||function(t,u,e,a){var i=e.length,o=i,f=!a;if(null==t)return!o;for(t=Object(t);i--;){var c=e[i];if(f&&c[2]?c[1]!==t[c[0]]:!(c[0]in t))return!1}for(;++i<o;){var l=(c=e[i])[0],s=t[l],v=c[1];if(f&&c[2]){if(void 0===s&&!(l in t))return!1}else{var b=new n;if(a)var d=a(s,v,l,t,u,b);if(!(void 0===d?r(v,s,3,a,b):d))return!1}}return!0}(u,t,e)}}function p(n,r){return null!=n&&r in Object(n)}function h(n,r){return null!=n&&function(n,r,t){for(var u=-1,l=(r=e(r,n)).length,s=!1;++u<l;){var v=a(r[u]);if(!(s=null!=n&&t(n,v)))break;n=n[v]}return s||++u!=l?s:!!(l=null==n?0:n.length)&&i(l)&&o(v,l)&&(f(n)||c(n))}(n,r,p)}function m(n){return l(n)?(r=a(n),function(n){return null==n?void 0:n[r]}):function(n){return function(r){return s(r,n)}}(n);var r}function O(n){return"function"==typeof n?n:null==n?v:"object"==typeof n?f(n)?(t=n[0],u=n[1],l(t)&&d(u)?g(a(t),u):function(n){var e=b(n,t);return void 0===e&&e===u?h(n,t):r(u,e,3)}):j(n):m(n);var t,u}export{O as b};
