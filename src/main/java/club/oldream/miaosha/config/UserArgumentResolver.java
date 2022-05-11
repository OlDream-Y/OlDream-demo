package club.oldream.miaosha.config;

import club.oldream.miaosha.domain.MiaoshaUser;
import club.oldream.miaosha.service.MiaoShaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private MiaoShaUserService userService;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		Class<?> parameterType = methodParameter.getParameterType();
		return parameterType==MiaoshaUser.class;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		String paramToken=request.getParameter(MiaoShaUserService.COOKI_NAME_TOKEN);
		String cookieToken=getCookieValue(request, MiaoShaUserService.COOKI_NAME_TOKEN);
		if (cookieToken==null&&paramToken==null){
			
			return null;
		}
		String token=paramToken==null?cookieToken:paramToken;
		return userService.getByToken(response,token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiNameToken) {
		Cookie[] cookies = request.getCookies();
		if (cookies==null||cookies.length<=0){
			return null;
		}
		for (Cookie cookie:cookies){
			if (cookie.getName().equals(cookiNameToken))
				return cookie.getValue();
		}
		return null;
	}
}
