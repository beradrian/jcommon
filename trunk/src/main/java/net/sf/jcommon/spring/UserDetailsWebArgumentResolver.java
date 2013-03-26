package net.sf.jcommon.spring;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/** 
 * A Spring WebArgumentResolver which resolves a controller method parameter with any type that implements the
 * UserDetails interface. The value is populated with the currently logged in user. 
 */
public class UserDetailsWebArgumentResolver implements WebArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		if (UserDetails.class.isAssignableFrom(methodParameter.getParameterType())) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && methodParameter.getParameterType().isInstance(auth.getPrincipal()))
				return auth.getPrincipal();
			return null;
		}
		return WebArgumentResolver.UNRESOLVED;
	}

}
