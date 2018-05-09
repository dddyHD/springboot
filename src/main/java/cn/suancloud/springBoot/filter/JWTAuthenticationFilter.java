package cn.suancloud.springBoot.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.suancloud.springBoot.security.CustomUserService;
import io.jsonwebtoken.Jwts;

import static cn.suancloud.springBoot.util.Constant.JWT_SECRET;

/**
 * Created by admin on 2018/5/9.
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
  private CustomUserService customUserService;
  public JWTAuthenticationFilter(AuthenticationManager authenticationManager,CustomUserService customUserService) {
    super(authenticationManager);
    this.customUserService = customUserService;
  }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);

  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
      // parse the token.
      String user = Jwts.parser()
              .setSigningKey(JWT_SECRET)
              .parseClaimsJws(token.replace("Bearer ", ""))
              .getBody()
              .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null,customUserService
                .loadUserByUsername(user).getAuthorities());
      }
      return null;
    }
    return null;
  }
}