document.addEventListener('DOMContentLoaded', async () => {
    const directorId = localStorage.getItem('currentDirectorId');
    
    if (!directorId) {
        mostrarError('No se especificó un director');
        window.location.href = 'inicio.html';
        return;
    }
    
    await cargarDirector(directorId);
});

async function cargarDirector(directorId) {
    try {
        mostrarCargando(true);
        
        
        const response = await fetch(`http://44.209.91.221:7002/buscarDirectorPorId?id=${directorId}`);
        
        if (!response.ok) {
            throw new Error('Error al obtener datos del director');
        }
        
        const director = await response.json();
        mostrarDatosDirector(director);
    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los datos del director');
        window.location.href = 'inicio.html';
    } finally {
        mostrarCargando(false);
    }
}

function mostrarDatosDirector(director) {
    document.getElementById('directorName').textContent = director.nombre || 'Nombre no disponible';
    
    const biographyText = document.getElementById('biographyText');
    if (director.biografia) {
        biographyText.innerHTML = `<p>${director.biografia.replace(/\n/g, '</p><p>')}</p>`;
    } else {
        biographyText.innerHTML = '<p>Biografía no disponible</p>';
    }
    
    
    const directorImage = document.getElementById('directorImage');
    if (director.imagen_url) {
        directorImage.style.backgroundImage = `url(${director.imagen_url})`;
        directorImage.textContent = '';
    } else {
        directorImage.textContent = 'Imagen no disponible';
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