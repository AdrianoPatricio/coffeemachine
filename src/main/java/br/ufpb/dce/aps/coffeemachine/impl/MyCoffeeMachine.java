package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;

public class MyCoffeeMachine implements CoffeeMachine {

	private int total;
	private ComponentsFactory factory;
	

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void insertCoin(Coin dime) {
		if(dime==null){
			throw new CoffeeMachineException("Invalid coin");
		}
		total += dime.getValue();
		this.factory.getDisplay().info(
				"Total: US$ " + this.total / 100 + "." + this.total % 100);
	}

}
