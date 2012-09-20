package com.fastbiz.solution.exception;

public class CategoryException extends WMSException{

    private static final long   serialVersionUID                = 1L;

    private static final String CATEGORY_SERVICE_NAME           = "Category Service";

    private static int          PARENT_CATEGORY_NON_EXIST       = 10000001;

    private static int          DISABLE_CATEGORY_CHILDREN_EXIST = 10000002;

    private static int          CATEGORY_ALREADY_EXIST          = 10000003;

    public CategoryException(int errorCode) {
        super(CATEGORY_SERVICE_NAME, errorCode);
    }

    public CategoryException(int errorCode, Throwable cause) {
        super(CATEGORY_SERVICE_NAME, errorCode, cause);
    }

    public CategoryException(int errorCode, String message, Object ... args) {
        super(CATEGORY_SERVICE_NAME, errorCode, message, args);
    }

    public static CategoryException parentCategoryNotExistException(){
        return new CategoryException(PARENT_CATEGORY_NON_EXIST);
    }

    public static CategoryException disableCategoryChildrenExistException(){
        return new CategoryException(DISABLE_CATEGORY_CHILDREN_EXIST);
    }
    
    public static CategoryException CategoryAlreadyExistException(){
        return new CategoryException(CATEGORY_ALREADY_EXIST);
    }
}
