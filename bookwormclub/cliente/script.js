// 📋 CADASTRO

const cadastroForm = document.getElementById('cadastroForm');
if (cadastroForm) {
  cadastroForm.addEventListener('submit', e => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const confirmEmail = document.getElementById('confirmEmail').value;
    const senha = document.getElementById('senha').value;
    const confirmSenha = document.getElementById('confirmSenha').value;

    if (email !== confirmEmail) return alert('Os e-mails não coincidem!');
    if (senha !== confirmSenha) return alert('As senhas não coincidem!');

    localStorage.setItem('user', JSON.stringify({ email, senha }));
    alert('Cadastro realizado com sucesso!');
    window.location.href = 'login.html';
  });
}

// LOGIN

const loginForm = document.getElementById('loginForm');
if (loginForm) {
  loginForm.addEventListener('submit', e => {
    e.preventDefault();

    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;
    const user = JSON.parse(localStorage.getItem('user'));

    if (!user || email !== user.email || senha !== user.senha) {
      alert('Email ou senha incorretos!');
      return;
    }

    alert('Login bem-sucedido!');
    window.location.href = 'index.html';
  });
}


// REDIRECIONAR PARA LIVRO

function verLivro(nome) {
  localStorage.setItem('livroSelecionado', nome);
  window.location.href = 'livro.html';
}

//  PÁGINA DO LIVRO

if (window.location.pathname.endsWith('livro.html')) {
  const titulo = localStorage.getItem('livroSelecionado');

  // 🔹 Buscar livros salvos pelos funcionários
  const livros = JSON.parse(localStorage.getItem('livros')) || [];
  const livro = livros.find(l => l.titulo === titulo) || {
    capa: "",
    descricao: "Informações não disponíveis."
  };

  document.getElementById('tituloLivro').innerText = titulo || 'Livro Desconhecido';
  document.getElementById('descricaoLivro').innerText = livro.descricao || "Sem descrição.";
  document.getElementById('livroCapa').src = livro.capa || "";

  const btnFav = document.getElementById('btnFavoritar');
  const btnAlugar = document.getElementById('btnAlugar');
  const msg = document.getElementById('mensagem');

  // Favoritar
  btnFav.addEventListener('click', () => {
    let favoritos = JSON.parse(localStorage.getItem('favoritos')) || [];
    if (!favoritos.includes(titulo)) {
      favoritos.push(titulo);
      localStorage.setItem('favoritos', JSON.stringify(favoritos));
      msg.innerText = "💖 Adicionado aos favoritos!";
    } else {
      msg.innerText = "✨ Já está nos seus favoritos!";
    }
  });

  // Alugar
  btnAlugar.addEventListener('click', () => {
    let alugados = JSON.parse(localStorage.getItem('alugados')) || [];
    if (!alugados.includes(titulo)) {
      alugados.push(titulo);
      localStorage.setItem('alugados', JSON.stringify(alugados));
      msg.innerText = "📚 Livro alugado com sucesso!";
    } else {
      msg.innerText = "🔁 Você já alugou este livro.";
    }
  });
}

//  HOME

if (window.location.pathname.endsWith('index.html')) {
  const listaLivros = document.getElementById('listaLivros');
  const listaFavoritos = document.getElementById('listaFavoritos');
  const campoBusca = document.getElementById('campoBusca');

  // 🔹 Buscar os livros adicionados pelos funcionários
  function getLivros() {
    return JSON.parse(localStorage.getItem('livros')) || [];
  }

  function renderizarLivros(filtro = "") {
    listaLivros.innerHTML = "";
    const livros = getLivros();

    if (livros.length === 0) {
      listaLivros.innerHTML = "<p>Nenhum livro disponível ainda 📖</p>";
      return;
    }

    livros
      .filter(l => l.titulo.toLowerCase().includes(filtro.toLowerCase()))
      .forEach(l => {
        const div = document.createElement('div');
        div.classList.add('livro');
        div.innerHTML = `
          <img src="${l.capa}" alt="${l.titulo}">
          <p>${l.titulo}</p>
        `;
        div.onclick = () => verLivro(l.titulo);
        listaLivros.appendChild(div);
      });
  }

  function renderizarFavoritos() {
    listaFavoritos.innerHTML = "";
    const livros = getLivros();
    const favoritos = JSON.parse(localStorage.getItem('favoritos')) || [];

    if (favoritos.length === 0) {
      listaFavoritos.innerHTML = "<p>Nenhum favorito ainda 😢</p>";
      return;
    }

    favoritos.forEach(titulo => {
      const livro = livros.find(l => l.titulo === titulo);
      if (livro) {
        const div = document.createElement('div');
        div.classList.add('livro');
        div.innerHTML = `
          <img src="${livro.capa}" alt="${livro.titulo}">
          <p>${livro.titulo}</p>
        `;
        div.onclick = () => verLivro(livro.titulo);
        listaFavoritos.appendChild(div);
      }
    });
  }

  //  Busca
  window.filtrarLivros = function () {
    const filtro = campoBusca.value;
    renderizarLivros(filtro);
  };

  // Inicializar
  renderizarLivros();
  renderizarFavoritos();
}


// PERFIL DO USUÁRIO

if (window.location.pathname.endsWith('perfil.html')) {
  const emailUsuario = document.getElementById('emailUsuario');
  const listaFavoritosPerfil = document.getElementById('listaFavoritosPerfil');
  const listaAlugados = document.getElementById('listaAlugados');
  const btnLogout = document.getElementById('btnLogout');

  const user = JSON.parse(localStorage.getItem('user'));
  const favoritos = JSON.parse(localStorage.getItem('favoritos')) || [];
  const alugados = JSON.parse(localStorage.getItem('alugados')) || [];
  const livros = JSON.parse(localStorage.getItem('livros')) || [];

  // Mostra email
  emailUsuario.innerText = user?.email || "Usuário não identificado";

  // Renderizar favoritos
  if (favoritos.length > 0) {
    favoritos.forEach(titulo => {
      const livro = livros.find(l => l.titulo === titulo);
      if (livro) {
        const div = document.createElement('div');
        div.classList.add('livro');
        div.innerHTML = `
          <img src="${livro.capa}" alt="${livro.titulo}">
          <p>${livro.titulo}</p>
        `;
        div.onclick = () => verLivro(livro.titulo);
        listaFavoritosPerfil.appendChild(div);
      }
    });
  } else {
    listaFavoritosPerfil.innerHTML = "<p>Nenhum favorito ainda</p>";
  }

  // Renderizar alugados
  if (alugados.length > 0) {
    alugados.forEach(titulo => {
      const livro = livros.find(l => l.titulo === titulo);
      if (livro) {
        const div = document.createElement('div');
        div.classList.add('livro');
        div.innerHTML = `
          <img src="${livro.capa}" alt="${livro.titulo}">
          <p>${livro.titulo}</p>
        `;
        div.onclick = () => verLivro(livro.titulo);
        listaAlugados.appendChild(div);
      }
    });
  } else {
    listaAlugados.innerHTML = "<p>Nenhum livro alugado ainda 📖</p>";
  }

  // Logout
  btnLogout.addEventListener('click', () => {
    if (confirm('Deseja realmente sair?')) {
      localStorage.removeItem('user');
      window.location.href = 'login.html';
    }
  });
}