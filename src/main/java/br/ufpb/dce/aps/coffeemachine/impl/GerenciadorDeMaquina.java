package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeMaquina {

	private GerenciadorDeBebidas gerenteDeBebidas = new GerenciadorDeBebidas();
	private static String modo = "";
	private int cracha = 0;

	public void iniciarPedido(ComponentsFactory factory, GerenciadorDeCaixa gerenteFinanceiro, Drink drink) {
		if (modo.equals("cracha")){
			iniciarPedidoComCracha(factory, drink);
		}else{
			iniciarPedidoComMoedas(factory, gerenteFinanceiro, drink);
		}
	}

	public void iniciarPedidoComMoedas(ComponentsFactory factory, GerenciadorDeCaixa gerenteFinanceiro, Drink drink){
		
		gerenteDeBebidas.iniciarBebida(factory, drink);
		if (!gerenteFinanceiro.conferirDinheiro(factory, gerenteDeBebidas.getValor())) {
			return;
		}

		if (!gerenteDeBebidas.conferirIngredientes(factory, drink)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}
		if (!this.gerenteDeBebidas.verificaAcucar(factory)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}

		if (!gerenteFinanceiro.verificarTroco(factory, gerenteDeBebidas.getValor())) {
			return;
		}

		gerenteDeBebidas.misturarIngredientes(factory, drink);
		gerenteDeBebidas.release(factory);

		if (gerenteFinanceiro.getTotal() >= gerenteDeBebidas.getValor()) {
			gerenteFinanceiro.liberarTroco(factory, gerenteDeBebidas.getValor());
		}

		factory.getDisplay().info(Messages.INSERT_COINS);
		GerenciadorDeMaquina.setModo ("");
		gerenteFinanceiro.limparMoedas();
	}

	public void iniciarPedidoComCracha(ComponentsFactory factory, Drink drink){
		
		gerenteDeBebidas.iniciarBebida(factory, drink);

		if (!gerenteDeBebidas.conferirIngredientes(factory, drink)) {
			return;
		}
		if (!gerenteDeBebidas.verificaAcucar(factory)) {
			return;
		}
		factory.getPayrollSystem().debit(gerenteDeBebidas.getValor(), cracha);

		this.gerenteDeBebidas.misturarIngredientes(factory, drink);
		this.gerenteDeBebidas.release(factory);

		factory.getDisplay().info(Messages.INSERT_COINS);
		GerenciadorDeMaquina.setModo (" ");
	}

	public void iniciarComMoedas(ComponentsFactory factory) {
		factory.getDisplay().info(Messages.INSERT_COINS);
		GerenciadorDeMaquina.setModo("moedas");
	}

	public void iniciarComCracha(ComponentsFactory factory, GerenciadorDeCaixa gerenteAuxiliar, int cracha) {
		
		if(gerenteAuxiliar.getTotal()>0){
			factory.getDisplay().warn(Messages.CAN_NOT_READ_BADGE);
			return;
		}
		else{
			factory.getDisplay().info(Messages.BADGE_READ);
			this.cracha = cracha;
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