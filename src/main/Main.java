package main;

import java.util.ArrayList;

import main.Grafo.Vertice;

/**
 * Matheus de Almeida Aguiar Candido
 * 3º Semestre Jogos Digitais
 * @author matheus
 *
 */
public class Main
{
	private static Grafo g;

	public static void main(String[] args) throws Exception
	{
		/*	Contrutor com o arquivo contento a representação do gráfico
			Deve estar no modelo:
			0
			1-2
			3-4
			
			Onde o primeiro número é o inicio do grafo, os outros são os vértices ligados por arestas.
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
