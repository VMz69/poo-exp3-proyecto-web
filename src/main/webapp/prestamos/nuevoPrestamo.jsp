<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nuevo Préstamo - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        .badge-libro { background: #0d6efd; }
        .badge-tesis { background: #198754; }
        .badge-revista { background: #ffc107; color: black; }
        .badge-cd { background: #fd7e14; }
        .badge-dvd { background: #dc3545; }
        .badge-informe { background: #6f42c1; }
        .badge-manual { background: #20c997; }
        .card-ejemplar:hover { border-color: #0d6efd; transform: translateY(-2px); transition: all 0.2s; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="row">
        <div class="col-md-10 mx-auto">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0"><i class="fas fa-plus-circle"></i> Registrar Nuevo Préstamo</h3>
                </div>
                <div class="card-body">

                    <!-- Mensajes de Error -->
                    <c:if test="${not empty listaErrores}">
                        <div class="alert alert-danger">
                            <strong>Errores:</strong>
                            <ul class="mb-0">
                                <c:forEach var="error" items="${listaErrores}">
                                    <li>${error}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>

                    <form action="prestamos.do" method="post">
                        <input type="hidden" name="op" value="insertar">

                        <!-- Usuario -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Usuario</label>
                            <select name="id_usuario" class="form-select" required>
                                <option value="">-- Seleccionar usuario --</option>
                                <c:forEach var="u" items="${usuarios}">
                                    <option value="${u.idUsuario}" ${u.idUsuario == param.id_usuario ? 'selected' : ''}>
                                            ${u.nombreCompleto} (${u.usuario}) (${u.tipoUsuario.nombreTipo})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Ejemplar con busqueda -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Buscar Ejemplar</label>
                            <input type="text" id="buscador" class="form-control" placeholder="Escribe título, autor o ISBN...">
                        </div>

                        <div class="row" id="listaEjemplares">
                            <c:forEach var="e" items="${ejemplaresDisponibles}">
                                <div class="col-md-6 mb-3 ejemplar-card" data-titulo="${e.titulo}" data-autor="${e.autor}" data-isbn="${e.isbn}">
                                    <div class="card card-ejemplar h-100 border">
                                        <div class="card-body d-flex flex-column">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <span class="badge ${
                                                    e.tipoDocumento.nombreTipo == 'Libro' ? 'badge-libro' :
                                                    e.tipoDocumento.nombreTipo == 'Tesis' ? 'badge-tesis' :
                                                    e.tipoDocumento.nombreTipo == 'Revista' ? 'badge-revista' :
                                                    e.tipoDocumento.nombreTipo == 'CD' ? 'badge-cd' :
                                                    e.tipoDocumento.nombreTipo == 'DVD' ? 'badge-dvd' :
                                                    e.tipoDocumento.nombreTipo == 'Informe' ? 'badge-informe' :
                                                    'badge-manual'
                                                }">${e.tipoDocumento.nombreTipo}</span>
                                                <span class="text-success fw-bold">Disponibles: ${e.cantidadDisponible}</span>
                                            </div>
                                            <h6 class="card-title">${e.titulo}</h6>
                                            <p class="card-text text-muted small mb-1">${e.autor}</p>
                                            <p class="card-text text-muted small">${e.informacionEspecifica}</p>
                                            <div class="mt-auto">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="id_ejemplar" value="${e.idEjemplar}"
                                                           id="ejemplar${e.idEjemplar}" ${e.idEjemplar == param.id_ejemplar ? 'checked' : ''}>
                                                    <label class="form-check-label text-primary fw-bold" for="ejemplar${e.idEjemplar}">
                                                        Seleccionar
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Dias de prestamo -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Días de préstamo</label>
                            <input type="number" name="dias" class="form-control" min="1"
                                   max="${config.diasPrestamoProfesor}"
                                   value="${param.dias != null ? param.dias : 1}"
                                   required>

                            <div class="form-text">
                                <c:choose>
                                    <c:when test="${config != null}">
                                        <strong>Máximo permitido:</strong>
                                        <span class="text-success">${config.diasPrestamoProfesor} días (profesores)</span> |
                                        <span class="text-info">${config.diasPrestamoAlumno} días (alumnos)</span>
                                        <br>
                                        <strong>Mora diaria:</strong>
                                        <span class="text-danger">$${config.moraDiaria}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Cargando configuración...</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="d-grid d-md-flex justify-content-md-end gap-2">
                            <a href="prestamos.do?op=listar" class="btn btn-secondary">
                                Cancelar
                            </a>
                            <button type="submit" class="btn btn-success btn-lg">
                                Registrar Préstamo
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Busqueda
    document.getElementById('buscador').addEventListener('keyup', function() {
        let filtro = this.value.toLowerCase();
        document.querySelectorAll('.ejemplar-card').forEach(card => {
            let texto = card.getAttribute('data-titulo').toLowerCase() +
                card.getAttribute('data-autor').toLowerCase() +
                card.getAttribute('data-isbn').toLowerCase();
            card.style.display = texto.includes(filtro) ? '' : 'none';
        });
    });
</script>
</body>
</html>