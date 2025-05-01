import './App.css'

export default function App() {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded shadow-md text-center">
        <h1 className="text-3xl font-bold text-blue-600 mb-4">Tailwind CSS 示例</h1>
        <p className="text-gray-700 mb-6">
          这是一个使用 Tailwind CSS 样式的简单页面。
        </p>
        <button className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded transition">
          点击我
        </button>
      </div>
    </div>
  )
}
