<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Ejemplares - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background: #f8f9fa; }
        .card { box-shadow: 0 10px 30px rgba(0,0,0,0.15); border: none; border-radius: 15px; overflow: hidden; }
        .card-header { background: linear-gradient(135deg, #0d6efd, #0d5bff); }
        th { background-color: #0d6efd !important; color: white !important; }
        .table-responsive { border-radius: 12px; overflow: hidden; }
        .badge { font-size: 0.85rem; padding: 0.4em 0.8em; }
        .search-box { max-width: 400px; }
        .page-link { border-radius: 8px; }
        .page-item.active .page-link { background-color: #0d6efd; border-color: #0d6efd; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-5 mb-5">
    <div class="card shadow-lg">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h3 class="mb-0"><i class="bi bi-bookshelf"></i> Gestión de Ejemplares</h3>
            <a href="ejemplares.do?op=nuevo" class="btn btn-light btn-lg">
                <i class="bi bi-plus-circle"></i> Nuevo Ejemplar
            </a>
        </div>

        <div class="card-body">
            <!-- Mensaje de éxito -->
            <c:if test="${not empty sessionScope.exito}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle-fill"></i> ${sessionScope.exito}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("exito"); %>
            </c:if>

            <!-- Mensaje de error/fracaso -->
            <c:if test="${not empty sessionScope.fracaso}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle-fill"></i> ${sessionScope.fracaso}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("fracaso"); %>
            </c:if>

            <!-- Búsqueda + Total -->
            <div class="row mb-4 align-items-center">
                <div class="col-md-6">
                    <div class="input-group search-box">
                        <span class="input-group-text bg-white"><i class="bi bi-search"></i></span>
                        <input type="text" id="buscar" class="form-control form-control-lg border-start-0"
                               placeholder="Buscar por título, autor, ISBN..." onkeyup="filtrarTabla()">
                    </div>
                </div>
                <div class="col-md-6 text-md-end mt-3 mt-md-0">
                    <span class="badge bg-primary fs-5 px-4 py-2">
                        Total: <strong>${listaEjemplares.size()}</strong> ejemplares
                    </span>
                </div>
            </div>

            <!-- Tabla -->
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle" id="tablaEjemplares">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>Tipo</th>
                        <th>ISBN</th>
                        <th>Ubicación</th>
                        <th>Disponibles</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="e" items="${listaEjemplares}" varStatus="i">
                        <tr>
                            <td>${i.index + 1}</td>
                            <td>
                                <strong>${e.titulo}</strong><br>
                                <small class="text-muted">Año: ${e.anioPublicacion}</small>
                            </td>
                            <td>${e.autor}</td>
                            <td>
                                <span class="badge bg-info text-dark">${e.tipoDocumento.nombreTipo}</span>
                            </td>
                            <td>${e.isbn}</td>
                            <td>
                                <small>
                                    Ed. ${e.ubicacion.edificio} -
                                    P${e.ubicacion.piso} -
                                    S${e.ubicacion.seccion} -
                                    E${e.ubicacion.estante}
                                </small>
                            </td>
                            <td class="text-center">
                                    <span class="badge ${e.cantidadDisponible > 0 ? 'bg-success' : 'bg-danger'}">
                                        ${e.cantidadDisponible} / ${e.cantidadTotal}
                                    </span>
                            </td>
                            <td>
                                <a href="ejemplares.do?op=obtener&id=${e.idEjemplar}"
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="bi bi-pencil-square"></i>
                                </a>
                                <button onclick="confirmarEliminar(${e.idEjemplar}, '${e.titulo}')"
                                        class="btn btn-danger btn-sm" title="Eliminar">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Paginación (si tienes implementada) -->
            <c:if test="${totalPaginas > 1}">
                <nav aria-label="Paginación de ejemplares" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${paginaActual == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="ejemplares.do?op=listar&pagina=${paginaActual - 1}">Anterior</a>
                        </li>
                        <c:forEach begin="1" end="${totalPaginas}" var="p">
                            <li class="page-item ${p == paginaActual ? 'active' : ''}">
                                <a class="page-link" href="ejemplares.do?op=listar&pagina=${p}">${p}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${paginaActual == totalPaginas ? 'disabled' : ''}">
                            <a class="page-link" href="ejemplares.do?op=listar&pagina=${paginaActual + 1}">Siguiente</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<!-- Modal Confirmar Eliminacion -->
<div class="modal fade" id="modalEliminar" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title"><i class="bi bi-exclamation-triangle"></i> Confirmar eliminación</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>¿Estás completamente seguro de eliminar el siguiente ejemplar?</p>
                <h5 class="text-primary" id="tituloEliminar"></h5>
                <p class="text-danger mt-3"><strong>Esta acción eliminará el ejemplar y sus préstamos asociados y no se podrá deshacer.</strong></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">Sí, eliminar permanentemente</a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Filtro en tiempo real
    function filtrarTabla() {
        let input = document.getElementById("buscar").value.toLowerCase();
        let rows = document.querySelectorAll("#tablaEjemplares tbody tr");

        rows.forEach(row => {
            let text = row.textContent.toLowerCase();
            row.style.display = text.includes(input) ? "" : "none";
        });
    }

    // Modal de confirmación
    function confirmarEliminar(id, titulo) {
        document.getElementById("tituloEliminar").textContent = titulo;
        document.getElementById("btnConfirmarEliminar").href =
            "ejemplares.do?op=eliminar&id=" + id;
        new bootstrap.Modal(document.getElementById('modalEliminar')).show();
    }

    // Auto-ocultar alertas despues de 5 segundos
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(alert => {
            if (alert) new bootstrap.Alert(alert).close();
        });
    }, 5000);
</script>
</body>
</html>