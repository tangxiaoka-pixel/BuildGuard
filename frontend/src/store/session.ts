import { defineStore } from 'pinia'
import { ref } from 'vue'

const defaultContext = { type: 'platform', id: null, companyId: null, name: '平台级' }

export const useSessionStore = defineStore('session', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(localStorage.getItem('user') || '')
  const name = ref(localStorage.getItem('name') || '')
  const role = ref(localStorage.getItem('role') || '')
  const companyIds = ref<number[]>(JSON.parse(localStorage.getItem('companyIds') || '[]'))
  const companyModules = ref<Record<string, string[]>>(JSON.parse(localStorage.getItem('companyModules') || '{}'))
  const projectModules = ref<Record<string, string[]>>(JSON.parse(localStorage.getItem('projectModules') || '{}'))
  const context = ref<any>(JSON.parse(localStorage.getItem('buildguard-context') || JSON.stringify(defaultContext)))

  function login(value: { token: string; user: string; name: string; role: string; companyIds?: number[]; companyModules?: Record<string,string[]>; projectModules?: Record<string,string[]> }) {
    token.value = value.token
    user.value = value.user
    localStorage.setItem('token', value.token)
    localStorage.setItem('user', value.user)
    name.value = value.name
    localStorage.setItem('name', value.name)
    role.value = value.role
    localStorage.setItem('role', value.role)
    companyIds.value = value.companyIds || []
    localStorage.setItem('companyIds', JSON.stringify(companyIds.value))
    companyModules.value = value.companyModules || {}
    projectModules.value = value.projectModules || {}
    localStorage.setItem('companyModules', JSON.stringify(companyModules.value))
    localStorage.setItem('projectModules', JSON.stringify(projectModules.value))
  }
  function setContext(value: any) {
    context.value = value
    localStorage.setItem('buildguard-context', JSON.stringify(value))
  }
  function logout() {
    token.value = ''
    user.value = ''
    context.value = defaultContext
    localStorage.clear()
  }
  return { token, user, name, role, companyIds, companyModules, projectModules, context, login, setContext, logout }
})
