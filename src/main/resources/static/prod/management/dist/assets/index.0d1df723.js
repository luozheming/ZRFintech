import{e,cs as o,aO as s,bd as t,be as a,aI as r,g as n,j as i,k as l,w as c,bu as d,bq as u,r as p,o as m,m as f,n as k,C as g,aN as h,E as _,F as b}from"./index.22b3842c.js";import{D as x}from"./siteSetting.c485f07c.js";import{b as C}from"./useModal.f7817ea0.js";import{h as w}from"./header.d801b988.js";import"./vendor.afa0338c.js";import"./useWindowSizeFn.84bb5680.js";import"./FullscreenOutlined.c2732f78.js";var D=e({name:"UserDropdown",components:{Dropdown:o,Menu:s,MenuItem:t((()=>a((()=>__import__("./DropMenuItem.878e2686.js")),["./assets/DropMenuItem.878e2686.js","./assets/index.22b3842c.js","./assets/index.72ccf31d.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css"]))),MenuDivider:s.Divider,LockAction:t((()=>a((()=>__import__("./LockModal.ebe96e96.js")),["./assets/LockModal.ebe96e96.js","./assets/LockModal.c2a09df5.css","./assets/index.22b3842c.js","./assets/index.72ccf31d.css","./assets/vendor.afa0338c.js","./assets/vendor.4a5fa02d.css","./assets/useModal.f7817ea0.js","./assets/useModal.b1a1b797.css","./assets/useWindowSizeFn.84bb5680.js","./assets/FullscreenOutlined.c2732f78.js","./assets/useForm.e3cb60e7.js","./assets/useForm.bd5dea06.css","./assets/index.47947cc7.js","./assets/index.81ecbe86.css","./assets/index.315fd84a.js","./assets/index.0edc9df2.css","./assets/responsiveObserve.c545f1cc.js","./assets/_baseIteratee.2a793644.js","./assets/get.29e7fdfb.js","./assets/index.bcd11ebc.js","./assets/RedoOutlined.22cccd55.js","./assets/index.4507acc2.js","./assets/index.7b6c5efe.css","./assets/index.c5b9a475.js","./assets/index.06c7b3c0.css","./assets/index.6d15acf2.js","./assets/index.0b3eba88.css","./assets/UpOutlined.42537489.js","./assets/index.54d27705.js","./assets/index.4a4a1593.css","./assets/CountdownInput.e8b0e229.js","./assets/CountdownInput.05ee5bdd.css","./assets/index.c0f1a3c5.js","./assets/index.2b38113f.css","./assets/download.107083c5.js","./assets/header.d801b988.js"])))},props:{theme:r.oneOf(["dark","light"])},setup(){const{prefixCls:e}=n("header-user-dropdown"),{t:o}=i(),{getShowDoc:s}=d(),t=l((()=>{const{userName:e="",desc:o}=c.getUserInfoState||{};return{userName:e,desc:o}})),[a,{openModal:r}]=C();return{prefixCls:e,t:o,getUserInfo:t,handleMenuClick:function(e){switch(e.key){case"logout":c.confirmLoginOut();break;case"doc":u(x);break;case"lock":r(!0)}},getShowDoc:s,headerImg:w,register:a}}});D.render=function(e,o,s,t,a,r){const n=p("MenuDivider"),i=p("MenuItem"),l=p("Menu"),c=p("Dropdown"),d=p("LockAction");return m(),f(b,null,[k(c,{placement:"bottomLeft",overlayClassName:`${e.prefixCls}-dropdown-overlay`},{overlay:g((()=>[k(l,{onClick:e.handleMenuClick},{default:g((()=>[e.getShowDoc?(m(),f(n,{key:0})):h("",!0),k(i,{key:"lock",text:e.t("layout.header.tooltipLock"),icon:"ion:lock-closed-outline"},null,8,["text"]),k(i,{key:"logout",text:e.t("layout.header.dropdownItemLoginOut"),icon:"ion:power-outline"},null,8,["text"])])),_:1},8,["onClick"])])),default:g((()=>[k("span",{class:[[e.prefixCls,`${e.prefixCls}--${e.theme}`],"flex"]},[k("span",{class:`${e.prefixCls}__info hidden md:block`},[k("span",{class:[`${e.prefixCls}__name  `,"truncate"]},_(e.getUserInfo.userName),3)],2)],2)])),_:1},8,["overlayClassName"]),k(d,{onRegister:e.register},null,8,["onRegister"])],64)};export default D;
