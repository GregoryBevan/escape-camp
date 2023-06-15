import { defineConfig } from 'vite'
import dns from 'dns'

dns.setDefaultResultOrder('verbatim')

export default defineConfig({
    server: {
        proxy: {
            // with options: http://localhost:5173/api/bar-> http://jsonplaceholder.typicode.com/bar
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
            },
        },
    },
})
