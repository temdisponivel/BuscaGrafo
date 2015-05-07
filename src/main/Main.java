package main;

public class Main
{
	private static Grafo g;

	public static void main(String[] args)
	{
		g = new Grafo("C:\\Users\\matheus\\Documents\\grafo.txt");
		g.PercursoLargura();
	}
}
