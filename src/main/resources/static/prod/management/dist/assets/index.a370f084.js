import{e,a7 as o,R as n,S as c,a2 as l,n as a,dr as i,ds as t,p as s,a$ as r,F as u,a0 as d,dq as v,aH as p}from"./index.e5737575.js";function f(e,o,n){return o in e?Object.defineProperty(e,o,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[o]=n,e}var b=e({name:"ACheckableTag",props:{prefixCls:o.string,checked:o.looseBool,onChange:{type:Function},onClick:{type:Function}},emits:["update:checked","change","click"],setup:function(e,o){var i=o.slots,t=o.emit,s=n("configProvider",c).getPrefixCls,r=function(o){var n=e.checked;t("update:checked",!n),t("change",!n),t("click",o)};return function(){var o,n,c=e.checked,t=e.prefixCls,u=s("tag",t),d=l(u,(f(o={},"".concat(u,"-checkable"),!0),f(o,"".concat(u,"-checkable-checked"),c),o));return a("span",{class:d,onClick:r},[null===(n=i.default)||void 0===n?void 0:n.call(i)])}}});function g(e,o,n){return o in e?Object.defineProperty(e,o,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[o]=n,e}var k=new RegExp("^(".concat(i.join("|"),")(-inverse)?$")),C=new RegExp("^(".concat(t.join("|"),")$")),h={prefixCls:o.string,color:{type:String},closable:o.looseBool.def(!1),closeIcon:o.VNodeChild,visible:o.looseBool,onClose:{type:Function},icon:o.VNodeChild},m=e({name:"ATag",emits:["update:visible","close"],setup:function(e,o){var i=o.slots,t=o.emit,f=o.attrs,b=n("configProvider",c).getPrefixCls,h=s(!0);r((function(){void 0!==e.visible&&(h.value=e.visible)}));var m=function(o){o.stopPropagation(),t("update:visible",!1),t("close",o),o.defaultPrevented||void 0===e.visible&&(h.value=!1)},x=function(){var o=e.color;return!!o&&(k.test(o)||C.test(o))};return function(){var o,n,c,t,s,r=e.prefixCls,k=e.icon,C=void 0===k?null===(n=i.icon)||void 0===n?void 0:n.call(i):k,y=e.color,j=e.closeIcon,P=void 0===j?null===(c=i.closeIcon)||void 0===c?void 0:c.call(i):j,w=e.closable,F=void 0!==w&&w,O=x(),T=b("tag",r),B={backgroundColor:y&&!x()?y:void 0},I=l(T,(g(o={},"".concat(T,"-").concat(y),O),g(o,"".concat(T,"-has-color"),y&&!O),g(o,"".concat(T,"-hidden"),!h.value),o)),R=C||null,S=null===(t=i.default)||void 0===t?void 0:t.call(i),$=R?a(u,null,[R,a("span",null,[S])]):S,A="onClick"in f,E=a("span",{class:I,style:B},[$,F?P?a("div",{class:"".concat(T,"-close-icon"),onClick:m},[P]):a(p,{class:"".concat(T,"-close-icon"),onClick:m},null):null]);return A?a(v,null,"function"==typeof(s=E)||"[object Object]"===Object.prototype.toString.call(s)&&!d(s)?E:{default:function(){return[E]}}):E}}});m.props=h,m.CheckableTag=b,m.install=function(e){return e.component(m.name,m),e.component(b.name,b),e};export{m as T};
