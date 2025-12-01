<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Usuario - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            border-radius: 25px;
            overflow: hidden;
            border: none;
        }
        .card-header {
            background: linear-gradient(45deg, #ff6b6b, #ee5a52);
            color: white;
            padding: 2.5rem;
            text-align: center;
        }
        .btn-update {
            background: linear-gradient(45deg, #f39c12, #e67e22);
            border: none;
            padding: 14px 60px;
            font-size: 1.4em;
            border-radius: 50px;
            transition: all 0.4s ease;
        }
        .btn-update:hover {
            transform: translateY(-6px) scale(1.05);
            box-shadow: 0 20px 40px rgba(243, 156, 18, 0.5);
        }
        .form-control:focus, .form-select:focus {
            border-color: #e67e22;
            box-shadow: 0 0 0 0.3rem rgba(230, 126, 34, 0.25);
        }
        .form-check-input:checked {
            background-color: #e67e22;
            border-color: #e67e22;
        }
        .badge-mora {
            font-size: 1.1em;
            padding: 0.8em 1.2em;
        }
        .user-avatar {
            width: 120px;
            height: 120px;
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: 6px solid white;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-9">
            <div class="card">
                <div class="card-header">
                    <div class="d-flex flex-column align-items-center">
                        <div class="user-avatar rounded-circle d-flex align-items-center justify-content-center mb-3">
                            <i class="fas fa-user-edit fa-4x text-white"></i>
                        </div>
                        <h1 class="mb-0">
                            Editar Usuario
                        </h1>
                        <p class="mb-0 mt-3 fs-4 opacity-90">
                            ID: ${usuario.idUsuario} | Registrado el ${usuario.fechaRegistro}
                        </p>
                    </div>
                </div>

                <div class="card-body p-5">

                    <!-- Estado actual del usuario -->
                    <div class="row mb-4">
                        <div class="col-md-6">
                            <div class="alert ${usuario.activo ? 'alert-success' : 'alert-danger'} text-center py-3">
                                <strong>Estado:</strong>
                                ${usuario.activo ? 'ACTIVO' : 'INACTIVO'}
                            </div>
                        </div>
                    </div>

                    <form action="usuarios.do?op=actualizar" method="post">
                        <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">

                        <div class="row g-4">
                            <!-- Nombre Completo -->
                            <div class="col-md-12">
                                <label class="form-label fs-5">
                                    Nombre Completo
                                </label>
                                <input type="text" name="nombre_completo" class="form-control form-control-lg"
                                       value="${usuario.nombreCompleto}" required>
                            </div>

                            <!-- Correo -->
                            <div class="col-md-6">
                                <label class="form-label fs-5">
                                    Correo Electrónico
                                </label>
                                <input type="email" name="correo" class="form-control form-control-lg"
                                       value="${usuario.correo}" required>
                            </div>

                            <!-- Usuario -->
                            <div class="col-md-6">
                                <label class="form-label fs-5">
                                    Nombre de Usuario
                                </label>
                                <input type="text" name="usuario" class="form-control form-control-lg"
                                       value="${usuario.usuario}" required maxlength="20">
                            </div>

                            <!-- Tipo de Usuario -->
                            <div class="col-md-6">
                                <label class="form-label fs-5">
                                    Tipo de Usuario
                                </label>
                                <select name="tipo_usuario" class="form-select form-select-lg" required>
                                    <c:forEach var="tipo" items="${tiposUsuario}">
                                        <option value="${tipo.idTipo}"
                                            ${usuario.tipoUsuario.idTipo == tipo.idTipo ? 'selected' : ''}>
                                                ${tipo.nombreTipo}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="text-center mt-5">
                            <button type="submit" class="btn btn-update text-white shadow-lg">
                                ACTUALIZAR USUARIO
                            </button>
                            <a href="usuarios.do" class="btn btn-secondary btn-lg ms-3">
                                Cancelar
                            </a>
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