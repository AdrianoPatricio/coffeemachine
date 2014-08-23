package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeMaquina {

	private GerenciadorDeBebidas gerenteDeBebidas = new GerenciadorDeBebidas();
	private static String modo = "";
	
	public void iniciarPedidoDeBebida(ComponentsFactory factory, GerenciadorDeCaixa gerenteDeCaixa, Drink drink) {
		gerenteDeBebidas.iniciarBebida(factory, drink);
		
		if (!gerenteDeCaixa.conferirDinheiro(factory, gerenteDeBebidas.getValor())) {
			return;
		}

		if (!gerenteDeBebidas.conferirIngredientes(factory, drink)) {
			gerenteDeCaixa.liberarMoedas(factory, false);
			return;
		}
		if (!gerenteDeBebidas.verificaAcucar(factory)) {
			gerenteDeCaixa.liberarMoedas(factory, false);
			return;
		}

		if (!gerenteDeCaixa.verificarTroco(factory,gerenteDeBebidas.getValor())) {
			return;
		}
	

		gerenteDeBebidas.misturarIngredientes(factory, drink);
		gerenteDeBebidas.release(factory);

		if (gerenteDeCaixa.getTotal() >= gerenteDeBebidas.getValor()) {
			gerenteDeCaixa.liberarTroco(factory, gerenteDeBebidas.getValor());
		}

		factory.getDisplay().info(Messages.INSERT_COINS);
		GerenciadorDeMaquina.setModo (" ");
		gerenteDeCaixa.limparMoedas();
		
	}

	public void iniciarComMoedas(ComponentsFactory factory) {
			factory.getDisplay().info(Messages.INSERT_COINS);
			GerenciadorDeMaquina.setModo("moedas");
	}
	
	public void iniciarComCracha(ComponentsFactory factory, GerenciadorDeCaixa gerenteDeCaixa, int cracha) {
		if(gerenteDeCaixa.getTotal()>0){
			factory.getDisplay().warn(Messages.CAN_NOT_READ_BADGE);
			return;
		}
		else{
			factory.getDisplay().info(Messages.BADGE_READ);
			GerenciadorDeMaquina.setModo("cracha");
		}
	}
		
	
	public static void setModo(String novoModo) {
		modo = novoModo;
	}
	
	public String getModo(){
		return modo;
	}

}