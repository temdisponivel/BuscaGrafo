//TODO: construir lista de adjacensia ou validar se precisa de uma

package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Classe que representa um grafo.
 * @author matheus
 *
 */
public class Grafo
{
	/**
	 * Classe que representa uma aresta do grafo.
	 * @author matheus
	 *
	 */
	public class Aresta
	{
		public Vertice a = null;
		public Vertice b = null;
	}
	
	/**
	 * Classe que representa um vértice do grafo.
	 * @author matheus
	 *
	 */
	public class Vertice
	{
		public ArrayList<Aresta> arestas = null;
		public int valor = 0;
		public boolean visitado = false;
		
		/**
		 * Cria um vértice com várias arestas.
		 */
		public Vertice(int valor)
		{
			this.valor = valor;
			this.arestas = new ArrayList<Aresta>();  
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return this.hashCode() == obj.hashCode();
		}
		
		@Override
		public int hashCode()
		{
			return valor;
		}
	}
	
	private Vertice _inicio = null;
	private HashMap<Vertice, ArrayList<Vertice>> _listaAdjacensia = null;
	private String _arquivo = null;
	
	/**
	 * Cria um novo grafo baseado na leitura de arquivo.
	 * @param arquivo Nome do arquivo para leitura.
	 */
	public Grafo(String arquivo)
	{
		_listaAdjacensia = new HashMap<Vertice, ArrayList<Vertice>>();
		_arquivo = arquivo;
		this.ControiGrafo();
	}
	
	/**
	 * Contrói os vértices e arestas do grafo.
	 */
	private void ControiGrafo()
	{
		FileReader leitor = null;
		Vertice verticeAtual = null;
		Aresta arestaAtual = null;
		char[] caracteres = null;
		char caracter = '\0';
		char inicio = '\0';
		int quantidade = 0;
		ArrayList<Vertice> vertices;
		
		try
		{
			leitor = new FileReader(new File(_arquivo));
			caracteres = new char[1000];
			vertices = new ArrayList<Vertice>();
			
			for (quantidade = 0; leitor.ready();)
			{
				caracter = (char) leitor.read();
				
				if (caracter == '-' || caracter == '\n' || caracter == '\r')
					continue;
				
				caracteres[quantidade++] = caracter;
			}
			
			inicio = caracteres[0];
			
			for (int i = 1; i < quantidade;)
			{
				//controi o vertice e adiciona na lista
				verticeAtual = new Vertice(caracteres[i++]);
				
				//se já houver o elemento no array, só recupera, senão adiciona
				if (vertices.contains(verticeAtual))
					verticeAtual = vertices.get(vertices.indexOf(verticeAtual));
				else
					vertices.add(verticeAtual);
				
				//controi a aresta e define o vertice que acabamos de criar 
				arestaAtual = new Aresta();
				arestaAtual.a = verticeAtual;
				
				//adiciona a aresta ao vertice a
				verticeAtual.arestas.add(arestaAtual);
				
				//cria o outro vertice e define na aresta
				verticeAtual = new Vertice(caracteres[i++]);
				arestaAtual.b = verticeAtual;
				
				//adiciona a aresta no vértice b
				verticeAtual.arestas.add(arestaAtual);
			}			
			
			//para cada vértice, contrói a lista de adjasencia
			for (int i = 0; i < vertices.size(); i++)
			{
				verticeAtual = vertices.get(i);
				
				if (verticeAtual.valor == inicio)
					_inicio = verticeAtual;
				
				_listaAdjacensia.put(verticeAtual, new ArrayList<Vertice>());
				
				for (int j = 0; j < verticeAtual.arestas.size(); j++)
				{
					_listaAdjacensia.get(verticeAtual).add(verticeAtual.arestas.get(j).b);
				}
			}
			
			System.out.println("");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Define um novo inicio para o grafo. Ponto onde as buscas e percursos começam. Caso o vértice seja inválido, as buscas e percurssos podem não funcionar ou não retornar resultado.
	 * @param inicio Vertice para definir como inicio.
	 */
	public void SetInicio(Vertice inicio)
	{
		_inicio = inicio;
	}
	
	/**
	 * Percorre todo o grafo em profundidade.
	 */
	public void PercursoProfundidade()
	{
		Vertice atual;
		Vertice vizinho;
		
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			atual = entrada.getKey();
			
			System.out.println("ACESSANDO VERTICE");
			System.out.println((char)atual.valor);
			
			System.out.println("PERCORRENDO PROFUNDIDADE");
			for (int i = 0; i < entrada.getValue().size(); i++)
			{
				vizinho = entrada.getValue().get(i);
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);
			}
			
			atual.visitado = true;
		}
		
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			atual = entrada.getKey();
			atual.visitado = false;
		}
	}
	
	/**
	 * Percorre todo o grafo em largura.
	 */
	public void PercursoLargura()
	{
		Vertice atual;
		Vertice vizinho;
		
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			atual = entrada.getKey();
			
			System.out.println("ACESSANDO VERTICE");
			System.out.println((char)atual.valor);
			
			System.out.println("PERCORRENDO VIZINHOS");
			for (int i = 0; i < entrada.getValue().size(); i++)
			{
				vizinho = entrada.getValue().get(i);
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);
			}
			
			atual.visitado = true;
		}
		
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			atual = entrada.getKey();
			atual.visitado = false;
		}
	}
	
	/**
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param destino Vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso não exista o caminho.
	 */
	public ArrayList<Vertice> BuscaLargura(Vertice destino)
	{
		return null;
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param destino Vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso não exista o caminho.
	 */
	public ArrayList<Vertice> BuscaProfundidade(Vertice destino)
	{
		return null;
	}
}
