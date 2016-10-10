package net.sf.jcommon.spring;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

public class ChainedLocaleResolver implements LocaleResolver {

    private List<LocaleResolver> localeResolvers;
    
    public ChainedLocaleResolver(List<LocaleResolver> localeResolvers) {
        this.localeResolvers = localeResolvers;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return null;
    }

    @Override
    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, Locale locale) {
        for (LocaleResolver localeResolver : localeResolvers) {
            try {
                localeResolver.setLocale(request, response, locale);
            } catch (UnsupportedOperationException exc) {
                // do nothing, silently ignore and set the locale for the others
            }
        }
    }

}
