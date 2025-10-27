import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import '../App.css'

const LoginPage: React.FC = () => {
  const [cpf, setCpf] = useState('')
  const [senha, setSenha] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (!cpf.trim() || !senha.trim()) {
      setError('CPF e senha são obrigatórios')
      return
    }

    setLoading(true)
    try {
      const res = await fetch('/api/membro/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cpf, senha })
      })

      if (!res.ok) {
        try {
          const errJson = await res.json()
          const msg = errJson?.message || JSON.stringify(errJson)
          setError(msg || res.statusText)
        } catch {
          const txt = await res.text().catch(() => '')
          setError(txt || res.statusText)
        }
        setLoading(false)
        return
      }

      const data = await res.json().catch(() => ({}))

      // Salva os dados retornados pelo backend em localStorage como "membro"
      try {
        localStorage.setItem('membro', JSON.stringify(data))
      } catch (e) {
        console.warn('Falha ao salvar membro no localStorage', e)
      }

      // Redireciona para dashboard
      navigate('/dashboard')
    } catch (err: any) {
      setError(err?.message || 'Erro ao se comunicar com o servidor')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="bg-white flex items-center justify-center min-h-screen p-4">
      <form className="p-[40px] gap-[20px] flex flex-col bg-gray-400" onSubmit={handleSubmit}>
        <h2>Entrar — EcoScore</h2>

        {error && <div className="login-error">{error}</div>}

        <label>
          CPF
          <input
            type="text"
            value={cpf}
            onChange={(e) => setCpf(e.target.value)}
            placeholder="000.000.000-00"
            autoComplete="username"
          />
        </label>

        <label>
          Senha
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            placeholder="Senha"
            autoComplete="current-password"
          />
        </label>

        <button type="submit" disabled={loading} className="login-button">
          {loading ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
    </div>
  )
}

export default LoginPage

