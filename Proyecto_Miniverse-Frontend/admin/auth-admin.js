//const API_BASE_URL = 'http://44.209.91.221:7002';

// Función para verificar autenticación
async function verifyAuth() {
    const token = localStorage.getItem('adminToken');
    const isLoginPage = window.location.pathname.includes('login-admin.html');
    
    // Si no hay token y no es página de login -> redirigir a login
    if (!token && !isLoginPage) {
        localStorage.setItem('redirectAfterLogin', window.location.pathname);
        window.location.href = '/admin/login-admin.html';
        return false;
    }
    
    // Si hay token y es página de login -> redirigir a admin
    if (token && isLoginPage) {
        const redirectTo = localStorage.getItem('redirectAfterLogin') || '/admin/admin.html';
        localStorage.removeItem('redirectAfterLogin');
        window.location.href = redirectTo;
        return false;
    }
    
    return true;
}

// Función para cerrar sesión
function logout() {
    localStorage.removeItem('adminToken');
    window.location.href = '/admin/login-admin.html';
}

// Interceptor para añadir token a las peticiones
function setupFetchInterceptor() {
    const originalFetch = window.fetch;
    
    window.fetch = async function(url, options = {}) {
        // No añadir token a la petición de login
        if (!url.includes('/admin/iniciarSesion')) {
            const token = localStorage.getItem('adminToken');
            if (token) {
                options.headers = {
                    ...options.headers,
                    'Authorization': `Bearer ${token}`
                };
            }
        }
        
        const response = await originalFetch(url, options);
        
        // Si recibimos 401, redirigir a login
        if (response.status === 401) {
            localStorage.removeItem('adminToken');
            localStorage.setItem('redirectAfterLogin', window.location.pathname);
            window.location.href = '/admin/login-admin.html';
            return Promise.reject('Unauthorized');
        }
        
        return response;
    };
}

// Configuración inicial
document.addEventListener('DOMContentLoaded', async () => {
    // Solo verificar autenticación si no es la página de login
    if (!window.location.pathname.includes('login-admin.html')) {
        await verifyAuth();
    }
    
    // Configurar botones de logout
    document.querySelectorAll('.logout-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    });
    
    // Configurar interceptor de fetch
    setupFetchInterceptor();
});

function checkAdminAuth() {
    return verifyAuth();
}