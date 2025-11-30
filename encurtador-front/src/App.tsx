import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

interface UrlData {
    id: number;
    originalUrl: string;
    shortCode: string;
    clickCount: number;
    createdAt: string;
}

function App() {
    const [url, setUrl] = useState('')
    const [urls, setUrls] = useState<UrlData[]>([])
    const [loading, setLoading] = useState(false)

    const fetchStats = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/stats')
            setUrls(response.data)
        } catch (error) {
            console.error("Erro ao buscar estatísticas", error)
        }
    }

    useEffect(() => {
        fetchStats();
        const interval = setInterval(fetchStats, 2000);
        return () => clearInterval(interval);
    }, [])

    const handleShorten = async () => {
        if (!url) return
        setLoading(true)
        try {
            await axios.post('http://localhost:8080/shorten', url, {
                headers: { 'Content-Type': 'text/plain' }
            })
            setUrl('')
            fetchStats()
        } catch (error) {
            alert('Erro ao encurtar URL')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div style={{ maxWidth: '800px', margin: '0 auto', padding: '2rem', fontFamily: 'Arial, sans-serif' }}>
            <h1 style={{ textAlign: 'center', color: '#333' }}>🚀 Dashboard High-Performance</h1>
            <div style={{ display: 'flex', gap: '10px', marginBottom: '40px', padding: '20px', background: '#f8f9fa', borderRadius: '8px' }}>
                <input
                    type="text"
                    placeholder="Cole sua URL original aqui..."
                    value={url}
                    onChange={(e) => setUrl(e.target.value)}
                    style={{ flex: 1, padding: '12px', borderRadius: '6px', border: '1px solid #ddd', fontSize: '16px' }}
                />
                <button
                    onClick={handleShorten}
                    disabled={loading}
                    style={{ padding: '12px 24px', background: '#007bff', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }}
                >
                    {loading ? 'Criando...' : 'Encurtar'}
                </button>
            </div>
            <h2 style={{ color: '#555' }}>Últimos Links & Tráfego</h2>
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px', boxShadow: '0 2px 15px rgba(0,0,0,0.1)' }}>
                <thead>
                <tr style={{ background: '#007bff', color: 'white', textAlign: 'left' }}>
                    <th style={{ padding: '12px' }}>Código</th>
                    <th style={{ padding: '12px' }}>URL Original</th>
                    <th style={{ padding: '12px', textAlign: 'center' }}>🔥 Cliques</th>
                </tr>
                </thead>
                <tbody>
                {urls.map((item) => (
                    <tr key={item.id} style={{ borderBottom: '1px solid #eee' }}>
                        <td style={{ padding: '12px' }}>
                            <a href={`http://localhost:8080/${item.shortCode}`} target="_blank" style={{ color: '#007bff', fontWeight: 'bold', textDecoration: 'none' }}>
                                {item.shortCode}
                            </a>
                        </td>
                        <td style={{ padding: '12px', maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', color: '#666' }}>
                            {item.originalUrl}
                        </td>
                        <td style={{ padding: '12px', textAlign: 'center', fontSize: '1.2rem', fontWeight: 'bold', color: item.clickCount > 0 ? '#28a745' : '#ccc' }}>
                            {item.clickCount}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default App