package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class MyCoffeeMachine implements CoffeeMachine {

	private int total, indice;
	private Coin coin;
	private ComponentsFactory factory;
	private ArrayList<Coin> coins = new ArrayList<Coin>();


	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void insertCoin(Coin dime) {
		if(dime==null){
			throw new CoffeeMachineException("Invalid coin!");
		}
		total += dime.getValue();
		coins.add(dime);
		indice ++;
		this.factory.getDisplay().info(
				"Total: US$ " + this.total / 100 + "." + this.total % 100);
	}

	public void cancel() {
		if(total==0)
			throw new CoffeeMachineException("Without inserting coins!");
		if (this.coins.size() > 0) {
			Coin[] reverse = Coin.reverse();
			this.factory.getDisplay().warn("Cancelling drink. Please, get your coins.");
			for (Coin r : reverse) {
				for (Coin aux : this.coins) {
					if (aux == r) {
						this.factory.getCashBox().release(aux);
					}
				}
			}
			this.factory.getDisplay().info("Insert coins and select a drink!");
		}
	}

	public void select(Drink drink) {
		factory.getCupDispenser().contains(1);
		factory.getWaterDispenser().contains(3);	
		factory.getCoffeePowderDispenser().contains(200);
		 
		factory.getDisplay().info("Mixing ingredients.");
		factory.getCoffeePowderDispenser().release(200);
		factory.getWaterDispenser().release(3);	
		 
		factory.getDisplay().info("Releasing drink.");
		factory.getCupDispenser().release(1);
		factory.getDrinkDispenser().release(1);
		
		factory.getDisplay().info("Please, take your drink.");
		factory.getDisplay().info("Insert coins and select a drink!");
	}
}