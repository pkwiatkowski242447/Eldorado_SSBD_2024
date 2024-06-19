import axios from 'axios'

export const API_URL = "https://team-3.proj-sum.it.p.lodz.pl/api/v1"
export const API_TEST_URL = "http://localhost:8080/api/v1"
export const TIMEOUT_IN_MS = 30000
export const DEFAULT_HEADERS = {
    Accept: 'application/json',
    'Content-type': 'application/json',
}
export const apiWithConfig = axios.create({
    baseURL: API_TEST_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

export const apiAnonymous = axios.create({
    baseURL: API_TEST_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

apiWithConfig.interceptors.response.use(
    (response) => response,
    (error) => {
        // const status = error.response?.status
        // if (status === 401 || status === 403) {
        //     localStorage.removeItem('token')
        // }
        return Promise.reject(error)
    },
)

apiWithConfig.interceptors.request.use((config) => {
    let token = window.localStorage.getItem('token');
    token = (token && token !== "null") ? token : null;
    if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})