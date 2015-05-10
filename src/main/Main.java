package main;

import java.util.ArrayList;

import main.Grafo.Vertice;

public class Main
{
	private static Grafo g;

	public static void main(String[] args) throws Exception
	{
		g = new Grafo("C:\\Users\\matheus\\Documents\\grafo.txt");

		System.out.println("PROFUNDIDADE:");
		g.PercursoProfundidade();

		System.out.println("LARGURA:");
		g.PercursoLargura();
		
		ArrayList<Vertice> caminho = g.BuscaLargura('9');
		
		System.out.println("CAMINHO:");
		
		for (int i = 0; i < caminho.size(); i++)
		{
			System.out.println(caminho.get(i));
		}
	}
}
