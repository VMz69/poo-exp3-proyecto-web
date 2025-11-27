<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Biblioteca UDB - Sistema de Gestión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
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
        .btn-active {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            color: white;
        }
        .btn-active:hover {
            background: linear-gradient(45deg, #218838, #1ba085);
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<div class="container py-5">
    <!-- Mensajes de éxito o error -->
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

    <!-- Título principal -->
    <div class="text-center mb-5">
        <h1 class="display-4 hero-header fw-bold">
            <i class="fas fa-book-open"></i> Biblioteca UDB
        </h1>
        <p class="lead text-white">Sistema de Gestión de Recursos Bibliográficos</p>
        <p class="text-white-50">Bienvenido, <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong></p>
        <a href="cambiarContrasena.do" class="btn btn-warning btn-lg shadow-lg px-5 py-3">
            Cambiar Mi Contraseña
        </a>
    </div>

    <div class="row g-5">

        <!-- Modulo Ejemplares -->
        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0">
                <div class="card-body text-center p-5">
                    <i class="fas fa-book fa-5x text-primary mb-4"></i>
                    <h3 class="card-title fw-bold">Ejemplares</h3>
                    <p class="card-text">Registrar, editar y consultar libros, tesis, revistas, CDs, DVDs y más.</p>
                    <div class="mt-4">
                        <a href="ejemplares.do?op=nuevo" class="btn btn-primary btn-lg me-2">
                            <i class="fas fa-plus"></i> Nuevo
                        </a>
                        <a href="ejemplares.do?op=listar" class="btn btn-outline-primary btn-lg">
                            <i class="fas fa-list"></i> Ver Todos
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modulo Prestamos -->
        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0">
                <div class="card-body text-center p-5">
                    <i class="fas fa-exchange-alt fa-5x text-success mb-4"></i>
                    <h3 class="card-title fw-bold text-success">Préstamos</h3>
                    <p class="card-text">Control total de préstamos, devoluciones, moras y configuración dinámica.</p>
                    <div class="mt-4">
                        <a href="prestamos.do?op=nuevo" class="btn btn-success btn-active btn-lg me2">
                            <i class="fas fa-plus-circle"></i> Nuevo Préstamo
                        </a>
                        <a href="prestamos.do?op=listar" class="btn btn-outline-success btn-lg">
                            <i class="fas fa-clipboard-list"></i> Ver Préstamos
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modulo Usuarios -->
        <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-users-cog fa-5x text-info mb-4"></i>
                        <h3 class="card-title fw-bold">Usuarios</h3>
                        <p class="card-text">Crear, editar y eliminar cuentas de estudiantes, profesores y administradores.</p>
                        <a href="usuarios.do" class="btn btn-info btn-lg text-white shadow-lg">
                            Gestionar Usuarios
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Modulo Configuracion - SOLO PARA ADMINISTRADORES -->
        <c:if test="${sessionScope.usuarioLogueado.tipoUsuario.nombreTipo == 'Administrador'}">
            <div class="col-md-4">
                <div class="card h-100 bg-white text-dark card-hover border-0 position-relative">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-cogs fa-5x mb-4 text-purple"></i>
                        <h3 class="card-title fw-bold">Configuración</h3>
                        <p class="card-text">
                            Gestionar parámetros globales del sistema:<br>
                            <small class="text-muted">
                                • Días máximos de préstamo<br>
                                • Límite de ejemplares por usuario<br>
                                • Mora diaria y sanciones
                            </small>
                        </p>
                        <div class="mt-4">
                            <a href="configuracion.do" class="btn btn-lg shadow-lg text-white"
                               style="background: linear-gradient(45deg, #667eea, #764ba2); border: none;">
                                <i class="fas fa-wrench"></i> Abrir Configuración
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>