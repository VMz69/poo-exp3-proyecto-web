<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Ejemplares - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            box-shadow: 0 20px 50px rgba(0,0,0,0.4);
            border-radius: 25px;
            border: none;
        }
        .card-header {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border-radius: 25px 25px 0 0 !important;
        }
        .badge-libro { background: #0d6efd; }
        .badge-tesis { background: #198754; }
        .badge-revista { background: #ffc107; color: black; }
        .badge-cd { background: #fd7e14; }
        .badge-dvd { background: #dc3545; }
        .badge-informe { background: #6f42c1; }
        .badge-manual { background: #20c997; }
        .ejemplar-card {
            transition: all 0.3s;
            border: 2px solid transparent;
        }
        .ejemplar-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.3);
            border-color: #667eea;
        }
        .disponible {
            background: linear-gradient(45deg, #28a745, #20c997);
            color: white;
        }
        .no-disponible {
            background: linear-gradient(45deg, #dc3545, #c82333);
            color: white;
        }
        .buscador {
            border-radius: 50px;
            padding: 15px 25px;
            font-size: 1.1em;
        }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="card">
        <div class="card-header text-center">
            <h1 class="mb-0">
                <i class="fas fa-search fa-lg"></i> Consulta de Ejemplares
            </h1>
            <p class="mb-0 mt-2 fs-5 opacity-90">
                Busca libros, tesis, revistas y más
            </p>
        </div>

        <div class="card-body p-5">
            <!-- Bienvenida al usuario -->
            <div class="text-center mb-4">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <h4>
                            <i class="fas fa-user-graduate text-primary"></i>
                            Bienvenido(a), <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong>
                        </h4>
                        <p class="text-muted">Tipo: ${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo}</p>
                    </c:when>
                    <c:otherwise>
                        <h4>
                            <i class="fas fa-user-graduate text-primary"></i>
                            Bienvenido(a)
                        </h4>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Buscador -->
            <div class="row mb-4">
                <div class="col-md-8 mx-auto">
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0">
                            <i class="fas fa-search"></i>
                        </span>
                        <input type="text" id="buscador" class="form-control buscador border-start-0 shadow-sm"
                               placeholder="Buscar por título, autor, ISBN, categoría..." autofocus>
                    </div>
                </div>
            </div>

            <!-- Filtros rápidos -->
            <div class="text-center mb-4">
                <button class="btn btn-outline-primary btn-sm me-2" onclick="filtrarTipo('')">Todos</button>
                <button class="btn btn-outline-primary btn-sm me-2" onclick="filtrarTipo('Libro')">Libros</button>
                <button class="btn btn-outline-success btn-sm me-2" onclick="filtrarTipo('Tesis')">Tesis</button>
                <button class="btn btn-outline-warning btn-sm me-2" onclick="filtrarTipo('Revista')">Revistas</button>
                <button class="btn btn-outline-info btn-sm" onclick="filtrarTipo('CD')">CD/DVD</button>
            </div>

            <!-- Resultados -->
            <div class="row" id="resultados">
                <c:forEach var="e" items="${ejemplares}">
                    <%
                        // Obtener el ejemplar actual del contexto
                        com.biblioteca.beans.Ejemplar ejemplar = (com.biblioteca.beans.Ejemplar) pageContext.getAttribute("e");
                        String fechaFormateada = "";
                        if (ejemplar != null && ejemplar.getFechaIngreso() != null) {
                            fechaFormateada = ejemplar.getFechaIngreso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        }
                        pageContext.setAttribute("fechaFormateada", fechaFormateada);
                    %>
                    <div class="col-md-6 col-lg-4 mb-4 ejemplar-card"
                         data-titulo="${e.titulo}"
                         data-autor="${e.autor}"
                         data-isbn="${e.isbn}"
                         data-tipo="${e.tipoDocumento.nombreTipo}">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <span class="badge ${e.tipoDocumento.nombreTipo == 'Libro' ? 'badge-libro' :
                                                        e.tipoDocumento.nombreTipo == 'Tesis' ? 'badge-tesis' :
                                                        e.tipoDocumento.nombreTipo == 'Revista' ? 'badge-revista' :
                                                        e.tipoDocumento.nombreTipo == 'CD' ? 'badge-cd' :
                                                        e.tipoDocumento.nombreTipo == 'DVD' ? 'badge-dvd' :
                                                        e.tipoDocumento.nombreTipo == 'Informe' ? 'badge-informe' : 'badge-manual'}">
                                            ${e.tipoDocumento.nombreTipo}
                                    </span>
                                    <c:choose>
                                        <c:when test="${e.cantidadDisponible > 0}">
                                            <span class="badge fs-6 disponible">
                                                <i class="fas fa-check-circle"></i>
                                                Disponible (${e.cantidadDisponible})
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge fs-6 no-disponible">
                                                <i class="fas fa-times-circle"></i>
                                                No disponible
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <h5 class="card-title text-primary fw-bold">${e.titulo}</h5>
                                <p class="card-text">
                                    <strong>Autor:</strong> ${e.autor}<br>
                                    <strong>Editorial:</strong> ${e.editorial}<br>
                                    <strong>ISBN:</strong> ${e.isbn}<br>
                                    <strong>Año:</strong> ${e.anioPublicacion}<br>
                                    <strong>Ubicación:</strong> ${e.ubicacion.edificio} - ${e.ubicacion.piso} - ${e.ubicacion.seccion} - ${e.ubicacion.estante}<br>
                                    <strong>Categoría:</strong> ${e.categoria.nombreCategoria}
                                </p>

                                <div class="mt-auto">
                                    <small class="text-muted">
                                        <i class="fas fa-calendar-alt"></i> Ingresado: ${fechaFormateada}
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Sin resultados -->
            <c:if test="${empty ejemplares}">
                <div class="text-center py-5">
                    <i class="fas fa-book-open fa-5x text-muted mb-4"></i>
                    <h3 class="text-muted">No se encontraron ejemplares</h3>
                    <p class="text-muted">Intenta con otra búsqueda</p>
                </div>
            </c:if>
        </div>

        <div class="card-footer text-center bg-light">
            <a href="index.jsp" class="btn btn-outline-secondary">
                <i class="fas fa-home"></i> Volver al inicio
            </a>
        </div>
    </div>
</div>

<script>
    // Búsqueda en tiempo real
    document.getElementById('buscador').addEventListener('keyup', function() {
        let filtro = this.value.toLowerCase();
        document.querySelectorAll('.ejemplar-card').forEach(card => {
            let texto = card.getAttribute('data-titulo').toLowerCase() +
                card.getAttribute('data-autor').toLowerCase() +
                card.getAttribute('data-isbn').toLowerCase() +
                card.getAttribute('data-tipo').toLowerCase();
            card.style.display = texto.includes(filtro) ? '' : 'none';
        });
    });

    // Filtro por tipo
    function filtrarTipo(tipo) {
        document.querySelectorAll('.ejemplar-card').forEach(card => {
            let tipoCard = card.getAttribute('data-tipo');
            card.style.display = (tipo === '' || tipoCard === tipo) ? '' : 'none';
        });
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>