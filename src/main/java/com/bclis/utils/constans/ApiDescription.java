package com.bclis.utils.constans;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiDescription {

    // AuthController
    public static final String AUTH_CONTROLLER_DESCRIPTION = "This controller handles operations related to user login and registration.";
    public static final String LOGIN_DESCRIPTION = "Endpoint that allows users to log in using their username and password.";
    public static final String REGISTER_DESCRIPTION = "Endpoint that allows the creation of new users";

    //CategoryController
    public static final String CATEGORY_CONTROLLER_DESCRIPTION = "This controller manages the categories that can exist for each document.";
    public static final String GET_CATEGORY_DESCRIPTION = "Endpoint that allows retrieving all available categories. \n"
            + "\n"
            +"It can be accessed by all existing roles.";

    public static final String CREATE_CATEGORY_DESCRIPTION = "Endpoint that allows create a category and you must indicate the name of the category in the body of the request. \n" +
            "\n"
            +"It can be accessed by all existing roles.";

    public static final String DELETE_CATEGORY_DESCRIPTION = "Endpoint that allows deleting a category by specifying its name \n" +
            "\n"
            +"It can be accessed by all existing roles.";

    // DocumentController
    public static final String DOCUMENT_CONTROLLER_DESCRIPTION = "This controller handles operations related to document management.";
    public static final String CREATE_DOCUMENT_DESCRIPTION = "Endpoint that allows the creation of new documents.";
    public static final String GET_DOCUMENT_DESCRIPTION = "Endpoint that allows you to obtain a specific document by its ID.";
    public static final String GET_ALL_DOCUMENT_DESCRIPTION = "Endpoint that allows you to obtain all documents.";
    public static final String UPDATE_DOCUMENT_DESCRIPTION = "Endpoint that allows updating a specific document by its ID.";
    public static final String DELETE_DOCUMENT_DESCRIPTION = "Endpoint that allows you to delete a specific document by its ID.";
}
