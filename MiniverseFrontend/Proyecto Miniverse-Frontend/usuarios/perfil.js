document.addEventListener('DOMContentLoaded', () => {
    // Verificar autenticación al cargar
    const token = localStorage.getItem('authToken');
    
    if (!token) {
        mostrarModalLogin();
        return;
    }

    
    configurarMenuUsuario();
    
    
    cargarPerfilCompleto(token);
});


function configurarMenuUsuario() {
    const settingsBtn = document.querySelector('.settings-btn');
    const settingsMenu = document.getElementById('settingsMenu');
    
    if (settingsBtn && settingsMenu) {
        settingsBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            settingsMenu.classList.toggle('show');
        });
        
        // Cerrar menú al hacer click fuera
        document.addEventListener('click', (e) => {
            if (!settingsMenu.contains(e.target) && !settingsBtn.contains(e.target)) {
                settingsMenu.classList.remove('show');
            }
        });
    }
}
// Función principal para cargar todos los datos del perfil
async function cargarPerfilCompleto(token) {
    try {
        // Mostrar estado de carga
        mostrarCargando(true);
        
        // Cargar datos en paralelo para mejor rendimiento
        const [usuario, seriesFavoritas, actividad] = await Promise.all([
            obtenerDatosUsuario(token),
            obtenerSeriesFavoritas(token),
            obtenerActividadUsuario(token)
        ]);
        
        // Actualizar la interfaz con los datos obtenidos
        actualizarUIUsuario(usuario);
        mostrarSeriesFavoritas(seriesFavoritas);
        mostrarActividadUsuario(actividad.resenas, actividad.comentarios);
        
    } catch (error) {
        console.error('Error al cargar perfil:', error);
        mostrarError(error.message || 'Error al cargar el perfil');
        
        // Si el error es de autenticación, redirigir a login
        if (error.message.includes('401')) {
            localStorage.removeItem('authToken');
            mostrarModalLogin();
        }
    } finally {
        mostrarCargando(false);
    }
}


function mostrarModalLogin() {
    const modal = document.getElementById('loginModal');
    if (!modal) return;

    modal.classList.add('show');
    
    document.getElementById('confirmLoginBtn')?.addEventListener('click', () => {
        window.location.href = '/usuarios/ingresar.html';
    });
    
    document.getElementById('cancelLoginBtn')?.addEventListener('click', () => {
        modal.classList.remove('show');
        window.location.href = '/usuarios/inicio.html';
    });
}


async function obtenerDatosUsuario(token) {
    const response = await fetch('http://44.209.91.221:7002/mostrarMiUsuario', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (response.status === 401) {
        throw new Error('401: No autorizado');
    }

    if (!response.ok) {
        throw new Error('Error al obtener datos del usuario');
    }

    return await response.json();
}

// Función para obtener series favoritas
async function obtenerSeriesFavoritas(token) {
    const response = await fetch('http://44.209.91.221:7002/mostrarSeriesFavoritas', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (!response.ok) {
        throw new Error('Error al obtener series favoritas');
    }

    return await response.json();
}

// Función para obtener actividad (reseñas y comentarios)
async function obtenerActividadUsuario(token) {
    try {
        const [resenasResponse, comentariosResponse] = await Promise.all([
            fetch('http://44.209.91.221:7002/mostrarUltimasResenas', {
                headers: { 'Authorization': `Bearer ${token}` }
            }),
            fetch('http://44.209.91.221:7002/mostrarUltimosComentarios', {
                headers: { 'Authorization': `Bearer ${token}` }
            })
        ]);

        return {
            resenas: resenasResponse.ok ? await resenasResponse.json() : [],
            comentarios: comentariosResponse.ok ? await comentariosResponse.json() : []
        };
    } catch (error) {
        console.error('Error al obtener actividad:', error);
        return { resenas: [], comentarios: [] };
    }
}

// Función para actualizar la UI con datos del usuario
function actualizarUIUsuario(usuario) {
    if (!usuario) return;

    
    const nombreElement = document.querySelector('.user-name');
    if (nombreElement) {
        nombreElement.textContent = usuario.nombre || 'Usuario';
    }

    
    const avatarElement = document.querySelector('.user-avatar');
    if (avatarElement) {
        const inicial = usuario.nombre ? usuario.nombre.charAt(0).toUpperCase() : 'U';
        avatarElement.textContent = inicial;
        avatarElement.style.backgroundColor = generarColorDesdeNombre(usuario.nombre || 'Usuario');
    }

    
    const emailElement = document.getElementById('userEmail');
    const ageElement = document.getElementById('userAge');
    
    if (emailElement) emailElement.textContent = usuario.correo || '';
    if (ageElement && usuario.edad) ageElement.textContent = `${usuario.edad} años`;
}

// Función para mostrar series favoritas en el DOM
function mostrarSeriesFavoritas(series) {
    const seriesGrid = document.querySelector('.series-grid');
    if (!seriesGrid) return;

    if (!series || series.length === 0) {
        seriesGrid.innerHTML = '<p class="no-series">No tienes series favoritas aún</p>';
        return;
    }

    seriesGrid.innerHTML = series.map(serie => {
        // Asegurar compatibilidad con nombres de propiedades diferentes
        const imagenUrl = serie.imagen_url || serie.imagenUrl || '/img/serie-default.jpg';
        const nombre = serie.nombre || serie.titulo || 'Serie sin nombre';
        const estreno = serie.estreno || serie.anio || '';

        return `
            <div class="series-card" data-id="${serie.id}">
                <div class="series-image">
                    <img src="${imagenUrl}" 
                         alt="${nombre}" 
                         onerror="this.src='/img/serie-default.jpg'">
                    <div class="favorite-badge">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="#FFD700" stroke="#FFD700">
                            <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" 
                                  stroke-width="2" stroke-linejoin="round"/>
                        </svg>
                    </div>
                </div>
                <div class="series-info">
                    <h3 class="series-title">${nombre}</h3>
                    <span class="series-year">${estreno}</span>
                </div>
            </div>
        `;
    }).join('');

    
    document.querySelectorAll('.series-card').forEach(card => {
        card.addEventListener('click', () => {
            const serieId = card.getAttribute('data-id');
            window.location.href = `/usuarios/serie.html?id=${serieId}`;
        });
    });
}


function mostrarActividadUsuario(resenas = [], comentarios = []) {
    const activityGrid = document.querySelector('.activity-grid');
    if (!activityGrid) return;

    // Combinar y ordenar actividad
    const actividadCombinada = [
        ...resenas.map(r => ({ ...r, tipo: 'resena', fecha: new Date(r.fecha_creacion) })),
        ...comentarios.map(c => ({ ...c, tipo: 'comentario', fecha: new Date(c.fecha_creacion) }))
    ].sort((a, b) => b.fecha - a.fecha);

    if (actividadCombinada.length === 0) {
        activityGrid.innerHTML = '<p class="no-activity">No hay actividad reciente</p>';
        return;
    }

    // Mostrar las 10 actividades más recientes
    activityGrid.innerHTML = actividadCombinada.slice(0, 10).map(item => `
        <div class="activity-card" data-id="${item.id}" data-type="${item.tipo}">
            <div class="activity-header">
                <span class="activity-type">
                    ${item.tipo === 'resena' ? '📝 Reseña' : '💬 Comentario'}
                </span>
                <span class="activity-date">${formatearFecha(item.fecha)}</span>
            </div>
            <p class="activity-text">${item.contenido || 'Sin contenido'}</p>
            ${item.serieNombre ? `<p class="activity-location">En ${item.serieNombre}</p>` : ''}
        </div>
    `).join('');
}

// Función para generar color desde el nombre (para el avatar)
function generarColorDesdeNombre(nombre) {
    const colores = ['#FF5733', '#33FF57', '#3357FF', '#F333FF', '#33FFF3', '#FF33F3'];
    const hash = nombre.split('').reduce((acc, char) => char.charCodeAt(0) + acc, 0);
    return colores[hash % colores.length];
}

// Función para formatear fechas
function formatearFecha(fecha) {
    if (!(fecha instanceof Date)) {
        fecha = new Date(fecha);
    }

    const ahora = new Date();
    const diff = ahora - fecha;
    const diffDays = Math.floor(diff / (1000 * 60 * 60 * 24));
    const diffHours = Math.floor(diff / (1000 * 60 * 60));
    const diffMinutes = Math.floor(diff / (1000 * 60));

    if (diffMinutes < 1) return 'Hace unos momentos';
    if (diffMinutes < 60) return `Hace ${diffMinutes} minuto${diffMinutes !== 1 ? 's' : ''}`;
    if (diffHours < 24) return `Hace ${diffHours} hora${diffHours !== 1 ? 's' : ''}`;
    if (diffDays === 1) return 'Ayer';
    if (diffDays < 7) return `Hace ${diffDays} días`;
    
    return fecha.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });
}


function mostrarError(mensaje) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = mensaje;
    document.body.appendChild(errorDiv);
    
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}


function mostrarCargando(mostrar) {
    const loader = document.getElementById('loadingIndicator');
    if (loader) {
        loader.style.display = mostrar ? 'flex' : 'none';
        return;
    }

    if (mostrar) {
        const loaderDiv = document.createElement('div');
        loaderDiv.id = 'loadingIndicator';
        loaderDiv.style.position = 'fixed';
        loaderDiv.style.top = '0';
        loaderDiv.style.left = '0';
        loaderDiv.style.right = '0';
        loaderDiv.style.bottom = '0';
        loaderDiv.style.backgroundColor = 'rgba(0,0,0,0.7)';
        loaderDiv.style.display = 'flex';
        loaderDiv.style.alignItems = 'center';
        loaderDiv.style.justifyContent = 'center';
        loaderDiv.style.zIndex = '1000';
        loaderDiv.innerHTML = `
            <div class="spinner"></div>
            <span style="color: white; margin-left: 10px;">Cargando...</span>
        `;
        document.body.appendChild(loaderDiv);
    }
}