<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Biblioteca UDB - Sistema de Gestión</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
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
        .card-hover {
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .card-hover:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0,0,0,0.3);
        }
        .hero-header {
            color: white;
            text-shadow: 0 2px 10px rgba(0,0,0,0.3);
        }
        .btn-indigo {
            background: linear-gradient(45deg, #5a67d8, #4c51bf);
            border: none;
        }
        .btn-indigo:hover {
            background: linear-gradient(45deg, #4c51bf, #4338ca);
            transform: translateY(-3px);
        }
        .btn-login {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            color: white;
            font-weight: 500;
            padding: 8px 25px;
            border-radius: 25px;
            transition: all 0.3s;
        }
        .btn-login:hover {
            background: linear-gradient(45deg, #20c997, #17a2b8);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.4);
            color: white;
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

                <!-- Prestamos - TODOS LOS USUARIOS (parcial) -->
                <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'
                    || sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Profesor'
                    || sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Alumno'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-exchange-alt me-1"></i> Préstamos
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="prestamos.do?op=nuevo">
                                <i class="fas fa-plus-circle text-success"></i> Nuevo Préstamo
                            </a></li>
<%--                                        VER Y ADMINISTRAR PRESTAMOS SOLO PARA ADMINISTRADORES      --%>
                                <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
                                    <li><a class="dropdown-item" href="prestamos.do?op=listar">
                                        <i class="fas fa-clipboard-list"></i> Ver Activos
                                    </a></li>
                                </c:if>

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

<!-- Contenido Principal -->
<div class="container py-5 mt-5">
    <!-- Mensajes -->
    <c:if test="${not empty sessionScope.exito}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.exito}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="exito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.fracaso}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle"></i> ${sessionScope.fracaso}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="fracaso" scope="session"/>
    </c:if>

    <!-- Titulo -->
    <div class="text-center mb-5">
        <h1 class="display-4 hero-header fw-bold">
            <i class="fas fa-graduation-cap"></i> Bienvenido
        </h1>
        <p class="lead text-white">Sistema de Gestión Bibliográfica UDB</p>
    </div>

    <!-- Cards -->
    <div class="row g-5">
        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0">
                <div class="card-body text-center p-5">
                    <i class="fas fa-search fa-5x mb-4" style="color: #5a67d8;"></i>
                    <h3 class="card-title fw-bold">Consultar Catálogo</h3>
                    <p class="card-text">Busca libros, tesis, revistas y más</p>
                    <a href="consulta.do" class="btn btn-indigo text-white btn-lg px-5 shadow">
                        <i class="fas fa-book-reader"></i> Explorar
                    </a>
                </div>
            </div>
        </div>

        <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-book fa-5x text-primary mb-4"></i>
                        <h3 class="card-title fw-bold">Ejemplares</h3>
                        <p class="card-text">Gestión completa del catálogo</p>
                        <a href="ejemplares.do?op=listar" class="btn btn-primary btn-lg">
                            Administrar
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-exchange-alt fa-5x text-success mb-4"></i>
                        <h3 class="card-title fw-bold">Préstamos</h3>
                        <p class="card-text">Control de préstamos y devoluciones</p>
                        <a href="prestamos.do?op=listar" class="btn btn-success btn-lg">
                            Gestionar
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-users-cog fa-5x text-info mb-4"></i>
                        <h3 class="card-title fw-bold">Usuarios</h3>
                        <p class="card-text">Administración de usuarios del sistema</p>
                        <a href="usuarios.do" class="btn btn-info text-white btn-lg">
                            Gestionar
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-cogs fa-5x text-warning mb-4"></i>
                        <h3 class="card-title fw-bold">Configuración</h3>
                        <p class="card-text">Parámetros y ajustes del sistema</p>
                        <a href="configuracion.do" class="btn btn-warning text-white btn-lg">
                            Configurar
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>