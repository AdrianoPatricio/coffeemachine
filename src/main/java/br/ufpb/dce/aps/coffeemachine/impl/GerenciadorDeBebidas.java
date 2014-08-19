package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeBebidas {

	private Bebidas bebida;
	private double valor = 35;
	private double valorCaldo = 25;

	public void iniciarBebida(ComponentsFactory factory, Drink drink) {
		if (drink == Drink.BLACK || drink == Drink.BLACK_SUGAR) {
			bebida = new Preto(drink);
		} else if (drink == Drink.WHITE || drink == Drink.WHITE_SUGAR) {
			bebida = new Branco(drink);
		} else {
			bebida = new Bouillon(drink);
			valor = valorCaldo;
		}
	}

	public boolean conferirIngredientes(ComponentsFactory factory, Drink drink) {
		if (bebida.getDrink() == Drink.BLACK || bebida.getDrink() == Drink.BLACK_SUGAR) {
			return (conferirIngredientes(factory, drink, 1, 100, 15, 0, 0));

		} else if (bebida.getDrink() == Drink.WHITE || bebida.getDrink() == Drink.WHITE_SUGAR) {
			return (conferirIngredientes(factory, drink, 1, 80, 15, 20, 0));
		} else {
			return (conferirIngredientes(factory, drink, 1, 100, 0, 0, 10));
		}
	}

	public boolean verificaAcucar(ComponentsFactory factory) {

		if (bebida.getDrink() == Drink.BLACK_SUGAR || bebida.getDrink() == Drink.WHITE_SUGAR) {
			if (!factory.getSugarDispenser().contains(5)) {
				factory.getDisplay().warn(Messages.OUT_OF_SUGAR);
				return false;
			}
		}
		return true;
	}

	public void misturarIngredientes(ComponentsFactory factory, Drink drink) {
		factory.getDisplay().info(Messages.MIXING);
		if (bebida.getDrink() == Drink.BOUILLON) {
			factory.getBouillonDispenser().release(10);
		} 
		else {
			factory.getCoffeePowderDispenser().release(15);
		}
	}

	public void release(ComponentsFactory factory) {
		bebida.release(factory);
		factory.getDrinkDispenser().release(100.0);
		factory.getDisplay().info(Messages.TAKE_DRINK);

	}

	public double getValor() {
		return this.valor;
	}

	public boolean conferirIngredientes(ComponentsFactory factory, Drink drink, int cup, int water, int powder, int cream, int bouillon) {
		if (cup > 0) {
			if (!factory.getCupDispenser().contains(cup)) {
				factory.getDisplay().warn(Messages.OUT_OF_CUP);
				return false;
			}
		}
		if (!factory.getWaterDispenser().contains(water)) {
			factory.getDisplay().warn(Messages.OUT_OF_WATER);
			return false;
		}
		if (powder > 0) {
			if (!factory.getCoffeePowderDispenser().contains(powder)) {
				factory.getDisplay().warn(Messages.OUT_OF_COFFEE_POWDER);
				return false;
			}
		}
		if (bebida.getDrink() == Drink.WHITE || bebida.getDrink() == Drink.WHITE_SUGAR) {
			if (!factory.getCreamerDispenser().contains(cream)) {
				factory.getDisplay().warn(Messages.OUT_OF_CREAMER);
				return false;
			}
		}
		if (bouillon > 0) {
			if (!factory.getBouillonDispenser().contains(bouillon)) {
				return false;
			}
		}
		return true;
	}
}