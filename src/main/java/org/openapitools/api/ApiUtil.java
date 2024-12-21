package org.openapitools.api;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

public class ApiUtil {
    // Constructor privado para evitar instanciación
    private ApiUtil() {
        // Evitar instanciación accidental
    }

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            res.setCharacterEncoding("UTF-8");
            res.addHeader("Content-Type", contentType);
            res.getWriter().print(example);
        } catch (IOException e) {
            // Lanzar una excepción más específica con un mensaje descriptivo
            throw new IllegalStateException("Error al escribir la respuesta de ejemplo", e);
        }
    }
}

