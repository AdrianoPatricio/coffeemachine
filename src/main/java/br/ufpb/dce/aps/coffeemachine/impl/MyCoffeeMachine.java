package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class MyCoffeeMachine implements CoffeeMachine {

	private int total;
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
		factory.getDisplay().info(
				"Total: US$ " + total / 100 + "." + total % 100);
	}

	public void cancel() {
		if(total==0)
			throw new CoffeeMachineException("Without inserting coins!");
		if (coins.size() > 0) {
			Coin[] reverse = Coin.reverse();
			this.factory.getDisplay().warn("Cancelling drink. Please, get your coins.");
			for (Coin r : reverse) {
				for (Coin aux : coins) {
					if (aux == r) {
						factory.getCashBox().release(aux);
					}
				}
			}
			coins.clear();
		}
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void select(Drink drink) {
		if(!factory.getCupDispenser().contains(1)){
			factory.getDisplay().warn("Out of Cup");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);
			factory.getDisplay().info("Insert coins and select a drink!");
			return;
		}

		if(!factory.getWaterDispenser().contains(0.1)){
			factory.getDisplay().warn("Out of Water");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);
			factory.getDisplay().info("Insert coins and select a drink!");
			return;
		}

		if(!this.factory.getCoffeePowderDispenser().contains(0.1)){
			factory.getDisplay().warn("Out of Coffee Powder");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);
			factory.getDisplay().info("Insert coins and select a drink!");
		} 

		else {

			if(drink == Drink.BLACK_SUGAR){
				if(!factory.getSugarDispenser().contains(0.1)){
					factory.getDisplay().warn("Out of Sugar");
					factory.getCashBox().release(Coin.halfDollar);
					factory.getDisplay().info("Insert coins and select a drink!");
					return;
				}
			}
			if(drink == Drink.WHITE){
				this.factory.getCreamerDispenser().contains(0.1);
			}

			factory.getDisplay().info("Mixing ingredients.");
			factory.getCoffeePowderDispenser().release(0.1);
			factory.getWaterDispenser().release(3);	

			if(drink == Drink.BLACK_SUGAR){
				factory.getSugarDispenser().release(0.1);
			}
			if (drink == Drink.WHITE){
				this.factory.getCreamerDispenser().release(0.1);
			}

			factory.getDisplay().info("Releasing drink.");
			factory.getCupDispenser().release(1);
			factory.getDrinkDispenser().release(1);

			factory.getDisplay().info("Please, take your drink.");
			factory.getDisplay().info("Insert coins and select a drink!");

			coins.clear();
		}
	}
}