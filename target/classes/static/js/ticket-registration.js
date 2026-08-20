(function(){
const $=s=>document.querySelector(s), $$=s=>Array.from(document.querySelectorAll(s));
const API={send:'/ticket/send-otp',verify:'/ticket/verify-otp',register:'/ticket/register'};
const ministries=[
"Ministry of Agriculture and Farmers Welfare","Ministry of Ayush","Ministry of Chemicals and Fertilizers","Ministry of Civil Aviation",
"Ministry of Coal","Ministry of Commerce and Industry","Ministry of Communications","Ministry of Consumer Affairs, Food and Public Distribution",
"Ministry of Cooperation","Ministry of Corporate Affairs","Ministry of Culture","Ministry of Defence","Ministry of Development of North Eastern Region",
"Ministry of Earth Sciences","Ministry of Education","Ministry of Electronics and Information Technology","Ministry of Environment, Forest and Climate Change",
"Ministry of External Affairs","Ministry of Finance","Ministry of Fisheries, Animal Husbandry and Dairying","Ministry of Food Processing Industries",
"Ministry of Health and Family Welfare","Ministry of Heavy Industries","Ministry of Home Affairs","Ministry of Housing and Urban Affairs",
"Ministry of Information and Broadcasting","Ministry of Jal Shakti","Ministry of Labour and Employment","Ministry of Law and Justice",
"Ministry of Micro, Small and Medium Enterprises","Ministry of Mines","Ministry of Minority Affairs","Ministry of New and Renewable Energy",
"Ministry of Panchayati Raj","Ministry of Parliamentary Affairs","Ministry of Personnel, Public Grievances and Pensions","Ministry of Petroleum and Natural Gas",
"Ministry of Ports, Shipping and Waterways","Ministry of Power","Ministry of Planning","Ministry of Railways","Ministry of Road Transport and Highways",
"Ministry of Rural Development","Ministry of Science and Technology","Ministry of Skill Development and Entrepreneurship","Ministry of Social Justice and Empowerment",
"Ministry of Statistics and Programme Implementation","Ministry of Steel","Ministry of Textiles","Ministry of Tourism",
"Ministry of Tribal Affairs","Ministry of Women and Child Development","Ministry of Youth Affairs and Sports"];
const states=["Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal","Andaman and Nicobar Islands","Chandigarh","Dadra and Nagar Haveli and Daman and Diu","Delhi","Jammu and Kashmir","Ladakh","Lakshadweep","Puducherry"];
const departments=["General Administration","Finance","Home","Education","Higher Education","School Education","Health and Family Welfare","Industries and Commerce","Information Technology / Electronics","Science and Technology","Agriculture","Animal Husbandry and Fisheries","Co-operation","Rural Development","Urban Development / Municipal Administration","Revenue","Transport","Public Works","Water Resources","Energy","Environment and Forests","Labour and Employment","Skill Development","Tourism and Culture","Social Welfare","Women and Child Development","Tribal Welfare","Food and Civil Supplies","Information and Public Relations","Planning","Law","Personnel","Other"];
let verified=false, timer=null;

function msg(el,text,ok=false){el.textContent=text;el.className='tr-msg '+(ok?'ok':'error');}
function show(view){$$('.tr-view').forEach(v=>v.hidden=true); $(`[data-view="${view}"]`).hidden=false; const n={verify:1,details:2,preview:3,complete:4}[view]; $$('[data-step-dot]').forEach(x=>x.classList.toggle('active',+x.dataset.stepDot<=n));}
function esc(v){return String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[c]));}
function field(label,name,type='text',extra=''){return `<label>${label}<input name="${name}" type="${type}" ${extra}></label>`}
function select(label,name,opts,extra=''){return `<label>${label}<select name="${name}" ${extra}><option value="">Choose</option>${opts.map(o=>`<option value="${esc(o)}">${esc(o)}</option>`).join('')}</select></label>`}
function renderConditional(){
 const cat=$('#category').value, box=$('#conditional'); let h='';
 if(cat==='ACADEMIA'){
  h='<div class="tr-subtitle">Academia</div><div class="tr-grid">';
  h+=field('College / Institution name','collegeName','text','list="collegeList" required');
  h+='<datalist id="collegeList"><option value="Anna University"><option value="Indian Institute of Technology Madras"><option value="University of Delhi"><option value="Indian Institute of Science"><option value="Amrita Vishwa Vidyapeetham"></datalist>';
  h+=field('College district','collegeDistrict','text','required')+field('College state','collegeState','text','required');
  h+=field('University / affiliating university','universityName','text','required')+select('Student or Faculty','academiaRole',['Student','Faculty']);
  h+=field('Register number (as per ID card)','registerNumber','text','required')+'</div>';
 } else if(cat==='CENTRAL_GOVERNMENT'){
  h='<div class="tr-subtitle">Central Government</div><div class="tr-grid">';
  h+=select('Central ministry','centralMinistry',ministries,'required')+field('Organization / office name','organizationName','text','required');
  h+=field('Designation','designation','text','required')+field('Organization location','organizationLocation','text','required')+'</div>';
 } else if(cat==='STATE_GOVERNMENT'){
  h='<div class="tr-subtitle">State Government</div><div class="tr-grid">';
  h+=select('State','stateName',states,'required')+select('State government department','stateDepartment',departments,'required');
  h+=field('Organization / office name','organizationName','text','required')+field('Designation','designation','text','required');
  h+=field('Organization location','organizationLocation','text','required')+'</div>';
 } else if(cat==='INDUSTRY_STARTUP'){
  h='<div class="tr-subtitle">Industry / Startup</div><div class="tr-grid">';
  h+=select('Type','industryOrStartup',['Industry','Startup'],'required')+field('Company name','organizationName','text','required');
  h+=field('Company location','organizationLocation','text','required')+field('Designation','designation','text','required')+'</div>';
 }
 box.innerHTML=h;
}
function renderPassport(){
 const c=$('#citizenship').value, box=document.querySelector('#conditional .tr-passport');
 if(c==='FOREIGNER'){
   if(!box){const d=document.createElement('div');d.className='tr-passport';d.innerHTML='<div class="tr-subtitle">Passport details</div><div class="tr-grid">'+field('Passport number','passportNumber','text','required')+field('Passport valid until','passportValidUntil','date','required')+field('Name as per passport','passportName','text','required')+'</div>';$('#conditional').appendChild(d);}
 } else if(box) box.remove();
}
function collect(){
 const fd=new FormData($('#detailsForm')); const o=Object.fromEntries(fd.entries()); o.email=$('#email').value.trim().toLowerCase();o.phone=$('#phone').value.trim();o.officialName=$('#verifyName').value.trim();
 o.attendanceDays=fd.getAll('attendanceDays'); return o;
}
function preview(){
 const o=collect(); const labels={category:'Category',officialName:'Official name',email:'Email',phone:'Phone',citizenshipStatus:'Citizenship',academiaType:'Academia type',collegeName:'College',collegeDistrict:'College district',collegeState:'College state',universityName:'University',academiaRole:'Role',registerNumber:'Register number',centralMinistry:'Central ministry',stateName:'State',stateDepartment:'State department',organizationName:'Organization / company',organizationLocation:'Location',designation:'Designation',industryOrStartup:'Industry / Startup',passportNumber:'Passport number',passportValidUntil:'Passport valid until',passportName:'Passport name'};
 $('#preview').innerHTML=Object.entries(labels).filter(([k])=>o[k]).map(([k,l])=>`<div><span>${esc(l)}</span><strong>${esc(o[k])}</strong></div>`).join('')+`<div><span>Attendance</span><strong>${esc(o.attendanceDays.join(', '))}</strong></div>`;
 window.ticketPayload=o; show('preview');
}
async function post(url,data){const r=await fetch(url,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)});const j=await r.json().catch(()=>({}));if(!r.ok)throw new Error(j.message||j.error||'Request failed');return j;}

$('#sendOtp').onclick=async()=>{
 const name=$('#verifyName').value.trim(),email=$('#email').value.trim(),phone=$('#phone').value.trim(),m=$('#verifyMsg');
 if(!name||!email||!phone){msg(m,'Enter your name, email and phone number.');return;}
 try{await post(API.send,{fullName:name,email,phone,passType:'TEC-VERSE 2026 Ticket'});$('#otpEmail').textContent=email;$('#otpBox').hidden=false;msg(m,'OTP sent. Check your email.',true);startTimer();}
 catch(e){msg(m,e.message);}
};
function startTimer(){let n=30;$('#resendOtp').disabled=true;clearInterval(timer);timer=setInterval(()=>{n--;$('#resendOtp').textContent=n>0?`Resend OTP (${n}s)`:'Resend OTP';if(n<=0){clearInterval(timer);$('#resendOtp').disabled=false;}},1000);$('#resendOtp').textContent='Resend OTP (30s)';}
$('#resendOtp').onclick=()=>$('#sendOtp').click();
$$('.tr-otp-inputs input').forEach((i,idx,a)=>{i.oninput=()=>{i.value=i.value.replace(/\D/g,'').slice(0,1);if(i.value&&a[idx+1])a[idx+1].focus()};});
$('#verifyOtp').onclick=async()=>{
 const otp=$$('.tr-otp-inputs input').map(x=>x.value).join(''),m=$('#verifyMsg');if(otp.length!==6){msg(m,'Enter the complete 6-digit OTP.');return;}
 try{await post(API.verify,{email:$('#email').value.trim(),otp});verified=true;$('#detailsForm [name="officialName"]').value=$('#verifyName').value.trim();msg(m,'Email verified successfully.',true);show('details');}
 catch(e){msg(m,e.message);}
};
$('#category').onchange=renderConditional;$('#citizenship').onchange=renderPassport;
$('#backVerify').onclick=()=>show('verify');
$('#detailsForm').onsubmit=e=>{e.preventDefault();const days=$$('input[name="attendanceDays"]:checked');const m=$('#detailsMsg');if(!$('#category').value){msg(m,'Choose your category.');return;}if(!days.length){msg(m,'Select at least one attendance day.');return;}if(days.length===2){days[0].checked=false;days[1].checked=false;const both=$$('input[name="attendanceDays"]')[2];both.checked=true;}preview();};
$$('input[name="attendanceDays"]').forEach(i=>i.onchange=()=>{if(i.value==='BOTH'&&i.checked)$$('input[name="attendanceDays"]').slice(0,2).forEach(x=>x.checked=false);else if(i.checked)$$('input[name="attendanceDays"]')[2].checked=false;});
$('#editForm').onclick=()=>show('details');
const categoryLabels={ACADEMIA:'Academia',CENTRAL_GOVERNMENT:'Central Govt.',STATE_GOVERNMENT:'State Govt.',INDUSTRY_STARTUP:'Industry / Startup'};
const dayLabels={DAY_1:'Day 1 · 26 Nov',DAY_2:'Day 2 · 27 Nov',BOTH:'Both Days · 26 & 27 Nov'};

function renderRefDigits(ref){
 const box=$('#referenceNumber'); box.innerHTML='';
 String(ref||'').split('').forEach((ch,i)=>{
  const s=document.createElement('span'); s.textContent=ch;
  s.style.animationDelay=(0.55+i*0.045)+'s';
  box.appendChild(s);
 });
}

function seededQr(seed){
 // Deterministic decorative pattern derived from the reference number — visual only, not a scannable code.
 let h=0; for(let i=0;i<seed.length;i++){h=(h*31+seed.charCodeAt(i))>>>0;}
 const size=7, cell=60/size; let rects='';
 for(let y=0;y<size;y++){
  for(let x=0;x<size;x++){
   h=(h*1103515245+12345)>>>0;
   const on=((h>>>16)&1)===1;
   const isFinder=(x<2&&y<2)||(x>size-3&&y<2)||(x<2&&y>size-3);
   if(on||isFinder) rects+=`<rect x="${x*cell}" y="${y*cell}" width="${cell}" height="${cell}" fill="#07182f"/>`;
  }
 }
 return rects;
}

function launchIdCard(data,payload){
 renderRefDigits(data.referenceNumber);
 $('#cardName').textContent=(payload&&payload.officialName)||'Guest';
 $('#cardCategory').textContent=(payload&&categoryLabels[payload.category])||'Delegate';
 const days=(payload&&payload.attendanceDays)||[];
 $('#cardDays').textContent=days.length?(dayLabels[days[0]]||'TEC-VERSE 2026'):'TEC-VERSE 2026';
 $('#cardQr').innerHTML=seededQr(data.referenceNumber||'TECVERSE2026');
 const card=$('#idCard');
 card.classList.remove('is-in'); void card.offsetWidth; // restart animation if replayed
 card.classList.add('is-in');
}

$('#submitFinal').onclick=async()=>{
 const m=$('#previewMsg');if(!verified){msg(m,'Email verification is required.');show('verify');return;}
 try{const result=await post(API.register,window.ticketPayload);const data=result.data||result;$('#completeEmail').textContent=data.email;show('complete');launchIdCard(data,window.ticketPayload);}
 catch(e){msg(m,e.message);}
};
})();