package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public abstract class Bebidas {

	protected Drink drink;
	
	public abstract void release(ComponentsFactory factory);	
	
	public Drink getDrink(){
		return this.drink;
	}
}