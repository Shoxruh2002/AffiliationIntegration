package uz.atm.exception.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiMessages {

    public static final String BAD_REQUEST = "Invalid request. Please check request once more. ";
    public static final String NOT_FOUND = "Could not find requesting data ";
    public static final String INTERNAL_SERVER_ERROR = "Error occurs while exchange data with database. Please try again later, after checking given error details. ";
    public static final String UNSUPPORTED_MEDIA_TYPE = "Error occurs cause coming unsupported media type. Please send shown body only. ";
    public static final String METHOD_NOT_ALLOWED = "Used not valid http method. Please check it. ";
    public static final String ACCESS_DENIED = "This resource is forbidden for this credentials! ";
    public static final String UNAUTHORIZED = "Please login first in order to access the resource! ";
    public static final String ERROR_WHILE_CALLING_JUSTICE = "Error Occurred while calling Justice API ";
    public static final String SERVICE_UNAVAILABLE = "Third Party service is unavailable ";

}
