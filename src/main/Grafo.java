//TODO: construir lista de adjacensia ou validar se precisa de uma

package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * Classe que representa um grafo.
 * @author matheus
 *
 */
public class Grafo
{	
	/**
	 * Classe que representa um vértice do grafo.
	 * @author matheus
	 *
	 */
	public static class Vertice
	{
		static public ArrayList<Vertice> verticesGrafo = null;
		public char valor = 0;
		public boolean visitado = false;
		
		/**
		 * Cria um vértice com várias arestas.
		 */
		public Vertice(char valor)
		{
			this.valor = valor;
			
			if (verticesGrafo == null)
				verticesGrafo = new ArrayList<Vertice>();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == null)
				return false;
			
			return this.hashCode() == obj.hashCode();
		}
		
		@Override
		public int hashCode()
		{
			return valor;
		}
		
		@Override
		public String toString()
		{
			return String.valueOf(valor);
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
		Vertice verticeA = null;
		Vertice verticeB = null;
		char[] caracteres = null;
		char caracter = '\0';
		int quantidade = 0;
		
		try
		{
			leitor = new FileReader(new File(_arquivo));
			caracteres = new char[1000];
			
			for (quantidade = 0; leitor.ready();)
			{
				caracter = (char) leitor.read();
				
				if (caracter == '-' || caracter == '\n' || caracter == '\r')
					continue;
				
				caracteres[quantidade++] = caracter;
			}
			
			for (int i = 0; i < quantidade;)
			{
				//controi o vertice e adiciona na lista
				verticeA = new Vertice(caracteres[i++]);
				
				//consistencia de referencia
				if (Vertice.verticesGrafo.contains(verticeA))
					verticeA = Vertice.verticesGrafo.get(Vertice.verticesGrafo.indexOf(verticeA)); 
				else
					Vertice.verticesGrafo.add(verticeA);
				
				if (i == 1)
				{
					_inicio = verticeA;
					continue;
				}
				
				verticeB = new Vertice(caracteres[i++]);
				
				//consistencia de referencia
				if (Vertice.verticesGrafo.contains(verticeB))
					verticeB = Vertice.verticesGrafo.get(Vertice.verticesGrafo.indexOf(verticeB));
				else
					Vertice.verticesGrafo.add(verticeB);
				
				//se já houver o elemento no array, só recupera, senão adiciona
				if (!_listaAdjacensia.containsKey(verticeA))
					_listaAdjacensia.put(verticeA, new ArrayList<Vertice>());
				
				if (!_listaAdjacensia.containsKey(verticeB))
					_listaAdjacensia.put(verticeB, new ArrayList<Vertice>());
					
				_listaAdjacensia.get(verticeA).add(verticeB);
				_listaAdjacensia.get(verticeB).add(verticeA);
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
		this.PercorreProfundidade(_inicio, null);
		this.LimpaVisitas();
	}
	
	/**
	 * Percorre todo o grafo em largura.
	 */
	public void PercursoLargura()
	{
		this.BuscaLargura(null);
		this.LimpaVisitas();
	}
	
	/**
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso não exista o caminho.
	 */
	public ArrayList<Vertice> BuscaLargura(char valorDestino)
	{
		ArrayList<Vertice> retorno = BuscaLargura(new Vertice(valorDestino));
		
		if (retorno == null)
			System.out.println("Não existe caminho");
		
		return retorno;
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso o caminho não exista o caminho.
	 */
	public ArrayList<Vertice> BuscaProfundidade(char valorDestino)
	{
		ArrayList<Vertice> retorno = PercorreProfundidade(new Vertice(valorDestino));
		
		if (retorno == null)
			System.out.println("Não existe caminho");
		
		return retorno;
		
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @param atual Vértice atual para recursão.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso o caminho não exista o caminho.
	 */
	private ArrayList<Vertice> PercorreProfundidade(Vertice atual, Vertice destino)
	{
		ArrayList<Vertice> caminho = null;
		
		if (atual.equals(destino))
		{
			caminho = new ArrayList<Vertice>();
			caminho.add(atual);
			return caminho;
		}
		
		//pega os vizinhos do vértice atual
		Iterator<Vertice> iterador = _listaAdjacensia.get(atual).iterator();
		
		//valida se já foi visto para poder acessar
		if (atual.visitado)
			return null;
		else
			atual.visitado = true;
		
		System.out.println("ACESSANDO:");
		System.out.println((char)atual.valor);
		
		System.out.println("VIZINHO NÃO ACESSADO:");
		
		//para cada vizinho, chama recursao
		while (iterador.hasNext())
		{
			Vertice vizinho = iterador.next();
			
			if (vizinho.visitado)
				continue;
			
			System.out.println((char)vizinho.valor);
			
			if ((caminho = this.PercorreProfundidade(vizinho, destino)) != null)
			{
				caminho.add(0, atual);
				return caminho;
			}
		}
		
		return null;
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso o caminho não exista o caminho.
	 */
	private ArrayList<Vertice> PercorreProfundidade(Vertice destino)
	{
		
		Vertice atual = _inicio;
		Stack<Vertice> pilha = new Stack<Vertice>();
		ArrayList<Vertice> caminho = null;
		
		//adiciona na pilha
		pilha.push(atual);
		
		//enquanto a pilha nao estiver vazia
		while (!pilha.empty())
		{
			if (pilha.contains(destino))
			{
				caminho = new ArrayList<Vertice>();
				caminho.addAll(pilha);
				return caminho;
			}
			
			//desempinha
			Iterator<Vertice> iterador = _listaAdjacensia.get((atual = pilha.pop())).iterator();
			
			//valida se já foi visitado para poder acessar
			if (atual.visitado)
				continue;
			else
				atual.visitado = true;
			
			System.out.println("ACESSANDO:");
			System.out.println((char)atual.valor);
			
			System.out.println("VIZINHO NÃO ACESSADO:");
			//para cada vizinho, adiciona na pilha
			while (iterador.hasNext())
			{
				Vertice vizinho = iterador.next();
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);

				pilha.push(vizinho);
			}
		}
		
		return null;
	}
	
	/**
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @param atual Vértice atual para recursão.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso o caminho não exista o caminho.
	 */
	private ArrayList<Vertice> BuscaLargura(Vertice destino)
	{
		ArrayList<Vertice> caminho = new ArrayList<Vertice>();;
		LinkedList<Vertice> listaVertices = new LinkedList<Vertice>();
		Vertice atual = _inicio;
		Vertice vizinho;
		
		//adiciona na fila
		listaVertices.addLast(atual);
		
		//enquanto a fila não estiver vazia
		while (!listaVertices.isEmpty())
		{
			//desenfila
			atual = listaVertices.removeFirst();
			
			//valida se já foi visitado para poder acessar
			if (atual.visitado)
				continue;
			else
				atual.visitado = true;
			
			caminho.add(atual);
			
			if (atual.equals(destino))
				return caminho;
			
			//acessa o vértice
			System.out.println("ACESSANDO:");
			System.out.println((char)atual.valor);
			
			//para cada vizinho do vértice, mostra na tela
			System.out.println("VIZINHO NÃO ACESSADO:");
			for (int i = 0; i < _listaAdjacensia.get(atual).size(); i++)
			{
				vizinho = _listaAdjacensia.get(atual).get(i);
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);
				
				listaVertices.addLast(vizinho);
			}
		}
		
		return null;
	}
	
	private void LimpaVisitas()
	{
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			entrada.getKey().visitado = false;
		}
	}
}
