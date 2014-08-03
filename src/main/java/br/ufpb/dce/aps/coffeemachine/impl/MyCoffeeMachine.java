package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;
import java.util.List;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;

public class MyCoffeeMachine implements CoffeeMachine {

	private boolean temTroco;
	private ComponentsFactory factory;
	private int total, cents, dolars, troco;
	private List<Coin> coins;


	public MyCoffeeMachine(ComponentsFactory factory) {
		temTroco = false;
		this.factory = factory;
		coins = new ArrayList<Coin>();
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void insertCoin(Coin dime) {
		if(dime==null){
			throw new CoffeeMachineException("Invalid coin!");
		}
		total += dime.getValue();
		coins.add(dime);
		cents += dime.getValue() % 100;
		dolars += dime.getValue() / 100; 
		
		factory.getDisplay().info(
				"Total: US$ " + total / 100 + "." + total % 100);
		
		troco = (cents + dolars) - 35;
		
		if(troco>0){
			temTroco = true;
		}
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

	private void planCoins(int troco){

		Coin[] inverso = Coin.reverse();
		for (Coin r : inverso) {
			if(r.getValue() <= troco){
				factory.getCashBox().count(r);
				troco -= r.getValue();
			}
		}

	}

	private void releaseCoins(int troco){

		Coin[] inverso = Coin.reverse();
		for (Coin r : inverso) {
			if(r.getValue() <= troco){
				factory.getCashBox().release(r);
				troco -= r.getValue(); 
			}
		}
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
			if(drink == Drink.WHITE){
				this.factory.getCreamerDispenser().contains(0.1);
			}

			if (drink == Drink.WHITE_SUGAR) {
				factory.getCreamerDispenser().contains(0.1);
				factory.getSugarDispenser().contains(0.1);

			}

			if(drink == Drink.BLACK_SUGAR){
				if(!factory.getSugarDispenser().contains(0.1)){
					factory.getDisplay().warn("Out of Sugar");
					factory.getCashBox().release(Coin.halfDollar);
					factory.getDisplay().info("Insert coins and select a drink!");
					return;
				}
			}

			if(temTroco){
				planCoins(troco);
			}

			factory.getDisplay().info("Mixing ingredients.");
			factory.getCoffeePowderDispenser().release(0.1);
			factory.getWaterDispenser().release(3);	

			if (drink == Drink.WHITE){
				this.factory.getCreamerDispenser().release(0.1);
			}

			if (drink == Drink.WHITE_SUGAR) {
				factory.getCreamerDispenser().release(0.1);
				factory.getSugarDispenser().release(0.1);
			}

			if(drink == Drink.BLACK_SUGAR){
				factory.getSugarDispenser().release(0.1);
			}

			factory.getDisplay().info("Releasing drink.");
			factory.getCupDispenser().release(1);
			factory.getDrinkDispenser().release(0.1);

			factory.getDisplay().info("Please, take your drink.");

			if(temTroco){
				releaseCoins(troco);
			}

			factory.getDisplay().info("Insert coins and select a drink!");
			coins.clear();
		}
	}
}