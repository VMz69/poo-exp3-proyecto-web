<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Editar Ejemplar - Biblioteca UDB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .campo-especifico {
            display: none;
            margin-top: 25px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 12px;
            border: 2px solid #0d6efd;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .campo-especifico h5 {
            color: #0d6efd;
            border-bottom: 2px solid #0d6efd;
            padding-bottom: 10px;
            margin-bottom: 15px;
            font-weight: bold;
        }
        .required { color: #dc3545; font-weight: bold; }
        .card { max-width: 1100px; margin: 0 auto; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-5 mb-5">
    <div class="card shadow-lg">
        <div class="card-header bg-warning text-dark text-center">
            <h3 class="mb-0">
                <i class="bi bi-pencil-square"></i> Editar Ejemplar
            </h3>
        </div>
        <div class="card-body p-4">

            <!-- Mensajes de error -->
            <c:if test="${not empty listaErrores}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <strong>Por favor corrige los siguientes errores:</strong>
                    <ul class="mb-0 mt-2">
                        <c:forEach var="error" items="${listaErrores}">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Mensaje de éxito -->
            <c:if test="${not empty sessionScope.exito}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle-fill"></i> ${sessionScope.exito}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% session.removeAttribute("exito"); %>
            </c:if>

            <form action="ejemplares.do?op=modificar" method="post">
                <!-- ID oculto -->
                <input type="hidden" name="idEjemplar" value="${ejemplar.idEjemplar}">

                <div class="row g-3">
                    <!-- Tipo de Documento (solo lectura) -->
                    <div class="col-md-6">
                        <label class="form-label">Tipo de Documento</label>
                        <input type="text" class="form-control form-control-lg bg-light"
                               value="${ejemplar.tipoDocumentoString}" readonly>
                        <input type="hidden" name="idTipoDocumento" value="${ejemplar.tipoDocumento.idTipoDoc}">
                    </div>
                    <!-- Título -->
                    <div class="col-md-6">
                        <label class="form-label">Título <span class="required">*</span></label>
                        <input type="text" name="titulo" class="form-control form-control-lg"
                               value="${ejemplar.titulo}" required maxlength="200">
                    </div>
                </div>

                <!-- Campos comunes -->
                <div class="row g-3 mt-3">
                    <div class="col-md-6">
                        <label class="form-label">Autor / Artista / Director</label>
                        <input type="text" name="autor" class="form-control" value="${ejemplar.autor}" maxlength="150">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Editorial / Productora</label>
                        <input type="text" name="editorial" class="form-control" value="${ejemplar.editorial}" maxlength="100">
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    <div class="col-md-4">
                        <label class="form-label">ISBN / ISSN</label>
                        <input type="text" name="isbn" class="form-control" value="${ejemplar.isbn}" maxlength="20">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Año de Publicación</label>
                        <input type="number" name="anioPublicacion" class="form-control"
                               value="${ejemplar.anioPublicacion}" min="1500" max="2100">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Idioma</label>
                        <input type="text" name="idioma" class="form-control" value="${ejemplar.idioma}">
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    <div class="col-md-4">
                        <label class="form-label">Categoría</label>
                        <select name="idCategoria" class="form-select">
                            <option value="">-- Sin categoría --</option>
                            <c:forEach var="cat" items="${categorias}">
                                <option value="${cat.idCategoria}"
                                    ${ejemplar.categoria != null && ejemplar.categoria.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                        ${cat.nombreCategoria}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Ubicación <span class="required">*</span></label>
                        <select name="idUbicacion" class="form-select" required>
                            <c:forEach var="ubi" items="${ubicaciones}">
                                <option value="${ubi.idUbicacion}"
                                    ${ejemplar.ubicacion.idUbicacion == ubi.idUbicacion ? 'selected' : ''}>
                                    Edificio ${ubi.edificio} - Piso ${ubi.piso} - Sección ${ubi.seccion} - Estante ${ubi.estante}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Cantidad Total <span class="required">*</span></label>
                        <input type="number" name="cantidadTotal" class="form-control"
                               value="${ejemplar.cantidadTotal}" min="1" required>
                    </div>
                </div>

                <!-- CAMPOS ESPECÍFICOS SEGÚN TIPO -->
                <!-- LIBRO -->
                <div id="camposLibro" class="campo-especifico">
                    <h5><i class="bi bi-book"></i> Información de Libro</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Número de páginas</label>
                            <input type="number" name="numPaginasLibro" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Libro' ? ejemplar.numPaginas : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Colección</label>
                            <input type="text" name="coleccion" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Libro' ? ejemplar.coleccion : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Número/Serie</label>
                            <input type="text" name="numeroSerie" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Libro' ? ejemplar.numeroSerie : ''}">
                        </div>
                    </div>
                </div>

                <!-- TESIS -->
                <div id="camposTesis" class="campo-especifico">
                    <h5><i class="bi bi-mortarboard"></i> Información de Tesis</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Páginas</label>
                            <input type="number" name="numPaginasTesis" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.numPaginas : ''}">
                        </div>
                        <div class="col-md-8">
                            <label class="form-label">Universidad</label>
                            <input type="text" name="universidad" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.universidad : ''}">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Facultad</label>
                            <input type="text" name="facultad" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.facultad : ''}">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Carrera</label>
                            <input type="text" name="carrera" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.carrera : ''}">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Asesor</label>
                            <input type="text" name="asesor" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.asesor : ''}">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Grado Académico</label>
                            <input type="text" name="gradoAcademico" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Tesis' ? ejemplar.gradoAcademico : ''}">
                        </div>
                    </div>
                </div>

                <!-- REVISTA -->
                <div id="camposRevista" class="campo-especifico">
                    <h5><i class="bi bi-newspaper"></i> Información de Revista</h5>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Páginas</label>
                            <input type="number" name="numPaginasRevista" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Revista' ? ejemplar.numPaginas : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Volumen</label>
                            <input type="text" name="volumen" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Revista' ? ejemplar.volumen : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Número</label>
                            <input type="text" name="numero" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Revista' ? ejemplar.numero : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Periodicidad</label>
                            <input type="text" name="periodicidad" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Revista' ? ejemplar.periodicidad : ''}">
                        </div>
                    </div>
                </div>

                <!-- CD -->
                <div id="camposCD" class="campo-especifico">
                    <h5><i class="bi bi-disc"></i> Información de CD</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Duración (min)</label>
                            <input type="number" name="duracionCD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'CD' ? ejemplar.duracion : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Formato</label>
                            <input type="text" name="formatoCD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'CD' ? ejemplar.formato : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Artista</label>
                            <input type="text" name="artistaCD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'CD' ? ejemplar.artista : ''}">
                        </div>
                    </div>
                </div>

                <!-- DVD -->
                <div id="camposDVD" class="campo-especifico">
                    <h5><i class="bi bi-film"></i> Información de DVD</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Duración (min)</label>
                            <input type="number" name="duracionDVD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'DVD' ? ejemplar.duracion : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Formato</label>
                            <input type="text" name="formatoDVD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'DVD' ? ejemplar.formato : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Director</label>
                            <input type="text" name="directorDVD" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'DVD' ? ejemplar.director : ''}">
                        </div>
                    </div>
                </div>

                <!-- INFORME -->
                <div id="camposInforme" class="campo-especifico">
                    <h5><i class="bi bi-file-earmark-text"></i> Información de Informe</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Páginas</label>
                            <input type="number" name="numPaginasInforme" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Informe' ? ejemplar.numPaginas : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Institución</label>
                            <input type="text" name="institucion" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Informe' ? ejemplar.institucion : ''}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Supervisor</label>
                            <input type="text" name="supervisor" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Informe' ? ejemplar.supervisor : ''}">
                        </div>
                    </div>
                </div>

                <!-- MANUAL -->
                <div id="camposManual" class="campo-especifico">
                    <h5><i class="bi bi-journal-code"></i> Información de Manual</h5>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Páginas</label>
                            <input type="number" name="numPaginasManual" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Manual' ? ejemplar.numPaginas : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Área</label>
                            <input type="text" name="areaManual" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Manual' ? ejemplar.area : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Nivel de usuario</label>
                            <input type="text" name="nivelUsuario" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Manual' ? ejemplar.nivelUsuario : ''}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Versión</label>
                            <input type="text" name="versionManual" class="form-control"
                                   value="${ejemplar['class'].simpleName == 'Manual' ? ejemplar.version : ''}">
                        </div>
                    </div>
                </div>

                <hr class="my-4">
                <div class="text-end">
                    <a href="ejemplares.do?op=listar" class="btn btn-secondary btn-lg px-4">Cancelar</a>
                    <button type="submit" class="btn btn-warning btn-lg px-5">
                        <i class="bi bi-save"></i> Guardar Cambios
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        var tipoClase = "${ejemplar['class'].simpleName}";

        $('.campo-especifico').hide();

        // Mostrar según el tipo
        switch(tipoClase) {
            case 'Libro':
                $('#camposLibro').show();
                break;
            case 'Tesis':
                $('#camposTesis').show();
                break;
            case 'Revista':
                $('#camposRevista').show();
                break;
            case 'CD':
                $('#camposCD').show();
                break;
            case 'DVD':
                $('#camposDVD').show();
                break;
            case 'Informe':
                $('#camposInforme').show();
                break;
            case 'Manual':
                $('#camposManual').show();
                break;
            default:
        }
    });
</script>
</body>
</html>