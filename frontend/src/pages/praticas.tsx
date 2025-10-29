import React, {useEffect, useState} from "react";

type Dificuldade = "FACIL" | "MEDIA" | "DIFICIL";

type Frequencia = "diaria" | "semanal" | "mensal" | "anual";

type Pratica = {
    id_pratica?: number;
    id_categoria: number;
    nome: string;
    descricao: string;
    pontos_base: number;
    dificuldade: Dificuldade;
    frequencia_esperada: Frequencia;
};

type Familia = {
    id_familia: number;
    nome: string;
};

export default function PraticasPage() {
    const [praticas, setPraticas] = useState<Pratica[]>([]);
    const [categorias, setCategorias] = useState<{ id_categoria: number; nome: string }[]>([]);
    const [selectedFamily, setSelectedFamily] = useState<Familia | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [editingId, setEditingId] = useState<number | null>(null);

    const [form, setForm] = useState<Pratica>({
        id_categoria: 1,
        nome: "",
        descricao: "",
        pontos_base: 10,
        dificuldade: "FACIL",
        frequencia_esperada: "diaria",
    });

    useEffect(() => {
        fetchData();
        fetchFamilias();
        fetchCategorias();
    }, []);

    async function fetchData() {
        setLoading(true);
        try {
            const res = await fetch("http://localhost:8080/api/pratica-sustentavel");
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            const data = await res.json();
            setPraticas(data || []);
        } catch (e: any) {
            setError("Erro ao carregar práticas: " + (e.message || e));
        } finally {
            setLoading(false);
        }
    }

    async function fetchFamilias() {
        try {
            const res = await fetch("http://localhost:8080/api/familia");
            if (!res.ok) return;
            const data = await res.json();
            if (data && data.length > 0) {
                const rand = data[Math.floor(Math.random() * data.length)];
                setSelectedFamily(rand);
            }
        } catch (e) {
            // ignore
        }
    }

    async function fetchCategorias() {
        try {
            const res = await fetch("http://localhost:8080/api/categoria-pratica");
            if (!res.ok) return;
            const data = await res.json();
            if (data && data.length > 0) {
                setCategorias(data);
                // set default category in form if currently default
                setForm((f) => ({...f, id_categoria: data[0].id_categoria}));
            }
        } catch (e) {
            // ignore
        }
    }

    function handleChange<K extends keyof Pratica>(key: K, value: Pratica[K]) {
        setForm((s) => ({...s, [key]: value}));
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setError(null);
        try {
            if (editingId != null) {
                // update via PUT (controller expects JSON @RequestBody)
                const res = await fetch(`http://localhost:8080/api/pratica-sustentavel/${editingId}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        id_categoria: form.id_categoria,
                        nome: form.nome,
                        descricao: form.descricao,
                        pontos_base: form.pontos_base,
                        dificuldade: form.dificuldade,
                        frequencia_esperada: form.frequencia_esperada,
                    }),
                });
                if (!res.ok) {
                    const text = await res.text();
                    throw new Error(text || `HTTP ${res.status}`);
                }
                setEditingId(null);
            } else {
                // create via POST as form data (existing backend binding)
                const params = new URLSearchParams();
                params.append("id_categoria", String(form.id_categoria));
                params.append("nome", form.nome);
                params.append("descricao", form.descricao);
                params.append("pontos_base", String(form.pontos_base));
                params.append("dificuldade", String(form.dificuldade));
                params.append("frequencia_esperada", form.frequencia_esperada);

                const res = await fetch("http://localhost:8080/api/pratica-sustentavel", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
                    },
                    body: params.toString(),
                });
                if (!res.ok) {
                    const text = await res.text();
                    throw new Error(text || `HTTP ${res.status}`);
                }
            }

            // refresh list
            await fetchData();
            // reset form
            setForm({
                id_categoria: form.id_categoria,
                nome: "",
                descricao: "",
                pontos_base: 10,
                dificuldade: "FACIL",
                frequencia_esperada: "diaria",
            });
        } catch (e: any) {
            setError("Erro ao salvar prática: " + (e.message || e));
        }
    }

    function handleEdit(p: Pratica) {
        // populate form and enter edit mode
        setForm({
            id_categoria: p.id_categoria,
            nome: p.nome,
            descricao: p.descricao,
            pontos_base: p.pontos_base,
            dificuldade: p.dificuldade,
            frequencia_esperada: p.frequencia_esperada,
        });
        setEditingId(p.id_pratica ?? null);
    }

    async function handleDelete(id?: number) {
        if (!id) return;
        const ok = window.confirm("Tem certeza que deseja deletar esta prática?");
        if (!ok) return;
        try {
            const res = await fetch(`http://localhost:8080/api/pratica-sustentavel/${id}`, { method: "DELETE" });
            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || `HTTP ${res.status}`);
            }
            // refresh
            await fetchData();
        } catch (e: any) {
            setError("Erro ao deletar prática: " + (e.message || e));
        }
    }

    return (
        <div className="flex flex-col min-h-screen">
            {/*Header*/}
            <div className="w-full flex flex-row p-7 bg-white rounded-b-[10px] shadow">
                <p className="text-2xl font-bold">EcoScore</p>
            </div>
            {/* Conteúdo*/}
            <div className="flex-1 w-full overflow-auto flex items-center justify-center">
                <div className="w-full mx-70">
                    <h1 className="text-2xl font-semibold mb-4">Criar Prática Sustentável</h1>

                    {selectedFamily && (
                        <div className="mb-4 text-sm text-gray-600">
                            Família aleatória
                            selecionada: <strong>{selectedFamily.nome}</strong> (id={selectedFamily.id_familia})
                        </div>
                    )}

                    <div className="flex gap-20">
                        {/*Formulário*/}
                        <form onSubmit={handleSubmit} className="w-[30%] bg-white p-4 rounded shadow">
                            <div className="mb-3">
                                <label className="block text-sm font-medium">Categoria</label>
                                <select
                                    value={form.id_categoria}
                                    onChange={(e) => handleChange("id_categoria", Number(e.target.value))}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                >
                                    {categorias.length === 0 ? (
                                        <option value={1}>Categoria 1</option>
                                    ) : (
                                        categorias.map((c) => (
                                            <option key={c.id_categoria} value={c.id_categoria}>
                                                {c.nome}
                                            </option>
                                        ))
                                    )}
                                </select>
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium">Nome</label>
                                <input
                                    value={form.nome}
                                    onChange={(e) => handleChange("nome", e.target.value)}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                    required
                                />
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium">Descrição</label>
                                <textarea
                                    value={form.descricao}
                                    onChange={(e) => handleChange("descricao", e.target.value)}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium">Pontos base</label>
                                <input
                                    type="number"
                                    value={form.pontos_base}
                                    onChange={(e) => handleChange("pontos_base", Number(e.target.value))}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium">Dificuldade</label>
                                <select
                                    value={form.dificuldade}
                                    onChange={(e) => handleChange("dificuldade", e.target.value as Dificuldade)}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                >
                                    <option value="FACIL">Fácil</option>
                                    <option value="MEDIA">Média</option>
                                    <option value="DIFICIL">Difícil</option>
                                </select>
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium">Frequência esperada</label>
                                <select
                                    value={form.frequencia_esperada}
                                    onChange={(e) => handleChange("frequencia_esperada", e.target.value as Frequencia)}
                                    className="mt-1 block w-full rounded border px-2 py-1"
                                >
                                    <option value="diaria">Diária</option>
                                    <option value="semanal">Semanal</option>
                                    <option value="mensal">Mensal</option>
                                    <option value="anual">Anual</option>
                                </select>
                            </div>

                            <div className="flex items-center gap-2">
                                <button
                                    type="submit"
                                    className="bg-primary text-white px-3 py-1 rounded"
                                >
                                    {editingId != null ? "Salvar" : "Criar"}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => { setForm({
                                        id_categoria: 1,
                                        nome: "",
                                        descricao: "",
                                        pontos_base: 10,
                                        dificuldade: "FACIL",
                                        frequencia_esperada: "diaria"
                                    }); setEditingId(null); }}
                                    className="px-3 py-1 rounded border"
                                >
                                    Limpar
                                </button>
                            </div>

                            {error && <div className="mt-3 text-sm text-red-600">{error}</div>}
                        </form>

                        {/*Lista de práticas*/}
                        <div className="flex-1 bg-white p-4 rounded shadow overflow-auto">
                            <h2 className="text-lg font-medium mb-3">Práticas cadastradas</h2>
                            {loading ? (
                                <div>Carregando...</div>
                            ) : (
                                <table className="w-full text-sm">
                                    <thead>
                                    <tr className="text-left">
                                        <th className="py-1">ID</th>
                                        <th>Nome</th>
                                        <th>Categoria</th>
                                        <th>Pontos</th>
                                        <th>Dificuldade</th>
                                        <th className="text-right">Ações</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {praticas.map((p) => (
                                        <tr key={p.id_pratica} className="border-t">
                                            <td className="py-1">{p.id_pratica}</td>
                                            <td>{p.nome}</td>
                                            <td>{p.id_categoria}</td>
                                            <td>{p.pontos_base}</td>
                                            <td>{p.dificuldade}</td>
                                            <td className="py-1 text-right">
                                                <div className="inline-flex items-center gap-2">
                                                    <button
                                                        type="button"
                                                        onClick={() => handleEdit(p)}
                                                        title="Editar"
                                                        className="p-1 rounded hover:bg-gray-100"
                                                    >
                                                        {/* pencil icon */}
                                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5h6M4 7v13h13v-6M17 3l4 4-7 7H10v-4l7-7z" />
                                                        </svg>
                                                    </button>
                                                    <button
                                                        type="button"
                                                        onClick={() => handleDelete(p.id_pratica)}
                                                        title="Apagar"
                                                        className="p-1 rounded hover:bg-gray-100 text-red-600"
                                                    >
                                                        {/* trash icon */}
                                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6M1 7h22M10 3h4a1 1 0 011 1v1H9V4a1 1 0 011-1z" />
                                                        </svg>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
