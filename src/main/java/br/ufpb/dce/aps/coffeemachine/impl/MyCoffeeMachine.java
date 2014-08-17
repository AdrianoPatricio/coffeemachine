package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class MyCoffeeMachine implements CoffeeMachine {

	private int total;
	private ComponentsFactory factory;
	private ArrayList<Coin> spartacus = new ArrayList<Coin>();
	private GerenciadorDeBebidas gerenteBebidas;
	private Coin[] reverso = Coin.reverse();

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		gerenteBebidas = new GerenciadorDeBebidas(this.factory);
		factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public void insertCoin(Coin dime) throws CoffeeMachineException {
		try {
			total += dime.getValue();
			spartacus.add(dime);
			factory.getDisplay().info("Total: US$ " + this.total / 100 + "." + this.total % 100);
		} catch (NullPointerException e) {
			throw new CoffeeMachineException("moeda invalida");
		}
	}

	public void cancel() throws CoffeeMachineException {
		if (this.total == 0) {
			throw new CoffeeMachineException("sem moedas inseridas");
		}
		this.cancel(true);
		
	}
		
	public void cancel(Boolean confirm) throws CoffeeMachineException{
		if (this.spartacus.size() > 0) {
			if(confirm){
				this.factory.getDisplay().warn(Messages.CANCEL);
			}
			for (Coin re : this.reverso) {
				for (Coin aux : this.spartacus) {
					if (aux == re) {
						this.factory.getCashBox().release(aux);
					}
				}
			}
			this.total = 0;
			this.spartacus.clear();
		}
		this.factory.getDisplay().info(Messages.INSERT_COINS);
	}
	
	public boolean calculaTroco (double troco){
		for(Coin re : this.reverso){
			if(re.getValue() <= troco && this.factory.getCashBox().count (re) > 0){
					troco -= re.getValue();
			}
		}		
		return (troco == 0);
	}
	
	public void liberarTroco (double troco){
		for(Coin re : this.reverso){
			while(re.getValue() <= troco ){
				this.factory.getCashBox().release (re);
				troco -= re.getValue(); 
			}
		}
	}
	
	public void select(Drink drink) {
		
		if(this.total < this.gerenteBebidas.getValor() || this.total == 0){
			this.factory.getDisplay().warn(Messages.NO_ENOUGHT_MONEY);
			this.cancel(false);
			return;
		}

		this.gerenteBebidas.iniciarBebida(drink);
		
		if (!this.gerenteBebidas.conferirIngredientes()) {
			this.cancel(false);
			return;
		}
		if(!this.gerenteBebidas.verificaAcucar()){ 
			this.cancel(false);
			return;
		} 
		if(this.total % this.gerenteBebidas.getValor() != 0 && this.total > this.gerenteBebidas.getValor()){
			if(!this.calculaTroco(this.total - this.gerenteBebidas.getValor())){
				this.factory.getDisplay().warn(Messages.NO_ENOUGHT_CHANGE);
				this.cancel(false);
				return;
			}
		}
				
		this.gerenteBebidas.misturarIngredientes();
		this.gerenteBebidas.release();
		
		if(this.total % this.gerenteBebidas.getValor() != 0 && this.total > this.gerenteBebidas.getValor()){
			this.liberarTroco(this.total - this.gerenteBebidas.getValor());
		}
		this.factory.getDisplay().info(Messages.INSERT_COINS);
		this.spartacus.clear();
	}
}