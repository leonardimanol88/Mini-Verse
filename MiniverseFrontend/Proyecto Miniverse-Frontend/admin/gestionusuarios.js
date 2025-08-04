document.addEventListener('DOMContentLoaded', function() {
    // Configuración base
    const API_BASE_URL = 'http://44.209.91.221:7002';
    
    // Elementos del DOM
    const searchUserInput = document.getElementById('searchUserInput');
    const searchUserBtn = document.getElementById('searchUserBtn');
    const searchResults = document.getElementById('searchResults');
    const usersList = document.getElementById('usersList');
    const userProfile = document.getElementById('userProfile');
    const sectionsContainer = document.getElementById('sectionsContainer');
    const actionButtons = document.getElementById('actionButtons');
    const userName = document.getElementById('userName');
    const userEmail = document.getElementById('userEmail');
    const userAge = document.getElementById('userAge');
    const userId = document.getElementById('userId');
    const reviewsList = document.getElementById('reviewsList');
    const commentsList = document.getElementById('commentsList');
    const deleteUserBtn = document.getElementById('deleteUserBtn');
    const confirmModal = document.getElementById('confirmModal');
    const modalMessage = document.getElementById('modalMessage');
    const confirmActionBtn = document.getElementById('confirmActionBtn');
    const cancelActionBtn = document.getElementById('cancelActionBtn');
    const closeModal = document.querySelector('.close-modal');

    // Variables de estado
    let currentUser = null;
    let currentAction = null;
    let currentItemId = null;

    // Inicialización
    
    setupEventListeners();

    // Funciones principales
    function checkAuth() {
        const token = localStorage.getItem('token');
        if (!token) {
            alert('No estás autenticado. Serás redirigido al login.');
            window.location.href = '/login.html';
        }
    }

    function setupEventListeners() {
        // Búsqueda
        searchUserBtn.addEventListener('click', searchUsers);
        searchUserInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') searchUsers();
        });
        
        // Eliminación
        deleteUserBtn.addEventListener('click', () => {
            showModal('Eliminar Usuario', '¿Estás seguro de que deseas eliminar este usuario?', 'deleteUser');
        });
        
        // Modal
        confirmActionBtn.addEventListener('click', executeAction);
        cancelActionBtn.addEventListener('click', hideModal);
        closeModal.addEventListener('click', hideModal);
        
        // Secciones plegables
        document.querySelectorAll('.section-header').forEach(header => {
            header.addEventListener('click', function() {
                toggleSection(this);
            });
        });
    }

    function toggleSection(header) {
        const content = header.nextElementSibling;
        const arrow = header.querySelector('.dropdown-arrow');
        
        if (content.classList.contains('open')) {
            content.classList.remove('open');
            arrow.classList.remove('rotated');
        } else {
            content.classList.add('open');
            arrow.classList.add('rotated');
        }
    }

    function searchUsers() {
        const searchTerm = searchUserInput.value.trim();
        if (!searchTerm) return;

        // Mostrar loading
        searchUserBtn.innerHTML = '<span class="loading-spinner"></span>';
        
        fetch(`${API_BASE_URL}/admin/busquedaUsuarios?nombre=${encodeURIComponent(searchTerm)}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    localStorage.removeItem('token');
                    window.location.href = '/login.html';
                }
                throw new Error(`Error HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(users => {
            displaySearchResults(users);
        })
        .catch(error => {
            console.error('Error en la búsqueda:', error);
            alert('Error al buscar usuarios: ' + error.message);
        })
        .finally(() => {
            // Restaurar botón de búsqueda
            searchUserBtn.innerHTML = `
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M21 21L16.514 16.506M19 10.5C19 15.194 15.194 19 10.5 19C5.806 19 2 15.194 2 10.5C2 5.806 5.806 2 10.5 2C15.194 2 19 5.806 19 10.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            `;
        });
    }

    function displaySearchResults(users) {
        usersList.innerHTML = '';
        
        if (users.length === 0) {
            usersList.innerHTML = '<div class="user-item">No se encontraron usuarios</div>';
            searchResults.style.display = 'block';
            return;
        }

        users.forEach(user => {
            const userItem = document.createElement('div');
            userItem.className = 'user-item';
            userItem.innerHTML = `
                <div>
                    <div class="user-item-name">${user.nombre}</div>
                    <div class="user-item-email">${user.correo}</div>
                </div>
                <div>ID: ${user.id}</div>
            `;
            userItem.addEventListener('click', () => loadUserDetails(user));
            usersList.appendChild(userItem);
        });

        searchResults.style.display = 'block';
    }

    function loadUserDetails(user) {
        currentUser = user;
        
        // Mostrar información básica del usuario
        userName.value = user.nombre;
        userEmail.value = user.correo;
        userAge.value = user.edad;
        userId.value = user.id;
        
        // Mostrar secciones
        userProfile.style.display = 'flex';
        sectionsContainer.style.display = 'flex';
        actionButtons.style.display = 'flex';
        searchResults.style.display = 'none';
        
        // Cargar reseñas y comentarios
        loadUserReviews(user.id);
        loadUserComments(user.id);
    }

    function loadUserReviews(userId) {
        fetch(`${API_BASE_URL}/admin/obtenerResenasdelUsuario?id=${userId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al obtener reseñas');
            return response.json();
        })
        .then(reviews => {
            displayReviews(reviews);
        })
        .catch(error => {
            console.error('Error:', error);
            reviewsList.innerHTML = '<div class="review-item">Error al cargar reseñas</div>';
        });
    }

    function displayReviews(reviews) {
        reviewsList.innerHTML = '';
        
        if (reviews.length === 0) {
            reviewsList.innerHTML = '<div class="review-item">El usuario no tiene reseñas</div>';
            return;
        }

        reviews.forEach(review => {
            const reviewItem = document.createElement('div');
            reviewItem.className = 'review-item';
            reviewItem.innerHTML = `
                <div class="review-content">${review.contenido}</div>
                <button class="delete-review-btn" data-id="${review.id}">Eliminar</button>
            `;
            reviewsList.appendChild(reviewItem);
        });

        // Agregar event listeners a los botones de eliminar
        document.querySelectorAll('.delete-review-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                const reviewId = btn.getAttribute('data-id');
                showModal('Eliminar Reseña', '¿Estás seguro de que deseas eliminar esta reseña?', 'deleteReview', reviewId);
            });
        });
    }

    function loadUserComments(userId) {
        fetch(`${API_BASE_URL}/admin/obtenerComentariosdelUsuario?id=${userId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al obtener comentarios');
            return response.json();
        })
        .then(comments => {
            displayComments(comments);
        })
        .catch(error => {
            console.error('Error:', error);
            commentsList.innerHTML = '<div class="comment-item">Error al cargar comentarios</div>';
        });
    }

    function displayComments(comments) {
        commentsList.innerHTML = '';
        
        if (comments.length === 0) {
            commentsList.innerHTML = '<div class="comment-item">El usuario no tiene comentarios</div>';
            return;
        }

        comments.forEach(comment => {
            const commentItem = document.createElement('div');
            commentItem.className = 'comment-item';
            commentItem.innerHTML = `
                <div class="comment-content">${comment.contenido}</div>
                <button class="delete-comment-btn" data-id="${comment.id}">Eliminar</button>
            `;
            commentsList.appendChild(commentItem);
        });

        // Agregar event listeners a los botones de eliminar
        document.querySelectorAll('.delete-comment-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                const commentId = btn.getAttribute('data-id');
                showModal('Eliminar Comentario', '¿Estás seguro de que deseas eliminar este comentario?', 'deleteComment', commentId);
            });
        });
    }

    function showModal(title, message, action, itemId = null) {
        document.getElementById('modalTitle').textContent = title;
        modalMessage.textContent = message;
        currentAction = action;
        currentItemId = itemId;
        confirmModal.style.display = 'block';
    }

    function hideModal() {
        confirmModal.style.display = 'none';
    }

    function executeAction() {
        hideModal();
        
        switch(currentAction) {
            case 'deleteUser':
                deleteUser(currentUser.id);
                break;
            case 'deleteReview':
                deleteReview(currentItemId);
                break;
            case 'deleteComment':
                deleteComment(currentItemId);
                break;
        }
    }

    function deleteUser(userId) {
        fetch(`${API_BASE_URL}/admin/eliminarUsuario?id=${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al eliminar usuario');
            return response.json();
        })
        .then(data => {
            alert('Usuario eliminado correctamente');
            resetUI();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar usuario');
        });
    }

    function deleteReview(reviewId) {
        fetch(`${API_BASE_URL}/admin/eliminarResena?id=${reviewId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al eliminar reseña');
            return response.json();
        })
        .then(data => {
            alert('Reseña eliminada correctamente');
            loadUserReviews(currentUser.id);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar reseña');
        });
    }

    function deleteComment(commentId) {
        fetch(`${API_BASE_URL}/admin/eliminarComentario?id=${commentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Error al eliminar comentario');
            return response.json();
        })
        .then(data => {
            alert('Comentario eliminado correctamente');
            loadUserComments(currentUser.id);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar comentario');
        });
    }

    function resetUI() {
        searchUserInput.value = '';
        usersList.innerHTML = '';
        searchResults.style.display = 'none';
        userProfile.style.display = 'none';
        sectionsContainer.style.display = 'none';
        actionButtons.style.display = 'none';
        reviewsList.innerHTML = '';
        commentsList.innerHTML = '';
        currentUser = null;
    }

    // Cerrar modal al hacer clic fuera del contenido
    window.addEventListener('click', (event) => {
        if (event.target === confirmModal) {
            hideModal();
        }
    });
});