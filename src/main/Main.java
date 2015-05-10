package main;

import java.util.ArrayList;

import main.Grafo.Vertice;

/**
 * Matheus de Almeida Aguiar Candido
 * 3� Semestre Jogos Digitais
 * @author matheus
 *
 */
public class Main
{
	private static Grafo g;

	public static void main(String[] args) throws Exception
	{
		/*	Contrutor com o arquivo contento a representa��o do gr�fico
			Deve estar no modelo:
			0
			1-2
			3-4
			
			Onde o primeiro n�mero � o inicio do grafo, os outros s�o os v�rtices ligados por arestas.
		*/
		g = new Grafo("grafo.txt");

		System.out.println("PROFUNDIDADE:");
		g.PercursoProfundidade();

		System.out.println("LARGURA:");
		g.PercursoLargura();
		
		ArrayList<Vertice> caminho = g.BuscaProfundidade('6');
		
		System.out.println("CAMINHO:");
		
		for (int i = 0; i < caminho.size(); i++)
		{
			System.out.println(caminho.get(i));
		}
	}
}
