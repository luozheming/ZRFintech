var e=Object.defineProperty,t=Object.prototype.hasOwnProperty,n=Object.getOwnPropertySymbols,l=Object.prototype.propertyIsEnumerable,o=(t,n,l)=>n in t?e(t,n,{enumerable:!0,configurable:!0,writable:!0,value:l}):t[n]=l,a=(e,a)=>{for(var i in a||(a={}))t.call(a,i)&&o(e,i,a[i]);if(n)for(var i of n(a))l.call(a,i)&&o(e,i,a[i]);return e};import{D as i,u as s}from "./index.5231ee00.js";import{j as r,aI as u,e as E,g as _,k as d,r as g,o as c,m as O,F as S,b2 as T,aq as p,C as A,D as C,E as N,aN as D,cH as R,n as M,bn as f,p as b,df as m,dp as I,v as y,x as H,cB as h,a$ as L,aW as v,X as U,b1 as B,aY as w,bC as P,bG as F,z as x,aP as W,q as G,bX as k,cL as $,ae as X,cN as j,bd as V,be as K,dT as Y,dU as Q,dV as z,aJ as Z,aK as q,dW as J,bD as ee,bv as te,dX as ne,dY as le,dZ as oe,d_ as ae,d$ as ie,bo as se,e0 as re,e1 as ue,e2 as Ee,bz as _e,aS as de,bu as ge,aM as ce}from "./index.2da64865.js";import{A as Oe}from "./ArrowLeftOutlined.f88bfc6d.js";import{D as Se}from "./index.ca3908e8.js";const{t:Te}=r(),pe={confirmLoading:u.bool,showCancelBtn:u.bool.def(!0),cancelButtonProps:Object,cancelText:u.string.def(Te("common.cancelText")),showOkBtn:u.bool.def(!0),okButtonProps:Object,okText:u.string.def(Te("common.okText")),okType:u.string.def("primary"),showFooter:u.bool,footerHeight:{type:[String,Number],default:60}},Ae=a({isDetail:u.bool,title:u.string.def(""),loadingText:u.string,showDetailBack:u.bool.def(!0),visible:u.bool,loading:u.bool,maskClosable:u.bool.def(!0),getContainer:{type:[Object,String]},scrollOptions:{type:Object,default:null},closeFunc:{type:[Function,Object],default:null},triggerWindowResize:u.bool,destroyOnClose:u.bool},pe);var Ce=E({name:"BasicDrawerFooter",props:a(a({},pe),{height:{type:String,default:"60px"}}),emits:["ok","close"],setup(e, {emit:t}){const{prefixCls:n}=_("basic-drawer-footer");return{handleOk:function(){t("ok")},prefixCls:n,handleClose:function(){t("close")},getStyle:d((()=>{const t=`${e.height}`;return{height:t,lineHeight:t}}))}}});Ce.render=function(e, t, n, l, o, a){const i=g("a-button");return e.showFooter||e.$slots.footer?(c(),O("div",{key:0,class:e.prefixCls,style:e.getStyle},[e.$slots.footer?T(e.$slots,"footer",{key:1}):(c(),O(S,{key:0},[T(e.$slots,"insertFooter"),e.showCancelBtn?(c(),O(i,p({key:0},e.cancelButtonProps,{onClick:e.handleClose,class:"mr-2"}),{default:A((()=>[C(N(e.cancelText),1)])),_:1},16,["onClick"])):D("",!0),T(e.$slots,"centerFooter"),e.showOkBtn?(c(),O(i,p({key:1,type:e.okType,onClick:e.handleOk},e.okButtonProps,{class:"mr-2",loading:e.confirmLoading}),{default:A((()=>[C(N(e.okText),1)])),_:1},16,["type","onClick","loading"])):D("",!0),T(e.$slots,"appendFooter")],64))],6)):D("",!0)};var Ne=E({name:"BasicDrawerHeader",components:{BasicTitle:R,ArrowLeftOutlined:Oe},props:{isDetail:u.bool,showDetailBack:u.bool,title:u.string},emits:["close"],setup(e, {emit:t}){const{prefixCls:n}=_("basic-drawer-header");return{prefixCls:n,handleClose:function(){t("close")}}}});const De={key:1};Ne.render=function(e, t, n, l, o, a){const i=g("BasicTitle"),s=g("ArrowLeftOutlined");return e.isDetail?(c(),O("div",{key:1,class:[e.prefixCls,`${e.prefixCls}--detail`]},[M("span",{class:`${e.prefixCls}__twrap`},[e.showDetailBack?(c(),O("span",{key:0,onClick:t[1]||(t[1]=(...t)=>e.handleClose&&e.handleClose(...t))},[M(s,{class:`${e.prefixCls}__back`},null,8,["class"])])):D("",!0),e.title?(c(),O("span",De,N(e.title),1)):D("",!0)],2),M("span",{class:`${e.prefixCls}__toolbar`},[T(e.$slots,"titleToolbar")],2)],2)):(c(),O(i,{key:0,class:e.prefixCls},{default:A((()=>[T(e.$slots,"title"),C(" "+N(e.$slots.title?"":e.title),1)])),_:3},8,["class"]))};var Re=E({components:{Drawer:i,ScrollContainer:f,DrawerFooter:Ce,DrawerHeader:Ne},inheritAttrs:!1,props:Ae,emits:["visible-change","ok","close","register"],setup(e, {emit:t}){const n=b(!1),l=m(),o=b(null),{t:i}=r(),{prefixVar:s,prefixCls:u}=_("basic-drawer"),E={setDrawerProps:function(e){o.value=I(y(o)||{},e),Reflect.has(e,"visible")&&(n.value=!!e.visible)},emitVisible:void 0},g=B();g&&t("register",E,g.uid);const c=d((()=>I(H(e),y(o)))),O=d((()=>{const e=a(a(a({placement:"right"},y(l)),y(c)),{visible:y(n)});e.title=void 0;const{isDetail:t,width:o,wrapClassName:i,getContainer:r}=e;if(t){o||(e.width="100%");const t=`${u}__detail`;e.wrapClassName=i?`${i} ${t}`:t,r||(e.getContainer=`.${s}-layout-content`)}return e})),S=d((()=>a(a({},l),y(O)))),T=d((()=>{const{footerHeight:e,showFooter:t}=y(O);return t&&e?h(e)?`${e}px`:`${e.replace("px","")}px`:"0px"})),p=d((()=>({position:"relative",height:`calc(100% - ${y(T)})`}))),A=d((()=>{var e;return!!(null==(e=y(O))?void 0:e.loading)}));return L((()=>{n.value=e.visible})),v((()=>n.value),(e=>{U((()=>{var n;t("visible-change",e),g&&(null==(n=E.emitVisible)||n.call(E,e,g.uid))}))})),{onClose:function(e){return l=this,o=null,a=function*(){const{closeFunc:l}=y(O);if(t("close",e),l&&w(l)){const e=yield l();n.value=!e}else n.value=!1},new Promise(((t, n)=>{var i= t=>{try{r(a.next(t))}catch(e){n(e)}},s= t=>{try{r(a.throw(t))}catch(e){n(e)}},r= e=>e.done?t(e.value):Promise.resolve(e.value).then(i,s);r((a=a.apply(l,o)).next())}));var l,o,a},t:i,prefixCls:u,getMergeProps:c,getScrollContentStyle:p,getProps:O,getLoading:A,getBindValues:S,getFooterHeight:T,handleOk:function(){t("ok")}}}});Re.render=function(e, t, n, l, o, a){const i=g("DrawerHeader"),s=g("ScrollContainer"),r=g("DrawerFooter"),u=g("Drawer"),E=P("loading");return c(),O(u,p({class:e.prefixCls,onClose:e.onClose},e.getBindValues),F({default:A((()=>[x(M(s,{style:e.getScrollContentStyle,"loading-tip":e.loadingText||e.t("common.loadingText")},{default:A((()=>[T(e.$slots,"default")])),_:3},8,["style","loading-tip"]),[[E,e.getLoading]]),M(r,p(e.getProps,{onClose:e.onClose,onOk:e.handleOk,height:e.getFooterHeight}),F({_:2},[W(Object.keys(e.$slots),(t=>({name:t,fn:A((n=>[T(e.$slots,t,n)]))})))]),1040,["onClose","onOk","height"])])),_:2},[e.$slots.title?void 0:{name:"title",fn:A((()=>[M(i,{title:e.getMergeProps.title,isDetail:e.isDetail,showDetailBack:e.showDetailBack,onClose:e.onClose},{titleToolbar:A((()=>[T(e.$slots,"titleToolbar")])),_:1},8,["title","isDetail","showDetailBack","onClose"])]))}]),1040,["class","onClose"])};const Me=G({}),fe=G({});const be=V((()=>K((()=>__import__("./TypePicker.32ba3b47.js")),["./assets/TypePicker.32ba3b47.js","./assets/TypePicker.6425076b.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css"]))),me=V((()=>K((()=>__import__("./ThemePicker.ab119029.js")),["./assets/ThemePicker.ab119029.js","./assets/ThemePicker.9c3911f9.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/index.5231ee00.js","./assets/index.693c4e12.css","./assets/RedoOutlined.e4a50862.js","./assets/_baseIteratee.aeb63f7c.js","./assets/get.498bc099.js","./assets/useSortable.1f87e7ef.js","./assets/FullscreenOutlined.e9192cae.js","./assets/index.d57cf454.js","./assets/index.a9fdc7b4.css","./assets/useWindowSizeFn.be34850b.js","./assets/index.75aadffb.js","./assets/index.aad873ea.css","./assets/ArrowLeftOutlined.f88bfc6d.js","./assets/index.ca3908e8.js","./assets/index.2b38113f.css"]))),Ie=V((()=>K((()=>__import__("./SettingFooter.7d4a613d.js")),["./assets/SettingFooter.7d4a613d.js","./assets/SettingFooter.e64c0c59.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/RedoOutlined.e4a50862.js","./assets/_baseIteratee.aeb63f7c.js","./assets/get.498bc099.js"]))),ye=V((()=>K((()=>__import__("./SwitchItem.c95b7183.js")),["./assets/SwitchItem.c95b7183.js","./assets/SwitchItem.ee1ef7af.css","./assets/index.a825569d.js","./assets/index.4a4a1593.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/index.5231ee00.js","./assets/index.693c4e12.css","./assets/RedoOutlined.e4a50862.js","./assets/_baseIteratee.aeb63f7c.js","./assets/get.498bc099.js","./assets/useSortable.1f87e7ef.js","./assets/FullscreenOutlined.e9192cae.js","./assets/index.d57cf454.js","./assets/index.a9fdc7b4.css","./assets/useWindowSizeFn.be34850b.js","./assets/index.75aadffb.js","./assets/index.aad873ea.css","./assets/ArrowLeftOutlined.f88bfc6d.js","./assets/index.ca3908e8.js","./assets/index.2b38113f.css"]))),He=V((()=>K((()=>__import__("./SelectItem.f3327508.js")),["./assets/SelectItem.f3327508.js","./assets/SelectItem.b191de8c.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/index.5231ee00.js","./assets/index.693c4e12.css","./assets/RedoOutlined.e4a50862.js","./assets/_baseIteratee.aeb63f7c.js","./assets/get.498bc099.js","./assets/useSortable.1f87e7ef.js","./assets/FullscreenOutlined.e9192cae.js","./assets/index.d57cf454.js","./assets/index.a9fdc7b4.css","./assets/useWindowSizeFn.be34850b.js","./assets/index.75aadffb.js","./assets/index.aad873ea.css","./assets/ArrowLeftOutlined.f88bfc6d.js","./assets/index.ca3908e8.js","./assets/index.2b38113f.css"]))),he=V((()=>K((()=>__import__("./InputNumberItem.740b6290.js")),["./assets/InputNumberItem.740b6290.js","./assets/InputNumberItem.cfca3b5d.css","./assets/index.da5aa414.js","./assets/index.0b3eba88.css","./assets/index.2da64865.js","./assets/index.fc9d9f0a.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/RedoOutlined.e4a50862.js","./assets/_baseIteratee.aeb63f7c.js","./assets/get.498bc099.js","./assets/index.5231ee00.js","./assets/index.693c4e12.css","./assets/useSortable.1f87e7ef.js","./assets/FullscreenOutlined.e9192cae.js","./assets/index.d57cf454.js","./assets/index.a9fdc7b4.css","./assets/useWindowSizeFn.be34850b.js","./assets/index.75aadffb.js","./assets/index.aad873ea.css","./assets/ArrowLeftOutlined.f88bfc6d.js","./assets/index.ca3908e8.js","./assets/index.2b38113f.css"]))),{t:Le}=r();var ve,Ue;(Ue=ve||(ve={}))[Ue.CHANGE_LAYOUT=0]="CHANGE_LAYOUT",Ue[Ue.CHANGE_THEME_COLOR=1]="CHANGE_THEME_COLOR",Ue[Ue.MENU_HAS_DRAG=2]="MENU_HAS_DRAG",Ue[Ue.MENU_ACCORDION=3]="MENU_ACCORDION",Ue[Ue.MENU_TRIGGER=4]="MENU_TRIGGER",Ue[Ue.MENU_TOP_ALIGN=5]="MENU_TOP_ALIGN",Ue[Ue.MENU_COLLAPSED=6]="MENU_COLLAPSED",Ue[Ue.MENU_COLLAPSED_SHOW_TITLE=7]="MENU_COLLAPSED_SHOW_TITLE",Ue[Ue.MENU_WIDTH=8]="MENU_WIDTH",Ue[Ue.MENU_SHOW_SIDEBAR=9]="MENU_SHOW_SIDEBAR",Ue[Ue.MENU_THEME=10]="MENU_THEME",Ue[Ue.MENU_SPLIT=11]="MENU_SPLIT",Ue[Ue.MENU_FIXED=12]="MENU_FIXED",Ue[Ue.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE=13]="MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE",Ue[Ue.MENU_TRIGGER_MIX_SIDEBAR=14]="MENU_TRIGGER_MIX_SIDEBAR",Ue[Ue.MENU_FIXED_MIX_SIDEBAR=15]="MENU_FIXED_MIX_SIDEBAR",Ue[Ue.HEADER_SHOW=16]="HEADER_SHOW",Ue[Ue.HEADER_THEME=17]="HEADER_THEME",Ue[Ue.HEADER_FIXED=18]="HEADER_FIXED",Ue[Ue.HEADER_SEARCH=19]="HEADER_SEARCH",Ue[Ue.TABS_SHOW_QUICK=20]="TABS_SHOW_QUICK",Ue[Ue.TABS_SHOW_REDO=21]="TABS_SHOW_REDO",Ue[Ue.TABS_SHOW=22]="TABS_SHOW",Ue[Ue.TABS_SHOW_FOLD=23]="TABS_SHOW_FOLD",Ue[Ue.LOCK_TIME=24]="LOCK_TIME",Ue[Ue.FULL_CONTENT=25]="FULL_CONTENT",Ue[Ue.CONTENT_MODE=26]="CONTENT_MODE",Ue[Ue.SHOW_BREADCRUMB=27]="SHOW_BREADCRUMB",Ue[Ue.SHOW_BREADCRUMB_ICON=28]="SHOW_BREADCRUMB_ICON",Ue[Ue.GRAY_MODE=29]="GRAY_MODE",Ue[Ue.COLOR_WEAK=30]="COLOR_WEAK",Ue[Ue.SHOW_LOGO=31]="SHOW_LOGO",Ue[Ue.SHOW_FOOTER=32]="SHOW_FOOTER",Ue[Ue.ROUTER_TRANSITION=33]="ROUTER_TRANSITION",Ue[Ue.OPEN_PROGRESS=34]="OPEN_PROGRESS",Ue[Ue.OPEN_PAGE_LOADING=35]="OPEN_PAGE_LOADING",Ue[Ue.OPEN_ROUTE_TRANSITION=36]="OPEN_ROUTE_TRANSITION";const Be=[{value:Y.FULL,label:Le("layout.setting.contentModeFull")},{value:Y.FIXED,label:Le("layout.setting.contentModeFixed")}],we=[{value:Q.CENTER,label:Le("layout.setting.topMenuAlignRight")},{value:Q.START,label:Le("layout.setting.topMenuAlignLeft")},{value:Q.END,label:Le("layout.setting.topMenuAlignCenter")}],Pe=[z.ZOOM_FADE,z.FADE,z.ZOOM_OUT,z.FADE_SIDE,z.FADE_BOTTOM,z.FADE_SCALE].map((e=>({label:e,value:e}))),Fe=[{title:Le("layout.setting.menuTypeSidebar"),mode:Z.INLINE,type:q.SIDEBAR},{title:Le("layout.setting.menuTypeMix"),mode:Z.INLINE,type:q.MIX},{title:Le("layout.setting.menuTypeTopMenu"),mode:Z.HORIZONTAL,type:q.TOP_MENU},{title:Le("layout.setting.menuTypeMixSidebar"),mode:Z.INLINE,type:q.MIX_SIDEBAR}],xe=[{value:J.HOVER,label:Le("layout.setting.triggerHover")},{value:J.CLICK,label:Le("layout.setting.triggerClick")}];function We(e, t){const n=function(e, t){const{getThemeColor:n}=se();switch(e){case ve.CHANGE_LAYOUT:const{mode:e,type:l,split:o}=t;return{menuSetting:a({mode:e,type:l,collapsed:!1,show:!0,hidden:!1},void 0===o?{split:o}:{})};case ve.CHANGE_THEME_COLOR:return n.value===t?{}:(ie(t),{themeColor:t});case ve.MENU_HAS_DRAG:return{menuSetting:{canDrag:t}};case ve.MENU_ACCORDION:return{menuSetting:{accordion:t}};case ve.MENU_TRIGGER:return{menuSetting:{trigger:t}};case ve.MENU_TOP_ALIGN:return{menuSetting:{topMenuAlign:t}};case ve.MENU_COLLAPSED:return{menuSetting:{collapsed:t}};case ve.MENU_WIDTH:return{menuSetting:{menuWidth:t}};case ve.MENU_SHOW_SIDEBAR:return{menuSetting:{show:t}};case ve.MENU_COLLAPSED_SHOW_TITLE:return{menuSetting:{collapsedShowTitle:t}};case ve.MENU_THEME:return ae(t),{menuSetting:{bgColor:t}};case ve.MENU_SPLIT:return{menuSetting:{split:t}};case ve.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE:return{menuSetting:{closeMixSidebarOnChange:t}};case ve.MENU_FIXED:return{menuSetting:{fixed:t}};case ve.MENU_TRIGGER_MIX_SIDEBAR:return{menuSetting:{mixSideTrigger:t}};case ve.MENU_FIXED_MIX_SIDEBAR:return{menuSetting:{mixSideFixed:t}};case ve.OPEN_PAGE_LOADING:return te.commitPageLoadingState(!1),{transitionSetting:{openPageLoading:t}};case ve.ROUTER_TRANSITION:return{transitionSetting:{basicTransition:t}};case ve.OPEN_ROUTE_TRANSITION:return{transitionSetting:{enable:t}};case ve.OPEN_PROGRESS:return{transitionSetting:{openNProgress:t}};case ve.LOCK_TIME:return{lockTime:t};case ve.FULL_CONTENT:return{fullContent:t};case ve.CONTENT_MODE:return{contentMode:t};case ve.SHOW_BREADCRUMB:return{showBreadCrumb:t};case ve.SHOW_BREADCRUMB_ICON:return{showBreadCrumbIcon:t};case ve.GRAY_MODE:return oe(t),{grayMode:t};case ve.SHOW_FOOTER:return{showFooter:t};case ve.COLOR_WEAK:return le(t),{colorWeak:t};case ve.SHOW_LOGO:return{showLogo:t};case ve.TABS_SHOW_QUICK:return{multiTabsSetting:{showQuick:t}};case ve.TABS_SHOW:return{multiTabsSetting:{show:t}};case ve.TABS_SHOW_REDO:return{multiTabsSetting:{showRedo:t}};case ve.TABS_SHOW_FOLD:return{multiTabsSetting:{showFold:t}};case ve.HEADER_THEME:return ne(t),{headerSetting:{bgColor:t}};case ve.HEADER_SEARCH:return{headerSetting:{showSearch:t}};case ve.HEADER_FIXED:return{headerSetting:{fixed:t}};case ve.HEADER_SHOW:return{headerSetting:{show:t}};default:return{}}}(e,t);te.commitProjectConfigState(n)}const{t:Ge}=r();var ke=E({name:"SettingDrawer",setup(e, {attrs:t}){const{getContentMode:n,getShowFooter:l,getShowBreadCrumb:o,getShowBreadCrumbIcon:a,getShowLogo:i,getFullContent:r,getColorWeak:u,getGrayMode:E,getLockTime:_,getThemeColor:g}=se(),{getOpenPageLoading:c,getBasicTransition:O,getEnableTransition:T,getOpenNProgress:A}=_e(),{getIsHorizontal:C,getShowMenu:N,getMenuType:D,getTrigger:R,getCollapsedShowTitle:f,getMenuFixed:b,getCollapsed:m,getCanDrag:I,getTopMenuAlign:H,getAccordion:h,getMenuWidth:L,getMenuBgColor:v,getIsTopMenu:U,getSplit:B,getIsMixSidebar:w,getCloseMixSidebarOnChange:P,getMixSideTrigger:F,getMixSideFixed:x}=de(),{getShowHeader:W,getFixed:G,getHeaderBgColor:k,getShowSearch:$}=ge(),{getShowMultipleTab:X,getShowQuick:j,getShowRedo:V,getShowFold:K}=s(),Y=d((()=>y(N)&&!y(C)));function Q(){let e=y(R);const t=(l=y(B),[{value:ee.NONE,label:Le("layout.setting.menuTriggerNone")},{value:ee.FOOTER,label:Le("layout.setting.menuTriggerBottom")},...l?[]:[{value:ee.HEADER,label:Le("layout.setting.menuTriggerTop")}]]);var l;return t.some((t=>t.value===e))||(e=ee.FOOTER),M(S,null,[M(ye,{title:Ge("layout.setting.splitMenu"),event:ve.MENU_SPLIT,def:y(B),disabled:!y(Y)||y(D)!==q.MIX},null),M(ye,{title:Ge("layout.setting.mixSidebarFixed"),event:ve.MENU_FIXED_MIX_SIDEBAR,def:y(x),disabled:!y(w)},null),M(ye,{title:Ge("layout.setting.closeMixSidebarOnChange"),event:ve.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE,def:y(P),disabled:!y(w)},null),M(ye,{title:Ge("layout.setting.menuCollapse"),event:ve.MENU_COLLAPSED,def:y(m),disabled:!y(Y)},null),M(ye,{title:Ge("layout.setting.menuDrag"),event:ve.MENU_HAS_DRAG,def:y(I),disabled:!y(Y)},null),M(ye,{title:Ge("layout.setting.menuSearch"),event:ve.HEADER_SEARCH,def:y($),disabled:!y(W)},null),M(ye,{title:Ge("layout.setting.menuAccordion"),event:ve.MENU_ACCORDION,def:y(h),disabled:!y(Y)},null),M(ye,{title:Ge("layout.setting.collapseMenuDisplayName"),event:ve.MENU_COLLAPSED_SHOW_TITLE,def:y(f),disabled:!y(Y)||!y(m)||y(w)},null),M(ye,{title:Ge("layout.setting.fixedHeader"),event:ve.HEADER_FIXED,def:y(G),disabled:!y(W)},null),M(ye,{title:Ge("layout.setting.fixedSideBar"),event:ve.MENU_FIXED,def:y(b),disabled:!y(Y)||y(w)},null),M(He,{title:Ge("layout.setting.mixSidebarTrigger"),event:ve.MENU_TRIGGER_MIX_SIDEBAR,def:y(F),options:xe,disabled:!y(w)},null),M(He,{title:Ge("layout.setting.topMenuLayout"),event:ve.MENU_TOP_ALIGN,def:y(H),options:we,disabled:!y(W)||y(B)||!y(U)&&!y(B)||y(w)},null),M(He,{title:Ge("layout.setting.menuCollapseButton"),event:ve.MENU_TRIGGER,def:e,options:t,disabled:!y(Y)||y(w)},null),M(He,{title:Ge("layout.setting.contentMode"),event:ve.CONTENT_MODE,def:y(n),options:Be},null),M(he,{title:Ge("layout.setting.autoScreenLock"),min:0,event:ve.LOCK_TIME,defaultValue:y(_),formatter: e=>0===parseInt(e)?`0(${Ge("layout.setting.notAutoScreenLock")})`:`${e}${Ge("layout.setting.minute")}`},null),M(he,{title:Ge("layout.setting.expandedMenuWidth"),max:600,min:100,step:10,event:ve.MENU_WIDTH,disabled:!y(Y),defaultValue:y(L),formatter: e=>`${parseInt(e)}px`},null)])}return()=>M(Re,p(t,{title:Ge("layout.setting.drawerTitle"),width:330,wrapClassName:"setting-drawer"}),{default:()=>[M(Se,null,{default:()=>Ge("layout.setting.navMode")}),M(S,null,[M(be,{menuTypeList:Fe,handler: e=>{We(ve.CHANGE_LAYOUT,{mode:e.mode,type:e.type,split:!y(C)&&void 0})},def:y(D)},null)]),M(Se,null,{default:()=>Ge("layout.setting.sysTheme")}),M(me,{colorList:re,def:y(g),event:ve.CHANGE_THEME_COLOR},null),M(Se,null,{default:()=>Ge("layout.setting.headerTheme")}),M(me,{colorList:ue,def:y(k),event:ve.HEADER_THEME},null),M(Se,null,{default:()=>Ge("layout.setting.sidebarTheme")}),M(me,{colorList:Ee,def:y(v),event:ve.MENU_THEME},null),M(Se,null,{default:()=>Ge("layout.setting.interfaceFunction")}),Q(),M(Se,null,{default:()=>Ge("layout.setting.interfaceDisplay")}),M(S,null,[M(ye,{title:Ge("layout.setting.breadcrumb"),event:ve.SHOW_BREADCRUMB,def:y(o),disabled:!y(W)},null),M(ye,{title:Ge("layout.setting.breadcrumbIcon"),event:ve.SHOW_BREADCRUMB_ICON,def:y(a),disabled:!y(W)},null),M(ye,{title:Ge("layout.setting.tabs"),event:ve.TABS_SHOW,def:y(X)},null),M(ye,{title:Ge("layout.setting.tabsRedoBtn"),event:ve.TABS_SHOW_REDO,def:y(V),disabled:!y(X)},null),M(ye,{title:Ge("layout.setting.tabsQuickBtn"),event:ve.TABS_SHOW_QUICK,def:y(j),disabled:!y(X)},null),M(ye,{title:Ge("layout.setting.tabsFoldBtn"),event:ve.TABS_SHOW_FOLD,def:y(K),disabled:!y(X)},null),M(ye,{title:Ge("layout.setting.sidebar"),event:ve.MENU_SHOW_SIDEBAR,def:y(N),disabled:y(C)},null),M(ye,{title:Ge("layout.setting.header"),event:ve.HEADER_SHOW,def:y(W)},null),M(ye,{title:"Logo",event:ve.SHOW_LOGO,def:y(i),disabled:y(w)},null),M(ye,{title:Ge("layout.setting.footer"),event:ve.SHOW_FOOTER,def:y(l)},null),M(ye,{title:Ge("layout.setting.fullContent"),event:ve.FULL_CONTENT,def:y(r)},null),M(ye,{title:Ge("layout.setting.grayMode"),event:ve.GRAY_MODE,def:y(E)},null),M(ye,{title:Ge("layout.setting.colorWeak"),event:ve.COLOR_WEAK,def:y(u)},null)]),M(Se,null,{default:()=>Ge("layout.setting.animation")}),M(S,null,[M(ye,{title:Ge("layout.setting.progress"),event:ve.OPEN_PROGRESS,def:y(A)},null),M(ye,{title:Ge("layout.setting.switchLoading"),event:ve.OPEN_PAGE_LOADING,def:y(c)},null),M(ye,{title:Ge("layout.setting.switchAnimation"),event:ve.OPEN_ROUTE_TRANSITION,def:y(T)},null),M(He,{title:Ge("layout.setting.animationType"),event:ve.ROUTER_TRANSITION,def:y(O),options:Pe,disabled:!y(T)},null)]),M(Se,null,null),M(Ie,null,null)]})}}),$e=E({name:"SettingButton",components:{SettingDrawer:ke,Icon:ce},setup(){const[e,{openDrawer:t}]=function(){const e=b(null),t=b(!1),n=b(""),l=()=>{const t=y(e);return t||j("useDrawer instance is undefined!"),t};return[function(l, o){k((()=>{e.value=null,t.value=null,Me[y(n)]=null})),y(t)&&$()&&l===y(e)||(n.value=o,e.value=l,t.value=!0,l.emitVisible=(e, t)=>{fe[t]=e})},{setDrawerProps: e=>{var t;null==(t=l())||t.setDrawerProps(e)},getVisible:d((()=>fe[~~y(n)])),openDrawer:(e=!0, t, o=!0)=>{var a;if(null==(a=l())||a.setDrawerProps({visible:e}),t)return o?(Me[y(n)]=null,void(Me[y(n)]=H(t))):void(X(H(Me[y(n)]),H(t))||(Me[y(n)]=H(t)))}}]}();return{register:e,openDrawer:t}}});$e.render=function(e, t, n, l, o, a){const i=g("Icon"),s=g("SettingDrawer");return c(),O("div",{onClick:t[1]||(t[1]=(...t)=>e.openDrawer&&e.openDrawer(...t))},[M(i,{icon:"ion:settings-outline"}),M(s,{onRegister:e.register},null,8,["onRegister"])])};var Xe=Object.freeze({__proto__:null,[Symbol.toStringTag]:"Module",default:$e});export{We as b,Xe as i};
