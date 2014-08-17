package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class Preto extends Bebidas {

	public Preto (Drink drink, ComponentsFactory factory) {
		this.factory = factory;
		if (drink == Drink.BLACK) {
			this.drink = Drink.BLACK;
		} else {
			this.drink = Drink.BLACK_SUGAR;
		}
	}

	public void release() {
		factory.getWaterDispenser().release(100);
		if (drink == Drink.BLACK_SUGAR) {
			this.factory.getSugarDispenser().release(5);
		}
	}
}