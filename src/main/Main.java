package main;

public class Main
{
	private static Grafo g;

	public static void main(String[] args)
	{
		g = new Grafo("C:\\Users\\matheus\\Documents\\grafo.txt");

		System.out.println("PROFUNDIDADE:");
		g.PercursoProfundidade();

		System.out.println("LARGURA:");
		g.PercursoLargura();
	}
}
