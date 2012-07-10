package com.fastbiz.core.solution;

import com.fastbiz.core.solution.spring.StandardSolutionFactory;

public class DirSolutionFactory extends StandardSolutionFactory{

    public DirSolutionFactory() {
        super(new StandardSolutionBrowser());
    }
    
    public DirSolutionFactory(String solutionsDirPath) {
        super(new StandardSolutionBrowser(solutionsDirPath));
    }
}
