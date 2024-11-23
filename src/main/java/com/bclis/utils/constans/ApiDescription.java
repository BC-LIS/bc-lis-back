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
    public static final String GET_CATEGORY_DESCRIPTION = "Endpoint that allows retrieving all available categories.\n\nIt can be accessed by all existing roles.";
    public static final String CREATE_CATEGORY_DESCRIPTION = "Endpoint that allows create a category and you must indicate the name of the category in the body of the request.\n\nIt can be accessed by all existing roles.";
    public static final String DELETE_CATEGORY_DESCRIPTION = "Endpoint that allows deleting a category by specifying its name.\n\nIt can be accessed by all existing roles.";

    //Type controller
    public static final String TYPE_CONTROLLER_DESCRIPTION = "This controller manages the type that can exist for each document.";
    public static final String GET_TYPE_DESCRIPTION = "Endpoint that allows retrieving all available types.\n\nIt can be accessed by all existing roles.";
    public static final String CREATE_TYPE_DESCRIPTION = "Endpoint that allows create a type of document.\n\nYou must indicate the name of the type in the body of the request.\n\nIt can be accessed by all existing roles.";
    public static final String DELETE_TYPE_DESCRIPTION = "Endpoint that allows deleting a type by specifying its name.\n\nIt can be accessed by all existing roles.";

    //Comment controller
    public static final String COMMENT_CONTROLLER_DESCRIPTION = "This controller manages the comments that can exist for each document.";
    public static final String CREATE_COMMENT_DESCRIPTION = "Endpoint that allows creating comments on each document.\n\nIt is necessary to send the content of the comment and the document Id";
    public static final String GET_COMMENT_DESCRIPTION = "Endpoint that allows retrieving a comment by documentId.\n\nIt can be accessed by all existing roles.";
    public static final String UPDATE_COMMENT_STATE_DESCRIPTION = "Endpoint that allows updating the document status.\n\nIt is necessary to send the document Id and the new status of the comment.\n\nThe available states are: VISIBLE and HIDDEN.\n\nIt can be accessed by all existing roles.";
    public static final String UPDATE_COMMENT_CONTENT_DESCRIPTION = "Endpoint that allows updating the document content.\n\nIt is necessary to send the Id and the new content of the comment.\n\nIt can be accessed by all existing roles.";
    public static final String DELETE_COMMENT_DESCRIPTION = "Endpoint that allows deleting a comment by specifying its id.\n\nIt can be accessed by all existing roles.";

    // Document Controller
    public static final String DOCUMENT_CONTROLLER_DESCRIPTION = "This controller handles operations related to document management.";
    public static final String CREATE_DOCUMENT_DESCRIPTION = "Endpoint that allows the creation of new documents.\n\nIn the body of the request, you must indicate: name, description, attach document, document status, document type, creator user, and associated category.\n\nIt can be accessed by all existing roles.";
    public static final String GET_DOCUMENT_DESCRIPTION = "Endpoint that allows you to obtain a specific document by its Id.\n\nIt can be accessed by all existing roles.";
    public static final String GET_ALL_DOCUMENT_DESCRIPTION = "Endpoint that allows you to obtain all documents.\n\nIt can be accessed by all existing roles.";
    public static final String GET_DOCUMENT_BY_FILTER_DESCRIPTION = "Endpoint that allows me to obtain the documents according to the specified filters.\n\nIt can be accessed by all existing roles.\n\nThe available filters with their respective format are:\n\n\"name\": \"value\",\n\n\"description\": \"value\",\n\n\"state\": \"value\",\n\n\"username\": \"value\",\n\n\"typeName\": \"value\",\n\n\"categories\": \"value1,value2,value3, ...\"\n\n\"createdBefore\": \"yyyy-MM-dd\",\n\n\"updatedBefore\": \"yyyy-MM-dd\",\n\n\"createdAfter\": \"yyyy-MM-dd\",\n\n\"updatedAfter\": \"yyyy-MM-dd\"";
    public static final String GET_DOCUMENT_TO_DOWNLOAD_DESCRIPTION = "Endpoint that allows you to obtain document by Id to download.\n\nIt can be accessed by all existing roles.";
    public static final String UPDATE_DOCUMENT_DESCRIPTION = "Endpoint that allows updating a specific document by its Id.";
    public static final String DELETE_DOCUMENT_DESCRIPTION = "Endpoint that allows you to delete a specific document by its ID.";
}
