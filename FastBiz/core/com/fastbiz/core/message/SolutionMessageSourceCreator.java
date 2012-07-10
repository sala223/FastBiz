package com.fastbiz.core.message;

import org.springframework.context.MessageSource;

public interface SolutionMessageSourceCreator{

    MessageSource createMessageSource(String solutionId, String[] basenames);

    void setDefaultEncoding(String defaultEncoding);

    void setCacheSeconds(int cacheSeconds);

    void setAlwaysUseMessageFormat(boolean useMessageFormat);
}
