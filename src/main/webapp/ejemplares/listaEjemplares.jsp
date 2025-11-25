<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Ejemplares - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        .table-responsive { border-radius: 12px; overflow: hidden; }
        th { background-color: #0d6efd !important; color: white !important; }
        .btn-sm i { font-size: 0.9rem; }
        .search-box { max-width: 400px; }
        .badge { font-size: 0.85rem; }
        .card { box-shadow: 0 10px 30px rgba(0,0,0,0.15); }
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

            <!-- Búsqueda -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="input-group search-box">
                        <span class="input-group-text"><i class="bi bi-search"></i></span>
                        <input type="text" id="buscar" class="form-control form-control-lg"
                               placeholder="Buscar por título, autor, ISBN o tipo..."
                               onkeyup="filtrarTabla()">
                    </div>
                </div>
                <div class="col-md-6 text-end">
                    <span class="badge bg-primary fs-6">Total: <strong>${totalEjemplares}</strong> ejemplares</span>
                </div>
            </div>

            <!-- Tabla -->
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle" id="tablaEjemplares">
                    <thead class="text-center">
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>Tipo</th>
                        <th>ISBN</th>
                        <th>Ubicación</th>
                        <th>Cantidad</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="e" items="${listaEjemplares}" varStatus="i">
                        <tr>
                            <td class="text-center"><strong>${i.index + 1}</strong></td>
                            <td>
                                <strong>${e.titulo}</strong>
                                <br><small class="text-muted">Año: ${e.anioPublicacion} | Idioma: ${e.idioma}</small>
                            </td>
                            <td>${e.autor}</td>
                            <td>
                                <span class="badge bg-info text-dark">${e.tipoDocumento}</span>
                            </td>
                            <td>${e.isbn}</td>
                            <td>
                                <small>
                                    Edif. ${e.ubicacion.edificio} - P${e.ubicacion.piso}
                                    - Sec ${e.ubicacion.seccion} - Est ${e.ubicacion.estante}
                                </small>
                            </td>
                            <td class="text-center">
                                    <span class="badge ${e.cantidadTotal > 0 ? 'bg-success' : 'bg-danger'}">
                                            ${e.cantidadTotal}
                                    </span>
                            </td>
                            <td class="text-center">
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

                <!-- Si no hay resultados -->
                <c:if test="${empty listaEjemplares}">
                    <div class="text-center py-5">
                        <i class="bi bi-inbox display-1 text-muted"></i>
                        <h4 class="text-muted mt-3">No hay ejemplares registrados</h4>
                        <a href="ejemplares.do?op=nuevo" class="btn btn-primary mt-3">Registrar el primero</a>
                    </div>
                </c:if>
            </div>

            <!-- Paginación (si tienes implementada) -->
            <c:if test="${totalPaginas > 1}">
                <nav class="mt-4">
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

<!-- Modal de confirmación para eliminar -->
<div class="modal fade" id="modalEliminar" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title"><i class="bi bi-exclamation-triangle"></i> Confirmar eliminación</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p>¿Estás seguro de eliminar el ejemplar:</p>
                <h5 id="tituloEliminar"></h5>
                <p class="text-danger"><strong>Esta acción no se puede deshacer.</strong></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">Sí, eliminar</a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>

    function filtrarTabla() {
        let input = document.getElementById("buscar");
        let filter = input.value.toLowerCase();
        let rows = document.querySelectorAll("#tablaEjemplares tbody tr");

        rows.forEach(row => {
            let text = row.textContent.toLowerCase();
            row.style.display = text.includes(filter) ? "" : "none";
        });
    }

    function confirmarEliminar(id, titulo) {
        document.getElementById("tituloEliminar").textContent = titulo;
        document.getElementById("btnConfirmarEliminar").href =
            "ejemplares.do?op=eliminar&id=" + id;
        new bootstrap.Modal(document.getElementById('modalEliminar')).show();
    }
</script>
</body>
</html>