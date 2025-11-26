<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(135deg, #667eea, #764ba2);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
            padding: 20px;
        }
        .login-box {
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.3);
            max-width: 400px;
            width: 100%;
        }
        .btn-login {
            background: #0d6efd;
            border: none;
            border-radius: 50px;
            padding: 12px;
            font-weight: 600;
        }
    </style>
</head>
<body>
<div class="login-box text-center">
    <h2 class="mb-4 text-primary">
        <i class="bi bi-book-half"></i> Biblioteca UDB
    </h2>
    <p class="text-muted mb-4">Sistema de Gestión Bibliotecaria</p>

    <!-- Mensaje de error -->
    <c:if test="${param.error eq '1'}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-x-circle"></i> Usuario o contraseña incorrectos
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.logout eq '1'}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> Sesión cerrada correctamente
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="login.do" method="post">
        <div class="mb-3">
            <input type="text" name="usuario" class="form-control form-control-lg"
                   placeholder="Usuario" required autofocus>
        </div>
        <div class="mb-4">
            <input type="password" name="password" class="form-control form-control-lg"
                   placeholder="Contraseña" required>
        </div>
        <button type="submit" class="btn btn-primary btn-login w-100">
            <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión
        </button>
    </form>
</div>
</body>
</html>