<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Préstamos Activos - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
            border: none;
            border-radius: 20px;
            overflow: hidden;
        }
        .card-header {
            background: linear-gradient(45deg, #007bff, #6610f2);
            padding: 1.5rem;
        }
        .badge-atrasado {
            background: linear-gradient(45deg, #dc3545, #c82333);
            font-size: 0.9em;
        }
        .mora-roja {
            color: #dc3545;
            font-weight: bold;
            font-size: 1.1em;
        }
        .dias-restantes {
            font-weight: bold;
        }
        th {
            background: #495057;
            color: white;
            font-weight: 600;
        }
        .table tr:hover {
            background-color: rgba(0,123,255,0.1);
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
                                <li><a class="dropdown-item text-danger" href="logout.do">
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
    <!-- Mensajes de éxito/fracaso -->
    <c:if test="${not empty sessionScope.exito}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle"></i> ${sessionScope.exito}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="exito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.fracaso}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-triangle"></i> ${sessionScope.fracaso}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="fracaso" scope="session"/>
    </c:if>

    <div class="card">
        <div class="card-header text-white text-center">
            <h2 class="mb-0">
                <i class="fas fa-clipboard-list"></i> Préstamos Activos
            </h2>
            <p class="mb-0 mt-2 fs-5">
                Total: <strong class="text-warning">${totalPrestamos}</strong> |
                Atrasados: <strong class="text-danger">${totalAtrasados}</strong>
            </p>
        </div>
        <div class="card-body p-4">
            <div class="text-end mb-4">
                <a href="prestamos.do?op=nuevo" class="btn btn-success btn-lg shadow">
                    <i class="fas fa-plus-circle"></i> Nuevo Préstamo
                </a>
            </div>

            <c:choose>
                <c:when test="${empty listaPrestamos}">
                    <div class="text-center py-5">
                        <i class="fas fa-box-open fa-5x text-muted mb-4"></i>
                        <h3 class="text-white">No hay préstamos activos</h3>
                        <p class="text-white-50">¡Es momento de registrar el primero!</p>
                        <a href="prestamos.do?op=nuevo" class="btn btn-primary btn-lg mt-3">
                            <i class="fas fa-plus"></i> Registrar Préstamo
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

                        <!-- definir 'hoy' una sola vez y normalizar a yyyy-MM-dd (midnight) -->
                        <jsp:useBean id="hoy" class="java.util.Date" />
                        <fmt:formatDate value="${hoy}" pattern="yyyy-MM-dd" var="hoyStr" />
                        <fmt:parseDate value="${hoyStr}" pattern="yyyy-MM-dd" var="hoyDate" />

                        <table class="table table-hover align-middle mb-0">
                            <thead> <tr>
                                <th>#</th>
                                <th>Usuario</th>
                                <th>Ejemplar</th>
                                <th>Tipo</th>
                                <th>Fecha Préstamo</th>
                                <th>Vence</th>
                                <th>Días Restantes</th>
                                <th>Estado</th>
                                <th>Mora</th>
                                <th>Acción</th>
                            </tr> </thead>
                            <tbody>
                            <c:forEach var="p" items="${listaPrestamos}" varStatus="i">

                                <!-- si fechaVencimiento es null evitamos errores -->
                                <c:if test="${not empty p.fechaVencimiento}">
                                    <!-- normalizamos fechaVencimiento a yyyy-MM-dd (midnight) -->
                                    <fmt:formatDate value="${p.fechaVencimiento}" pattern="yyyy-MM-dd" var="venceStr"/>
                                    <fmt:parseDate value="${venceStr}" pattern="yyyy-MM-dd" var="venceDate"/>

                                    <!-- calcular diferencia en días (puede ser negativo) -->
                                    <c:set var="diasRestantes" value="${(venceDate.time - hoyDate.time) / 86400000}" />
                                </c:if>
                                <c:if test="${empty p.fechaVencimiento}">
                                    <c:set var="diasRestantes" value="0" />
                                </c:if>

                                <tr class="${diasRestantes < 0 ? 'table-danger' : ''}">
                                    <td>${i.index + 1}</td>
                                    <td><strong>${p.usuario.nombreCompleto}</strong></td>
                                    <td><strong>${p.ejemplar.titulo}</strong></td>
                                    <td>
                                        <span class="badge bg-info text-dark">${p.ejemplar.tipoDocumento.nombreTipo}</span>
                                    </td>
                                    <td><fmt:formatDate value="${p.fechaPrestamo}" pattern="dd/MM/yyyy"/></td>
                                    <td><fmt:formatDate value="${p.fechaVencimiento}" pattern="dd/MM/yyyy"/></td>

                                    <td class="dias-restantes ${ diasRestantes < 0 ? 'text-danger' : (diasRestantes <= 2 ? 'text-warning' : 'text-success') }">
                                        <c:choose>
                                            <c:when test="${diasRestantes < 0}">
                                                ${(-diasRestantes).intValue()} días atrasado
                                            </c:when>
                                            <c:when test="${diasRestantes == 0}">
                                                Hoy vence
                                            </c:when>
                                            <c:when test="${diasRestantes == 1}">
                                                1 día restante
                                            </c:when>
                                            <c:otherwise>
                                                ${diasRestantes.intValue()} días
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${diasRestantes < 0}">
                                                <span class="badge badge-atrasado">ATRASADO</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success">Al día</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <c:if test="${p.moraCalculada > 0}">
                                            <span class="mora-roja">$${String.format('%.2f', p.moraCalculada)}</span>
                                        </c:if>
                                        <c:if test="${p.moraCalculada == 0}">
                                            <span class="text-success">Sin mora</span>
                                        </c:if>
                                    </td>

                                    <td>
                                        <button type="button"
                                                class="btn btn-success btn-sm"
                                                data-id="${p.idPrestamo}"
                                                data-usuario="${p.usuario.nombreCompleto}"
                                                data-titulo="${p.ejemplar.titulo}"
                                                data-mora="<fmt:formatNumber value='${p.moraCalculada}' pattern='0.00'/>"
                                                onclick="devolverPrestamo(this)">
                                            Devolver
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Modal de confiramcion de devolucion -->
    <div class="modal fade" id="modalDevolver" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content shadow-lg border-0">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">
                        Confirmar Devolución
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body text-center py-5">
                    <i class="fas fa-question-circle fa-5x text-warning mb-4"></i>
                    <h4>¿Registrar la devolución de este ejemplar?</h4>
                    <div class="mt-4">
                        <p class="mb-2"><strong>Usuario:</strong> <span id="modalUsuario" class="text-primary"></span></p>
                        <p class="mb-3"><strong>Ejemplar:</strong> <span id="modalTitulo" class="text-primary"></span></p>
                        <p class="fs-5" id="modalMora"></p>
                    </div>
                </div>
                <div class="modal-footer justify-content-center gap-3">
                    <button type="button" class="btn btn-secondary btn-lg px-5" data-bs-dismiss="modal">
                        Cancelar
                    </button>
                    <a href="#" id="btnConfirmarDevolucion" class="btn btn-success btn-lg px-5">
                        Sí, devolver
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function devolverPrestamo(button) {
        const id = button.getAttribute('data-id');
        const usuario = button.getAttribute('data-usuario');
        const titulo = button.getAttribute('data-titulo');
        const moraStr = button.getAttribute('data-mora');
        const mora = parseFloat(moraStr) || 0;

        document.getElementById('modalUsuario').textContent = usuario;
        document.getElementById('modalTitulo').textContent = titulo;

        let moraText;
        if (mora > 0) {
            const moraFormateada = mora.toFixed(2);
            moraText = 'Se generará una mora de: <strong class="text-danger">$' + moraFormateada + '</strong>';
        } else {
            moraText = '<span class="text-success fw-bold">Sin mora generada</span>';
        }

        console.log('moraText a insertar:', moraText);

        document.getElementById('modalMora').innerHTML = moraText;

        document.getElementById('btnConfirmarDevolucion').href = 'prestamos.do?op=devolver&id=' + id;

        new bootstrap.Modal(document.getElementById('modalDevolver')).show();
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>