<script setup lang="ts">
import {nextTick,onBeforeUnmount,onMounted,ref,watch} from 'vue'
import * as echarts from 'echarts'

const props=defineProps<{data:any}>()
const now=ref(new Date()),trendEl=ref<HTMLElement>(),teamEl=ref<HTMLElement>(),unitEl=ref<HTMLElement>(),deviceEl=ref<HTMLElement>()
let clock:number|undefined
const charts:echarts.ECharts[]=[]
const fmt=(v:string)=>v?String(v).replace('T',' ').slice(0,19):'-'
const status=(v:string)=>({ALLOW:'允许',DENY:'拒绝',ONLINE:'在线',OFFLINE:'离线',UNKNOWN:'未知'} as any)[v]||v||'-'
function base(){return{backgroundColor:'transparent',textStyle:{color:'#a7bbcd'},tooltip:{trigger:'axis',backgroundColor:'rgba(10,29,43,.96)',borderColor:'#27506a',textStyle:{color:'#fff'}}}}
function draw(){
  if(!props.data||!trendEl.value||!teamEl.value||!unitEl.value||!deviceEl.value)return
  charts.splice(0).forEach(c=>c.dispose())
  const trend=echarts.init(trendEl.value);trend.setOption({...base(),grid:{left:34,right:18,top:28,bottom:24},xAxis:{type:'category',data:props.data.trend.map((x:any)=>x.date.slice(5)),axisLine:{lineStyle:{color:'#27475c'}},axisLabel:{color:'#7894a8'}},yAxis:{type:'value',axisLabel:{color:'#7894a8'},splitLine:{lineStyle:{color:'rgba(104,151,179,.12)'}}},series:[{name:'出勤人数',type:'line',smooth:true,symbolSize:6,data:props.data.trend.map((x:any)=>x.attendanceCount),lineStyle:{width:3,color:'#39d4c7'},itemStyle:{color:'#39d4c7'},areaStyle:{color:new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(57,212,199,.28)'},{offset:1,color:'rgba(57,212,199,0)'}])}},{name:'异常次数',type:'bar',barWidth:8,data:props.data.trend.map((x:any)=>x.abnormalCount),itemStyle:{color:'#ff826e',borderRadius:[4,4,0,0]}}]})
  const bar=(el:HTMLElement,data:any[],color:string)=>{const c=echarts.init(el);c.setOption({...base(),grid:{left:8,right:28,top:8,bottom:4,containLabel:true},xAxis:{type:'value',show:false},yAxis:{type:'category',inverse:true,data:data.slice(0,5).map(x=>x.name),axisLabel:{color:'#a7bbcd',width:92,overflow:'truncate'},axisLine:{show:false},axisTick:{show:false}},series:[{type:'bar',data:data.slice(0,5).map(x=>x.count),barWidth:8,label:{show:true,position:'right',color:'#d9e7f0'},itemStyle:{color,borderRadius:8}}]});return c}
  const team=bar(teamEl.value,props.data.teamStats,'#48a8ff'),unit=bar(unitEl.value,props.data.unitStats,'#9a7cff')
  const device=echarts.init(deviceEl.value);device.setOption({...base(),legend:{bottom:0,textStyle:{color:'#8fa9bb'}},series:[{type:'pie',radius:['55%','76%'],center:['50%','44%'],label:{show:false},data:(props.data.deviceStatusStats||[]).map((x:any)=>({name:status(x.name),value:x.count,itemStyle:{color:x.name==='ONLINE'?'#39d4c7':x.name==='OFFLINE'?'#ff826e':'#6d8292'}}))}]})
  charts.push(trend,team,unit,device)
}
watch(()=>props.data,()=>nextTick(draw))
onMounted(()=>{clock=window.setInterval(()=>now.value=new Date(),1000);window.addEventListener('resize',resize);nextTick(draw)})
onBeforeUnmount(()=>{if(clock)clearInterval(clock);window.removeEventListener('resize',resize);charts.forEach(c=>c.dispose())})
function resize(){charts.forEach(c=>c.resize())}
</script>

<template>
<div class="project-screen">
  <div v-if="!data" class="screen-loading">正在加载项目运行数据...</div>
  <template v-else>
    <header class="ps-header">
      <div class="ps-brand"><span>BG</span><div><b>BuildGuard</b><small>智慧工地运行中心</small></div></div>
      <div class="ps-title"><small>{{data.companyName}}</small><h1>{{data.project.projectName}}</h1><p><i></i> 项目运行数据实时汇总</p></div>
      <div class="ps-time"><b>{{now.toLocaleTimeString('zh-CN',{hour12:false})}}</b><span>{{now.toLocaleDateString('zh-CN',{year:'numeric',month:'long',day:'numeric',weekday:'long'})}}</span></div>
    </header>

    <section class="ps-metrics">
      <div v-for="m in [{l:'项目总人数',v:data.workerCount,u:'人',s:`在场率 ${data.onSiteRate}%`},{l:'当前在场',v:data.onSiteCount,u:'人',s:`今日出勤 ${data.todayAttendance} 人`},{l:'安全教育通过率',v:data.safetyRate,u:'%',s:`覆盖 ${data.workerCount} 名人员`},{l:'设备在线率',v:data.deviceOnlineRate,u:'%',s:`${data.onlineDevices} / ${data.deviceCount} 台在线`},{l:'今日异常考勤',v:data.abnormalCount,u:'条',s:'门禁校验拒绝记录'},{l:'上报成功率',v:data.reportRate,u:'%',s:'监管数据模拟上报'}]" class="ps-metric">
        <span>{{m.l}}</span><strong>{{m.v}}<em>{{m.u}}</em></strong><small>{{m.s}}</small>
      </div>
    </section>

    <main class="ps-layout">
      <section class="ps-column">
        <div class="ps-card project-card"><div class="ps-card-title"><b>项目概况</b><span>{{data.project.status||'未设置状态'}}</span></div><div class="project-name">{{data.project.projectCode||'-'}}</div><dl><dt>项目地址</dt><dd>{{data.project.address||'-'}}</dd><dt>项目经理</dt><dd>{{data.project.managerName||'-'}} {{data.project.managerPhone||''}}</dd><dt>项目周期</dt><dd>{{data.project.startDate||'-'}} 至 {{data.project.endDate||'-'}}</dd></dl><div class="project-facts"><span><b>{{data.participantUnitCount}}</b>参建单位</span><span><b>{{data.teamCount}}</b>班组</span><span><b>{{data.areaCount}}</b>区域</span></div></div>
        <div class="ps-card chart-card"><div class="ps-card-title"><b>参建单位人员分布</b><small>TOP 5</small></div><div ref="unitEl" class="ps-chart"></div></div>
        <div class="ps-card chart-card"><div class="ps-card-title"><b>班组人员分布</b><small>TOP 5</small></div><div ref="teamEl" class="ps-chart"></div></div>
      </section>

      <section class="ps-column ps-center">
        <div class="ps-card trend-card"><div class="ps-card-title"><b>近 7 日出勤趋势</b><span>出勤人数 / 异常次数</span></div><div ref="trendEl" class="trend-chart"></div></div>
        <div class="ps-center-row">
          <div class="ps-card"><div class="ps-card-title"><b>今日区域考勤</b><small>{{data.areaAttendanceStats.length}} 个活跃区域</small></div><div class="rank-list"><div v-for="(x,i) in data.areaAttendanceStats.slice(0,6)" :key="x.name"><i>{{String(i+1).padStart(2,'0')}}</i><span>{{x.name}}</span><b>{{x.count}}<small>次</small></b></div><div v-if="!data.areaAttendanceStats.length" class="ps-empty">今日暂无区域考勤</div></div></div>
          <div class="ps-card"><div class="ps-card-title"><b>异常原因</b><small>今日</small></div><div class="rank-list danger-list"><div v-for="(x,i) in data.abnormalReasonStats.slice(0,6)" :key="x.name"><i>{{String(i+1).padStart(2,'0')}}</i><span>{{x.name}}</span><b>{{x.count}}<small>条</small></b></div><div v-if="!data.abnormalReasonStats.length" class="ps-empty good">今日暂无异常考勤</div></div></div>
        </div>
        <div class="ps-card attendance-card"><div class="ps-card-title"><b>最近考勤动态</b><span>实时门禁通行记录</span></div><div class="attendance-head"><span>人员</span><span>区域 / 设备</span><span>时间</span><span>结果</span></div><div v-for="a in data.recentAttendance.slice(0,6)" :key="a.id" class="attendance-row"><b>{{a.workerName||'未知人员'}}</b><span>{{a.areaName||'-'}} · {{a.deviceName||'-'}}</span><time>{{fmt(a.attendanceTime)}}</time><em :class="{deny:a.accessResult==='DENY'}">{{status(a.accessResult)}}</em></div><div v-if="!data.recentAttendance.length" class="ps-empty">暂无考勤动态</div></div>
      </section>

      <section class="ps-column">
        <div class="ps-card device-card"><div class="ps-card-title"><b>设备运行状态</b><span>{{data.onlineDevices}} 台在线</span></div><div ref="deviceEl" class="device-chart"></div><div class="device-facts"><span><b>{{data.deviceCount}}</b>设备总数</span><span><b>{{data.onlineDevices}}</b>在线设备</span><span><b>{{data.deviceOfflineCount}}</b>离线/未知</span></div></div>
        <div class="ps-card"><div class="ps-card-title"><b>安全与门禁态势</b><small>实时</small></div><div class="status-list"><div><span>安全教育通过率</span><b>{{data.safetyRate}}%</b><i><em :style="{width:data.safetyRate+'%'}"></em></i></div><div><span>设备在线率</span><b>{{data.deviceOnlineRate}}%</b><i><em :style="{width:data.deviceOnlineRate+'%'}"></em></i></div><div><span>人员在场率</span><b>{{data.onSiteRate}}%</b><i><em :style="{width:data.onSiteRate+'%'}"></em></i></div><div><span>上报成功率</span><b>{{data.reportRate}}%</b><i><em :style="{width:data.reportRate+'%'}"></em></i></div></div></div>
        <div class="ps-card alert-card"><div class="ps-card-title"><b>最近异常记录</b><span>{{data.recentAbnormal.length}} 条</span></div><div v-for="a in data.recentAbnormal.slice(0,5)" :key="a.id" class="alert-row"><i></i><div><b>{{a.workerName||'未知人员'}} · {{a.areaName||'-'}}</b><span>{{a.denyReason||'门禁校验拒绝'}} · {{fmt(a.attendanceTime)}}</span></div></div><div v-if="!data.recentAbnormal.length" class="ps-empty good">当前无待关注异常</div></div>
      </section>
    </main>
  </template>
</div>
</template>
