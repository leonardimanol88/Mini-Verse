let generoSeleccionado = null;

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('authToken');
    const loginItem = document.getElementById('loginItem');
    const registerItem = document.getElementById('registerItem');
    const logoutItem = document.getElementById('logoutItem');

    if (token) {
        // Usuario autenticado
        if (loginItem) loginItem.style.display = 'none';
        if (registerItem) registerItem.style.display = 'none';
        if (logoutItem) logoutItem.style.display = 'block';
    } else {
        // Usuario no autenticado
        if (loginItem) loginItem.style.display = 'block';
        if (registerItem) registerItem.style.display = 'block';
        if (logoutItem) logoutItem.style.display = 'none';
    }

    // Manejar cierre de sesión
    if (logoutItem) {
        logoutItem.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('authToken');
            window.location.href = '/inicio.html';
        });
    }
});
document.addEventListener('DOMContentLoaded', async () => {
    await cargarGeneros(); // Cargar géneros primero
    await cargarTodasLasSeries();
});

// Función para cargar géneros desde la API
async function cargarGeneros() {
    try {
        const response = await fetch('http://44.209.91.221:7002/obtenerGenerosparaUsuario');
        if (!response.ok) {
            throw new Error('Error al obtener géneros');
        }
        const generos = await response.json();
        
        const container = document.getElementById('genresContainer');
        container.innerHTML = ''; // Limpiar contenedor
        
        // Verificar si hay géneros
        if (generos.length === 0) {
            const mensaje = document.createElement('p');
            mensaje.textContent = 'No hay géneros disponibles';
            mensaje.style.color = '#ccc';
            container.appendChild(mensaje);
            return;
        }
        
        
        generos.forEach(genero => {
            const tag = document.createElement('div');
            tag.className = 'genre-tag';
            tag.textContent = genero.nombre;
            
            tag.addEventListener('click', async () => {
                if (generoSeleccionado === genero.nombre) {
                    generoSeleccionado = null;
                    tag.classList.remove('active');
                    await cargarTodasLasSeries();
                } else {
                    generoSeleccionado = genero.nombre;
                    document.querySelectorAll('.genre-tag').forEach(t => t.classList.remove('active'));
                    tag.classList.add('active');
                    await cargarSeriesPorGenero(genero.nombre);
                }
            });
            
            container.appendChild(tag);
        });
        
    } catch (error) {
        console.error('Error al cargar géneros:', error);
        mostrarMensajeError('Error al cargar los géneros');
    }
}

async function cargarSeriesPorGenero(genero) {
    try {
        console.log(`Enviando género: "${genero}"`);
        
        // Codifica el género para URL y reemplaza espacios si es necesario
        const generoCodificado = encodeURIComponent(genero);
        const url = `http://44.209.91.221:7002/mostrarSeriesporGenero?genero=${generoCodificado}`;
        
        console.log('URL completa:', url);
        
        const response = await fetch(url);
        
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Error en respuesta:', response.status, errorText);
            throw new Error(`Error ${response.status}: ${errorText}`);
        }
        
        const data = await response.json();
        console.log('Datos recibidos:', data);
        
        // Verificación del formato de los datos
        if (!Array.isArray(data)) {
            console.error('Formato de datos incorrecto:', data);
            throw new Error('Formato de respuesta no válido');
        }
        
        mostrarSeries(data);
        
    } catch (error) {
        console.error('Error al cargar series por género:', error);
        mostrarMensajeError(`Error al cargar series: ${error.message}`);
        
        // Restaura el estado anterior
        generoSeleccionado = null;
        document.querySelectorAll('.genre-tag').forEach(t => t.classList.remove('active'));
        await cargarTodasLasSeries();
    }
}


function mostrarMensajeError(mensaje) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = mensaje;
    document.body.appendChild(errorDiv);
    
    setTimeout(() => {
        errorDiv.remove();
    }, 3000);
}

async function cargarTodasLasSeries() {
    try {
        const response = await fetch('http://44.209.91.221:7002/mostrarSeries');
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        
        const series = await response.json();
        
        // Verificar que las series tengan IDs
        const seriesValidas = series.filter(serie => {
            if (!serie.id) {
                console.warn('Serie sin ID ignorada:', serie);
                return false;
            }
            return true;
        });
        
        if (seriesValidas.length === 0) {
            mostrarMensajeError('No se encontraron series válidas');
            return;
        }
        
        mostrarSeries(mezclarSeries(seriesValidas));
    } catch (error) {
        console.error('Error al cargar todas las series:', error);
        mostrarMensajeError('Error al cargar el catálogo');
    }
}

function mezclarSeries(array) {
    const newArray = [...array];
    for (let i = newArray.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [newArray[i], newArray[j]] = [newArray[j], newArray[i]];
    }
    return newArray;
}

function mostrarSeries(series) {
    const container = document.getElementById('seriesContainer');
    container.innerHTML = '';

    series.forEach(serie => {
        // Verifica que la serie tenga un ID
        if (!serie.id) {
            console.error('Serie sin ID:', serie);
            return; // Saltar esta serie si no tiene ID
        }

        const card = document.createElement('div');
        card.className = 'series-card';
        
        // Asegurar que el ID sea un número
        const serieId = parseInt(serie.id);
        if (isNaN(serieId)) {
            console.error('ID de serie no válido:', serie.id);
            return;
        }

        card.addEventListener('click', () => {
            console.log('Redirigiendo a serie con ID:', serieId); // Para depuración
            window.location.href = `serie.html?id=${serieId}`;
        });
        
        const imageDiv = document.createElement('div');
        imageDiv.className = 'series-image';
        
        let imagenUrl = serie.imagen_url || serie.imagenUrl || '';
        
        if (imagenUrl && !imagenUrl.startsWith('http') && !imagenUrl.startsWith('/')) {
            imagenUrl = `/${imagenUrl}`;
        }
        
        if (!imagenUrl || imagenUrl.trim() === '') {
            imagenUrl = 'https://via.placeholder.com/300x450?text=No+Image';
        }
        
        imageDiv.style.backgroundImage = `url('${imagenUrl}')`;
        
        const title = document.createElement('h3');
        title.className = 'series-title';
        title.textContent = serie.nombre || 'Serie sin título';
        
        card.appendChild(imageDiv);
        card.appendChild(title);
        container.appendChild(card);
    });
}


window.addEventListener('message', (event) => {
    if (event.data.type === 'generoAgregado') {
        cargarGeneros();
    }
});