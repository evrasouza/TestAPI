package Core;

import io.restassured.http.ContentType;

public interface Constantes {
	String APP_BASE_URL = "https://serverest.dev";
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 5000L;
}
