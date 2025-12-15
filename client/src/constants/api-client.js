import axios from 'axios';

class ApiClient {
    constructor(baseURL = 'http://localhost:8080') {
        this.client = axios.create({
            baseURL,
            timeout: 10000,
            headers: {
                'Content-Type': 'application/json',
            },
        });

        this.client.interceptors.response.use(
            (response) => response,
            (error) => {
                console.error('API Error:', error.response?.data || error.message);
                return Promise.reject(error);
            }
        );
    }

    async get(url, config = {}) {
        const response = await this.client.get(url, config);
        return response.data;
    }

    async post(url, data = {}, config = {}) {
        const response = await this.client.post(url, data, config);
        return response.data;
    }

    async put(url, data = {}, config = {}) {
        const response = await this.client.put(url, data, config);
        return response.data;
    }

    async del(url, config = {}) {
        const response = await this.client.delete(url, config);
        return response.data;
    }

    async patch(url, data = {}, config = {}) {
        const response = await this.client.patch(url, data, config);
        return response.data;
    }
}

export const api = new ApiClient('http://localhost:8080');

export default ApiClient;