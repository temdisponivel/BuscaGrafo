package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * Classe que representa um grafo. Implementada utilizando lista de adjacensia.
 * @author matheus
 *
 */
public class Grafo
{	
	/**
	 * Classe que representa um v�rtice do grafo.
	 * @author matheus
	 *
	 */
	public static class Vertice
	{
		static public ArrayList<Vertice> verticesGrafo = null;
		public char valor = 0;
		public boolean visitado = false;
		
		/**
		 * Cria um v�rtice com v�rias arestas.
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
	 * Contr�i os v�rtices e arestas do grafo.
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
				
				//se j� houver o elemento no array, s� recupera, sen�o adiciona
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
	 * Define um novo inicio para o grafo. Ponto onde as buscas e percursos come�am. Caso o v�rtice seja inv�lido, as buscas e percurssos podem n�o funcionar ou n�o retornar resultado.
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
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada at� o resultado. Retorna nulo caso n�o exista um caminho.
	 * @param valorDestino Valor do v�rtice de destino da busca.
	 * @return Caminho do ponto de entrada at� o destino. Ou nulo caso n�o exista o caminho.
	 */
	public ArrayList<Vertice> BuscaLargura(char valorDestino)
	{
		ArrayList<Vertice> retorno = BuscaLargura(new Vertice(valorDestino));
		
		if (retorno == null)
			System.out.println("N�o existe caminho");
		
		return retorno;
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada at� o resultado. Retorna nulo caso n�o exista um caminho.
	 * @param valorDestino Valor do v�rtice de destino da busca.
	 * @return Caminho do ponto de entrada at� o destino. Ou nulo caso o caminho n�o exista o caminho.
	 */
	public ArrayList<Vertice> BuscaProfundidade(char valorDestino)
	{
		ArrayList<Vertice> retorno = PercorreProfundidade(_inicio, new Vertice(valorDestino));
		
		if (retorno == null)
			System.out.println("N�o existe caminho");
		
		return retorno;
		
	}
	
	/**
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada at� o resultado. Retorna nulo caso n�o exista um caminho.
	 * @param valorDestino Valor do v�rtice de destino da busca.
	 * @param atual V�rtice atual para recurs�o.
	 * @return Caminho do ponto de entrada at� o destino. Ou nulo caso o caminho n�o exista o caminho.
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
		
		//pega os vizinhos do v�rtice atual
		Iterator<Vertice> iterador = _listaAdjacensia.get(atual).iterator();
		
		//valida se j� foi visto para poder acessar
		if (atual.visitado)
			return null;
		else
			atual.visitado = true;
		
		System.out.println("ACESSANDO:");
		System.out.println((char)atual.valor);
		
		System.out.println("VIZINHO N�O ACESSADO:");
		
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
	 * Faz a busca em profundidade e retorna o caminho do grafo. Do ponto de entrada at� o resultado. Retorna nulo caso n�o exista um caminho.
	 * @param valorDestino Valor do v�rtice de destino da busca.
	 * @return Caminho do ponto de entrada at� o destino. Ou nulo caso o caminho n�o exista o caminho.
	 */
	private ArrayList<Vertice> PercorreProfundidade(Vertice destino)
	{
		
		Vertice atual = _inicio;
		Stack<Vertice> pilha = new Stack<Vertice>();
		ArrayList<Vertice> caminho = new ArrayList<Vertice>();;
		
		//adiciona na pilha
		pilha.push(atual);
		
		//enquanto a pilha nao estiver vazia
		while (!pilha.empty())
		{
			//desempinha
			ListIterator<Vertice> iterador = _listaAdjacensia.get((atual = pilha.pop())).listIterator(_listaAdjacensia.get((atual)).size());
			
			//valida se j� foi visitado para poder acessar
			if (atual.visitado)
				continue;
			else
				atual.visitado = true;
			
			caminho.add(atual);
			
			if (atual.equals(destino))
				return caminho;
			
			System.out.println("ACESSANDO:");
			System.out.println((char)atual.valor);
			
			System.out.println("VIZINHO N�O ACESSADO:");
			//para cada vizinho, adiciona na pilha
			while (iterador.hasPrevious())
			{
				Vertice vizinho = iterador.previous();
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);

				pilha.push(vizinho);
			}
		}
		
		return null;
	}
	
	/**
	 * Faz a busca em largura e retorna o caminho do grafo. Do ponto de entrada at� o resultado. Retorna nulo caso n�o exista um caminho.
	 * @param valorDestino Valor do v�rtice de destino da busca.
	 * @param atual V�rtice atual para recurs�o.
	 * @return Caminho do ponto de entrada at� o destino. Ou nulo caso o caminho n�o exista o caminho.
	 */
	private ArrayList<Vertice> BuscaLargura(Vertice destino)
	{
		HashMap<Vertice, Vertice> relacionamentos = new HashMap<Vertice, Vertice>();
		ArrayList<Vertice> caminho = new ArrayList<Vertice>();
		LinkedList<Vertice> listaVertices = new LinkedList<Vertice>();
		Vertice pai = null, vizinho = null, atual = _inicio;
		
		//adiciona na fila
		listaVertices.addLast(atual);
		
		//enquanto a fila n�o estiver vazia
		while (!listaVertices.isEmpty())
		{
			//desenfila
			atual = listaVertices.removeFirst();
			
			//valida se j� foi visitado para poder acessar
			if (atual.visitado)
				continue;
			else
				atual.visitado = true;
			
			//acessa o v�rtice
			System.out.println("ACESSANDO:");
			System.out.println((char)atual.valor);
			
			//para cada vizinho do v�rtice, mostra na tela
			System.out.println("VIZINHO N�O ACESSADO:");
			for (int i =  0; i < _listaAdjacensia.get(atual).size(); i++)
			{
				vizinho = _listaAdjacensia.get(atual).get(i);
				
				if (vizinho.visitado)
					continue;
				
				System.out.println((char)vizinho.valor);
				
				//enfila
				listaVertices.addLast(vizinho);
				
				//cria um relacionamento do vizinho com o v�rtice anterior, para podermos pegar o caminho de volta
				relacionamentos.put(vizinho, atual);
				
				//se estamos em um v�rtice que � vizinho do nosso destino
				if (vizinho.equals(destino))
				{
					//adiciona o destino no caminho
					caminho.add(vizinho);
					
					//pega o primeiro relacionamento
					pai = relacionamentos.get(vizinho);
					
					//enquanto nao chegamos no inicio, vai retornando atrav�s dos relacionamentos e construindo o caminho
					while (pai != _inicio)
					{
						caminho.add(pai);
						pai = relacionamentos.get(pai);
					}
					
					//adiciona o inicio no caminho
					caminho.add(_inicio);
					
					//faz o caminho contr�rio
					Collections.reverse(caminho);
					
					//retorna o caminho
					return caminho;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Adiciona um v�rtice no grafo.
	 * @param vizinho V�rtice j� existente, ao qual ser� adicionado o novo.
	 * @param novo Novo v�rtice a ser adicionado.
	 */
	public void Adicionar(Vertice vizinho, Vertice novo)
	{
		_listaAdjacensia.get(vizinho).add(novo);
		
		if (!_listaAdjacensia.containsKey(novo))
		{
			_listaAdjacensia.put(novo, new ArrayList<Vertice>());
		}
		
		_listaAdjacensia.get(novo).add(vizinho);
	}
	
	private void LimpaVisitas()
	{
		for (Entry<Vertice, ArrayList<Vertice>> entrada : _listaAdjacensia.entrySet())
		{
			entrada.getKey().visitado = false;
		}
	}
}
