package com.fastbiz.solution.exception;

public class CategoryException extends WMSException{

    private static final long   serialVersionUID                           = 1L;

    private static final String CATEGORY_SERVICE_NAME                      = "Category Service";

    private static final String ERROR_CODE_PARENT_CATEGORY_NON_EXIST       = "PARENT_CATEGORY_NON_EXIST";

    private static final String ERROR_CODE_DISABLE_CATEGORY_CHILDREN_EXIST = "DISABLE_CATEGORY_CHILDREN_EXIST";

    private static final String ERROR_CODE_CATEGORY_ALREADY_EXIST          = "CATEGORY_ALREADY_EXIST";

    private static final String ERROR_CODE_CATEGORY_NON_EXIST              = "CATEGORY_NON_EXIST";

    public CategoryException(String errorCode) {
        super(CATEGORY_SERVICE_NAME, errorCode);
    }

    public CategoryException(String errorCode, Throwable cause) {
        super(CATEGORY_SERVICE_NAME, errorCode, cause);
    }

    public CategoryException(String errorCode, String message, Object ... args) {
        super(CATEGORY_SERVICE_NAME, errorCode, message, args);
    }

    public static CategoryException parentCategoryNotExistException(long parentCategoryId){
        String fmt = "Parent category ID=%s does not exist";
        return new CategoryException(ERROR_CODE_PARENT_CATEGORY_NON_EXIST, fmt, parentCategoryId);
    }
    
    public static CategoryException parentCategoryNotExistException(String parentCategoryId){
        String fmt = "Parent category ID=%s does not exist";
        return new CategoryException(ERROR_CODE_PARENT_CATEGORY_NON_EXIST, fmt, parentCategoryId);
    }

    public static CategoryException disableCategoryChildrenExistException(){
        return new CategoryException(ERROR_CODE_DISABLE_CATEGORY_CHILDREN_EXIST);
    }

    public static CategoryException CategoryAlreadyExistException(String categoryName){
        return new CategoryException(ERROR_CODE_CATEGORY_ALREADY_EXIST, "Category NAME=%s already exist", categoryName);
    }

    public static CategoryException CategoryNonExistException(long categoryId){
        return new CategoryException(ERROR_CODE_CATEGORY_NON_EXIST, "Category ID=%s does not exist", categoryId);
    }

    public static CategoryException CategoryNonExistException(String categoryName){
        return new CategoryException(ERROR_CODE_CATEGORY_NON_EXIST, "Category NAME=%s does not exist", categoryName);
    }
}
