import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'

export default defineConfig({
    plugins: [vue()],
    root: 'ui',
    server: {
        port: 61234,
        proxy: {
            '/api': {
                target: 'http://localhost:8081',
                changeOrigin: true,
            },
        },
    },
    build: {
        outDir: path.resolve(__dirname, '../dist'),
        sourcemap: true,
        emptyOutDir: true,
    },
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './ui/src'),
        },
    },
})
