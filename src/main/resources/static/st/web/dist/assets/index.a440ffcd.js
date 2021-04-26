import{e as r,a7 as e,R as t,S as n,Y as o,n as a,bT as i,cs as s,a4 as c,U as u,aO as p,a0 as l,bV as f,af as d,O as m}from "./index.2da64865.js";var b=r({name:"ABreadcrumbItem",__ANT_BREADCRUMB_ITEM:!0,props:{prefixCls:e.string,href:e.string,separator:e.VNodeChild.def("/"),overlay:e.VNodeChild},setup:function(){return{configProvider:t("configProvider",n)}},methods:{renderBreadcrumbNode:function(r, e){var t=o(this,"overlay");return t?a(s,{overlay:t,placement:"bottomCenter"},{default:function(){return[a("span",{class:"".concat(e,"-overlay-link")},[r,a(i,null,null)])]}}):r}},render:function(){var r,e=this.prefixCls,t=(0,this.configProvider.getPrefixCls)("breadcrumb",e),n=o(this,"separator"),i=c(this);return r=u(this,"href")?a("a",{class:"".concat(t,"-link")},[i]):a("span",{class:"".concat(t,"-link")},[i]),r=this.renderBreadcrumbNode(r,t),i?a("span",null,[r,n&&""!==n&&a("span",{class:"".concat(t,"-separator")},[n])]):null}});function h(r){return(h="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(r){return typeof r}:function(r){return r&&"function"==typeof Symbol&&r.constructor===Symbol&&r!==Symbol.prototype?"symbol":typeof r})(r)}function y(r){return function(r){if(Array.isArray(r))return v(r)}(r)||function(r){if("undefined"!=typeof Symbol&&Symbol.iterator in Object(r))return Array.from(r)}(r)||function(r, e){if(!r)return;if("string"==typeof r)return v(r,e);var t=Object.prototype.toString.call(r).slice(8,-1);"Object"===t&&r.constructor&&(t=r.constructor.name);if("Map"===t||"Set"===t)return Array.from(r);if("Arguments"===t||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(t))return v(r,e)}(r)||function(){throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function v(r, e){(null==e||e>r.length)&&(e=r.length);for(var t=0,n=new Array(e); t<e; t++)n[t]=r[t];return n}function g(r){return"function"==typeof r||"[object Object]"===Object.prototype.toString.call(r)&&!l(r)}function O(r){var e=r.route,t=r.params,n=r.routes,o=r.paths,i=n.indexOf(e)===n.length-1,s=function(r, e){if(!r.breadcrumbName)return null;var t=Object.keys(e).join("|");return r.breadcrumbName.replace(new RegExp(":(".concat(t,")"),"g"),(function(r, t){return e[t]||r}))}(e,t);return i?a("span",null,[s]):a("a",{href:"#/".concat(o.join("/"))},[s])}var P=r({name:"ABreadcrumb",props:{prefixCls:e.string,routes:{type:Array},params:e.any,separator:e.VNodeChild,itemRender:{type:Function}},setup:function(){return{configProvider:t("configProvider",n)}},methods:{getPath:function(r, e){return r=(r||"").replace(/^\//,""),Object.keys(e).forEach((function(t){r=r.replace(":".concat(t),e[t])})),r},addChildPath:function(r){var e=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"",t=arguments.length>2?arguments[2]:void 0,n=y(r),o=this.getPath(e,t);return o&&n.push(o),n},genForRoutes:function(r){var e=this,t=r.routes,n=void 0===t?[]:t,o=r.params,i=void 0===o?{}:o,s=r.separator,c=r.itemRender,u=void 0===c?O:c,l=[];return n.map((function(r){var t,o=e.getPath(r.path,i);o&&l.push(o);var c,f=[].concat(l),d=null;r.children&&r.children.length&&(d=a(p,null,g(c=r.children.map((function(r){var t;return a(p.Item,{key:r.path||r.breadcrumbName},g(t=u({route:r,params:i,routes:n,paths:e.addChildPath(f,r.path,i)}))?t:{default:function(){return[t]}})})))?c:{default:function(){return[c]}}));return a(b,{overlay:d,separator:s,key:o||r.breadcrumbName},g(t=u({route:r,params:i,routes:n,paths:f}))?t:{default:function(){return[t]}})}))}},render:function(){var r,e=this.prefixCls,t=this.routes,n=this.params,i=void 0===n?{}:n,s=this.$slots,u=(0,this.configProvider.getPrefixCls)("breadcrumb",e),p=f(c(this)),l=o(this,"separator"),b=this.itemRender||s.itemRender||O;return t&&t.length>0?r=this.genForRoutes({routes:t,params:i,separator:l,itemRender:b}):p.length&&(r=p.map((function(r, e){return d("object"===h(r.type)&&(r.type.__ANT_BREADCRUMB_ITEM||r.type.__ANT_BREADCRUMB_SEPARATOR),"Breadcrumb","Only accepts Breadcrumb.Item and Breadcrumb.Separator as it's children"),m(r,{separator:l,key:e})}))),a("div",{class:u},[r])}});function j(r, e){var t=Object.keys(r);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(r);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable}))),t.push.apply(t,n)}return t}function A(r, e, t){return e in r?Object.defineProperty(r,e,{value:t,enumerable:!0,configurable:!0,writable:!0}):r[e]=t,r}var R=r({name:"ABreadcrumbSeparator",__ANT_BREADCRUMB_SEPARATOR:!0,inheritAttrs:!1,props:{prefixCls:e.string},setup:function(){return{configProvider:t("configProvider",n)}},render:function(){var r=this.prefixCls,e=this.$attrs;e.separator;var t=e.class,n=function(r, e){var t={};for(var n in r)Object.prototype.hasOwnProperty.call(r,n)&&e.indexOf(n)<0&&(t[n]=r[n]);if(null!=r&&"function"==typeof Object.getOwnPropertySymbols){var o=0;for(n=Object.getOwnPropertySymbols(r); o<n.length; o++)e.indexOf(n[o])<0&&Object.prototype.propertyIsEnumerable.call(r,n[o])&&(t[n[o]]=r[n[o]])}return t}(e,["separator","class"]),o=(0,this.configProvider.getPrefixCls)("breadcrumb",r),i=c(this);return a("span",function(r){for(var e=1; e<arguments.length; e++){var t=null!=arguments[e]?arguments[e]:{};e%2?j(Object(t),!0).forEach((function(e){A(r,e,t[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(r,Object.getOwnPropertyDescriptors(t)):j(Object(t)).forEach((function(e){Object.defineProperty(r,e,Object.getOwnPropertyDescriptor(t,e))}))}return r}({class:["".concat(o,"-separator"),t]},n),[i.length>0?i:"/"])}});P.Item=b,P.Separator=R,P.install=function(r){return r.component(P.name,P),r.component(b.name,b),r.component(R.name,R),r};export{P as B};
