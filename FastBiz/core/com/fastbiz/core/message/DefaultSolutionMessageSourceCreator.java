package com.fastbiz.core.message;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import com.fastbiz.core.solution.SolutionBrowser;
import com.fastbiz.core.solution.SolutionBrowserAware;
import com.fastbiz.core.solution.SolutionException;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.spring.SolutionResoruceLoader;

public class DefaultSolutionMessageSourceCreator implements SolutionMessageSourceCreator, SolutionBrowserAware{

    public int              cacheSeconds = -1;

    private String          defaultEncoding;

    private boolean         useMessageFormat;

    private SolutionBrowser browser;

    public DefaultSolutionMessageSourceCreator() {}

    public DefaultSolutionMessageSourceCreator(SolutionBrowser browser) {
        this.setSolutionBrowser(browser);
    }

    @Override
    public MessageSource createMessageSource(String solutionId, String[] basenames){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setAlwaysUseMessageFormat(useMessageFormat);
        messageSource.setCacheSeconds(-1);
        SolutionDescriptor solutionDescriptor = browser.getSolutionDescriptor(solutionId);
        if (solutionDescriptor == null) {
            throw SolutionException.SolutionDoesNotExist(solutionId);
        }
        messageSource.setResourceLoader(new SolutionResoruceLoader(solutionDescriptor));
        if (defaultEncoding != null) {
            messageSource.setDefaultEncoding(defaultEncoding);
        }
        messageSource.setBasenames(basenames);
        return messageSource;
    }

    @Override
    public void setDefaultEncoding(String defaultEncoding){
        this.defaultEncoding = defaultEncoding;
    }

    @Override
    public void setCacheSeconds(int cacheSeconds){
        this.cacheSeconds = cacheSeconds;
    }

    @Override
    public void setAlwaysUseMessageFormat(boolean useMessageFormat){
        this.useMessageFormat = useMessageFormat;
    }

    @Override
    public void setSolutionBrowser(SolutionBrowser browser){
        this.browser = browser;
    }
}
