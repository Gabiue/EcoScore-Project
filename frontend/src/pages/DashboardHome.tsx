import React from 'react'

const DashboardHome: React.FC = () => {
  const membroJson = localStorage.getItem('membro')
  const membro = membroJson ? JSON.parse(membroJson) : null

  return (
    <div>
      <h2>Dashboard — Resumo</h2>
      <p>Bem-vindo, {membro?.nome || membro?.cpf || 'Membro'}!</p>

      <section>
        <h3>Dados do membro (raw)</h3>
        <pre style={{ background: '#f3f4f6', padding: 8 }}>{JSON.stringify(membro, null, 2)}</pre>
      </section>
    </div>
  )
}

export default DashboardHome

