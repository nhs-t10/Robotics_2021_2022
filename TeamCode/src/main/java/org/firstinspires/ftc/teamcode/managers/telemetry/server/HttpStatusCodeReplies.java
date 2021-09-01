package org.firstinspires.ftc.teamcode.managers.telemetry.server;

public class HttpStatusCodeReplies {
    public static String status(int snum) {
        switch (snum) {
            case 100: return Continue;
            case 101: return Switching_Protocols;
            case 103: return Early_Hints;
            case 200: return OK;
            case 201: return Created;
            case 202: return Accepted;
            case 203: return Non_Authoritative_Information;
            case 204: return No_Content;
            case 205: return Reset_Content;
            case 206: return Partial_Content;
            case 300: return Multiple_Choices;
            case 301: return Moved_Permanently;
            case 302: return Found;
            case 303: return See_Other;
            case 304: return Not_Modified;
            case 307: return Temporary_Redirect;
            case 308: return Permanent_Redirect;
            case 400: return Bad_Request;
            case 401: return Unauthorized;
            case 402: return Payment_Required;
            case 403: return Forbidden;
            case 404: return Not_Found;
            case 405: return Method_Not_Allowed;
            case 406: return Not_Acceptable;
            case 407: return Proxy_Authentication_Required;
            case 408: return Request_Timeout;
            case 409: return Conflict;
            case 410: return Gone;
            case 411: return Length_Required;
            case 412: return Precondition_Failed;
            case 413: return Payload_Too_Large;
            case 414: return URI_Too_Long;
            case 415: return Unsupported_Media_Type;
            case 416: return Range_Not_Satisfiable;
            case 417: return Expectation_Failed;
            case 418: return I_m_a_teapot;
            case 422: return Unprocessable_Entity;
            case 425: return Too_Early;
            case 426: return Upgrade_Required;
            case 428: return Precondition_Required;
            case 429: return Too_Many_Requests;
            case 431: return Request_Header_Fields_Too_Large;
            case 451: return Unavailable_For_Legal_Reasons;
            case 500: return Internal_Server_Error;
            case 501: return Not_Implemented;
            case 502: return Bad_Gateway;
            case 503: return Service_Unavailable;
            case 504: return Gateway_Timeout;
            case 505: return HTTP_Version_Not_Supported;
            case 506: return Variant_Also_Negotiates;
            case 507: return Insufficient_Storage;
            case 508: return Loop_Detected;
            case 510: return Not_Extended;
            case 511: return Network_Authentication_Required;
        }
        return HTTP_Version_Not_Supported;
    }
    public static String Continue = "HTTP/1.1 100 Continue\nContent-Length: 3\n\n100";

    public static String Switching_Protocols = "HTTP/1.1 101 Switching Protocols\nContent-Length: 3\n\n101";

    public static String Early_Hints = "HTTP/1.1 103 Early Hints\nContent-Length: 3\n\n103";

    public static String OK = "HTTP/1.1 200 OK\nContent-Length: 3\n\n200";

    public static String Created = "HTTP/1.1 201 Created\nContent-Length: 3\n\n201";

    public static String Accepted = "HTTP/1.1 202 Accepted\nContent-Length: 3\n\n202";

    public static String Non_Authoritative_Information = "HTTP/1.1 203 Non-Authoritative Information\nContent-Length: 3\n\n203";

    public static String No_Content = "HTTP/1.1 204 No Content\nContent-Length: 3\n\n204";

    public static String Reset_Content = "HTTP/1.1 205 Reset Content\nContent-Length: 3\n\n205";

    public static String Partial_Content = "HTTP/1.1 206 Partial Content\nContent-Length: 3\n\n206";

    public static String Multiple_Choices = "HTTP/1.1 300 Multiple Choices\nContent-Length: 3\n\n300";

    public static String Moved_Permanently = "HTTP/1.1 301 Moved Permanently\nContent-Length: 3\n\n301";

    public static String Found = "HTTP/1.1 302 Found\nContent-Length: 3\n\n302";

    public static String See_Other = "HTTP/1.1 303 See Other\nContent-Length: 3\n\n303";

    public static String Not_Modified = "HTTP/1.1 304 Not Modified\nContent-Length: 3\n\n304";

    public static String Temporary_Redirect = "HTTP/1.1 307 Temporary Redirect\nContent-Length: 3\n\n307";

    public static String Permanent_Redirect = "HTTP/1.1 308 Permanent Redirect\nContent-Length: 3\n\n308";

    public static String Bad_Request = "HTTP/1.1 400 Bad Request\nContent-Length: 3\n\n400";

    public static String Unauthorized = "HTTP/1.1 401 Unauthorized\nContent-Length: 3\n\n401";

    public static String Payment_Required = "HTTP/1.1 402 Payment Required\nContent-Length: 3\n\n402";

    public static String Forbidden = "HTTP/1.1 403 Forbidden\nContent-Length: 3\n\n403";

    public static String Not_Found = "HTTP/1.1 404 Not Found\nContent-Length: 3\n\n404";

    public static String Method_Not_Allowed = "HTTP/1.1 405 Method Not Allowed\nContent-Length: 3\n\n405";

    public static String Not_Acceptable = "HTTP/1.1 406 Not Acceptable\nContent-Length: 3\n\n406";

    public static String Proxy_Authentication_Required = "HTTP/1.1 407 Proxy Authentication Required\nContent-Length: 3\n\n407";

    public static String Request_Timeout = "HTTP/1.1 408 Request Timeout\nContent-Length: 3\n\n408";

    public static String Conflict = "HTTP/1.1 409 Conflict\nContent-Length: 3\n\n409";

    public static String Gone = "HTTP/1.1 410 Gone\nContent-Length: 3\n\n410";

    public static String Length_Required = "HTTP/1.1 411 Length Required\nContent-Length: 3\n\n411";

    public static String Precondition_Failed = "HTTP/1.1 412 Precondition Failed\nContent-Length: 3\n\n412";

    public static String Payload_Too_Large = "HTTP/1.1 413 Payload Too Large\nContent-Length: 3\n\n413";

    public static String URI_Too_Long = "HTTP/1.1 414 URI Too Long\nContent-Length: 3\n\n414";

    public static String Unsupported_Media_Type = "HTTP/1.1 415 Unsupported Media Type\nContent-Length: 3\n\n415";

    public static String Range_Not_Satisfiable = "HTTP/1.1 416 Range Not Satisfiable\nContent-Length: 3\n\n416";

    public static String Expectation_Failed = "HTTP/1.1 417 Expectation Failed\nContent-Length: 3\n\n417";

    public static String I_m_a_teapot = "HTTP/1.1 418 I'm a teapot\nContent-Length: 3\n\n418";

    public static String Unprocessable_Entity = "HTTP/1.1 422 Unprocessable Entity\nContent-Length: 3\n\n422";

    public static String Too_Early = "HTTP/1.1 425 Too Early\nContent-Length: 3\n\n425";

    public static String Upgrade_Required = "HTTP/1.1 426 Upgrade Required\nContent-Length: 3\n\n426";

    public static String Precondition_Required = "HTTP/1.1 428 Precondition Required\nContent-Length: 3\n\n428";

    public static String Too_Many_Requests = "HTTP/1.1 429 Too Many Requests\nContent-Length: 3\n\n429";

    public static String Request_Header_Fields_Too_Large = "HTTP/1.1 431 Request Header Fields Too Large\nContent-Length: 3\n\n431";

    public static String Unavailable_For_Legal_Reasons = "HTTP/1.1 451 Unavailable For Legal Reasons\nContent-Length: 3\n\n451";

    public static String Internal_Server_Error = "HTTP/1.1 500 Internal Server Error\nContent-Length: 3\n\n500";

    public static String Not_Implemented = "HTTP/1.1 501 Not Implemented\nContent-Length: 3\n\n501";

    public static String Bad_Gateway = "HTTP/1.1 502 Bad Gateway\nContent-Length: 3\n\n502";

    public static String Service_Unavailable = "HTTP/1.1 503 Service Unavailable\nContent-Length: 3\n\n503";

    public static String Gateway_Timeout = "HTTP/1.1 504 Gateway Timeout\nContent-Length: 3\n\n504";

    public static String HTTP_Version_Not_Supported = "HTTP/1.1 505 HTTP Version Not Supported\nContent-Length: 3\n\n505";

    public static String Variant_Also_Negotiates = "HTTP/1.1 506 Variant Also Negotiates\nContent-Length: 3\n\n506";

    public static String Insufficient_Storage = "HTTP/1.1 507 Insufficient Storage\nContent-Length: 3\n\n507";

    public static String Loop_Detected = "HTTP/1.1 508 Loop Detected\nContent-Length: 3\n\n508";

    public static String Not_Extended = "HTTP/1.1 510 Not Extended\nContent-Length: 3\n\n510";

    public static String Network_Authentication_Required = "HTTP/1.1 511 Network Authentication Required\nContent-Length: 3\n\n511";


}
