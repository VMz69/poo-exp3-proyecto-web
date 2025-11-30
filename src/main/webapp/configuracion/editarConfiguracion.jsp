<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración del Sistema - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            box-shadow: 0 20px 50px rgba(0,0,0,0.4);
            border-radius: 20px;
            overflow: hidden;
        }
        .card-header {
            background: linear-gradient(45deg, #1e3c72, #2a5298);
        }
        .btn-save {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            padding: 12px 40px;
            font-size: 1.2em;
            border-radius: 50px;
        }
        .btn-save:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(40,167,69,0.4);
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
<%--Contenido principal--%>
<div class="container py-5">
    <!-- Mensajes -->
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

    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card border-0">
                <div class="card-header text-white text-center py-4">
                    <h1 class="mb-0">
                        <i class="fas fa-cog"></i> Configuración del Sistema
                    </h1>
                    <p class="mb-0 mt-2">Administra los parámetros globales de la biblioteca</p>
                </div>
                <div class="card-body p-5">
                    <form action="configuracion.do?op=guardar" method="post">
                        <div class="row g-4">
                            <!-- Alumnos -->
                            <div class="col-md-6">
                                <div class="card h-100 border-primary">
                                    <div class="card-header bg-primary text-white text-center">
                                        <i class="fas fa-user-graduate fa-2x"></i><br>
                                        <strong>Alumnos</strong>
                                    </div>
                                    <div class="card-body">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">
                                                <i class="fas fa-book"></i> Máximo de préstamos
                                            </label>
                                            <input type="number" name="max_alumno" class="form-control form-control-lg text-center"
                                                   value="${config.maxPrestamosAlumno}" min="1" max="10" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">
                                                <i class="fas fa-calendar"></i> Días de préstamo
                                            </label>
                                            <input type="number" name="dias_alumno" class="form-control form-control-lg text-center"
                                                   value="${config.diasPrestamoAlumno}" min="1" max="30" required>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Profesores -->
                            <div class="col-md-6">
                                <div class="card h-100 border-success">
                                    <div class="card-header bg-success text-white text-center">
                                        <i class="fas fa-chalkboard-teacher fa-2x"></i><br>
                                        <strong>Profesores</strong>
                                    </div>
                                    <div class="card-body">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">
                                                Máximo de préstamos
                                            </label>
                                            <input type="number" name="max_profesor" class="form-control form-control-lg text-center"
                                                   value="${config.maxPrestamosProfesor}" min="1" max="20" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">
                                                Días de préstamo
                                            </label>
                                            <input type="number" name="dias_profesor" class="form-control form-control-lg text-center"
                                                   value="${config.diasPrestamoProfesor}" min="1" max="60" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Mora Diaria -->
                        <div class="text-center mt-5 p-4 bg-light rounded-4">
                            <h3><i class="fas fa-dollar-sign text-danger"></i> Mora Diaria</h3>
                            <div class="col-md-6 mx-auto">
                                <label class="form-label fw-bold fs-4">Monto por día atrasado</label>
                                <div class="input-group input-group-lg">
                                    <span class="input-group-text bg-danger text-white">$</span>
                                    <input type="number" step="0.01" name="mora_diaria" class="form-control text-center fs-3 fw-bold"
                                           value="${config.moraDiaria}" min="0.50" max="10.00" required>
                                </div>
                                <small class="text-muted">Este valor se aplica automáticamente en todos los préstamos atrasados</small>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="text-center mt-5">
                            <button type="submit" class="btn btn-save text-white shadow-lg">
                                <i class="fas fa-save"></i> GUARDAR CONFIGURACIÓN
                            </button>
<%--                            <a href="index.jsp" class="btn btn-secondary btn-lg ms-3">--%>
<%--                                <i class="fas fa-arrow-left"></i> Volver--%>
<%--                            </a>--%>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>