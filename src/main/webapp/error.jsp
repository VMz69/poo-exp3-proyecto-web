<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error - Biblioteca UDB</title>
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
            border-radius: 30px;
            border: none;
            max-width: 600px;
            margin: 150px auto;
        }
        .btn-back {
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: none;
            padding: 15px 60px;
            font-size: 1.4em;
            border-radius: 50px;
        }
        .btn-back:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(102, 126, 234, 0.5);
        }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center">
<div class="card text-center">
    <div class="card-body p-5">
        <i class="fas fa-exclamation-triangle fa-5x text-danger mb-4"></i>
        <h2 class="text-danger">Error del Sistema</h2>
        <p class="lead fs-4 text-muted">
            Lo sentimos, ha ocurrido un error inesperado.
        </p>
        <p class="fs-5 text-muted mb-4">
            Detalles: ${error != null ? error : 'No se pudo completar la operación. Intenta más tarde.'}
        </p>
        <a href="index.jsp" class="btn btn-back text-white shadow-lg">
            Volver al Inicio
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>