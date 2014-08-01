package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class MyCoffeeMachine implements CoffeeMachine {

	private Coin coin;
	private Drink drink;
	private int total, indice;
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

	public void cancelWithoutIngredients(){
		Coin[] reverse = Coin.reverse();

		for (Coin r : reverse) {
			for (Coin aux : coins) {
				if (aux == r) {
					factory.getCashBox().release(aux);		
				}
			}
		}
		coins.clear();
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void select(Drink drink) {
		factory.getCupDispenser().contains(1);
		factory.getWaterDispenser().contains(3);	
		if(!this.factory.getCoffeePowderDispenser().contains(200)){
			factory.getDisplay().warn("Out of Coffee Powder");
			cancelWithoutIngredients();
		} 

		else {

			if(drink == drink.BLACK_SUGAR){
				if(!factory.getSugarDispenser().contains(0.1)){
					factory.getDisplay().warn("Out of Sugar");
					factory.getCashBox().release(Coin.halfDollar);
					factory.getDisplay().info("Insert coins and select a drink!");
					return;
				}
			}

			factory.getDisplay().info("Mixing ingredients.");
			factory.getCoffeePowderDispenser().release(200);
			factory.getWaterDispenser().release(3);	

			if(drink == this.drink.BLACK_SUGAR){
				factory.getSugarDispenser().release(200);
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