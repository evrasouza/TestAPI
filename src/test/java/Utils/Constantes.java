package Utils;

import io.restassured.http.ContentType;

public interface Constantes {
	String APP_BASE_URL = "https://serverest.dev";
	String APP_BASE_URL_USUARIOS = "https://serverest.dev/usuarios/";
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 5000L;

}
