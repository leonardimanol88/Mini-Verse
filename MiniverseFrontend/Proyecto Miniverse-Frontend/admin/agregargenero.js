document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.genre-form');
    const genreInput = document.getElementById('genre-name');
    const registeredSection = document.querySelector('.registered-section');
    
    const API_BASE_URL = 'http://44.209.91.221:7002'; 
    
    function showMessage(message, isError = false) {
        const existingMessage = document.querySelector('.message');
        if (existingMessage) {
            existingMessage.remove();
        }
        
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${isError ? 'error' : 'success'}`;
        messageDiv.textContent = message;
        
        messageDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 8px;
            color: white;
            font-weight: 500;
            z-index: 1000;
            opacity: 0;
            transform: translateY(-20px);
            transition: all 0.3s ease;
            background-color: ${isError ? '#dc3545' : '#28a745'};
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
        `;
        
        document.body.appendChild(messageDiv);
        
        setTimeout(() => {
            messageDiv.style.opacity = '1';
            messageDiv.style.transform = 'translateY(0)';
        }, 100);
        
        setTimeout(() => {
            messageDiv.style.opacity = '0';
            messageDiv.style.transform = 'translateY(-20px)';
            setTimeout(() => {
                if (messageDiv.parentNode) {
                    messageDiv.parentNode.removeChild(messageDiv);
                }
            }, 300);
        }, 3000);
    }
    
    function addGenreToList(genreName) {
        const registeredItem = document.createElement('div');
        registeredItem.className = 'registered-item';
        registeredItem.innerHTML = `
            <span class="item-name">${genreName}</span>
            <button class="delete-btn" data-genre="${genreName}">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M19 7L18.1327 19.1425C18.0579 20.1891 17.187 21 16.1378 21H7.86224C6.81296 21 5.94208 20.1891 5.86732 19.1425L5 7M10 11V17M14 11V17M15 7V4C15 3.44772 14.5523 3 14 3H10C9.44772 3 9 3.44772 9 4V7M4 7H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        `;
        
        // Agregar evento de clic al botón de eliminar
        const deleteBtn = registeredItem.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', (e) => {
            e.preventDefault();
            deleteGenre(genreName);
        });
        
        registeredSection.appendChild(registeredItem);
        
        // Animación de entrada
        registeredItem.style.opacity = '0';
        registeredItem.style.transform = 'translateY(20px)';
        setTimeout(() => {
            registeredItem.style.transition = 'all 0.3s ease';
            registeredItem.style.opacity = '1';
            registeredItem.style.transform = 'translateY(0)';
        }, 100);
    }

    async function deleteGenre(genreName) {
        if (!confirm(`¿Estás seguro de eliminar el género "${genreName}"? Esta acción no se puede deshacer.`)) {
            return;
        }
        
        try {
            const response = await fetch(`${API_BASE_URL}/admin/eliminarGenero/${encodeURIComponent(genreName)}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json'
                }
            });
            
            if (response.ok) {
                showMessage(`Género "${genreName}" eliminado correctamente`);
                // Encontrar y eliminar el elemento visualmente con animación
                const items = document.querySelectorAll('.registered-item');
                items.forEach(item => {
                    if (item.querySelector('.item-name').textContent === genreName) {
                        item.style.transform = 'translateX(100%)';
                        item.style.opacity = '0';
                        setTimeout(() => item.remove(), 300);
                    }
                });
                
                // Si no quedan géneros, mostrar mensaje
                if (document.querySelectorAll('.registered-item').length === 0) {
                    const emptyMessage = document.createElement('p');
                    emptyMessage.className = 'no-genres';
                    emptyMessage.textContent = 'No hay géneros registrados';
                    registeredSection.appendChild(emptyMessage);
                }
                
                // Notificar a la ventana padre si es necesario
                if (window.opener && !window.opener.closed) {
                    window.opener.postMessage({ type: 'generoEliminado', nombre: genreName }, '*');
                }
            } else {
                const errorData = await response.json();
                showMessage(errorData.error || 'Error al eliminar el género', true);
            }
        } catch (error) {
            console.error('Error al eliminar género:', error);
            showMessage('Error de conexión con el servidor', true);
        }
    }
    
    function validateGenreName(name) {
        if (!name || name.trim().length === 0) {
            return { isValid: false, message: 'El nombre del género es obligatorio' };
        }
        
        if (name.trim().length < 2) {
            return { isValid: false, message: 'El nombre del género debe tener al menos 2 caracteres' };
        }
        
        if (name.trim().length > 50) {
            return { isValid: false, message: 'El nombre del género no puede tener más de 50 caracteres' };
        }
        
        if (/^\d+$/.test(name.trim())) {
            return { isValid: false, message: 'El nombre del género no puede ser solo números' };
        }
        
        return { isValid: true };
    }
    
    async function loadExistingGenres() {
        try {
            const response = await fetch(`${API_BASE_URL}/admin/obtenerGeneros`);
            if (!response.ok) {
                throw new Error('Error al obtener géneros');
            }
            const generos = await response.json();
            
            // Limpiar la sección
            registeredSection.innerHTML = '<h2 class="section-title">Géneros Registrados</h2>';
            
            if (generos.length === 0) {
                const emptyMessage = document.createElement('p');
                emptyMessage.className = 'no-genres';
                emptyMessage.textContent = 'No hay géneros registrados';
                registeredSection.appendChild(emptyMessage);
                return;
            }
            
            // Agregar cada género con su botón de eliminar
            generos.forEach(genero => {
                const genreItem = document.createElement('div');
                genreItem.className = 'registered-item';
                genreItem.innerHTML = `
                    <span class="item-name">${genero.nombre}</span>
                    <button class="delete-btn" data-genre="${genero.nombre}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M19 7L18.1327 19.1425C18.0579 20.1891 17.187 21 16.1378 21H7.86224C6.81296 21 5.94208 20.1891 5.86732 19.1425L5 7M10 11V17M14 11V17M15 7V4C15 3.44772 14.5523 3 14 3H10C9.44772 3 9 3.44772 9 4V7M4 7H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                `;
                
                // Evento para eliminar
                genreItem.querySelector('.delete-btn').addEventListener('click', (e) => {
                    e.preventDefault();
                    deleteGenre(genero.nombre);
                });
                
                // Animación
                genreItem.style.opacity = '0';
                genreItem.style.transform = 'translateY(20px)';
                registeredSection.appendChild(genreItem);
                
                setTimeout(() => {
                    genreItem.style.transition = 'all 0.3s ease';
                    genreItem.style.opacity = '1';
                    genreItem.style.transform = 'translateY(0)';
                }, 100 * generos.indexOf(genero));
            });
            
        } catch (error) {
            console.error('Error al cargar géneros:', error);
            showMessage('Error al cargar la lista de géneros', true);
        }
    }
    
    
    async function submitGenre(genreName) {
        const submitBtn = document.querySelector('.submit-btn');
        const originalText = submitBtn.textContent;
        
        submitBtn.disabled = true;
        submitBtn.textContent = 'Guardando...';
        submitBtn.style.opacity = '0.7';
        
        try {
            const response = await fetch(`${API_BASE_URL}/admin/agregarGenero`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    nombre: genreName.trim()
                })
            });
            
            if (response.ok) {
                const data = await response.json();
                showMessage(data.mensaje || 'Género agregado correctamente');
                
                // Recargar la lista de géneros después de agregar uno nuevo
                await loadExistingGenres();
                
                genreInput.value = '';
                genreInput.focus();

                if (window.opener && !window.opener.closed) {
                    window.opener.postMessage({ type: 'generoAgregado' }, '*');
                }
                
                return true;
            } else {
                const errorData = await response.json();
                showMessage(errorData.error || 'Error al agregar el género', true);
                return false;
            }
        } catch (error) {
            console.error('Error de conexión:', error);
            showMessage('Error de conexión con el servidor', true);
            return false;
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
            submitBtn.style.opacity = '1';
        }
    }
    
    // Event listeners
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const genreName = genreInput.value;
        
        const validation = validateGenreName(genreName);
        if (!validation.isValid) {
            showMessage(validation.message, true);
            genreInput.focus();
            return;
        }
        
        await submitGenre(genreName);
    });
    
    genreInput.addEventListener('input', function() {
        const name = this.value;
        const validation = validateGenreName(name);
        
        if (name.length > 0 && !validation.isValid) {
            this.style.borderColor = '#dc3545';
        } else {
            this.style.borderColor = '#404040';
        }
    });
    
    genreInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            form.dispatchEvent(new Event('submit'));
        }
    });
    
    // Inicialización
    loadExistingGenres();
    genreInput.focus();
});

// Manejadores de errores globales
window.addEventListener('error', function(e) {
    console.error('Error global:', e.error);
});

window.addEventListener('unhandledrejection', function(e) {
    console.error('Promesa rechazada:', e.reason);
});