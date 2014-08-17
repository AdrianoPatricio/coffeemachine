package br.ufpb.dce.aps.coffeemachine.impl;

import static org.mockito.Matchers.anyDouble;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public abstract class Bebidas {

	protected Drink drink;
	protected double anyDouble = anyDouble();
	protected ComponentsFactory factory;

	public abstract void release();	
	
	public Drink getDrink(){
		return this.drink;
	}
}