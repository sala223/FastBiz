package com.fastbiz.core.solution;

import java.io.File;
import com.fastbiz.common.exception.InfrastructureException;

public class SolutionException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public SolutionException(Throwable cause) {
        super(cause);
    }

    public SolutionException(String format, Object ... args) {
        super(format, args);
    }

    public SolutionException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }

    public static SolutionException SolutionDoesNotExist(String solutionId){
        return new SolutionException("Solution %s does not exist",solutionId);
    }

    public static SolutionException CannotFindSolutionDescriptionFile(String solutionDescriptionFilePath){
        return new SolutionException("Cannot find solution description file %s", solutionDescriptionFilePath);
    }

    public static SolutionException CannotFindSolutionDescriptionFile(File solutionDescriptionFile){
        return new SolutionException("Cannot find solution description file %s", solutionDescriptionFile);
    }
}
