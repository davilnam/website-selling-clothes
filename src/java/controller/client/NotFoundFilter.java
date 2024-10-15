package controller.client;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@WebFilter(filterName = "NotFoundFilter", urlPatterns = {"/*"})
public class NotFoundFilter implements Filter {

    private static final boolean debug = true;
    private FilterConfig filterConfig = null;

    public NotFoundFilter() {
    }

    private boolean isValidPath(String path) {
        Set<String> validPaths = new HashSet<>();
        validPaths.add("/");
        validPaths.add("/login");
        validPaths.add("/home");
        validPaths.add("/register");
        validPaths.add("/product");
        validPaths.add("/filter");
        validPaths.add("/blog");        
        validPaths.add("/contacts");
        validPaths.add("/cart");
        validPaths.add("/product-detail");
        validPaths.add("/admin/category");
        validPaths.add("/admin/product");
        validPaths.add("/admin/order");
        validPaths.add("/admin/stats");
        validPaths.add("/add-to-cart");
        validPaths.add("/checkout");
        validPaths.add("/logout");
        validPaths.add("/search");
        validPaths.add("/thank-you");
        validPaths.add("/profile");
        validPaths.add("/purchase");
        validPaths.add("/notFound"); // Add notFound path

        return validPaths.contains(path);
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/images/") || path.startsWith("/css/") || path.startsWith("/js/");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("NotFoundFilter:doFilter()");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Exclude static resources from the filter
        if (isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the URL is invalid
        if (!isValidPath(path)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/notFound");
            return;
        }

        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            problem = t;
            t.printStackTrace();
            sendProcessingError(problem, response);
        }
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>");
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                // Handle the exception
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                // Handle the exception
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
            // Handle the exception
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null && debug) {
            log("NotFoundFilter:Initializing filter");
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return "NotFoundFilter()";
        }
        StringBuilder sb = new StringBuilder("NotFoundFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return sb.toString();
    }
}
