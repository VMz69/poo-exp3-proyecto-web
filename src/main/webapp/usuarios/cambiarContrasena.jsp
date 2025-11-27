<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Cambiar Contraseña</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            max-width: 500px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.4);
            border-radius: 25px;
            border: none;
        }
        .card-header {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 2.5rem;
            border-radius: 25px 25px 0 0 !important;
        }
        .btn-cambiar {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            padding: 15px 50px;
            font-size: 1.4rem;
            border-radius: 50px;
        }
        .btn-cambiar:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(40,167,69,0.5);
        }
    </style>
</head>
<body class="d-flex align-items-center">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-12">

            <div class="card mx-auto">
                <div class="card-header text-center">
                    <h1 class="mb-0">
                        Cambiar Contraseña
                    </h1>
                    <p class="mb-0 mt-3 fs-5">
                        <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong>
                    </p>
                </div>

                <div class="card-body p-5">

                    <!-- Mensajes -->
                    <c:if test="${not empty sessionScope.exito}">
                        <div class="alert alert-success text-center py-4 fs-5">
                                ${sessionScope.exito}
                        </div>
                        <c:remove var="exito" scope="session"/>
                    </c:if>
                    <c:if test="${not empty sessionScope.fracaso}">
                        <div class="alert alert-danger text-center">
                                ${sessionScope.fracaso}
                        </div>
                        <c:remove var="fracaso" scope="session"/>
                    </c:if>

                    <form action="cambiarContrasena.do?op=cambiar" method="post" class="mt-4">
                        <div class="mb-4">
                            <label class="form-label fw-bold fs-5">
                                Nueva Contraseña
                            </label>
                            <input type="password" name="nueva" class="form-control form-control-lg"
                                   required minlength="4" placeholder="Mínimo 4 caracteres">
                        </div>

                        <div class="mb-5">
                            <label class="form-label fw-bold fs-5">
                                Confirmar Contraseña
                            </label>
                            <input type="password" name="confirmar" class="form-control form-control-lg"
                                   required placeholder="Repite la contraseña">
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-cambiar text-white shadow-lg">
                                CAMBIAR CONTRASEÑA
                            </button>
                        </div>
                    </form>

                    <div class="text-center mt-4">
                        <a href="index.jsp" class="btn btn-outline-light btn-lg">
                            Volver al Inicio
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>