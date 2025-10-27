import React from 'react'
import { Outlet, Link } from 'react-router-dom'
import '../App.css'

const DashboardLayout: React.FC = () => {
  return (
    <div className="dashboard-root" style={{ display: 'flex', minHeight: '100vh' }}>
      <aside style={{ width: 240, background: '#f8fafc', padding: '1rem', borderRight: '1px solid #e6e9ef' }}>
        <h3>EcoScore</h3>
        <nav>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            <li><Link to="/dashboard">Resumo</Link></li>
            <li><Link to="/dashboard/subpage">Subpágina</Link></li>
          </ul>
        </nav>
      </aside>
      <main style={{ flex: 1, padding: '1.25rem' }}>
        <Outlet />
      </main>
    </div>
  )
}

export default DashboardLayout

