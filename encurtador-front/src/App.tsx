import {useState} from 'react';
import axios from "axios";
import './App.css';

function App() {

    const [url, setUrl] = useState('');
    const [shortUrl, setShortUrl ] = useState('');
    const [loading, setLoading] = useState(false);

    const handleShorten = async () => {
        if(!url) return;
        setLoading(true);
        try{
            const response = await axios.post('http://localhost:8080/shorten', url, {
                headers: { 'content-type': 'text/plain' }
            })
            setShortUrl(response.data);
        } catch (error) {
            console.log(error);
            alert('Erro ao gerar URL');
        } finally {
            setLoading(false)
        }
    }

    return (
        <div style={{ maxWidth: '600px', margin: '0 auto', textAlign: 'center', padding: '2rem' }}>
            <h1>🚀 Encurtador High-Performance</h1>

            <div style={{ display: 'flex', gap: '10px', marginTop: '20px' }}>
                <input
                    type="text"
                    placeholder="Cole sua URL original aqui..."
                    value={url}
                    onChange={(e) => setUrl(e.target.value)}
                    style={{ flex: 1, padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}
                />
                <button
                    onClick={handleShorten}
                    disabled={loading}
                    style={{ padding: '10px 20px', cursor: 'pointer' }}
                >
                    {loading ? 'Encurtando...' : 'Encurtar'}
                </button>
            </div>

            {shortUrl && (
                <div style={{ marginTop: '20px', padding: '20px', background: '#f0f0f0', borderRadius: '8px' }}>
                    <p>Sua URL curta:</p>
                    <a href={shortUrl} target="_blank" rel="noopener noreferrer" style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>
                        {shortUrl}
                    </a>
                </div>
            )}
        </div>
    )
}

export default App;
