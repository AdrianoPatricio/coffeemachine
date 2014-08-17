package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeBebidas {

	private Bebidas bebida;
	private double valor = 35;

	public void iniciarBebida(Drink drink, ComponentsFactory factory){
		if (drink == Drink.BLACK ||drink == Drink.BLACK_SUGAR){
			bebida = new Preto (drink, factory);
		}
		else{
			bebida =  new Branco (drink, factory);
		}
	}

	public boolean conferirIngredientes(Drink drink, ComponentsFactory factory) {
		if (!factory.getCupDispenser().contains(1)) {
			factory.getDisplay().warn(Messages.OUT_OF_CUP);
			return false;
		}
		if(drink == Drink.BLACK || drink == Drink.BLACK_SUGAR){
			if (!factory.getWaterDispenser().contains(100)) {
				factory.getDisplay().warn(Messages.OUT_OF_WATER);
				return false;
			}
		}
		else{
			if(!factory.getWaterDispenser().contains(80)) {
				factory.getDisplay().warn(Messages.OUT_OF_WATER);
				return false;
			}
		}
		if (!factory.getCoffeePowderDispenser().contains(15)) {
			factory.getDisplay().warn(Messages.OUT_OF_COFFEE_POWDER);
			return false;
		}
		else if (bebida.getDrink() == Drink.WHITE || bebida.getDrink() == Drink.WHITE_SUGAR){
			if (!factory.getCreamerDispenser().contains(20)){
				factory.getDisplay().warn(Messages.OUT_OF_CREAMER);
				return false;
			}
		}
		return true;
	}

	public boolean verificaAcucar(ComponentsFactory factory){
		if(bebida.getDrink() == Drink.BLACK_SUGAR || bebida.getDrink() == Drink.WHITE_SUGAR){
			if (!factory.getSugarDispenser().contains(5)) {
				factory.getDisplay().warn(Messages.OUT_OF_SUGAR);
				return false;
			}
		}		
		return true;
	}	

	public void misturarIngredientes (ComponentsFactory factory){
		factory.getDisplay().info(Messages.MIXING);
		factory.getCoffeePowderDispenser().release(15);
	}

	public void release(ComponentsFactory factory){
		bebida.release();
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(1);
		factory.getDrinkDispenser().release(100.0);
		factory.getDisplay().info(Messages.TAKE_DRINK);

	}	

	public double getValor(){
		return this.valor;
	}
}