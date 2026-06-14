import axios from 'axios'
export const api=axios.create({baseURL:`${import.meta.env.BASE_URL}api`,timeout:10000})
api.interceptors.request.use(config=>{const token=localStorage.getItem('token');if(token)config.headers.Authorization=`Bearer ${token}`;return config})
api.interceptors.response.use(r=>r.data.data,e=>{
  const message=e.response?.data?.message||e.message
  if(message==='登录状态已失效，请重新登录'){
    localStorage.clear()
    if(location.pathname==='/')location.reload()
  }
  return Promise.reject(new Error(message))
})
export const get=async(url:string):Promise<any>=>api.get(url)
export const post=async(url:string,data:unknown={}):Promise<any>=>api.post(url,data)
export const put=async(url:string,data:unknown={}):Promise<any>=>api.put(url,data)
export const del=async(url:string):Promise<any>=>api.delete(url)
