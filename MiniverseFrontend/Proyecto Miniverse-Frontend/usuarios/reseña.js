document.addEventListener('DOMContentLoaded', async () => {
    actualizarUI();
    configurarEventos();
    
    // Obtener parámetros de la URL
    const urlParams = new URLSearchParams(window.location.search);
    const idCapitulo = urlParams.get('idCapitulo');
    
    // Validar idCapitulo
    if (!idCapitulo || isNaN(idCapitulo)) {
        mostrarError('ID de capítulo no válido');
        return;
    }
    
    // Guardar en localStorage para referencia
    localStorage.setItem('currentCapituloId', idCapitulo);
    
    // Obtener idSerie de localStorage
    const idSerie = localStorage.getItem('currentSerieId');
    
    console.log('Datos cargados - ID Capítulo:', idCapitulo, 'ID Serie:', idSerie);
    
    if (!idSerie) {
        mostrarError('No se especificó una serie');
        return;
    }

    try {
        await Promise.all([
            cargarDatosSerie(idSerie),
            cargarResenas(idCapitulo),
            cargarDirector(idSerie)
        ]);
        
        // Configurar botón de favoritos con el ID de serie
        configurarBotonFavoritos(idSerie);
    } catch (error) {
        console.error('Error inicial:', error);
        mostrarError('Error al cargar datos iniciales');
    }
});

async function cargarDatosSerie(idSerie) {
    try {
        console.log('Cargando datos para la serie con ID:', idSerie);
        mostrarCargando(true);
        
        const response = await fetch(`http://44.209.91.221:7002/mostrarSeries?id=${idSerie}`);
        if (!response.ok) {
            throw new Error('Error al obtener datos de la serie');
        }
        
        const data = await response.json();
        console.log('Datos de la serie recibidos:', data);
        
        // Buscar la serie específica en el array devuelto
        const serie = Array.isArray(data) ? data.find(s => s.id == idSerie) : data;
        if (!serie) throw new Error('Serie no encontrada');
        
        mostrarDatosSerie(serie);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los datos de la serie');
    } finally {
        mostrarCargando(false);
    }
}

async function cargarDirector(idSerie) {
    try {
        const response = await fetch(`http://44.209.91.221:7002/detalleDirector?id=${idSerie}`);
        if (!response.ok) {
            throw new Error('No se pudo obtener el director');
        }

        const data = await response.json();
        const director = data.director;

        const directorDiv = document.getElementById("director-info");
        if (!director || !director.nombre) {
            directorDiv.textContent = "🎬 Director: No disponible";
            return;
        }

        directorDiv.textContent = `🎬 Director: ${director.nombre}`;
    } catch (error) {
        console.error("Error al obtener el director:", error);
        document.getElementById("director-info").textContent = "🎬 Director: No disponible";
    }
}


function mostrarDatosSerie(serie) {
    console.log('Objeto serie completo:', serie);
    const posterImg = document.querySelector('.poster-img');
    const baseUrl = "https://image.tmdb.org/t/p/original";
    
    let rutaImagen = serie.imagen_url || serie.poster_path || serie.imagenUrl || serie.imageUrl || serie.imagen || null;

    console.log('Ruta de imagen detectada:', rutaImagen);
    
    if (posterImg && rutaImagen) {
        let imagenCompleta = rutaImagen;
        if (rutaImagen.startsWith('/')) {
            imagenCompleta = baseUrl + rutaImagen;
        }
        posterImg.src = imagenCompleta;
        posterImg.alt = `Poster de ${serie.nombre}`;
    }
    
    document.querySelector('.serie-title').textContent = serie.nombre || 'Serie sin título';
    document.querySelector('.serie-year').textContent = serie.estreno || '';
    document.querySelector('.serie-description').textContent = serie.sinopsis || 'Descripción no disponible';
}

async function cargarResenas(idCapitulo) {
    try {
        mostrarCargando(true);
        console.log(`Cargando reseñas para capítulo ${idCapitulo}...`);
        
        const response = await fetch(`http://44.209.91.221:7002/resenasConComentarios/${idCapitulo}`);
        
        console.log('Respuesta del servidor:', response);
        
        if (response.status === 404) {
            // No hay reseñas, pero no es un error
            mostrarResenas([]);
            return;
        }
        
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        
        const resenas = await response.json();
        console.log('Reseñas recibidas:', resenas);
        
        if (!resenas || resenas.length === 0) {
            mostrarResenas([]);
            return;
        }
        
        mostrarResenas(resenas);
    } catch (error) {
        console.error('Error al cargar reseñas:', error);
        mostrarError('No se pudieron cargar las reseñas');
        mostrarResenas([]); // Mostrar lista vacía
    } finally {
        mostrarCargando(false);
    }
}

function mostrarResenas(resenasConComentarios) {
    const reviewsList = document.querySelector('.reviews-list');
    reviewsList.innerHTML = '';
    
    if (!resenasConComentarios || resenasConComentarios.length === 0) {
        reviewsList.innerHTML = '<p class="no-reviews">No hay reseñas para este episodio aún</p>';
        return;
    }
    
    // Obtener información del usuario actual
    const token = localStorage.getItem('authToken');
    const idUsuarioActual = token ? JWTUtil.verificarToken(token) : null;
    const nombreUsuarioActual = token ? JWTUtil.obtenerNombreUsuario(token) : null;

    resenasConComentarios.forEach(resena => {
        if (!resena || !resena.nombreUsuario || !resena.contenido) {
            console.warn('Reseña inválida:', resena);
            return;
        }

        const reviewItem = document.createElement('div');
        reviewItem.className = 'review-item';
        
        // Marcar como propio si el usuario es el autor
        const esPropietario = idUsuarioActual && resena.idUsuario == idUsuarioActual;
        if (esPropietario) {
            reviewItem.classList.add('propio');
        }
        
        let fecha;
        try {
            fecha = new Date(resena.fechaCreacion);
            if (isNaN(fecha.getTime())) throw new Error('Fecha inválida');
        } catch {
            fecha = new Date(); // Fallback si la fecha es inválida
        }

        const fechaFormateada = formatearFecha(fecha);
        
        reviewItem.innerHTML = `
            <div class="review-header">
                <div class="user-info">
                    <div class="user-avatar small">${resena.nombreUsuario.charAt(0)}</div>
                    <div class="user-details">
                        <span class="user-name">${resena.nombreUsuario}</span>
                        <span class="review-date">${fechaFormateada}</span>
                    </div>
                </div>
                <div class="review-actions">
                    ${esPropietario ? `
                    <div class="dropdown">
                        <button class="dropdown-toggle" data-resena-id="${resena.id || ''}">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 13C12.5523 13 13 12.5523 13 12C13 11.4477 12.5523 11 12 11C11.4477 11 11 11.4477 11 12C11 12.5523 11.4477 13 12 13Z" fill="currentColor"/>
                                <path d="M12 6C12.5523 6 13 5.55228 13 5C13 4.44772 12.5523 4 12 4C11.4477 4 11 4.44772 11 5C11 5.55228 11.4477 6 12 6Z" fill="currentColor"/>
                                <path d="M12 20C12.5523 20 13 19.5523 13 19C13 18.4477 12.5523 18 12 18C11.4477 18 11 18.4477 11 19C11 19.5523 11.4477 20 12 20Z" fill="currentColor"/>
                            </svg>
                        </button>
                        <div class="dropdown-menu">
                            <button class="dropdown-item edit-btn">Editar</button>
                            <button class="dropdown-item delete-btn">Eliminar</button>
                        </div>
                    </div>
                    ` : ''}
                    <button class="like-btn" data-resena-id="${resena.id || ''}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" stroke="currentColor" stroke-width="2" fill="none"/>
                        </svg>
                    </button>
                </div>
            </div>
            <p class="review-text">${resena.contenido}</p>
            <div class="respond-section">
                <button class="respond-btn" data-resena-id="${resena.id || ''}">Responder</button>
            </div>
            <div class="comment-form-container" style="display: none;">
                <textarea class="comment-input-text" placeholder="Escribe tu comentario..." rows="3"></textarea>
                <button class="send-comment-btn" data-resena-id="${resena.id || ''}">Enviar comentario</button>
            </div>
            <div class="toggle-comments-section">
                <button class="toggle-comments-btn">Ver comentarios</button>
                <div class="comentarios-container" style="display: none;">
                    ${generarComentariosHTML(resena.comentarios, idUsuarioActual)}
                </div>
            </div>
        `;
        
        reviewsList.appendChild(reviewItem);
    });
}

function generarComentariosHTML(comentarios, idUsuarioActual) {
    if (!comentarios || comentarios.length === 0) return '<div class="comentario-item">No hay comentarios</div>';
    
    return comentarios.map(comentario => {
        if (!comentario || !comentario.contenido) return '';
        
        const nombreUsuario = comentario.nombreUsuario || 'Usuario';
        const inicial = nombreUsuario.charAt(0);
        
        // Verificar si el usuario actual es el autor del comentario
        const esPropietario = idUsuarioActual && comentario.idUsuario == idUsuarioActual;
        
        const comentarioItem = document.createElement('div');
        comentarioItem.className = 'comentario-item';
        if (esPropietario) comentarioItem.classList.add('propio');
        
        comentarioItem.innerHTML = `
            <div class="user-info">
                <div class="user-avatar xsmall">${inicial}</div>
                <span class="user-name">${nombreUsuario}</span>
                <div class="comentario-text">${comentario.contenido}</div>
            </div>
            ${esPropietario ? `
            <div class="comentario-actions">
                <div class="dropdown">
                    <button class="dropdown-toggle" data-comentario-id="${comentario.id || ''}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 13C12.5523 13 13 12.5523 13 12C13 11.4477 12.5523 11 12 11C11.4477 11 11 11.4477 11 12C11 12.5523 11.4477 13 12 13Z" fill="currentColor"/>
                            <path d="M12 6C12.5523 6 13 5.55228 13 5C13 4.44772 12.5523 4 12 4C11.4477 4 11 4.44772 11 5C11 5.55228 11.4477 6 12 6Z" fill="currentColor"/>
                            <path d="M12 20C12.5523 20 13 19.5523 13 19C13 18.4477 12.5523 18 12 18C11.4477 18 11 18.4477 11 19C11 19.5523 11.4477 20 12 20Z" fill="currentColor"/>
                        </svg>
                    </button>
                    <div class="dropdown-menu">
                        <button class="dropdown-item edit-comment-btn">Editar</button>
                        <button class="dropdown-item delete-comment-btn">Eliminar</button>
                    </div>
                </div>
            </div>
            ` : ''}
        `;
        
        return comentarioItem.outerHTML;
    }).join('');
}

async function configurarBotonFavoritos(idSerie) {
    const favoriteBtn = document.getElementById('favoriteBtn');
    if (!favoriteBtn) return;

    favoriteBtn.addEventListener('click', async () => {
        if (!verificarAutenticacion()) return;
        
        const token = localStorage.getItem('authToken');
        if (!token) {
            mostrarError('No se encontró token de autenticación');
            return;
        }

        try {
            mostrarCargando(true);
            
            // Verificar si ya es favorita para determinar la acción
            const esFavorita = favoriteBtn.classList.contains('favorited');
            
            if (esFavorita) {
                mostrarError('Eliminar de favoritos no está implementado aún');
                return;
            } else {
                // Agregar a favoritos
                const response = await fetch(`http://44.209.91.221:7002/agregarFavorita/${idSerie}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                
                if (response.status === 401) {
                    localStorage.removeItem('authToken');
                    actualizarUI();
                    throw new Error('Tu sesión ha expirado');
                }
                
                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText.includes('No se puede agregar mas de 4') ? 
                        'Límite de 4 favoritos alcanzado' : 
                        'Error al agregar a favoritos');
                }
                
                favoriteBtn.classList.add('favorited');
                favoriteBtn.innerHTML = `
                    <svg class="star-icon" width="16" height="16" viewBox="0 0 24 24" fill="#FFD700" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" stroke="#FFD700" stroke-width="2" stroke-linejoin="round"/>
                    </svg>
                    En favoritos
                `;
                mostrarError('Serie agregada a favoritos');
            }
            
        } catch (error) {
            console.error('Error:', error);
            mostrarError(error.message);
        } finally {
            mostrarCargando(false);
        }
    });

    // Verificar estado inicial
    await verificarEstadoFavorito(idSerie, favoriteBtn);
}

async function verificarEstadoFavorito(idSerie, favoriteBtn) {
    const token = localStorage.getItem('authToken');
    if (!token || !favoriteBtn) return;

    try {
        const idUsuario = JWTUtil.verificarToken(token);
        if (!idUsuario) return;

        const response = await fetch(`http://44.209.91.221:7002/mostrarSeriesFavoritas`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            actualizarUI();
            return;
        }
        
        if (!response.ok) return;

        const favoritas = await response.json();
        if (favoritas.some(serie => serie.id == idSerie)) {
            favoriteBtn.classList.add('favorited');
            favoriteBtn.innerHTML = `
                <svg class="star-icon" width="16" height="16" viewBox="0 0 24 24" fill="#FFD700" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" stroke="#FFD700" stroke-width="2" stroke-linejoin="round"/>
                </svg>
                En favoritos
            `;
        }
    } catch (error) {
        console.error('Error verificando favoritos:', error);
    }
}

async function enviarResena() {
    if (!verificarAutenticacion()) return;
    
    const idCapitulo = localStorage.getItem('currentCapituloId');
    const contenido = document.querySelector('.comment-input').value.trim();
    
    if (!contenido) {
        mostrarError('La reseña no puede estar vacía');
        return;
    }
    
    if (!idCapitulo) {
        mostrarError('No se identificó el capítulo');
        return;
    }
    
    try {
        mostrarCargando(true);
        const token = localStorage.getItem('authToken');
        
        const datosResena = {
            id: parseInt(idCapitulo),
            contenido: contenido
        };
        
        console.log('Enviando reseña:', datosResena);
        
        const response = await fetch('http://44.209.91.221:7002/hacerResena', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(datosResena)
        });
        
        console.log('Respuesta del servidor:', response);
        
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            actualizarUI();
            throw new Error('Tu sesión ha expirado');
        }
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            console.error('Detalles del error:', errorData);
            throw new Error(errorData.error || `Error ${response.status} al enviar reseña`);
        }
        
        // Limpiar y recargar
        document.querySelector('.comment-input').value = '';
        await cargarResenas(idCapitulo);
        
        mostrarError('✅ Reseña enviada correctamente');
        
    } catch (error) {
        console.error('Error al enviar reseña:', error);
        mostrarError(error.message);
    } finally {
        mostrarCargando(false);
    }
}

async function enviarComentario(resenaId, contenido) {
    if (!verificarAutenticacion()) return;
    
    try {
        mostrarCargando(true);
        const token = localStorage.getItem('authToken');
        
        const datosComentario = {
            id: parseInt(resenaId),
            contenido: contenido
        };
        
        const response = await fetch('http://44.209.91.221:7002/hacerComentario', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(datosComentario)
        });
        
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            actualizarUI();
            throw new Error('Tu sesión ha expirado');
        }
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error al enviar comentario');
        }
        
        // Limpiar el campo de comentario y ocultar el formulario
        const urlParams = new URLSearchParams(window.location.search);
        await cargarResenas(urlParams.get('idCapitulo'));
        
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message);
    } finally {
        mostrarCargando(false);
    }
}

function verificarAutenticacion() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        mostrarError('Debes iniciar sesión para realizar esta acción');
        window.location.href = `/usuarios/ingresar.html?redirect=${encodeURIComponent(window.location.pathname + window.location.search)}`;
        return false;
    }
    
    // Verificar si el token es válido localmente
    const idUsuario = JWTUtil.verificarToken(token);
    if (!idUsuario) {
        localStorage.removeItem('authToken');
        mostrarError('Tu sesión ha expirado');
        window.location.href = `/usuarios/ingresar.html?redirect=${encodeURIComponent(window.location.pathname + window.location.search)}`;
        return false;
    }
    
    return true;
}

function formatearFecha(fecha) {
    const ahora = new Date();
    const diferencia = ahora - fecha;
    
    if (diferencia < 60000) return 'ahora';
    if (diferencia < 3600000) {
        const minutos = Math.floor(diferencia / 60000);
        return `hace ${minutos} minuto${minutos !== 1 ? 's' : ''}`;
    }
    if (fecha.toDateString() === ahora.toDateString()) {
        return `hoy a las ${fecha.toLocaleTimeString('es-ES', {hour: '2-digit', minute:'2-digit'})}`;
    }
    
    const ayer = new Date(ahora);
    ayer.setDate(ahora.getDate() - 1);
    if (fecha.toDateString() === ayer.toDateString()) {
        return `ayer a las ${fecha.toLocaleTimeString('es-ES', {hour: '2-digit', minute:'2-digit'})}`;
    }
    
    return fecha.toLocaleDateString('es-ES', {
        day: 'numeric', 
        month: 'long', 
        year: fecha.getFullYear() !== ahora.getFullYear() ? 'numeric' : undefined
    });
}

function configurarEventos() {
    // Botón de comentario principal
    document.querySelector('.comment-btn')?.addEventListener('click', enviarResena);
    
    // Permitir enviar con Enter
    document.querySelector('.comment-input')?.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') enviarResena();
    });
    
    // Delegación de eventos para elementos dinámicos
    document.querySelector('.reviews-list')?.addEventListener('click', (e) => {
        // Manejar clic en Responder
        if (e.target.classList.contains('respond-btn')) {
            if (!verificarAutenticacion()) return;
            
            const respondBtn = e.target;
            const reviewItem = respondBtn.closest('.review-item');
            const commentForm = reviewItem.querySelector('.comment-form-container');
            
            // Alternar la visibilidad del formulario de comentario
            if (commentForm.style.display === 'none') {
                commentForm.style.display = 'block';
                respondBtn.textContent = 'Cancelar';
            } else {
                commentForm.style.display = 'none';
                respondBtn.textContent = 'Responder';
            }
        }
        
        // Manejar clic en Enviar comentario
        if (e.target.classList.contains('send-comment-btn')) {
            if (!verificarAutenticacion()) return;
            
            const sendBtn = e.target;
            const resenaId = sendBtn.getAttribute('data-resena-id');
            const commentInput = sendBtn.closest('.comment-form-container').querySelector('.comment-input-text');
            const contenido = commentInput.value.trim();
            
            if (!contenido) {
                mostrarError('El comentario no puede estar vacío');
                return;
            }
            
            enviarComentario(resenaId, contenido);
        }
        
        if (e.target.closest('.like-btn')) {
            if (!verificarAutenticacion()) return;
            const likeBtn = e.target.closest('.like-btn');
            manejarLike(likeBtn);
        }

        if (e.target.classList.contains('toggle-comments-btn')) {
            const button = e.target;
            const commentsContainer = button.closest('.toggle-comments-section').querySelector('.comentarios-container');
            
            if (commentsContainer.style.display === 'none') {
                commentsContainer.style.display = 'block';
                button.textContent = 'Ocultar comentarios';
            } else {
                commentsContainer.style.display = 'none';
                button.textContent = 'Ver comentarios';
            }
        }
    });
    
    // Delegación de eventos para menú desplegable
    document.addEventListener('click', (e) => {
        // Cerrar menús al hacer clic fuera
        if (!e.target.closest('.dropdown-toggle')) {
            document.querySelectorAll('.dropdown').forEach(dropdown => {
                dropdown.classList.remove('active');
            });
        }
    });

    document.querySelector('.reviews-list')?.addEventListener('click', (e) => {
        // Manejar clic en el menú de tres puntos
        if (e.target.closest('.dropdown-toggle')) {
            e.preventDefault();
            const dropdown = e.target.closest('.dropdown');
            dropdown.classList.toggle('active');
            
            // Cerrar otros menús abiertos
            document.querySelectorAll('.dropdown').forEach(d => {
                if (d !== dropdown) d.classList.remove('active');
            });
        }
        
        // Manejar clic en Editar reseña
        if (e.target.classList.contains('edit-btn')) {
            const dropdown = e.target.closest('.dropdown');
            const resenaId = dropdown.querySelector('.dropdown-toggle').getAttribute('data-resena-id');
            const reviewItem = dropdown.closest('.review-item');
            const contenidoActual = reviewItem.querySelector('.review-text').textContent;
            
            iniciarEdicionResena(resenaId, contenidoActual, reviewItem);
            dropdown.classList.remove('active');
        }
        
        // Manejar clic en Eliminar reseña
        if (e.target.classList.contains('delete-btn')) {
            const dropdown = e.target.closest('.dropdown');
            const resenaId = dropdown.querySelector('.dropdown-toggle').getAttribute('data-resena-id');
            const confirmar = confirm('¿Estás seguro de que quieres eliminar esta reseña?');
            
            if (confirmar) {
                eliminarResena(resenaId);
            }
            dropdown.classList.remove('active');
        }
        
        // Manejar clic en Editar comentario
        if (e.target.classList.contains('edit-comment-btn')) {
            const dropdown = e.target.closest('.dropdown');
            const comentarioId = dropdown.querySelector('.dropdown-toggle').getAttribute('data-comentario-id');
            const comentarioItem = dropdown.closest('.comentario-item');
            const contenidoActual = comentarioItem.querySelector('.comentario-text').textContent;
            
            iniciarEdicionComentario(comentarioId, contenidoActual, comentarioItem);
            dropdown.classList.remove('active');
        }
        
        // Manejar clic en Eliminar comentario
        if (e.target.classList.contains('delete-comment-btn')) {
            const dropdown = e.target.closest('.dropdown');
            const comentarioId = dropdown.querySelector('.dropdown-toggle').getAttribute('data-comentario-id');
            const confirmar = confirm('¿Estás seguro de que quieres eliminar este comentario?');
            
            if (confirmar) {
                eliminarComentario(comentarioId);
            }
            dropdown.classList.remove('active');
        }
    });
}

function iniciarEdicionResena(resenaId, contenidoActual, reviewItem) {
    const reviewText = reviewItem.querySelector('.review-text');
    const editForm = document.createElement('div');
    editForm.className = 'edit-form';
    
    editForm.innerHTML = `
        <textarea class="edit-textarea">${contenidoActual}</textarea>
        <div class="edit-buttons">
            <button class="cancel-edit-btn">Cancelar</button>
            <button class="save-edit-btn" data-resena-id="${resenaId}">Guardar</button>
        </div>
    `;
    
    reviewText.replaceWith(editForm);
    
    // Enfocar el textarea
    editForm.querySelector('.edit-textarea').focus();
    
    // Manejar eventos de los botones
    editForm.querySelector('.cancel-edit-btn').addEventListener('click', () => {
        editForm.replaceWith(reviewText);
    });
    
    editForm.querySelector('.save-edit-btn').addEventListener('click', async () => {
        const nuevoContenido = editForm.querySelector('.edit-textarea').value.trim();
        if (!nuevoContenido) {
            mostrarError('La reseña no puede estar vacía');
            return;
        }
        
        if (nuevoContenido === contenidoActual) {
            editForm.replaceWith(reviewText);
            return;
        }
        
        try {
            mostrarCargando(true);
            const token = localStorage.getItem('authToken');
            
            const datosEdicion = {
                Id: parseInt(resenaId),
                contenidoNuevo: nuevoContenido
            };
            
            console.log('Enviando datos de edición:', datosEdicion);
            
            const response = await fetch('http://44.209.91.221:7002/editarResena', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(datosEdicion)
            });
            
            if (response.status === 401) {
                localStorage.removeItem('authToken');
                actualizarUI();
                throw new Error('Tu sesión ha expirado');
            }
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Error al editar reseña');
            }
            
            // Actualizar la UI con el nuevo contenido
            reviewText.textContent = nuevoContenido;
            editForm.replaceWith(reviewText);
            
            mostrarError('✅ Reseña actualizada correctamente');
        } catch (error) {
            console.error('Error al editar reseña:', error);
            mostrarError(error.message);
        } finally {
            mostrarCargando(false);
        }
    });
}

async function eliminarResena(resenaId) {
    if (!verificarAutenticacion()) return;
    
    const idCapitulo = localStorage.getItem('currentCapituloId');
    if (!idCapitulo) {
        mostrarError('No se identificó el capítulo');
        return;
    }
    
    try {
        mostrarCargando(true);
        const token = localStorage.getItem('authToken');
        
        const datosEliminacion = {
            Id: parseInt(resenaId)
        };
        
        console.log('Enviando datos de eliminación:', datosEliminacion);
        
        const response = await fetch('http://44.209.91.221:7002/eliminarResena', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(datosEliminacion)
        });
        
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            actualizarUI();
            throw new Error('Tu sesión ha expirado');
        }
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Error al eliminar reseña');
        }
        
        // Recargar las reseñas
        await cargarResenas(idCapitulo);
        mostrarError('✅ Reseña eliminada correctamente');
    } catch (error) {
        console.error('Error al eliminar reseña:', error);
        mostrarError(error.message);
    } finally {
        mostrarCargando(false);
    }
}

async function iniciarEdicionComentario(comentarioId, contenidoActual, comentarioItem) {
    const comentarioText = comentarioItem.querySelector('.comentario-text');
    const editForm = document.createElement('div');
    editForm.className = 'edit-form';
    
    editForm.innerHTML = `
        <textarea class="edit-textarea">${contenidoActual}</textarea>
        <div class="edit-buttons">
            <button class="cancel-edit-btn">Cancelar</button>
            <button class="save-edit-btn" data-comentario-id="${comentarioId}">Guardar</button>
        </div>
    `;
    
    comentarioText.replaceWith(editForm);
    
    editForm.querySelector('.edit-textarea').focus();
    
    editForm.querySelector('.cancel-edit-btn').addEventListener('click', () => {
        editForm.replaceWith(comentarioText);
    });
    
    editForm.querySelector('.save-edit-btn').addEventListener('click', async () => {
        const nuevoContenido = editForm.querySelector('.edit-textarea').value.trim();
        if (!nuevoContenido) {
            mostrarError('El comentario no puede estar vacío');
            return;
        }
        
        if (nuevoContenido === contenidoActual) {
            editForm.replaceWith(comentarioText);
            return;
        }
        
        try {
            mostrarCargando(true);
            const token = localStorage.getItem('authToken');
            
            const datosEdicion = {
                Id: parseInt(comentarioId),
                contenidoNuevo: nuevoContenido
            };
            
            console.log('Enviando datos de edición:', datosEdicion);
            
            const response = await fetch('http://44.209.91.221:7002/editarComentario', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(datosEdicion)
            });
            
            // Manejar respuesta no JSON
            const responseText = await response.text();
            let responseData;
            try {
                responseData = responseText ? JSON.parse(responseText) : {};
            } catch (e) {
                console.error('Error parsing JSON:', e);
                throw new Error(responseText || 'Error al editar comentario');
            }
            
            if (response.status === 401) {
                localStorage.removeItem('authToken');
                actualizarUI();
                throw new Error('Tu sesión ha expirado');
            }
            
            if (!response.ok) {
                throw new Error(responseData.error || responseData.message || 'Error al editar comentario');
            }
            
            // 1. Actualizar el texto localmente
            reviewText.textContent = nuevoContenido;
            editForm.replaceWith(reviewText)
            
            // 2. Recargar los comentarios desde el servidor
            const idCapitulo = localStorage.getItem('currentCapituloId');
            await cargarResenas(idCapitulo);
            
            mostrarError('✅ Comentario actualizado correctamente');
        } catch (error) {
            console.error('Error al editar comentario:', error);
            mostrarError(error.message);
        } finally {
            mostrarCargando(false);
        }
    });
}


async function eliminarComentario(comentarioId) {
    if (!verificarAutenticacion()) return;
    
    try {
        mostrarCargando(true);
        const token = localStorage.getItem('authToken');
        
        const datosEliminacion = {
            Id: parseInt(comentarioId) // Cambiado de 'Id' a 'id' para coincidir con el backend
        };
        
        console.log('Enviando datos de eliminación:', datosEliminacion);
        
        const response = await fetch('http://44.209.91.221:7002/eliminarComentario', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(datosEliminacion)
        });
        
        // Manejar respuesta no JSON
        const responseText = await response.text();
        let responseData;
        try {
            responseData = responseText ? JSON.parse(responseText) : {};
        } catch (e) {
            console.error('Error parsing JSON:', e);
            throw new Error(responseText || 'Error al eliminar comentario');
        }
        
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            actualizarUI();
            throw new Error('Tu sesión ha expirado');
        }
        
        if (!response.ok) {
            throw new Error(responseData.error || responseData.message || 'Error al eliminar comentario');
        }
        
        // Recargar las reseñas y comentarios
        const idCapitulo = localStorage.getItem('currentCapituloId');
        await cargarResenas(idCapitulo);
        mostrarError('✅ Comentario eliminado correctamente');
    } catch (error) {
        console.error('Error al eliminar comentario:', error);
        mostrarError(error.message.includes('No se pudo') ? error.message : 'No se pudo eliminar el comentario');
    } finally {
        mostrarCargando(false);
    }
}

function manejarLike(btn) {
    const heart = btn.querySelector('path');
    const isLiked = heart.getAttribute('fill') !== 'none' && heart.getAttribute('fill');
    
    try {
        if (isLiked) {
            heart.setAttribute('fill', 'none');
            heart.setAttribute('stroke', 'currentColor');
        } else {
            heart.setAttribute('fill', '#ff0000');
            heart.setAttribute('stroke', '#ff0000');
        }
    } catch (error) {
        console.error('Error al manejar like:', error);
        // Revertir cambio visual si falla
        if (isLiked) {
            heart.setAttribute('fill', '#ff0000');
            heart.setAttribute('stroke', '#ff0000');
        } else {
            heart.setAttribute('fill', 'none');
            heart.setAttribute('stroke', 'currentColor');
        }
    }
}

function mostrarError(mensaje) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = mensaje;
    document.body.appendChild(errorDiv);
    
    setTimeout(() => {
        errorDiv.remove();
    }, 3000);
}

function mostrarCargando(mostrar) {
    const existingLoader = document.querySelector('.loading-indicator');
    if (existingLoader) existingLoader.remove();
    
    if (mostrar) {
        const loader = document.createElement('div');
        loader.className = 'loading-indicator';
        loader.innerHTML = `
            <div class="spinner"></div>
            <span>Cargando...</span>
        `;
        document.body.appendChild(loader);
    }
}

function actualizarUI() {
    const isLoggedIn = !!localStorage.getItem('authToken');
    const commentInput = document.querySelector('.comment-input');
    const commentBtn = document.querySelector('.comment-btn');
    
    if (commentInput && commentBtn) {
        commentInput.placeholder = isLoggedIn ? 
            'Escribe tu reseña...' : 
            'Inicia sesión para comentar';
        commentInput.disabled = !isLoggedIn;
        commentBtn.disabled = !isLoggedIn;
    }
}

// Escuchar cambios en el almacenamiento local (por si se cierra sesión en otra pestaña)
window.addEventListener('storage', () => {
    actualizarUI();
});

// Objeto JWTUtil para verificación local de tokens
const JWTUtil = {
    verificarToken: (token) => {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.id;
        } catch (e) {
            console.error('Error verificando token:', e);
            return null;
        }
    },
    obtenerNombreUsuario: (token) => {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.nombreUsuario || null;
        } catch (e) {
            console.error('Error obteniendo nombre de usuario:', e);
            return null;
        }
    }
};