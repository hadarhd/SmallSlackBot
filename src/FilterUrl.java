import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter
public class FilterUrl implements javax.servlet.Filter {
    private String UserName = ".*";
    private static Pattern pattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String UserName = filterConfig.getInitParameter("UserName");
        if (UserName != null) {
            this.UserName = UserName;
        }
        pattern = Pattern.compile("^\\/average\\/(.+)$");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletReq = (HttpServletRequest) servletRequest;
        Matcher match = pattern.matcher(httpServletReq.getRequestURI());
        if (match.matches()) {
            servletRequest.setAttribute("UserName",match.group(1));
            RequestDispatcher dispatcher = httpServletReq.getRequestDispatcher("personalAverage.jsp");
            dispatcher.forward(servletRequest, servletResponse);
        }
        RequestDispatcher dispatcher = httpServletReq.getRequestDispatcher("index.jsp");
        dispatcher.forward(servletRequest, servletResponse);
    }

    public void destroy() {}
}