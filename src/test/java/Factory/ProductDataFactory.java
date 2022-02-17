package Factory;

import com.github.javafaker.Faker;

import POJO.ProductsPojo;

public class ProductDataFactory {	
	
	Faker faker = new Faker();
	
	public String nomeProduto = faker.commerce().productName();
	public Integer preco = faker.number().numberBetween(10,100);
	public String descricao = faker.commerce().material();
	public Integer quantidade = faker.number().numberBetween(10,100);

	
	public ProductsPojo getNewProduct() {
		ProductsPojo produto = new ProductsPojo();
		produto.setNome(nomeProduto);
		produto.setPreco(preco);
		produto.setDescricao(descricao);
		produto.setQuantidade(quantidade);
		return produto;
	}

}
