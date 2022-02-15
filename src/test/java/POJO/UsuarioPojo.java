package POJO;

public class UsuarioPojo extends LoginPojo {
	
	private String nome;
	private String administrador;
	//private String _id;
	

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAdministrador() {
		return administrador;
	}
	
	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}
	
	/*
	 * public String get_id() { return _id; }
	 * 
	 * public void set_id(String _id) { this._id = _id; }
	 */

}
