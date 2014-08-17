package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class Branco extends Bebidas {

	public Branco(Drink drink, ComponentsFactory factory) {
		this.factory = factory;
		if (drink == Drink.WHITE) {
			this.drink = Drink.WHITE;
		} else {
			this.drink = Drink.WHITE_SUGAR;
		}
	}

	public void release() {
		factory.getCreamerDispenser().release(super.anyDouble);
		if (drink == Drink.WHITE_SUGAR) {
			factory.getSugarDispenser().release(super.anyDouble);
		}
	}
}