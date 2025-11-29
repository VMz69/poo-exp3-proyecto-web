<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios - Biblioteca UDB</title>
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
            overflow: hidden;
            border: none;
        }
        .card-header {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 2.5rem;
        }
        th {
            background: #495057;
            color: white;
            font-weight: 600;
        }
        .btn-nuevo {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            padding: 12px 30px;
            font-size: 1.2em;
            border-radius: 50px;
        }
        .btn-nuevo:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(40,167,69,0.4);
        }
        .badge-mora {
            font-size: 1.1em;
            padding: 0.6em 1em;
        }
        /* NAVBAR FIJO Y SIEMPRE VISIBLE */
        .navbar-custom {
            background: rgba(30, 30, 60, 0.95) !important;
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.15);
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
            z-index: 1050;
        }
        .navbar-custom .nav-link {
            color: white !important;
            font-weight: 500;
            transition: all 0.3s;
        }
        .navbar-custom .nav-link:hover {
            color: #a0d8ff !important;
            transform: translateY(-2px);
        }
        .navbar-custom .dropdown-item {
            color: #333;
        }
        .navbar-custom .dropdown-item:hover {
            background-color: #667eea;
            color: white !important;
        }
        .hero-header {
            color: white;
            text-shadow: 0 2px 10px rgba(0,0,0,0.3);
        }
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-custom sticky-top">
    <div class="container-fluid px-4">
        <a class="navbar-brand text-white fw-bold fs-4" href="index.jsp">
            <i class="fas fa-book-open text-warning me-2"></i>Biblioteca UDB
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                <!-- Consultar Catalogo - PARA TODOS -->
                <li class="nav-item">
                    <a class="nav-link" href="consulta.do">
                        <i class="fas fa-search me-1"></i> Consultar Catálogo
                    </a>
                </li>

                <!-- Ejemplares - SOLO ADMIN -->
                <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-book me-1"></i> Ejemplares
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="ejemplares.do?op=nuevo">
                                <i class="fas fa-plus text-success"></i> Nuevo Ejemplar
                            </a></li>
                            <li><a class="dropdown-item" href="ejemplares.do?op=listar">
                                <i class="fas fa-list"></i> Ver Todos
                            </a></li>
                        </ul>
                    </li>
                </c:if>

                <!-- Prestamos - SOLO ADMIN -->
                <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-exchange-alt me-1"></i> Préstamos
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="prestamos.do?op=nuevo">
                                <i class="fas fa-plus-circle text-success"></i> Nuevo Préstamo
                            </a></li>
                            <li><a class="dropdown-item" href="prestamos.do?op=listar">
                                <i class="fas fa-clipboard-list"></i> Ver Activos
                            </a></li>
                        </ul>
                    </li>
                </c:if>

                <!-- Usuarios y configuracion - SOLO ADMIN -->
                <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
                    <li class="nav-item">
                        <a class="nav-link" href="usuarios.do">
                            <i class="fas fa-users-cog me-1"></i> Usuarios
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="configuracion.do">
                            <i class="fas fa-cogs me-1"></i> Configuración
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Usuario Logueado o Botón de Login -->
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogueado}">
                        <!-- Usuario logueado -->
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="fas fa-user-circle fa-lg me-1"></i>
                                    ${sessionScope.usuarioLogueado.nombreCompleto}
                                <span class="badge bg-light text-dark ms-1">
                                        ${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo}
                                </span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="cambiarContrasena.do">
                                    <i class="fas fa-key"></i> Cambiar Contraseña
                                </a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="login.do?logout=1">
                                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                                </a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <!-- Usuario NO logueado -->
                        <li class="nav-item">
                            <a href="login.do" class="btn btn-login">
                                <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<%--Contenido Principal--%>
<div class="container py-5">
    <div class="card">
        <div class="card-header text-center">
            <h1 class="mb-0">
                Gestión de Usuarios
            </h1>
            <p class="mb-0 mt-3 fs-5 opacity-90">
                Administra estudiantes, profesores y administradores
            </p>
        </div>

        <div class="card-body p-5">

            <!-- Mensajes de éxito/error -->
            <c:if test="${not empty sessionScope.exito}">
                <div class="alert alert-success alert-dismissible fade show">
                        ${sessionScope.exito}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="exito" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.fracaso}">
                <div class="alert alert-danger alert-dismissible fade show">
                        ${sessionScope.fracaso}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="fracaso" scope="session"/>
            </c:if>

            <!-- Boton Nuevo Usuario -->
            <div class="text-end mb-4">
                <a href="usuarios.do?op=nuevo" class="btn btn-nuevo text-white shadow-lg">
                    Nuevo Usuario
                </a>
            </div>

            <!-- Tabla de usuarios -->
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre Completo</th>
                        <th>Usuario</th>
                        <th>Correo</th>
                        <th>Tipo</th>
                        <th>Estado</th>
                        <th>Mora</th>
                        <th>Registrado</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="u" items="${listaUsuarios}" varStatus="i">
                        <tr class="${u.tieneMora ? 'table-danger' : ''}">
                            <td>${i.index + 1}</td>
                            <td><strong>${u.nombreCompleto}</strong></td>
                            <td>${u.usuario}</td>
                            <td>${u.correo}</td>
                            <td>
                                    <span class="badge
                                        ${u.tipoUsuario.nombreTipo == 'Administrador' ? 'bg-danger' :
                                          u.tipoUsuario.nombreTipo == 'Profesor' ? 'bg-success' : 'bg-primary'}">
                                            ${u.tipoUsuario.nombreTipo}
                                    </span>
                            </td>
                            <td>
                                    <span class="badge bg-${u.activo ? 'success' : 'secondary'}">
                                            ${u.activo ? 'Activo' : 'Inactivo'}
                                    </span>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.tieneMora}">
                                        <div class="d-flex flex-column align-items-center gap-2">
                                            <span class="badge bg-danger badge-mora">$ ${u.montoMora}</span>
                                            <!-- Boton pagar mora -->
                                            <button type="button" class="btn btn-success btn-sm shadow-sm"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#modalPagarMora"
                                                    onclick="prepararPago(${u.idUsuario}, '${u.nombreCompleto}', ${u.montoMora})">
                                                Pagar Ahora
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success fw-bold">Sin mora</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <fmt:formatDate value="${u.fechaRegistro}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td>
                                <a href="usuarios.do?op=editar&id=${u.idUsuario}"
                                   class="btn btn-warning btn-sm" title="Editar">
                                    Editar
                                </a>

                                <!-- BOTÓN ELIMINAR CON MODAL -->
                                <button type="button" class="btn btn-danger btn-sm"
                                        data-bs-toggle="modal"
                                        data-bs-target="#modalEliminar"
                                        onclick="prepararEliminacion(${u.idUsuario}, '${u.nombreCompleto}')">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- MODAL ELIMINAR USUARIO -->
<div class="modal fade" id="modalEliminar" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title">Confirmar Eliminación</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-center py-5">
                <i class="fas fa-exclamation-triangle fa-5x text-danger mb-4"></i>
                <h3>¿Estás completamente seguro?</h3>
                <p class="fs-5 text-muted mb-2">Vas a eliminar permanentemente al usuario:</p>
                <h4 class="text-dark fw-bold" id="nombreUsuarioModal"></h4>
                <p class="text-danger fw-bold mt-4">Esta acción <u>NO</u> se puede deshacer.</p>
            </div>
            <div class="modal-footer justify-content-center border-0 pt-0">
                <button type="button" class="btn btn-secondary btn-lg px-5" data-bs-dismiss="modal">Cancelar</button>
                <a id="btnConfirmarEliminar" href="#" class="btn btn-danger btn-lg px-5">Sí, Eliminar</a>
            </div>
        </div>
    </div>
</div>

<!-- MODAL PAGAR MORA -->
<div class="modal fade" id="modalPagarMora" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title">Pagar Mora</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-center py-5">
                <i class="fas fa-money-bill-wave fa-5x text-success mb-4"></i>
                <h4>Confirmar pago de mora</h4>
                <p class="fs-5">Usuario: <strong id="nombrePagoModal"></strong></p>
                <h3 class="text-success fw-bold">Monto a pagar: $<span id="montoPagoModal"></span></h3>
                <p class="text-muted mt-3">Al confirmar, se eliminará la mora del usuario.</p>
            </div>
            <div class="modal-footer justify-content-center border-0">
                <button type="button" class="btn btn-secondary btn-lg px-4" data-bs-dismiss="modal">Cancelar</button>
                <a id="btnConfirmarPago" href="#" class="btn btn-success btn-lg px-5">Confirmar Pago</a>
            </div>
        </div>
    </div>
</div>

<!-- SCRIPTS -->
<script>
    function prepararEliminacion(id, nombre) {
        nombre = nombre.replace(/'/g, "\\'");
        document.getElementById("nombreUsuarioModal").textContent = nombre;
        document.getElementById("btnConfirmarEliminar").href = "usuarios.do?op=eliminar&id=" + id;
    }

    function prepararPago(id, nombre, monto) {
        document.getElementById("nombrePagoModal").textContent = nombre;
        document.getElementById("montoPagoModal").textContent = parseFloat(monto).toFixed(2);
        document.getElementById("btnConfirmarPago").href = "usuarios.do?op=pagarMora&id=" + id;
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>