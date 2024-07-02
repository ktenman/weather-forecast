import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    root: 'ui',
    base: '/',
    server: {
        port: 61234, // Using a non-standard port for development
        proxy: {
            '/api': {
                target: 'http://localhost:8081', // Proxy API requests to backend server
                changeOrigin: true,
            },
        },
    },
    build: {
        outDir: resolve(__dirname, '../dist'),
        sourcemap: true,
        emptyOutDir: true,
    },
    resolve: {
        alias: {
            '@': resolve(__dirname, './ui/src'),
        },
    },
})
