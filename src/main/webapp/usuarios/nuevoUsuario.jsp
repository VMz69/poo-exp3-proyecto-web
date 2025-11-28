<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nuevo Usuario - Biblioteca UDB</title>
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
        }
        .card-header {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 2rem;
        }
        .btn-save {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            padding: 12px 50px;
            font-size: 1.3em;
            border-radius: 50px;
            transition: all 0.3s ease;
        }
        .btn-save:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(40,167,69,0.5);
        }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118, 75, 162, 0.25);
        }
        .form-label {
            font-weight: 600;
            color: #495057;
        }
    </style>
</head>
<body>
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card border-0">
                <div class="card-header text-center">
                    <h1 class="mb-0">
                        <i class="fas fa-user-plus fa-2x"></i><br>
                        Crear Nuevo Usuario
                    </h1>
                    <p class="mb-0 mt-3 fs-5">Complete todos los campos para registrar un nuevo usuario</p>
                </div>
                <div class="card-body p-5">

                    <!-- Mensajes de error -->
                    <c:if test="${not empty listaErrores}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            <i class="fas fa-exclamation-triangle"></i>
                            <strong>Por favor corrige los siguientes errores:</strong>
                            <ul class="mb-0 mt-2">
                                <c:forEach var="error" items="${listaErrores}">
                                    <li>${error}</li>
                                </c:forEach>
                            </ul>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="usuarios.do?op=insertar" method="post">
                        <div class="row g-4">
                            <!-- Nombre Completo -->
                            <div class="col-md-12">
                                <label class="form-label">
                                    <i class="fas fa-user"></i> Nombre Completo
                                </label>
                                <input type="text" name="nombre_completo" class="form-control form-control-lg"
                                       value="${param.nombre_completo}" required
                                       placeholder="Ej: Juan Perez ">
                            </div>

                            <!-- Correo -->
                            <div class="col-md-6">
                                <label class="form-label">
                                    <i class="fas fa-envelope"></i> Correo Electrónico
                                </label>
                                <input type="email" name="correo" class="form-control form-control-lg"
                                       value="${param.correo}" required
                                       placeholder="usuario@udb.edu.sv">
                            </div>

                            <!-- Usuario -->
                            <div class="col-md-6">
                                <label class="form-label">
                                    <i class="fas fa-user-tag"></i> Nombre de Usuario
                                </label>
                                <input type="text" name="usuario" class="form-control form-control-lg"
                                       value="${param.usuario}" required
                                       placeholder="juan.perez" maxlength="20">
                            </div>

                            <!-- Contraseña -->
                            <div class="col-md-6">
                                <label class="form-label">
                                    <i class="fas fa-lock"></i> Contraseña
                                </label>
                                <input type="password" name="contrasena" class="form-control form-control-lg"
                                       required placeholder="******">
                            </div>

                            <!-- Tipo de Usuario -->
                            <div class="col-md-6">
                                <label class="form-label">
                                    <i class="fas fa-user-shield"></i> Tipo de Usuario
                                </label>
                                <select name="tipo_usuario" class="form-select form-select-lg" required>
                                    <option value="">-- Seleccionar tipo --</option>
                                    <c:forEach var="tipo" items="${tiposUsuario}">
                                        <option value="${tipo.idTipo}"
                                            ${param.tipo_usuario == tipo.idTipo ? 'selected' : ''}>
                                                ${tipo.nombreTipo}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="text-center mt-5">
                            <button type="submit" class="btn btn-save text-white shadow-lg">
                                <i class="fas fa-save"></i> GUARDAR USUARIO
                            </button>
                            <a href="usuarios.do" class="btn btn-secondary btn-lg ms-3">
                                <i class="fas fa-arrow-left"></i> Cancelar
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