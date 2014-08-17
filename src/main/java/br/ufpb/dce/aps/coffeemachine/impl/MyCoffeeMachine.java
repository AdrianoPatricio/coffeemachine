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
	private int inteiro = 0;
	private int centavos = 0;
	private ComponentsFactory factory;
	private ArrayList<Coin> spartacus = new ArrayList<Coin>();
	private GerenciadorDeBebidas gerenteBebidas;
	private Coin[] reverso = Coin.reverse();

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		this.gerenteBebidas = new GerenciadorDeBebidas(this.factory);
		this.factory.getDisplay().info(Messages.INSERT_COINS);
	}

	public void insertCoin(Coin dime) throws CoffeeMachineException {
		try {
			spartacus.add(dime);
			total += dime.getValue();
			inteiro = total / 100;
			centavos = total % 100;
			factory.getDisplay().info("Total: US$ " + inteiro + "." + centavos);
		} catch (NullPointerException e) {
			throw new CoffeeMachineException("moeda invalida");
		}
	}

	public void cancel() throws CoffeeMachineException {
		if (this.total == 0) {
 			throw new CoffeeMachineException("sem moedas inseridas");
 		}
		cancel(true);
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
			total = 0;
			spartacus.clear();
		}
		factory.getDisplay().info(Messages.INSERT_COINS);
	}
	
	public void calculaTroco (double troco){
		double aux = troco;
		for(Coin re : reverso){
			while(re.getValue() <= aux ){
				factory.getCashBox().count (re);
				aux -= re.getValue(); 
			}
		}
	}
	
	public void liberaTroco (double troco){
		for(Coin re : reverso){
			while(re.getValue() <= troco ){
				factory.getCashBox().release (re);
				troco -= re.getValue(); 
			}
		}
	}
	
	public void select(Drink drink) {

		if(total < gerenteBebidas.getValor() || total == 0){
			factory.getDisplay().warn(Messages.NO_ENOUGHT_MONEY);
			cancel(false);
			return;
		}
		
		gerenteBebidas.iniciarBebida(drink);
		
		if (!gerenteBebidas.conferirIngredientes()) {
			cancel(false);
			return;
		}
		if(!gerenteBebidas.verificaAcucar()){ 
			cancel(false);
			return;
		} 
		if(total % gerenteBebidas.getValor() != 0 && total > gerenteBebidas.getValor()){
			calculaTroco(total - gerenteBebidas.getValor());
		}
				
		gerenteBebidas.misturarIngredientes();
		gerenteBebidas.release();
		
		if(total % gerenteBebidas.getValor() != 0 && total > gerenteBebidas.getValor()){
			liberaTroco(total - gerenteBebidas.getValor());
		}
		factory.getDisplay().info(Messages.INSERT_COINS);
		spartacus.clear();
	}
}