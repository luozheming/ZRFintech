var e=Object.defineProperty,l=Object.prototype.hasOwnProperty,n=Object.getOwnPropertySymbols,t=Object.prototype.propertyIsEnumerable,o=(l,n,t)=>n in l?e(l,n,{enumerable:!0,configurable:!0,writable:!0,value:t}):l[n]=t,a=(e,a)=>{for(var r in a||(a={}))l.call(a,r)&&o(e,r,a[r]);if(n)for(var r of n(a))t.call(a,r)&&o(e,r,a[r]);return e},r=(e,l,n)=>new Promise(((t,o)=>{var a=e=>{try{s(n.next(e))}catch(l){o(l)}},r=e=>{try{s(n.throw(e))}catch(l){o(l)}},s=e=>e.done?t(e.value):Promise.resolve(e.value).then(a,r);s((n=n.apply(e,l)).next())}));import{j as s,aI as i,a$ as c,v as u,aQ as d,e as p,ax as f,df as g,n as h,du as m,a0 as b,dv as v,a_ as C,aZ as y,bn as F,p as H,k,aW as M,ag as O,cK as S,X as P,r as x,bC as w,o as T,m as j,C as W,z as $,b2 as R,aH as B,g as E,F as N,aN as A,aq as _,D as I,E as L,cH as V,bm as D,cF as q,b1 as z,aY as X,dp as Y,bG as Q,aP as G,q as K,cL as Z,x as J,ae as U,cN as ee,bQ as le}from"./index.e5737575.js";import{u as ne}from"./useWindowSizeFn.cd822dd7.js";import{F as te,a as oe}from"./FullscreenOutlined.b2c49886.js";const{t:ae}=s(),re={visible:i.bool,scrollTop:i.bool.def(!0),height:i.number,minHeight:i.number,draggable:i.bool.def(!0),centered:i.bool,cancelText:i.string.def(ae("common.cancelText")),okText:i.string.def(ae("common.okText")),closeFunc:Function},se=Object.assign({},re,{defaultFullscreen:i.bool,canFullscreen:i.bool.def(!0),wrapperFooterOffset:i.number.def(0),helpMessage:[String,Array],useWrapper:i.bool.def(!0),loading:i.bool,loadingTip:i.string,showCancelBtn:i.bool.def(!0),showOkBtn:i.bool.def(!0),wrapperProps:Object,afterClose:Function,bodyStyle:Object,closable:i.bool.def(!0),closeIcon:Object,confirmLoading:i.bool,destroyOnClose:i.bool,footer:Object,getContainer:Function,mask:i.bool.def(!0),maskClosable:i.bool.def(!0),keyboard:i.bool.def(!0),maskStyle:Object,okType:i.string.def("primary"),okButtonProps:Object,cancelButtonProps:Object,title:i.string,visible:i.bool,width:[String,Number],wrapClassName:i.string,zIndex:i.number});function ie(e){const l=(e,l)=>getComputedStyle(e)[l],n=n=>{if(!n)return;n.setAttribute("data-drag",u(e.draggable));const t=n.querySelector(".ant-modal-header"),o=n.querySelector(".ant-modal");t&&o&&u(e.draggable)&&(t.style.cursor="move",t.onmousedown=e=>{if(!e)return;const n=e.clientX,t=e.clientY,a=document.body.clientWidth,r=document.documentElement.clientHeight,s=o.offsetWidth,i=o.offsetHeight,c=o.offsetLeft,u=a-o.offsetLeft-s,d=o.offsetTop,p=r-o.offsetTop-i,f=l(o,"left"),g=l(o,"top");let h=+f,m=+g;f.includes("%")?(h=+document.body.clientWidth*(+f.replace(/%/g,"")/100),m=+document.body.clientHeight*(+g.replace(/%/g,"")/100)):(h=+f.replace(/px/g,""),m=+g.replace(/px/g,"")),document.onmousemove=function(e){let l=e.clientX-n,a=e.clientY-t;-l>c?l=-c:l>u&&(l=u),-a>d?a=-d:a>p&&(a=p),o.style.cssText+=`;left:${l+h}px;top:${a+m}px;`},document.onmouseup=()=>{document.onmousemove=null,document.onmouseup=null}})};c((()=>{u(e.visible)&&u(e.draggable)&&d((()=>{(()=>{const t=document.querySelectorAll(".ant-modal-wrap");for(const o of Array.from(t)){if(!o)continue;const t=l(o,"display"),a=o.getAttribute("data-drag");"none"!==t&&(null===a||u(e.destroyOnClose))&&n(o)}})()}),30)}))}var ce=p({name:"Modal",inheritAttrs:!1,props:se,setup(e,{slots:l}){const{visible:n,draggable:t,destroyOnClose:o}=f(e),r=g();return ie({visible:n,destroyOnClose:o,draggable:t}),()=>{let n;const t=a(a({},u(r)),e);return h(v,t,"function"==typeof(o=n=m(l))||"[object Object]"===Object.prototype.toString.call(o)&&!b(o)?n:{default:()=>[n]});var o}}});const ue=Symbol();function de(){return C(ue)}var pe=p({name:"ModalWrapper",components:{ScrollContainer:F},inheritAttrs:!1,props:{loading:i.bool,useWrapper:i.bool.def(!0),modalHeaderHeight:i.number.def(57),modalFooterHeight:i.number.def(74),minHeight:i.number.def(200),height:i.number,footerOffset:i.number.def(0),visible:i.bool,fullScreen:i.bool,loadingTip:i.string},emits:["height-change","ext-height"],setup(e,{emit:l}){const n=H(null),t=H(null),o=H(0),a=H(0);let s=0;ne(d.bind(null,!1)),y({redoModalHeight:d},ue);const i=k((()=>({minHeight:`${e.minHeight}px`,height:`${u(o)}px`})));function d(){return r(this,null,(function*(){if(!e.visible)return;const a=u(n);if(!a)return;const r=a.$el.parentElement;if(r){r.style.padding="0",yield P();try{const n=r.parentElement&&r.parentElement.parentElement;if(!n)return;const a=getComputedStyle(n).top,i=Number.parseInt(a);let c=window.innerHeight-2*i+(e.footerOffset||0)-e.modalFooterHeight-e.modalHeaderHeight;i<40&&(c-=26),yield P();const d=u(t);if(!d)return;yield P(),s=d.scrollHeight,e.fullScreen?o.value=window.innerHeight-e.modalFooterHeight-e.modalHeaderHeight-28:o.value=e.height?e.height:s>c?c:s,l("height-change",u(o))}catch(i){}}}))}return c((()=>{e.useWrapper&&d()})),M((()=>e.fullScreen),(e=>{d(),e?a.value=o.value:o.value=a.value})),O((()=>{const{modalHeaderHeight:n,modalFooterHeight:t}=e;l("ext-height",n+t)})),S((()=>{})),{wrapperRef:n,spinRef:t,spinStyle:i,scrollTop:function(){return r(this,null,(function*(){P((()=>{var e;const l=u(n);l&&(null==(e=null==l?void 0:l.scrollTo)||e.call(l,0))}))}))},setModalHeight:d}}});pe.render=function(e,l,n,t,o,a){const r=x("ScrollContainer"),s=w("loading");return T(),j(r,{ref:"wrapperRef"},{default:W((()=>[$(h("div",{ref:"spinRef",style:e.spinStyle,"loading-tip":e.loadingTip},[R(e.$slots,"default")],12,["loading-tip"]),[[s,e.loading]])])),_:3},512)};var fe=p({name:"ModalClose",components:{FullscreenExitOutlined:te,FullscreenOutlined:oe,CloseOutlined:B},props:{canFullscreen:i.bool.def(!0),fullScreen:i.bool},emits:["cancel","fullscreen"],setup(e,{emit:l}){const{prefixCls:n}=E("basic-modal-close");return{getClass:k((()=>[n,`${n}--custom`,{[`${n}--can-full`]:e.canFullscreen}])),prefixCls:n,handleCancel:function(){l("cancel")},handleFullScreen:function(e){null==e||e.stopPropagation(),null==e||e.preventDefault(),l("fullscreen")}}}});fe.render=function(e,l,n,t,o,a){const r=x("FullscreenExitOutlined"),s=x("FullscreenOutlined"),i=x("CloseOutlined");return T(),j("div",{class:e.getClass},[e.canFullscreen?(T(),j(N,{key:0},[e.fullScreen?(T(),j(r,{key:0,role:"full",onClick:e.handleFullScreen},null,8,["onClick"])):(T(),j(s,{key:1,role:"close",onClick:e.handleFullScreen},null,8,["onClick"]))],64)):A("",!0),h(i,{onClick:e.handleCancel},null,8,["onClick"])],2)};var ge=p({name:"BasicModalFooter",props:se,emits:["ok","cancel"],setup:(e,{emit:l})=>({handleOk:function(){l("ok")},handleCancel:function(){l("cancel")}})});ge.render=function(e,l,n,t,o,a){const r=x("a-button");return T(),j("div",null,[R(e.$slots,"insertFooter"),e.showCancelBtn?(T(),j(r,_({key:0},e.cancelButtonProps,{onClick:e.handleCancel}),{default:W((()=>[I(L(e.cancelText),1)])),_:1},16,["onClick"])):A("",!0),R(e.$slots,"centerFooter"),e.showOkBtn?(T(),j(r,_({key:1,type:e.okType,onClick:e.handleOk,loading:e.confirmLoading},e.okButtonProps),{default:W((()=>[I(L(e.okText),1)])),_:1},16,["type","onClick","loading"])):A("",!0),R(e.$slots,"appendFooter")])};var he=p({name:"BasicModalHeader",components:{BasicTitle:V},props:{helpMessage:{type:[String,Array]},title:i.string}});he.render=function(e,l,n,t,o,a){const r=x("BasicTitle");return T(),j(r,{helpMessage:e.helpMessage},{default:W((()=>[I(L(e.title),1)])),_:1},8,["helpMessage"])};var me=p({name:"BasicModal",components:{Modal:ce,ModalWrapper:pe,ModalClose:fe,ModalFooter:ge,ModalHeader:he},inheritAttrs:!1,props:se,emits:["visible-change","height-change","cancel","ok","register"],setup(e,{emit:l,attrs:n}){const t=H(!1),o=H(null),s=H(null),i=H(0),d={setModalProps:function(e){if(o.value=Y(u(o)||{},e),!Reflect.has(e,"visible"))return;t.value=!!e.visible},emitVisible:void 0,redoModalHeight:()=>{P((()=>{u(s)&&u(s).setModalHeight()}))}},p=z();p&&l("register",d,p.uid);const f=k((()=>a(a({},e),u(o)))),{handleFullScreen:g,getWrapClassName:h,fullScreenRef:m}=function(e){const l=H(!1);return{getWrapClassName:k((()=>{const n=u(e.wrapClassName)||"";return u(l)?`fullscreen-modal ${n} `:u(n)})),handleFullScreen:function(e){e&&e.stopPropagation(),l.value=!u(l)},fullScreenRef:l}}({modalWrapperRef:s,extHeightRef:i,wrapClassName:D(f.value,"wrapClassName")}),b=k((()=>{const e=a(a({},u(f)),{visible:u(t),title:void 0});return a(a({},e),{wrapClassName:u(h)})})),v=k((()=>{const e=a(a({},n),u(b));return u(m)?q(e,"height"):e})),C=k((()=>{if(!u(m))return u(b).height}));return c((()=>{t.value=!!e.visible,m.value=!!e.defaultFullscreen})),M((()=>u(t)),(n=>{var t;l("visible-change",n),p&&(null==(t=d.emitVisible)||t.call(d,n,p.uid)),P((()=>{e.scrollTop&&n&&u(s)&&u(s).scrollTop()}))}),{immediate:!1}),{handleCancel:function(n){return r(this,null,(function*(){if(null==n||n.stopPropagation(),e.closeFunc&&X(e.closeFunc)){const l=yield e.closeFunc();t.value=!l}else t.value=!1,l("cancel")}))},getBindValue:v,getProps:b,handleFullScreen:g,fullScreenRef:m,getMergeProps:f,handleOk:function(){l("ok")},visibleRef:t,omit:q,modalWrapperRef:s,handleExtHeight:function(e){i.value=e},handleHeightChange:function(e){l("height-change",e)},handleTitleDbClick:function(l){e.canFullscreen&&(l.stopPropagation(),g(l))},getWrapperHeight:C}}});me.render=function(e,l,n,t,o,a){const r=x("ModalClose"),s=x("ModalHeader"),i=x("ModalFooter"),c=x("ModalWrapper"),u=x("Modal");return T(),j(u,_({onCancel:e.handleCancel},e.getBindValue),Q({default:W((()=>[h(c,_({useWrapper:e.getProps.useWrapper,footerOffset:e.wrapperFooterOffset,fullScreen:e.fullScreenRef,ref:"modalWrapperRef",loading:e.getProps.loading,"loading-tip":e.getProps.loadingTip,minHeight:e.getProps.minHeight,height:e.getWrapperHeight,visible:e.visibleRef,modalFooterHeight:void 0===e.footer||e.footer?void 0:0},e.omit(e.getProps.wrapperProps,"visible","height"),{onExtHeight:e.handleExtHeight,onHeightChange:e.handleHeightChange}),{default:W((()=>[R(e.$slots,"default")])),_:3},16,["useWrapper","footerOffset","fullScreen","loading","loading-tip","minHeight","height","visible","modalFooterHeight","onExtHeight","onHeightChange"])])),_:2},[e.$slots.closeIcon?void 0:{name:"closeIcon",fn:W((()=>[h(r,{canFullscreen:e.getProps.canFullscreen,fullScreen:e.fullScreenRef,onCancel:e.handleCancel,onFullscreen:e.handleFullScreen},null,8,["canFullscreen","fullScreen","onCancel","onFullscreen"])]))},e.$slots.title?void 0:{name:"title",fn:W((()=>[h(s,{helpMessage:e.getProps.helpMessage,title:e.getMergeProps.title,onDblclick:e.handleTitleDbClick},null,8,["helpMessage","title","onDblclick"])]))},e.$slots.footer?void 0:{name:"footer",fn:W((()=>[h(i,_(e.getProps,{onOk:e.handleOk,onCancel:e.handleCancel}),Q({_:2},[G(Object.keys(e.$slots),(l=>({name:l,fn:W((n=>[R(e.$slots,l,n)]))})))]),1040,["onOk","onCancel"])]))},G(Object.keys(e.omit(e.$slots,"default")),(l=>({name:l,fn:W((n=>[R(e.$slots,l,n)]))})))]),1040,["onCancel"])};const be=K({}),ve=K({});function Ce(){const e=H(null),l=H(!1),n=H("");const t=()=>{const l=u(e);return l||ee("useModal instance is undefined!"),l};return[function(t,o){n.value=o,S((()=>{e.value=null,l.value=!1,be[u(n)]=null})),u(l)&&Z()&&t===u(e)||(e.value=t,t.emitVisible=(e,l)=>{ve[l]=e})},{setModalProps:e=>{var l;null==(l=t())||l.setModalProps(e)},getVisible:k((()=>ve[~~u(n)])),redoModalHeight:()=>{var e,l;null==(l=null==(e=t())?void 0:e.redoModalHeight)||l.call(e)},openModal:(e=!0,l,o=!0)=>{var a;if(null==(a=t())||a.setModalProps({visible:e}),!l)return;if(o)return be[u(n)]=null,void(be[u(n)]=J(l));U(J(be[u(n)]),J(l))||(be[u(n)]=J(l))}}]}const ye=e=>{const l=H(null),n=z(),t=H(""),o=()=>{const e=u(l);return e||ee("useModalInner instance is undefined!"),e};return c((()=>{const l=be[u(t)];l&&e&&X(e)&&P((()=>{e(l)}))})),[(e,o)=>{le((()=>{l.value=null})),t.value=o,l.value=e,null==n||n.emit("register",e,o)},{changeLoading:(e=!0)=>{var l;null==(l=o())||l.setModalProps({loading:e})},getVisible:k((()=>ve[~~u(t)])),changeOkLoading:(e=!0)=>{var l;null==(l=o())||l.setModalProps({confirmLoading:e})},closeModal:()=>{var e;null==(e=o())||e.setModalProps({visible:!1})},setModalProps:e=>{var l;null==(l=o())||l.setModalProps(e)}}]};export{me as _,ye as a,Ce as b,de as u};
