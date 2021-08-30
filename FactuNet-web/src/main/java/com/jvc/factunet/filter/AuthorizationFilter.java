package com.jvc.factunet.filter;

import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.OpcionesMenu;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
            try {

                    HttpServletRequest reqt = (HttpServletRequest) request;
                    HttpServletResponse resp = (HttpServletResponse) response;
                    HttpSession ses = reqt.getSession(false);

                    String reqURI = reqt.getRequestURI();
                    if (reqURI.indexOf("/login.xhtml") >= 0
					|| (ses != null && ses.getAttribute("username") != null)
					|| reqURI.indexOf("/public/") >= 0
					|| reqURI.contains("javax.faces.resource"))
                    {
                        if(!((reqURI.contains("/faces/index.xhtml")) || (reqURI.contains("/seguridades/miPerfilAdmin.xhtm"))))
                        {
                            try {
                                Empleado empleado = (Empleado) ses.getAttribute("empleado");
                                if(empleado != null)
                                {
                                    Boolean ban = Boolean.FALSE;
                                    for(OpcionesMenu opciones : empleado.getCuenta().getRol().getOpcionesMenuList())
                                    {
                                        if(!opciones.getMenu().getUrl().trim().isEmpty())
                                        {
                                            if(reqURI.contains(opciones.getMenu().getUrl()))
                                            {
                                                ban = Boolean.TRUE;
                                                break;
                                            }
                                        }
                                    }
                                    if(ban)
                                    {
                                        if(reqURI.contains("/transacciones/facturaVenta.xhtml"))
                                        {
                                            if(empleado.getPuntoVenta() == null)
                                            {
                                                resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml"); 
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(!reqURI.contains("/faces/error.xhtml"))
                                        {
                                            if(reqURI.contains("Dialog"))
                                            {
                                                resp.sendRedirect(reqt.getContextPath() + "/faces/error.xhtml"); 
                                            }
                                            else
                                            {
                                                if(!reqURI.contains("/faces/login.xhtml"))
                                                {
                                                    resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml"); 
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                if(!reqURI.contains("/faces/login.xhtml"))
                                {
                                    resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml"); 
                                }
                            }
                        }
                        chain.doFilter(request, response);
                    }
                    else
                    {
                        resp.sendRedirect(reqt.getContextPath() + "/faces/login.xhtml");
                    }
            } catch (Exception e) {
                    System.out.println(e.getMessage());
            }
    }

    @Override
    public void destroy() {

    }
}