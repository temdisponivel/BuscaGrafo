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
		public int valor = 0;
		public boolean visitado = false;
		
		/**
		 * Cria um vértice com várias arestas.
		 */
		public Vertice(int valor)
		{
			this.valor = valor;
			
			if (verticesGrafo == null)
				verticesGrafo = new ArrayList<Vertice>();
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
		this.PercorreProfundidade();
		this.LimpaVisitas();
	}
	
	/**
	 * Percorre o grafo em profundidade recursivamente.
	 * @param atual
	 */
	private void PercorreProfundidade(Vertice atual)
	{
		//pega os vizinhos do vértice atual
		Iterator<Vertice> iterador = _listaAdjacensia.get(atual).iterator();
		
		//valida se já foi visto para poder acessar
		if (atual.visitado)
			return;
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
			
			this.PercorreProfundidade(vizinho);
		}
	}
	
	/**
	 * Percorre o grafo em profundidade iterativamente.
	 */
	private void PercorreProfundidade()
	{
		Vertice atual = _inicio;
		Stack<Vertice> pilha = new Stack<Vertice>();
		
		//adiciona na pilha
		pilha.push(atual);
		
		//enquanto a pilha nao estiver vazia
		while (!pilha.empty())
		{
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
		
		//seta todas as posições como não visitadas
		this.LimpaVisitas();
	}
	
	/**
	 * Percorre todo o grafo em largura.
	 */
	public void PercursoLargura()
	{
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
		
		this.LimpaVisitas();
	}
	
	private void LimpaVisitas()
	{
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			entrada.getKey().visitado = false;
		}
	}
	
	/**
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso não exista o caminho.
	 * @throws Exception Exception caso o destino informado não exista.
	 */
	public ArrayList<Vertice> BuscaLargura(int valorDestino) throws Exception
	{
		Vertice destino = null;
		
		if (!_listaAdjacensia.containsKey(destino = new Vertice(valorDestino)))
		{
			System.out.println("Não existe o destino informado");
			throw new Exception("Não existe o destino informado");	
		}
		
		return null;
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada até o resultado. Retorna nulo caso não exista um caminho.
	 * @param valorDestino Valor do vértice de destino da busca.
	 * @return Caminho do ponto de entrada até o destino. Ou nulo caso não exista o caminho.
	 * @throws Exception Exception caso o destino informado não exista.
	 */
	public ArrayList<Vertice> BuscaProfundidade(int valorDestino) throws Exception
	{
		Vertice destino = null;
		
		if (!_listaAdjacensia.containsKey(destino = new Vertice(valorDestino)))
		{
			System.out.println("Não existe o destino informado");
			throw new Exception("Não existe o destino informado");	
		}
		
		return null;
	}
}
