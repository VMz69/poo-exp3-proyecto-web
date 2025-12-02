package com.biblioteca.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
            "/",                 // raíz
            "/index.jsp",        // index siempre accesible
            "/login.jsp",
            "/login.do",
            "/logout.do",
            "/consulta.do",

            // recursos estáticos
            "/css/",
            "/js/",
            "/images/",
            "/resources/"
    };

    @Override
    public void init(FilterConfig filterConfig) {}

    private boolean isPublic(HttpServletRequest req) {
        String path = req.getRequestURI()
                .substring(req.getContextPath().length());

        // raíz
        if (path.isEmpty() || path.equals("/")) {
            return true;
        }

        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // evitar cache de páginas protegidas
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null &&
                session.getAttribute("usuarioLogueado") != null;

        // ✅ permitir todo lo público
        if (isPublic(req)) {
            chain.doFilter(request, response);
            return;
        }

        // ❌ página privada sin sesión → login
        if (!loggedIn) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?timeout=1");
            return;
        }

        // ✅ usuario autenticado → continuar
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
