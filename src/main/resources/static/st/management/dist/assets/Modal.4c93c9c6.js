var e=Object.defineProperty,t=Object.prototype.hasOwnProperty,a=Object.getOwnPropertySymbols,o=Object.prototype.propertyIsEnumerable,l=(t,a,o)=>a in t?e(t,a,{enumerable:!0,configurable:!0,writable:!0,value:o}):t[a]=o,r=(e,r)=>{for(var n in r||(r={}))t.call(r,n)&&l(e,n,r[n]);if(a)for(var n of a(r))o.call(r,n)&&l(e,n,r[n]);return e},n=(e,t,a)=>new Promise(((o,l)=>{var r=e=>{try{s(a.next(e))}catch(t){l(t)}},n=e=>{try{s(a.throw(e))}catch(t){l(t)}},s=e=>e.done?o(e.value):Promise.resolve(e.value).then(r,n);s((a=a.apply(e,t)).next())}));import{cz as s,e as d,q as i,dz as u,k as c,w as p,dB as m,z as f,dN as h,dO as b,dG as g,dH as v,r as y,o as w,m as I,n as R,ar as _,bz as x,E as O}from"./index.e3ab3d33.js";import{_ as U,a as M}from"./useModal.ee72b48f.js";import{g as F,U as P,h as j}from"./useForm.4c49c850.js";import{T as B}from"./index.40de8c80.js";function k(){return[{title:"id",dataIndex:"id",width:100,defaultHidden:!0},{title:"图片路径",dataIndex:"photoRoute",width:100,defaultHidden:!0},{title:"图片",dataIndex:"photo",width:120,slots:{customRender:"photo"}},{title:"类型",width:70,dataIndex:"photoType",customRender:({text:e})=>({children:1==e?"小程序":"pc",attrs:{}})},{title:"排序",dataIndex:"orderNo",width:50},{title:"状态",dataIndex:"status",width:70,customRender:({record:e})=>{const t=0==~~e.status,a=t?"启用":"禁用";return s(B,{color:t?"green":"red"},(()=>a))}},{title:"跳转链接",dataIndex:"linkUrl",width:100}]}const L=[{field:"id",label:"id",component:"Input",defaultValue:"",show:!1},{field:"linkUrl",label:"跳转链接",component:"Input",defaultValue:""},{field:"photoType",label:"类型",component:"RadioButtonGroup",defaultValue:1,componentProps:{options:[{label:"小程序",value:1},{label:"pc",value:2}]}},{label:"排序",field:"orderNo",component:"Input",required:!0},{field:"status",label:"状态",component:"RadioButtonGroup",defaultValue:0,componentProps:{options:[{label:"启用",value:0},{label:"禁用",value:1}]}},{label:"图片路径",field:"photoRoute",component:"Input",show:!1,defaultValue:""}];var T=d({name:"Modal",components:{BasicModal:U,BasicForm:F,Upload:P},emits:["success"],setup(e,{emit:t}){const a=i([]),o=i(""),l=i(!0),{VITE_GLOB_API_URL:s}=u(),{createMessage:d}=f(),[g,{setFieldsValue:v,resetFields:y,validate:w,getFieldsValue:I}]=j({labelWidth:100,schemas:L,showActionButtonGroup:!1,actionColOptions:{span:23}}),[R,{setModalProps:_,closeModal:x}]=M((e=>n(this,null,(function*(){y(),_({confirmLoading:!1}),l.value=!!(null==e?void 0:e.isUpdate),o.value="",p(l)&&(o.value=`data:image/png;base64,${e.record.photo}`,v(r({},e.record)))})))),O=c((()=>p(l)?"编辑banner":"新增banner"));return{registerModal:R,registerForm:g,handleSubmit:function(){return n(this,null,(function*(){let e={},n="";p(l)?(e=I(),n=h):(e=yield w(),n=b);try{if(!o.value)return d.warning("请上传图片");_({confirmLoading:!0});const i=r({},e),u=new FormData;Object.keys(i).forEach((e=>{u.append(e,i[e])})),u.append("photoFile",a.value);200===(yield m({url:s+n,method:"post",data:u,headers:{"Content-Type":"multipart/form-data"}})).data.code&&(d.success(p(l)?"修改成功！":"新增成功！"),x(),t("success"))}finally{_({confirmLoading:!1})}}))},fileList:a,imageUrl:o,beforeUpload:e=>new Promise(((t,l)=>e.size/1024/1024<4?(function(e){return new Promise(((t,a)=>{const o=new FileReader;o.readAsDataURL(e),o.onload=()=>t({result:o.result,file:e}),o.onerror=e=>a(e)}))}(e).then((({result:e})=>{o.value=e})),a.value=e,l(!1)):(d.error("只能上传不超过4MB的文件!"),l(!1)))),getTitle:O}}});const V=x("data-v-14d5e604");g("data-v-14d5e604");const z=R("label",{class:"ml-10 mr-5 ant-form-item-required ant-form-item-no-colon"},"请上传图片",-1),G=O(" 选择文件 ");v();const S=V(((e,t,a,o,l,r)=>{const n=y("BasicForm"),s=y("upload-outlined"),d=y("a-button"),i=y("Upload"),u=y("BasicModal");return w(),I(u,_({height:340},e.$attrs,{onRegister:e.registerModal,title:e.getTitle,onOk:e.handleSubmit}),{default:V((()=>[R(n,{onRegister:e.registerForm},null,8,["onRegister"]),z,R(i,{"file-list":e.fileList,name:"avatar",class:"avatar-uploader","show-upload-list":!1,"before-upload":e.beforeUpload},{default:V((()=>[e.imageUrl?(w(),I("img",{key:0,src:e.imageUrl,alt:"avatar"},null,8,["src"])):(w(),I(d,{key:1,type:"primary"},{default:V((()=>[R(s),G])),_:1}))])),_:1},8,["file-list","before-upload"])])),_:1},16,["onRegister","title","onOk"])}));T.render=S,T.__scopeId="data-v-14d5e604";var E=Object.freeze({__proto__:null,[Symbol.toStringTag]:"Module",default:T});export{E as M,T as _,k as g};
