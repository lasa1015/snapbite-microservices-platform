/** @type {import('tailwindcss').Config} */

import defaultTheme from 'tailwindcss/defaultTheme';



export default {
  // 指定哪些文件中会使用到 Tailwind 的类名
  content: [
    "./index.html",                      // 扫描 index.html 文件
    "./src/**/*.{js,ts,jsx,tsx}",        // 扫描 src 文件夹下所有 JS/TS/JSX/TSX 文件
  ],
  theme: {
    extend: {
      fontFamily: {
        calsans: ['"CalSans"', ...defaultTheme.fontFamily.sans],
        opensans: ['OpenSans', ...defaultTheme.fontFamily.sans],
      }
    }    

  },
  plugins: [
    // 这里可以添加插件，例如：
    // require('@tailwindcss/forms'),
    // require('@tailwindcss/typography'),
  ],
}
