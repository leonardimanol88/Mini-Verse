const API_BASE_URL = 'http://44.209.91.221:7002';

document.addEventListener('DOMContentLoaded', function() {
    // Verificar autenticación
    checkAdminAuth();
    
    // Cargar datos iniciales
    loadStatistics();
    
    // Configurar botón de refresh
    document.getElementById('refreshBtn').addEventListener('click', loadStatistics);
    
    // Configurar botón de logout
    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('adminToken');
        window.location.href = '/login.html';
    });
});

function loadStatistics() {
    showLoading(true);
    
    const token = localStorage.getItem('adminToken');
    if (!token) {
        showMessage('Error de autenticación', 'error');
        window.location.href = '/login.html';
        return;
    }
    
    Promise.all([
        fetch(`${API_BASE_URL}/admin/capitulosMasResenados`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }),
        fetch(`${API_BASE_URL}/admin/capitulosMasResenadosEdad`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
    ])
    .then(async responses => {
        // Verificar respuestas
        const errors = responses.filter(res => !res.ok);
        if (errors.length > 0) {
            throw new Error(`Error en petición: ${errors.map(e => e.statusText).join(', ')}`);
        }
        
        // Parsear JSON
        const [topReviewed, ageStats] = await Promise.all(responses.map(res => res.json()));
        
        // Validar datos
        if (!Array.isArray(topReviewed) || !Array.isArray(ageStats)) {
            throw new Error('Formato de datos incorrecto');
        }
        
        return [topReviewed, ageStats];
    })
    .then(([topReviewed, ageStats]) => {
        renderTopReviewedChart(topReviewed);
        renderTopReviewedTable(topReviewed);
        renderAgeReviewersChart(ageStats);
        renderAgeReviewersTable(ageStats);
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Error al cargar estadísticas: ' + error.message, 'error');
    })
    .finally(() => {
        showLoading(false);
    });
}

function renderTopReviewedChart(data) {
    const ctx = document.getElementById('topReviewedChart').getContext('2d');
    
    // Verificar si hay datos
    if (!data || data.length === 0) {
        console.error('No hay datos para mostrar el gráfico');
        return;
    }

    // Limitar a los 10 primeros para mejor visualización
    const limitedData = data.slice(0, 10);
    const labels = limitedData.map(item => `${item.titulo} (${item.serie})`);
    const values = limitedData.map(item => item.cantidadResenas);
    
    // Destruir el gráfico anterior si existe y es válido
    if (window.topReviewedChart && typeof window.topReviewedChart.destroy === 'function') {
        window.topReviewedChart.destroy();
    }
    
    // Crear nuevo gráfico
    try {
        window.topReviewedChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Número de reseñas',
                    data: values,
                    backgroundColor: '#ff4500',
                    borderColor: '#ff6600',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `Reseñas: ${context.raw}`;
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: '#fff'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    },
                    x: {
                        ticks: {
                            color: '#fff',
                            callback: function(value) {
                                // Acortar etiquetas largas
                                const label = this.getLabelForValue(value);
                                return label.length > 20 ? label.substring(0, 20) + '...' : label;
                            }
                        },
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error al crear el gráfico:', error);
    }
}

function renderTopReviewedTable(data) {
    const tbody = document.getElementById('topReviewedTableBody');
    tbody.innerHTML = '';
    
    data.forEach((item, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${item.titulo}</td>
            <td>${item.serie}</td>
            <td>${item.cantidadResenas}</td>
        `;
        tbody.appendChild(row);
    });
}

function renderAgeReviewersChart(data) {
    const ctx = document.getElementById('ageReviewersChart').getContext('2d');
    
    // Verificar si hay datos
    if (!data || data.length === 0) {
        console.error('No hay datos para mostrar el gráfico');
        return;
    }

    // Limitar a los 10 primeros para mejor visualización
    const limitedData = data.slice(0, 10);
    const labels = limitedData.map(item => `${item.titulo} (${item.serie})`);
    const ages = limitedData.map(item => item.edadPromedio);
    const reviews = limitedData.map(item => item.cantidadResenas);
    
    // Destruir el gráfico anterior si existe y es válido
    if (window.ageReviewersChart && typeof window.ageReviewersChart.destroy === 'function') {
        window.ageReviewersChart.destroy();
    }
    
    // Crear nuevo gráfico
    try {
        window.ageReviewersChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Edad promedio',
                        data: ages,
                        backgroundColor: '#ff8c00',
                        borderColor: '#ffa500',
                        borderWidth: 1,
                        yAxisID: 'y'
                    },
                    {
                        label: 'Número de reseñas',
                        data: reviews,
                        backgroundColor: 'rgba(255, 69, 0, 0.5)',
                        borderColor: 'rgba(255, 69, 0, 0.8)',
                        borderWidth: 1,
                        yAxisID: 'y1',
                        type: 'line'
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                if (context.datasetIndex === 0) {
                                    return `Edad promedio: ${context.raw.toFixed(1)} años`;
                                } else {
                                    return `Reseñas: ${context.raw}`;
                                }
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        title: {
                            display: true,
                            text: 'Edad promedio',
                            color: '#fff'
                        },
                        ticks: {
                            color: '#fff'
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)'
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        title: {
                            display: true,
                            text: 'Número de reseñas',
                            color: '#fff'
                        },
                        ticks: {
                            color: '#fff'
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    },
                    x: {
                        ticks: {
                            color: '#fff',
                            callback: function(value) {
                                // Acortar etiquetas largas
                                const label = this.getLabelForValue(value);
                                return label.length > 20 ? label.substring(0, 20) + '...' : label;
                            }
                        },
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error al crear el gráfico:', error);
    }
}

function renderAgeReviewersTable(data) {
    const tbody = document.getElementById('ageReviewersTableBody');
    tbody.innerHTML = '';
    
    data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.titulo}</td>
            <td>${item.serie}</td>
            <td>${item.edadPromedio.toFixed(1)}</td>
            <td>${item.cantidadResenas}</td>
        `;
        tbody.appendChild(row);
    });
}

function showLoading(loading) {
    const refreshBtn = document.getElementById('refreshBtn');
    if (loading) {
        refreshBtn.disabled = true;
        refreshBtn.innerHTML = `
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" class="spin">
                <path d="M12 2v4m0 12v4M4.93 4.93l2.83 2.83m8.48 8.48l2.83 2.83M2 12h4m12 0h4M4.93 19.07l2.83-2.83m8.48-8.48l2.83-2.83" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Cargando...
        `;
    } else {
        refreshBtn.disabled = false;
        refreshBtn.innerHTML = `
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Actualizar Datos
        `;
    }
}

function showMessage(text, type) {
    const messageEl = document.getElementById('message');
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    
    // Mostrar el mensaje
    setTimeout(() => {
        messageEl.style.opacity = '1';
        messageEl.style.transform = 'translateY(0)';
    }, 10);
    
    // Ocultar después de 5 segundos
    setTimeout(() => {
        messageEl.style.opacity = '0';
        messageEl.style.transform = 'translateY(-20px)';
    }, 5000);
}

// Añadir estilo para el spinner
const style = document.createElement('style');
style.textContent = `
    .spin {
        animation: spin 1s linear infinite;
    }
    @keyframes spin {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
    }
`;
document.head.appendChild(style);