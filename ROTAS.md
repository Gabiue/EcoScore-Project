# ROTAS.md

# API Usuário

## 1. Listar todos os usuários

* **Método:** `GET`
* **URL:** `/usuarios`
* **Body:** *(não precisa)*
* **Exemplo de resposta:**

```json
[
  {
    "id": 1,
    "nome": "Geovane Saraiva",
    "email": "geovane@ucb.br"
  },
  {
    "id": 2,
    "nome": "Maria Silva",
    "email": "maria@email.com"
  }
]
```

---

## 2. Buscar usuário por ID

* **Método:** `GET`
* **URL:** `/usuarios/{id}`
* **Body:** *(não precisa)*
* **Exemplo de resposta:**

```json
{
  "id": 1,
  "nome": "Geovane Saraiva",
  "email": "geovane@ucb.br"
}
```

---

## 3. Criar novo usuário

* **Método:** `POST`
* **URL:** `/usuarios`
* **Body (JSON):**

```json
{
  "nome": "João Almeida",
  "email": "joao@email.com"
}
```

* **Exemplo de resposta:**

```json
"Usuário criado com sucesso!"
```

---

## 4. Atualizar usuário existente

* **Método:** `PUT`
* **URL:** `/usuarios/{id}`
* **Body (JSON):**

```json
{
  "nome": "Geovane Atualizado",
  "email": "novoemail@ucb.br"
}
```

* **Exemplo de resposta:**

```json
"Usuário atualizado com sucesso!"
```

---

## 5. Deletar usuário

* **Método:** `DELETE`
* **URL:** `/usuarios/{id}`
* **Body:** *(não precisa)*
* **Exemplo de resposta:**

```json
"Usuário deletado com sucesso!"
```