package br.ufpb.dce.aps.coffeemachine.impl;

import static org.mockito.Matchers.anyDouble;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeBebidas {
	
	private ComponentsFactory factory;
	private Bebidas bebida;
	private double valor = 35;
	
	public GerenciadorDeBebidas(ComponentsFactory factory){
		this.factory = factory;
	}
	
	public void iniciarBebida(Drink drink){
		if (drink == Drink.BLACK ||drink == Drink.BLACK_SUGAR){
			bebida = new Preto (drink, this.factory);
		}
		else{
			bebida =  new Branco (drink, this.factory);
		}
	}
	
	public boolean conferirIngredientes() {
		if (!factory.getCupDispenser().contains(1)) {
			factory.getDisplay().warn(Messages.OUT_OF_CUP);
			return false;
		}
		else if (!factory.getWaterDispenser().contains(anyDouble())) {
			factory.getDisplay().warn(Messages.OUT_OF_WATER);
			return false;
		}
		else if (!factory.getCoffeePowderDispenser().contains(anyDouble())) {
			factory.getDisplay().warn(Messages.OUT_OF_COFFEE_POWDER);
			return false;
		}
		else if (bebida.getDrink() == Drink.WHITE || bebida.getDrink() == Drink.WHITE_SUGAR){
			if (!factory.getCreamerDispenser().contains(anyDouble())){
				return false;
			}
		}
			return true;
	}
	
	public boolean verificaAcucar(){
		if(bebida.getDrink() == Drink.BLACK_SUGAR || bebida.getDrink() == Drink.WHITE_SUGAR){
			if (!this.factory.getSugarDispenser().contains(anyDouble())) {
				this.factory.getDisplay().warn(Messages.OUT_OF_SUGAR);
				return false;
			}
		}		
		return true;
	}	
	
	public void misturarIngredientes (){
		factory.getDisplay().info(Messages.MIXING);
		factory.getCoffeePowderDispenser().release(anyDouble());
		factory.getWaterDispenser().release(anyDouble());
	}
	
	public void release(){
		bebida.release();
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(1);
		factory.getDrinkDispenser().release(1);
		factory.getDisplay().info(Messages.TAKE_DRINK);
		
	}	

	public double getValor(){
		return this.valor;
	}
}