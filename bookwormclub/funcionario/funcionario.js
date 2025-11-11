// ===== Funções utilitárias =====
function getJSON(key) {
  try { return JSON.parse(localStorage.getItem(key)); } catch { return null; }
}
function setJSON(key, val) {
  localStorage.setItem(key, JSON.stringify(val));
}

// ===== Cadastro de Funcionário =====
const cadastroForm = document.getElementById('cadastroFuncionarioForm');
if (cadastroForm) {
  cadastroForm.addEventListener('submit', e => {
    e.preventDefault();

    const nome = document.getElementById('nome').value.trim();
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;

    if (senha !== confirmarSenha) {
      alert('As senhas não coincidem!');
      return;
    }

    const funcionarios = getJSON('funcionarios') || [];
    if (funcionarios.some(f => f.email === email)) {
      alert('Este e-mail já está cadastrado!');
      return;
    }

    funcionarios.push({ nome, email, senha });
    setJSON('funcionarios', funcionarios);
    alert('Cadastro realizado com sucesso!');
    window.location.href = 'login-funcionario.html';
  });
}

// ===== Login de Funcionário =====
const loginForm = document.getElementById('loginFuncionarioForm');
if (loginForm) {
  loginForm.addEventListener('submit', e => {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const funcionarios = getJSON('funcionarios') || [];

    const funcionario = funcionarios.find(f => f.email === email && f.senha === senha);

    if (!funcionario) {
      alert('Email ou senha incorretos!');
      return;
    }

    setJSON('funcionarioLogado', funcionario);
    alert(`Bem-vindo(a), ${funcionario.nome}!`);
    window.location.href = 'area-funcionario.html';
  });
}

// ===== Área do Funcionário =====
if (window.location.pathname.includes('area-funcionario.html')) {
  const funcionario = getJSON('funcionarioLogado');
  if (!funcionario) {
    alert('Acesso negado. Faça login primeiro.');
    window.location.href = 'login-funcionario.html';
  }

  const form = document.getElementById('formLivro');
  const lista = document.getElementById('listaLivros');
  const logoutBtn = document.getElementById('logoutBtn');

  function getLivros() {
    return getJSON('livros') || [];
  }

  function saveLivros(livros) {
    setJSON('livros', livros);
  }

  function renderizarLivros() {
    const livros = getLivros();
    lista.innerHTML = '';

    if (livros.length === 0) {
      lista.innerHTML = '<p>Nenhum livro cadastrado ainda.</p>';
      return;
    }

    livros.forEach((livro, index) => {
      const div = document.createElement('div');
      div.classList.add('livro-item');
      div.innerHTML = `
        <img src="${livro.capa}" alt="${livro.titulo}" width="60" style="border-radius:6px; vertical-align:middle;">
        <strong style="margin-left:10px">${livro.titulo}</strong>
        <button data-index="${index}" style="margin-left:10px;">Remover</button>
      `;
      lista.appendChild(div);
    });

    lista.querySelectorAll('button[data-index]').forEach(btn => {
      btn.addEventListener('click', () => {
        const idx = parseInt(btn.getAttribute('data-index'));
        const livros = getLivros();
        const [removido] = livros.splice(idx, 1);
        saveLivros(livros);
        renderizarLivros();
        alert(`Livro "${removido.titulo}" removido!`);
      });
    });
  }

  form.addEventListener('submit', e => {
    e.preventDefault();
    const titulo = document.getElementById('titulo').value.trim();
    const capa = document.getElementById('capa').value.trim();

    if (!titulo || !capa) {
      alert('Preencha todos os campos!');
      return;
    }

    const livros = getLivros();
    livros.push({ titulo, capa });
    saveLivros(livros);
    form.reset();
    renderizarLivros();
    alert('Livro adicionado com sucesso!');
  });

  logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('funcionarioLogado');
    window.location.href = 'login-funcionario.html';
  });

  renderizarLivros();
}