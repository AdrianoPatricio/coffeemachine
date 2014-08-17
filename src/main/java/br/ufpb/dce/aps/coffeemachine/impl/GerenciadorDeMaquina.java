package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeMaquina {
	
	private GerenciadorDeBebidas gerenteDeBebidas = new GerenciadorDeBebidas();
	
public void iniciarPedido(ComponentsFactory factory, GerenciadorDeCaixa gerenciadorDeCaixa, Drink drink) {
		
		if(!gerenciadorDeCaixa.conferirDinheiro(factory, gerenteDeBebidas.getValor())){
			return;
		}
		
		gerenteDeBebidas.iniciarBebida(drink,factory);

		if (!gerenteDeBebidas.conferirIngredientes(factory)) {
			gerenciadorDeCaixa.liberarMoedas(factory, false);
			return;
		}
		if (!gerenteDeBebidas.verificaAcucar(factory)) {
			gerenciadorDeCaixa.liberarMoedas(factory, false);
			return;
		}
		
		if(!gerenciadorDeCaixa.verificarTroco(factory, gerenteDeBebidas.getValor())){
			return;
		}

		gerenteDeBebidas.misturarIngredientes(factory);
		gerenteDeBebidas.release(factory);
		
		if( gerenciadorDeCaixa.getTotal() % gerenteDeBebidas.getValor() != 0 && gerenciadorDeCaixa.getTotal() > gerenteDeBebidas.getValor()) {
			gerenciadorDeCaixa.liberaTroco(factory, gerenteDeBebidas.getValor());
		}
		
		factory.getDisplay().info(Messages.INSERT_COINS);
		
		gerenciadorDeCaixa.zerarMoedas();
	}

}