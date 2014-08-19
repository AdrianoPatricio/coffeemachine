package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeMaquina {

	private GerenciadorDeBebidas gerenciadorDeBebidas = new GerenciadorDeBebidas();

	public void iniciarPedido(ComponentsFactory factory, GerenciadorDeCaixa gerenciadorDeCaixa, Drink drink) {

		this.gerenciadorDeBebidas.iniciarBebida(factory, drink);
		
		if (!gerenciadorDeCaixa.conferirDinheiro(factory,
				gerenciadorDeBebidas.getValor())) {
			return;
		}

		if (!gerenciadorDeBebidas.conferirIngredientes(factory,drink)) {
			gerenciadorDeCaixa.liberaMoedas(factory, false);
			return;
		}
		if (!gerenciadorDeBebidas.verificaAcucar(factory)) {
			gerenciadorDeCaixa.liberaMoedas(factory, false);
			return;
		}
		
		if (!gerenciadorDeCaixa.verificarTroco(factory,
				gerenciadorDeBebidas.getValor())) {
			return;
		}
		
		gerenciadorDeBebidas.misturarIngredientes(factory, drink);
		gerenciadorDeBebidas.release(factory);

		if (gerenciadorDeCaixa.getTotal() >= gerenciadorDeBebidas.getValor()) {
			gerenciadorDeCaixa.liberarTroco(factory, gerenciadorDeBebidas.getValor());}

		factory.getDisplay().info(Messages.INSERT_COINS);
		gerenciadorDeCaixa.limparMoedas();
	}
	
	/*public void apresentarMensagemInicial(ComponentsFactory factory){
		factory.getDisplay().info(Messages.INSERT_COINS);
	}*/

}