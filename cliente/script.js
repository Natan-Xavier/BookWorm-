// Cadastro
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
    window.location.href = 'index.html'; // 👈 redireciona pra home
  });
}

// Redirecionar para página do livro
function verLivro(nome) {
  localStorage.setItem('livroSelecionado', nome);
  window.location.href = 'livro.html';
}

// Mostrar detalhes do livro
if (window.location.pathname.endsWith('livro.html')) {
  const titulo = localStorage.getItem('livroSelecionado');
  document.getElementById('tituloLivro').innerText = titulo || 'Livro Desconhecido';
  document.getElementById('descricaoLivro').innerText =
    `Você está visualizando detalhes do livro "${titulo}". Aqui você poderá alugar em breve!`;
}