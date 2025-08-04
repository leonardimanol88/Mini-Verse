document.addEventListener('DOMContentLoaded', async () => {
    // Obtener ID de serie de los parámetros de URL
    const urlParams = new URLSearchParams(window.location.search);
    const serieId = urlParams.get('id');

    // Guardar ID de serie en localStorage para usar en reseña.html
    localStorage.setItem('currentSerieId', serieId);
    console.log('ID de serie guardado:', localStorage.getItem('currentSerieId'));

    if (!serieId) {
        mostrarError('No se encontró la serie solicitada');
        return;
    }
    

    try {
        // Obtener datos de la serie desde la API
        const response = await fetch(`http://44.209.91.221:7002/detalleSerie?id=${serieId}`);
        if (!response.ok) {
            throw new Error('Error al obtener datos de la serie');
        }
        
        const data = await response.json();
        actualizarVistaSerie(data);
        
        // Configurar botón de favoritos
        configurarBotonFavoritosSerie(serieId);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('Error al cargar los datos de la serie');
    }
});


async function actualizarVistaSerie(data) {
    // Normalización de datos
    const normalizedData = {
        ...data,
        serie: {
            ...data.serie,
            imagen_url: data.serie.imagen_url || data.serie.imagenUrl
        },
        temporadas: data.temporadas?.map(t => ({
            ...t,
            imagen_url: t.imagen_url || t.imagenUrl,
            descripcion: t.descripcion || t.description
        })) || []
    };

    const { serie, temporadas, capitulosPorTemporada } = normalizedData;

    // Actualizar información básica de la serie
    const serieImage = document.getElementById('serieImage');
    serieImage.style.backgroundImage = `url('${serie.imagen_url || 'https://via.placeholder.com/300x450?text=Imagen+no+disponible'}')`;
    
    document.getElementById('year').textContent = serie.estreno || 'Año no disponible';
    document.getElementById('serieTitle').textContent = serie.nombre || 'Serie sin título';
    document.getElementById('serieDescription').textContent = serie.sinopsis || 'Descripción no disponible';

    // Renderizar temporadas
    const seasonsContainer = document.getElementById('seasonsContainer');
    seasonsContainer.innerHTML = '';
    
    if (!temporadas || temporadas.length === 0) {
        seasonsContainer.innerHTML = '<p class="no-seasons">No hay temporadas disponibles</p>';
        return;
    }
    
    temporadas.forEach(temporada => {
        const seasonCard = document.createElement('div');
        seasonCard.className = 'season-card';
        
        seasonCard.innerHTML = `
            <div class="season-image" style="background-image: url('${temporada.imagen_url || ''}')">
                ${!temporada.imagen_url ? '<span>Imagen no disponible</span>' : ''}
            </div>
            <div class="season-info">
                <input type="checkbox" id="season${temporada.id}" class="season-toggle">
                <label for="season${temporada.id}" class="season-title">
                    ${temporada.nombre || `Temporada ${temporada.numeroTemporada}`}
                </label>
                <div class="season-episodes" id="episodes${temporada.id}">
                    ${renderizarCapitulos(capitulosPorTemporada?.[temporada.id] || [], serie.id)}
                </div>
                ${temporada.descripcion ? `<div class="season-description">${temporada.descripcion}</div>` : ''}
            </div>
        `;
        
        seasonsContainer.appendChild(seasonCard);
    });
}


function renderizarCapitulos(capitulos, idSerie) {
    if (capitulos.length === 0) {
        return '<div class="episode empty">No hay capítulos disponibles</div>';
    }

    return capitulos.map(capitulo => {
        let duracion = '00:00:00';
        try {
            if (capitulo.duracion) {
                if (typeof capitulo.duracion === 'string') {
                    const timeParts = capitulo.duracion.split(':');
                    if (timeParts.length === 3) {
                        duracion = capitulo.duracion;
                    }
                } else if (capitulo.duracion.hour !== undefined) {
                    const pad = num => num.toString().padStart(2, '0');
                    duracion = `${pad(capitulo.duracion.hour)}:${pad(capitulo.duracion.minute)}:${pad(capitulo.duracion.second)}`;
                }
            }
        } catch (e) {
            console.error('Error al formatear duración:', e);
        }

        return `
            <div class="episode" onclick="window.location.href='reseña.html?idCapitulo=${capitulo.id}&idSerie=${idSerie}'">
                <div class="episode-info">
                    <span class="episode-number">Episodio ${capitulo.numero}</span>
                    <span class="episode-duration">${duracion}</span>
                </div>
                <div class="episode-title">${capitulo.titulo || 'Título no disponible'}</div>
            </div>
        `;
    }).join('');
}


async function configurarBotonFavoritosSerie(serieId) {
    const favoriteBtn = document.getElementById('favoriteBtn');
    if (!favoriteBtn) return;

    favoriteBtn.addEventListener('click', async () => {
        if (!verificarAutenticacion()) return;
        
        const token = localStorage.getItem('authToken');
        if (!token) {
            mostrarError('Debes iniciar sesión para agregar a favoritos');
            return;
        }

        try {
            mostrarCargando(true);
            
            const response = await fetch(`http://44.209.91.221:7002/agregarFavorita/${serieId}`, {
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
                throw new Error(errorText || 'Error al agregar a favoritos');
            }
            
            // Actualizar estado del botón
            favoriteBtn.classList.add('favorited');
            favoriteBtn.innerHTML = `
                <svg class="star-icon" width="16" height="16" viewBox="0 0 24 24" fill="#FFD700" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" stroke="#FFD700" stroke-width="2" stroke-linejoin="round"/>
                </svg>
                En favoritos
            `;
            mostrarError('Serie agregada a favoritos');
            
        } catch (error) {
            console.error('Error:', error);
            mostrarError(error.message.includes('No se puede agregar mas de 4 series favoritas') 
                ? 'Límite de 4 favoritos alcanzado' 
                : error.message);
        } finally {
            mostrarCargando(false);
        }
    });

    // Verificar estado inicial
    await verificarEstadoFavorito(serieId, favoriteBtn);
}


async function verificarEstadoFavorito(serieId, favoriteBtn) {
    const token = localStorage.getItem('authToken');
    if (!token || !favoriteBtn) return;

    try {
        const idUsuario = await obtenerIdUsuario(token);
        if (!idUsuario) return;

        const response = await fetch(`http://44.209.91.221:7002/obtenerFavoritas?idUsuario=${idUsuario}`);
        if (!response.ok) return;

        const favoritas = await response.json();
        if (favoritas.some(serie => serie.id == serieId)) {
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


async function obtenerIdUsuario(token) {
    try {
        const response = await fetch('http://44.209.91.221:7002/verificarToken', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) return null;
        const data = await response.json();
        return data.id;
    } catch (error) {
        console.error('Error obteniendo ID de usuario:', error);
        return null;
    }
}


function verificarAutenticacion() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        mostrarError('Debes iniciar sesión para realizar esta acción');
        window.location.href = `/usuarios/ingresar.html?redirect=${encodeURIComponent(window.location.pathname + window.location.search)}`;
        return false;
    }
    return true;
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

// Función para mostrar/ocultar indicador de carga
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

// Función para actualizar la UI según estado de autenticación
function actualizarUI() {
    const isLoggedIn = !!localStorage.getItem('authToken');

}

