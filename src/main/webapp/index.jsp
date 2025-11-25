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
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .hero-header {
            color: white;
            text-shadow: 0 2px 10px rgba(0,0,0,0.3);
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
    </div>

    <div class="row g-4">
        <!-- Gestión de Ejemplares -->
        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0">
                <div class="card-body text-center p-5">
                    <i class="fas fa-book fa-4x text-primary mb-4"></i>
                    <h3 class="card-title">Ejemplares</h3>
                    <p class="card-text">Registrar, editar y consultar libros, revistas, tesis, CDs, DVDs y más.</p>
                    <div class="mt-4">
                        <a href="ejemplares.do?op=nuevo" class="btn btn-primary me-2">
                            <i class="fas fa-plus"></i> Nuevo Ejemplar
                        </a>
                        <a href="ejemplares.do?op=listar" class="btn btn-outline-primary">
                            <i class="fas fa-list"></i> Ver Todos
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Próximos módulos (puedes activarlos después) -->
        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0 opacity-75">
                <div class="card-body text-center p-5">
                    <i class="fas fa-users fa-4x text-success mb-4"></i>
                    <h3 class="card-title">Usuarios</h3>
                    <p class="card-text">Gestión de estudiantes, docentes y administrativos.</p>
                    <button class="btn btn-secondary" disabled>Próximamente</button>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card h-100 bg-white text-dark card-hover border-0 opacity-75">
                <div class="card-body text-center p-5">
                    <i class="fas fa-exchange-alt fa-4x text-warning mb-4"></i>
                    <h3 class="card-title">Préstamos</h3>
                    <p class="card-text">Control de préstamos, devoluciones y moras.</p>
                    <button class="btn btn-secondary" disabled>Próximamente</button>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center mt-5">
        <small class="text-white-50">
            © 2025 Biblioteca Universitaria Don Bosco | Sistema desarrollado con Java + JSP + MySQL
        </small>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>