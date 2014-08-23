package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class MyCoffeeMachine implements CoffeeMachine {

	private ComponentsFactory factory;
	private GerenciadorDeCaixa gerenciadorDeCaixa;
	private GerenciadorDeMaquina gerenciadorDeMaquina;

	public void insertCoin(Coin dime) {
		gerenciadorDeCaixa.inserirMoedas(factory, dime);
	}

	public void cancel() throws CoffeeMachineException {
		gerenciadorDeCaixa.cancelar(factory);
		
	}
		
	public void select(Drink drink) {
		gerenciadorDeMaquina.iniciarPedido(factory, gerenciadorDeCaixa, drink);
	}

	public void setFactory(ComponentsFactory factory) {
		this.factory = factory;
		gerenciadorDeCaixa = new GerenciadorDeCaixa();
		gerenciadorDeMaquina = new GerenciadorDeMaquina();
		factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public void readBadge(int badgeCode) {
		factory.getDisplay().info(Messages.BADGE_READ);
		gerenciadorDeCaixa.setModo("cracha");
	}
}