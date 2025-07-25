document.addEventListener('DOMContentLoaded', () => {
    const searchBtn = document.querySelector('.search-btn');
    const searchContainer = document.getElementById('searchContainer');
    const searchInput = document.getElementById('searchInput');
    const searchCloseBtn = document.getElementById('searchCloseBtn');
    
    if (!searchBtn || !searchContainer || !searchInput) return;
    
    // Toggle del buscador
    searchBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        searchContainer.style.display = searchContainer.style.display === 'none' ? 'flex' : 'none';
        if (searchContainer.style.display === 'flex') {
            searchInput.focus();
        }
    });
    
    // Cerrar buscador
    searchCloseBtn.addEventListener('click', () => {
        searchContainer.style.display = 'none';
        clearSearchResults();
    });
    
    // Cerrar al hacer click fuera
    document.addEventListener('click', (e) => {
        if (!searchContainer.contains(e.target)) {
            searchContainer.style.display = 'none';
            clearSearchResults();
        }
    });
    
    // Buscar al presionar Enter
    searchInput.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') {
            const query = searchInput.value.trim();
            if (query.length >= 2) {
                buscarSeries(query);
            }
        }
    });
    
    // Limpiar resultados al borrar
    searchInput.addEventListener('input', (e) => {
        if (e.target.value.trim() === '') {
            clearSearchResults();
        }
    });
});

function buscarSeries(query) {
    const token = localStorage.getItem('authToken');
    
    if (!token) {
        mostrarError('Debes iniciar sesión para buscar series');
        return;
    }
    
    mostrarCargando(true);
    
    fetch(`http://44.209.91.221:7002/busquedaSeries?nombre=${encodeURIComponent(query)}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401) {
            localStorage.removeItem('authToken');
            window.location.reload();
            throw new Error('Sesión expirada');
        }
        return response.json();
    })
    .then(series => {
        mostrarResultadosBusqueda(series);
    })
    .catch(error => {
        console.error('Error al buscar series:', error);
        mostrarError('Error al buscar series');
    })
    .finally(() => {
        mostrarCargando(false);
    });
}

function mostrarResultadosBusqueda(series) {
    const searchContainer = document.getElementById('searchContainer');
    let resultsContainer = document.getElementById('searchResults');
    
    // Crear contenedor si no existe
    if (!resultsContainer) {
        resultsContainer = document.createElement('div');
        resultsContainer.id = 'searchResults';
        resultsContainer.className = 'search-results';
        searchContainer.appendChild(resultsContainer);
    } else {
        resultsContainer.innerHTML = '';
    }
    
    if (!series || series.length === 0) {
        resultsContainer.innerHTML = '<div class="no-results">No se encontraron series</div>';
        resultsContainer.style.display = 'block';
        return;
    }
    
    series.forEach(serie => {
        const serieItem = document.createElement('div');
        serieItem.className = 'search-result-item';
        
        const imagenUrl = serie.imagen_url || serie.imagenUrl || 'https://via.placeholder.com/50x75?text=No+Image';
        
        serieItem.innerHTML = `
            <div class="search-result-image" style="background-image: url('${imagenUrl}')"></div>
            <div class="search-result-info">
                <div class="search-result-title">${serie.nombre || 'Serie sin título'}</div>
                <div class="search-result-year">${serie.estreno || 'Año desconocido'}</div>
            </div>
        `;
        
        serieItem.addEventListener('click', () => {
            window.location.href = `serie.html?id=${serie.id}`;
            clearSearchResults();
        });
        
        resultsContainer.appendChild(serieItem);
    });
    
    resultsContainer.style.display = 'block';
}

function clearSearchResults() {
    const resultsContainer = document.getElementById('searchResults');
    if (resultsContainer) {
        resultsContainer.style.display = 'none';
        resultsContainer.innerHTML = '';
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
            <span>Buscando...</span>
        `;
        document.body.appendChild(loader);
    }
}